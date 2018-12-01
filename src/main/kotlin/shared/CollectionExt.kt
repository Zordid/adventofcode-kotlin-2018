package shared

fun <T> Collection<T>.asInfiniteSequence(): Sequence<T> = sequence {
    while (true) yieldAll(this@asInfiniteSequence)
}