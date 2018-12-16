package day16

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Day16KtTest {

    @Test
    fun demo1() {
        val input = """
            Before: [3, 2, 1, 1]
            9 2 1 2
            After:  [3, 2, 2, 1]

        """.trimIndent().split("\n")

        part1(input)
    }

}