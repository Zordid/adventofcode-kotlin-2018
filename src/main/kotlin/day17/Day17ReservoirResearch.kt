package day17

import shared.extractAllPositiveInts
import shared.measureRuntime
import shared.readPuzzle

abstract class Clay {
    abstract val minY: Int
    abstract val maxY: Int
    abstract val minX: Int
    abstract val maxX: Int
    abstract fun isClay(tX: Int, tY: Int): Boolean
}

data class HClay(val x: Int, val y: IntRange) : Clay() {
    override val minY = y.start
    override val maxY = y.endInclusive
    override val minX = x
    override val maxX = x
    override fun isClay(tX: Int, tY: Int): Boolean {
        return (tX == x) && (tY in y)
    }
}

data class VClay(val x: IntRange, val y: Int) : Clay() {
    override val minY = y
    override val maxY = y
    override val minX = x.start
    override val maxX = x.endInclusive
    override fun isClay(tX: Int, tY: Int): Boolean {
        return (tX in x) && (tY == y)
    }
}

enum class Element(val c: Char) { Free('.'), Clay('#'), Soaked('|'), Water('~');

    val blocksWater: Boolean get() = this == Clay || this == Water
    val isWet: Boolean get() = this == Water || this == Soaked
    override fun toString(): String = c.toString()
}

class Scan(puzzle: List<String>) {

    private val clays = puzzle
        .map { it[0] to it.extractAllPositiveInts().toList() }
        .map { (c, n) -> if (c == 'x') HClay(n[0], n[1]..n[2]) else VClay(n[1]..n[2], n[0]) }

    private val minY = clays.minBy { it.minY }!!.minY
    private val maxY = clays.maxBy { it.maxY }!!.maxY
    private val minX = clays.minBy { it.minX }!!.minX - 1
    private val maxX = clays.maxBy { it.maxX }!!.maxX + 1

    val map: Array<Array<Element>> = Array(maxY + 1) { Array(maxX - minX + 1) { Element.Free } }

    init {
        for (y in 0..maxY) {
            for (x in minX..maxX) {
                if (clays.any { it.isClay(x, y) })
                    this[x, y] = Element.Clay
            }
        }
    }

    operator fun get(x: Int, y: Int) = if (y < map.size) map[y][x - minX] else Element.Free

    operator fun set(x: Int, y: Int, e: Element) {
        map[y][x - minX] = e
    }

    private fun leftRight(x: Int, y: Int): Pair<Int, Int> {
        val left =
            (x downTo minX).first { this[it, y].blocksWater || !this[it, y + 1].blocksWater }
        val right =
            (x..maxX).first { this[it, y].blocksWater || !this[it, y + 1].blocksWater }
        return left to right
    }

    fun pourWater(x: Int = 500, y: Int = 0) {
        if (this[x, y].isWet) return

        var dropY = y
        while (!this[x, dropY + 1].blocksWater && dropY <= maxY) {
            this[x, dropY] = Element.Soaked
            dropY++
        }
        val alreadySoaked = this[x, dropY] == Element.Soaked
        if (dropY + 1 <= maxY && !alreadySoaked) {
            do {
                val (left, right) = leftRight(x, dropY)
                val openLeft = !this[left, dropY].blocksWater
                val openRight = !this[right, dropY].blocksWater
                val closed = !openLeft && !openRight

                if (closed) {
                    (left + 1 until right).forEach { this[it, dropY] = Element.Water }
                } else {
                    (left + 1 until right).forEach { this[it, dropY] = Element.Soaked }
                    if (openLeft) {
                        pourWater(left, dropY)
                    }
                    if (openRight) {
                        pourWater(right, dropY)
                    }
                }
                dropY--
            } while (closed)
        }
    }

    fun print() {
        for (y in 0..maxY) {
            for (x in minX..maxX) {
                if (x == 500 && y == 0)
                    print('+')
                else {
                    val element = this[x, y]
                    print(element)
                }
            }
            println()
        }
        println()
    }

    private fun count(predicate: (Element) -> Boolean) = (minY..maxY).sumBy { y ->
        (minX..maxX).count { predicate(this[it, y]) }
    }

    fun countWater() = count { it == Element.Water }

    fun countWaterReach() = count { it.isWet }

}

fun part1(puzzle: List<String>): Int {
    val scan = Scan(puzzle)
    scan.pourWater()
    return scan.countWaterReach()
}

fun part2(puzzle: List<String>): Int {
    val scan = Scan(puzzle)
    scan.pourWater()
    return scan.countWater()
}

fun main(args: Array<String>) {
    val puzzle = readPuzzle(17)

    measureRuntime {
        println(part1(puzzle))
        println(part2(puzzle))
    }
}