package day10

import shared.extractAllInts
import shared.minMax
import shared.readPuzzle

operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>) =
    first + other.first to second + other.second

fun Pair<Int, Int>.toRange() = first..second
fun Pair<Int, Int>.size() = Math.abs(second - first)

fun part1and2(puzzle: List<List<Int>>): Any {
    val (initialPositions, velocities) =
            puzzle.map { (it[0] to it[1]) to (it[2] to it[3]) }.unzip()

    var positions = initialPositions
    var c = 0
    do {
        positions = positions.mapIndexed { index, p -> p + velocities[index] }
        val boundY = positions.map { it.second }.minMax()!!
        c++
    } while (boundY.size() != 9)

    val boundY = positions.map { it.second }.minMax()!!
    val boundX = positions.map { it.first }.minMax()!!
    for (y in boundY.toRange()) {
        for (x in boundX.toRange()) {
            print(if (positions.contains(x to y)) "#" else '.')
        }
        println()
    }

    return c
}

fun main(args: Array<String>) {
    val puzzle = readPuzzle(10) { it.extractAllInts().toList() }

    println(part1and2(puzzle))
}