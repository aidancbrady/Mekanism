package mekanism.patchouli.dsl

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

class EmptyPage: EntryPage("empty") {
    /**
     * Defaults to true. Set to false to draw a completely empty page, without the page filler... for whatever reason.
     */
    @SerializedName("draw_filler")
    var drawFiller: Boolean? = null

    override fun toJson(): JsonObject {
        return super.toJson().also { json->
            drawFiller?.let { json.addProperty("draw_filler", it) }
        }
    }
}