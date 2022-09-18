package day3

import shared.readPuzzle

data class Claim(val id: String, val left: Int, val top: Int, val width: Int, val height: Int) {
    private val right = left + width - 1
    private val bottom = top + height - 1

    fun cut(fabric: List<ByteArray>) {
        for (line in top until (top + height)) {
            for (col in left until (left + width)) {
                fabric[line][col]++
            }
        }
    }

    infix fun overlaps(other: Claim): Boolean {
        return !(left > other.right || right < other.left) &&
                !(top > other.bottom || bottom < other.top)
    }

}

private val claimDefinition = Regex("#(\\d+) @ (\\d+),(\\d+): (\\d+)x(\\d+)")

fun String.toClaim(): Claim {
    val (id, left, top, width, height) =
        claimDefinition.matchEntire(this)?.destructured ?: throw UnsupportedOperationException(this)
    return Claim(id, left.toInt(), top.toInt(), width.toInt(), height.toInt())
}

fun part1(claims: List<Claim>): Int {
    val fabric = (0..999).map { ByteArray(1000) { 0 } }
    claims.forEach { it.cut(fabric) }
    return fabric.sumOf { it.count { it > 1 } }
}

fun part2(claims: List<Claim>): Claim {
    return claims.single { a -> (claims - a).all { b -> !a.overlaps(b) } }
}

fun main() {
    val claims = readPuzzle(3) { it.toClaim() }

    println(part1(claims))
    println(part2(claims))
}