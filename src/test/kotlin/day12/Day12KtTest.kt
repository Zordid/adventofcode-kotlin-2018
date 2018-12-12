package day12

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Day12KtTest {

    private val input = """initial state: #..#.#..##......###...###

...## => #
..#.. => #
.#... => #
.#.#. => #
.#.## => #
.##.. => #
.#### => #
#.#.# => #
#.### => #
##.#. => #
##.## => #
###.. => #
###.# => #
####. => #""".split("\n")

    @Test
    fun part1() {
        assertEquals(325L, part1(input))
    }

    @Test
    fun demo() {
        assertEquals(325L, Pots(input).solvePart1())
    }
}