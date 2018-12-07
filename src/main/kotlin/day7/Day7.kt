package day7

import shared.readPuzzle
import java.lang.StringBuilder

fun part1(puzzle: List<String>): Any {
    val required = mutableMapOf<Char, MutableSet<Char>>()
    puzzle.forEach { def ->
        def.split(" ").also {
            val step = it[7][0]
            val requires = it[1][0]
            required.getOrPut(requires) { mutableSetOf() }
            required.getOrPut(step) { mutableSetOf() }.add(requires)
        }
    }

    val order = StringBuilder()
    while (required.isNotEmpty()) {
        val ready = required.filter { it.value.isEmpty() }.keys.min()!!
        order.append(ready)
        required.remove(ready)
        required.forEach { it.value.remove(ready) }
    }

    return order.toString()
}

fun Map<Char, MutableSet<Char>>.nextToWorkOn(): List<Char> {
    return this.filter { it.value.isEmpty() }.keys.sorted()
}

fun part2(puzzle: List<String>, maxWorkers: Int = 5, baseWork: Int = 60): Any {
    val required = mutableMapOf<Char, MutableSet<Char>>()
    puzzle.forEach { def ->
        def.split(" ").also {
            val step = it[7][0]
            val requires = it[1][0]
            required.getOrPut(requires) { mutableSetOf() }
            required.getOrPut(step) { mutableSetOf() }.add(requires)
        }
    }

    val order = StringBuilder()
    val workers = mutableMapOf<Char, Int>()

    var time = 0
    while (required.isNotEmpty()) {
        if (workers.size == maxWorkers || (required.nextToWorkOn() - workers.keys).isEmpty()) {
            val nextFinished = workers.minBy { it.value }!!
            time = nextFinished.value
            workers.remove(nextFinished.key)
            order.append(nextFinished.key)
            println(order)
            required.forEach { it.value.remove(nextFinished.key) }
            required.remove(nextFinished.key)
        }

        val workOn = (required.nextToWorkOn() - workers.keys).firstOrNull()
        if (workOn != null)
            workers[workOn] = time + baseWork + (workOn - 'A' + 1)
        else if (workers.isNotEmpty()) {
            val nextFinished = workers.minBy { it.value }!!
            time = nextFinished.value
            workers.remove(nextFinished.key)
            order.append(nextFinished.key)
            println(order)
            required.forEach { it.value.remove(nextFinished.key) }
            required.remove(nextFinished.key)
        }
    }

    return time
}

fun main(args: Array<String>) {
    val puzzle = readPuzzle(7)

    println(part1(puzzle))
    println(part2(puzzle))
}