package day3

import shared.readPuzzle

data class Claim(val id: String, val left: Int, val top: Int, val width: Int, val height: Int) {
    private val right = left + width - 1
    private val bottom = top + height - 1

    fun cut(fabric: List<CharArray>) {
        for (line in top until (top + height)) {
            for (col in left until (left + width)) {
                fabric[line][col] = if (fabric[line][col] == '.') id[0] else 'X'
            }
        }
    }

    infix fun overlaps(other: Claim): Boolean {
        return !(left > other.right || other.left > right) and
                !(top > other.bottom || other.top > bottom)
    }

    fun covers(line: Int, col: Int): Boolean {
        return (line in top..bottom) and (col in left..right)
    }

}

fun String.toClaim(): Claim {
    val regex = Regex("#(\\d+) @ (\\d+),(\\d+): (\\d+)x(\\d+)")
    val (id, left, top, width, height) =
            regex.matchEntire(this)?.destructured ?: throw UnsupportedOperationException(this)
    return Claim(id, left.toInt(), top.toInt(), width.toInt(), height.toInt())
}

fun part1(claims: List<Claim>): Int {
//    return (0..999).map { line ->
//        (0..999).count { col ->
//            claims.count { it.covers(line, col) } > 1
//        }
//    }.sum()
    val fabric = (0..999).map { CharArray(1000) { '.' } }
    claims.forEach { it.cut(fabric) }
    return fabric.map { it.count { it == 'X' } }.sum()
}

fun part2(claims: List<Claim>): Claim {
    return claims.single { a -> (claims - a).all { b -> !a.overlaps(b) } }
}

fun main(args: Array<String>) {
    val puzzle = readPuzzle(3)

    println(part1(puzzle.map { it.toClaim() }))
    println(part2(puzzle.map { it.toClaim() }))
}