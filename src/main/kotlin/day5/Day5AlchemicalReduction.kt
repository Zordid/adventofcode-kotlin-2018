package day5

import shared.readPuzzle
import kotlin.math.abs

/* Brilliant idea copied from Peter Tseng! */
private const val caseDifference = 'a'.code - 'A'.code

infix fun Char.matches(other: Char) = abs(this.code - other.code) == caseDifference

fun String.removeAllUnitsOf(type: Char) = asSequence().filter { it.lowercaseChar() != type }

fun Sequence<Char>.polymerReaction(): String {
    val reacted = StringBuilder()
    for (c in this) {
        if (reacted.isNotEmpty() && reacted.last() matches c)
            reacted.setLength(reacted.length - 1)
        else
            reacted.append(c)
    }
    return reacted.toString()
}

fun String.polymerReaction() = asSequence().polymerReaction()

fun part1(polymer: String): Any {
    return polymer.polymerReaction().length
}

fun part2(polymer: String): Any {
    return polymer.asSequence().filter { it.isLowerCase() }.distinct()
        .map { it to polymer.removeAllUnitsOf(it).polymerReaction().length }
        .minBy { it.second }
}

fun main() {
    val polymer = readPuzzle(5).single()

    println(part1(polymer))
    println(part2(polymer))
}