package day4

import shared.readPuzzle

data class Guard(val id: Int) {
    var totalMinutesAsleep: Int = 0

    val minuteStatistics = IntArray(60)

    var fellAsleepMinute = -1

    fun process(logEntry: String) {
        val minute = logEntry.substring(15, 17).toInt()
        if (logEntry.contains("falls asleep"))
            fellAsleepMinute = minute
        else {
            totalMinutesAsleep += minute - fellAsleepMinute
            for (min in fellAsleepMinute until minute)
                minuteStatistics[min]++
        }
    }

    fun maxMinuteAsleep(): Pair<Int, Int> {
        val max = minuteStatistics.max() ?: throw UnsupportedOperationException()
        return minuteStatistics.indexOf(max) to max
    }
}

fun processLogs(log: List<String>): List<Guard> {
    val guards = mutableMapOf<Int, Guard>()
    val guardRegex = Regex("\\[(.*)\\] Guard #(\\d+) begins shift")

    var currentGuard: Guard? = null
    for (logEntry in log) {
        val match = guardRegex.matchEntire(logEntry)
        if (match != null) {
            val guardId = match.groupValues[2].toInt()
            currentGuard = guards.getOrPut(guardId) { Guard(guardId) }
        } else if (currentGuard != null) {
            currentGuard.process(logEntry)
        } else
            throw UnsupportedOperationException("No guard on duty")
    }

    return guards.values.toList()
}

fun part1(guards: List<Guard>): Any {

    val sleepyGuard = guards.maxBy { it.totalMinutesAsleep } ?: throw UnsupportedOperationException()

    val max = sleepyGuard.minuteStatistics.max() ?: throw UnsupportedOperationException()
    val maxMinute = sleepyGuard.minuteStatistics.indexOf(max)
    return "$sleepyGuard.id * $maxMinute = ${sleepyGuard.id * maxMinute}"
}

fun part2(guards: List<Guard>): Any {
    val masterOfSleep = guards.maxBy { it.maxMinuteAsleep().second } ?: throw UnsupportedOperationException()
    return "$masterOfSleep.id * $masterOfSleep = ${masterOfSleep.id * masterOfSleep.maxMinuteAsleep().first}"
}

fun main(args: Array<String>) {
    val puzzle = readPuzzle(4)

    val log = puzzle.sortedBy { it.substring(0, 18) }
    log.forEach { println(it) }
    val guards = processLogs(log)

    println(part1(guards))
    println(part2(guards))
}