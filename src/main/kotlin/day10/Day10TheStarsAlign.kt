package day10

import shared.extractAllInts
import shared.minToMaxRange
import shared.readPuzzle

data class Light(val x: Int, val y: Int, val vx: Int, val vy: Int) {
    fun posAt(time: Int) = x + vx * time to y + vy * time
}

fun part1and2(puzzle: List<List<Int>>): Int {
    val lights = puzzle.map { Light(it[0], it[1], it[2], it[3]) }

    val solutionTime =
        (0..Int.MAX_VALUE).first { time -> lights.heightAt(time) < lights.heightAt(time + 1) }

    lights.at(solutionTime).printLights()
    return solutionTime
}

fun List<Light>.at(time: Int) = map { it.posAt(time) }

fun List<Light>.heightAt(time: Int) = at(time).map { it.second }.minToMaxRange()!!.let { it.last - it.first }

fun List<Pair<Int, Int>>.printLights() {
    val xRange = map { it.first }.minToMaxRange()!!
    val yRange = map { it.second }.minToMaxRange()!!
    for (y in yRange) {
        for (x in xRange) {
            print(if (contains(x to y)) "#" else '.')
        }
        println()
    }
}

fun main() {
    val puzzle = readPuzzle(10) { it.extractAllInts().toList() }

    println(part1and2(puzzle))
}