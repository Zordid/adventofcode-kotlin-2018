package day1

import shared.asInfiniteSequence
import shared.readPuzzleAsInts
import shared.reduceIntermediate

fun Iterable<Int>.endFrequency() = sum()

fun List<Int>.firstDuplicateFrequency(): Int {
    val seen = mutableSetOf<Int>()
    return asInfiniteSequence().reduceIntermediate { acc, v -> acc+v }.first { !seen.add(it) }
}

fun main(args: Array<String>) {
    val frequencyChanges = readPuzzleAsInts(1)

    println("Part 1")
    println("End frequency is ${frequencyChanges.endFrequency()} Hz")

    println("Part 2")
    println("First duplicate frequency is ${frequencyChanges.firstDuplicateFrequency()} Hz")
}