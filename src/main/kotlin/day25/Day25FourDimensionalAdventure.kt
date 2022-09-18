package day25

import shared.extractAllInts
import shared.measureRuntime
import shared.readPuzzle
import kotlin.math.abs

typealias Point = List<Int>
typealias Constellation = Set<Point>

infix fun Point.distanceTo(other: Point) =
    zip(other).sumOf { (a, b) -> abs(a - b) }

infix fun Point.distanceToConstellation(constellation: Constellation) =
    constellation.minOf { it distanceTo this }

fun part1(puzzle: List<String>): Any {
    val points = puzzle.map { it.extractAllInts().toList() }

    val constellations = mutableListOf<Constellation>()
    points.forEach { point ->
        val belongsTo = constellations.filter { (point distanceToConstellation it) <= 3 }

        val newConstellation = setOf(point) + belongsTo.flatten()
        constellations.removeAll(belongsTo.toSet())
        constellations.add(newConstellation)
    }
    return constellations.size
}

fun main() {
    val puzzle = readPuzzle(25)

    measureRuntime {
        println(part1(puzzle))
    }
}