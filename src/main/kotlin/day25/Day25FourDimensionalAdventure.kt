package day25

import shared.extractAllInts
import shared.measureRuntime
import shared.readPuzzle

typealias Point = List<Int>
typealias Constellation = Set<Point>

infix fun Point.distanceTo(other: Point) =
    zip(other).sumBy { (a, b) -> Math.abs(a - b) }

fun part1(puzzle: List<String>): Any {
    val points = puzzle.map { it.extractAllInts().toList() }

    val inConstellationWith =
        points.associateWith { p1 -> points.filter { p2 -> p1 distanceTo p2 <= 3 }.toSet() }

    fun addToAndFollow(p: List<Int>, c: MutableSet<Point> = mutableSetOf()): Constellation {
        c.add(p)
        inConstellationWith[p]!!.filter { !c.contains(it) }.forEach { addToAndFollow(it, c) }
        return c
    }

    val constellations = mutableSetOf<Constellation>()
    points.asSequence()
        .filter { p -> !constellations.any { it.contains(p) } }
        .forEach { p -> constellations.add(addToAndFollow(p)) }

    return constellations.size
}

fun main(args: Array<String>) {
    val puzzle = readPuzzle(25)

    measureRuntime {
        println(part1(puzzle))
    }
}