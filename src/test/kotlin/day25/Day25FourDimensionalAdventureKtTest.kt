package day25

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Day25FourDimensionalAdventureKtTest {

    @Test
    fun demo1() {
        val p = """
             0,0,0,0
             3,0,0,0
             0,3,0,0
             0,0,3,0
             0,0,0,3
             0,0,0,6
             9,0,0,0
            12,0,0,0
        """.trimIndent().split("\n")

        assertEquals(2, part1(p))
    }

}