package shared

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class MapsAndNavigationExtKtTest {

    @Test
    fun allDistancesSequenceShouldWork() {
        val firstThreeLevels = floodFill(0 to 0) { _, _ -> true }.take(3).toList()
        assertEquals(listOf(
            0 to setOf(0 to 0),
            1 to setOf(0 to -1, 1 to 0, 0 to 1, -1 to 0),
            2 to setOf(0 to -2, 1 to -1, 2 to 0, 1 to 1, 0 to 2, -1 to 1, -2 to 0, -1 to -1)
        ), firstThreeLevels)
    }

    @Test
    fun allDistancesSequenceShouldWorkWithObstacle() {
        val firstThreeLevels = floodFill(0 to 0) { x, y -> !(x == 0 && y == -1) }.take(3).toList()
        assertEquals(listOf(
            0 to setOf(0 to 0),
            1 to setOf(1 to 0, 0 to 1, -1 to 0),
            2 to setOf(1 to -1, 2 to 0, 1 to 1, 0 to 2, -1 to 1, -2 to 0, -1 to -1)
        ), firstThreeLevels)
    }

    @Test
    fun firstReachedWithoutObstacle() {
        assertEquals(listOf(5 to 0), listOf(5 to 3, 1 to 10, 5 to 0).filterFirstReached(0 to 0) {_,_->true})
    }

    @Test
    fun testCompleteRectangle() {
        val inRect = floodFill(-2 to -2) { x, y -> (x in -2..2) && (y in -2..2) }.map { it.second }.flatten()
        assertEquals(25, inRect.count())
    }

    @Test
    fun testFillWithBorder() {
        val inRect = floodFill(-2 to -2) { x, y -> (x in -2..2) && (y in -2..2) && (y != 0) }.map { it.second }.flatten()
        assertEquals(10, inRect.count())
    }

}