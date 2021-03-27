package mekanism.patchouli

import mekanism.patchouli.dsl.IGuideEntry
import java.util.HashSet

private val UNIQUE_CACHE: MutableSet<String> = HashSet()

enum class GeneratorsGuideEntry(folder: String?, name: String): IGuideEntry {
    FUSION("multiblocks","fusion"),
    FISSION("multiblocks", "fission"),
    TURBINE("multiblocks", "turbine")
    ;

    override val entryId: String = IGuideEntry.generate(folder, name)

    init {
        require(UNIQUE_CACHE.add(entryId)) { "Duplicate page id: $entryId" }
    }

    constructor(name: String) : this(null, name)
}