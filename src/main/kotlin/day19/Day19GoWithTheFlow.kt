package day19

import shared.ElfDeviceCpu
import shared.readPuzzle

fun part1(puzzle: List<String>): Any {
    val cpu = ElfDeviceCpu(puzzle)
    cpu.run().last()
    return cpu.registers[0]
}

fun part2(startR0: Int = 1): Any {
    var r0 = startR0
    var r1 = 0
    var r3 = 0

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
    //println("$r0 $r1 $r2 $r3 $instructionPointer $r5")

    var r2 = 1
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