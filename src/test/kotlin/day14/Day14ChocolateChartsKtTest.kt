package day14

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import shared.extractAllPositiveInts
import shared.readPuzzle

internal class Day14ChocolateChartsKtTest {

    @Test
    fun testRecipeSequence() {
        val correctSequence = "3  7  1  0 [1] 0  1  2 (4) 5  1  5  8  9  1  6  7  7  9  2"
            .extractAllPositiveInts().toList()
        assertEquals(correctSequence, recipeSequencer().take(20).toList())
    }

    @Test
    fun demo1() {
        assertEquals("5158916779", part1("9"))
        assertEquals("0124515891", part1("5"))
        assertEquals("9251071085", part1("18"))
        assertEquals("5941429882", part1("2018"))
    }

    @Test
    fun part1() {
        val puzzle = readPuzzle(14).single()
        assertEquals("9411137133", part1(puzzle))
    }

    @Test
    fun demo2() {
        assertEquals(9, part2("51589"))
        assertEquals(5, part2("01245"))
        assertEquals(18, part2("92510"))
        assertEquals(2018, part2("59414"))
    }

    @Test
    fun part2() {
        val puzzle = readPuzzle(14).single()
        assertEquals(20317612, part2(puzzle))
    }

}