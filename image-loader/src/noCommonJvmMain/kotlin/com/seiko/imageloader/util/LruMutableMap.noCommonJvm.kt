package com.seiko.imageloader.util

/**
 * Create a [MutableMap] that orders its entries by most recently used to least recently used.
 *
 * https://youtrack.jetbrains.com/issue/KT-52183
 */
@Suppress("FunctionName")
internal actual fun <K : Any, V : Any> LruMutableMap(
    initialCapacity: Int,
    loadFactor: Float,
): MutableMap<K, V> {
    return LruMutableMap(LinkedHashMap(initialCapacity, loadFactor))
}

private class LruMutableMap<K : Any, V : Any>(
    private val delegate: MutableMap<K, V>,
) : MutableMap<K, V> by delegate {

    override val entries: MutableSet<MutableMap.MutableEntry<K, V>>
        get() = delegate.entries.mapTo(mutableSetOf(), ::MutableEntry)

    override fun get(key: K): V? {
        // Remove then re-add the item to move it to the top of the insertion order.
        val item = delegate.remove(key)
        if (item != null) {
            delegate[key] = item
        }
        return item
    }

    override fun put(key: K, value: V): V? {
        // Remove then re-add the item to move it to the top of the insertion order.
        val item = delegate.remove(key)
        delegate[key] = value
        return item
    }

    override fun putAll(from: Map<out K, V>) {
        for ((key, value) in from) {
            put(key, value)
        }
    }

    private inner class MutableEntry(
        private val delegate: MutableMap.MutableEntry<K, V>,
    ) : MutableMap.MutableEntry<K, V> by delegate {

        override fun setValue(newValue: V): V {
            val oldValue = delegate.setValue(newValue)
            put(key, value)
            return oldValue
        }
    }
}
