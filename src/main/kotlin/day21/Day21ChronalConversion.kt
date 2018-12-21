package day21

import shared.ElfDeviceCpu
import shared.measureRuntime
import shared.readPuzzle

fun part1(cpu: ElfDeviceCpu): Int {
    cpu.reset()
    val sequenceOfR3ValuesAt28 = cpu.run().filter { (_, ip) -> ip == 28 }.map { (regs, _) -> regs[3] }
    
    // the very first value of that sequence is our solution!
    return sequenceOfR3ValuesAt28.first()
}

fun part2(cpu: ElfDeviceCpu): Int {
    cpu.reset()
    val sequenceOfR3ValuesAt28 = cpu.run().filter { (_, ip) -> ip == 28 }.map { (regs, _) -> regs[3] }

    // the last value of the sequence of R3's taken while it is not seen before is our solution!
    val seen = mutableSetOf<Int>()
    return sequenceOfR3ValuesAt28.takeWhile { !seen.contains(it) }.onEach { seen.add(it) }.last()
}

fun part2KotlinCode(): Int {
//    #ip 5
    var r1: Int
    var r2: Int
    var r3 = 0
    var r4: Int

    // nothing important in these first instructions
    //   0  seti 123 0 3
    //   1  bani 3 456 3
    //   2  eqri 3 72 3
    //   3  addr 3 5 5
    //   5  seti 0 9 3
    val seen = mutableSetOf<Int>()
    var previous: Int? = null

    i6@
    while (true) {
        //   6  bori 3 65536 1
        r1 = r3 or 65536
        //   7  seti 14906355 8 3
        r3 = 14906355

        i8@
        while (true) {
            //   8  bani 1 255 4
            r4 = r1 and 255
            //   9  addr 3 4 3
            r3 += r4
            //  10  bani 3 16777215 3
            r3 = r3 and 16777215
            //  11  muli 3 65899 3
            r3 *= 65899
            //  12  bani 3 16777215 3
            r3 = r3 and 16777215

            // 13  gtir 256 1 4
            //if ( 256 > r1) r4 = 1 else r4 = 0
            if (256 > r1) {
                //  14  addr 4 5 5
                //  16  seti 27 8 5
                //  28  eqrr 3 0 4
                //  29  addr 4 5 5
                // Here's where the program would halt in case r0 == r3
                if (previous != null && seen.contains(r3)) {
                    return previous
                }
                seen.add(r3)
                previous = r3

                //  30  seti 5 3 5
                continue@i6
            } else {
                //  14  addr 4 5 5
                //  15  addi 5 1 5
                // go on with 17
                //  17  seti 0 4 4
                r4 = 0

                i18@
                while (true) {
                    //  18  addi 4 1 2
                    //  19  muli 2 256 2
                    r2 = (r4 + 1) * 256

                    //  20  gtrr 2 1 2
                    if (r2 > r1) {
                        //  21  addr 2 5 5
                        //  23  seti 25 1 5
                        //  26  setr 4 9 1
                        //  27  seti 7 0 5
                        r1 = r4
                        continue@i8

                    }
                    //  21  addr 2 5 5
                    //  22  addi 5 1 5
                    //  24  addi 4 1 4
                    //  25  seti 17 2 5
                    r4++
                }
            }
        }

    }
}

fun main(args: Array<String>) {
    val puzzle = readPuzzle(21)

    val cpu = ElfDeviceCpu(puzzle)
    measureRuntime {
        println(part1(cpu))
    }
    measureRuntime {
        println(part2(cpu))
    }
    measureRuntime {
        println(part2KotlinCode())
    }
}
