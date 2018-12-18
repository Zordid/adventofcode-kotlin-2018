package day6

import shared.*

fun part1(puzzle: List<String>): Int {
    val coordinates = puzzle.map { it.extractAllInts().toList().let { (x, y) -> x toY y } }
    val range = coordinates.range()!!

    val infinite = mutableSetOf<Int>()
    val ownedArea = IntArray(coordinates.size)

    range.allCoordinates().forEach { c ->
        val atEdge = c.isAtEdge(range)
        coordinates
            .mapIndexed { idx, owner -> idx to owner }
            .minByIfUnique { c manhattanDistanceTo it.second }
            ?.also { (idx, _) ->
                if (atEdge) infinite.add(idx)
                ownedArea[idx]++
            }
    }

    return (coordinates.indices - infinite).map { it to ownedArea[it] }.maxBy { it.second }!!.second
}

tailrec fun part2(puzzle: List<String>, threshold: Int = 10000, safetyMargin: Int = 0): Int {
    val coordinates = puzzle.map { it.extractAllInts().toList().let { (x, y) -> x toY y } }

    val (rangeX, rangeY) = coordinates.range()!!.increaseBy(safetyMargin)

    var safeAreaTouchedEdge = false
    val size = rangeY.sumBy { row ->
        if (!safeAreaTouchedEdge)
            rangeX.count { col ->
                val coordinate = col toY row
                val atEdge = coordinate.isAtEdge(rangeX to rangeY)
                val belongsToSafeArea = !safeAreaTouchedEdge &&
                        coordinates.sumBy { coordinate manhattanDistanceTo it } < threshold
                if (belongsToSafeArea && atEdge) {
                    println("Warning: safe area reached outer bounds, increasing margin to ${safetyMargin + 1}!")
                    safeAreaTouchedEdge = true
                }
                belongsToSafeArea
            } else
            0
    }
    return if (safeAreaTouchedEdge) part2(puzzle, threshold, safetyMargin + 1) else size
}

fun main(args: Array<String>) {
    val puzzle = readPuzzle(6)

    println(part1(puzzle))
    println(part2(puzzle))
}