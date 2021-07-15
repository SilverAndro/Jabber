package mc.jabber.util

import net.minecraft.util.registry.Registry
import java.util.*

/**
 * Implementation of the recommend replacement for kotlin's stdlib capitalize
 * @return The string but with the first character in title case
 */
fun String.capitalize(): String {
    return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}

/**
 * Allows for a simple transform from [other] to [this] through ID
 */
fun <A, B> Registry<A>.idFlip(other: Registry<B>, instance: B): A? {
    return this.get(other.getId(instance))
}
