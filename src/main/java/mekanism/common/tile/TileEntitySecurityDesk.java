package mekanism.common.tile;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import javax.annotation.Nonnull;
import mekanism.api.NBTConstants;
import mekanism.common.Mekanism;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.container.ISecurityContainer;
import mekanism.common.inventory.slot.SecurityInventorySlot;
import mekanism.common.lib.frequency.FrequencyType;
import mekanism.common.lib.security.SecurityData;
import mekanism.common.lib.security.SecurityFrequency;
import mekanism.common.lib.security.SecurityMode;
import mekanism.common.network.to_client.PacketSecurityUpdate;
import mekanism.common.registries.MekanismBlocks;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.tile.interfaces.IBoundingBlock;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.NBTUtils;
import mekanism.common.util.SecurityUtils;
import mekanism.common.util.WorldUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.minecraftforge.items.CapabilityItemHandler;

public class TileEntitySecurityDesk extends TileEntityMekanism implements IBoundingBlock {

    public UUID ownerUUID;
    public String clientOwner;

    private SecurityInventorySlot unlockSlot;
    private SecurityInventorySlot lockSlot;

    public TileEntitySecurityDesk() {
        super(MekanismBlocks.SECURITY_DESK);
        //Even though there are inventory slots make this return none as accessible by automation, as then people could lock items to other
        // people unintentionally
        addDisabledCapabilities(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
    }

    @Nonnull
    @Override
    protected IInventorySlotHolder getInitialInventory() {
        InventorySlotHelper builder = InventorySlotHelper.forSide(this::getDirection);
        builder.addSlot(unlockSlot = SecurityInventorySlot.unlock(() -> ownerUUID, this, 146, 18));
        builder.addSlot(lockSlot = SecurityInventorySlot.lock(this, 146, 97));
        return builder.build();
    }

    @Override
    protected void onUpdateServer() {
        super.onUpdateServer();
        SecurityFrequency frequency = getFreq();
        if (ownerUUID != null && frequency != null) {
            unlockSlot.unlock(ownerUUID);
            lockSlot.lock(ownerUUID, frequency);
        }
    }

    /**
     * Only call on the server side
     */
    public void toggleOverride() {
        SecurityFrequency frequency = getFreq();
        if (frequency != null) {
            frequency.setOverridden(!frequency.isOverridden());
            markDirty(false);
            // send the security update to other players; this change will be visible on machine security tabs
            Mekanism.packetHandler.sendToAll(new PacketSecurityUpdate(frequency.getOwner(), new SecurityData(frequency)));
            validateAccess();
        }
    }

    /**
     * Validates access for anyone who might be accessing a GUI that changed security modes
     */
    private void validateAccess() {
        if (hasLevel()) {
            MinecraftServer server = getWorldNN().getServer();
            if (server != null) {
                for (ServerPlayerEntity player : server.getPlayerList().getPlayers()) {
                    if (player.containerMenu instanceof ISecurityContainer) {
                        if (!SecurityUtils.canAccess(player, ((ISecurityContainer) player.containerMenu).getSecurityObject())) {
                            //Boot any players out of the container if they no longer have access to viewing it
                            player.closeContainer();
                        }
                    }
                }
            }
        }
    }

    public void removeTrusted(int index) {
        SecurityFrequency frequency = getFreq();
        if (frequency != null) {
            frequency.removeTrusted(index);
            markDirty(false);
        }
    }

    public void setSecurityDeskMode(SecurityMode mode) {
        SecurityFrequency frequency = getFreq();
        if (frequency != null) {
            SecurityMode old = frequency.getSecurityMode();
            if (old != mode) {
                frequency.setSecurityMode(mode);
                markDirty(false);
                // send the security update to other players; this change will be visible on machine security tabs
                Mekanism.packetHandler.sendToAll(new PacketSecurityUpdate(frequency.getOwner(), new SecurityData(frequency)));
                if (old == SecurityMode.PUBLIC || (old == SecurityMode.TRUSTED && mode == SecurityMode.PRIVATE)) {
                    validateAccess();
                }
            }
        }
    }

    public void addTrusted(String name) {
        SecurityFrequency frequency = getFreq();
        if (frequency != null) {
            GameProfile profile = ServerLifecycleHooks.getCurrentServer().getProfileCache().get(name);
            if (profile != null) {
                frequency.addTrusted(profile.getId(), profile.getName());
                markDirty(false);
            }
        }
    }

    @Override
    public void load(@Nonnull BlockState state, @Nonnull CompoundNBT nbtTags) {
        super.load(state, nbtTags);
        NBTUtils.setUUIDIfPresent(nbtTags, NBTConstants.OWNER_UUID, uuid -> ownerUUID = uuid);
    }

    @Nonnull
    @Override
    public CompoundNBT save(@Nonnull CompoundNBT nbtTags) {
        super.save(nbtTags);
        if (ownerUUID != null) {
            nbtTags.putUUID(NBTConstants.OWNER_UUID, ownerUUID);
        }
        return nbtTags;
    }

    @Override
    public void onPlace() {
        super.onPlace();
        WorldUtils.makeBoundingBlock(getLevel(), getBlockPos().above(), getBlockPos());
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        if (level != null) {
            level.removeBlock(getBlockPos().above(), false);
        }
    }

    @Nonnull
    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(worldPosition, worldPosition.offset(1, 2, 1));
    }

    @Nonnull
    @Override
    public CompoundNBT getReducedUpdateTag() {
        CompoundNBT updateTag = super.getReducedUpdateTag();
        if (ownerUUID != null) {
            updateTag.putUUID(NBTConstants.OWNER_UUID, ownerUUID);
            updateTag.putString(NBTConstants.OWNER_NAME, MekanismUtils.getLastKnownUsername(ownerUUID));
        }
        return updateTag;
    }

    @Override
    public void handleUpdateTag(BlockState state, @Nonnull CompoundNBT tag) {
        super.handleUpdateTag(state, tag);
        NBTUtils.setUUIDIfPresent(tag, NBTConstants.OWNER_UUID, uuid -> ownerUUID = uuid);
        NBTUtils.setStringIfPresent(tag, NBTConstants.OWNER_NAME, uuid -> clientOwner = uuid);
    }

    public SecurityFrequency getFreq() {
        return getFrequency(FrequencyType.SECURITY);
    }

    @Override
    public boolean isOffsetCapabilityDisabled(@Nonnull Capability<?> capability, Direction side, @Nonnull Vector3i offset) {
        //Don't allow proxying any capabilities by marking them all as disabled
        return true;
    }
}