package day6

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import shared.extractAllInts

internal class Day6KtTest {

    private val input = """1, 1
1, 6
8, 3
3, 4
5, 5
8, 9""".split("\n").map { it.extractAllInts().let { it.first() to it.last() } }

    @Test
    fun part1() {
        assertEquals(4 to 17, part1(input))
    }

    @Test
    fun part2() {
        assertEquals(16, part2(input, 32))
    }
}