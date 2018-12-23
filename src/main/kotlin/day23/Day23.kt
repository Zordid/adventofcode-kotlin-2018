package day23

import shared.extractAllInts
import shared.measureRuntime
import shared.minToMaxRange
import shared.readPuzzle
import kotlin.random.Random

data class Coordinate3d(val x: Int, val y: Int, val z: Int) {

    infix fun manhattanDistanceTo(other: Coordinate3d) =
        Math.abs(x - other.x) + Math.abs(y - other.y) + Math.abs(z - other.z)

    operator fun plus(other: Coordinate3d) = Coordinate3d(x + other.x, y + other.y, z + other.z)
    operator fun minus(other: Coordinate3d) = Coordinate3d(x - other.x, y - other.y, z - other.z)
    operator fun unaryMinus() = Coordinate3d(-x, -y, -z)

    override fun toString() = "($x,$y,$z)"

}

data class NanoBot(val coordinate: Coordinate3d, val signalRadius: Int) {

    infix fun inRangeOf(other: NanoBot) = coordinate manhattanDistanceTo other.coordinate <= other.signalRadius

}

fun coordinateInRange(coordinate: Coordinate3d, bot: NanoBot) =
    coordinate manhattanDistanceTo bot.coordinate <= bot.signalRadius

fun part1(puzzle: List<List<Int>>): Any {
    val bots = puzzle.map { (x, y, z, r) -> NanoBot(Coordinate3d(x, y, z), r) }

    val strongest = bots.maxBy { it.signalRadius }!!

    return bots.count { it inRangeOf strongest }
}

class Optimizer(val bots: List<NanoBot>) {

    val origin = Coordinate3d(0, 0, 0)
    val boundedBy = bots.map { it.coordinate } + listOf(origin)
    val boundsX = boundedBy.map { it.x }.minToMaxRange()!!
    val boundsY = boundedBy.map { it.y }.minToMaxRange()!!
    val boundsZ = boundedBy.map { it.z }.minToMaxRange()!!

    var bestPoint = origin
    var bestCount = 0
    var bestDistance = 0

    private fun testPoint(point: Coordinate3d) {
        val c = bots.count { coordinateInRange(point, it) }
        if (c >= bestCount) {
            val d = point manhattanDistanceTo origin
            if (c > bestCount || d < bestDistance) {
                updateBest(point, d, c)

                if (c > 800) {
                    improve(point, 20000, 2000)
                }

                println("Moving on")
            }
        }
    }

    tailrec fun improve(pStart: Coordinate3d, rangeToTry: Int, increment: Int) {
        val range = -rangeToTry until rangeToTry step increment
        var p = pStart
        start@
        for (xd in range) {
            for (yd in range) {
                for (zd in range) {
                    val delta = Coordinate3d(xd, yd, zd)
                    val p2 = p + delta
                    val c2 = bots.count { coordinateInRange(p2, it) }
                    if (c2 >= bestCount) {
                        val d2 = p2 manhattanDistanceTo origin
                        if (c2 > bestCount || d2 < bestDistance) {
                            updateBest(p2, d2, c2)
                            p = bestPoint
                            continue@start
                        }
                    }
                }
            }
        }
        if (increment == 1)
            return
        improve(p, increment + 1, Math.max(1, increment / 5))
    }

    private fun updateBest(point: Coordinate3d, distance: Int, count: Int) {
        bestDistance = distance
        bestCount = count
        bestPoint = point
        println("$bestCount - $bestDistance - $bestPoint")
    }

    fun optimize() {
        while (true) {
            testPoint(randomPoint())
        }
    }

    private fun randomPoint(): Coordinate3d = Coordinate3d(
        Random.nextInt(boundsX.start, boundsX.endInclusive),
        Random.nextInt(boundsY.start, boundsY.endInclusive),
        Random.nextInt(boundsZ.start, boundsZ.endInclusive)
    )

}


fun part2(puzzle: List<List<Int>>): Any {
    val bots = puzzle.map { (x, y, z, r) -> NanoBot(Coordinate3d(x, y, z), r) }

    //bots.sortedBy { it.signalRadius }.asReversed().forEach { println(it) }

    val o = Optimizer(bots)
    return o.optimize()
}


fun main(args: Array<String>) {
    val puzzle = readPuzzle(23) { it.extractAllInts().toList() }

    measureRuntime {
        println(part1(puzzle))
        println(part2(puzzle))
    }
}