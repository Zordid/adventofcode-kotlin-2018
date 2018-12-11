package shared

import java.io.File
import kotlin.system.measureTimeMillis

fun readPuzzle(day: Int, extra: String = "") =
    File("puzzles/day$day$extra.txt").bufferedReader().readLines()
        .also { println("Read ${it.size} input lines for day $day...") }

fun <T> readPuzzle(day: Int, extra: String = "", mapper: (String) -> T) =
    readPuzzle(day, extra).map(mapper)

fun readPuzzleAsInts(day: Int, extra: String = "") =
    readPuzzle(day, extra) { it.toInt() }

inline fun measureRuntime(block: () -> Unit) = measureTimeMillis(block).also { println("Took: $it ms") }

