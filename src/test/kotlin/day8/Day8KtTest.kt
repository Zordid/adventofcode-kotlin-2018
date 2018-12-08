package day8

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Day8KtTest {

    private val input = "2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2"

    @Test
    fun part1() {
        assertEquals(138, part1(input))
    }

    @Test
    fun part2() {
        assertEquals(66, part2(input))
    }

}