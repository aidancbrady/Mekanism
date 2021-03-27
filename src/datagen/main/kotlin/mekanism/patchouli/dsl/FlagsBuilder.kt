package mekanism.patchouli.dsl

@PatchouliDSL
class FlagsBuilder {
    private var comboType: String? = null

    private lateinit var flags: List<String>

    @PatchouliDSL
    fun single(init: FlagsBuilderInner.()->Unit) {
        comboType = null
        flags = FlagsBuilderInner(false).apply(init).build()
    }

    @PatchouliDSL
    fun and(init: FlagsBuilderInner.()->Unit) {
        comboType = AND
        flags = FlagsBuilderInner(true).apply(init).build()
    }

    @PatchouliDSL
    fun or(init: FlagsBuilderInner.()->Unit) {
        comboType = OR
        flags = FlagsBuilderInner(true).apply(init).build()
    }

    /** Inverts the contained flag */
    @PatchouliDSL
    fun not(init: FlagsBuilderInner.()->Unit) {
        single {
            not(init)
        }
    }

    /**
     * Is true when the mod [modId] is loaded in the game.
     */
    @PatchouliDSL
    fun modLoaded(modId: String) {
        single { modLoaded(modId) }
    }

    /** Is true when Mekanism: Generators is loaded */
    @PatchouliDSL
    fun generatorsInstalled() {
        modLoaded("mekanismgenerators")
    }

    /**  Is true when the game is being loaded from an IDE Debug mode */
    @PatchouliDSL
    fun isDebug() {
        single { isDebug() }
    }

    /** Is true when the "Disable Advancement Locking" option in the Patchouli config is true */
    @PatchouliDSL
    fun advancementsDisabled() {
        single { advancementsDisabled() }
    }

    /** Is true when the "Testing Mode" option in the Patchouli config is true */
    @PatchouliDSL
    fun isTestingMode() {
        single { isTestingMode() }
    }

    internal fun build():String = flags.joinToString(",", comboType ?: "")

    companion object {
        const val AND: String = "&"
        const val OR: String = "|"
        const val NOT: String = "!"
    }
}

@PatchouliDSL
class FlagsBuilderInner(private val multipleAllowed: Boolean) {
    private val flags: MutableList<String> = mutableListOf()

    @Suppress("NOTHING_TO_INLINE")
    private inline fun checkMultiples() {
        if (!multipleAllowed && flags.isNotEmpty()) {
            throw IllegalStateException("Cannot add multiple flags to this configuration. Use And/Or builders at root level")
        }
    }

    /** Inverts the contained flag */
    @PatchouliDSL
    fun not(init: FlagsBuilderInner.()->Unit) {
        flags.add(FlagsBuilder.NOT+(FlagsBuilderInner(false).apply(init).build()))
    }

    /**
     * Is true when the mod [modId] is loaded in the game.
     */
    @PatchouliDSL
    fun modLoaded(modId: String) {
        checkMultiples()
        flags.add("mod:$modId")
    }

    /**  Is true when the game is being loaded from an IDE Debug mode */
    @PatchouliDSL
    fun isDebug() {
        checkMultiples()
        flags.add("debug")
    }

    /** Is true when the "Disable Advancement Locking" option in the Patchouli config is true */
    @PatchouliDSL
    fun advancementsDisabled() {
        checkMultiples()
        flags.add("advancements_disabled")
    }

    /** Is true when the "Testing Mode" option in the Patchouli config is true */
    @PatchouliDSL
    fun isTestingMode() {
        flags.add("testing_mode")
    }


    internal fun build(): List<String> = flags
}