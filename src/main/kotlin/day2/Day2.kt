package day2

import shared.allPairs
import shared.readPuzzle

fun String.containsOneCharExactly(times: Int) =
    toCharArray().distinct().any { c -> count { it == c } == times }

infix fun String.differenceCountTo(other: String) =
    indices.count { this[it] != other.getOrNull(it) } + Math.max(other.length - length, 0)

infix fun String.differenceTo(other: String) =
    toCharArray().filterIndexed { index, c -> c == other[index] }.joinToString("")

fun part1(ids: List<String>): Int {
    return ids.count { it.containsOneCharExactly(2) } * ids.count { it.containsOneCharExactly(3) }
}

fun part2(ids: List<String>): String {
    return ids.allPairs().single { it.first differenceCountTo it.second == 1 }.let {
        it.first differenceTo it.second
    }
}

fun main(args: Array<String>) {
    val puzzle = readPuzzle(2)

    println(part1(puzzle))
    println(part2(puzzle))
}