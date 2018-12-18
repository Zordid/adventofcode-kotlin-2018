package day18

import shared.measureRuntime
import shared.readPuzzle

data class Coordinate(val x: Int, val y: Int) : Comparable<Coordinate> {
    val neighbors get() = deltas.map { transposeBy(it) }

    override fun compareTo(other: Coordinate) =
        if (y == other.y) x.compareTo(other.x) else y.compareTo(other.y)

    infix fun distanceTo(other: Coordinate) = Math.abs(y - other.y) + Math.abs(x - other.x)

    private fun transposeBy(delta: Pair<Int, Int>) = Coordinate(x + delta.first, y + delta.second)

    companion object {
        val deltas = listOf(
            -1 to -1,
            0 to -1,
            1 to -1,
            1 to 0,
            1 to 1,
            0 to 1,
            -1 to 1,
            -1 to 0
        )
    }
}

class Layout(puzzle: List<String>) {

    val initialMap = puzzle.map { it.toCharArray().toList() }

    fun nextState(current: Char, neighbors: List<Char>): Char {
        val map = neighbors.distinct().map { c -> c to neighbors.count { it == c } }.toMap()
        val trees = map['|'] ?: 0
        val lumberyard = map['#'] ?: 0
        val open = map['.'] ?: 0

        return when (current) {
            '.' -> if (trees >= 3) '|' else '.'
            '|' -> if (lumberyard >= 3) '#' else '|'
            '#' -> if ((lumberyard >= 1) && (trees >= 1)) '#' else '.'
            else -> throw IllegalStateException(current.toString())
        }
    }

    fun List<List<Char>>.of(c: Coordinate) =
        if ((c.y >= 0) && (c.y < size) && (c.x >= 0) && (c.x < this[c.y].size)) this[c.y][c.x] else ' '

    fun List<List<Char>>.nextGeneration(): List<List<Char>> {
        return mapIndexed { y, chars ->
            chars.mapIndexed { x, c ->
                nextState(c, Coordinate(x, y).let {
                    it.neighbors.map { of(it) }
                })
            }
        }
    }

    fun after(min: Int): List<List<Char>> {
        var l = initialMap
        println(l.neat())
        repeat(min) {
            l = l.nextGeneration()
            println("After ${it+1} minutes:\n${l.neat()}")
        }
        return l
    }

    fun afterFast(min: Int): List<List<Char>> {
        var map = initialMap

        val seen = mutableMapOf<List<List<Char>>, Int>()
        var minutesLeft = min
        do {
            seen[map] = minutesLeft
            map = map.nextGeneration()
            minutesLeft--
            if (minutesLeft % 1000 == 0)
                println("$minutesLeft left... seen ${seen.size} unique constellations before!")
        } while (!seen.contains(map) && minutesLeft > 0)

        if (minutesLeft==0)
            return map

        val before = seen[map]!!
        val loopSize = before - minutesLeft
        println("Stabilized with $minutesLeft minutes to go!")
        println("Loop length is $loopSize")

        minutesLeft = minutesLeft % loopSize
        println("Still $minutesLeft to go!")
        repeat(minutesLeft) { map = map.nextGeneration() }

        return map
    }

}

fun List<List<Char>>.neat() = joinToString("") { it.joinToString("", postfix = "\n") }

fun List<List<Char>>.count(search: Char) = sumBy { it.count { c -> c == search } }

fun part1(puzzle: List<String>): Any {
    val l = Layout(puzzle)
    val after = l.after(10)

    val trees = after.count('|')
    println(trees)
    val lumberyards = after.count('#')
    println(lumberyards)
    return trees * lumberyards
}

fun part2(puzzle: List<String>): Any {
    val l = Layout(puzzle)

    val after = l.afterFast(1000000000)
    val trees = after.count('|')
    println(trees)
    val lumberyards = after.count('#')
    println(lumberyards)
    return trees * lumberyards



}

fun main(args: Array<String>) {
    val puzzle = readPuzzle(18)

    measureRuntime {
        println(part1(puzzle))
        println(part2(puzzle))
    }
}