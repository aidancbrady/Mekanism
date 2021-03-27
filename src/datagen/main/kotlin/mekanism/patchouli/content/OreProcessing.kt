package mekanism.patchouli.content

import mekanism.common.registries.MekanismBlocks.*
import mekanism.common.registries.MekanismGases.*
import mekanism.common.resource.OreType
import mekanism.patchouli.GuideCategory
import mekanism.patchouli.GuideEntry
import mekanism.patchouli.dsl.*

fun PatchouliBook.oreProcessing() {
    GuideCategory.ORE_PROCESSING {
        name = "Ore Processing"
        description = "Get more ingots from your ore with these machine combinations."
        icon = ORES[OreType.OSMIUM]!!

        GuideEntry.ORE_DOUBLING {
            name = "2x - Ore Doubling"
            icon = ENRICHMENT_CHAMBER
            +"To double your ores, simply use an ${link(ENRICHMENT_CHAMBER, "Enrichment Chamber")} to turn your ores into dusts, which can be smelted into ingots in a furnace or our patented ${link(ENERGIZED_SMELTER, "Energized Smelter")}"
        }
        GuideEntry.ORE_TRIPLING {
            name = "3x - Ore Tripling"
            icon = PURIFICATION_CHAMBER
            +"To triple ores, convert them into clumps with a ${link(PURIFICATION_CHAMBER, "Purification Chamber")} (requires oxygen), then convert those into dirty dusts with a ${link(CRUSHER, "Crusher")}"
            +"These dirty dusts can then be fed into your enrichment chamber"
        }
        GuideEntry.ORE_QUADRUPLING {
            name = "4x - Ore Quadrupling"
            icon = CHEMICAL_INJECTION_CHAMBER
            +"Quadrupling your ores is significantly more complex than doubling or tripling, due to the need for ${link(HYDROGEN_CHLORIDE, "Hydrogen Chloride")}."
            +"Once you have that set up, use a ${link(CHEMICAL_INJECTION_CHAMBER, "Chemical Injection Chamber")} to convert ores into shards, and feed those into your crusher."
        }
        GuideEntry.ORE_QUINTUPLING {
            name = "5x - Ore Quintupling"
            icon = CHEMICAL_DISSOLUTION_CHAMBER
            +"Quintupling ores is quite complicated, however it can lead to huge riches, especially with a ${link(DIGITAL_MINER, "Digital Miner")}."
            +"To quintuple ores, put them and ${link(SULFURIC_ACID, "Sulfuric Acid")} into a ${link(CHEMICAL_DISSOLUTION_CHAMBER, "Chemical Dissolution Chamber")}, which will convert them into slurry.  Clean the slurry in a ${link(CHEMICAL_WASHER, "Chemical Washer")}, then use a ${link(CHEMICAL_CRYSTALLIZER, "Chemical Crystallizer")} to turn them into crystals."
            +"Put the crystals into your injection chamber."
        }
    }
}