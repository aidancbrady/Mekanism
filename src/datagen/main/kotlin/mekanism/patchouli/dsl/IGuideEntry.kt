package mekanism.patchouli.dsl

import java.util.*

interface IGuideEntry {
    val entryId: String

    companion object {
        fun generate(folder: String?, name: String): String = (if (folder != null) "$folder/" else "") + name.toLowerCase(Locale.ROOT)
    }
}