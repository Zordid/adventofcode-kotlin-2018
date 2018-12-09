package day9

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Day9MarbleManiaKtTest {

    @Test
    fun part1() {
        assertEquals(32, part1(9, 25))
        assertEquals(8317, part1(10, 1618))
        assertEquals(146373, part1(13, 7999))
        assertEquals(2764, part1(17, 1104))
        assertEquals(54718, part1(21, 6111))
        assertEquals(37305, part1(30, 5807))
    }

}