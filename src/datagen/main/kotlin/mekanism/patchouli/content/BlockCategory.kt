package mekanism.patchouli.content

import mekanism.common.MekanismLang
import mekanism.patchouli.dsl.*
import mekanism.common.registries.MekanismBlocks.*
import mekanism.patchouli.GuideCategory
import mekanism.patchouli.GuideEntry
import mekanism.patchouli.MekanismMainBookProvider

fun PatchouliBook.blockCategory() {
    GuideCategory.BLOCKS {
        name = "Blocks List"
        description = "A list of the blocks in Mekanism."
        icon = ULTIMATE_ENERGY_CUBE
        sortNum = MekanismMainBookProvider.FORCED_BLOCK_SORT_NUM

        GuideEntry.PIPES_LOGISTICAL {
            name = "Logistical Transporters"
            icon = BASIC_LOGISTICAL_TRANSPORTER
            +"The Logistical Transporter is the basic item transport pipe for Mekanism logistics.$(br)With the Configurator the player can choose to \"paint\" the pipe with colors can can be detected by the pipe's color sorter by $(k:sneak) + right-clicking the center of a transporter with a Configurator.$(br2)It has 2 other cousins called the Diversion Transporter and the Restrictive Transporter."
            spotlight(BASIC_LOGISTICAL_TRANSPORTER)
            spotlight(ADVANCED_LOGISTICAL_TRANSPORTER)
            spotlight(ELITE_LOGISTICAL_TRANSPORTER)
            spotlight(ULTIMATE_LOGISTICAL_TRANSPORTER)
        }

        GuideEntry.PIPES_MECHANICAL {
            name = "Mechanical Pipes"
            icon = BASIC_MECHANICAL_PIPE
            +"Mechanical Pipe is the fluid pipe for Mekanism logistics. It can be used to connect to any blocks with Fluid Handlers in them."
            spotlight(BASIC_MECHANICAL_PIPE)
            spotlight(ADVANCED_MECHANICAL_PIPE)
            spotlight(ELITE_MECHANICAL_PIPE)
            spotlight(ULTIMATE_MECHANICAL_PIPE)
        }

        GuideEntry.PIPES_GAS {
            name = "Pressurized Tubes"
            icon = BASIC_PRESSURIZED_TUBE
            +"Pressurized Tubes are used to transport Gases. Similar to their mechanical counterparts, they can be upgraded to higher tiers to increase flow rate and capacity of pumped gases."
            spotlight(BASIC_PRESSURIZED_TUBE)
            spotlight(ADVANCED_PRESSURIZED_TUBE)
            spotlight(ELITE_PRESSURIZED_TUBE)
            spotlight(ULTIMATE_PRESSURIZED_TUBE)
        }

        GuideEntry.PIPES_HEAT {
            name = "Thermodynamic Conductors"
            icon = BASIC_THERMODYNAMIC_CONDUCTOR
            +"Similar to the Universal Cable, the Thermodynamic Conductor is Mekanism's way of transferring power in the form of heat (essentially a heat pipe).$(2br)Transfer is lossy, depending on the biome the conductor is in. Warmer biomes have a higher transfer efficiency (less heat is lost), while colder biomes are lower (more heat is lost)."
            spotlight(BASIC_THERMODYNAMIC_CONDUCTOR)
            spotlight(ADVANCED_THERMODYNAMIC_CONDUCTOR)
            spotlight(ELITE_THERMODYNAMIC_CONDUCTOR)
            spotlight(ULTIMATE_THERMODYNAMIC_CONDUCTOR)
        }

        GuideEntry.PIPES_POWER {
            name = "Universal Cables"
            icon = BASIC_UNIVERSAL_CABLE
            +"Universal Cables are Mekanism's way to transfer power. They are capable of transferring Mekanism's power Joules (J), as well as a variety of other power types such as Forge Energy (FE), Thermal Expansion's Redstone Flux (RF), Buildcraft's Minecraftjoules (MJ - display only), and Industrialcraft Energy Unit (EU). This flexibility allows players to mix power generation from different mods while still only using one type of cabling."
            spotlight(BASIC_UNIVERSAL_CABLE)
            spotlight(ADVANCED_UNIVERSAL_CABLE)
            spotlight(ELITE_UNIVERSAL_CABLE)
            spotlight(ULTIMATE_UNIVERSAL_CABLE)
        }

        GuideEntry.TANKS_LIQUID {
            name = "Fluid Tanks"
            icon = BASIC_FLUID_TANK
            +"Tanks which store fluids. They can be placed as a block or used in Bucket mode ($(k:sneak) + $(k:${MekanismLang.KEY_HAND_MODE.translationKey}) to toggle)"
            spotlight(BASIC_FLUID_TANK)
            spotlight(ADVANCED_FLUID_TANK)
            spotlight(ELITE_FLUID_TANK)
            spotlight(ULTIMATE_FLUID_TANK)
            spotlight(CREATIVE_FLUID_TANK)
        }

        GuideEntry.TANKS_GAS {
            name = "Chemical Tanks"
            icon = BASIC_CHEMICAL_TANK
            +"Chemical Tanks are Mekanism's batteries for storing Gases and other chemicals. They can be placed as a block and interact with Pressurized Tubes. They come in four tiers, each increasing the storage capacity and output rate."
            spotlight(BASIC_CHEMICAL_TANK)
            spotlight(ADVANCED_CHEMICAL_TANK)
            spotlight(ELITE_CHEMICAL_TANK)
            spotlight(ULTIMATE_CHEMICAL_TANK)
            spotlight(CREATIVE_CHEMICAL_TANK)
        }

        GuideEntry.ENERGY_CUBES {
            name = "Energy Cubes"
            icon = BASIC_ENERGY_CUBE
            +"An Energy Cube is an advanced type of battery that is compatible with multiple energy systems. The Input/Output mode of the side can be configured in the GUI"
            spotlight(BASIC_ENERGY_CUBE)
            spotlight(ADVANCED_ENERGY_CUBE)
            spotlight(ELITE_ENERGY_CUBE)
            spotlight(ULTIMATE_ENERGY_CUBE)
            spotlight(CREATIVE_ENERGY_CUBE)
        }

        GuideEntry.INDUCTION_CELL {
            name = "Induction Cells"
            icon = BASIC_INDUCTION_CELL
            +"Induction Cells are components in the ${link(GuideEntry.INDUCTION, "Induction Matrix")}. Each cell increases the total energy storage of a Matrix. Note that this does not increase transfer rate; look to the ${link(GuideEntry.INDUCTION_PROVIDER, "Induction Providers")} for that."
            spotlight(BASIC_INDUCTION_CELL, "Adds 8 GJ to the Matrix's capacity")
            spotlight(ADVANCED_INDUCTION_CELL, "Adds 64 GJ to the Matrix's capacity.")
            spotlight(ELITE_INDUCTION_CELL, "Adds 512 GJ to the Matrix's capacity")
            spotlight(ULTIMATE_INDUCTION_CELL, "Adds 4 TJ to the Matrix's capacity")
            crafting(BASIC_INDUCTION_CELL) {
                secondaryRecipe = ADVANCED_INDUCTION_CELL
            }
            crafting(ELITE_INDUCTION_CELL) {
                secondaryRecipe = ULTIMATE_INDUCTION_CELL
            }

        }

        GuideEntry.INDUCTION_PROVIDER {
            name = "Induction Providers"
            icon = BASIC_INDUCTION_PROVIDER
            +"The Induction Providers are used in the ${link(GuideEntry.INDUCTION, "Induction Matrix")} to determine how fast it is able to output energy through the Induction Port.$(2br)Using multiple Induction Providers in the same Induction Matrix will add extra output capacity, by adding their values together.$(2br)The total output value is for the entire multi-block structure, and not on a \"per port\" basis."
            spotlight(BASIC_INDUCTION_PROVIDER, "Adds 256 kJ/t to output/input rate.")
            spotlight(ADVANCED_INDUCTION_PROVIDER, "Adds 2.04 MJ/t to output/input rate.")
            spotlight(ELITE_INDUCTION_PROVIDER, "Adds 16.38 MJ/t to output/input rate.")
            spotlight(ULTIMATE_INDUCTION_PROVIDER, "Adds 131.07 MJ/t to output/input rate.")
        }

        GuideEntry.BINS{ name = "Bins"
            icon = BASIC_BIN
            +"Bins are storage blocks which can hold large amounts of a single item. It will retain its inventory when broken. Each tier increases the storage capacity.$(p)To store something in a Bin right-click any side while holding an item or stack. This will store what's in your hand."
            +"Double right-click to put the complete amount of an item in your inventory to the bin.$(p)Left-click on the front of the bin to extract a stack. $(k:sneak)-click to extract a single item.$(p)Items can be piped into the bin from the top, and piped out from the bottom. NB: other mods' item handlers are not restricted in this manner."
            +"If you $(k:sneak)-click the bin with a Configurator it will be placed into auto-eject mode. This is indicated by green accents on the front, top, and bottom. In this mode it will pump items out of the bottom automatically."
            spotlight(BASIC_BIN, "Holds 4,096 items.")
            spotlight(ADVANCED_BIN, "Holds 8,192 items.")
            spotlight(ELITE_BIN, "Holds 32,768 items.")
            spotlight(ULTIMATE_BIN, "Holds 262,144 items.")
            spotlight(CREATIVE_BIN, "Holds an infinite amount, does not deplete when withdrawing items.")

        }
    }
}