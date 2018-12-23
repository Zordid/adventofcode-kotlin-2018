package day23

import shared.extractAllInts
import shared.measureRuntime
import shared.readPuzzle

data class Coordinate3d(val x: Int, val y: Int, val z: Int) {

    infix fun manhattanDistanceTo(other: Coordinate3d) =
        Math.abs(x - other.x) + Math.abs(y - other.y) + Math.abs(z - other.z)

}

data class NanoBot(val coordinate: Coordinate3d, val signalRadius: Int) {

    infix fun inRangeOf(other: NanoBot) = coordinate manhattanDistanceTo other.coordinate <= other.signalRadius

}

fun inRange(x: Int, y: Int, z: Int, other: NanoBot) =
    Math.abs(x - other.coordinate.x) + Math.abs(y - other.coordinate.y) + Math.abs(z - other.coordinate.z) <= other.signalRadius

fun coordinateInRange(coordinate: Coordinate3d, bot: NanoBot) =
    coordinate manhattanDistanceTo bot.coordinate <= bot.signalRadius

fun part1(puzzle: List<List<Int>>): Any {
    val bots = puzzle.map { (x, y, z, r) -> NanoBot(Coordinate3d(x, y, z), r) }

    val strongest = bots.maxBy { it.signalRadius }!!

    return bots.count { it inRangeOf strongest }
}

fun part2(puzzle: List<List<Int>>): Any {
    val bots = puzzle.map { (x, y, z, r) -> NanoBot(Coordinate3d(x, y, z), r) }

    bots.sortedBy { it.signalRadius }.asReversed().forEach { println(it) }



    return ""
}

fun main(args: Array<String>) {
    val puzzle = readPuzzle(23) { it.extractAllInts().toList() }

    measureRuntime {
        println(part1(puzzle))
        println(part2(puzzle))
    }
}