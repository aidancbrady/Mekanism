package mekanism.common.item.gear;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import mcp.MethodsReturnNonnullByDefault;
import mekanism.client.render.armor.CustomArmor;
import mekanism.client.render.armor.JetpackArmor;
import mekanism.client.render.item.ISTERProvider;
import mekanism.common.Mekanism;
import mekanism.common.config.MekanismConfig;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemArmoredJetpack extends ItemJetpack {

    private static final ArmoredJetpackMaterial ARMORED_JETPACK_MATERIAL = new ArmoredJetpackMaterial();

    public ItemArmoredJetpack(Properties properties) {
        super(ARMORED_JETPACK_MATERIAL, properties.setISTER(ISTERProvider::armoredJetpack));
    }

    @Nonnull
    @Override
    @OnlyIn(Dist.CLIENT)
    public CustomArmor getGearModel() {
        return JetpackArmor.ARMORED_JETPACK;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return true;
    }

    @ParametersAreNonnullByDefault
    @MethodsReturnNonnullByDefault
    private static class ArmoredJetpackMaterial extends JetpackMaterial {

        @Override
        public int getDamageReductionAmount(EquipmentSlotType slotType) {
            return slotType == EquipmentSlotType.CHEST ? MekanismConfig.gear.armoredJetpackArmor.get() : 0;
        }

        @Override
        public String getName() {
            return Mekanism.MODID + ":jetpack_armored";
        }

        @Override
        public float getToughness() {
            return MekanismConfig.gear.armoredJetpackToughness.get();
        }
    }
}