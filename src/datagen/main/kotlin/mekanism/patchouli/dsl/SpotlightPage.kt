package mekanism.patchouli.dsl

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import net.minecraft.item.ItemStack

class SpotlightPage: EntryPage("spotlight") {
    /**
     * An ItemStack String representing the item to be spotlighted.
     */
    lateinit var item: ItemStack

    /**
     * A custom title to show instead on top of the item. If this is empty or not defined, it'll use the item's name instead.
     */
    var title: String? = null

    /**
     * Defaults to false. Set this to true to mark this spotlight page as the "recipe page" for the item being spotlighted.
     * If you do so, when looking at pages that display the item, you can shift-click the item to be taken to this page.
     * Highly recommended if the spotlight page has instructions on how to create an item by non-conventional means.
     */
    @SerializedName("link_recipe")
    var linkRecipe: Boolean? = null

    /**
     * The text to display on this page, under the item. This text can be formatted.
     */
    var text: String? = null

    override fun toJson(): JsonObject {
        return super.toJson().also { json->
            json.addProperty("item", item)
            title?.let {  json.addProperty("title", it) }
            linkRecipe?.let {  json.addProperty("link_recipe", it) }
            text?.let {  json.addProperty("text", it) }
        }
    }
}