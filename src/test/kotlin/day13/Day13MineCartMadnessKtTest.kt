package day13

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Day13MineCartMadnessKtTest {

    private val part1Demo = """/->-\
|   |  /----\
| /-+--+-\  |
| | |  | v  |
\-+-/  \-+--/
  \------/   """.split("\n")

    private val part2Demo = """/>-<\
|   |
| /<+-\
| | | v
\>+</ |
  |   ^
  \<->/""".split("\n")

    @Test
    fun demo1() {
        val layout = extractLayout(part1Demo)
        val carts = extractCarts(part1Demo)

        assertEquals("7,3", part1(carts, layout, true))
    }

    @Test
    fun demo2() {
        val layout = extractLayout(part2Demo)
        val carts = extractCarts(part2Demo)

        assertEquals("6,4", part2(carts, layout, true))
    }
}