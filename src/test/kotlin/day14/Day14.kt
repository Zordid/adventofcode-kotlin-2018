package day14

import shared.readPuzzle

fun main(args: Array<String>) {
    val puzzle = readPuzzle(14).single().toInt()

    println(part1(puzzle))
    println(part2(puzzle))
}

fun part1(puzzle: Int): String {
    val recipes = ByteArray(puzzle + 20)
    var end = 2
    recipes[0] = 3
    recipes[1] = 7

    var elf1 = 0
    var elf2 = 1
    while (end < puzzle + 10) {
        val sum = recipes[elf1] + recipes[elf2]
        if (sum >= 10) {
            recipes[end++] = (sum / 10).toByte()
        }
        recipes[end++] = (sum % 10).toByte()
        elf1 = (elf1 + recipes[elf1] + 1) % end
        elf2 = (elf2 + recipes[elf2] + 1) % end
        //println(recipes.joinToString(" "))
    }

    return recipes.slice(puzzle until puzzle + 10).joinToString("")
}

fun part2(puzzle: Int): Int {
    val key = puzzle.toString().toCharArray().map { (it - '0').toByte() }

    println(key)

    val recipes = ArrayList<Byte>(100000)
    recipes.add(3)
    recipes.add(7)

    var elf1 = 0
    var elf2 = 1
    var keyDone = 0
    while (true) {
        val sum = recipes[elf1] + recipes[elf2]
        if (sum >= 10) {
            val newR = (sum / 10).toByte()
            recipes.add(newR)
            if (newR == key[keyDone]) keyDone++ else keyDone = 0
            if (keyDone == key.size) break
        }
        val new2 = (sum % 10).toByte()
        recipes.add(new2)
        if (new2 == key[keyDone]) keyDone++ else keyDone = 0
        if (keyDone == key.size) break
        elf1 = (elf1 + recipes[elf1] + 1) % recipes.size
        elf2 = (elf2 + recipes[elf2] + 1) % recipes.size
        //println(recipes.joinToString(" "))
    }
    println(recipes.slice(recipes.size - 30 until recipes.size).joinToString(" "))
    return (recipes.size - key.size)
}
