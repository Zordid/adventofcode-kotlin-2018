package day25

import shared.extractAllInts
import shared.measureRuntime
import shared.readPuzzle

typealias Point = List<Int>
typealias Constellation = Set<Point>

infix fun Point.distanceTo(other: Point) =
    zip(other).sumBy { (a, b) -> Math.abs(a - b) }

infix fun Point.distanceToConstellation(constellation: Constellation) =
    constellation.map { it distanceTo this }.min()!!

fun part1(puzzle: List<String>): Any {
    val points = puzzle.map { it.extractAllInts().toList() }

    val constellations = mutableListOf<Constellation>()
    points.forEach { point ->
        val belongsTo = constellations.filter { (point distanceToConstellation it) <= 3 }

        val newConstellation = setOf(point) + belongsTo.flatten()
        constellations.removeAll(belongsTo)
        constellations.add(newConstellation)
    }
    return constellations.size
}

fun main(args: Array<String>) {
    val puzzle = readPuzzle(25)

    measureRuntime {
        println(part1(puzzle))
    }
}