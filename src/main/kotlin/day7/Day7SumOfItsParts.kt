package day7

import shared.readPuzzle

fun part1(input: List<String>): Any {
    val required = prepareData(input)

    val order = mutableListOf<Char>()
    while (required.filter { (step, requires) ->
            !order.contains(step) && order.containsAll(requires)
        }.keys.minOrNull()?.also {
            order.add(it)
        } != null);

    return order.joinToString("")
}

fun Map<Char, MutableSet<Char>>.nextToWorkOn(): List<Char> {
    return this.filter { it.value.isEmpty() }.keys.sorted()
}

fun part2(input: List<String>, maxWorkers: Int = 5, baseWork: Int = 60): Any {
    val instructions = input.map { line -> line.split(' ').let { it[7][0] to it[1][0] } }
    val required = mutableMapOf<Char, MutableSet<Char>>()
    instructions.forEach { (step, requires) ->
        required.getOrPut(requires) { mutableSetOf() }
        required.getOrPut(step) { mutableSetOf() }.add(requires)
    }

    val order = StringBuilder()
    val workers = mutableMapOf<Char, Int>()

    var time = 0
    while (true) {
        while (workers.size == maxWorkers || (required.nextToWorkOn() - workers.keys).isEmpty()) {
            val nextFinished = workers.minBy { it.value }
            workers.remove(nextFinished.key)
            required.forEach { it.value.remove(nextFinished.key) }
            required.remove(nextFinished.key)
            time = nextFinished.value

            order.append(nextFinished.key)
            println(order)
            if (required.isEmpty())
                return time
        }

        val workOn = (required.nextToWorkOn() - workers.keys).first()
        workers[workOn] = time + baseWork + (workOn - 'A' + 1)
    }
}

fun prepareData(input: List<String>): Map<Char, Set<Char>> {
    val instructions = input.map { line -> line.split(' ').let { it[7][0] to it[1][0] } }
    val required = mutableMapOf<Char, MutableSet<Char>>()
    instructions.forEach { (step, requires) ->
        required.getOrPut(requires) { mutableSetOf() }
        required.getOrPut(step) { mutableSetOf() }.add(requires)
    }
    return required
}

fun Char.effort(): Int = this - 'A' + 1

/**
 * This is a reimplementation of the algorithm free of the double inner loop
 */
fun part2(requirements: Map<Char, Set<Char>>, maxWorkers: Int = 5, baseWork: Int = 60): Any {
    val order = mutableListOf<Char>()
    val workers = mutableMapOf<Char, Int>()
    while (true) {
        val time = workers.values.minOrNull() ?: 0

        val finishedSteps = workers.filterValues { it == time }.keys
        order.addAll(finishedSteps.sorted())
        workers -= finishedSteps

        val availableSteps = requirements.filter { (step, requiredSteps) ->
            !order.contains(step) &&
                    !workers.keys.contains(step) &&
                    order.containsAll(requiredSteps)
        }.keys

        if (availableSteps.isEmpty() && workers.isEmpty())
            return time

        val freeWorkers = maxWorkers - workers.size
        availableSteps.sorted().asSequence().take(freeWorkers).forEach { step ->
            workers[step] = time + baseWork + step.effort()
        }
    }
}

fun main() {
    val stepRequires = readPuzzle(7)

    val requirements = prepareData(stepRequires)
    println(part1(stepRequires))
    println(part2(stepRequires))
    println(part2(requirements))
}