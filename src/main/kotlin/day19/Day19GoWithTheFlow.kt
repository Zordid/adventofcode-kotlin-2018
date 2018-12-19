package day19

import shared.extractAllInts
import shared.extractAllPositiveInts
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

fun part1(puzzle: List<String>, conditionalDebug: (IntArray) -> Boolean = { false }): Any {
    val program =
        puzzle.filter { !it.startsWith('#') }.map { it.substring(0, 4) to it.extractAllPositiveInts().toList() }
    val ipBinding = puzzle.first().extractAllInts().first()
    val registers = IntArray(6)
    var ip = 0

    registers[0] = 0
    while (ip in program.indices) {

        registers[ipBinding] = ip
        val (mnemonic, operands) = program[ip]
        val (a, b, c) = operands

        val debug = conditionalDebug(registers)
        if (debug)
            print("ip=$ip [${registers.joinToString()}] $mnemonic ${operands.joinToString(" ")} ")
        instructionSet[mnemonic]!!(a, b, c, registers)
        if (debug)
            println("[${registers.joinToString()}]")
        ip = registers[ipBinding]

        ip++
    }

    return registers[0]
}

fun part2(startR0: Int = 1): Any {
    var r0 = startR0
    var r1 = 0
    var r2 = 0
    var r3 = 0
    val instructionPointer = 0
    val r5 = 0

    // CO ROUTINE JUMPED TO BY 0
    fun setup() {
// 17  addi 1 2 1
// 18  mulr 1 1 1
// 19  mulr 4 1 1
// 20  muli 1 11 1
        r1 = 2 * 2 * 11 * 19
// 21  addi 3 3 3
// 22  mulr 3 4 3
// 23  addi 3 9 3
        r3 += 3 * 22 + 9
// 24  addr 1 3 1
        r1 += r3
// 25  addr 4 0 4
        if (r0 != 0) {
// 27  setr 4 9 3
// 28  mulr 3 4 3
// 29  addr 4 3 3
            r3 = 27 * 28 + 29
// 30  mulr 4 3 3
// 31  muli 3 14 3
// 32  mulr 3 4 3
            r3 *= 30 * 14 * 32
// 33  addr 1 3 1
            r1 += r3
// 34  seti 0 6 0
            r0 = 0
// 35  seti 0 7 4
            // JUMP TO 1 - EXIT SETUP ROUTINE
        }
    }

//  0  addi 4 16 4
    // JUMP TO 17
    setup()
    println("$r0 $r1 $r2 $r3 $instructionPointer $r5")

    r2 = 1
    do {
        // r5 runs from 1 to r1
        // adds r2 to r0 only if r1 is divisible by r2!
        // => result is the sum of all divisors
        if (r1 % r2 == 0)
            r0 += r2
//        r5 = 1
//        do {
//            if (r2 * r5 == r1) {
//                r0 += r2
//            }
//            r5++
//        } while (r5 <= r1)
        r2++
    } while (r2 <= r1)

    return r0
}

fun main(args: Array<String>) {
    val puzzle = readPuzzle(19)

    println(part1(puzzle))
    println(part2(1))
}