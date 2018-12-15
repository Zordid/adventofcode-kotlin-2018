package day15

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Day15KtTest {

//    #######       #######       #######       #######       #######
//    #E..G.#       #E.?G?#       #E.@G.#       #E.!G.#       #E.+G.#
//    #...#.#  -->  #.?.#?#  -->  #.@.#.#  -->  #.!.#.#  -->  #...#.#
//    #.G.#G#       #?G?#G#       #@G@#G#       #!G.#G#       #.G.#G#
//    #######       #######       #######       #######       #######

    @Test
    fun testBasics() {
        var p = """#######
#E..G.#
#...#.#
#.G.#G#
#######""".split("\n")

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
    fun testFirstStep() {
        val p = """#######
#.E...#
#.....#
#...G.#
#######""".split("\n")

        val combat = Combat(p)
        val generatedMap = generateMap(combat.layout, combat.players)
        val firstElf = combat.players.first()

        assertEquals(Coordinate(2, 1), firstElf.coordinate)
        firstElf.move(generatedMap, combat.players.filter { it.type == 'G' })

        assertEquals(Coordinate(3, 1), firstElf.coordinate)


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
    fun minidemo1() {
        val p = """#######
#.G...#
#...EG#
#.#.#G#
#..G#E#
#.....#
#######""".split("\n")

        val c = Combat(p)

        assertEquals(27730, c.part1())

    }

    @Test
    fun demo1a() {
        val p = """#######
#G..#E#
#E#E.E#
#G.##.#
#...#E#
#...E.#
#######""".split("\n")
        val c = Combat(p)
        assertEquals(36334, c.part1())

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
        val p = """#######
#E..EG#
#.#G.E#
#E.##E#
#G..#.#
#..E#.#
#######""".split("\n")
        val c = Combat(p)
        assertEquals(39514, c.part1())
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
        val p = """#######
#.E...#
#.#..G#
#.###.#
#E#G#G#
#...#G#
#######""".split("\n")
        val c = Combat(p)
        assertEquals(28944, c.part1())
    }

    @Test
    fun demo1z() {
        val p = """#########
#G......#
#.E.#...#
#..##..G#
#...##..#
#...#...#
#.G...G.#
#.....G.#
#########""".split("\n")

        val c = Combat(p)
        assertEquals(18740, c.part1())

    }

    @Test
    fun moveTest() {
        val p = """#########
#G..G..G#
#.......#
#.......#
#G..E..G#
#.......#
#.......#
#G..G..G#
#########""".split("\n")

        val c = Combat(p)

        generateMap(c.layout, c.alivePlayers).printMap()
        repeat(3) {
            c.playRound()
            println("After ${it + 1}")
            generateMap(c.layout, c.alivePlayers).printMap()
        }

    }


}