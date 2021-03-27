package mekanism.patchouli.dsl

import com.google.gson.GsonBuilder
import com.mojang.serialization.Dynamic
import com.mojang.serialization.JsonOps
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTDynamicOps
import java.util.*

object ItemStackUtils {
    private val GSON = GsonBuilder().create()

    /**
     * From [vazkii.patchouli.common.util.ItemStackUtil.serializeStack]
     * Class used to be in API, but was moved out.
     *
     * @param stack Itemstack to serialise
     * @return the serialised stack
     */
    fun serializeStack(stack: ItemStack): String {
        val builder = StringBuilder()
        builder.append(Objects.requireNonNull(stack.item.registryName, "Unregistered stack").toString())
        val count = stack.count
        if (count > 1) {
            builder.append("#")
            builder.append(count)
        }
        if (stack.hasTag()) {
            val dyn: Dynamic<*> = Dynamic(NBTDynamicOps.INSTANCE, stack.tag)
            val j = dyn.convert(JsonOps.INSTANCE).value
            builder.append(GSON.toJson(j))
        }
        return builder.toString()
    }
}