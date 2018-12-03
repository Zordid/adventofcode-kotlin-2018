package shared

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class ListExtKtTest {

    @Test
    fun allPairs() {
        assertEquals(0, emptyList<Int>().allPairs().count())
        assertEquals(listOf(1 to 2), listOf(1, 2).allPairs().toList())
        assertEquals(listOf(1 to 2, 1 to 3, 2 to 3), listOf(1, 2, 3).allPairs().toList())
    }
}