package day13

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Day13MineCartMadnessKtTest {

    private val simpleInput = """/->-\
|   |  /----\
| /-+--+-\  |
| | |  | v  |
\-+-/  \-+--/
  \------/   """.split("\n")

    val inputCrash = """/>-<\
|   |
| /<+-\
| | | v
\>+</ |
  |   ^
  \<->/""".split("\n")

    @Test
    fun demo1() {
        val layout = extractLayout(simpleInput)
        val carts = extractCarts(simpleInput)

        part1(carts, layout)
    }

    @Test
    fun demo2() {
        val layout = extractLayout(inputCrash)
        val carts = extractCarts(inputCrash)

        assertEquals("6,4", part2(carts, layout))
    }
}