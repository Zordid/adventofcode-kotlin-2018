package shared

data class Coordinate(val x: Int, val y: Int) : Comparable<Coordinate> {
    val allNeighbors get() = allDeltas.map { transposeBy(it) }
    val manhattanNeighbors get() = manhattanDeltas.map { transposeBy(it) }

    override fun compareTo(other: Coordinate) =
        if (y == other.y) x.compareTo(other.x) else y.compareTo(other.y)

    infix fun manhattanDistanceTo(other: Coordinate) = Math.abs(y - other.y) + Math.abs(x - other.x)

    infix fun transposeBy(other: Coordinate) = Coordinate(x + other.x, y + other.y)

    infix fun transposeBy(delta: Pair<Int, Int>) = Coordinate(x + delta.first, y + delta.second)

    fun toPair() = x to y

    override fun toString() = "($x,$y)"

    fun isAtEdge(range: Pair<IntRange, IntRange>) =
        (x == range.first.start || x == range.first.endInclusive) ||
                (y == range.second.start || y == range.second.endInclusive)

    companion object {
        private val allDeltas = listOf(
            -1 to -1, 0 to -1, 1 to -1,
            1 to 0, 1 to 1,
            0 to 1, -1 to 1, -1 to 0
        )
        private val manhattanDeltas = listOf(
            0 to -1, 1 to 0, 0 to 1, -1 to 0
        )

        fun fromPair(p: Pair<Int, Int>) = Coordinate(p.first, p.second)
    }
}

infix fun Int.toY(other: Int) = Coordinate(this, other)

fun allCoordinates(columns: Int, rows: Int = columns, baseCol: Int = 0, baseRow: Int = 0) =
    ((baseCol until baseCol + columns) to (baseRow until baseRow + rows)).allCoordinates()

fun Pair<IntRange, IntRange>.allCoordinates() = sequence {
    for (y in second) {
        for (x in first)
            yield(x toY y)
    }
}

fun List<Coordinate>.range() =
    map { it.x }.minToMaxRange()?.let { xRange -> xRange to map { it.y }.minToMaxRange()!! }

fun Pair<IntRange, IntRange>.increaseBy(margin: Int) =
    first.start - margin..first.endInclusive + margin to second.start - margin..second.endInclusive + margin
