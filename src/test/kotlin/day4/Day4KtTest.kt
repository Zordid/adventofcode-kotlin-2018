package day4

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day4KtTest {

    private val input = """[1518-11-01 00:00] Guard #10 begins shift
[1518-11-01 00:05] falls asleep
[1518-11-01 00:25] wakes up
[1518-11-01 00:30] falls asleep
[1518-11-01 00:55] wakes up
[1518-11-01 23:58] Guard #99 begins shift
[1518-11-02 00:40] falls asleep
[1518-11-02 00:50] wakes up
[1518-11-03 00:05] Guard #10 begins shift
[1518-11-03 00:24] falls asleep
[1518-11-03 00:29] wakes up
[1518-11-04 00:02] Guard #99 begins shift
[1518-11-04 00:36] falls asleep
[1518-11-04 00:46] wakes up
[1518-11-05 00:03] Guard #99 begins shift
[1518-11-05 00:45] falls asleep
[1518-11-05 00:55] wakes up""".split("\n")

    @Test
    fun demo() {
        val guards = processLogs(input)

        assertEquals(2, guards.size)

        val guard10 = guards.single { it.id == 10 }
        val guard99 = guards.single { it.id == 99 }

        assertEquals(50, guard10.totalMinutesAsleep)
        assertEquals(30, guard99.totalMinutesAsleep)

        assertEquals(24, guard10.maxMinuteAsleep)
        assertEquals(2, guard10.maxMinuteAsleepCount)
        assertEquals(45, guard99.maxMinuteAsleep)
        assertEquals(3, guard99.maxMinuteAsleepCount)
    }


}