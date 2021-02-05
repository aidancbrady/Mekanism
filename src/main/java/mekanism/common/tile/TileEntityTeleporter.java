package mekanism.common.tile;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import mekanism.api.Action;
import mekanism.api.Coord4D;
import mekanism.api.NBTConstants;
import mekanism.api.inventory.AutomationType;
import mekanism.api.math.FloatingLong;
import mekanism.api.text.EnumColor;
import mekanism.common.Mekanism;
import mekanism.common.block.basic.BlockTeleporterFrame;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.config.MekanismConfig;
import mekanism.common.content.teleporter.TeleporterFrequency;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.SyncableByte;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.lib.chunkloading.IChunkLoader;
import mekanism.common.lib.frequency.FrequencyType;
import mekanism.common.network.PacketPortalFX;
import mekanism.common.registries.MekanismBlocks;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.tile.component.TileComponentChunkLoader;
import mekanism.common.util.EnumUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.NBTUtils;
import mekanism.common.util.WorldUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.PortalInfo;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SSetPassengersPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.server.ServerChunkProvider;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class TileEntityTeleporter extends TileEntityMekanism implements IChunkLoader {

    private static final ImmutableMap<Direction.Axis, ImmutableList<Direction>> DIRECTIONS_BY_AXIS = ImmutableMap.of(
            Direction.Axis.X, ImmutableList.of(Direction.EAST, Direction.WEST),
            Direction.Axis.Y, ImmutableList.of(Direction.UP, Direction.DOWN),
            Direction.Axis.Z, ImmutableList.of(Direction.SOUTH, Direction.NORTH)
    );
    private static final int MAX_TELEPORTER_SIZE = 10;

    public final Set<UUID> didTeleport = new ObjectOpenHashSet<>();
    private AxisAlignedBB teleportBounds;
    public int teleDelay = 0;
    public boolean shouldRender;
    @Nullable
    private Direction frameDirection;
    private int frameHeight;
    private EnumColor color;

    /**
     * This teleporter's current status.
     */
    public byte status = 0;

    private final TileComponentChunkLoader<TileEntityTeleporter> chunkLoaderComponent;

    private MachineEnergyContainer<TileEntityTeleporter> energyContainer;
    private EnergyInventorySlot energySlot;

    public TileEntityTeleporter() {
        super(MekanismBlocks.TELEPORTER);
        chunkLoaderComponent = new TileComponentChunkLoader<>(this);
        frequencyComponent.track(FrequencyType.TELEPORTER, true, true, false);
    }

    @Nonnull
    @Override
    protected IEnergyContainerHolder getInitialEnergyContainers() {
        EnergyContainerHelper builder = EnergyContainerHelper.forSide(this::getDirection);
        builder.addContainer(energyContainer = MachineEnergyContainer.input(this));
        return builder.build();
    }

    @Nonnull
    @Override
    protected IInventorySlotHolder getInitialInventory() {
        InventorySlotHelper builder = InventorySlotHelper.forSide(this::getDirection);
        builder.addSlot(energySlot = EnergyInventorySlot.fillOrConvert(energyContainer, this::getWorld, this, 153, 7));
        return builder.build();
    }

    public static void alignPlayer(ServerPlayerEntity player, BlockPos position) {
        Direction side = null;
        float yaw = player.rotationYaw;
        BlockPos upperPos = position.up();
        for (Direction iterSide : MekanismUtils.SIDE_DIRS) {
            if (player.world.isAirBlock(upperPos.offset(iterSide))) {
                side = iterSide;
                break;
            }
        }

        if (side != null) {
            switch (side) {
                case NORTH:
                    yaw = 180;
                    break;
                case SOUTH:
                    yaw = 0;
                    break;
                case WEST:
                    yaw = 90;
                    break;
                case EAST:
                    yaw = 270;
                    break;
                default:
                    break;
            }
        }
        player.connection.setPlayerLocation(player.getPosX(), player.getPosY(), player.getPosZ(), yaw, player.rotationPitch);
    }

    @Override
    protected void onUpdateServer() {
        super.onUpdateServer();
        status = canTeleport();
        if (MekanismUtils.canFunction(this) && status == 1 && teleDelay == 0) {
            teleport();
        }
        if (teleDelay == 0 && teleportBounds != null && !didTeleport.isEmpty()) {
            cleanTeleportCache();
        }

        boolean prevShouldRender = shouldRender;
        shouldRender = status == 1 || status > 4;
        EnumColor prevColor = color;
        TeleporterFrequency freq = getFrequency(FrequencyType.TELEPORTER);
        color = freq != null ? freq.getColor() : null;
        if (shouldRender != prevShouldRender) {
            //This also means the comparator output changed so notify the neighbors we have a change
            WorldUtils.notifyLoadedNeighborsOfTileChange(world, getPos());
            sendUpdatePacket();
        } else if (color != prevColor) {
            sendUpdatePacket();
        }
        teleDelay = Math.max(0, teleDelay - 1);
        energySlot.fillContainerOrConvert();
    }

    @Nullable
    private Coord4D getClosest() {
        TeleporterFrequency frequency = getFrequency(FrequencyType.TELEPORTER);
        return frequency == null ? null : frequency.getClosestCoords(Coord4D.get(this));
    }

    private void cleanTeleportCache() {
        List<UUID> list = new ArrayList<>();
        for (Entity e : world.getEntitiesWithinAABB(Entity.class, teleportBounds)) {
            list.add(e.getUniqueID());
        }
        Set<UUID> teleportCopy = new ObjectOpenHashSet<>(didTeleport);
        for (UUID id : teleportCopy) {
            if (!list.contains(id)) {
                didTeleport.remove(id);
            }
        }
    }

    /**
     * Checks whether, or why not, this teleporter can teleport entities.
     *
     * @return 1: yes, 2: no frame, 3: no link found, 4: not enough electricity
     */
    private byte canTeleport() {
        Direction direction = getFrameDirection();
        if (direction == null) {
            frameDirection = null;
            teleportBounds = null;
            return 2;
        } else if (frameDirection != direction) {
            frameDirection = direction;
        }
        Coord4D closestCoords = getClosest();
        if (closestCoords == null) {
            return 3;
        }
        FloatingLong sum = FloatingLong.ZERO;
        for (Entity entity : getToTeleport()) {
            sum = sum.plusEqual(calculateEnergyCost(entity, closestCoords));
        }
        if (energyContainer.extract(sum, Action.SIMULATE, AutomationType.INTERNAL).smallerThan(sum)) {
            return 4;
        }
        return 1;
    }

    /**
     * @apiNote Only call this from the server
     */
    private void teleport() {
        Coord4D closestCoords = getClosest();
        if (closestCoords == null) {
            return;
        }
        MinecraftServer currentServer = ServerLifecycleHooks.getCurrentServer();
        World teleWorld = currentServer.getWorld(closestCoords.dimension);
        BlockPos closestPos = closestCoords.getPos();
        TileEntityTeleporter teleporter = WorldUtils.getTileEntity(TileEntityTeleporter.class, teleWorld, closestPos);
        if (teleporter != null) {
            List<Entity> entitiesToTeleport = getToTeleport();
            if (!entitiesToTeleport.isEmpty()) {
                Set<Coord4D> activeCoords = getFrequency(FrequencyType.TELEPORTER).getActiveCoords();
                for (Entity entity : entitiesToTeleport) {
                    entity.getSelfAndPassengers().forEach(e -> teleporter.didTeleport.add(e.getUniqueID()));
                    teleporter.teleDelay = 5;
                    //Calculate energy cost before teleporting the entity, as after teleporting it
                    // the cost will be negligible due to being on top of the destination
                    FloatingLong energyCost = calculateEnergyCost(entity, closestCoords);
                    teleportEntityTo(entity, closestCoords, teleporter);
                    if (entity instanceof ServerPlayerEntity) {
                        alignPlayer((ServerPlayerEntity) entity, closestPos);
                    }
                    for (Coord4D coords : activeCoords) {
                        BlockPos coordsPos = coords.getPos();
                        TileEntityTeleporter tile = WorldUtils.getTileEntity(TileEntityTeleporter.class, world, coordsPos);
                        if (tile != null) {
                            if (tile.frameDirection != null) {
                                coordsPos = coordsPos.down().offset(tile.frameDirection);
                            }
                            Mekanism.packetHandler.sendToAllTracking(new PacketPortalFX(coordsPos), currentServer.getWorld(coords.dimension), coordsPos);
                        }
                    }
                    energyContainer.extract(energyCost, Action.EXECUTE, AutomationType.INTERNAL);
                    world.playSound(entity.getPosX(), entity.getPosY(), entity.getPosZ(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, entity.getSoundCategory(), 1.0F, 1.0F, false);
                }
            }
        }
    }

    public static void teleportEntityTo(Entity entity, Coord4D coord, TileEntityTeleporter teleporter) {
        BlockPos target = coord.getPos();
        if (teleporter.frameDirection == null) {
            target = target.up();
        } else if (teleporter.frameDirection == Direction.DOWN
                && teleporter.teleportBounds.maxY - teleporter.teleportBounds.minY > 3) {
            // If the teleporter block is at the top of the frame and there is room,
            // push the target down by one to avoid bumping the player's head.
            target = target.down(2);
        } else {
            target = target.offset(teleporter.frameDirection);
        }
        if (entity.getEntityWorld().getDimensionKey() == coord.dimension) {
            entity.setPositionAndUpdate(target.getX() + 0.5, target.getY(), target.getZ() + 0.5);
            if (!entity.getPassengers().isEmpty()) {
                //Force re-apply any passengers so that players don't get "stuck" outside what they may be riding
                ((ServerChunkProvider) entity.getEntityWorld().getChunkProvider()).sendToAllTracking(entity, new SSetPassengersPacket(entity));
            }
        } else {
            ServerWorld newWorld = ((ServerWorld) teleporter.getWorld()).getServer().getWorld(coord.dimension);
            if (newWorld != null) {
                Vector3d destination = new Vector3d(target.getX() + 0.5, target.getY(), target.getZ() + 0.5);
                //Note: We grab the passengers here instead of in placeEntity as changeDimension starts by removing any passengers
                List<Entity> passengers = entity.getPassengers();
                entity.changeDimension(newWorld, new ITeleporter() {
                    @Override
                    public Entity placeEntity(Entity entity, ServerWorld currentWorld, ServerWorld destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
                        Entity repositionedEntity = repositionEntity.apply(false);
                        if (repositionedEntity != null) {
                            //Teleport all passengers to the other dimension and then make them start riding the entity again
                            for (Entity passenger : passengers) {
                                teleportPassenger(destWorld, repositionedEntity, passenger);
                            }
                        }
                        return repositionedEntity;
                    }

                    @Override
                    public PortalInfo getPortalInfo(Entity entity, ServerWorld destWorld, Function<ServerWorld, PortalInfo> defaultPortalInfo) {
                        return new PortalInfo(destination, entity.getMotion(), entity.rotationYaw, entity.rotationPitch);
                    }
                });
            }
        }
    }

    private static void teleportPassenger(ServerWorld destWorld, Entity repositionedEntity, Entity passenger) {
        //Note: We grab the passengers here instead of in placeEntity as changeDimension starts by removing any passengers
        List<Entity> passengers = passenger.getPassengers();
        passenger.changeDimension(destWorld, new ITeleporter() {
            @Override
            public Entity placeEntity(Entity entity, ServerWorld currentWorld, ServerWorld destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
                Entity repositionedPassenger = repositionEntity.apply(false);
                if (repositionedPassenger != null) {
                    //Force our passenger to start riding the new entity again
                    repositionedPassenger.startRiding(repositionedEntity, true);
                    //Teleport "nested" passengers
                    for (Entity passenger : passengers) {
                        teleportPassenger(destWorld, repositionedPassenger, passenger);
                    }
                }
                return repositionedPassenger;
            }
        });
    }

    private List<Entity> getToTeleport() {
        //Don't get entities that are currently spectator, are a passenger, or recently teleported
        //Note: Passengers get handled separately
        return world == null || teleportBounds == null ? Collections.emptyList() : world.getEntitiesWithinAABB(Entity.class, teleportBounds,
              entity -> !entity.isSpectator() && !entity.isPassenger() && !didTeleport.contains(entity.getUniqueID()));
    }

    @Nonnull
    public static FloatingLong calculateEnergyCost(Entity entity, Coord4D coords) {
        FloatingLong energyCost = MekanismConfig.usage.teleporterBase.get();
        if (entity.world.getDimensionKey() == coords.dimension) {
            energyCost = energyCost.add(MekanismConfig.usage.teleporterDistance.get().multiply(Math.sqrt(entity.getDistanceSq(coords.getX(), coords.getY(), coords.getZ()))));
        } else {
            energyCost = energyCost.add(MekanismConfig.usage.teleporterDimensionPenalty.get());
        }
        //Factor the number of passengers of this entity into the teleportation energy cost
        int passengerCount = entity.getRecursivePassengers().size();
        return passengerCount > 0 ? energyCost.multiply(passengerCount) : energyCost;
    }

    /**
     * Checks in what direction there is a frame.
     *
     * @return in what direction there is a frame, null if none.
     */
    @Nullable
    private Direction getFrameDirection() {
        // Cache the chunks we are looking up to check the frames of
        Long2ObjectMap<IChunk> chunkMap = new Long2ObjectOpenHashMap<>();

        // Both neighbor blocks need to be frame on the axis of the base
        List<Direction.Axis> validAxes =
                EnumSet.allOf(Direction.Axis.class).stream()
                        .filter(axis ->
                                DIRECTIONS_BY_AXIS.get(axis).stream()
                                        .allMatch(direction -> isFrame(chunkMap, direction)))
                        .collect(Collectors.toList());

        List<Direction> possibleDirections = Arrays.stream(EnumUtils.DIRECTIONS)
                // The next block in the frame direction should be open.
                .filter(direction -> isPassable(chunkMap, direction))
                // At least one perpendicular axis must be frame blocks
                .filter(direction -> validAxes.stream().anyMatch(axis -> !axis.equals(direction.getAxis())))
                .collect(Collectors.toList());

        // Look up to 10 blocks away in each possible frame direction
        teleportBounds = null;
        frameHeight = 0;
        return possibleDirections.stream()
                .flatMap(
                        direction -> {
                            BlockPos position = pos.offset(direction);
                            for (int i = 2; i < MAX_TELEPORTER_SIZE; i++) {
                                position = position.offset(direction);
                                if (isFrame(chunkMap, position)) {
                                    // Look for a complete frame with this second frame block in the teleporter column.
                                    Optional<AxisAlignedBB> completeFrame =
                                            getCompleteFrame(chunkMap, i, direction, validAxes);
                                    if (completeFrame.isPresent()) {
                                        teleportBounds = completeFrame.get();
                                        frameHeight = i;
                                        return Stream.of(direction);
                                    }
                                }
                                if (!isPassable(chunkMap, position)) {
                                    break;
                                }
                            }
                            return Stream.empty();
                        })
                .findFirst().orElse(null);
    }

    /**
     * Checks whether this Teleporter has a Frame in the given Direction on any of the provided axes.
     *
     * @param chunkMap a cache of the blocks that have been checked
     * @param height the distance, in the direction, to the closest frame block to the teleporter
     * @param direction the direction from the Teleporter block in which the frame should be.
     * @param validAxes a list possible orientations of the frame
     *
     * @return a bounding box of the frame, if it exists
     */
    private Optional<AxisAlignedBB> getCompleteFrame(
            Long2ObjectMap<IChunk> chunkMap, Integer height, Direction direction, List<Direction.Axis> validAxes) {
        for (Direction.Axis axis : validAxes) {
            if (!direction.getAxis().equals(axis)) {
                Optional<AxisAlignedBB> frame = hasFrame(chunkMap, direction, height, axis);
                if (frame.isPresent()) {
                    return frame;
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Checks whether this Teleporter has a Frame in the given Direction on the given axis.
     *
     * @param chunkMap a cache of the blocks that have been checked
     * @param direction the direction from the Teleporter block in which the frame should be.
     * @param height the distance, in the direction, to the closest frame block to the teleporter
     * @param axis the possible orientation of the frame
     *
     * @return a bounding box of the frame, if it exists
     */
    private Optional<AxisAlignedBB> hasFrame(
            Long2ObjectMap<IChunk> chunkMap, Direction direction, int height, Direction.Axis axis) {
        Direction positive = DIRECTIONS_BY_AXIS.get(axis).get(0);
        Direction negative = DIRECTIONS_BY_AXIS.get(axis).get(1);

        Optional<Integer> firstPillar =
                findCompleteFramePillar(chunkMap, direction, positive, height,
                        MAX_TELEPORTER_SIZE - 2 /* Teleporter column and minimum for other pillar */);
        if (!firstPillar.isPresent()) {
            return Optional.empty();
        }

        Optional<Integer> secondPillar =
                findCompleteFramePillar(chunkMap, direction, negative, height,
                        MAX_TELEPORTER_SIZE - 1 /* Teleporter column */ - firstPillar.get());
        if (!secondPillar.isPresent()) {
            return Optional.empty();
        }

        BlockPos corner1 = pos.offset(positive, firstPillar.get());
        BlockPos corner2 = pos.offset(negative, secondPillar.get()).offset(direction, height);
        return Optional.of(new AxisAlignedBB(
                Math.min(corner1.getX(), corner2.getX()),
                Math.min(corner1.getY(), corner2.getY()),
                Math.min(corner1.getZ(), corner2.getZ()),
                Math.max(corner1.getX(), corner2.getX())+1,
                Math.max(corner1.getY(), corner2.getY())+1,
                Math.max(corner1.getZ(), corner2.getZ())+1));
    }

    private Optional<Integer> findCompleteFramePillar(
            Long2ObjectMap<IChunk> chunkMap, Direction columnDirection, Direction rowDirection, int height,
            int searchDistance) {
        BlockPos possiblePillarBase = pos;
        for (int i = 1; i <= searchDistance; i++) {
            possiblePillarBase = possiblePillarBase.offset(rowDirection);
            if (isCompleteFramePillar(chunkMap, possiblePillarBase, columnDirection, height)) {
                return Optional.of(i);
            }
            if (!isEmptyFrameColumnSection(chunkMap, possiblePillarBase, columnDirection, height)) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    private boolean isCompleteFramePillar(
            Long2ObjectMap<IChunk> chunkMap, BlockPos base, Direction direction, int height) {
        BlockPos position = base;
        for (int i = 0; i <=height; i++) {
            if (!isFrame(chunkMap, position)) {
                return false;
            }
            position = position.offset(direction);
        }
        return true;
    }

    private boolean isEmptyFrameColumnSection(
            Long2ObjectMap<IChunk> chunkMap, BlockPos base, Direction direction, int height) {
        BlockPos position = base;
        if (!isFrame(chunkMap, position)) {
            return false;
        }
        position = position.offset(direction);
        for (int i=1; i<=height-1; i++) {
            if (!isPassable(chunkMap, position)) {
                return false;
            }
            position = position.offset(direction);
        }
        return isFrame(chunkMap, position);
    }

    private boolean isFrame(Long2ObjectMap<IChunk> chunkMap, Direction direction) {
        return isFrame(chunkMap, pos.offset(direction));
    }

    private boolean isFrame(Long2ObjectMap<IChunk> chunkMap, BlockPos position) {
        Optional<BlockState> state = WorldUtils.getBlockState(world, chunkMap, position);
        return state.filter(blockState -> blockState.getBlock() instanceof BlockTeleporterFrame).isPresent();
    }

    private boolean isPassable(Long2ObjectMap<IChunk> chunkMap, Direction direction) {
        return isPassable(chunkMap, pos.offset(direction));
    }

    private boolean isPassable(Long2ObjectMap<IChunk> chunkMap, BlockPos position) {
        Optional<BlockState> state = WorldUtils.getBlockState(world, chunkMap, position);
        return state.filter(blockState -> {
            Material material = blockState.getMaterial();
            return !(material.blocksMovement() || material.isOpaque() || material.isSolid());
        }).isPresent();
    }

    /**
     * Gets the direction from the teleporter in which the frame is.
     *
     * @return the direction of the frame.
     */
    @Nullable
    public Direction frameDirection() {
        if (frameDirection == null) {
            return getFrameDirection();
        }
        return frameDirection;
    }

    @Nonnull
    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        //Note: If the frame direction is "null" we instead just only mark the teleporter itself.
        Direction frameDirection = getFrameDirection();
        return frameDirection == null ? new AxisAlignedBB(pos, pos.add(1, 1, 1)) : teleportBounds.shrink(0.46);
    }

    @Override
    public TileComponentChunkLoader<TileEntityTeleporter> getChunkLoader() {
        return chunkLoaderComponent;
    }

    @Override
    public Set<ChunkPos> getChunkSet() {
        return Collections.singleton(new ChunkPos(getPos()));
    }

    @Override
    public int getRedstoneLevel() {
        return shouldRender ? 15 : 0;
    }

    @Override
    public int getCurrentRedstoneLevel() {
        //We don't cache the redstone level for the teleporter
        return getRedstoneLevel();
    }

    public MachineEnergyContainer<TileEntityTeleporter> getEnergyContainer() {
        return energyContainer;
    }

    public EnumColor getColor() {
        return color;
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableByte.create(() -> status, value -> status = value));
    }

    @Nonnull
    @Override
    public CompoundNBT getReducedUpdateTag() {
        CompoundNBT updateTag = super.getReducedUpdateTag();
        updateTag.putBoolean(NBTConstants.RENDERING, shouldRender);
        if (color != null) {
            updateTag.putInt(NBTConstants.COLOR, color.ordinal());
        }
        return updateTag;
    }

    @Override
    public void handleUpdateTag(BlockState state, @Nonnull CompoundNBT tag) {
        super.handleUpdateTag(state, tag);
        NBTUtils.setBooleanIfPresent(tag, NBTConstants.RENDERING, value -> shouldRender = value);
        if (tag.contains(NBTConstants.COLOR, NBT.TAG_INT)) {
            color = EnumColor.byIndexStatic(tag.getInt(NBTConstants.COLOR));
        } else {
            color = null;
        }
    }
}