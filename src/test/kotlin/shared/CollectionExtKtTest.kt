package shared

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class CollectionExtKtTest {

    @Test
    fun testInfiniteSequence() {
        val subject = listOf(1, 2, 3)

        assertEquals(listOf(1, 2, 3, 1, 2, 3, 1, 2, 3), subject.asInfiniteSequence().take(9).toList())
    }
}