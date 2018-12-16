package day16

import com.marcinmoskala.math.combinations
import com.marcinmoskala.math.permutations
import shared.extractAllInts
import shared.readPuzzle

fun execute(instruction: List<Int>, registers: List<Int>, testOpCode: Int = -1): List<Int>? {
    val opcode = if (testOpCode >= 0) testOpCode else instruction[0]
    val a = instruction[1]
    val aReg = registers.getOrNull(a) ?: return null
    val b = instruction[2]
    val bReg = registers.getOrNull(b) ?: return null
    val destinationReg = instruction[3]

    val result = when (opcode) {
        // addr
        0 -> aReg + bReg
        // addi
        1 -> aReg + b
        // mulr
        2 -> aReg * bReg
        // muli
        3 -> aReg * b
        // banr
        4 -> aReg and bReg
        // bani
        5 -> aReg and b
        // borr
        6 -> aReg or bReg
        // bori
        7 -> aReg or b
        // setr
        8 -> aReg
        // seti
        9 -> a
        // gtir
        10 -> if (a > bReg) 1 else 0
        // gtri
        11 -> if (aReg > b) 1 else 0
        // gtrr
        12 -> if (aReg > bReg) 1 else 0
        // eqir
        13 -> if (a == bReg) 1 else 0
        // eqri
        14 -> if (aReg == b) 1 else 0
        // eqrr
        15 -> if (aReg == bReg) 1 else 0
        else -> throw IllegalArgumentException("$opcode")
    }
    val resultingRegs = registers.toMutableList()
    resultingRegs[destinationReg] = result
    return resultingRegs
}

fun readSamples(puzzle: List<String>): List<Pair<List<Int>, List<Int>>> {
    return puzzle.windowed(4, step = 4).map {
        val inputRegisters = it[0].extractAllInts().toList()
        val instruction = it[1].extractAllInts().toList()
        val resultingRegisters = it[2].extractAllInts().toList()

        instruction to ((0..15).filter {
            execute(instruction, inputRegisters, it) == resultingRegisters
        })
    }
}

fun determinePossibleMapping(allowed: Map<Int, Set<Int>>): List<Map<Int, Int>> {
    val nextCandidate = allowed.entries.sortedBy { (k, v) -> v.size }.first()
    if (nextCandidate.value.isEmpty())
        return emptyList()

    val assign = nextCandidate.key
    return nextCandidate.value.flatMap { to ->
        val assignment = assign to to
        val remaining = allowed.reduceBy(assignment)
        if (remaining.isNotEmpty())
            determinePossibleMapping(remaining).filter { it.isNotEmpty() }.map { it + mapOf(assignment) }
        else
            listOf(mapOf(assignment))
    }
}

private fun Map<Int, Set<Int>>.reduceBy(assignment: Pair<Int, Int>): Map<Int, Set<Int>> {
    return (this - assignment.first).mapValues { (_, v) -> v - assignment.second }
}

fun part1(puzzle: List<String>): Any {
    val samples = readSamples(puzzle)
    return samples.count { (test, couldBe) -> couldBe.size >= 3 }
}

fun part2(puzzleA: List<String>, puzzleB: List<String>): Any {
    val samples = readSamples(puzzleA)
    val result = mutableMapOf<Int, Set<Int>>()
    samples.forEach { (test, couldBe) ->
        if (!result.containsKey(test[0]))
            result[test[0]] = couldBe.toSet()
        else
            result[test[0]] = result[test[0]]!! intersect couldBe
    }

    result.toList().sortedBy { (k, v) -> v.size }.forEach { println(it) }

    println(result.size)
    println(result.count { (k, v) -> v.size == 1 })

    val table = determinePossibleMapping(result).single()

    val instructions = puzzleB.map { it.extractAllInts().toList() }

    val translated = instructions.map {
        it.toMutableList().also { it[0] = table[it[0]]!! }
    }

    var regs = listOf(0, 0, 0, 0)
    translated.forEach { regs = execute(it, regs)!! }
    return regs
}

fun main(args: Array<String>) {
    val puzzle = readPuzzle(16)

    println(part1(puzzle))
    println(part2(puzzle, readPuzzle(16, "b")))
}