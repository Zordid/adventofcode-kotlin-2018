package day6

import shared.extractAllInts
import shared.minByIfUnique
import shared.minMax
import shared.readPuzzle

infix fun Pair<Int, Int>.distanceTo(other: Pair<Int, Int>) =
    Math.abs(first - other.first) + Math.abs(second - other.second)

fun part1(coordinates: List<Pair<Int, Int>>): Any {
    val (boundLeft, boundRight) = coordinates.map { it.first }.minMax()!!
    val (boundTop, boundBottom) = coordinates.map { it.second }.minMax()!!

    val infinite = mutableSetOf<Int>()
    val ownedArea = IntArray(coordinates.size)

    for (row in boundTop..boundBottom) {
        val topOrBottomEdge = row == boundTop || row == boundBottom
        for (col in boundLeft..boundRight) {
            val leftOrRightEdge = col == boundLeft || col == boundRight
            coordinates
                .mapIndexed { idx, c -> idx to ((col to row) distanceTo c) }
                .minByIfUnique { it.second }?.also {
                    if (topOrBottomEdge || leftOrRightEdge)
                        infinite.add(it.first)
                    ownedArea[it.first]++
                }
        }
    }

    return (coordinates.indices - infinite).map { it to ownedArea[it] }.maxBy { it.second }!!
}

fun part2(coordinates: List<Pair<Int, Int>>, threshold: Int = 10000, safetyMargin: Int = 0): Any {
    val (boundLeft, boundRight) = coordinates.map { it.first }.minMax()!!
    val (boundTop, boundBottom) = coordinates.map { it.second }.minMax()!!

    var warningIssued = false
    val checkRows = boundTop - safetyMargin..boundBottom + safetyMargin
    val checkColumns = boundLeft - safetyMargin..boundRight + safetyMargin
    return checkRows.sumBy { row ->
        val topOrBottomEdge = row == checkRows.first || row == checkRows.last
        checkColumns.count { col ->
            val leftOrRightEdge = col == checkColumns.first || col == checkColumns.last
            val counts = coordinates.sumBy { (col to row) distanceTo it } < threshold
            if (!warningIssued && counts && (topOrBottomEdge || leftOrRightEdge)) {
                println("Warning: safe area reaches outer bounds, result may be invalid!")
                warningIssued = true
            }
            counts
        }
    }
}

fun main(args: Array<String>) {
    val puzzle = readPuzzle(6) { it.extractAllInts().toList().let { c -> c[0] to c[1] } }

    println(part1(puzzle))
    println(part2(puzzle))
}