package mekanism.patchouli.dsl

import com.google.gson.JsonObject
import net.minecraft.util.ResourceLocation

class QuestPage: EntryPage("quest") {
    /**
     * The advancement that should be completed to clear this quest. You may leave this empty should you want the quest to be completed manually. The image shows a quest with "trigger" set on the left and one with it unset on the right.
     */
    var trigger: ResourceLocation? = null

    /**
     * The title of this page, to display above the links. If this is missing or empty, it'll show "Objective" instead.
     */
    var title: String? = null

    /**
     * The text to display on this page, under the links. This text can be formatted.
     */
    var text: String? = null

    override fun toJson(): JsonObject {
        return super.toJson().also { json ->
            trigger?.let { json.addProperty("trigger", it) }
            title?.let { json.addProperty("title", it) }
            text?.let { json.addProperty("text", it) }
        }
    }
}