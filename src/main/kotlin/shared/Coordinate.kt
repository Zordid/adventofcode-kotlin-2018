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