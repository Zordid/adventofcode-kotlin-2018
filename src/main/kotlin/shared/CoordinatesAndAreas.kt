package shared

enum class Direction {
    N, E, S, W;

    val opposite: Direction
        get() =
            when (this) {
                N -> S
                S -> N
                E -> W
                W -> E
            }
}

data class Coordinate(val x: Int, val y: Int) : Comparable<Coordinate> {
    val allNeighbors get() = allDeltas.map { transposeBy(it) }
    val manhattanNeighbors get() = manhattanDeltas.map { transposeBy(it) }

    override fun compareTo(other: Coordinate) =
        if (y == other.y) x.compareTo(other.x) else y.compareTo(other.y)

    infix fun manhattanDistanceTo(other: Coordinate) = Math.abs(y - other.y) + Math.abs(x - other.x)

    infix fun transposeBy(other: Coordinate) = Coordinate(x + other.x, y + other.y)

    infix fun transposeBy(delta: Pair<Int, Int>) = Coordinate(x + delta.first, y + delta.second)

    infix fun isWithin(area: Area) = area contains this

    infix fun isAtEdgeOf(area: Area) =
        !area.isEmpty() &&
                (x == area.xRange.start || x == area.xRange.endInclusive) ||
                (y == area.yRange.start || y == area.yRange.endInclusive)

    fun toPair() = x to y

    override fun toString() = "($x,$y)"

    companion object {
        private val allDeltas = arrayOf(
            -1 to -1, 0 to -1, 1 to -1,
            1 to 0, 1 to 1,
            0 to 1, -1 to 1, -1 to 0
        )
        private val manhattanDeltas = arrayOf(
            0 to -1, 1 to 0, 0 to 1, -1 to 0
        )

        fun fromPair(p: Pair<Int, Int>) = Coordinate(p.first, p.second)
    }
}

data class Area(val xRange: IntRange, val yRange: IntRange) : Iterable<Coordinate> {
    override fun iterator(): Iterator<Coordinate> = allCoordinates().iterator()

    val size = if (isEmpty()) 0 else (xRange.endInclusive - xRange.start + 1) * (yRange.endInclusive - yRange.start + 1)

    fun allCoordinates() = sequence {
        for (y in yRange) {
            for (x in xRange)
                yield(x toY y)
        }
    }

    operator fun plus(margin: Int) = increasedBy(margin)

    operator fun minus(margin: Int) = increasedBy(-margin)

    private fun increasedBy(margin: Int) =
        if (isEmpty())
            this
        else
            Area(
                xRange.start - margin..xRange.endInclusive + margin,
                yRange.start - margin..yRange.endInclusive + margin
            )

    infix fun contains(c: Coordinate) = c.x in xRange && c.y in yRange

    infix fun contains(other: Area) =
        if (isEmpty() || other.isEmpty()) false else
            other.xRange.start in xRange && other.xRange.endInclusive in xRange &&
                    other.yRange.start in yRange && other.yRange.endInclusive in yRange

    fun isEmpty() = xRange.isEmpty()

    override fun toString(): String {
        if (isEmpty())
            return "(Empty area)"
        return "(${xRange.start},${yRange.start})-(${xRange.endInclusive},${yRange.endInclusive})"
    }

    companion object {
        val EMPTY = Area(IntRange.EMPTY, IntRange.EMPTY)

        fun from(topLeft: Coordinate, bottomRight: Coordinate) =
            from(topLeft.x, bottomRight.x, topLeft.y, bottomRight.y)

        fun from(x1: Int, y1: Int, x2: Int, y2: Int) =
            Area(x1..x2, y1..y2)
    }
}

infix fun Int.toY(other: Int) = Coordinate(this, other)

fun allCoordinates(columns: Int, rows: Int = columns, baseCol: Int = 0, baseRow: Int = 0) =
    Area(baseCol until baseCol + columns, baseRow until baseRow + rows).allCoordinates()

fun <T> List<List<T>>.toArea() =
    if (isNotEmpty()) Area((0 until this.first().size), (0 until size)) else Area.EMPTY
