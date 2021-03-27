package mekanism.patchouli.dsl

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import mekanism.api.providers.IItemProvider
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation

@PatchouliDSL
class Entry(
        val id: String,
        /** mandatory. The category this entry belongs to. This must be set to one of your categories' ID. For best results, use a fully-qualified ID that includes your book namespace yourbooknamespace:categoryname. In the future this will be enforced. */
        val category: ICategory
) {
    /** mandatory. The name of this entry. */
    lateinit var name: String

    /** mandatory. The icon for this entry. This can either be an ItemStack String, if you want an item to be the icon, or a resource location pointing to a square texture. If you want to use a resource location, make sure to end it with .png */
    @SerializedName("icon")
    lateinit var iconStr: String

    @PatchouliDSL
    fun icon(stack: ItemStack){
        iconStr = ItemStackUtils.serializeStack(stack)
    }

    /**
     * Not directly written to json. Sets the iconStr from the item provider
     * Gettable so we can see which items have an entry and which don't
     */
    var icon: IItemProvider? = null
        set(value) {
            field = value!!
            iconStr = ItemStackUtils.serializeStack(value.itemStack)
        }

    var iconItem: ItemStack
        get() {
            throw UnsupportedOperationException()
        }
        set(value) {
            iconStr = ItemStackUtils.serializeStack(value)
        }

    /** mandatory. The array of pages for this entry. */
    var pages: MutableList<EntryPage> = mutableListOf()

    /** The name of the advancement you want this entry to be locked behind. See Locking Content with Advancements for more info on locking content. */
    var advancement: String? = null

    /** A config flag expression that determines whether this entry should exist or not. See Using Config Flags for more info on config flags. */
    var flag: String? = null

    /** Defaults to false. If set to true, the entry will show up with an italicized name, and will always show up at the top of the category. Use for really important entries you want to show up at the top. */
    var priority: Boolean? = null

    /** Defaults to false. Set this to true to make this entry a secret entry. Secret entries don't display as "Locked" when locked, and instead will not display at all until unlocked. Secret entries do not count for % completion of the book, and when unlocked will instead show as an additional line in the tooltip. */
    var secret: Boolean? = null

    /** Defaults to false. Set this to true if you want to entry to not show the unread ("(!!)") indicator if it hasn't been opened yet. */
    @SerializedName("read_by_default")
    var readByDefault: Boolean? = null

    /**
     * The sorting number for this entry. Defaults to 0. Entries with the same sorting number are sorted alphabetically,
     * whereas entries with different sorting numbers are sorted from lowest to highest. Priority entries always show up first.
     * It's recommended you do NOT use this, as breaking the alphabetical sorting order can make things confusing, but it's left as an option.
     */
    var sortnum: Int? = null

    /** The ID of an advancement the player needs to do to "complete" this entry. The entry will show up at the top of the list with a (?) icon next to it until this advancement is complete. This can be used as a quest system or simply to help guide players along a starting path. */
    var turnin: String? = null

    /**
     * Additional list of items this page teaches the crafting process for,
     * for use with the in-world right click and quick lookup feature.
     * Keys are ItemStack strings, values are 0-indexed page numbers.
     */
    @SerializedName("extra_recipe_mappings")
    var extraRecipeMappings: MutableMap<String, Int> = mutableMapOf()

    @PatchouliDSL
    fun extraMapping(item: ItemStack, page: Int) {
        extraRecipeMappings[ItemStackUtils.serializeStack(item)] = page
    }

    @PatchouliDSL
    fun text(init: TextPage.() -> Unit) {
        this.pages.add(TextPage().also(init))
    }

    @PatchouliDSL
    fun text(title: String, init: TextPage.() -> Unit) {
        this.pages.add(TextPage().also {
            it.title = title
            it.init()
        })
    }

    @PatchouliDSL
    operator fun String.unaryPlus(){
        text { text = this@unaryPlus }
    }

    @PatchouliDSL
    fun image(init: ImagePage.() -> Unit) {
        this.pages.add(ImagePage().also(init))
    }

    @PatchouliDSL
    fun crafting(recipe: ResourceLocation, init: (CraftingPage.() -> Unit)? = null) {
        this.pages.add(CraftingPage(recipe).also { page->
            init?.let { init(page) }
        })
    }

    @PatchouliDSL
    fun crafting(result: IItemProvider, init: (CraftingPage.() -> Unit)? = null) {
        crafting(result.registryName, init)
    }

    @PatchouliDSL
    fun smelting(recipe: ResourceLocation, init: SmeltingPage.() -> Unit) {
        this.pages.add(SmeltingPage(recipe).also(init))
    }

    @PatchouliDSL
    fun multiblock(init: MultiblockPage.() -> Unit) {
        this.pages.add(MultiblockPage().also(init))
    }

    @PatchouliDSL
    fun entity(init: EntityPage.() -> Unit) {
        this.pages.add(EntityPage().also(init))
    }

    @PatchouliDSL
    fun spotlight(init: SpotlightPage.() -> Unit) {
        this.pages.add(SpotlightPage().also(init))
    }

    @PatchouliDSL
    fun spotlight(item: IItemProvider, spotlightText: String? = null) {
        spotlight {
            this.item = item.itemStack
            linkRecipe = true
            text = spotlightText
        }
    }

    @PatchouliDSL
    fun linkPage(init: LinkPage.() -> Unit) {
        this.pages.add(LinkPage().also(init))
    }

    @PatchouliDSL
    fun relations(init: RelationsPage.() -> Unit) {
        this.pages.add(RelationsPage().also(init))
    }

    @PatchouliDSL
    fun quest(init: QuestPage.() -> Unit) {
        this.pages.add(QuestPage().also(init))
    }

    @PatchouliDSL
    fun emptyPage(init: EmptyPage.() -> Unit) {
        this.pages.add(EmptyPage().also(init))
    }

    @PatchouliDSL
    fun emptyPage() {
        this.pages.add(EmptyPage())
    }

    @PatchouliDSL
    fun flags(init: FlagsBuilder.()->Unit) {
        this.flag = FlagsBuilder().apply(init).build()
    }

    fun toJson(): JsonObject {
        val json = JsonObject()
        json.addProperty("name", name)
        json.addProperty("category", category.id)
        if (!this::iconStr.isInitialized){
            throw RuntimeException("$id doesnt have an icon set")
        }
        json.addProperty("icon", iconStr)
        val pages = JsonArray()
        for (page in this.pages) {
            pages.add(page.toJson())
        }
        json.add("pages", pages)
        advancement?.let {  json.addProperty("advancement", it) }
        flag?.let {  json.addProperty("flag", it) }
        priority?.let {  json.addProperty("priority", it) }
        secret?.let {  json.addProperty("secret", it) }
        readByDefault?.let {  json.addProperty("read_by_default", it) }
        sortnum?.let {  json.addProperty("sortnum", it) }
        turnin?.let {  json.addProperty("turnin", it) }
        if (extraRecipeMappings.isNotEmpty()) {
            val mappings = JsonObject()
            for ((key, value) in extraRecipeMappings) {
                mappings.addProperty(key, value)
            }
            json.add("extra_recipe_mappings", mappings)
        }
        return json
    }
}