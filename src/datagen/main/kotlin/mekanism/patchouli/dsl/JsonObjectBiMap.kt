package mekanism.patchouli.dsl

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import com.google.gson.JsonObject

class JsonObjectBiMap(private val biMap: HashBiMap<String, String> = HashBiMap.create()): BiMap<String, String> by biMap {
    val json = JsonObject()

    override fun put(key: String?, value: String?): String? {
        val retVal = biMap.put(key!!, value!!)
        json.addProperty(key, value)
        return retVal
    }
}