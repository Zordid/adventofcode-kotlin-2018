package shared

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class GraphSearchTests {
    data class Node(val name: String, val neighbors: Collection<Node> = emptyList()) {
        override fun toString() = name
    }

    @Test
    fun findSolution() {
        val graph = generateGraph()
        val graphSearch = SearchEngineWithNodes<Node> { it.neighbors }
        val path = graphSearch.bfsSearch(graph) { it.name == "Z" }

        assertEquals("[A, C, E, Z]", path.toString())
    }

    @Test
    fun noSolution() {
        val graph = generateGraph()
        val graphSearch = SearchEngineWithNodes<Node> { it.neighbors }
        val path = graphSearch.bfsSearch(graph) { it.name == "Y" }

        assertEquals("[]", path.toString())
    }

    private fun generateGraph(): Node = Node("A", listOf(
            Node("B"),
            Node("C", listOf(
                    Node("E", listOf(Node("Z"))),
                    Node("F")
            )),
            Node("D", listOf(
                    Node("G"),
                    Node("H")

            ))))
}