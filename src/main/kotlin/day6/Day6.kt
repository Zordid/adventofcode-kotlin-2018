package day6

import shared.extractAllInts
import shared.readPuzzle

fun Pair<Int, Int>.distanceTo(other: Pair<Int, Int>): Int {
    return Math.abs(first - other.first) + Math.abs(second - other.second)
}


fun part1(coordinates: List<String>): Any {
    val c = coordinates.map { it.extractAllInts().toList() }.map { it[0] to it[1] }

    val boundLeft = c.minBy { it.first }!!.first
    val boundRight = c.maxBy { it.first }!!.first
    val boundTop = c.minBy { it.second }!!.second
    val boundBottom = c.maxBy { it.second }!!.second

    val height = boundBottom - boundTop + 1
    val width = boundRight - boundLeft + 1
    val area = Array(height) { IntArray(width) { -1 } }

    for (row in boundTop..boundBottom) {
        for (col in boundLeft..boundRight) {
            val p = col to row
            val distances = c.mapIndexed { index, pair -> index to pair.distanceTo(p) }
            val minDistance = distances.minBy { it.second }!!
            val sameDistance = distances.any { it.first != minDistance.first && it.second == minDistance.second }
            if (!sameDistance)
                area[row - boundTop][col - boundLeft] = minDistance.first
        }
    }

    val borderAreas =
        (0 until height).flatMap { setOf(area[it][0], area[it][width - 1]) }.toSet() +
                (0 until width).flatMap { setOf(area[0][it], area[height - 1][it]) }


    return (c.indices - borderAreas).map { idx -> idx to area.sumBy { it.count { it == idx } } }.maxBy { it.second }!!
}

fun part2(coordinates: List<String>, threshold: Int = 10000): Any {
    val c = coordinates.map { it.extractAllInts().toList() }.map { it[0] to it[1] }

    val boundLeft = c.minBy { it.first }!!.first
    val boundRight = c.maxBy { it.first }!!.first
    val boundTop = c.minBy { it.second }!!.second
    val boundBottom = c.maxBy { it.second }!!.second

    var count = 0
    for (row in boundTop..boundBottom) {
        for (col in boundLeft..boundRight) {
            val p = col to row
            val totalDistance = c.sumBy { it.distanceTo(p) }

            if (totalDistance < threshold)
                count++
        }
    }

    return count
}

fun main(args: Array<String>) {
    val puzzle = readPuzzle(6)

    println(part1(puzzle))
    println(part2(puzzle))
}