package day24

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import shared.readPuzzle

internal class Day24ImmuneSystemSimulatorKtTest {

    @Test
    fun demo() {
        val p = """
            Immune System:
            17 units each with 5390 hit points (weak to radiation, bludgeoning) with an attack that does 4507 fire damage at initiative 2
            989 units each with 1274 hit points (immune to fire; weak to bludgeoning, slashing) with an attack that does 25 slashing damage at initiative 3

            Infection:
            801 units each with 4706 hit points (weak to radiation) with an attack that does 116 bludgeoning damage at initiative 1
            4485 units each with 2961 hit points (immune to radiation; weak to fire, cold) with an attack that does 12 slashing damage at initiative 4
        """.trimIndent().split("\n")

        assertEquals(5216, part1(p))
    }

    @Test
    fun demo2() {
        val p = """
            Immune System:
            17 units each with 5390 hit points (weak to radiation, bludgeoning) with an attack that does 4507 fire damage at initiative 2
            989 units each with 1274 hit points (immune to fire; weak to bludgeoning, slashing) with an attack that does 25 slashing damage at initiative 3

            Infection:
            801 units each with 4706 hit points (weak to radiation) with an attack that does 116 bludgeoning damage at initiative 1
            4485 units each with 2961 hit points (immune to radiation; weak to fire, cold) with an attack that does 12 slashing damage at initiative 4
        """.trimIndent().split("\n")

        assertEquals(51, part2(p))
    }

    @Test
    fun part1() {
        assertEquals(16086, part1(readPuzzle(24)))
    }

    @Test
    fun part2() {
        assertEquals(3957, part2(readPuzzle(24)))
    }


}