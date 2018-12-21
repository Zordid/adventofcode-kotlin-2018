package day21

import day19.part1
import shared.readPuzzle

fun part2(puzzle: List<String>): Any {
    return ""
}

fun program2(startR0: Int): Any {
//    #ip 5
    var r0 = startR0
    var r1 = 0
    var r2 = 0
    var r3 = 0
    var r4 = 0
    var instructionPointer = 0

//   0  seti 123 0 3
//   1  bani 3 456 3
//   2  eqri 3 72 3
//   3  addr 3 5 5
//   5  seti 0 9 3
    var c = 5L

    var seen = mutableMapOf<String, Pair<Int, Long>>()
    var previousr3 : Int? = null

    i6@
    while (true) {
//   6  bori 3 65536 1
        r1 = r3 or 65536 // 1 0000 0000 0000 0000
        c++

//   7  seti 14906355 8 3
        r3 = 14906355  // 1110 0011 0111 0011 1111 0011
        c++

        i8@
        while (true) {
//   8  bani 1 255 4
            r4 = r1 and 255
            c++

//   9  addr 3 4 3
            r3 = r3 + r4
            c++

//  10  bani 3 16777215 3
            r3 = r3 and 16777215 // 0000 0000 1111 1111 1111 1111 1111 1111
            c++

//  11  muli 3 65899 3
            r3 = r3 * 65899 // 1 0000 0001 0110 1011
            c++

//  12  bani 3 16777215 3
            r3 = r3 and 16777215 // 0000 0000 1111 1111 1111 1111 1111 1111
            c++


            // 13  gtir 256 1 4
            //if ( 256 > r1) r4 = 1 else r4 = 0
            c++
            if (256 <= r1) {
                //  14  addr 4 5 5
                //  15  addi 5 1 5
                // go on with 17
                //  17  seti 0 4 4
                r4 = 0
                c += 3

                i18@
                while (true) {
                    //  18  addi 4 1 2
                    //  19  muli 2 256 2
                    c += 2
                    r2 = (r4 + 1) * 256

                    //  20  gtrr 2 1 2
                    c++
                    if (r2 > r1) {
                        //  21  addr 2 5 5
                        //  23  seti 25 1 5
                        //  26  setr 4 9 1
                        //  27  seti 7 0 5
                        c += 4
                        r1 = r4
                        continue@i8

                    }
                    //  21  addr 2 5 5
                    //  22  addi 5 1 5
                    //  24  addi 4 1 4
                    //  25  seti 17 2 5
                    c += 4
                    r4++
                }
            } else {
                //  14  addr 4 5 5
                //  16  seti 27 8 5
                //  28  eqrr 3 0 4
                //  29  addr 4 5 5
                c += 4

//                if (c>=2388926282)
//                    println(r3)

                if (previousr3 != null) {

                    val key = "$r3"

                    if (seen.contains(key)) {
                        println(c)
                        println(previousr3)
                        return previousr3
                    }


                    seen[key] = previousr3 to c
                }
                previousr3 = r3

                if (r3 == r0)
                    return 0
                //  30  seti 5 3 5
                c++
                continue@i6
            }
        }

    }
}

fun program(startR0: Int): Int {
//    #ip 5
    var r0 = startR0
    var r1 = 0
    var r2 = 0
    var r3 = 0
    var r4 = 0
    var instructionPointer = 0

    var seen = mutableMapOf<String, Int>()
    var previousr3 : Int? = null
    var c = 5
    i6@
    while (true) {
        r1 = r3 or 65536 // 1 0000 0000 0000 0000
        r3 = 14906355  // 0000 0000 1110 0011 0111 0011 1111 0011
        c += 2

        i8@
        while (true) {
            r4 = r1 and 255
            r3 = r3 + r4
            r3 = r3 and 16777215 // 0000 0000 1111 1111 1111 1111 1111 1111
            r3 = r3 * 65899 // 1 0000 0001 0110 1011
            r3 = r3 and 16777215 // 0000 0000 1111 1111 1111 1111 1111 1111
            c += 5

            c++
            if (256 <= r1) {
                r4 = 0
                c += 3

                while (true) {
                    r2 = (r4 + 1) * 256
                    c += 2

                    c++
                    if (r2 > r1) {
                        c += 4
                        r1 = r4
                        break
                    }
                    c += 4
                    r4++
                }
            } else {
                c += 3
                //println("r3 = $r3")
                if (previousr3 != null) {

                    val key = "$r0 $r1 $r2 $r3 $r4"

                    if (seen.contains(key)) {
                        println(seen[key])
                        println(c)
                        return seen[key]!!
                    }


                    seen[key] = previousr3
                }
                previousr3 = r3
                c++
                if (r3 == r0)
                    return c
                c++
                continue@i6
            }
        }

    }
}


fun main(args: Array<String>) {
    val puzzle = readPuzzle(21)

    for (i in 0..0)
        println("$i: ${program2(i)}")


    //println(part1(puzzle) { r -> r[5] == 28 })
    println(part2(puzzle))
}