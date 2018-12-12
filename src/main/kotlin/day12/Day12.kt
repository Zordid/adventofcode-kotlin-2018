package day12

import shared.readPuzzle

val seen = mutableMapOf<String, Pair<Long, Long>>()
var finish = false

private tailrec fun Pair<String, Long>.transform(
    transforms: Map<String, String>,
    generations: Long
): Pair<String, Long> {
    val s = "....${this.first}...."
    var base = this.second - 2

    var r = s.windowed(5).map { transforms.getOrDefault(it, ".") }.joinToString("")
    val start = r.indexOf('#')
    r = r.substring(start).trim('.')
    base += start

    println("$r $base")
    var result = r to base
    var genLeft = generations

    if (!finish) {
        if (seen.contains(r)) {
            val firstSeen = seen[r]!!
            val drift = base - firstSeen.first
            val loopLength = firstSeen.second - generations
            println("Loop detected between ${firstSeen.second} and $generations")
            println("loop length is $loopLength")
            println("drift is $drift")

            genLeft = (generations % loopLength)
            val skip = generations - genLeft
            val skipTimes = skip / loopLength
            println("Skipping $skipTimes iterations and now at gen $genLeft")

            result = r to (base + drift * skipTimes)

            finish = true
        } else {
            seen[r] = base to generations
        }
    }

    return if (genLeft > 0) result.transform(transforms, genLeft - 1) else result
}

fun part1(puzzle: List<String>): Any {
    val initial = puzzle[0].substring(puzzle[0].indexOf(": ") + 2)
    val transforms = puzzle.filter { it.contains("=>") }.map { it.split(" ").let { it[0] to it[2] } }.toMap()

    seen.clear()
    finish = false
    val result = (initial to 0L).transform(transforms, 19)

    return result.first.mapIndexed { idx, c -> idx + result.second to c }
        .fold(0L) { acc, (idx, c) -> if (c == '#') acc + idx else acc }
}

fun part2(puzzle: List<String>): Any {
    val initial = puzzle[0].substring(puzzle[0].indexOf(": ") + 2)
    val transforms = puzzle.filter { it.contains("=>") }.map { it.split(" ").let { it[0] to it[2] } }.toMap()

    seen.clear()
    finish = false
    val result = (initial to 0L).transform(transforms, 50000000000L - 1)

    return result.first.mapIndexed { idx, c -> idx + result.second to c }
        .fold(0L) { acc, (idx, c) -> if (c == '#') acc + idx else acc }

}

fun main(args: Array<String>) {
    val puzzle = readPuzzle(12)

    println(part1(puzzle))
    println(part2(puzzle))
}