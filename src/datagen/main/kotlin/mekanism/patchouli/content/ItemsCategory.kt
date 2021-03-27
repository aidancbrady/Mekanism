package mekanism.patchouli.content

import mekanism.client.MekanismKeyHandler
import mekanism.common.content.gear.Modules
import mekanism.common.registries.MekanismBlocks.*
import mekanism.common.registries.MekanismGases.*
import mekanism.common.registries.MekanismItems.*
import mekanism.common.resource.OreType
import mekanism.common.resource.PrimaryResource
import mekanism.common.resource.ResourceType
import mekanism.patchouli.GuideCategory
import mekanism.patchouli.GuideEntry
import mekanism.patchouli.MekanismMainBookProvider
import mekanism.patchouli.dsl.PatchouliBook
import mekanism.patchouli.dsl.invoke
import mekanism.patchouli.dsl.link

fun PatchouliBook.itemCategory() {
    GuideCategory.ITEMS {
        name = "Items List"
        description = "A list of the items in Mekanism."
        icon = INFUSED_ALLOY
        sortNum = MekanismMainBookProvider.FORCED_ITEM_SORT_NUM

        GuideCategory.ITEMS_GEAR {
            name = "Gear"
            description = "Suit up, attack, or configure with these items."
            icon = ELECTRIC_BOW

            JETPACK("We here at Mekanism, Inc. are not responsible for any incidents involving fall damage.$(p)The Jetpack is an item that allows the player to fly, equippable in the chestplate slot. It uses ${link(HYDROGEN, "Hydrogen")} as a fuel, of which it can store up to 24,000 mB.") {
                text {
                    title = "Fueling"
                    text = "The Jetpack can be filled up wherever Hydrogen gas is outputted into a slot.$(br)" +
                            "Here are a few examples:" +
                            "$(li)It can be placed in the ${link(ELECTROLYTIC_SEPARATOR, "Electrolytic Separator's")} hydrogen slot after placing water in the machine's input slot" +
                            "$(li)It can be placed in a ${link(GuideEntry.TANKS_GAS, "Chemical Tank's")} output slot.$(br2)" +
                            "While worn, the Jetpack displays the Hydrogen remaining and the mode active in the bottom left corner."
                }
                text {
                    title = "Operation Modes"
                    text = "The Jetpack has three modes to choose from, which can be toggled by pressing the ${MekanismKeyHandler.chestModeSwitchKey()} key." +
                            "$(li)$(bold)Regular$() (default): Press $(k:jump) to increase your height and release to fall. Note that you will take fall damage unless you carefully lower yourself to the ground." +
                            "$(li)$(bold)Hover$(): Constant flight, without the need to level yourself like you do with Regular mode. Press $(k:jump) to increase altitude and press $(k:sneak) to decrease. "
                }
                +("Note that this mode constantly consumes Hydrogen, but at a reduced rate as compared to Regular mode." +
                        "$(li)$(bold)Disabled$(): The Jetpack is disabled.")
                text {
                    title = "Tips"
                    text = "\$(li)The Jetpack cannot be worn with chestplate armor, since it uses the same slot, consider upgrading it to the ${link(ARMORED_JETPACK, "Armored Jetpack")} if you want protection." +
                            "\$(li)The Jetpack emits fire particles; however, it will not set anything on fire." +
                            "\$(li)If you want to maintain your altitude, choose Hover mode. " +
                            "\$(li)If you want to ascend/descend rapidly, use Regular mode. "
                }
                text {
                    text = "\$(li)If you want to conserve fuel while trekking across hills, mountains, consider Disabled mode." +
                            "\$(li)The Jetpack can be paired with the Free Runners to protect against fall damage."
                }
            }
            ATOMIC_DISASSEMBLER("The Atomic Disassembler is Mekanism's an all-in-one tool, essentially the ultimate, electronic version of the Paxel (working at any mining level). Also functions as a Hoe & Scoop (Forestry)$(p)The Atomic Disassembler has multiple modes that can be cycled with $(k:sneak) + right click.") {
                text {
                    title = "Normal Mode"
                    text = "Base speed setting, single block, roughly equivalent to Efficiency II.$(p)Farmland tilling and Grass Path functions have been moved to the MekaTool ${link(Modules.FARMING_UNIT, "Farming Unit")}"
                }
                text {
                    title = "Slow Mode"
                    text = "Slower than Normal Mode, less power usage"
                }
                text {
                    title = "Fast Mode"
                    text = "Super mode, roughly equivalent to Efficiency V. Uses more energy to function."
                }
                text {
                    title = "Vein Mode"
                    text = "Like normal mode but will mine a vein of Ore or Log blocks (tagged with forge:ores or forge:logs) matching the start block."
                }
                text {
                    title = "Extended Vein Mining"
                    text = "Like Vein Mode, but works with any block."
                }
                text {
                    title = "Off"
                    text = "Functions as if out of power - no mining speed benefits or extended functionality."
                }
            }
            ARMORED_JETPACK("The Armored Jetpack is an upgraded version of the ${link(JETPACK, "Jetpack")}. It is intended to provide 12 armor points, offering slightly better protection than a Diamond Chestplate with Protection IV. Numbers accurate as of Minecraft 1.7.10")
            SCUBA_TANK("A piece of equipment worn in the chest armor slot that provides underwater respiration when a ${link(SCUBA_MASK, "Scuba Mask")} is worn. The Scuba Tank must be filled with Oxygen gas in order to function.") {
                +"When you first put on the Scuba Tank, its oxygen supply will be turned off. In order to use it underwater you must turn it on by using the ${MekanismKeyHandler.chestModeSwitchKey()} button. Also, you must have the Gas Mask equipped in the helmet armor slot or you won't be able to breathe and will start to drown."
                text {
                    title = "Tips"
                    text = "$(li)Any potion effect will instantly stop once you equip the Gas Mask and turn on the oxygen supply." +
                            "$(li)Be sure to turn the oxygen supply OFF when you are above surface, otherwise you'll waste oxygen." +
                            "$(li)Leave the oxygen supply on while underwater. You can't cheat to conserve oxygen by turning it on and off, because the oxygen will be consumed faster to refill the breath meter." +
                            "$(li)The Scuba Tank will not work in Airless dimensions in some other mods, such as the outer space dimensions in Galacticraft (Moon, Mars, etc.)."
                }
            }
            SCUBA_MASK("The Scuba Mask is an utility head armor piece, used in conjunction with the ${link(SCUBA_TANK, "Scuba Tank")} to breathe underwater.$(p)It can be enchanted with respiration and water breathing for extended use.")
            CONFIGURATOR("The Configurator is a configuration tool for Mekanism machines & pipes.$(p)It comes with several different modes that you can switch between by sneaking and then pressing the Item Mode Switch Key (${MekanismKeyHandler.handModeSwitchKey()})") {
                text("Configurate") {
                    text = "Mousing over a Mekanism machine or factory will show the color for that side, using the the Configuration Color Scheme. Right clicking will print a message announcing both the color and input/output mode. $(k:sneak) + Right Clicking will cycle through the valid colors for the given sub-mode."
                }
                +("$(li)Grey is no connection (neither in nor out)." +
                        "\$(li)Dark Red is input (items, gasses)." +
                        "\$(li)Dark Blue is output (items, gasses)." +
                        "\$(li)Green is for Energy input (items, cable)." +
                        "\$(li)Purple is Infusion item input (for the Metallurgic Infuser)" +
                        "\$(li)Yellow is for fluids (for the Pressurized Reaction Chamber)")
                +("Additionally, you can interact with any of the cables, pipes, transporters, or tubes to set their connection type between machines/inventory and their redstone sensitivity. Right clicking on the center of the cable/pipe/transporter/tube will toggle sensitivity off/on (default is on). \$(k:sneak) + Right clicking on a segment between the center of the cable/etc. and machine will cycle between:" +
                        "$(li)Normal" +
                        "\$(li)Pull - try to take from the machine")
                +("\$(li)Push - try to insert only into the machine" +
                        "\$(li)None - no connection. Will not try to push or pull items from the machine.")
                text("Empty") {
                    text = "\$(k:sneak) + Right Clicking on the machine while in this mode will eject any and all items currently in the machine in random directions. It will not dump fluids or gasses."
                }
                text("Rotate") {
                    text = "Right clicking on a face will have that set as \"forward\" while \$(k:sneak) + Right clicking will have that set as \"back\" The Energy Cube can have its top and bottom faces designated as \"forward.\""
                }
                text("Wrench") {
                    text = "Behaves like a wrench from most other mods. Right click to rotate the machine clockwise on the ground, \$(k:sneak) + Right click to have the machine instantly pried loose as an item (works on cables and pipes, too!)"
                }
            }
            ELECTRIC_BOW("Arrows not included.") {
                +"Much like a normal bow, but uses energy instead of durability. Can also set arrows on fire (toggle with ${MekanismKeyHandler.handModeSwitchKey()})."
            }
            FLAMETHROWER("The Flamethrower is a ranged weapon which uses Hydrogen gas as its fuel. It is fairly effective against mobs as it deals damage when they are directly hit with the stream and sets them on fire. It is most effective on large groups of mobs, where the user can hose down the entire group with fuel at a short distance.") {
                text("Modes") {
                    text = "You can switch between three fire modes using \$(k:sneak) + ${MekanismKeyHandler.handModeSwitchKey()}. The modes are" +
                            "$(li)\$(bold)Combat\$() - The default mode. Damages mobs and sets them on fire. Destroys any items on the ground. Does not set fire to blocks nor damage them." +
                            "$(li)\$(bold)Heat\$() - Same as combat, but blocks/items that have a smelter recipe will be instantly converted into it. For example you can fire a short burst at iron ore block and a single ingot of iron"
                }
                +"will be dropped. \$(li)\$(bold)Inferno\$() - Same as combat, but blocks that the stream hits will be hit with blast damage (like with creepers, ghasts, TNT) and will usually be destroyed. Nearby blocks will be set on fire."
            }
            FREE_RUNNERS("Free Runners are an item that allows players to ascend 1-block inclines automatically, as well as preventing fall damage as long as they are charged. A fall will reduce the item's charge, depending on how far the fall was.$(p)Can be toggled with ${MekanismKeyHandler.feetModeSwitchKey()}")
            NETWORK_READER("Sends information about the targeted pipe network to chat.")
            PORTABLE_TELEPORTER("A player kept teleportation device. It can store power and like all Mekanism teleporters, energy drain increases with the distance the player teleports to.") {
                text {
                    title = "Usage"
                    text = "Right-clicking with this device in hand will open a GUI similar to that of the full Teleporter, allowing instant travel to any Teleporters that the player has set up. The Portable Teleporter is capable of multidimensional travel.$(p)Note that in order for the Portable Teleporter to be functional, the complete Teleporter Portal structure does $(bold)not$() need to be built; only the Teleporter block must be present (and supplied with power)."
                }
            }
            SEISMIC_READER("The Seismic Reader is used in conjunction with the ${link(SEISMIC_VIBRATOR, "Seismic Vibrator")} to analyze the ground immediately around the vibrator, informing you of the blocks, by level, all the way to bedrock level.")
            CANTEEN("The Canteen is used to store ${link(NUTRITIONAL_PASTE, "Nutritional Paste")} (total of 64 Buckets). When hungry, you can hold right click to drink some Nutritional Paste. Each hunger point (half a hunger bar) consumes 50mB of Nutritional Paste.")
            GAUGE_DROPPER("The Gauge Dropper is a really handy tool for managing the fluid/chemical inventories of machines/blocks.$(p)Open up the inventory of the machine, click on your Gauge Dropper to move it around the GUI. Hover over the substance you want to extract with the Gauge Dropper and left click. The substance should now be") {
                +"in the Gauge Dropper (it can hold up to 16,000mB).\$(p) You can hover over a fluid/gas gauge and right click to deposit the stored contents in your Gauge Dropper. \$(p)Shift left click on a gauge to dump all of the content in said gauge. \$(p)Shift right click on any block, while holding the Gauge Dropper, to dump all of the Gauge Dropper's contents."
            }
            //TODO check dictionary functions
            DICTIONARY("Don't worry, you won't have to read much.") {
                +"The Dictionary allows you to check the Tags (vanilla mechanic) of an item which can be used in a Tag Filter for things like the ${link(DIGITAL_MINER, "Digital Miner")} & ${link(LOGISTICAL_SORTER, "Logistical Sorter")}, or in the ${link(OREDICTIONIFICATOR, "Oredictionificator")}."
                text {
                    title = "Usage"
                    text = "Right click to open the GUI and insert an item into the slot. The tags will be listed.$(p)Right clicking a block in-world will tell you the Tags of that block."
                }
            }

            DOSIMETER("Measures your accumulated radiation dosage.\$(p)Use the ${link(GEIGER_COUNTER, "Geiger Counter")} to assess the level of radiation in an area.") {
                text {
                    title = "Usage"
                    text = "Right click in the air to show $(bold)your$() current radiation dose in the chat feed. NB: this will never be zero, as there is always some background radiation."
                }
            }
            GEIGER_COUNTER("Measures the radiation level around you.\$(p)Use the ${link(DOSIMETER, "Dosimeter")} to assess your existing radiation exposure."){
                text {
                    title = "Usage"
                    text = "Right click in the air to show the $(bold)surrounding$() radiation level in the chat feed. NB: this will never be zero, as there is always some background radiation."
                }
            }
            entry(HAZMAT_GOWN) {
                name = "Hazmat Suit"
                +"The hazmat suit will protect you from surrounding radiation. You have to wear all the pieces of the hazmat suit in order to receive full protection from radiation. Otherwise, your radiation dosage will increase.$(p)Damage from $(bold)prior$() exposure will continue."
                spotlight(HAZMAT_MASK, "For your face.")
                spotlight(HAZMAT_GOWN, "For your torso.")
                spotlight(HAZMAT_PANTS, "For your legs.")
                spotlight(HAZMAT_BOOTS, "For your feet.")
            }
        }
        GuideCategory.ITEMS_METAL_AND_ORE {
            name = "Metals & Ores"
            description = "Ore/Metal processing based materials."
            icon = PROCESSED_RESOURCES.get(ResourceType.INGOT, PrimaryResource.OSMIUM)

            entry(BRONZE_INGOT) {
                name = "Bronze"
                readByDefault = true
                +"Bronze is an alloy of Copper and Tin."
                spotlight(BRONZE_INGOT)
                spotlight(BRONZE_DUST)
                spotlight(BRONZE_NUGGET)
            }

            CHARCOAL_DUST("Crushed form of Charcoal")
            COAL_DUST("Crushed form of Coal")
            DIAMOND_DUST("Crushed form of Diamond")
            EMERALD_DUST("Crushed form of Emerald")
            OBSIDIAN_DUST("Crushed form of Obsidian")
            QUARTZ_DUST("Crushed form of Quartz")
            LAPIS_LAZULI_DUST("Crushed form of Lapis Lazuli")

            LITHIUM_DUST("Crystallized form of ${link(LITHIUM, "Lithium")}")

            entry(REFINED_GLOWSTONE_INGOT) {
                name = "Refined Glowstone"
                readByDefault = true
                +"Refined glowstone is a stronger form of Glowstone, reinforced with Osmium in the ${link(OSMIUM_COMPRESSOR, "Osmium Compressor")}."
                spotlight(REFINED_GLOWSTONE_INGOT)
                spotlight(REFINED_GLOWSTONE_NUGGET)
                spotlight(REFINED_GLOWSTONE_BLOCK)
            }

            entry(REFINED_OBSIDIAN_INGOT) {
                name = "Refined Obsidian"
                readByDefault = true
                +"Harder obsidian? Unpossible!$(p)Obsidian reinforced with Osmium in the ${link(OSMIUM_COMPRESSOR, "Osmium Compressor")}. Can be used to form a Nether Portal"

                spotlight(REFINED_OBSIDIAN_DUST)
                spotlight(REFINED_OBSIDIAN_INGOT)
                spotlight(REFINED_OBSIDIAN_NUGGET)
                spotlight(REFINED_OBSIDIAN_BLOCK)
            }

            entry(STEEL_INGOT) {
                name = "Steel"
                readByDefault = true
                +"Steel is a hardened metal used in most Mekanism constructions."
                spotlight(ENRICHED_IRON, "Intermediate step in Mekanism Steel production.")
                spotlight(STEEL_INGOT)
                spotlight(STEEL_DUST)
                spotlight(STEEL_NUGGET)
                spotlight(STEEL_BLOCK)
            }

            SULFUR_DUST("Solidified sulfur, can be used to make ${link(SULFURIC_ACID, "Sulfuric Acid")}.")

            entry(PROCESSED_RESOURCES.get(ResourceType.CLUMP, PrimaryResource.IRON)!!) {
                name = "Clumps"
                readByDefault = true
                +"Clumps are part of the ${link(GuideEntry.ORE_TRIPLING, "3x Ore Processing")} pipeline and above."
                PROCESSED_RESOURCES.row(ResourceType.CLUMP).values.forEach(this::spotlight)
            }

            entry(PROCESSED_RESOURCES.get(ResourceType.SHARD, PrimaryResource.IRON)!!) {
                name = "Crystals"
                readByDefault = true
                +"Crystals are part of the ${link(GuideEntry.ORE_QUADRUPLING, "4x Ore Processing")} pipeline and above."
                PROCESSED_RESOURCES.row(ResourceType.SHARD).values.forEach(this::spotlight)
            }

            entry(PROCESSED_RESOURCES.get(ResourceType.CRYSTAL, PrimaryResource.IRON)!!) {
                name = "Crystals"
                readByDefault = true
                +"Crystals are part of the ${link(GuideEntry.ORE_QUINTUPLING, "5x Ore Processing")} pipeline and above."
                PROCESSED_RESOURCES.row(ResourceType.CRYSTAL).values.forEach(this::spotlight)
            }

            DIRTY_NETHERITE_SCRAP("Dirty Netherite Scraps are part of the ore processing of Ancient Debris.")

            entry(PROCESSED_RESOURCES.get(ResourceType.INGOT, PrimaryResource.OSMIUM)) {
                name = "Osmium"
                readByDefault = true
                +"Osmium is a hard, brittle, bluish-white transition metal in the platinum group that is found as a trace element in alloys, mostly in platinum ores.$(p)Osmium is the densest stable element; it is approximately twice as dense as lead and slightly denser than iridium."
                spotlight(ORES[OreType.OSMIUM]!!)
                PROCESSED_RESOURCES.column(PrimaryResource.OSMIUM).values.forEach(this::spotlight)
            }

            entry(PROCESSED_RESOURCES.get(ResourceType.INGOT, PrimaryResource.COPPER)) {
                name = "Copper"
                readByDefault = true
                +"Copper is a soft, malleable, and ductile metal with very high thermal and electrical conductivity. A freshly exposed surface of pure copper has a pinkish-orange color."
                spotlight(ORES[OreType.COPPER]!!)
                PROCESSED_RESOURCES.column(PrimaryResource.COPPER).values.forEach(this::spotlight)
            }

            entry(PROCESSED_RESOURCES.get(ResourceType.INGOT, PrimaryResource.TIN)) {
                name = "Tin"
                readByDefault = true
                +"Tin is a silvery metal that characteristically has a faint yellow hue. Tin, like indium, is soft enough to be cut without much force."
                spotlight(ORES[OreType.TIN]!!)
                PROCESSED_RESOURCES.column(PrimaryResource.TIN).values.forEach(this::spotlight)
            }

            entry(PROCESSED_RESOURCES.get(ResourceType.INGOT, PrimaryResource.URANIUM)) {
                name = "Uranium"
                readByDefault = true
                +"Uranium is a silvery-grey metal in the actinide series of the periodic table. A uranium atom has 92 protons and 92 electrons, of which 6 are valence electrons. Uranium is weakly radioactive because all isotopes of uranium are unstable; the half-lives of its naturally occurring isotopes range between 159,200 years and 4.5 billion years."
                spotlight(ORES[OreType.URANIUM]!!)
                PROCESSED_RESOURCES.column(PrimaryResource.URANIUM).values.forEach(this::spotlight)
            }

            entry(PROCESSED_RESOURCES.get(ResourceType.INGOT, PrimaryResource.LEAD)) {
                name = "Lead"
                readByDefault = true
                +"Lead is a heavy metal that is denser than most common materials. Lead is soft and malleable, and also has a relatively low melting point."
                spotlight(ORES[OreType.URANIUM]!!)
                PROCESSED_RESOURCES.column(PrimaryResource.URANIUM).values.forEach(this::spotlight)
            }

            entry(FLUORITE_GEM) {
                name = "Fluorite"
                readByDefault = true
                +"Fluorite (also called fluorspar) is the mineral form of calcium fluoride, CaF2. It belongs to the halide minerals group."
                spotlight(ORES[OreType.FLUORITE]!!)
                spotlight(FLUORITE_DUST)
                spotlight(FLUORITE_GEM)
            }

        }
        GuideCategory.ITEMS_UPGRADES {
            name = "Upgrades"
            description = "You gotta pump up them numbers, rookie. Increase various abilities of machines with these items.$(br)Insert via the machine's GUI, Upgrades tab."
            icon = SPEED_UPGRADE

            SPEED_UPGRADE("An upgrade to increate the running speed of a machine.") {
                +"Note that every speed upgrade makes the machine 33% faster, and the Power Usage increases with ~77% (33²%), which makes for an increase in power usage for each operation with 33%.$(p)$(bold)The machine must have enough buffer to run one operation or it will not run at all."
            }
            ENERGY_UPGRADE("Upgrades the energy buffer of a machine and reduces its per-operation consumption.$(p)The ${link(ELECTROLYTIC_SEPARATOR, "Electrolytic Separator")} only receives a buffer increase.")
            FILTER_UPGRADE("The Filter Upgrade is an upgrade that, when used in the ${link(ELECTRIC_PUMP, "Electric Pump")}, allows the Electric Pump to produce Heavy Water.")
            MUFFLING_UPGRADE("Reduces the sound produced by a machine.")
            GAS_UPGRADE("The gas upgrade allows you to increase the gas usage efficiency of a Mekanism machine which consumes gas.")
            ANCHOR_UPGRADE("The Anchor Upgrade is a machine upgrade which keeps the chunk of the machine to which it is applied loaded. This is helpful for machines like the Digital Miner and Teleporter which must be in loaded chunks to function properly.$(p)$(bold)Compatible machines$()$(li)${link(DIGITAL_MINER, "Digital Miner")}$(li)${link(QUANTUM_ENTANGLOPORTER, "Quantum Entangloporter")}$(li)${link(GuideEntry.TELEPORTER, "Teleporter")}")
        }

        GuideEntry.ALLOYS {
            name = "Alloys"
            icon = INFUSED_ALLOY
            +"Crafting components used to make tiered items. Can also be right clicked on Logistical Transporters, Mechanical Pipes, Pressurized Tubes, Thermodynamic Conductors, and Universal Cables to upgrade tiers in-world.$(p)Created in a ${link(METALLURGIC_INFUSER, "Metallurgic Infuser")}."
            spotlight(INFUSED_ALLOY, "Redstone infused")
            spotlight(REINFORCED_ALLOY, "Diamond infused")
            spotlight(ATOMIC_ALLOY, "Refined Obsidian infused")
        }

        GuideEntry.CIRCUITS {
            name = "Circuits"
            icon = BASIC_CONTROL_CIRCUIT
            +"Crafting components used to make tiered items. Created in the ${link(METALLURGIC_INFUSER, "Metallurgic Infuser")}."
            spotlight(BASIC_CONTROL_CIRCUIT, "Osmium based.")
            spotlight(ADVANCED_CONTROL_CIRCUIT, "Infused Alloy based.")
            spotlight(ELITE_CONTROL_CIRCUIT, "Reinforced Alloy based.")
            spotlight(ULTIMATE_CONTROL_CIRCUIT, "Atomic Alloy based.")
        }

        GuideEntry.INSTALLERS {
            name = "Installers"
            icon = BASIC_TIER_INSTALLER
            +"Upgrade the tier of a block in world, without needing to put it in a crafting grid.$(p)Can upgrade factory machines, Bins, and Energy Cubes"
            spotlight(BASIC_TIER_INSTALLER, "Upgrades block to basic tier. Used to turn machines into their factory variant.")
            spotlight(ADVANCED_TIER_INSTALLER, "Upgrades block to Advanced tier. Requires block to be Basic tier.")
            spotlight(ELITE_TIER_INSTALLER, "Upgrades block to Elite tier. Requires block to be Advanced tier.")
            spotlight(ULTIMATE_TIER_INSTALLER, "Upgrades block to Ultimate tier. Requires block to be Elite tier.")
        }

        BIO_FUEL("A fuel made from plant material in a Crusher.$(p)Used in a Biofuel Generator (Mekanism Generators required) for power or ${link(PRESSURIZED_REACTION_CHAMBER, "Pressurized Reaction Chamber")} to produce Ethylene.") {
            //todo override in mek generators' book generator?
        }
        CONFIGURATION_CARD("An item used to copy configuration data from one machine to another.$(p)To copy data to the card, $(k:sneak) + right click on the source machine, then right click the destination machine. Chat messages will inform you of the success/failure") {
            +"Supported machines: \$(li)${link(DIGITAL_MINER, "Digital Miner")} \$(li)${link(GuideEntry.ENERGY_CUBES, "Energy Cubes")} \$(li)${link(FORMULAIC_ASSEMBLICATOR, "Formulaic Assemblicator")} \$(li)${link(LOGISTICAL_SORTER, "Logistical Sorter")} \$(li)${link(OREDICTIONIFICATOR, "Oredictionificator")} \$(li)and any machine with configurable sides."
        }
        CRAFTING_FORMULA("Used in the ${link(FORMULAIC_ASSEMBLICATOR, "Formulaic Assemblicator")} to encode a crafting recipe for automatic operation.") {
            text {
                title = "Usage"
                text = "Open up a Formualaic Assemblicator's GUI. Put in the crafting recipe of the item you want to craft. Insert a Crafting Formula into that blank spot to the left of the crafting grid and press Encode.$(p)Now the recipe has been encoded into the Crafting Formula, and if you insert the Crafting Formula into any Formulaic Assemblicator, "
            }
            +"the recipe will appear in the crafting grid. $(p)$(k:sneak) + $(k:use) the Crafting Formula in the air to clear the encoded recipe."
        }
        ELECTROLYTIC_CORE("Crafting ingredient used for electrolysis.")
        ENERGY_TABLET("The Energy Tablet is chiefly an a crafting component and a can be used directly as a battery. Charge will be retained when used as a crafting ingredient.")
        GuideEntry.ENRICHED_INFUSION {
            name = "Enriched Infusion"
            icon = ENRICHED_REDSTONE
            +"Infusion ingredients can be enriched in the ${link(ENRICHMENT_CHAMBER, "Enrichment Chamber")} to provide more infusion amount in the ${link(METALLURGIC_INFUSER, "Metallurgic Infuser")}."
            spotlight(ENRICHED_REDSTONE)
            spotlight(ENRICHED_DIAMOND)
            spotlight(ENRICHED_OBSIDIAN)
            spotlight(ENRICHED_CARBON)
            spotlight(ENRICHED_GOLD)
            spotlight(ENRICHED_TIN)
        }
        entry(HDPE_SHEET) {
            name = "HDPE"
            +"High Density PolyEthylene is used to make plastic."
            spotlight(HDPE_PELLET, "First stage of HDPE production")
            spotlight(HDPE_SHEET, "A sheet of plastic.")
            spotlight(HDPE_ROD, "A rod of plastic")
            spotlight(HDPE_STICK, "It's a stick.")
        }
    }// end items category
}