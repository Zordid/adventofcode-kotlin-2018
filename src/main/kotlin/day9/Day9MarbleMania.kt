package day9

import shared.circularListIterator
import shared.extractAllPositiveInts
import shared.readPuzzle
import java.util.*

fun part1(players: Int, marbles: Int): Long {
    val circle = LinkedList(listOf(0)).circularListIterator()

    val scores = LongArray(players)
    var currentElf = 0

    for (nextMarble in 1..marbles) {
        if (nextMarble % 23 != 0) {
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
        currentElf = (currentElf + 1) % players
    }

    return scores.max()
}

fun part2(players: Int, marbles: Int): Long {
    return part1(players, 100 * marbles)
}

fun main() {
    val (players, marbles) = readPuzzle(9).single().extractAllPositiveInts().toList()

    println(part1(players, marbles))
    println(part2(players, marbles))
}