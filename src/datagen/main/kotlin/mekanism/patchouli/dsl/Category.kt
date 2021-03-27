package mekanism.patchouli.dsl

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import mekanism.api.providers.IGasProvider
import mekanism.api.providers.IItemProvider
import mekanism.common.registries.MekanismBlocks
import net.minecraft.item.ItemStack

interface ICategory {
    val id: String
    val book: PatchouliBook

    @PatchouliDSL
    fun category(id: String, init: Category.() -> Unit): Category {
        val theParent = this
        return this.book.category(id) {
            parent = theParent
            init()
        }
    }

    @PatchouliDSL
    fun category(guideCat: IGuideCategory, init: Category.() -> Unit) {
        category(guideCat.id, init)
    }

    @PatchouliDSL
    operator fun IGuideCategory.invoke(init: Category.() -> Unit) {
        category(this, init)
    }

    @PatchouliDSL
    fun entry(id: String, init: Entry.() -> Unit): Entry {
        return Entry(id, this).also {
            this.book.entries.add(it)
            it.init()
        }
    }

    @PatchouliDSL
    fun entry(item: IItemProvider, init: Entry.() -> Unit): Entry {
        return entry(item.bookId) {
            icon = item
            init()
        }
    }

    @PatchouliDSL
    operator fun IItemProvider.invoke(spotlightText: String? = null, init: (Entry.() -> Unit)? = null) {
        entry(this.bookId) {
            name = translationKey
            icon = this@invoke
            readByDefault = true//no one wants to have to go through EVERY item in the index...
            spotlight(this@invoke, spotlightText)
            init?.invoke(this)
        }
    }

    @PatchouliDSL
    operator fun IGasProvider.invoke(spotlightText: String? = null, init: (Entry.() -> Unit)) {
        entry(this.bookId) {
            name = translationKey
            icon = MekanismBlocks.ULTIMATE_CHEMICAL_TANK//todo icon or filled
            readByDefault = true//no one wants to have to go through EVERY item in the index...
            init.invoke(this)
        }
    }

    @PatchouliDSL
    operator fun IGuideEntry.invoke(init: Entry.() -> Unit) {
        entry(this.entryId) {
            init()
        }
    }
}

/** Category that doesn't make its own JSON file */
@PatchouliDSL
class DelegateCategory(override val book: PatchouliBook, override val id: String): ICategory

@PatchouliDSL
class Category(override val book: PatchouliBook, override val id: String): ICategory {

    /** mandatory. The name of this category. */
    lateinit var name: String

    /** mandatory. The description for this category. This displays in the category's main page, and can be formatted. */
    lateinit var description: String

    /** mandatory. The icon for this category. This can either be an ItemStack String, if you want an item to be the icon, or a resource location pointing to a square texture. If you want to use a resource location, make sure to end it with .png */
    @SerializedName("icon")
    lateinit var iconStr: String

    /**
     * not directly written to json. Sets the iconStr from the item provider
     */
    var icon: IItemProvider
        get() {
            throw UnsupportedOperationException()
        }
        set(value) {
            iconStr = ItemStackUtils.serializeStack(value.itemStack)
        }

    var iconItem: ItemStack
        get() {
            throw UnsupportedOperationException()
        }
        set(value) {
            iconStr = ItemStackUtils.serializeStack(value)
        }

    /** The parent category to this one. If this is a sub-category, simply put the name of the category this is a child to here. If not, don't define it. Use fully-qualified names including both a namespace and a path. */
    var parent: ICategory? = null

    /** A config flag expression that determines whether this category should exist or not. See Using Config Flags for more info on config flags. */
    var flag: String? = null


    /** The sorting number for this category. Defaults to 0. Categories are sorted in the main page from lowest sorting number to highest, so if you define this in every category you make, you can set what order they display in. */
    var sortNum: Int? = null


    /** Defaults to false. Set this to true to make this category a secret category. Secret categories don't display a locked icon when locked, and instead will not display at all until unlocked. */
    var secret: Boolean? = null

    @PatchouliDSL
    fun flags(init: FlagsBuilder.()->Unit) {
        this.flag = FlagsBuilder().apply(init).build()
    }

    fun toJson(): JsonObject {
        val json = JsonObject()
        json.addProperty("name", name)
        json.addProperty("description", description)
        json.addProperty("icon", iconStr)
        parent?.let {  json.addProperty("parent", it.id) }
        flag?.let {  json.addProperty("flag", it) }
        sortNum?.let {  json.addProperty("sortnum", it) }
        secret?.let {  json.addProperty("secret", it) }
        return json
    }
}