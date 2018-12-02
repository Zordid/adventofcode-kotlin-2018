package shared

fun <S, T : S> Sequence<T>.reduceIntermediate(operation: (acc: S, v: T) -> S): Sequence<S> =
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



