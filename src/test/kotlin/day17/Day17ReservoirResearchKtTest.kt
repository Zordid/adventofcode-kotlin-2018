package day17

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import shared.readPuzzle

internal class Day17ReservoirResearchKtTest {

    @Test
    fun demo1and2() {
        val p = """
            x=495, y=2..7
            y=7, x=495..501
            x=501, y=3..7
            x=498, y=2..4
            x=506, y=1..2
            x=498, y=10..13
            x=504, y=10..13
            y=13, x=498..504
        """.trimIndent().split("\n")

        val scan = Scan(p)
        scan.print()
        scan.pourWater()
        scan.print()
        assertEquals(57, scan.countWaterReach())
        assertEquals(29, scan.countWater())
    }

    @Test
    fun part1and2() {
        val scan = Scan(readPuzzle(17))
        scan.pourWater()
        assertEquals(30380, scan.countWaterReach())
        assertEquals(25068, scan.countWater())
    }

}