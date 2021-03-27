package mekanism.patchouli.dsl

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import net.minecraft.util.ResourceLocation

class MultiblockPage: EntryPage("multiblock"){
    /** The name of the multiblock you're displaying. Shows as a header above the multiblock display. */
    lateinit var name: String

    /**
     * For modders only. The ID of the multiblock you want to display. See API Usage for how to create and register Multiblocks in code.
     * Note: Either this or "multiblock" need to be set for this page type to work.
     */
    @SerializedName("multiblock_id")
    var multiblockId: ResourceLocation? = null
        set(value) {
            if (multiblock != null && value != null)
                throw IllegalStateException("can't set both multiblock and multiblock_id")
            field = value
        }

    private var multiblock: MultiblockInfo? = null
        set(value) {
            if (multiblockId != null && value != null)
                throw IllegalStateException("can't set both multiblock and multiblock_id")
            field = value
        }

    /**
     * The multiblock object to display. See Using Multiblocks for how to create this object.
     * Note: Either this or "multiblock_id" need to be set for this page type to work.
     */
    @PatchouliDSL
    fun definition(init: MultiblockInfo.() -> Unit) {
        multiblock = MultiblockInfo().also(init)
    }

    /** Defaults to true. Set this to false to disable the "Visualize" button. */
    @SerializedName("enable_visualize")
    var enableVisualize: Boolean? = null

    /** The text to display on this page, under the multiblock. This text can be formatted. */
    var text: String? = null

    override fun toJson(): JsonObject {
        return super.toJson().also { json ->
            json.addProperty("name", name)
            multiblockId?.let { json.addProperty("multiblock_id", it) }
            multiblock?.let { json.add("multiblock", it.toJson()) }
            enableVisualize?.let { json.addProperty("enable_visualize", it)}
            text?.let {  json.addProperty("text", it) }
        }
    }
}