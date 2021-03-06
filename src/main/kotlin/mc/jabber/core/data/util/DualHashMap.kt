/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package mc.jabber.core.data.util

/**
 * A data structure that keeps 2 [HashMap]s updated in parallel
 */
class DualHashMap<KEY, TYPE_A, TYPE_B> {
    val backingOfA: HashMap<KEY, TYPE_A> = hashMapOf()
    val backingOfB: HashMap<KEY, TYPE_B> = hashMapOf()

    @PublishedApi
    internal var lastKeySet: Set<KEY>? = null

    fun isEmpty(): Boolean {
        return backingOfA.isEmpty()
    }

    fun setA(key: KEY, value: TYPE_A) {
        backingOfA[key] = value
    }

    fun setB(key: KEY, value: TYPE_B) {
        backingOfB[key] = value
    }

    fun set(key: KEY, value1: TYPE_A, value2: TYPE_B) {
        setA(key, value1)
        setB(key, value2)
    }

    fun remove(key: KEY) {
        backingOfA.remove(key)
        backingOfB.remove(key)
    }

    fun contains(key: KEY): Boolean {
        return backingOfA.containsKey(key)
    }

    fun clear() {
        backingOfA.clear()
        backingOfB.clear()
    }

    operator fun get(key: KEY): Pair<TYPE_A?, TYPE_B?> = Pair(backingOfA[key], backingOfB[key])

    inline fun forEach(receiver: (KEY, TYPE_A?, TYPE_B?) -> Unit) {
        val set = backingOfA.keys union backingOfB.keys
        lastKeySet = set
        for (key in set) {
            val entry = get(key)
            receiver(key, entry.first, entry.second)
        }
    }

    inline fun any(receiver: (KEY, TYPE_A?, TYPE_B?) -> Boolean): Boolean {
        for (key in backingOfA.keys) {
            val entry = get(key)
            if (receiver(key, entry.first, entry.second)) {
                return true
            }
        }
        return false
    }

    override fun toString(): String {
        return "DualHashMap(" +
                "\n\tbackingA=$backingOfA" +
                "\n\tbackingB=$backingOfB" +
                "\n)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DualHashMap<*, *, *>

        if (backingOfA != other.backingOfA) return false
        if (backingOfB != other.backingOfB) return false
        if (lastKeySet != other.lastKeySet) return false

        return true
    }

    override fun hashCode(): Int {
        var result = backingOfA.hashCode()
        result = 31 * result + backingOfB.hashCode()
        result = 31 * result + (lastKeySet?.hashCode() ?: 0)
        return result
    }
}
