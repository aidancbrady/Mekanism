package mekanism.api.recipes.cache;

import java.util.function.BiFunction;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import mekanism.api.annotations.NonNull;
import mekanism.api.gas.GasStack;
import mekanism.api.gas.GasTank;
import mekanism.api.recipes.GasToGasRecipe;
import mekanism.common.util.FieldsAreNonnullByDefault;

@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
public class GasToGasCachedRecipe extends CachedRecipe<GasToGasRecipe> {

    private final BiFunction<@NonNull GasStack, Boolean, Boolean> addToOutput;
    private final Supplier<@NonNull GasTank> inputTank;

    public GasToGasCachedRecipe(GasToGasRecipe recipe, BooleanSupplier canTileFunction, DoubleSupplier perTickEnergy, DoubleSupplier storedEnergy,
          IntSupplier requiredTicks, Consumer<Boolean> setActive, DoubleConsumer useEnergy, Runnable onFinish, Supplier<@NonNull GasTank> inputTank,
          BiFunction<@NonNull GasStack, Boolean, Boolean> addToOutput) {
        super(recipe, canTileFunction, perTickEnergy, storedEnergy, requiredTicks, setActive, useEnergy, onFinish);
        this.inputTank = inputTank;
        this.addToOutput = addToOutput;
    }

    @Nonnull
    private GasTank getGasTank() {
        return inputTank.get();
    }

    @Override
    public boolean hasResourcesForTick() {
        GasStack gasInput = getGasTank().getGas();
        return gasInput != null && recipe.test(gasInput);
    }

    @Override
    public boolean hasRoomForOutput() {
        return addToOutput.apply(recipe.getOutput(getGasTank().getGas()), true);
    }

    @Override
    protected void finishProcessing() {
        addToOutput.apply(recipe.getOutput(getGasTank().getGas()), false);
    }
}