package day11

import shared.readPuzzle

fun Pair<Int, Int>.powerLevel(serialNumber: Int): Int {
    val (x, y) = this
    val rackId = x + 10
    var powerLevel = rackId * y
    powerLevel += serialNumber
    powerLevel *= rackId
    powerLevel = (powerLevel / 100) - (powerLevel / 1000) * 10
    powerLevel -= 5
    return powerLevel
}

fun Pair<Int, Int>.powerLevelOfRegion(serialNumber: Int, size: Int = 3): Int {
    val (x, y) = this
    return (y until y + size).flatMap { y -> (x until x + size).map { x -> x to y } }
        .sumBy { it.powerLevel(serialNumber) }
}

fun Pair<Int, Int>.maxBySize(serialNumber: Int): Pair<Int, Int> {
    val (x, y) = this
    val maxSize = Math.min(301-x, 301-y)

    var power = powerLevelOfRegion(serialNumber, 1)
    var bestPower = power
    var bestSize = 1
    for(s in 2 .. maxSize) {
        power += (1..s).sumBy { dy-> (x+s-1 to y+dy-1).powerLevel(serialNumber)}
        power += (1..s -1).sumBy { dx -> (x+dx-1 to y+s-1).powerLevel(serialNumber)}

        if (power > bestPower) {
            bestPower = power
            bestSize = s
        }
    }
    return (bestSize to bestPower)
}

fun part1(serialNumber: Int): Any {
    return (1..298).flatMap { y -> (1..298).map { x -> x to y } }.maxBy { it.powerLevelOfRegion(serialNumber) }!!
}

fun part2(serialNumber: Int): Any {
    val coordinates = (1..300).flatMap { y -> (1..300).map { x -> x to y } }
    return coordinates.map { c-> c to c.maxBySize(serialNumber)}.maxBy { (c, p)-> p.second }!!
}

fun main(args: Array<String>) {
    val puzzle = readPuzzle(11).single().toInt()

    println(part1(puzzle))
    println(part2(puzzle))
}