package day9

import shared.circularListIterator
import shared.readPuzzle
import java.util.*

fun part1(players: Int, marbles: Long): Any {
    val l = LinkedList<Long>()
    l.add(0)

    val circle = l.circularListIterator()

    val elves = LongArray(players)

    var nextMarble = 1L
    while (nextMarble <= marbles) {
        elves.indices.forEach { elf ->
            if (nextMarble <= marbles) {
                if (nextMarble % 23L != 0L) {
                    //println("Adding marble $nextMarble")
                    circle.next()
                    circle.add(nextMarble)
                } else {
//                    print("Elf #$elf is lucky, gets $nextMarble and ")
                    repeat(7) { circle.previous() }
                    val takenMarble = circle.previous()
                    circle.remove()
                    circle.next()
//                    println("$takenMarble: new score is ${elves[elf]}")
                    val wins = nextMarble + takenMarble
                    elves[elf]+=  wins
                    //println("${elf to wins}, max player is ${elves.maxBy { it.value }!!.key}")
                }
            }
            nextMarble++
//            println(l)
        }
    }

    return elves.max()!!
}

fun part2(players: Int, marbles: Long): Any {
    return part1(players, 100L*marbles)
}

fun main(args: Array<String>) {
    val puzzle = readPuzzle(9)

    println(part1(435, 71184))
    println(part2(435, 71184))
}