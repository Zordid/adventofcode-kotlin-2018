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

tailrec fun part2(coordinates: List<Pair<Int, Int>>, threshold: Int = 10000, safetyMargin: Int = 0): Any {
    val (boundLeft, boundRight) = coordinates.map { it.first }.minMax()!!
    val (boundTop, boundBottom) = coordinates.map { it.second }.minMax()!!

    var safeAreaTouchedEdge = false
    val checkRows = boundTop - safetyMargin..boundBottom + safetyMargin
    val checkColumns = boundLeft - safetyMargin..boundRight + safetyMargin
    val size = checkRows.sumBy { row ->
        val topOrBottomEdge = row == checkRows.first || row == checkRows.last
        if (!safeAreaTouchedEdge)
            checkColumns.count { col ->
                val leftOrRightEdge = col == checkColumns.first || col == checkColumns.last
                val belongsToSafeArea = !safeAreaTouchedEdge &&
                        coordinates.sumBy { (col to row) distanceTo it } < threshold
                if (belongsToSafeArea && (topOrBottomEdge || leftOrRightEdge)) {
                    println("Warning: safe area reached outer bounds, increasing margin to ${safetyMargin + 1}!")
                    safeAreaTouchedEdge = true
                }
                belongsToSafeArea
            } else
            0
    }
    return if (safeAreaTouchedEdge) part2(coordinates, threshold, safetyMargin + 1) else size
}

fun main(args: Array<String>) {
    val puzzle = readPuzzle(6) { it.extractAllInts().toList().let { c -> c[0] to c[1] } }

    println(part1(puzzle))
    println(part2(puzzle))
}