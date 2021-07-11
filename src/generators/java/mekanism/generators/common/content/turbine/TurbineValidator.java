package mekanism.generators.common.content.turbine;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.Set;
import mekanism.common.content.blocktype.BlockType;
import mekanism.common.lib.math.voxel.VoxelCuboid;
import mekanism.common.lib.multiblock.CuboidStructureValidator;
import mekanism.common.lib.multiblock.FormationProtocol;
import mekanism.common.lib.multiblock.FormationProtocol.CasingType;
import mekanism.common.lib.multiblock.FormationProtocol.FormationResult;
import mekanism.common.registries.MekanismBlockTypes;
import mekanism.common.tile.TileEntityPressureDisperser;
import mekanism.common.util.WorldUtils;
import mekanism.generators.common.GeneratorsLang;
import mekanism.generators.common.registries.GeneratorsBlockTypes;
import mekanism.generators.common.tile.turbine.TileEntityElectromagneticCoil;
import mekanism.generators.common.tile.turbine.TileEntityRotationalComplex;
import mekanism.generators.common.tile.turbine.TileEntitySaturatingCondenser;
import mekanism.generators.common.tile.turbine.TileEntityTurbineRotor;
import mekanism.generators.common.tile.turbine.TileEntityTurbineVent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.IChunk;

public class TurbineValidator extends CuboidStructureValidator<TurbineMultiblockData> {

    public static final int MAX_BLADES = 28;

    public TurbineValidator() {
        super(new VoxelCuboid(3, 3, 3), new VoxelCuboid(17, 18, 17));
    }

    @Override
    protected CasingType getCasingType(BlockState state) {
        Block block = state.getBlock();
        if (BlockType.is(block, GeneratorsBlockTypes.TURBINE_CASING)) {
            return CasingType.FRAME;
        } else if (BlockType.is(block, GeneratorsBlockTypes.TURBINE_VALVE)) {
            return CasingType.VALVE;
        } else if (BlockType.is(block, GeneratorsBlockTypes.TURBINE_VENT)) {
            return CasingType.OTHER;
        }
        return CasingType.INVALID;
    }

    @Override
    protected boolean validateInner(BlockState state, Long2ObjectMap<IChunk> chunkMap, BlockPos pos) {
        if (super.validateInner(state, chunkMap, pos)) {
            return true;
        }
        return BlockType.is(state.getBlock(), MekanismBlockTypes.PRESSURE_DISPERSER, GeneratorsBlockTypes.TURBINE_ROTOR,
              GeneratorsBlockTypes.ROTATIONAL_COMPLEX, GeneratorsBlockTypes.ELECTROMAGNETIC_COIL, GeneratorsBlockTypes.SATURATING_CONDENSER);
    }

