package shared

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class SequenceExtKtTest {

    @Test
    fun testReduceIntermediateWithElements() {
        val subject = sequenceOf(1, 2, 3, 4, 5)

        assertEquals(listOf(1, 3, 6, 10, 15), subject.reduceIntermediate { acc, v -> acc + v }.toList())
    }

}