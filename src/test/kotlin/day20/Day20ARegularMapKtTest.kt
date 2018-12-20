package day20

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import shared.readPuzzle

internal class Day20ARegularMapKtTest {

    @Test
    fun demo1() {
        assertEquals(3, part1("^WNE$"))
        assertEquals(10, part1("^ENWWW(NEEE|SSE(EE|N))\$"))
        assertEquals(18, part1("^ENNWSWW(NEWS|)SSSEEN(WNSE|)EE(SWEN|)NNN\$"))
        assertEquals(23, part1("^ESSWWN(E|NNENN(EESS(WNSE|)SSS|WWWSSSSE(SW|NNNE)))\$"))
        assertEquals(31, part1("^WSSEESWWWNW(S|NENNEEEENN(ESSSSW(NWSW|SSEN)|WSWWN(E|WWS(E|SS))))\$"))
    }

    @Test
    fun part1and2() {
        val map = NorthPoleMap(readPuzzle(20).single())
        assertEquals(3656, map.solvePart1())
        assertEquals(8430, map.solvePart2())
    }

    private fun part1(p: String) = NorthPoleMap(p).solvePart1()

}