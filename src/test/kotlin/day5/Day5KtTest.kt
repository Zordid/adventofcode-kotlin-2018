package day5

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Day5KtTest {

    private val polymer = "dabAcCaCBAcCcaDA"

    @Test
    fun part1() {
        assertEquals(0, "aA".polymerReaction())
        assertEquals(0, "aAbB".polymerReaction())
        assertEquals(0, "baAB".polymerReaction())
        assertEquals(10, polymer.polymerReaction())
    }

    @Test
    fun part2() {
        assertEquals(6, polymer.removeAllUnitsOf('a').polymerReaction())
        assertEquals(8, polymer.removeAllUnitsOf('b').polymerReaction())
        assertEquals(4, polymer.removeAllUnitsOf('c').polymerReaction())
        assertEquals(6, polymer.removeAllUnitsOf('d').polymerReaction())
        assertEquals('c' to 4, day5.part2(polymer))
    }

}