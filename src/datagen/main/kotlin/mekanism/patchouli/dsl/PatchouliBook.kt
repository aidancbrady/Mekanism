package mekanism.patchouli.dsl

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation

@PatchouliDSL
class PatchouliBook(val id: ResourceLocation) {
    /** Mandatory. The name of the book that will be displayed in the book item and the GUI. For modders, this can be a localization key. */
    lateinit var name: String

    /** Mandatory.The text that will be displayed in the landing page of your book. This text can be formatted. For modders, this can be a localization key.*/
    @SerializedName("landing_text")
    lateinit var landingText: String

    /**
     * The texture for the background of the book GUI. You can use any resource location for this, but it is highly recommended
     * you use one of the built in ones so that if new elements get added, you have them right away.
     */
    @SerializedName("book_texture")
    var bookTexture: ResourceLocation? = null

    /**
     * The texture for the page filler (the cube thing that shows up on entries with an odd number of pages). Define if you want something else than the cube to fill your empty pages.
     */
    @SerializedName("filler_texture")
    var fillerTexture: ResourceLocation? = null

    /**
     * The texture for the crafting entry elements. Define if you want custom backdrops for these. Not really worth defining in most cases but if you want to be cool and stylish, you can.
     */
    @SerializedName("crafting_texture")
    var craftingTexture: ResourceLocation? = null

    /**
     * The model for the book's item. This can be any standard item model you define. Patchouli provides a few you can use:
     *
     * patchouli:book_blue
     * patchouli:book_brown (default value)
     * patchouli:book_cyan
     * patchouli:book_gray
     * patchouli:book_green
     * patchouli:book_purple
     * patchouli:book_red
     *
     * FOR MODDERS: Do NOT use any of the above models.
     * In the sake of books being distinguishable, having multiple mods using the same base textures is a bad idea, make your own!
     * They're provided for modpack makers.
     *
     * Patchouli automatically takes care of registering any models you pass in to this, so you don't have to mess with any code for them yourself.
     *
     * A property called "completion" is provided for book models, where its value is equal to the fraction of entries unlocked in the book,
     * which allows book models to change their display as they're more completed.
     */
    @SerializedName("model")
    var model: ResourceLocation? = null

    /**
     * The color of regular text, in hex ("RRGGBB", # not necessary). Defaults to "000000".
     */
    @SerializedName("text_color")
    var textColor: String? = null

    /**
     * The color of header text, in hex ("RRGGBB", # not necessary). Defaults to "333333".
     */
    @SerializedName("header_color")
    var headerColor: String? = null

    /**
     * The color of the book nameplate in the landing page, in hex ("RRGGBB", # not necessary). Defaults to "FFDD00".
     */
    @SerializedName("nameplate_color")
    var nameplateColor: String? = null

    /**
     * The color of link text, in hex ("RRGGBB", # not necessary). Defaults to "0000EE".
     */
    @SerializedName("link_color")
    var linkColor: String? = null

    /**
     * The color of hovered link text, in hex ("RRGGBB", # not necessary). Defaults to "8800EE".
     */
    @SerializedName("link_hover_color")
    var linkHoverColor: String? = null

    /**
     * The color of advancement progress bar, in hex ("RRGGBB", # not necessary). Defaults to "FFFF55".
     */
    @SerializedName("progress_bar_color")
    var progressBarColor: String? = null

    /**
     * The color of advancement progress bar's background, in hex ("RRGGBB", # not necessary). Defaults to "DDDDDD".
     */
    @SerializedName("progress_bar_background")
    var progressBarBackground: String? = null

    /**
     * The sound effect played when opening this book. This is a resource location pointing to the sound (which, for mooders, needs to be properly registered).
     */
    @SerializedName("open_sound")
    var openSound: ResourceLocation? = null

    /**
     * The sound effect played when flipping through pages in this book. This is a resource location pointing to the sound (which, for mooders, needs to be properly registered).
     */
    @SerializedName("flip_sound")
    var flipSound: ResourceLocation? = null

