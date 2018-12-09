package day9

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Day9KtTest {

    @Test
    fun part1() {
        assertEquals(32, part1(9, 25))
        assertEquals(146373, part1(13, 7999))
        assertEquals(8317, part1(10, 1618))
    }

}