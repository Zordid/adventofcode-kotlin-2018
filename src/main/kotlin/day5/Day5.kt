package day5

import shared.readPuzzle
import java.util.*

infix fun Char.matches(other: Char) =
    (isLowerCase() && other.isUpperCase() || isUpperCase() && other.isLowerCase()) &&
            toLowerCase() == other.toLowerCase()

fun String.removeAllUnitsOf(type: Char): List<Char> = toCharArray().filter { it.toLowerCase() != type }

fun String.polymerReaction(): Int = toCharArray().toList().polymerReaction()

fun List<Char>.polymerReaction(): Int {
    val list = LinkedList(this)
    var remaining = size

    val iterator = list.listIterator()
    var previousUnit = if (iterator.hasNext()) iterator.next() else return 0
    while (iterator.hasNext()) {
        val currentUnit = iterator.next()
        if (previousUnit matches currentUnit) {
            iterator.remove()
            iterator.previous()
            iterator.remove()
            remaining -= 2
            if (remaining > 0)
                previousUnit = if (iterator.hasPrevious()) iterator.previous() else iterator.next()
        } else {
            previousUnit = currentUnit
        }
    }
    return remaining
}

fun part1(polymer: String): Any {
    return polymer.polymerReaction()
}

fun part2(polymer: String): Any {
    return polymer.toCharArray().filter { it.isLowerCase() }.toSet()
        .map { it to polymer.removeAllUnitsOf(it).polymerReaction() }
        .minBy { it.second }!!
}

fun main(args: Array<String>) {
    val puzzle = readPuzzle(5)

    println(part1(puzzle.single()))
    println(part2(puzzle.single()))
}