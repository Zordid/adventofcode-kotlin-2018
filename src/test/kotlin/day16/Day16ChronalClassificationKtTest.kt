package day16

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import shared.readPuzzle

internal class Day16ChronalClassificationKtTest {

    @Test
    fun demo1() {
        val input = """
            Before: [3, 2, 1, 1]
            9 2 1 2
            After:  [3, 2, 2, 1]
        """.trimIndent().split("\n")

        assertEquals(listOf(9 to setOf("addi", "mulr", "seti")), readSamples(input))
    }

    @Test
    fun part1() {
        assertEquals(596, part1(readPuzzle(16)))
    }

    @Test
    fun part2() {
        assertEquals(554, part2(readPuzzle(16)))
    }
}