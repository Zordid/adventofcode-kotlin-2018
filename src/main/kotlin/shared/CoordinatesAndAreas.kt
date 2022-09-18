package shared

import kotlin.math.abs

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

    fun neighborToThe(direction: Direction) = transposeBy(manhattanDeltas[direction.ordinal])

    override fun compareTo(other: Coordinate) =
        if (y == other.y) x.compareTo(other.x) else y.compareTo(other.y)

    infix fun manhattanDistanceTo(other: Coordinate) = abs(y - other.y) + abs(x - other.x)

    infix fun transposeBy(other: Coordinate) = this + other

    infix fun scaleBy(factor: Int) = this * factor

    infix fun transposeBy(delta: Pair<Int, Int>) = Coordinate(x + delta.first, y + delta.second)

    infix fun isWithin(area: Area) = area contains this

    infix fun isAtEdgeOf(area: Area) =
        !area.isEmpty() &&
                (x == area.xRange.first || x == area.xRange.last) ||
                (y == area.yRange.first || y == area.yRange.last)

    fun toPair() = x to y

    operator fun unaryMinus() = Coordinate(-x, -y)

    operator fun plus(other: Coordinate) = Coordinate(x + other.x, y + other.y)

    operator fun minus(other: Coordinate) = Coordinate(x - other.x, y - other.y)

    operator fun times(factor: Int) = Coordinate(x * factor, y * factor)

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

    val width = if (isEmpty()) 0 else (xRange.last - xRange.first + 1)
    val height = if (isEmpty()) 0 else (yRange.last - yRange.first + 1)

    val size = if (isEmpty()) 0 else width * height

    val topLeft = xRange.first toY yRange.first
    val bottomRight = xRange.last toY yRange.last

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
                xRange.first - margin..xRange.last + margin,
                yRange.first - margin..yRange.last + margin
            )

    infix fun contains(c: Coordinate) = c.x in xRange && c.y in yRange

    infix fun contains(other: Area) =
        if (isEmpty() || other.isEmpty()) false else
            other.xRange.first in xRange && other.xRange.last in xRange &&
                    other.yRange.first in yRange && other.yRange.last in yRange

    fun isEmpty() = xRange.isEmpty()

    override fun toString(): String {
        if (isEmpty())
            return "(Empty area)"
        return "(${xRange.first},${yRange.first})-(${xRange.last},${yRange.last})"
    }

    companion object {
        val EMPTY = Area(IntRange.EMPTY, IntRange.EMPTY)

        fun from(topLeft: Coordinate, bottomRight: Coordinate) =
            from(topLeft.x, topLeft.y, bottomRight.x, bottomRight.y)

        fun from(x1: Int, y1: Int, x2: Int, y2: Int) =
            Area(x1..x2, y1..y2)
    }
}

infix fun Int.toY(other: Int) = Coordinate(this, other)

fun allCoordinates(columns: Int, rows: Int = columns, baseCol: Int = 0, baseRow: Int = 0) =
    Area(baseCol until baseCol + columns, baseRow until baseRow + rows).allCoordinates()

fun <T> List<List<T>>.toArea() =
    if (isNotEmpty()) Area((0 until this.first().size), indices) else Area.EMPTY
