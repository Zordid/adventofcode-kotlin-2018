package day10

import shared.extractAllInts
import shared.minMax
import shared.readPuzzle

data class Point(val p: Pair<Int, Int>, val v: Pair<Int, Int>) {
    constructor(n: List<Int>) : this(n[0] to n[1], n[2] to n[3])
}

operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>): Pair<Int, Int> {
    return first + other.first to second + other.second
}

fun part1(puzzle: List<String>): Any {
    val data = puzzle.map { Point(it.extractAllInts().toList()) }

    var p = data.map { it.p }
    val v = data.map { it.v }

    var boundX = p.map { it.first }.minMax()!!
    var boundY = p.map { it.second }.minMax()!!
    var c = 0
    while (boundY.second - boundY.first != 9) {
        p = p.mapIndexed { index, p -> p + v[index] }
        c++
        boundX = p.map { it.first }.minMax()!!
        boundY = p.map { it.second }.minMax()!!
    }

    for (y in boundY.first..boundY.second) {
        for (x in boundX.first..boundX.second) {
            val c = x to y
            print(if (p.contains(c)) "#" else '.')
        }
        println()
    }
    println(c)

    return ""
}

fun part2(puzzle: List<String>): Any {
    return ""
}

fun main(args: Array<String>) {
    val puzzle = readPuzzle(10)

    println(part1(puzzle))
    println(part2(puzzle))
}