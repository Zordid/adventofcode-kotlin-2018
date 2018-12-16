package day16

import shared.extractAllInts
import shared.readPuzzle

fun part1(puzzle: List<String>): Int {
    val samples = readSamples(puzzle)
    return samples.count { (_, couldBe) -> couldBe.size >= 3 }
}

fun part2(puzzle: List<String>): Int {
    val samples = readSamples(puzzle)
    val allowedMappings = createAllowedMappings(samples)
    val translationTable = determinePossibleMappingTables(allowedMappings).single()

    val instructions = readProgram(puzzle)
    var regs = listOf(0, 0, 0, 0)
    instructions.forEach { regs = execute(it, regs, translationTable[it[0]]!!)!! }
    return regs[0]
}

fun execute(instruction: List<Int>, reg: List<Int>, overrideOpCode: Int = -1): List<Int>? {
    val (opCode, a, b, c) = instruction

    try {
        val result = when (if (overrideOpCode >= 0) overrideOpCode else opCode) {
            // addr
            0 -> reg[a] + reg[b]
            // addi
            1 -> reg[a] + b
            // mulr
            2 -> reg[a] * reg[b]
            // muli
            3 -> reg[a] * b
            // banr
            4 -> reg[a] and reg[b]
            // bani
            5 -> reg[a] and b
            // borr
            6 -> reg[a] or reg[b]
            // bori
            7 -> reg[a] or b
            // setr
            8 -> reg[a]
            // seti
            9 -> a
            // gtir
            10 -> if (a > reg[b]) 1 else 0
            // gtri
            11 -> if (reg[a] > b) 1 else 0
            // gtrr
            12 -> if (reg[a] > reg[b]) 1 else 0
            // eqir
            13 -> if (a == reg[b]) 1 else 0
            // eqri
            14 -> if (reg[a] == b) 1 else 0
            // eqrr
            15 -> if (reg[a] == reg[b]) 1 else 0
            else -> throw IllegalArgumentException("$opCode")
        }
        return reg.toMutableList().also { it[c] = result }
    } catch (e: IndexOutOfBoundsException) {
        return null
    }
}

fun readSamples(puzzle: List<String>) =
    puzzle.windowed(4, step = 4, partialWindows = true)
        .filter { it.size >= 3 && it[0].startsWith("Before:") }
        .map {
            val before = it[0].extractAllInts().toList()
            val instruction = it[1].extractAllInts().toList()
            val after = it[2].extractAllInts().toList()

            instruction[0] to ((0..15).filter { testOpCode ->
                execute(instruction, before, testOpCode) == after
            }.toSet())
        }

private fun readProgram(puzzle: List<String>) =
    puzzle.takeLastWhile { it.isNotEmpty() }.map { it.extractAllInts().toList() }

private fun determinePossibleMappingTables(allowed: Map<Int, Set<Int>>): List<Map<Int, Int>> {
    val nextCandidate = allowed.entries.sortedBy { (_, v) -> v.size }.first()
    if (nextCandidate.value.isEmpty())
        return emptyList()

    val assign = nextCandidate.key
    return nextCandidate.value.flatMap { to ->
        val assignment = assign to to
        val remaining = allowed.reduceBy(assignment)
        if (remaining.isNotEmpty())
            determinePossibleMappingTables(remaining).filter { it.isNotEmpty() }.map { it + mapOf(assignment) }
        else
            listOf(mapOf(assignment))
    }
}

private fun Map<Int, Set<Int>>.reduceBy(assignment: Pair<Int, Int>) =
    (this - assignment.first).mapValues { (_, v) -> v - assignment.second }

private fun createAllowedMappings(samples: List<Pair<Int, Set<Int>>>): Map<Int, Set<Int>> {
    val initialAllowed = (0..15).map { it to (0..15).toSet() }.toMap().toMutableMap()
    return samples.fold(initialAllowed) { acc, pair ->
        val (opCode, couldBe) = pair
        acc[opCode] = acc[opCode]!! intersect couldBe
        acc
    }
}

fun main(args: Array<String>) {
    val puzzle = readPuzzle(16)

    println(part1(puzzle))
    println(part2(puzzle))
}