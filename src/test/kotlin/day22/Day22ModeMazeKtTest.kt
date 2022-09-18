package day22

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import shared.Coordinate
import shared.readPuzzle

internal class Day22ModeMazeKtTest {

    @Test
    fun demo1() {
        val p = """
            510
            10, 10
        """.trimIndent().split("\n")

        val m = CaveNavigator(p)
        val rl = m.map.totalRiskLevel()
        println("Calculations: ${m.map.totalCalc}")
        m.map.totalCalc = 0
        m.map.draw()
        println("Calculations: ${m.map.totalCalc}")
        assertEquals(114, m.map.totalRiskLevel())
    }

    @Test
    fun part1() {
        assertEquals(11972, part1(readPuzzle(22)))
    }

    @Test
    fun demo2() {
        val p = """
            510
            10, 10
        """.trimIndent().split("\n")

        val m = CaveNavigator(p)
        m.map.draw()
        val (_, path) = m.minimumTravelLengthAndPath()

        var cost = 0
        val intermediatePath = mutableListOf<Coordinate>()
        path.windowed(2, 1).forEach { (from, to) ->
            if (from.equipment != to.equipment) {
                m.map.draw(from.c, intermediatePath)
                println("Switch tool from ${from.equipment} to ${to.equipment}: 7 Min")
                cost += 7
            }
            intermediatePath.add(from.c)
            when {
                from.c.y < to.c.y -> print("down")
                from.c.y > to.c.y -> print("up")
                from.c.x < to.c.x -> print("right")
                from.c.x > to.c.x -> print("left")
                else -> print("straaaaange! $from -> $to")
            }
            println(" to ${to.c}")
            cost++
        }

        println("Total cost QC: $cost")


        assertEquals(45, part2(p))
    }

    @Test
    fun part2() {
        assertEquals(1092, part2(readPuzzle(22)))
    }
}