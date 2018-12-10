package day10

import shared.extractAllInts
import shared.minToMaxRange
import shared.readPuzzle

fun part1and2(puzzle: List<List<Int>>): Int {
    val (initialPositions, velocities) =
            puzzle.map { (it[0] to it[1]) to (it[2] to it[3]) }.unzip()

    var positions = initialPositions
    var time = 0
    do {
        positions = positions.mapIndexed { index, p -> p + velocities[index] }
        time++
        val height = positions.map { it.second }.minToMaxRange()!!.size()
    } while (height != 9)

    positions.printLights()

    return time
}

operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>) =
    first + other.first to second + other.second

fun IntRange.size() = Math.abs(last - first)

fun List<Pair<Int, Int>>.printLights() {
    val xRange = map { it.first }.minToMaxRange()!!
    val yRange = map { it.second }.minToMaxRange()!!
    for (y in yRange) {
        for (x in xRange) {
            print(if (contains(x to y)) "#" else '.')
        }
        println()
    }
}

fun main(args: Array<String>) {
    val puzzle = readPuzzle(10) { it.extractAllInts().toList() }

    println(part1and2(puzzle))
}