    @Override
    public FormationResult postcheck(TurbineMultiblockData structure, Set<BlockPos> innerNodes, Long2ObjectMap<IChunk> chunkMap) {
        if (structure.length() % 2 != 1 || structure.width() % 2 != 1) {
            return FormationResult.fail(GeneratorsLang.TURBINE_INVALID_EVEN_LENGTH);
        }
        int centerX = structure.getMinPos().getX() + (structure.length() - 1) / 2;
        int centerZ = structure.getMinPos().getZ() + (structure.width() - 1) / 2;

        BlockPos complex = null;

        Set<BlockPos> turbines = new ObjectOpenHashSet<>();
        Set<BlockPos> dispersers = new ObjectOpenHashSet<>();
        Set<BlockPos> coils = new ObjectOpenHashSet<>();
        Set<BlockPos> condensers = new ObjectOpenHashSet<>();

        //Scan for complex
        for (BlockPos pos : innerNodes) {
            TileEntity tile = WorldUtils.getTileEntity(world, chunkMap, pos);
            if (tile instanceof TileEntityRotationalComplex) {
                if (complex != null || pos.getX() != centerX || pos.getZ() != centerZ) {
                    return FormationResult.fail(GeneratorsLang.TURBINE_INVALID_BAD_COMPLEX, pos);
                }
                structure.internalLocations.add(pos);
                complex = pos;
            } else if (tile instanceof TileEntityTurbineRotor) {
                if (pos.getX() != centerX || pos.getZ() != centerZ) {
                    return FormationResult.fail(GeneratorsLang.TURBINE_INVALID_BAD_ROTOR, pos);
                }
                turbines.add(pos);
            } else if (tile instanceof TileEntityPressureDisperser) {
                dispersers.add(pos);
            } else if (tile instanceof TileEntityElectromagneticCoil) {
                coils.add(pos);
            } else if (tile instanceof TileEntitySaturatingCondenser) {
                condensers.add(pos);
            }
        }

        //Terminate if complex doesn't exist
        if (complex == null) {
            return FormationResult.fail(GeneratorsLang.TURBINE_INVALID_MISSING_COMPLEX);
        }

        int rotors = complex.getY() - structure.getMinPos().getY() + 1;
        int innerRadius = (Math.min(structure.length(), structure.width()) - 3) / 2;
        if (innerRadius < rotors / 4) {
            return FormationResult.fail(GeneratorsLang.TURBINE_INVALID_TOO_NARROW);
        }

        BlockPos.Mutable mutablePos = new BlockPos.Mutable();
        //Make sure a flat, horizontal plane of dispersers exists within the multiblock around the complex
        for (int x = complex.getX() - innerRadius; x <= complex.getX() + innerRadius; x++) {
            for (int z = complex.getZ() - innerRadius; z <= complex.getZ() + innerRadius; z++) {
                if (x != centerX || z != centerZ) {
                    mutablePos.set(x, complex.getY(), z);
                    TileEntityPressureDisperser tile = WorldUtils.getTileEntity(TileEntityPressureDisperser.class, world, chunkMap, mutablePos);
                    if (tile == null) {
                        return FormationResult.fail(GeneratorsLang.TURBINE_INVALID_MISSING_DISPERSER, mutablePos);
                    }
                    dispersers.remove(mutablePos);
                }
            }
        }

        //If any dispersers were not processed, they're in the wrong place
        if (!dispersers.isEmpty()) {
            return FormationResult.fail(GeneratorsLang.TURBINE_INVALID_MALFORMED_DISPERSERS);
        }

        //Make sure all condensers are in proper locations
        for (BlockPos coord : condensers) {
            if (coord.getY() <= complex.getY()) {
                return FormationResult.fail(GeneratorsLang.TURBINE_INVALID_CONDENSER_BELOW_COMPLEX, coord);
            }
        }

        structure.condensers = condensers.size();
        int turbineHeight = 0;
        int blades = 0;

        // Starting from the complex, walk down and count the number of rotors/blades in the structure
        for (int y = complex.getY() - 1; y > structure.getMinPos().getY(); y--) {
            mutablePos.set(centerX, y, centerZ);
            TileEntityTurbineRotor rotor = WorldUtils.getTileEntity(TileEntityTurbineRotor.class, world, chunkMap, mutablePos);
            if (rotor == null) {
                // Not a contiguous set of rotors
                return FormationResult.fail(GeneratorsLang.TURBINE_INVALID_ROTORS_NOT_CONTIGUOUS);
            }
            turbineHeight++;
            blades += rotor.getHousedBlades();
            structure.internalLocations.add(rotor.getBlockPos());
            turbines.remove(mutablePos);
        }

        // If there are any rotors left over, they are in the wrong place in the structure
        if (!turbines.isEmpty()) {
            return FormationResult.fail(GeneratorsLang.TURBINE_INVALID_BAD_ROTORS);
        }

        // Update the structure with number of blades found on rotors
        structure.blades = blades;

        BlockPos startCoord = complex.relative(Direction.UP);
        if (WorldUtils.getTileEntity(TileEntityElectromagneticCoil.class, world, chunkMap, startCoord) != null) {
            structure.coils = FormationProtocol.explore(startCoord, coord -> WorldUtils.getTileEntity(TileEntityElectromagneticCoil.class, world, chunkMap, coord) != null);
        }

        //Terminate if coils don't exist
        if (coils.isEmpty()) {
            return FormationResult.fail(GeneratorsLang.TURBINE_INVALID_MISSING_COILS);
        }

        if (coils.size() > structure.coils) {
            return FormationResult.fail(GeneratorsLang.TURBINE_INVALID_MALFORMED_COILS);
        }

        for (BlockPos coord : structure.locations) {
            if (WorldUtils.getTileEntity(TileEntityTurbineVent.class, world, chunkMap, coord) != null) {
                if (coord.getY() < complex.getY()) {
                    return FormationResult.fail(GeneratorsLang.TURBINE_INVALID_VENT_BELOW_COMPLEX, coord);
                }
                structure.vents++;
            }
        }
        structure.lowerVolume = structure.length() * structure.width() * turbineHeight;
        structure.complex = complex;
        return FormationResult.SUCCESS;
    }
}