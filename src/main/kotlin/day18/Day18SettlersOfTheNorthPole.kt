package day18

import shared.Coordinate
import shared.toArea
import shared.measureRuntime
import shared.readPuzzle

class LumberArea(puzzle: List<String>) {

    private val initialMap = puzzle.map { it.toList() }
    private val area = initialMap.toArea()

    fun solve(minutes: Int, debug: Boolean = false): Int {
        val future = future(minutes, debug)

        val trees = future.count('|')
        val lumberyards = future.count('#')
        if (debug) {
            println("Trees: $trees")
            println("Lumberyards: $lumberyards")
        }
        return trees * lumberyards
    }

    private fun future(minutes: Int, debug: Boolean = false): List<List<Char>> {
        var map = initialMap

        val seenBefore = mutableMapOf<List<List<Char>>, Int>()
        var minutesLeft = minutes
        if (debug) println("Initial state:\n${map.neat()}")
        while (!seenBefore.contains(map) && minutesLeft > 0) {
            seenBefore[map] = minutesLeft
            map = map.nextGeneration()
            minutesLeft--
            if (debug) println("After ${minutes - minutesLeft} minutes:\n${map.neat()}")
        }
        if (minutesLeft == 0)
            return map

        if (debug)
            println("Loop detected after ${minutes - minutesLeft} minutes with $minutesLeft minutes to go!")
        val before = seenBefore[map]!!
        val loopSize = before - minutesLeft
        minutesLeft %= loopSize
        if (debug)
            println("Loop length is $loopSize, fast forward to only $minutesLeft minutes left to go!")
        repeat(minutesLeft) { map = map.nextGeneration() }
        return map
    }

    private fun List<List<Char>>.nextGeneration() =
        mapIndexed { y, chars ->
            chars.mapIndexed { x, c ->
                nextState(c, Coordinate(x, y).let { coordinate ->
                    coordinate.allNeighbors.mapNotNull { of(it) }
                })
            }
        }

    private fun nextState(current: Char, neighbors: List<Char>) =
        arrayOf('|', '#').map { c -> neighbors.count { it == c } }.let { (trees, lumberyards) ->
            when (current) {
                '.' -> if (trees >= 3) '|' else '.'
                '|' -> if (lumberyards >= 3) '#' else '|'
                '#' -> if ((lumberyards >= 1) && (trees >= 1)) '#' else '.'
                else -> throw IllegalStateException(current.toString())
            }
        }

    private fun List<List<Char>>.of(c: Coordinate) =
        if (area contains c) this[c.y][c.x] else null

    private fun List<List<Char>>.neat() =
        joinToString("") { it.joinToString("", postfix = "\n") }

    private fun List<List<Char>>.count(searchFor: Char) = sumBy { it.count { c -> c == searchFor } }

}

fun part1(puzzle: List<String>, debug: Boolean = false) = LumberArea(puzzle).solve(10, debug)

fun part2(puzzle: List<String>, debug: Boolean = false) = LumberArea(puzzle).solve(1000000000, debug)

fun main(args: Array<String>) {
    val puzzle = readPuzzle(18)

    measureRuntime {
        println(part1(puzzle))
        println(part2(puzzle))
    }
}