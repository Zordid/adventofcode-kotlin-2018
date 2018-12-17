package day16

import shared.extractAllInts
import shared.readPuzzle

typealias Instruction = (a: Int, b: Int, c: Int, r: IntArray) -> Unit

val instructionSet = mapOf<String, Instruction>(
    "addr" to { a, b, c, r -> r[c] = r[a] + r[b] },
    "addi" to { a, b, c, r -> r[c] = r[a] + b },
    "mulr" to { a, b, c, r -> r[c] = r[a] * r[b] },
    "muli" to { a, b, c, r -> r[c] = r[a] * b },
    "banr" to { a, b, c, r -> r[c] = r[a] and r[b] },
    "bani" to { a, b, c, r -> r[c] = r[a] and b },
    "borr" to { a, b, c, r -> r[c] = r[a] or r[b] },
    "bori" to { a, b, c, r -> r[c] = r[a] or b },
    "setr" to { a, _, c, r -> r[c] = r[a] },
    "seti" to { a, _, c, r -> r[c] = a },
    "gtir" to { a, b, c, r -> r[c] = if (a > r[b]) 1 else 0 },
    "gtri" to { a, b, c, r -> r[c] = if (r[a] > b) 1 else 0 },
    "gtrr" to { a, b, c, r -> r[c] = if (r[a] > r[b]) 1 else 0 },
    "eqir" to { a, b, c, r -> r[c] = if (a == r[b]) 1 else 0 },
    "eqri" to { a, b, c, r -> r[c] = if (r[a] == b) 1 else 0 },
    "eqrr" to { a, b, c, r -> r[c] = if (r[a] == r[b]) 1 else 0 }
)

fun part1(puzzle: List<String>): Int {
    val samples = readSamples(puzzle)
    return samples.count { (_, couldBe) -> couldBe.size >= 3 }
}

fun part2(puzzle: List<String>): Int {
    val samples = readSamples(puzzle)
    val allowedMappings = createAllowedMappings(samples, instructionSet.keys)
    val translationTable = determinePossibleMappingTables(allowedMappings).single()

    val instructions = readProgram(puzzle)
    val regs = IntArray(4)
    instructions.forEach { (opcode, a, b, c) ->
        instructionSet[translationTable[opcode]]!!(a, b, c, regs)
    }
    return regs[0]
}

fun readSamples(puzzle: List<String>) =
    puzzle.windowed(4, step = 4, partialWindows = true)
        .filter { it.size >= 3 && it[0].startsWith("Before:") }
        .map { (l1, l2, l3) ->
            val before = l1.extractAllInts().toList()
            val instruction = l2.extractAllInts().toList()
            val after = l3.extractAllInts().toList()

            val (opcode, a, b, c) = instruction

            opcode to (instructionSet.filterValues { mnemonic ->
                val reg = before.toIntArray()
                try {
                    mnemonic(a, b, c, reg)
                    reg.toList() == after
                } catch (e: Exception) {
                    false
                }
            }.keys)
        }

private fun readProgram(puzzle: List<String>) =
    puzzle.takeLastWhile { it.isNotEmpty() }.map { it.extractAllInts().toList() }

private fun <T> determinePossibleMappingTables(allowed: Map<Int, Set<T>>): List<Map<Int, T>> {
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

private fun <T> Map<Int, Set<T>>.reduceBy(assignment: Pair<Int, T>) =
    (this - assignment.first).mapValues { (_, v) -> v - assignment.second }

private fun <T> createAllowedMappings(samples: List<Pair<Int, Set<T>>>, all: Set<T>): Map<Int, Set<T>> {
    val initialAllowed = (0 until all.size).map { it to all }.toMap().toMutableMap()
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