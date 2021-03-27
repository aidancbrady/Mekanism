package mekanism.api.providers;

import java.util.Objects;
import javax.annotation.Nonnull;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public interface IItemProvider extends IBaseProvider, net.minecraft.util.IItemProvider {

    @Nonnull
    Item getItem();

    @Nonnull
    @Override
    default Item asItem() {
        return getItem();
    }

    @Nonnull
    default ItemStack getItemStack() {
        return getItemStack(1);
    }

    @Nonnull
    default ItemStack getItemStack(int size) {
        return new ItemStack(getItem(), size);
    }

    default boolean itemMatches(ItemStack otherStack) {
        return itemMatches(otherStack.getItem());
    }

    default boolean itemMatches(Item other) {
        return getItem() == other;
    }

    @Nonnull
    @Override
    default ResourceLocation getRegistryName() {
        return Objects.requireNonNull(getItem().getRegistryName(), "Unregistered Item");
    }

    @Nonnull
    @Override
    default String getTranslationKey() {
        return getItem().getTranslationKey();
    }
}