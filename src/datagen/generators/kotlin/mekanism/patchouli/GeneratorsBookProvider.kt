package mekanism.patchouli

import mekanism.common.Mekanism
import mekanism.generators.common.MekanismGenerators
import mekanism.generators.common.registries.GeneratorsBlocks.*
import mekanism.generators.common.registries.GeneratorsItems.*
import net.minecraft.data.DataGenerator
import net.minecraft.data.DirectoryCache

import mekanism.patchouli.GeneratorsGuideCategory.*
import mekanism.patchouli.GeneratorsGuideEntry.*

/**
 * Created by Thiakil on 20/02/2021.
 */
class GeneratorsBookProvider(generator: DataGenerator): BasePatchouliProvider(generator, MekanismGenerators.MODID) {
    override fun act(output: DirectoryCache) {
        output("generators") {
            extend(Mekanism.MODID, MekanismMainBookProvider.bookId)
            locale = "en_us"
            existingCategory(GuideCategory.MULTIBLOCKS) {
                FUSION {
                    name = "Fusion Reactor"
                    icon = FUSION_REACTOR_CONTROLLER
                }
                FISSION {
                    name = "Fission Reactor"
                    icon = FISSION_FUEL_ASSEMBLY
                }
            }

            GENERATORS {
                name = "Generators"
                description = "Generate electricity with these single block generators."
                icon = SOLAR_PANEL
                SOLAR_GENERATOR {

                }
            }
        }
    }
}