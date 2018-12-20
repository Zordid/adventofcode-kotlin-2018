package shared

import java.util.*

fun <E> LinkedList<E>.circularListIterator(startingPosition: Int = 0) =
    CircularListIterator(this, startingPosition)

class CircularListIterator<T>(private val backingList: LinkedList<T>, startingPosition: Int = 0) :
    MutableListIterator<T> {
    private var iterator = backingList.listIterator(startingPosition)

    override fun add(element: T) {
        iterator.add(element)
    }

    override fun remove() {
        iterator.remove()
    }

    override fun set(element: T) {
        iterator.set(element)
    }

    override fun hasNext() = !backingList.isEmpty()

    override fun hasPrevious() = !backingList.isEmpty()

    override fun next(): T {
        if (!iterator.hasNext())
            iterator = backingList.listIterator()
        return iterator.next()
    }

    override fun nextIndex() = if (!iterator.hasNext()) 0 else iterator.nextIndex()

    override fun previous(): T {
        if (!iterator.hasPrevious())
            iterator = backingList.listIterator(backingList.size)
        return iterator.previous()
    }

    override fun previousIndex() = if (!iterator.hasPrevious()) backingList.size - 1 else iterator.previousIndex()

}

fun <T> List<T>.allPairs(): Sequence<Pair<T, T>> = sequence {
    forEachIndexed { e1Index, e1 ->
        for (e2Index in (e1Index + 1 until size)) {
            yield(e1 to get(e2Index))
        }
    }
}

fun <T : Comparable<T>> List<T>.minMax(): Pair<T, T>? = min()?.let { it to max()!! }

fun List<Int>.minToMaxRange(): IntRange? = minMax()?.let { it.first..it.second }

fun List<Long>.minToMaxRange(): LongRange? = minMax()?.let { it.first..it.second }

fun List<Coordinate>.enclosingArea(): Area =
    when {
        isEmpty() -> Area.EMPTY
        size == 1 -> Area.from(get(0), get(0))
        size == 2 && get(0) < get(1) -> Area.from(get(0), get(1))
        size == 2 -> Area.from(get(1), get(0))
        else -> Area(map { it.x }.minToMaxRange()!!, map { it.y }.minToMaxRange()!!)
    }

