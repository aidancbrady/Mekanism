package mekanism.common.item;

import java.util.List;

import mekanism.api.EnumColor;
import mekanism.api.MekanismConfig.general;
import mekanism.api.gas.Gas;
import mekanism.api.gas.GasRegistry;
import mekanism.api.gas.GasStack;
import mekanism.api.gas.IGasItem;
import mekanism.common.util.ItemDataUtils;
import mekanism.common.util.LangUtils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;

public class ItemFlamethrower extends ItemMekanism implements IGasItem
{
	public int TRANSFER_RATE = 16;
	
	public ItemFlamethrower()
	{
		super();
		setMaxStackSize(1);
	}
	
	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer entityplayer, List<String> list, boolean flag)
	{
		GasStack gasStack = getGas(itemstack);

		if(gasStack == null)
		{
			list.add(LangUtils.localize("tooltip.noGas") + ".");
		}
		else {
			list.add(LangUtils.localize("tooltip.stored") + " " + gasStack.getGas().getLocalizedName() + ": " + gasStack.amount);
		}

        list.add(EnumColor.GREY + LangUtils.localize("tooltip.mode") + ": " + EnumColor.GREY + getMode(itemstack).getName());
	}
	
	public void useGas(ItemStack stack)
	{
		setGas(stack, new GasStack(getGas(stack).getGas(), getGas(stack).amount - 1));
	}

	@Override
	public int getMaxGas(ItemStack itemstack)
	{
		return general.maxFlamethrowerGas;
	}

	@Override
	public int getRate(ItemStack itemstack)
	{
		return TRANSFER_RATE;
	}

	@Override
	public int addGas(ItemStack itemstack, GasStack stack)
	{
		if(getGas(itemstack) != null && getGas(itemstack).getGas() != stack.getGas())
		{
			return 0;
		}

		if(stack.getGas() != GasRegistry.getGas("hydrogen"))
		{
			return 0;
		}

		int toUse = Math.min(getMaxGas(itemstack)-getStored(itemstack), Math.min(getRate(itemstack), stack.amount));
		setGas(itemstack, new GasStack(stack.getGas(), getStored(itemstack)+toUse));

		return toUse;
	}

	@Override
	public GasStack removeGas(ItemStack itemstack, int amount)
	{
		return null;
	}

	public int getStored(ItemStack itemstack)
	{
		return getGas(itemstack) != null ? getGas(itemstack).amount : 0;
	}

	@Override
	public boolean canReceiveGas(ItemStack itemstack, Gas type)
	{
		return type == GasRegistry.getGas("hydrogen");
	}

	@Override
	public boolean canProvideGas(ItemStack itemstack, Gas type)
	{
		return false;
	}
	
	@Override
	public boolean showDurabilityBar(ItemStack stack)
	{
		return true;
	}
	
	@Override
	public double getDurabilityForDisplay(ItemStack stack)
	{
		return (getGas(stack) != null ? (double)getGas(stack).amount : 0D)/(double)getMaxGas(stack);
	}

	@Override
	public GasStack getGas(ItemStack itemstack)
	{
		return GasStack.readFromNBT(ItemDataUtils.getCompound(itemstack, "stored"));
	}

	@Override
	public void setGas(ItemStack itemstack, GasStack stack)
	{
		if(stack == null || stack.amount == 0)
		{
			ItemDataUtils.removeData(itemstack, "stored");
		}
		else {
			int amount = Math.max(0, Math.min(stack.amount, getMaxGas(itemstack)));
			GasStack gasStack = new GasStack(stack.getGas(), amount);

			ItemDataUtils.setCompound(itemstack, "stored", gasStack.write(new NBTTagCompound()));
		}
	}

	public ItemStack getEmptyItem()
	{
		ItemStack empty = new ItemStack(this);
		setGas(empty, null);
		return empty;
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tabs, NonNullList<ItemStack> list)
	{
		ItemStack empty = new ItemStack(this);
		setGas(empty, null);
		list.add(empty);

		ItemStack filled = new ItemStack(this);
		setGas(filled, new GasStack(GasRegistry.getGas("hydrogen"), ((IGasItem)filled.getItem()).getMaxGas(filled)));
		list.add(filled);
	}

    public void incrementMode(ItemStack stack)
    {
        setMode(stack, getMode(stack).increment());
    }

    public FlamethrowerMode getMode(ItemStack stack)
    {
        return FlamethrowerMode.values()[ItemDataUtils.getInt(stack, "mode")];
    }

    public void setMode(ItemStack stack, FlamethrowerMode mode)
    {
    	ItemDataUtils.setInt(stack, "mode", mode.ordinal());
    }

    public static enum FlamethrowerMode
    {
        COMBAT("tooltip.flamethrower.combat", EnumColor.YELLOW),
        HEAT("tooltip.flamethrower.heat", EnumColor.ORANGE),
        INFERNO("tooltip.flamethrower.inferno", EnumColor.DARK_RED);

        private String unlocalized;
        private EnumColor color;

        private FlamethrowerMode(String s, EnumColor c)
        {
            unlocalized = s;
            color = c;
        }

        public FlamethrowerMode increment()
        {
            return ordinal() < values().length-1 ? values()[ordinal()+1] : values()[0];
        }

        public String getName()
        {
            return color + LangUtils.localize(unlocalized);
        }
    }
}
