package mekanism.patchouli.dsl

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import net.minecraft.nbt.CompoundNBT
import net.minecraft.util.ResourceLocation

class EntityPage: EntryPage("entity") {
    /**
     * The ID of the entity you want to display. To display a chicken you'd use "minecraft:chicken". You can also add NBT data to the entity, in the same way you would in an ItemStack String.
     */
    lateinit var entity: ResourceLocation

    var nbt: CompoundNBT? = null

    /**
     * The scale to display the entity at. Defaults to 1.0. Values lower than 1.0 will have the entity be smaller than usual, while higher than 1.0 will have it be larger. Negative values will flip it upside down.
     */
    var scale: Float? = null

    /**
     * An amount to offset the entity display. Some mod entities have weird renders and won't fit in the box properly, you can change this to move them up and down.
     */
    var offset: Float? = null

    /**
     * Defaults to true. Set this to false to make the entity not rotate.
     */
    var rotate: Boolean? = null

    /**
     * The rotation at which this entity should be rendered. This value is only used if "rotate" is false. The default is -45.
     */
    @SerializedName("default_rotation")
    var defaultRotation: Float? = null

    /**
     * The name to display on top of the frame. If this is empty or not defined, it'll grab the name of the entity and use that instead.
     */
    var name: String? = null

    /**
     * The text to display on this page, under the entity. This text can be formatted.
     */
    var text: String? = null

    override fun toJson(): JsonObject {
        return super.toJson().also { json->
            json.addProperty("entity", entity.toString() + if (nbt != null) nbt.toString() else "")
            scale?.let {  json.addProperty("scale", it) }
            offset?.let {  json.addProperty("offset", it) }
            rotate?.let {  json.addProperty("rotate", it) }
            defaultRotation?.let {  json.addProperty("default_rotation", it) }
            name?.let {  json.addProperty("name", it) }
            text?.let {  json.addProperty("text", it) }
        }
    }
}