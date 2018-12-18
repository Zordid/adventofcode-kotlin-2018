package day5

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Day5AlchemicalReductionKtTest {

    private val polymer = "dabAcCaCBAcCcaDA"

    @Test
    fun preparation() {
        assertTrue('b' matches 'B')
        assertFalse('a' matches 'B')
        assertEquals("Hao Wet!", "Hallo Welt!".removeAllUnitsOf('l').joinToString(""))
    }

    @Test
    fun part1() {
        assertEquals("", "aA".polymerReaction())
        assertEquals("", "aAbB".polymerReaction())
        assertEquals("", "baAB".polymerReaction())
        assertEquals("Hao Welt!", "HalLLlo WeabBcdEeDCAlt!".polymerReaction())
        assertEquals(10, day5.part1(polymer))
    }

    @Test
    fun part2() {
        assertEquals(6, polymer.removeAllUnitsOf('a').polymerReaction().length)
        assertEquals(8, polymer.removeAllUnitsOf('b').polymerReaction().length)
        assertEquals(4, polymer.removeAllUnitsOf('c').polymerReaction().length)
        assertEquals(6, polymer.removeAllUnitsOf('d').polymerReaction().length)
        assertEquals('c' to 4, day5.part2(polymer))
    }

}