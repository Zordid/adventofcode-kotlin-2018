package day6

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Day6KtTest {

    val input = """1, 1
1, 6
8, 3
3, 4
5, 5
8, 9""".split("\n")

    @Test
    fun part2() {
        assertEquals(16, part2(input, 32))
    }
}