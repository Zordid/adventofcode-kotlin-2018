package day20

import shared.*

data class Room(val location: Coordinate) {

    val doorsTo = BooleanArray(4)

}

class NorthPoleMap(puzzle: String) {

    private val rooms = mutableMapOf(0 toY 0 to BooleanArray(4))

    private val shortestPathToRoom: Map<Coordinate, Int> by lazy {
        println("Analyzing ${rooms.size} rooms...")
        rooms.mapValues { (room, _) ->
            breadthFirstSearch(0 toY 0, ::allDoorsFrom) { it == room }.size - 1
        }
    }

    init {
        processLevel(puzzle.substring(1, puzzle.length - 1))
    }

    private fun processLevel(path: String, startRoom: Coordinate = 0 toY 0): Set<Coordinate> {
        val optionals = path.splitToOptionals()
        return optionals.flatMap { processOptional(it, startRoom) }.toSet()
    }

    private fun processOptional(path: String, startRoom: Coordinate): Set<Coordinate> {
        var index = 0
        var room = startRoom
        while (index < path.length) {
            val c = path[index++]
            when (c) {
                '(' -> {
                    val end = path.findMatchingClosing(index - 1)
                    val level = path.substring(index, end)
                    val rest = path.substring(end + 1)
                    val endRooms = processLevel(level, room)
                    return endRooms.flatMap {
                        processOptional(rest, it)
                    }.toSet()
                }
                else -> {
                    val doors = rooms.getOrPut(room) { BooleanArray(4) }
                    val direction = Direction.valueOf(c.toString())
                    doors[direction.ordinal] = true
                    room = room.manhattanNeighbors[direction.ordinal]
                    rooms.getOrPut(room) { BooleanArray(4) }[direction.opposite.ordinal] = true
                }
            }
        }
        return setOf(room)
    }

    private fun String.splitToOptionals(): List<String> {
        val optionals = mutableListOf(-1)
        var counter = 0
        forEachIndexed { idx, c ->
            if (c == '|' && counter == 0) optionals.add(idx)
            if (c == '(') counter++
            if (c == ')') counter--
        }
        optionals.add(length)

        return optionals.windowed(2, step = 1).map { (s, e) -> substring(s + 1, e) }
    }

    private fun String.findMatchingClosing(openIdx: Int): Int {
        var idx = openIdx
        var count = 0
        while (true) {
            when (this[idx]) {
                '(' -> count++
                ')' -> count--
            }
            if (count == 0)
                return idx
            idx++
        }
    }

    private fun allDoorsFrom(room: Coordinate): List<Coordinate> =
        room.manhattanNeighbors.filterIndexed { index, _ ->
            rooms[room]?.get(index) ?: false
        }

    fun solvePart1() = shortestPathToRoom.maxBy { it.value }!!.value

    fun solvePart2() = shortestPathToRoom.count { it.value >= 1000 }
}

fun main(args: Array<String>) {
    val northPoleMap = NorthPoleMap(readPuzzle(20).single())

    println(northPoleMap.solvePart1())
    println(northPoleMap.solvePart2())
}