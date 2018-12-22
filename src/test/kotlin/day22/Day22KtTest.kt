package day22

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import shared.Coordinate
import shared.toY

internal class Day22KtTest {

    @Test
    fun demo1() {
        val p = """
            510
            10, 10
        """.trimIndent().split("\n")

        val m = CaveSystem(p)
        assertEquals(114, m.totalRiskLevel())
    }

    @Test
    fun demo2() {
        val p = """
            510
            10, 10
        """.trimIndent().split("\n")

        val m = CaveSystem(p)
        m.draw()
        val (dist, prev) = m.minimumTravelLengthDetails()
        dist.keys.filter { it.c == 10 toY 10 }.forEach { println("$it: ${dist[it]}") }

        println("My path:")
        var x = State(Equipment.Torch, 10 toY 10)
        val moves = mutableListOf<State>()
        while (prev[x] != null) {
            moves.add(x)
            x = prev[x]!!
        }
        moves.add(x)

        var cost = 0
        val path = mutableSetOf<Coordinate>()
        moves.reversed().windowed(2, 1).forEach { (from, to) ->
            if (from.equipment != to.equipment) {
                m.draw(from.c, path)
                println("Switch tool from ${from.equipment} to ${to.equipment}: 7 Min")
                cost += 7
            }
            path.add(to.c)
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
}