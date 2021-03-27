package mekanism.patchouli.dsl

import com.google.gson.JsonObject
import mekanism.api.providers.IItemProvider
import net.minecraft.util.ResourceLocation

abstract class BaseCraftingPage(
        /** The ID of the first recipe you want to show. */
        val recipe: ResourceLocation,
        type: String
) : EntryPage(type) {

    /** The ID of the second recipe you want to show. Displaying two recipes is optional. */
    var recipe2: ResourceLocation? = null

    var secondaryRecipe: IItemProvider
        get() = throw UnsupportedOperationException()
        set(value) {
            this.recipe2 = value.registryName
        }

    /** The title of the page, to be displayed above both recipes. This is optional, but if you include it, only this title will be displayed, rather than the names of both recipe output items. */
    var title: String? = null

    /** The text to display on this page, under the recipes. This text can be formatted. */
    var text: String? = null

    override fun toJson(): JsonObject {
        return super.toJson().also { json->
            json.addProperty("recipe", recipe)
            recipe2?.let {  json.addProperty("recipe2", it) }
            title?.let {  json.addProperty("title", it) }
            text?.let {  json.addProperty("text", it) }
        }
    }
}

class CraftingPage(recipe: ResourceLocation): BaseCraftingPage(recipe, "crafting")

class SmeltingPage(recipe: ResourceLocation): BaseCraftingPage(recipe, "smelting")