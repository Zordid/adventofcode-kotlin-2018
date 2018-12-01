package shared

import java.io.File

fun readPuzzle(day: Int, extra: String = "") =
    File("puzzles/day$day$extra.txt").bufferedReader().readLines()

fun <T> readPuzzle(day: Int, extra: String = "", mapper: (String) -> T) =
    readPuzzle(day, extra).map(mapper)

fun readPuzzleAsInts(day: Int) = readPuzzle(day) { it.toInt() }

