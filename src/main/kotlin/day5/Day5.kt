package day5

import shared.readPuzzle
import java.util.*

fun String.removeUnits(type: Char): String {
    return toCharArray().filter { it.toLowerCase() != type }.joinToString("")
}

fun String.polymerReaction(): Int {
    val list = LinkedList(toCharArray().toList())
    var remaining = length

//    println(list.joinToString(""))
    do {
        val it = list.listIterator()
        var prevChar = it.next()
        var reaction = false
        while (it.hasNext()) {
            val char = it.next()
            if (((prevChar.isUpperCase() && !char.isUpperCase()) || (!prevChar.isUpperCase() && char.isUpperCase())) &&
                prevChar.toLowerCase() == char.toLowerCase()
            ) {
//                println("Found $prevChar $char")
                it.remove()
                it.previous()
                it.remove()
                remaining -= 2
                reaction = true
//                println(list.joinToString(""))
                break
            }
            prevChar = char
        }
    } while (reaction && remaining > 1)
    println("Done")
//    println(list.joinToString(""))
    return remaining
}

fun part1(polymer: String): Any {
    return polymer.polymerReaction()
}

fun part2(polymer: String): Any {
    return polymer.toCharArray().filter { it.isLowerCase() }.toSet()
        .map {
            println("Removing $it")
            val p = polymer.removeUnits(it)
            val l = p.polymerReaction()
            println("${polymer.length} -> ${p.length} -> $l")
            it to l }
        .minBy { it.second }!!
}

fun main(args: Array<String>) {
    val puzzle = readPuzzle(5)

    println(part1(puzzle.single()))
    println(part2(puzzle.single()))

    println("dabAcCaCBAcCcaDA".removeUnits('a'))
}