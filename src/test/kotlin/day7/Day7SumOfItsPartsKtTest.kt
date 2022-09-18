package day7

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day7SumOfItsPartsKtTest {

    private val input = """Step C must be finished before step A can begin.
Step C must be finished before step F can begin.
Step A must be finished before step B can begin.
Step A must be finished before step D can begin.
Step B must be finished before step E can begin.
Step D must be finished before step E can begin.
Step F must be finished before step E can begin.""".split("\n")

    @Test
    fun part1() {
        assertEquals("CABDFE", part1(input))
    }

    @Test
    fun part2() {
        assertEquals(15, part2(input, 2, 0))
    }

    @Test
    fun part2new() {
        assertEquals(15, part2(prepareData(input), 2, 0))
    }
}