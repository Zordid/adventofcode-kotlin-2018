package day22

import shared.*

enum class Type(val c: Char) {
    Rock('.'), Wet('='), Narrow('|'), Solid('#');

    override fun toString() = c.toString()
}

enum class Equipment { Neither, Torch, ClimbingGear }

data class State(val equipment: Equipment, val c: Coordinate)

class CaveMap(puzzle: List<String>) {
    private val depth = puzzle[0].extractAllInts().single()
    val origin = 0 toY 0
    val target = puzzle[1].extractCoordinate()

    var totalCalc = 0L

    private val erosionLevelCache = mutableMapOf<Coordinate, Int>()

    private fun Coordinate.geologyIndex(): Int {
        totalCalc++
        if (this == origin || this == target) return 0

        if (y == 0)
            return x * 16807
        if (x == 0)
            return y * 48271

        val west = this.transposeBy(-1 to 0)
        val north = this.transposeBy(0 to -1)
        return west.erosionLevel() * north.erosionLevel()
    }

    private fun Coordinate.erosionLevel() =
        erosionLevelCache.getOrPut(this) { (geologyIndex() + depth) % 20183 }

    private fun Int.type() = when (this % 3) {
        0 -> Type.Rock
        1 -> Type.Wet
        2 -> Type.Narrow
        else -> throw IllegalArgumentException(this.toString())
    }

    fun draw(marker: Coordinate? = null, path: Collection<Coordinate> = emptySet()) {
        val areaOfInterest = erosionLevelCache.keys.enclosingArea()
        (areaOfInterest + 1).forEach { c ->
            val cachedValue = erosionLevelCache[c]?.type()?.toString() ?: "?"
            if (c.x == -1) println()
            when (c) {
                marker -> print('X')
                origin -> print('M')
                target -> print('T')
                else -> print(if (path.contains(c)) 'O' else cachedValue)
            }
        }
        println()
    }

    fun totalRiskLevel(): Int {
        return Area.from(origin, target).sumOf { this[it].ordinal }
    }

    operator fun get(c: Coordinate): Type {
        if (c.x < 0 || c.y < 0) return Type.Solid
        return c.erosionLevel().type()
    }

}

class CaveNavigator(puzzle: List<String>) {

    val map = CaveMap(puzzle)

    private val validityMap = mapOf(
        Type.Rock to setOf(Equipment.ClimbingGear, Equipment.Torch),
        Type.Wet to setOf(Equipment.ClimbingGear, Equipment.Neither),
        Type.Narrow to setOf(Equipment.Torch, Equipment.Neither),
        Type.Solid to emptySet()
    )

    private fun State.possibleMoves(): Set<State> {
        val result = mutableSetOf<State>()
        val typeHere = map[c]
        val validHere = validityMap[typeHere]!!
        for (neighbor in c.manhattanNeighbors) {
            val neighborType = map[neighbor]
            if (neighborType != Type.Solid) {
                val neighborValid = validityMap[neighborType]!!
                val validBoth = validHere intersect neighborValid
                result.addAll(validBoth.map { State(it, neighbor) })
            }
        }
        return result
    }

    private fun neighbors(s: State) = s.possibleMoves()

    private fun cost(s: State, d: State) = if (s.equipment != d.equipment) 8 else 1

    private fun costLowerBounds(s: State, d: State) = s.c manhattanDistanceTo d.c

    private val aStar = AStar(::neighbors, ::cost, ::costLowerBounds)

    private val originState = State(Equipment.Torch, map.origin)

    private val targetState = State(Equipment.Torch, map.target)

    fun minimumTravelLengthAndPath(): Pair<Int, Collection<State>> {
        val (dist, prev) = aStar.search(originState, targetState)
        val path = mutableListOf<State>()
        var current: State? = targetState
        while (current != null) {
            path.add(current)
            current = prev[current]
        }
        return dist[targetState]!! to path.reversed()
    }

}

fun part1(puzzle: List<String>): Any {
    val m = CaveNavigator(puzzle)
    return m.map.totalRiskLevel()
}

fun part2(puzzle: List<String>, verbose: Boolean = false): Any {
    val m = CaveNavigator(puzzle)
    val (length, path) = m.minimumTravelLengthAndPath()
    if (verbose) {
        println("evaluated ${m.map.totalCalc} cells")
        m.map.draw(path = path.map { it.c })
        println(" needed $length minutes. ")
        val cost = path.switches() * 7 + path.size - 1
        println("${path.switches()} * 7 + ${path.size - 1} = $cost")
    }
    return length
}

fun Collection<State>.switches() = windowed(2, 1).count { (a, b) -> a.equipment != b.equipment }

fun main() {
    val puzzle = readPuzzle(22)

    measureRuntime {
        println(part1(puzzle))
        println(part2(puzzle))
    }
}