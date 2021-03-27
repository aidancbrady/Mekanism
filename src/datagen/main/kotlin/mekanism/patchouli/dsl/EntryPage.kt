package mekanism.patchouli.dsl

import com.google.gson.JsonObject

@PatchouliDSL
abstract class EntryPage(val type: String) {
    /** A resource location to point at, to make a page appear when that advancement is completed. See Locking Content with Advancements for more info on locking content. Excluding this attribute or leaving it empty will leave the page unlocked from the start. Providing a nonexistent advancement will permanently lock this entry unless the advancement at the resource location starts existing. */
    var advancement: String? = null

    /** A config flag expression that determines whether this page should exist or not. See Using Config Flags for more info on config flags. */
    var flag: String? = null

    /** An anchor can be used elsewhere to refer to this specific page in an internal link. See Text Formatting 101 for more details about internal links. */
    var anchor: String? = null

    @PatchouliDSL
    fun flags(init: FlagsBuilder.()->Unit) {
        this.flag = FlagsBuilder().apply(init).build()
    }

    open fun toJson(): JsonObject {
        val json = JsonObject()
        json.addProperty("type", type)
        json.addProperty("advancement", advancement)
        json.addProperty("flag", flag)
        json.addProperty("anchor", anchor)
        return json
    }
}