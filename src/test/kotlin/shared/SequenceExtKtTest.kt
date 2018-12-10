package shared

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class SequenceExtKtTest {

    @Test
    fun testReduceIntermediateWithElements() {
        val subject = sequenceOf(1, 2, 3, 4, 5)

        assertEquals(listOf(1, 3, 6, 10, 15), subject.reduceIntermediate { acc, v -> acc + v }.toList())
    }

    @Test
    fun testMinByIfUnique() {
        val subject = listOf(10, 5, 9, 5, 4, 5).mapIndexed { index, i -> index to i }

        assertEquals(4 to 4, subject.minByIfUnique { it.second })
    }

    @Test
    fun testMinByIfUniqueShouldReturnNull() {
        val subject = listOf(10, 5, 9, 5, 6, 5).mapIndexed { index, i -> index to i }

        assertEquals(null, subject.minByIfUnique { it.second })
    }

}