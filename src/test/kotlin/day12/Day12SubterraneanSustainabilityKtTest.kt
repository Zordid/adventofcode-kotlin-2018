package day12

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import shared.readPuzzle

class Day12SubterraneanSustainabilityKtTest {

    private val demo = """
        initial state: #..#.#..##......###...###

        ...## => #
        ..#.. => #
        .#... => #
        .#.#. => #
        .#.## => #
        .##.. => #
        .#### => #
        #.#.# => #
        #.### => #
        ##.#. => #
        ##.## => #
        ###.. => #
        ###.# => #
        ####. => #""".trimIndent().split("\n")

    @Test
    fun demo1() {
        assertEquals(325, part1(demo))
    }

    @Test
    fun part1() {
        assertEquals(3798, part1(readPuzzle(12)))
    }

    @Test
    fun part2() {
        assertEquals(3900000002212, part2(readPuzzle(12)))
    }

    @Test
    fun solveAny1() {
        assertEquals(3798, Pots(readPuzzle(12)).solveAny(20L))
    }

    @Test
    fun solveAny2() {
        assertEquals(3900000002212, Pots(readPuzzle(12)).solveAny(50000000000L))
    }
}