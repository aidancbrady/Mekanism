package mekanism.api.recipes.cache;

import java.util.Objects;
import javax.annotation.ParametersAreNonnullByDefault;
import mekanism.api.annotations.FieldsAreNonnullByDefault;
import mekanism.api.annotations.NonNull;
import mekanism.api.recipes.FluidToFluidRecipe;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.outputs.IOutputHandler;
import net.minecraftforge.fluids.FluidStack;

/**
 * Base class to help implement handling of fluid to fluid recipes.
 */
@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
public class FluidToFluidCachedRecipe extends CachedRecipe<FluidToFluidRecipe> {

    private final IOutputHandler<@NonNull FluidStack> outputHandler;
    private final IInputHandler<@NonNull FluidStack> inputHandler;

    private FluidStack recipeFluid = FluidStack.EMPTY;

    /**
     * @param recipe        Recipe.
     * @param inputHandler  Input handler.
     * @param outputHandler Output handler.
     */
    public FluidToFluidCachedRecipe(FluidToFluidRecipe recipe, IInputHandler<@NonNull FluidStack> inputHandler, IOutputHandler<@NonNull FluidStack> outputHandler) {
        super(recipe);
        this.inputHandler = Objects.requireNonNull(inputHandler, "Input handler cannot be null.");
        this.outputHandler = Objects.requireNonNull(outputHandler, "Output handler cannot be null.");
    }

    @Override
    protected int getOperationsThisTick(int currentMax) {
        currentMax = super.getOperationsThisTick(currentMax);
        if (currentMax <= 0) {
            //If our parent checks show we can't operate then return so
            return currentMax;
        }
        recipeFluid = inputHandler.getRecipeInput(recipe.getInput());
        //Test to make sure we can even perform a single operation. This is akin to !recipe.test(inputFluid)
        if (recipeFluid.isEmpty()) {
            return -1;
        }
        //Calculate the current max based on the fluid input
        currentMax = inputHandler.operationsCanSupport(recipeFluid, currentMax);
        if (currentMax <= 0) {
            //If our input can't handle it return that we should be resetting
            return -1;
        }
        //Calculate the max based on the space in the output
        return outputHandler.operationsRoomFor(recipe.getOutput(recipeFluid), currentMax);
    }

    @Override
    public boolean isInputValid() {
        return recipe.test(inputHandler.getInput());
    }

    @Override
    protected void finishProcessing(int operations) {
        if (recipeFluid.isEmpty()) {
            //Something went wrong, this if should never really be true if we got to finishProcessing
            return;
        }
        inputHandler.use(recipeFluid, operations);
        outputHandler.handleOutput(recipe.getOutput(recipeFluid), operations);
    }
}