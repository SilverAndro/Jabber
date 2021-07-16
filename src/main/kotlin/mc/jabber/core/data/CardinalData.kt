package mc.jabber.core.data

import kotlinx.serialization.Serializable
import mc.jabber.core.data.serial.LongBox
import mc.jabber.core.data.serial.NbtTransformable
import mc.jabber.core.math.Cardinal
import mc.jabber.util.assertType
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * A sealed class representing 4 stored values, in line with the 4 [Cardinal] directions
 *
 * Note that due to type erasure, most methods take [NbtTransformable<*>], these types are still enforced and cannot actually use any [NbtTransformable<*>]
 */
@Serializable
sealed class CardinalData<T : NbtTransformable<*>>(val up: T?, val down: T?, val left: T?, val right: T?) {
    operator fun get(direction: Cardinal): T? {
        return when (direction) {
            Cardinal.UP -> up
            Cardinal.DOWN -> down
            Cardinal.LEFT -> left
            Cardinal.RIGHT -> right
        }
    }

    /**
     * Gets random data
     *
     * @return null if all values are null, or a random value from the non-null ones
     */
    fun acquire(): Pair<Cardinal, T>? {
        val dirs = Cardinal.values()
        dirs.shuffle()
        dirs.forEach {
            val got = get(it)
            if (got != null) return it to got
        }
        return null
    }

    /**
     * Simple transform
     *
     * @return A new [CardinalData] of the same type, but with the [direction] replaced with [value]
     */
    fun with(direction: Cardinal, value: NbtTransformable<*>?): CardinalData<T> {
        return when (direction) {
            Cardinal.UP -> of(value, down, left, right)
            Cardinal.DOWN -> of(up, value, left, right)
            Cardinal.LEFT -> of(up, down, value, right)
            Cardinal.RIGHT -> of(up, down, left, value)
        }
    }

    /**
     * Data separation/extraction
     *
     * @return A new [CardinalData] with only the value specified with [direction]
     */
    fun only(direction: Cardinal): CardinalData<T> {
        return when (direction) {
            Cardinal.UP -> of(up, null, null, null)
            Cardinal.DOWN -> of(null, down, null, null)
            Cardinal.LEFT -> of(null, null, left, null)
            Cardinal.RIGHT -> of(null, null, null, right)
        }
    }

    /**
     * Makes a new [CardinalData] with all the specified values
     */
    fun of(
        up: NbtTransformable<*>?,
        down: NbtTransformable<*>?,
        left: NbtTransformable<*>?,
        right: NbtTransformable<*>?
    ): CardinalData<T> {
        return when (this) {
            is ComputeData -> ComputeData(
                up as LongBox?,
                down as LongBox?,
                left as LongBox?,
                right as LongBox?
            ).assertType()
        }
    }

    /**
     * Makes an empty form of the data
     */
    fun empty() = ofAll(null)

    /**
     * Creates a new [CardinalData] where all values are [value]
     */
    fun ofAll(value: NbtTransformable<*>?): CardinalData<T> {
        return of(value, value, value, value)
    }

    /**
     * Iterates all values provided, so your method will be called 4 times
     */
    @OptIn(ExperimentalContracts::class)
    inline fun forEach(method: (Cardinal, T?) -> Unit) {
        contract {
            callsInPlace(method, InvocationKind.AT_LEAST_ONCE)
        }

        method(Cardinal.UP, up)
        method(Cardinal.DOWN, down)
        method(Cardinal.LEFT, left)
        method(Cardinal.RIGHT, right)
    }

    override fun toString(): String {
        return this::class.simpleName + "(up=$up, down=$down, left=$left, right=$right)"
    }
}

class ComputeData(up: LongBox?, down: LongBox?, left: LongBox?, right: LongBox?) :
    CardinalData<LongBox>(up, down, left, right)
