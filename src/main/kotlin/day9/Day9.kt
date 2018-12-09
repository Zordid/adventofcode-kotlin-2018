package day9

import shared.circularListIterator
import shared.extractAllPositiveInts
import shared.readPuzzle
import java.util.*

fun part1(players: Int, marbles: Int): Long {
    val circle = LinkedList<Int>(listOf(0)).circularListIterator()

    val scores = LongArray(players)
    var nextMarble = 1
    var currentElf = 0

    while (nextMarble <= marbles) {
        if (nextMarble % 23L != 0L) {
            circle.next()
            circle.add(nextMarble)
        } else {
            repeat(7) { circle.previous() }
            val takenMarble = circle.previous()
            circle.remove()
            circle.next()

            val wins = nextMarble + takenMarble
            scores[currentElf] += wins.toLong()
        }
        nextMarble++
        currentElf = (currentElf + 1) % players
    }

    return scores.max()!!
}

fun part2(players: Int, marbles: Int): Long {
    return part1(players, 100 * marbles)
}

fun main(args: Array<String>) {
    val (players, marbles) = readPuzzle(9).single().extractAllPositiveInts().toList()

    println(part1(players, marbles))
    println(part2(players, marbles))
}