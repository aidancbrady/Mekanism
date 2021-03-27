package mekanism.api.providers;

import java.util.Objects;
import javax.annotation.Nonnull;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public interface IBlockProvider extends IItemProvider {

    @Nonnull
    Block getBlock();

    default boolean blockMatches(ItemStack otherStack) {
        Item item = otherStack.getItem();
        return item instanceof BlockItem && blockMatches(((BlockItem) item).getBlock());
    }

    default boolean blockMatches(Block other) {
        return getBlock() == other;
    }

    @Nonnull
    @Override
    default ResourceLocation getRegistryName() {
        //Make sure to use the block's registry name in case it somehow doesn't match
        return Objects.requireNonNull(getBlock().getRegistryName(), "Unregistered Block");
    }

    @Nonnull
    @Override
    default String getTranslationKey() {
        return getBlock().getTranslationKey();
    }
}