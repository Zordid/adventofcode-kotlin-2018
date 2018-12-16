package day15

import shared.filterFirstReached
import shared.measureRuntime
import shared.readPuzzle

data class Coordinate(val x: Int, val y: Int) : Comparable<Coordinate> {
    val neighbors get() = deltas.map { transposeBy(it) }

    override fun compareTo(other: Coordinate) =
        if (y == other.y) x.compareTo(other.x) else y.compareTo(other.y)

    infix fun distanceTo(other: Coordinate) = Math.abs(y - other.y) + Math.abs(x - other.x)

    private fun transposeBy(delta: Pair<Int, Int>) = Coordinate(x + delta.first, y + delta.second)

    companion object {
        val deltas = listOf(0 to -1, -1 to 0, 1 to 0, 0 to 1)
    }
}

class Player(val type: Char, x: Int, y: Int, val attackPoints: Int = 3) : Comparable<Player> {
    var position = Coordinate(x, y)
    var hitPoints = 200
    val isAlive get() = hitPoints > 0

    fun determineTarget(map: List<CharArray>, aliveEnemies: List<Player>): Coordinate? {
        val potentialTargets = aliveEnemies.flatMap { it.freeNeighborFields(map) }
        val fastestReachableTargets = potentialTargets.map { it.x to it.y }
            .filterFirstReached(position.x to position.y, map.mapFun())
            .map { Coordinate(it.first, it.second) }

        return fastestReachableTargets.sorted().firstOrNull()
    }

    private fun move(map: List<CharArray>, aliveEnemies: List<Player>) {
        val destination = determineTarget(map, aliveEnemies) ?: return

        val stepField = freeNeighborFields(map).map { it.x to it.y }
            .filterFirstReached(destination.x to destination.y, map.mapFun())
            .map { Coordinate(it.first, it.second) }
            .sorted().first()

        position = stepField
    }

    private fun attack(aliveEnemiesInRange: List<Player>) {
        val lowestHitPoints = aliveEnemiesInRange.minBy { it.hitPoints }!!.hitPoints
        val target = aliveEnemiesInRange.filter { it.hitPoints == lowestHitPoints }.sorted().first()

        target.hitPoints -= attackPoints
    }

    fun takeTurn(layout: List<CharArray>, alivePlayers: List<Player>): Boolean {
        val aliveEnemies = alivePlayers.filter { it.type != type }
        if (aliveEnemies.isEmpty()) return false

        var aliveEnemiesInRange = enemiesInRange(aliveEnemies)
        if (aliveEnemiesInRange.isEmpty()) {
            val map = generateMap(layout, alivePlayers - this)
            move(map, aliveEnemies)
            aliveEnemiesInRange = enemiesInRange(aliveEnemies)
        }
        if (aliveEnemiesInRange.isNotEmpty())
            attack(aliveEnemiesInRange)
        return true
    }

    override fun toString() = "$type($hitPoints)"

    override fun compareTo(other: Player) = position.compareTo(other.position)

    private infix fun distanceTo(other: Player) = position distanceTo other.position

    private fun freeNeighborFields(map: List<CharArray>) = map.freeNeighbors(position)

    private fun enemiesInRange(enemies: List<Player>) = enemies.filter { this distanceTo it == 1 }

}

class Combat(puzzle: List<String>, elfPower: Int = 3, private val logging: Boolean = false) {
    private val elves = puzzle.extractCoordinatesOf('E').map { (x, y) -> Player('E', x, y, attackPoints = elfPower) }
    private val goblins = puzzle.extractCoordinatesOf('G').map { (x, y) -> Player('G', x, y) }
    val players = (elves + goblins).toList()

    private var fullRoundsPlayed = 0

    val layout = puzzle.map {
        it.replace('E', '.').replace('G', '.').toCharArray()
    }

    private val alivePlayers get() = players.filter { it.isAlive }

    fun playRound(): Boolean {
        alivePlayers.sorted().forEach { player ->
            if (player.isAlive)
                if (!player.takeTurn(layout, alivePlayers))
                    return false
        }
        fullRoundsPlayed++
        return true
    }

    fun battle(stopOnElfDeath: Boolean = false): Int {
        log()
        while (playRound()) {
            if (stopOnElfDeath && elves.any { !it.isAlive }) return -1
            log()
        }
        val elvesWin = elves.any { it.isAlive }
        if (stopOnElfDeath && !elvesWin) return -1

        val remainingHitPoints = alivePlayers.sumBy { it.hitPoints }
        if (logging) {
            println("Finished after $fullRoundsPlayed full rounds.")
            val winner = if (elvesWin) "Elves" else "Goblins"
            println("$winner win with $remainingHitPoints remaining hit points.")
        }

        return fullRoundsPlayed * remainingHitPoints
    }

    private fun log() {
        if (!logging) return
        println("After $fullRoundsPlayed rounds:")
        generateMap(layout, alivePlayers).forEach { println(it.joinToString("")) }
        println(alivePlayers.joinToString())
        println()
    }

}

fun List<CharArray>.mapFun() = { x: Int, y: Int -> this[y][x] == '.' }

fun List<CharArray>.at(c: Coordinate) = this[c.y][c.x]

fun List<CharArray>.freeNeighbors(c: Coordinate) = c.neighbors.filter { at(it) == '.' }

fun List<String>.extractCoordinatesOf(type: Char) =
    mapIndexed { y, s ->
        s.mapIndexed { x, c ->
            if (c == type) Coordinate(x, y) else null
        }.filterNotNull()
    }.flatten().sorted()

fun generateMap(layout: List<CharArray>, players: List<Player>) =
    layout.map { it.clone() }.also {
        players.forEach { p -> it[p.position.y][p.position.x] = p.type }
    }

fun part1(puzzle: List<String>) = Combat(puzzle).battle()

fun part2(puzzle: List<String>) =
    (4..Int.MAX_VALUE).asSequence().map {
        Combat(puzzle, it).battle(true)
    }.first { it > 0 }

fun main(args: Array<String>) {
    val puzzle = readPuzzle(15)

    measureRuntime {
        println(part1(puzzle))
    }
    measureRuntime {
        println(part2(puzzle))
    }
}