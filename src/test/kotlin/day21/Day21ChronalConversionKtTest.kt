package day21

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import shared.ElfDeviceCpu
import shared.readPuzzle

internal class Day21ChronalConversionKtTest {

    @Test
    fun part1() {
        assertEquals(3173684, part1(ElfDeviceCpu(readPuzzle(21))))
    }

    @Test
    fun part2() {
        assertEquals(12464363, part2KotlinCode())
    }

}