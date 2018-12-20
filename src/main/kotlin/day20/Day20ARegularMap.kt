package day20

import shared.*

class NorthPoleMap(puzzle: String) {

    private val rooms = mutableMapOf(0 toY 0 to BooleanArray(4))

    private val searchEngine = SearchEngineWithNodes<Coordinate> {
        it.manhattanNeighbors.filterIndexed { index, _ ->
            rooms[it]?.get(index) ?: false
        }
    }

    init {
        processLevel(puzzle.substring(1, puzzle.length - 1))
    }

    private fun processLevel(path: String, startRoom: Coordinate = 0 toY 0): Set<Coordinate> {
        return path.splitToOptionals().flatMap { processOptional(it, startRoom) }.toSet()
    }

    private fun processOptional(path: String, startRoom: Coordinate): Set<Coordinate> {
        var index = 0
        var room = startRoom
        while (index < path.length) {
            val c = path[index++]
            if (c != '(') {
                val direction = Direction.valueOf(c.toString())
                val enteredRoom = room.neighborToThe(direction)
                room.doors()[direction.ordinal] = true
                enteredRoom.doors()[direction.opposite.ordinal] = true
                room = enteredRoom
            } else {
                val end = path.findMatchingClosing(index - 1)
                val level = path.substring(index, end)
                val endRooms = processLevel(level, room)
                if (end == path.length)
                    return endRooms
                if (endRooms.size > 1) {
                    val rest = path.substring(end + 1)
                    return endRooms.flatMap {
                        processOptional(rest, it)
                    }.toSet()
                }
                index = end + 1
                room = endRooms.single()
            }
        }
        return setOf(room)
    }

    private fun Coordinate.doors() = rooms.getOrPut(this) { BooleanArray(4) }

    private fun String.splitToOptionals(): List<String> {
        val dividers = mutableListOf(-1)
        var counter = 0
        forEachIndexed { idx, c ->
            if (c == '|' && counter == 0) dividers.add(idx)
            if (c == '(') counter++
            if (c == ')') counter--
        }
        dividers.add(length)
        return dividers.windowed(2, step = 1).map { (s, e) -> substring(s + 1, e) }
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

    private val solutions: Pair<Int, Int> by lazy {
        var maxLevel = 0
        var aboveThreshold = 0
        searchEngine.completeAcyclicTraverse(0 toY 0).forEach { (level, onLevel) ->
            maxLevel = level
            if (level >= 1000)
                aboveThreshold += onLevel.size
        }

        maxLevel to aboveThreshold
    }

    fun solvePart1() = solutions.first

    fun solvePart2() = solutions.second
}

fun main(args: Array<String>) {
    val northPoleMap = NorthPoleMap(readPuzzle(20).single())
    measureRuntime {
        println(northPoleMap.solvePart1())
        println(northPoleMap.solvePart2())
    }
}