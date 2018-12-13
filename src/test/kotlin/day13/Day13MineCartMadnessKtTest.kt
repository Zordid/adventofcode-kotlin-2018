package day13

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import shared.readPuzzle

internal class Day13MineCartMadnessKtTest {

    private val part1Demo = """/->-\
|   |  /----\
| /-+--+-\  |
| | |  | v  |
\-+-/  \-+--/
  \------/   """.split("\n")

    private val part2Demo = """/>-<\
|   |
| /<+-\
| | | v
\>+</ |
  |   ^
  \<->/""".split("\n")

    @Test
    fun demo1() {
        assertEquals("7,3", part1(part1Demo, true))
    }

    @Test
    fun demo2() {
        assertEquals("6,4", part2(part2Demo, true))
    }

    @Test
    fun part1() {
        val puzzle = readPuzzle(13)
        assertEquals("32,99", part1(puzzle))
    }

    @Test
    fun part2() {
        val puzzle = readPuzzle(13)
        assertEquals("56,31", part2(puzzle))
    }
}