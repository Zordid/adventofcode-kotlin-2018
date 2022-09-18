package day14

import shared.readPuzzle

fun recipeSequencer() = sequence {
    val scores = mutableListOf(3, 7)
    var (elf1, elf2) = 0 to 1
    yieldAll(scores)
    while (true) {
        val newScore = scores[elf1] + scores[elf2]
        if (newScore >= 10)
            yield((newScore / 10).also { scores.add(it) })
        yield((newScore % 10).also { scores.add(it) })
        elf1 = (elf1 + scores[elf1] + 1) % scores.size
        elf2 = (elf2 + scores[elf2] + 1) % scores.size
    }
}

fun part1(puzzle: String) =
    recipeSequencer().drop(puzzle.toInt()).take(10).joinToString("")

fun part2(puzzle: String): Int {
    val key = puzzle.map { (it - '0') }
    return recipeSequencer()
        .windowed(puzzle.length, step = 1)
        .takeWhile { it != key }.count()
}

fun main() {
    val puzzle = readPuzzle(14).single()

    println(part1(puzzle))
    println(part2(puzzle))
}
