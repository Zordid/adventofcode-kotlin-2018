package day22

import shared.*

enum class Type(val c: Char) {
    Rock('.'), Wet('='), Narrow('|'), Solid('#');

    override fun toString() = c.toString()
}

enum class Equipment { Neither, Torch, ClimbingGear }

data class State(val equipment: Equipment, val c: Coordinate)

class CaveSystem(puzzle: List<String>, bufferWidth: Int = 0) {
    val depth = puzzle[0].extractAllInts().single()
    val origin = 0 toY 0
    val target = puzzle[1].extractCoordinate()

    var area = Area.from(origin, target transposeBy (bufferWidth to 5))

    val geologyIndices = mutableListOf<MutableList<Int>>()
    val erosionLevels = mutableListOf<MutableList<Int>>()

    fun Coordinate.geologyIndex(): Int {
        if (this == origin || this == target) return 0

        if (y == 0)
            return x * 16807
        if (x == 0)
            return y * 48271

        val west = this.transposeBy(-1 to 0)
        val north = this.transposeBy(0 to -1)
        return erosionLevels[west.y][west.x] * erosionLevels[north.y][north.x]
    }

    fun Coordinate.erosionLevel() = (geologyIndex() + depth) % 20183

    fun Int.type() = when (this % 3) {
        0 -> Type.Rock
        1 -> Type.Wet
        2 -> Type.Narrow
        else -> throw IllegalArgumentException(this.toString())
    }

    fun Int.riskLevel() = type().ordinal

    init {
        area.forEach { c ->
            val (x, y) = c
            if (x == 0) {
                geologyIndices.add(mutableListOf())
                erosionLevels.add(mutableListOf())
            }
            geologyIndices[y].add(c.geologyIndex())
            erosionLevels[y].add(c.erosionLevel())
        }
    }

    fun draw(marker: Coordinate? = null, path: Collection<Coordinate> = emptySet()) {
        (area + 1).forEach { c ->
            if (c.x == -1) println()
            when (c) {
                marker -> print('X')
                origin -> print('M')
                target -> print('T')
                else -> print(if (path.contains(c)) 'O' else this[c])
            }
        }
        println()
    }

    fun totalRiskLevel(): Int {
        return Area.from(origin, target).sumBy { (x, y) -> erosionLevels[y][x].riskLevel() }
    }

    operator fun get(c: Coordinate): Type {
        if (!area.contains(c)) return Type.Solid
        return erosionLevels[c.y][c.x].type()
    }

    private val validityMap = mapOf(
        Type.Rock to setOf(Equipment.ClimbingGear, Equipment.Torch),
        Type.Wet to setOf(Equipment.ClimbingGear, Equipment.Neither),
        Type.Narrow to setOf(Equipment.Torch, Equipment.Neither),
        Type.Solid to emptySet()
    )

    fun Type.validWith(e: Equipment) = validityMap[this]!!.contains(e)


    fun State.possibleMoves(): Set<State> {
        val result = mutableSetOf<State>()
        val typeHere = this@CaveSystem[c]
        val validHere = validityMap[typeHere]!!
        for (neighbor in c.manhattanNeighbors) {
            val neighborType = this@CaveSystem[neighbor]
            if (neighborType != Type.Solid) {
                val neighborValid = validityMap[neighborType]!!
                val validBoth = validHere intersect neighborValid
                result.addAll(validBoth.map { State(it, neighbor) })
            }
        }
        return result
    }

    fun neighbors(s: State) = s.possibleMoves()

    fun cost(s: State, d: State) = if (s.equipment != d.equipment) 8 else 1

    val dijkstra = Dijkstra(::neighbors, ::cost)

    fun minimumTravelLengthDetails(): Pair<Map<State, Int>, Map<State, State>> {
        val (dist, prev) = dijkstra.search(State(Equipment.Torch, origin), State(Equipment.Torch, target))
        Equipment.values().forEach { e ->
            println("$e: ${dist[State(e, target)]}")
        }
        return dist to prev
    }

    fun minimumTravelLengthAndPath(): Pair<Int, Collection<State>> {
        val target = State(Equipment.Torch, target)
        val (dist, prev) = dijkstra.search(State(Equipment.Torch, origin), target)
        val path = mutableListOf<State>()
        var current: State? = target
        while (current != null) {
            path.add(current)
            current = prev[current]
        }
        return dist[target]!! to path.reversed()
    }

}

fun part1(puzzle: List<String>): Any {
    val m = CaveSystem(puzzle)
    return m.totalRiskLevel()
}

fun Collection<State>.switches() = windowed(2, 1).count { (a, b) -> a.equipment != b.equipment }

fun part2(puzzle: List<String>): Any {
    return (0..Int.MAX_VALUE).asSequence().map { buffer ->
        print("Trying buffer $buffer... ")
        val m = CaveSystem(puzzle, buffer)
        val (length, path) = m.minimumTravelLengthAndPath()
        println(" needed $length minutes. ")
        val cost = path.switches() * 7 + path.size - 1
        println("${path.switches()} * 7 + ${path.size - 1} = $cost")
        length
    }.windowed(5, 1).first { it.distinct().size == 1 }.first()

//
//    val (_, prev) = m.minimumTravelLengthDetails()
//    val path = mutableSetOf<Coordinate>()
//    var current: CaveSystem.State? = CaveSystem.State(CaveSystem.Equipment.Torch, m.target)
//    while (current != null) {
//        path.add(current.c)
//        current = prev[current]
//    }
//    m.draw(path = path)
//    return m.minimumTravelLength()
}

fun main(args: Array<String>) {
    val puzzle = readPuzzle(22)

    measureRuntime {
        println(part1(puzzle))
        println(part2(puzzle))
    }
}