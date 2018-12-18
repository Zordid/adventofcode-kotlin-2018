package day6

import shared.*

fun part1(puzzle: List<String>): Int {
    val coordinates = puzzle.map { it.extractCoordinate() }
    val area = coordinates.area()

    val infinite = mutableSetOf<Int>()
    val ownedArea = IntArray(coordinates.size)

    area.forEach { test ->
        val atEdge = test isAtEdgeOf area
        coordinates
            .mapIndexed { idx, c -> idx to c }
            .minByIfUnique { (_, c) -> test manhattanDistanceTo c }
            ?.also { (belongsTo, _) ->
                if (atEdge) infinite.add(belongsTo)
                ownedArea[belongsTo]++
            }
    }

    return (coordinates.indices - infinite).map { it to ownedArea[it] }.maxBy { it.second }!!.second
}

tailrec fun part2(puzzle: List<String>, threshold: Int = 10000, safetyMargin: Int = 0): Int {
    val coordinates = puzzle.map { it.extractCoordinate() }
    val area = coordinates.area() + safetyMargin

    var safeAreaTouchedEdge = false
    val size = area.count { test ->
        if (safeAreaTouchedEdge)
            return@count false

        val atEdge = test isAtEdgeOf area
        val belongsToSafeArea = coordinates.sumBy { test manhattanDistanceTo it } < threshold
        if (belongsToSafeArea && atEdge) {
            println("Warning: safe area reached outer bounds, increasing margin to ${safetyMargin + 1}!")
            safeAreaTouchedEdge = true
        }
        belongsToSafeArea
    }
    return if (safeAreaTouchedEdge) part2(puzzle, threshold, safetyMargin + 1) else size
}

fun main(args: Array<String>) {
    val puzzle = readPuzzle(6)

    println(part1(puzzle))
    println(part2(puzzle))
}