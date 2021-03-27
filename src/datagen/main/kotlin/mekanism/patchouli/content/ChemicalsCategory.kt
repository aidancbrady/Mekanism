package mekanism.patchouli.content

import mekanism.common.content.gear.Modules
import mekanism.common.registries.MekanismBlocks.*
import mekanism.common.registries.MekanismGases.*
import mekanism.common.registries.MekanismItems.*
import mekanism.patchouli.GuideCategory
import mekanism.patchouli.GuideEntry
import mekanism.patchouli.dsl.PatchouliBook
import mekanism.patchouli.dsl.link

fun PatchouliBook.chemicals() {
    GuideCategory.CHEMICALS {
        name = "Chemicals"
        description = "Gasses and other chemicals. Transport these with ${link(GuideEntry.PIPES_GAS, "Pressurized Tubes")} and store them in ${link(GuideEntry.TANKS_GAS, "Chemical Tanks")} or a high capacity ${link(GuideEntry.DYNAMIC_TANK, "Dynamic Tank")}."
        icon = ULTIMATE_CHEMICAL_TANK

        BRINE {
            +"The gaseous form of brine, made by putting ${link(SALT, "salt")} in a ${link(CHEMICAL_OXIDIZER, "Chemical Oxidizer")}, or by evaporating liquid brine in a ${link(ROTARY_CONDENSENTRATOR, "Rotary Condenstrator")}."
            +"The Condenstrator can also turn it back into liquid brine, by pressing the \"Switch Operation\" toggle."
        }

        CHLORINE {
            +"Chlorine is combined in a ${link(CHEMICAL_INFUSER, "Chemical Infuser")} along with ${link(HYDROGEN, "Hydrogen")} to make ${link(HYDROGEN_CHLORIDE, "Hydrogen Chloride")}."
            +"It is produced by putting brine in an ${link(ELECTROLYTIC_SEPARATOR, "Electrolytic Separator")}, which also produces ${link(SODIUM, "Sodium")}"
        }

        //todo move to Generators book
        GuideEntry.CHEMICAL_DEUTERIUM {
            name = "Deuterium"
            icon = ULTIMATE_CHEMICAL_TANK
            +"Deuterium is an isotope of ${link(HYDROGEN, "Hydrogen")} with an extra neutron, used in a ${link(GuideEntry.GENERATORS_FUSION, "Fusion Reactor")}."
            +"It is made by putting heavy water in an ${link(ELECTROLYTIC_SEPARATOR, "Electrolytic Separator")}, which also produces ${link(OXYGEN, "oxygen")}"
            +"Deuterium and ${link(GuideEntry.CHEMICAL_TRITIUM, "Tritium")} can be either injected straight into a reactor, or combined in a ${link(CHEMICAL_INFUSER, "Chemical Infuser")} to create ${link(GuideEntry.CHEMICAL_DT_FUEL, "D-T Fuel")}"
            +"See ${link(GuideEntry.GENERATORS_FUSION, "Fusion Reactor")} for more details."
        }

        //todo move to Generators book
        GuideEntry.CHEMICAL_DT_FUEL {
            name = "D-T Fuel"
            icon = ULTIMATE_CHEMICAL_TANK
            +"D-T Fuel is used to fill the hohlraum, and can also be injected into a ${link(GuideEntry.GENERATORS_FUSION, "Fusion Reactor")} for very high energy production"
            +"Direct D-T injection is only feasible for very high fuel production, see ${link(GuideEntry.GENERATORS_FUSION, "Fusion Reactor")} for more details."
        }

        ETHENE {
            +"Ethylene is the fuel for our ultra-efficient Gas-Burning generator.  It is a byproduct of ${link(HDPE_PELLET, "HDPE (High-Density Polyethylene)")}."
            +"It is produced by the ${link(PRESSURIZED_REACTION_CHAMBER, "Pressurized Reaction Chamber")} when turning ${link(BIO_FUEL, "bio-fuel")} into ${link(SUBSTRATE, "substrate")}, using ${link(HYDROGEN, "hydrogen")} and water."
        }

        HYDROGEN {
            +"Hydrogen is a carrier of energy.  It is produced by putting water in an ${link(ELECTROLYTIC_SEPARATOR, "Electrolytic Separator")}, and can be used as fuel in a Gas-Burning Generator, as well as a ${link(JETPACK, "Jetpack")} or ${link(ARMORED_JETPACK, "Armored Jetpack")} and ${link(FLAMETHROWER, "Flamethrower")}."
            +"It is also used in a ${link(CHEMICAL_INFUSER, "Chemical Infuser")} to make ${link(HYDROGEN_CHLORIDE, "Hydrogen Chloride")}."
        }

        HYDROGEN_CHLORIDE {
            +"Hydrogen Chloride is needed for the ${link(CHEMICAL_INJECTION_CHAMBER, "Injection Chamber")}, part of ${link(GuideEntry.ORE_QUADRUPLING, "Ore Quadrupling")}. It is created by putting ${link(CHLORINE, "Chlorine")} and ${link(HYDROGEN, "Hydrogen")} in a ${link(CHEMICAL_INFUSER, "Chemical Infuser")}."
        }

        LITHIUM {
            +"Lithium is produced by ${link(GuideEntry.THERMAL_EVAP, "evaporating")} ${link(BRINE, "brine")}.  It is processed into ${link(GuideEntry.CHEMICAL_TRITIUM, "Tritium")} in a ${link(SOLAR_NEUTRON_ACTIVATOR, "Solar Neutron Activator")}."
        }

        OXYGEN {
            +"Oxygen is produced by splitting water using an ${link(ELECTROLYTIC_SEPARATOR, "Electrolytic Separator")}, which also produces ${link(HYDROGEN, "Hydrogen")}"
            +"It is used in the ${link(PURIFICATION_CHAMBER, "Purification Chamber")}, part of ${link(GuideEntry.ORE_TRIPLING, "Ore Tripling")}.  It is also combined with ${link(SULFUR_TRIOXIDE, "Sulfur Trioxide")} in the ${link(CHEMICAL_INFUSER, "Chemical Infuser")} to make ${link(SULFUR_DIOXIDE, "Sulfur Dioxide")}, as well as in the ${link(SCUBA_MASK, "Scuba Mask")}."
        }

        SODIUM {
            +"Sodium is a coolant for the fission reactor.  it has a higher heat capacity than water and is needed for larger and/or more active reactors."
            +"It is produced by splitting ${link(BRINE, "Brine")} into Sodium and ${link(CHLORINE, "Chlorine")} in an ${link(ELECTROLYTIC_SEPARATOR, "Electrolytic Separator")}"
        }

        SUPERHEATED_SODIUM {
            +"${link(SODIUM, "Sodium")} that has been heated in a reactor. Remove the heat using a Boiler to return to normal Sodium"
        }

        SULFUR_DIOXIDE {
            +"Sulfur dioxide is a step in ${link(SULFURIC_ACID, "Sulfuric Acid")} production."
            +"It is created by putting sulfur in a ${link(CHEMICAL_OXIDIZER, "Chemical Oxidizer")} and used with ${link(OXYGEN, "Oxygen")} in the ${link(CHEMICAL_INFUSER, "Chemical Infuser")} to create ${link(SULFUR_TRIOXIDE, "Sulfur Trioxide")}."
        }

        SULFUR_TRIOXIDE {
            name = "Sulfur Trioxide"
            icon = ULTIMATE_CHEMICAL_TANK
            +"Sulfur trioxide is another step in ${link(SULFURIC_ACID, "Sulfuric Acid")} production. It is produced by combining ${link(SULFUR_DIOXIDE, "Sulfur Dioxide")} and ${link(OXYGEN, "Oxygen")} in a ${link(CHEMICAL_INFUSER, "Chemical Infuser")}."
            +"It is combined with ${link(WATER_VAPOR, "Water Vapor")} in a Chemical Infuser to create sulfuric acid."
        }

        SULFURIC_ACID {
            +"Sulfuric acid is necesary for ${link(GuideEntry.ORE_QUINTUPLING, "Ore Quintupling")}.  It is used in the ${link(CHEMICAL_DISSOLUTION_CHAMBER, "Chemical Dissolution Chamber")} to dissolve ores into slurry."
            +"It is created by combining ${link(SULFUR_TRIOXIDE, "Sulfur Trioxide")} and ${link(WATER_VAPOR, "Water Vapor")} in the ${link(CHEMICAL_INFUSER, "Chemical Infuser")}."
        }

        //todo move to generators
        GuideEntry.CHEMICAL_TRITIUM {
            name = "Tritium"
            icon = ULTIMATE_CHEMICAL_TANK
            +"Tritium is another isotope of ${link(HYDROGEN, "Hydrogen")}, with two extra neturons.  It is used as fuel in the ${link(GuideEntry.GENERATORS_FUSION, "Fusion Reactor")}."
            +"It can either be injected directly into the reactor along with ${link(GuideEntry.CHEMICAL_DEUTERIUM, "Deuterium")}, or combined into ${link(GuideEntry.CHEMICAL_DT_FUEL, "D-T Fuel")}.  The latter is only useful if production is very high, see ${link(GuideEntry.GENERATORS_FUSION, "Fusion Reactor")} for more information."
        }

        WATER_VAPOR {
            +"Water vapor is created from water in the ${link(ROTARY_CONDENSENTRATOR, "Rotary Condenstrator")}. It is not as hot as Steam"
            +"It is combined with ${link(SULFUR_TRIOXIDE, "Sulfur Trioxide")} in the ${link(CHEMICAL_INFUSER, "Chemical Infuser")} to create ${link(SULFURIC_ACID, "Sulfuric Acid")}.  It can also be combined with dirt in the ${link(CHEMICAL_INJECTION_CHAMBER, "Chemical Injection Chamber")} to make clay."
        }

        HYDROFLUORIC_ACID {
            +"An acid make from ${link(SULFURIC_ACID, "Sulfuric Acid")} and ${link(FLUORITE_GEM, "Fluorite")}, used to make ${link(URANIUM_HEXAFLUORIDE, "Uranium Hexafluoride")}"
        }

        URANIUM_OXIDE {
            +"Oxidized form of ${link(YELLOW_CAKE_URANIUM, "Yellow Cake Uranium")}, used to make ${link(URANIUM_HEXAFLUORIDE, "Uranium Hexafluoride")}"
        }

        URANIUM_HEXAFLUORIDE {
            +"Used to make ${link(FISSILE_FUEL, "Fissile Fuel")} in the ${link(ISOTOPIC_CENTRIFUGE, "Isotopic Centrifuge")}."
        }

        LIQUID_OSMIUM {
            +"Chemical form of Osmium used in the ${link(OSMIUM_COMPRESSOR, "Osmium Compressor")}."
        }

        FISSILE_FUEL {
            +"Fuel source made from ${link(URANIUM_HEXAFLUORIDE, "Uranium Hexafluoride")} in the ${link(ISOTOPIC_CENTRIFUGE, "Isotopic Centrifuge")}."
            text {
                text = "Used in the Fission Reactor."
                flags {
                    generatorsInstalled()
                }
            }
        }

        NUCLEAR_WASTE {
            +"Waste product of used ${link(FISSILE_FUEL, "Fissile Fuel")}."
        }

        SPENT_NUCLEAR_WASTE {
            +"Biproduct of reprocessed ${link(NUCLEAR_WASTE, "Nuclear Waste")}"
        }

        PLUTONIUM {
            +"First stage of ${link(PLUTONIUM_PELLET, "Plutonium Pellet")} production."
        }

        POLONIUM {
            +"First stage of ${link(POLONIUM_PELLET, "Polonium Pellet")} production."
        }

        ANTIMATTER {
            +"Late-game chemical used for advanced processes."
        }

        NUTRITIONAL_PASTE {
            +"Food, tasty paste form. Used with the ${link(CANTEEN, "Canteen")}, or in the ${link(Modules.NUTRITIONAL_INJECTION_UNIT, "Nutritional Injection Unit")}."
        }

    }
}
