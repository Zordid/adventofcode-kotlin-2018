package day11

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Day11ChronalChargeKtTest {

    @Test
    fun powerLevelOfCells() {
        assertEquals(4, (3 to 5).powerLevel(8))
        assertEquals(-5, (122 to 79).powerLevel(57))
        assertEquals(0, (217 to 196).powerLevel(39))
        assertEquals(4, (101 to 153).powerLevel(71))
    }

    @Test
    fun part1() {
        assertEquals("33,45", part1(18))
        assertEquals("21,61", part1(42))
        assertEquals("21,22", part1(7511))
    }

    @Test
    fun part2() {
        assertEquals("90,269,16", part2(18))
        assertEquals("232,251,12", part2(42))
        assertEquals("235,288,13", part2(7511))
    }

}