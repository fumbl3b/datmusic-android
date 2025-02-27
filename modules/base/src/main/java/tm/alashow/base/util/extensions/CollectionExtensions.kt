/*
 * Copyright (C) 2019, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.base.util.extensions

import java.util.*

fun <K, V> Map<K, V>.findKeys(target: V) = filterValues { it == target }.keys
fun <K, V> Map<K, V>.findFirstKey(target: V) = findKeys(target).first()

fun <K, V> Map<K, Set<V>>.getOrEmpty(key: K) = getOrElse(key, { emptySet() })

inline fun <T> Iterable<T>.sumByFloat(selector: (T) -> Float): Float {
    var sum = 0.0f
    for (element in this) {
        sum += selector(element)
    }

    return sum
}

fun <T> List<T>.toStack() = Stack<T>().also { stack ->
    forEach { stack.push(it) }
}

fun <T> Stack<T>.popOrNull() = when (isEmpty()) {
    true -> null
    else -> pop()
}

fun <T> List<T>.swap(from: Int, to: Int): List<T> {
    val new = toMutableList()
    val element = new.removeAt(from)
    new.add(to, element)
    return new
}
