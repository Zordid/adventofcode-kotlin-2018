package day12

import shared.measureRuntime
import shared.readPuzzle

class Pots(puzzle: List<String>) {
    private val initial =
        puzzle.first().split(" ")[2] to 0L
    private val transforms =
        puzzle.filter { it.contains("=>") }
            .map { l -> l.split(" ").let { it[0] to it[2] } }.toMap()

    fun solvePart1(): Long {
        var p = initial
        repeat(20) { p = p.nextGeneration() }
        return p.sum()
    }

    fun solvePart2(): Long {
        var leftGenerations = 50000000000L
        var prev: Pair<String, Long>
        var pots = initial
        do {
            prev = pots
            pots = prev.nextGeneration()
            leftGenerations--
        } while (pots.first != prev.first)
        val drift = pots.second - prev.second
        println("Stabilized with $leftGenerations to go!")
        println("Drift per generation is $drift.")

        pots = pots.first to pots.second + drift * leftGenerations
        return pots.sum()
    }

    fun solveAny(g: Long): Long {
        if (g == 0L) return initial.sum()
        var leftGenerations = g
        val seen = mutableMapOf<String, Pair<Long, Long>>()
        var pots = initial
        do {
            seen[pots.first] = leftGenerations to pots.second
            pots = pots.nextGeneration()
            leftGenerations--
        } while (!seen.contains(pots.first) && leftGenerations > 0)
        if (leftGenerations == 0L) return pots.sum()

        val before = seen[pots.first]!!
        val drift = pots.second - before.second
        val loopSize = before.first - leftGenerations
        println("Stabilized with $leftGenerations to go!")
        println("Drift per generation is $drift and loop size $loopSize.")
        val skipLoops = leftGenerations / loopSize
        leftGenerations %= loopSize
        pots = pots.first to pots.second + drift * skipLoops
        println("Skipping $skipLoops loops and finishing $leftGenerations loops..")
        repeat(leftGenerations.toInt()) { pots = pots.nextGeneration() }
        return pots.sum()
    }

    private fun Pair<String, Long>.nextGeneration(): Pair<String, Long> {
        val (pattern, start) = this
        val newPattern = "....$pattern...."
            .windowed(5).joinToString("") { transforms.getOrDefault(it, ".") }
        val newStart = start - 2 + newPattern.indexOf('#')
        return newPattern.trim('.') to newStart
    }

    private fun Pair<String, Long>.sum() = first.mapIndexed { idx, c -> (idx + second) to c }
        .fold(0L) { acc, (idx, c) -> if (c == '#') acc + idx else acc }

}

fun part1(puzzle: List<String>) = Pots(puzzle).solvePart1()

fun part2(puzzle: List<String>) = Pots(puzzle).solvePart2()

fun main(args: Array<String>) {
    val puzzle = readPuzzle(12)

    measureRuntime {
        println(part1(puzzle))
    }
    measureRuntime {
        println(part2(puzzle))
    }
}