package day6

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import shared.extractAllInts
import shared.readPuzzle

internal class Day6ChronalCoordinatesKtTest {

    private val input = """
        1, 1
        1, 6
        8, 3
        3, 4
        5, 5
        8, 9""".trimIndent().split("\n")

    @Test
    fun demo1() {
        assertEquals(17, part1(input))
    }

    @Test
    fun part1() {
        assertEquals( 3569, part1(readPuzzle(6)))
    }

    @Test
    fun demo2() {
        assertEquals(16, part2(input, 32))
    }

    @Test
    fun part2() {
        assertEquals(48978, part2(readPuzzle(6)))
    }
}