    /**
     * The icon to display for the Book Index. This can either be an ItemStack String, if you want an item to be the icon, or a resource location pointing to a square texture. If you want to use a resource location, make sure to end it with .png. This is optional, and if you don't include it, it'll default to the book's icon (which is the recommended value).
     */
    @SerializedName("index_icon")
    var indexIcon: ResourceLocation? = null

    /**
     * Defaults to true. Set to false to disable the advancement progress bar, even if advancements are enabled.
     */
    @SerializedName("show_progress")
    var showProgress: Boolean? = null

    /**
     * The "edition" of the book. This defaults to "0", and setting this to any other numerical value (let's call it X)
     * will display "X Edition" in the book's tooltip and landing page (e.g. X being 3 would display "3rd Edition").
     * Setting this to "0" or not modifying it will instead display whatever you set the "subtitle" key as.
     * If the value is non-numerical, it'll display "Writer's Edition".
     *
     * For modders. This is a good place you can inflate with gradle.
     * You can use something like ${book_version} here and set it your build script. As non-numerical values are accepted, this won't cause any issues.
     */
    var version: String? = null

    /**
     * A subtitle for your book, which will display in the tooltip and below the book name in the landing page if "version" is set to "0" or not set.
     */
    var subtitle: String? = null

    /**
     * The creative tab to display your book in. This defaults to Miscellaneous, but you can move it to any tab you wish.
     *
     * Here are the names for the vanilla tabs:
     *      buildingBlocks
     *      decorations
     *      redstone
     *      transportation
     *      misc (default value)
     *      food
     *      tools
     *      combat
     *      brewing
     *
     * For modders, simply put in the same string you use when constructing your creative tab here, and the book will show up there.
     */
    @SerializedName("creative_tab")
    var creativeTab: ItemGroup? = null

    /**
     * The name of the advancements tab you want this book to be associated to. If defined, an Advancements button will show up in the landing page that will open that tab.
     */
    @SerializedName("advancements_tab")
    var advancementsTab: ResourceLocation? = null

    /**
     * Defaults to false. Set this to true if you don't want Patchouli to make a book item for your book. Use only if you're a modder and you really need a custom Item class for whatever reason.
     */
    @SerializedName("dont_generate_book")
    var dontGenerateBook: Boolean? = null

    /**
     * Following from the previous key, if you do have a custom book, set it here. This is an ItemStack String.
     */
    @SerializedName("custom_book_item")
    var customBookItem: ItemStack? = null

    /**
     * Defaults to true. Set it to false if you don't want your book to show toast notifications when new entries are available.
     */
    @SerializedName("show_toasts")
    var showToasts: Boolean? = null

    /**
     * Defaults to false. Set it to true to use the vanilla blocky font rather than the slim font. If you have a font mod, it'll use whichever font that mod is providing instead.
     */
    @SerializedName("use_blocky_font")
    var useBlockyFont: Boolean? = null

    /**
     * Default false. If set to true, attempts to look up category, entry, and page titles as well as any page text in the lang files before rendering.
     */
    var i18n: Boolean? = null

    /**
     * Default true. When set true, opening any GUI from this book will pause the game in singleplayer.
     */
    @SerializedName("pause_game")
    var pauseGame: Boolean? = null

    /**
     * Define this to set this book as an extension of another. Extension books do not create a book item and don't really "exist".
     * All they serve to do is to add more content to another book that already exists.
     * This is mainly here for addon mods that want to add stuff to the book they're extending.
     *
     * The value you put here is simply the ID of the book you want to extend.
     * In the form of "modid:path", where modid is the ID of the mod that owns the book, and path the folder where it is.
     * For example, should you want to extend the book owned by "patchouli" that's in "/data/patchouli/patchouli_books/coolbook/book.json",
     * the value you'd put here would be "patchouli:coolbook".
     *
     * If this value is set, every single other value in the file is ignored.
     * This book will inherit any entries, categories, templates, and macros from the original one, so feel free to use them at will.
     */
    var extend: ResourceLocation? = null

