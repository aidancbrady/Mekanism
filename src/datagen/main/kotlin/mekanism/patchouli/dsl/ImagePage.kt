package mekanism.patchouli.dsl

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import net.minecraft.util.ResourceLocation

class ImagePage: EntryPage("image") {
    /**
     * An array with images to display. Images should be in resource location format.
     * For example, the value botania:textures/gui/entries/banners.png will point to /assets/botania/textures/gui/entries/banners.png
     * in the resource pack.
     * Images used here should ideally be dimensioned as 256x256, and use only a 200x200 canvas centered in the top-left corner for contents,
     * which are rendered at a 0.5x scale compared to the rest of the book in pixel size.
     *
     * If there's more than one image in this array, arrow buttons are shown like in the picture,
     * allowing the viewer to switch between images. If there's only one image, they're not.
     */
    var images: MutableList<ResourceLocation> = mutableListOf()

    fun image(img: ResourceLocation) {
        images.add(img)
    }

    operator fun ResourceLocation.unaryPlus(){
        images.add(this)
    }

    /**
     * The title of the page, shown above the image.
     */
    var title: String? = null

    /**
     * Defaults to false. Set to true if you want the image to be bordered, like in the picture. It's suggested that border is set to true for images that use the entire canvas, whereas images that don't touch the corners shouldn't have it.
     */
    var border: Boolean? = null

    /**
     * The text to display on this page, under the image. This text can be formatted.
     */
    var text: String? = null

    operator fun String.unaryPlus(){
        text = this
    }

    override fun toJson(): JsonObject {
        return super.toJson().also { json->
            val images = JsonArray()
            for (image in this.images) {
                images.add(image.toString())
            }
            json.add("images", images)
            title?.let {  json.addProperty("title", it) }
            border?.let {  json.addProperty("border", it) }
            text?.let {  json.addProperty("text", it) }
        }
    }
}