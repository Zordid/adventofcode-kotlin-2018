package day18

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import shared.readPuzzle

internal class Day18SettlersOfTheNorthPoleKtTest {

    @Test
    fun demo1() {
        val p = """
            .#.#...|#.
            .....#|##|
            .|..|...#.
            ..|#.....#
            #.#|||#|#|
            ...#.||...
            .|....|...
            ||...#|.#|
            |.||||..|.
            ...#.|..|.
        """.trimIndent().split("\n")

        assertEquals(1147, part1(p))
    }

    @Test
    fun part1() {
        assertEquals(763804, part1(readPuzzle(18), true))
    }

    @Test
    fun part2() {
        assertEquals(188400, part2(readPuzzle(18)))
    }

}