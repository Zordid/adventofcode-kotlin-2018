package day15

import day11.allCoordinates
import shared.breadthFirstSearch
import shared.readPuzzle

data class Coordinate(val x: Int, val y: Int) : Comparable<Coordinate> {
    override fun compareTo(other: Coordinate) =
        if (y == other.y) x.compareTo(other.x) else y.compareTo(other.y)

    infix fun distanceTo(other: Coordinate) = Math.abs(y - other.y) + Math.abs(x - other.x)
    fun transpose(delta: Pair<Int, Int>) = Coordinate(x + delta.first, y + delta.second)
}

fun List<CharArray>.freeNeighbors(c: Coordinate) = deltas.map { Coordinate(c.x + it.first, c.y + it.second) }
    .filter { (x, y) -> this[y][x] == '.' }

val deltas = listOf(0 to -1, -1 to 0, 1 to 0, 0 to 1)

fun List<CharArray>.at(c: Coordinate) = this[c.y][c.x]

fun List<CharArray>.shortestPath(s: Coordinate, d: Coordinate) =
    breadthFirstSearch(s, { p -> this.freeNeighbors(p) }) { it == d }

class Player(val type: Char, x: Int, y: Int, var hitPoints: Int = 200, var attackPoints: Int = 3) : Comparable<Player> {
    var coordinate = Coordinate(x, y)

    val isAlive get() = hitPoints > 0

    override fun compareTo(other: Player) = coordinate.compareTo(other.coordinate)

    infix fun distanceTo(other: Player) = coordinate distanceTo other.coordinate

    fun freeNeighborFields(map: List<CharArray>) =
        deltas.map { coordinate.transpose(it) }.filter { map.at(it) == '.' }

    fun enemiesInRange(enemies: List<Player>) = enemies.filter { distanceTo(it) == 1 }

    fun determineTarget(map: List<CharArray>, enemies: List<Player>): Coordinate? {
        val paths = enemies
            .flatMap { it.freeNeighborFields(map) }
            .map { it -> it to map.shortestPath(coordinate, it) }
            .filter { it.second.isNotEmpty() }
            .sortedBy { it.second.size }
        if (paths.isEmpty())
            return null

        val shortestDistance = paths.first().second.size
        return paths.filter { it.second.size == shortestDistance }.sortedBy { it.first }.firstOrNull()?.first
    }

    fun move(map: List<CharArray>, enemies: List<Player>) {
        val destination = determineTarget(map, enemies) ?: return
        val possibleNeighborsWithPath =
            freeNeighborFields(map).map { it to map.shortestPath(it, destination) }.sortedBy { it.second.size }
        val min = possibleNeighborsWithPath.first().second.size

        val step = possibleNeighborsWithPath.filter { it.second.size == min }.sortedBy { it.first }.first().first
        coordinate = step
    }

    fun attack(enemies: List<Player>) {
        val lowestHitPoints = enemies.filter { it.isAlive }.minBy { it.hitPoints }!!.hitPoints
        val target = enemies.filter { it.hitPoints == lowestHitPoints }.sorted().first()

        target.hitPoints -= attackPoints
    }

    fun takeTurn(layout: List<CharArray>, players: List<Player>): Boolean {
        val enemies = players.filter { it.isAlive && it.type != type }
        if (enemies.isEmpty()) return false

        val map = generateMap(layout, players.filter { it.isAlive } - this)
        var enemiesInRange = enemiesInRange(enemies)
        if (enemiesInRange.isEmpty()) {
            move(map, enemies)
            enemiesInRange = enemiesInRange(enemies)
        }
        if (enemiesInRange.isNotEmpty())
            attack(enemiesInRange)
        return true
    }

    override fun toString(): String {
        return "$type($hitPoints)"
    }

}


fun List<String>.findAll(c: Char) =
    allCoordinates(this[0].length, size, 0, 0).mapNotNull { (x, y) ->
        if (this[y][x] == c) Coordinate(x, y) else null
    }.sorted()

fun List<CharArray>.printMap() {
    forEach {
        println(it.joinToString(""))
    }
}

fun generateMap(layout: List<CharArray>, players: List<Player>): List<CharArray> {
    val result = layout.map { it.clone() }
    players.forEach { p -> result[p.coordinate.y][p.coordinate.x] = p.type }
    return result
}

class Combat(puzzle: List<String>, elfPower: Int = 3) {

    private val elves = puzzle.findAll('E').map { (x, y) -> Player('E', x, y, attackPoints = elfPower) }
    private val goblins = puzzle.findAll('G').map { (x, y) -> Player('G', x, y) }
    val players = (elves + goblins).toList()
    val layout = puzzle.map { s -> s.replace('E', '.').replace('G', '.').toCharArray() }

    val alivePlayers get() = players.filter { it.isAlive }

    fun playRound(): Boolean {
        players.sorted().forEach {
            if (it.isAlive)
                if (!it.takeTurn(layout, players))
                    return false
        }
        return true
    }

    fun part1(stopIfElfDied: Boolean = false): Int {
        var fullRounds = 0
        val initialElves = elveCount.size
        while (true) {
            if (!playRound()) {

                if (alivePlayers.count { it.type == 'E' } == 0) {
                    break
                }
                if (alivePlayers.count { it.type == 'G' } == 0) {
                    break
                }
            }
            if ((elveCount.size < initialElves) && stopIfElfDied)
                return -1
            fullRounds++
        }

        val remainingHitpoints = alivePlayers.sumBy { it.hitPoints }
        return fullRounds * remainingHitpoints
    }

    val elveCount get() = players.filter { it.isAlive && it.type == 'E' }

}

fun part1(puzzle: List<String>): Any {
    return Combat(puzzle).part1()
}

fun part2(puzzle: List<String>): Any {
    (4..Int.MAX_VALUE).first {
        val combat = Combat(puzzle, it)
        val result = combat.part1(true)
        if (result>0) {
            println(it)
            println(result)
        }
        result > 0
    }
    return ""
}

fun main(args: Array<String>) {
    val puzzle = readPuzzle(15)

    println(part1(puzzle))
    println(part2(puzzle))
}