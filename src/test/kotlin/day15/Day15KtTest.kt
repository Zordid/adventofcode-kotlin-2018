package day15

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import shared.readPuzzle

internal class Day15KtTest {

    //    #######       #######       #######       #######       #######
    //    #E..G.#       #E.?G?#       #E.@G.#       #E.!G.#       #E.+G.#
    //    #...#.#  -->  #.?.#?#  -->  #.@.#.#  -->  #.!.#.#  -->  #...#.#
    //    #.G.#G#       #?G?#G#       #@G@#G#       #!G.#G#       #.G.#G#
    //    #######       #######       #######       #######       #######
    @Test
    fun testBasics() {
        val p = """
                #######
                #E..G.#
                #...#.#
                #.G.#G#
                #######
                """.trimIndent().split("\n")

        val combat = Combat(p)
        val generatedMap = generateMap(combat.layout, combat.players)
        assertEquals(p, generatedMap.map { it.joinToString("") })

        assertEquals(
            Coordinate(3, 1),
            combat.players[0].determineTarget(generatedMap, combat.players.filter { it.type == 'G' })
        )
    }

    //    #######       #######       #######       #######       #######
    //    #.E...#       #.E...#       #.E...#       #4E212#       #..E..#
    //    #...?.#  -->  #...!.#  -->  #...+.#  -->  #32101#  -->  #.....#
    //    #..?G?#       #..!G.#       #...G.#       #432G2#       #...G.#
    //    #######       #######       #######       #######       #######
    @Test
    fun firstStepTowardsEnemy() {
        val p = """
                #######
                #.E...#
                #.....#
                #...G.#
                #######
                """.trimIndent().split("\n")

        val combat = Combat(p)
        val firstElf = combat.players.first()

        assertEquals(Coordinate(2, 1), firstElf.position)
        combat.playRound()
        assertEquals(Coordinate(3, 1), firstElf.position)
    }

    //    #########       #########
    //    #G......#       #.G.....#   G(137)
    //    #.E.#...#       #G.G#...#   G(200), G(200)
    //    #..##..G#       #.G##...#   G(200)
    //    #...##..#  -->  #...##..#
    //    #...#...#       #.G.#...#   G(200)
    //    #.G...G.#       #.......#
    //    #.....G.#       #.......#
    //    #########       #########
    @Test
    fun miniDemo1() {
        val p = """
                #######
                #.G...#
                #...EG#
                #.#.#G#
                #..G#E#
                #.....#
                #######
                """.trimIndent().split("\n")

        assertEquals(27730, Combat(p).battle())
    }

    @Test
    fun demo1a() {
        val p = """
                #######
                #G..#E#
                #E#E.E#
                #G.##.#
                #...#E#
                #...E.#
                #######
                """.trimIndent().split("\n")
        assertEquals(36334, Combat(p).battle())
    }

    //    #######       #######
    //    #E..EG#       #.E.E.#   E(164), E(197)
    //    #.#G.E#       #.#E..#   E(200)
    //    #E.##E#  -->  #E.##.#   E(98)
    //    #G..#.#       #.E.#.#   E(200)
    //    #..E#.#       #...#.#
    //    #######       #######
    @Test
    fun demo1b() {
        val p = """
                #######
                #E..EG#
                #.#G.E#
                #E.##E#
                #G..#.#
                #..E#.#
                #######
                """.trimIndent().split("\n")
        assertEquals(39514, Combat(p).battle())
    }

    //    #######       #######
    //    #.E...#       #.....#
    //    #.#..G#       #.#G..#   G(200)
    //    #.###.#  -->  #.###.#
    //    #E#G#G#       #.#.#.#
    //    #...#G#       #G.G#G#   G(98), G(38), G(200)
    //    #######       #######
    @Test
    fun demo1y() {
        val p = """
                #######
                #.E...#
                #.#..G#
                #.###.#
                #E#G#G#
                #...#G#
                #######
                """.trimIndent().split("\n")
        assertEquals(28944, Combat(p).battle())
    }

    @Test
    fun demo1z() {
        val p = """
                #########
                #G......#
                #.E.#...#
                #..##..G#
                #...##..#
                #...#...#
                #.G...G.#
                #.....G.#
                #########
                """.trimIndent().split("\n")
        assertEquals(18740, Combat(p).battle())
    }

    @Test
    fun part1() {
        assertEquals(181952, part1(readPuzzle(15)))
    }

    @Test
    fun part2() {
        assertEquals(47296, part2(readPuzzle(15)))
    }

}