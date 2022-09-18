package shared

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class MapsAndNavigationExtKtTest {

    @Test
    fun allDistancesSequenceShouldWork() {
        val firstThreeLevels = floodFill(0 toY 0) { true }.take(3).toList()
        assertEquals(
            listOf(
                0 to setOf(0 toY 0),
                1 to setOf(0 toY -1, 1 toY 0, 0 toY 1, -1 toY 0),
                2 to setOf(0 toY -2, 1 toY -1, 2 toY 0, 1 toY 1, 0 toY 2, -1 toY 1, -2 toY 0, -1 toY -1)
            ), firstThreeLevels
        )
    }

    @Test
    fun allDistancesSequenceShouldWorkWithObstacle() {
        val firstThreeLevels = floodFill(0 toY 0) { (x, y) -> !(x == 0 && y == -1) }.take(3).toList()
        assertEquals(
            listOf(
                0 to setOf(0 toY 0),
                1 to setOf(1 toY 0, 0 toY 1, -1 toY 0),
                2 to setOf(1 toY -1, 2 toY 0, 1 toY 1, 0 toY 2, -1 toY 1, -2 toY 0, -1 toY -1)
            ), firstThreeLevels
        )
    }

    @Test
    fun firstReachedWithoutObstacle() {
        assertEquals(listOf(5 toY 0), listOf(5 toY 3, 1 toY 10, 5 toY 0).filterFirstReached(0 toY 0) { true })
    }

    @Test
    fun testCompleteRectangle() {
        val inRect = floodFill(-2 toY -2) { (x, y) -> (x in -2..2) && (y in -2..2) }.map { it.second }.flatten()
        assertEquals(25, inRect.count())
    }

    @Test
    fun testFillWithBorder() {
        val inRect =
            floodFill(-2 toY -2) { (x, y) -> (x in -2..2) && (y in -2..2) && (y != 0) }.map { it.second }.flatten()
        assertEquals(10, inRect.count())
    }

}