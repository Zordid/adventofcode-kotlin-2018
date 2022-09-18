package day19

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import shared.readPuzzle

internal class Day19GoWithTheFlowKtTest {

    @Test
    fun demo1() {
        val p = """
            #ip 0
            seti 5 0 1
            seti 6 0 2
            addi 0 1 0
            addr 1 2 3
            setr 1 0 0
            seti 8 0 4
            seti 9 0 5
        """.trimIndent().split("\n")

        assertEquals(6, part1(p))
    }

    @Test
    fun part1() {
        assertEquals(912, part1(readPuzzle(19)))
    }

    @Test
    fun part2() {
        assertEquals(10576224, part2(1))
    }
}