    fun extend(modId: String, bookId: String) {
        this.extend = ResourceLocation(modId, bookId)
    }

    /**
     * Defaults to true. Set it to false if you want to not play nice and lock your book from being extended by other books.
     */
    @SerializedName("allow_extensions")
    var allowExtensions: Boolean? = null

    val macros: MutableMap<String, String> = mutableMapOf()

    var locale: String = "en_us"

    fun toJson(): JsonObject {
        val json = JsonObject()
        if (extend != null) {
            json.addProperty("extend", extend!!)
        } else {
            json.addProperty("name", name)
            json.addProperty("landing_text", landingText)
            bookTexture?.let { json.addProperty("book_texture", it) }
            fillerTexture?.let { json.addProperty("filler_texture", it) }
            craftingTexture?.let { json.addProperty("crafting_texture", it) }
            model?.let { json.addProperty("model", it) }
            textColor?.let { json.addProperty("text_color", it) }
            headerColor?.let { json.addProperty("header_color", it) }
            nameplateColor?.let { json.addProperty("nameplate_color", it) }
            linkColor?.let { json.addProperty("link_color", it) }
            linkHoverColor?.let { json.addProperty("link_hover_color", it) }
            progressBarColor?.let { json.addProperty("progress_bar_color", it) }
            progressBarBackground?.let { json.addProperty("progress_bar_background", it) }
            openSound?.let { json.addProperty("open_sound", it) }
            flipSound?.let { json.addProperty("flip_sound", it) }
            indexIcon?.let { json.addProperty("index_icon", it) }
            showProgress?.let { json.addProperty("show_progress", it) }
            version?.let { json.addProperty("version", it) }
            subtitle?.let { json.addProperty("subtitle", it) }
            creativeTab?.let { json.addProperty("creative_tab", it.path) }
            advancementsTab?.let { json.addProperty("advancements_tab", it) }
            dontGenerateBook?.let { json.addProperty("dont_generate_book", it) }
            customBookItem?.let { json.addProperty("custom_book_item", it) }
            showToasts?.let { json.addProperty("show_toasts", it) }
            useBlockyFont?.let { json.addProperty("use_blocky_font", it) }
            i18n?.let { json.addProperty("i18n", it) }
            pauseGame?.let { json.addProperty("pause_game", it) }
            allowExtensions?.let { json.addProperty("allow_extensions", it) }
        }
        if (macros.isNotEmpty()) {
            json.add("macros", jsonObject {
                macros.forEach { (key, value) -> addProperty(key, value) }
            })
        }
        return json
    }

    internal val categories: MutableList<Category> = mutableListOf()
    internal val entries: MutableList<Entry> = mutableListOf()

    @PatchouliDSL
    fun category(id: String, init: Category.() -> Unit): Category {
        return Category(this, id).also {
            categories.add(it)
            it.init()
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
    fun existingCategory(guideCat: IGuideCategory, init: DelegateCategory.() -> Unit): ICategory {
        return DelegateCategory(this, guideCat.id).also {
            it.init()
        }
    }

    companion object {
        val BOOK_BLUE = ResourceLocation("patchouli", "textures/gui/book_blue.png")
        val BOOK_BROWN = ResourceLocation("patchouli", "textures/gui/book_brown.png")// (default value)
        val BOOK_CYAN = ResourceLocation("patchouli", "textures/gui/book_cyan.png")
        val BOOK_GRAY = ResourceLocation("patchouli", "textures/gui/book_gray.png")
        val BOOK_GREEN = ResourceLocation("patchouli", "textures/gui/book_green.png")
        val BOOK_PURPLE = ResourceLocation("patchouli", "textures/gui/book_purple.png")
        val BOOK_RED = ResourceLocation("patchouli", "textures/gui/book_red.png")
    }
}

@PatchouliDSL
fun patchouliBook(modId: String, bookId: String, init: PatchouliBook.() -> Unit): PatchouliBook {
    return PatchouliBook(ResourceLocation(modId, bookId)).also(init)
}