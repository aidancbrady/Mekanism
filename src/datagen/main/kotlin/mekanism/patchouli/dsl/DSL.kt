@file:Suppress("MemberVisibilityCanBePrivate", "unused")//its an api

package mekanism.patchouli.dsl

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import mekanism.api.providers.IBlockProvider
import mekanism.api.providers.IGasProvider
import mekanism.api.providers.IItemProvider
import mekanism.common.content.gear.Modules
import net.minecraft.client.settings.KeyBinding
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import org.apache.logging.log4j.LogManager

fun JsonObject.addProperty(name: String, res: ResourceLocation) {
    addProperty(name, res.toString())
}

fun JsonObject.addProperty(name: String, item: ItemStack) {
    addProperty(name, ItemStackUtils.serializeStack(item))
}

fun jsonObject(receiver: JsonObject.() -> Unit): JsonObject {
    return JsonObject().also(receiver)
}

fun jsonArray(receiver: JsonArray.() -> Unit): JsonArray {
    return JsonArray().apply(receiver)
}

fun Array<String>.toJsonArray():JsonArray = JsonArray().also { array->
    this.forEach {
        array.add(it)
    }
}

fun Array<Number>.toJsonArray():JsonArray = JsonArray().also { array->
    this.forEach {
        array.add(it)
    }
}
fun Array<Int>.toJsonArray():JsonArray = JsonArray().also { array->
    this.forEach {
        array.add(it)
    }
}

val IItemProvider.bookId: String get() {
    val type = if (this is IBlockProvider) "block" else "item"
    return type + "/" + this.registryName.path
}

val IGasProvider.bookId: String get() = "gas/" + this.registryName.path

val Modules.ModuleData<*>.bookId: String get() = "items/modules/"+this.stack.item.registryName!!

private fun link(id:String, text:String): String = "$(l:${id})${text}$(/l)"

@PatchouliDSL
fun link(item: IItemProvider, text: String): String = link(item.bookId, text)

@PatchouliDSL
fun link(item: IGasProvider, text: String): String = link(item.bookId, text)

@PatchouliDSL
fun link(guideEntry: IGuideEntry, text: String): String = link(guideEntry.entryId, text)

@PatchouliDSL
fun link(module: Modules.ModuleData<*>, text: String): String = link(module.bookId, text)

operator fun KeyBinding.invoke(): String {
    return "$(k:${keyDescription})"
}

val LOGGER = LogManager.getLogger("PatchouliDSL")!!

@DslMarker
annotation class PatchouliDSL