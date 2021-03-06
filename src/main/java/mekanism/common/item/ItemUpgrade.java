package mekanism.common.item;

import java.util.List;
import javax.annotation.Nonnull;
import mekanism.api.Upgrade;
import mekanism.api.text.EnumColor;
import mekanism.client.key.MekKeyHandler;
import mekanism.client.key.MekanismKeyHandler;
import mekanism.common.MekanismLang;
import mekanism.common.item.interfaces.IUpgradeItem;
import mekanism.common.tile.component.TileComponentUpgrade;
import mekanism.common.tile.interfaces.IUpgradeTile;
import mekanism.common.util.WorldUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Rarity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemUpgrade extends Item implements IUpgradeItem {

    private final Upgrade upgrade;

    public ItemUpgrade(Upgrade type, Properties properties) {
        super(properties.stacksTo(type.getMax()).rarity(Rarity.UNCOMMON));
        upgrade = type;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(@Nonnull ItemStack stack, World world, @Nonnull List<ITextComponent> tooltip, @Nonnull ITooltipFlag flag) {
        if (MekKeyHandler.getIsKeyPressed(MekanismKeyHandler.detailsKey)) {
            tooltip.add(getUpgradeType(stack).getDescription());
        } else {
            tooltip.add(MekanismLang.HOLD_FOR_DETAILS.translateColored(EnumColor.GRAY, EnumColor.INDIGO, MekanismKeyHandler.detailsKey.getTranslatedKeyMessage()));
        }
    }

    @Override
    public Upgrade getUpgradeType(ItemStack stack) {
        return upgrade;
    }

    @Nonnull
    @Override
    public ActionResultType useOn(ItemUseContext context) {
        PlayerEntity player = context.getPlayer();
        if (player != null && player.isShiftKeyDown()) {
            World world = context.getLevel();
            TileEntity tile = WorldUtils.getTileEntity(world, context.getClickedPos());
            if (tile instanceof IUpgradeTile) {
                IUpgradeTile upgradeTile = (IUpgradeTile) tile;
                if (upgradeTile.supportsUpgrades()) {
                    TileComponentUpgrade component = upgradeTile.getComponent();
                    ItemStack stack = context.getItemInHand();
                    Upgrade type = getUpgradeType(stack);
                    if (component.supports(type)) {
                        if (!world.isClientSide) {
                            int added = component.addUpgrades(type, stack.getCount());
                            if (added > 0) {
                                stack.shrink(added);
                            }
                        }
                        return ActionResultType.SUCCESS;
                    }
                }
            }
        }
        return ActionResultType.PASS;
    }
}