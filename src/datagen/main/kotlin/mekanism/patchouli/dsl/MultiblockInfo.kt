package mekanism.patchouli.dsl

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import mekanism.common.block.attribute.Attribute
import mekanism.common.block.attribute.AttributeStateFacing
import mekanism.common.registration.impl.BlockRegistryObject
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.item.Item
import net.minecraft.state.StateHolder
import net.minecraft.tags.ITag
import net.minecraft.util.Direction
import java.util.*

class MultiblockInfo {
    private val pattern: MutableList<MultiblockLayer> = mutableListOf()

    private val patternToValue: JsonObjectBiMap = JsonObjectBiMap()
    private val valueToPattern = patternToValue.inverse()
    private var zeroValue: String? = null
        set(value) {
            if (field == null) {
                field = value!!
                patternToValue.json.addProperty("0", value)//nb: cant put in Map because of BiMap
            } else
                throw IllegalStateException("Zero value was already set")
        }

    //I pity the fool who needs more than 26 pattern keys...
    private val availablePatternItem: Queue<String> = ArrayDeque("ABCDEFGHIJKLMNOPQRSTUVWXYZ@#$%^&*123456789".map { it.toString() }.toList())
    private fun getMapping(value: String): String {
        if (valueToPattern.containsKey(value)) {
            return valueToPattern[value]!!
        }
        val pattern = availablePatternItem.remove()!!
        patternToValue[pattern] = value
        return pattern
    }

    var symmetrical: Boolean? = null
    private var offset: JsonArray? = null
    fun offset(x: Int, y: Int, z: Int) {
        this.offset = jsonArray {
            add(x)
            add(y)
            add(z)
        }
    }

    @PatchouliDSL
    fun layer(block: MultiblockLayer.()->Unit) {
        this.pattern.add(MultiblockLayer().apply(block))
    }

    fun toJson(): JsonObject {
        val json = JsonObject()
        json.add("pattern", jsonArray {
            pattern.forEach { layer ->
                this@jsonArray.add(layer.toJson())
            }
        })
        json.add("mapping", patternToValue.json)

        symmetrical?.let { json.addProperty("symmetrical", it) }
        offset?.let { json.add("offset", it) }
        return json
    }

    inner class MultiblockLayer {
        private val layerRows: MutableList<MultiblockRow> = mutableListOf()

        @PatchouliDSL
        fun row(block: MultiblockRow.()->Unit) {
            layerRows.add(MultiblockRow().apply(block))
        }

        fun toJson(): JsonArray {
            val layerJson = JsonArray()
            layerRows.forEach {
                layerJson.add(it.toJson())
            }
            return layerJson
        }
    }

    inner class MultiblockRow {
        private val column: MutableList<String> = mutableListOf()

        @PatchouliDSL
        operator fun BlockState.unaryPlus() {
            column.add(this.toString())
        }

        private fun BlockRegistryObject<out Block, out Item>.toPattern(): String {
            return block.block.registryName!!.toString()
        }

        @PatchouliDSL
        operator fun BlockRegistryObject<out Block, out Item>.unaryPlus() {
            column.add(toPattern())
        }

        @PatchouliDSL
        infix fun BlockRegistryObject<out Block, out Item>.facing(direction: Direction) {
            column.add(blockStatePattern(this){
                Attribute.get(this, AttributeStateFacing::class.java).setDirection(it, direction)
            })
        }

        private fun BlockState.toPattern(): String {
            return if (values.isNotEmpty()) {
                this.block.registryName!!.toString()+values.entries.joinToString(separator = ",", prefix = "[", postfix = "]", transform = StateHolder.field_235890_a_::apply)//+"["+(this.toString().substringAfter("["))
            } else {
                this.block.registryName!!.toString()
            }
        }

        private fun <BLOCK: Block> blockStatePattern(registryObject: BlockRegistryObject<BLOCK, out Item>, stateProvider: BLOCK.(defaultState: BlockState)-> BlockState): String {
            return stateProvider(registryObject.block, registryObject.block.defaultState).toPattern()
        }

        @PatchouliDSL
        operator fun <BLOCK: Block> BlockRegistryObject<BLOCK, out Item>.invoke(stateProvider: BLOCK.(defaultState: BlockState)-> BlockState) {
            column.add(blockStatePattern(this, stateProvider))
        }

        @PatchouliDSL
        operator fun ITag.INamedTag<Block>.unaryPlus() {
            column.add(toPattern())
        }

        private fun ITag.INamedTag<Block>.toPattern() = "#$name"

        @PatchouliDSL
        fun space() {
            column.add(" ")
        }

        @PatchouliDSL
        fun center() {
            column.add("0")
        }

        @PatchouliDSL
        fun <BLOCK: Block> center(blockRegistryObject: BlockRegistryObject<BLOCK, Item>, stateProvider: BLOCK.(defaultState: BlockState)-> BlockState) {
            center()
            zeroValue = blockStatePattern(blockRegistryObject, stateProvider)
        }

        @PatchouliDSL
        fun center(block: BlockRegistryObject<out Block, out Item>) {
            center()
            zeroValue = block.toPattern()
        }

        @PatchouliDSL
        fun center(tag: ITag.INamedTag<Block>) {
            center()
            zeroValue = tag.toPattern()
        }

        fun toJson(): String {
            return column.joinToString(separator = "") { item ->
                when (item) {
                    " ", "0" -> item
                    else -> getMapping(item)
                }
            }
        }
    }
}