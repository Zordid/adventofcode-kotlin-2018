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

fun part1(puzzle: List<String>): Any {
    val program =
        puzzle.filter { !it.startsWith('#') }.map { it.substring(0, 4) to it.extractAllPositiveInts().toList() }
    var ipBinding = puzzle.first().extractAllInts().first()
    val registers = IntArray(6)
    var ip = 0

    registers[0] = 0
    while (ip in program.indices) {
        var debug = false

        registers[ipBinding] = ip
        val (mnemonic, operands) = program[ip]
        val (a, b, c) = operands

        debug = ip == 1
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

fun part2(puzzle: List<String>): Any {
//    #ip 4
    var r0 = 1
    var r1 = 0
    var r2 = 0
    var r3 = 0
    var instructionPointer = 0
    var r5 = 0


    // CO ROUTINE JUMPED TO BY 0
    fun setup() {
// 17  addi 1 2 1
        r1 = r1 + 2
        instructionPointer = 18
// 18  mulr 1 1 1
        r1 = r1 * r1
        instructionPointer++
// 19  mulr 4 1 1
        r1 = instructionPointer * r1
        instructionPointer++
// 20  muli 1 11 1
        r1 = r1 * 11
        instructionPointer++
// 21  addi 3 3 3
        r3 = r3 + 3
        instructionPointer++
// 22  mulr 3 4 3
        r3 = r3 * instructionPointer
        instructionPointer++
// 23  addi 3 9 3
        r3 = r3 + 9
        instructionPointer++
// 24  addr 1 3 1
        r1 = r1 + r3
        instructionPointer++
// 25  addr 4 0 4
        instructionPointer += r0
        instructionPointer++
        if (r0 == 0) {
// 26  seti 0 1 4
            instructionPointer = 0
            instructionPointer++
            // JUMP TO 1
        } else {
// 27  setr 4 9 3
            r3 = instructionPointer
            instructionPointer++
// 28  mulr 3 4 3
            r3 = r3 * instructionPointer
            instructionPointer++
// 29  addr 4 3 3
            r3 = instructionPointer + r3
            instructionPointer++
// 30  mulr 4 3 3
            r3 = instructionPointer * r3
            instructionPointer++
// 31  muli 3 14 3
            r3 = r3 * 14
            instructionPointer++
// 32  mulr 3 4 3
            r3 = r3 * instructionPointer
            instructionPointer++
// 33  addr 1 3 1
            r1 = r1 + r3
            instructionPointer++
// 34  seti 0 6 0
            r0 = 0
// 35  seti 0 7 4
            instructionPointer = 0
            instructionPointer++
            // JUMP TO 1
        }
    }


//  0  addi 4 16 4
    // JUMP TO 17
    setup()
    println("$r0 $r1 $r2 $r3 $instructionPointer $r5")

    // r1

    r2 = 1
    do {
        r5 = 1
        // r5 = 1..r1
        // addiert r2 zu r0 wenn ein r5
        if (r1 % r2 == 0)
            r0 += r2
//        do {
//            if (r2 * r5 == r1) {
//                r0 += r2
//                continue
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
    println(part2(puzzle))
}