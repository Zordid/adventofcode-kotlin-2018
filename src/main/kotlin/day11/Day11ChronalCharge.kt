package day11

import shared.measureRuntime
import shared.readPuzzle

private fun powerLevelOf(x: Int, y: Int, serialNumber: Int): Int {
    val rackId = x + 10
    val powerLevel = ((rackId * y) + serialNumber) * rackId
    return ((powerLevel % 1000) / 100) - 5
}

fun Pair<Int, Int>.powerLevel(serialNumber: Int) = powerLevelOf(first, second, serialNumber)

fun Pair<Int, Int>.powerLevelOfRegion(serialNumber: Int) =
    allCoordinates(3, 3, first, second).sumBy { (x, y) -> powerLevelOf(x, y, serialNumber) }

fun Pair<Int, Int>.maximumPowerLevel(serialNumber: Int): Pair<Int, Int> {
    val (x, y) = this
    val maxSizeForCoordinate = Math.min(301 - x, 301 - y)

    var power = powerLevelOf(x, y, serialNumber)
    var bestPower = power
    var bestSize = 1
    for (size in 2..maxSizeForCoordinate) {
        power += (0 until size - 1).sumBy { delta ->
            powerLevelOf(x + size - 1, y + delta, serialNumber) +
                    powerLevelOf(x + delta, y + size - 1, serialNumber)
        }
        power += powerLevelOf(x + size - 1, y + size - 1, serialNumber)

        if (power > bestPower) {
            bestPower = power
            bestSize = size
        }
    }
    return (bestSize to bestPower)
}

fun allCoordinates(columns: Int, rows: Int = columns, baseCol: Int = 1, baseRow: Int = 1) = sequence {
    for (row in baseRow until baseRow + rows) {
        for (col in baseCol until baseCol + columns)
            yield(col to row)
    }
}

fun part1(serialNumber: Int) =
    allCoordinates(298)
        .maxBy { it.powerLevelOfRegion(serialNumber) }!!
        .let { "${it.first},${it.second}" }

fun part2(serialNumber: Int) =
    allCoordinates(300).asIterable()
        .map { c -> c to c.maximumPowerLevel(serialNumber) }
        .maxBy { (_, p) -> p.second }!!
        .let { "${it.first.first},${it.first.second},${it.second.first}" }

fun main(args: Array<String>) {
    val puzzle = readPuzzle(11).single().toInt()

    println(part1(puzzle))
    measureRuntime {
        println(part2(puzzle))
    }
}