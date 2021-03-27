package mekanism.patchouli.dsl

import com.google.gson.JsonObject

class RelationsPage: EntryPage("relations") {
    /**
     * An array of the entries that should be linked in this page. These are the IDs of the entries you want to link to in the same way you'd link an entry to a category's ID.
     */
    var entries: MutableList<String> = mutableListOf()

    /**
     * The title of this page, to display above the links. If this is missing or empty, it'll show "Related Chapters" instead.
     */
    var title: String? = null

    /**
     * The text to display on this page, under the links. This text can be formatted.
     */
    var text: String? = null

    override fun toJson(): JsonObject {
        return super.toJson().also { json->
            json.add("entries", jsonArray {
                entries.forEach(this@jsonArray::add)
            })
            title?.let {  json.addProperty("title", it) }
            text?.let {  json.addProperty("text", it) }
        }
    }
}