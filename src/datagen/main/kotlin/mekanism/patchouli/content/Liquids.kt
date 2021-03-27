package mekanism.patchouli.content

import mekanism.common.registries.MekanismBlocks.*
import mekanism.common.registries.MekanismFluids
import mekanism.common.registries.MekanismItems.*
import mekanism.patchouli.GuideCategory
import mekanism.patchouli.GuideEntry
import mekanism.patchouli.dsl.*
import net.minecraft.item.ItemStack

fun PatchouliBook.liquids() {
    GuideCategory.LIQUIDS {
        name = "Liquids"
        description = "Splish splash, Robit's taking a bath. Warranty void when exposed to liquids."
        icon = ULTIMATE_FLUID_TANK

        GuideEntry.LIQUID_HEAVY_WATER {
            name = "Heavy Water"
            iconItem = ItemStack(MekanismFluids.HEAVY_WATER.bucket, 1)
            +"Heavy Water is obtaind by having an ${link(ELECTRIC_PUMP, "Electric Pump")} with a ${link(FILTER_UPGRADE, "Filter Upgrade")} pump a regular water source."
        }
    }
}