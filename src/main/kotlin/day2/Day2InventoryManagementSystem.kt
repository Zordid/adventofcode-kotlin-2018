package day2

import shared.allPairs
import shared.readPuzzle

fun String.containsOneCharExactly(times: Int) =
    toCharArray().distinct().any { c -> count { it == c } == times }

infix fun String.difference(other: String) =
    indices.count { this[it] != other.getOrNull(it) } + (other.length - length).coerceAtLeast(0)

infix fun String.common(other: String) =
    toCharArray().filterIndexed { index, c -> c == other[index] }.joinToString("")

fun part1(ids: List<String>): Int {
    return ids.count { it.containsOneCharExactly(2) } * ids.count { it.containsOneCharExactly(3) }
}

fun part2(ids: List<String>): String {
    return ids.allPairs().single { it.first difference it.second == 1 }.let {
        it.first common it.second
    }
}

fun main() {
    val puzzle = readPuzzle(2)

    println(part1(puzzle))
    println(part2(puzzle))
}