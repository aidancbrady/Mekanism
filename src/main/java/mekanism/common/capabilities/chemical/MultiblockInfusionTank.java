package mekanism.common.capabilities.chemical;

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.LongSupplier;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import mcp.MethodsReturnNonnullByDefault;
import mekanism.api.annotations.FieldsAreNonnullByDefault;
import mekanism.api.annotations.NonNull;
import mekanism.api.chemical.infuse.IMekanismInfusionHandler;
import mekanism.api.chemical.infuse.InfuseType;
import mekanism.api.inventory.AutomationType;
import mekanism.common.lib.multiblock.MultiblockData;
import mekanism.common.tile.prefab.TileEntityMultiblock;

@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MultiblockInfusionTank<MULTIBLOCK extends MultiblockData> extends VariableCapacityInfusionTank {

    public static <MULTIBLOCK extends MultiblockData> MultiblockInfusionTank<MULTIBLOCK> create(MULTIBLOCK multiblock, TileEntityMultiblock<MULTIBLOCK> tile,
          LongSupplier capacity, Predicate<@NonNull InfuseType> validator) {
        Objects.requireNonNull(tile, "Tile cannot be null");
        Objects.requireNonNull(capacity, "Capacity supplier cannot be null");
        Objects.requireNonNull(validator, "Infuse type validity check cannot be null");
        return new MultiblockInfusionTank<>(multiblock, tile, capacity, validator);
    }

    public static <MULTIBLOCK extends MultiblockData> MultiblockInfusionTank<MULTIBLOCK> create(MULTIBLOCK multiblock, TileEntityMultiblock<MULTIBLOCK> tile,
          LongSupplier capacity, BiPredicate<@NonNull InfuseType, @NonNull AutomationType> canExtract, BiPredicate<@NonNull InfuseType, @NonNull AutomationType> canInsert,
          Predicate<@NonNull InfuseType> validator) {
        return create(multiblock, tile, capacity, canExtract, canInsert, validator, null);
    }

    public static <MULTIBLOCK extends MultiblockData> MultiblockInfusionTank<MULTIBLOCK> input(MULTIBLOCK multiblock, TileEntityMultiblock<MULTIBLOCK> tile,
          LongSupplier capacity, Predicate<@NonNull InfuseType> validator) {
      return create(multiblock, tile, capacity, (stack, automationType) -> automationType != AutomationType.EXTERNAL && multiblock.isFormed(),
            (stack, automationType) -> multiblock.isFormed(), validator, null);
  }

  public static <MULTIBLOCK extends MultiblockData> MultiblockInfusionTank<MULTIBLOCK> output(MULTIBLOCK multiblock, TileEntityMultiblock<MULTIBLOCK> tile,
        LongSupplier capacity, Predicate<@NonNull InfuseType> validator) {
      return create(multiblock, tile, capacity, (stack, automationType) -> multiblock.isFormed(),
            (stack, automationType) -> automationType != AutomationType.EXTERNAL && multiblock.isFormed(), validator, null);
  }

    public static <MULTIBLOCK extends MultiblockData> MultiblockInfusionTank<MULTIBLOCK> create(MULTIBLOCK multiblock, TileEntityMultiblock<MULTIBLOCK> tile,
          LongSupplier capacity, BiPredicate<@NonNull InfuseType, @NonNull AutomationType> canExtract, BiPredicate<@NonNull InfuseType, @NonNull AutomationType> canInsert,
          Predicate<@NonNull InfuseType> validator, @Nullable IMekanismInfusionHandler infusionHandler) {
        Objects.requireNonNull(tile, "Tile cannot be null");
        Objects.requireNonNull(capacity, "Capacity supplier cannot be null");
        Objects.requireNonNull(validator, "Infuse type validity check cannot be null");
        Objects.requireNonNull(canExtract, "Extraction validity check cannot be null");
        Objects.requireNonNull(canInsert, "Insertion validity check cannot be null");
        return new MultiblockInfusionTank<>(multiblock, tile, capacity, canExtract, canInsert, validator, infusionHandler);
    }

    protected final MULTIBLOCK multiblock;
    protected final TileEntityMultiblock<MULTIBLOCK> tile;

    protected MultiblockInfusionTank(MULTIBLOCK multiblock, TileEntityMultiblock<MULTIBLOCK> tile, LongSupplier capacity, Predicate<@NonNull InfuseType> validator) {
        this(multiblock, tile, capacity, (stack, automationType) -> automationType != AutomationType.EXTERNAL || multiblock.isFormed(),
              (stack, automationType) -> automationType != AutomationType.EXTERNAL || multiblock.isFormed(), validator, null);
    }

    protected MultiblockInfusionTank(MULTIBLOCK multiblock, TileEntityMultiblock<MULTIBLOCK> tile, LongSupplier capacity,
          BiPredicate<@NonNull InfuseType, @NonNull AutomationType> canExtract, BiPredicate<@NonNull InfuseType, @NonNull AutomationType> canInsert,
          Predicate<@NonNull InfuseType> validator, @Nullable IMekanismInfusionHandler infusionHandler) {
        super(capacity, canExtract, canInsert, validator, infusionHandler);
        this.multiblock = multiblock;
        this.tile = tile;
    }

    @Override
    public void onContentsChanged() {
        super.onContentsChanged();
        if (tile.hasWorld() && !tile.isRemote()) {
            tile.markDirty(false);
            multiblock.markDirtyComparator(tile.getWorld());
        }
    }
}