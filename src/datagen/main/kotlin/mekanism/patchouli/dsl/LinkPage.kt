package mekanism.patchouli.dsl

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

class LinkPage: TextPage() {
    /**
     * The URL to open when clicking the button. In theory everything is supported, but please stick to HTTP/HTTPS addresses.
     */
    lateinit var url: String

    /**
     * The text to display on the link button.
     */
    @SerializedName("link_text")
    lateinit var linkText: String

    override fun toJson(): JsonObject {
        return super.toJson().also { json->
            json.addProperty("url", url)
            json.addProperty("link_text", linkText)
        }
    }
}