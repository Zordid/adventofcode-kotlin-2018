package shared

inline fun <S, T : S> Sequence<T>.reduceIntermediate(crossinline operation: (acc: S, v: T) -> S): Sequence<S> =
    sequence {
        val iterator = this@reduceIntermediate.iterator()
        if (!iterator.hasNext()) throw UnsupportedOperationException("Empty sequence can't be reduced.")
        var accumulator: S = iterator.next()
        yield(accumulator)
        while (iterator.hasNext()) {
            accumulator = operation(accumulator, iterator.next())
            yield(accumulator)
        }
    }

inline fun <T, R : Comparable<R>> Sequence<T>.minByIfUnique(selector: (T) -> R): T? {
    val iterator = iterator()
    if (!iterator.hasNext()) return null
    var minElem = iterator.next()
    var minValue = selector(minElem)
    while (iterator.hasNext()) {
        val e = iterator.next()
        val v = selector(e)
        if (minValue == v) {
            return null
        }
        if (minValue > v) {
            minElem = e
            minValue = v
        }
    }
    return minElem
}

inline fun <T, R : Comparable<R>> Iterable<T>.minByIfUnique(selector: (T) -> R): T? {
    val iterator = iterator()
    if (!iterator.hasNext()) return null
    var minElem = iterator.next()
    var minValue = selector(minElem)
    while (iterator.hasNext()) {
        val e = iterator.next()
        val v = selector(e)
        if (minValue == v)
            return null
        if (minValue > v) {
            minElem = e
            minValue = v
        }
    }
    return minElem
}



