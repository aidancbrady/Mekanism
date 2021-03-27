package mekanism.patchouli

import mekanism.common.Mekanism
import mekanism.patchouli.content.*
import net.minecraft.data.DataGenerator
import net.minecraft.data.DirectoryCache

/**
 * Created by Thiakil on 16/09/2020.
 */
class MekanismMainBookProvider(generator: DataGenerator): BasePatchouliProvider(generator, Mekanism.MODID) {
    override fun act(output: DirectoryCache) {
        output(Companion.bookId) {
            name = "Mekanism HandyGuide"
            locale = "en_us"
            landingText = "Here at Mekanism, Inc. we pride ourselves on our user-friendly creations, but sometimes a little nudge in the right direction is needed. Enter: the Mekanism HandyGuide - your handy dandy guide to the world of Mekanism."
            creativeTab = Mekanism.tabMekanism
            showProgress = false
            i18n = true//some item names etc
            subtitle = Mekanism.instance.versionNumber.toString()

            itemCategory()
            blockCategory()
            multiblocks()
            oreProcessing()
            liquids()
            chemicals()
        }
    }

    companion object {
        const val FORCED_ITEM_SORT_NUM = 98
        const val FORCED_BLOCK_SORT_NUM = 99
        const val bookId = "mekanism"
    }
}