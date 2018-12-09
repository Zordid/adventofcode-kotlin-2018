package day1

import shared.asInfiniteSequence
import shared.readPuzzleAsInts
import shared.reduceIntermediate

fun Iterable<Int>.endFrequency() = sum()

fun List<Int>.firstDuplicateFrequency(): Int {
    val seen = mutableSetOf<Int>()
    return asInfiniteSequence().reduceIntermediate { acc, v -> acc + v }.first { !seen.add(it) }
}

fun part1(frequencyChanges: List<Int>): Int {
    return frequencyChanges.endFrequency()
}

fun part2(frequencyChanges: List<Int>): Int {
    return frequencyChanges.firstDuplicateFrequency()
}

fun main(args: Array<String>) {
    val puzzle = readPuzzleAsInts(1)

    println(part1(puzzle))
    println(part2(puzzle))
}