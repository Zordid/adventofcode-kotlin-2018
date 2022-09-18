package day4

import shared.extractAllInts
import shared.readPuzzle

data class Guard(val id: Int) {
    private val minuteStatistics = IntArray(60)

    val totalMinutesAsleep get() = minuteStatistics.sum()
    val maxMinuteAsleepCount get() = minuteStatistics.max()
    val maxMinuteAsleep get() = minuteStatistics.indexOf(maxMinuteAsleepCount)

    fun asleep(range: IntRange) {
        for (i in range) minuteStatistics[i]++
    }
}

fun processLogs(log: List<String>): List<Guard> {
    val guards = mutableMapOf<Int, Guard>()

    var currentGuard: Guard? = null
    var fellAsleep: Int? = null
    for (logEntry in log.sorted()) {
        val lastNumber = logEntry.extractAllInts().last()
        when {
            "shift" in logEntry -> currentGuard = guards.getOrPut(lastNumber) { Guard(lastNumber) }
            "asleep" in logEntry -> fellAsleep = lastNumber
            else -> currentGuard!!.asleep(fellAsleep!! until lastNumber)
        }
    }

    return guards.values.toList()
}

fun part1(guards: List<Guard>): Any {
    val sleepyGuard = guards.maxBy { it.totalMinutesAsleep }
    return "$sleepyGuard * ${sleepyGuard.maxMinuteAsleep} = ${sleepyGuard.id * sleepyGuard.maxMinuteAsleep}"
}

fun part2(guards: List<Guard>): Any {
    val masterOfSleep = guards.maxBy { it.maxMinuteAsleepCount }
    return "$masterOfSleep * ${masterOfSleep.maxMinuteAsleep} = ${masterOfSleep.id * masterOfSleep.maxMinuteAsleep}"
}

fun main() {
    val puzzle = readPuzzle(4)

    val guards = processLogs(puzzle)
    println(part1(guards))
    println(part2(guards))
}