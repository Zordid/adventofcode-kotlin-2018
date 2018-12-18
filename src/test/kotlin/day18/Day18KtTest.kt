package day18

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Day18KtTest {

    @Test
    fun demo1() {
        val p = """
            .#.#...|#.
            .....#|##|
            .|..|...#.
            ..|#.....#
            #.#|||#|#|
            ...#.||...
            .|....|...
            ||...#|.#|
            |.||||..|.
            ...#.|..|.
        """.trimIndent().split("\n")

        part1(p)
    }

}