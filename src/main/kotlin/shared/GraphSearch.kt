package shared

import java.util.*

typealias DebugHandler<N> = (level: Int, nodesOnLevel: Collection<N>, nodesVisited: Collection<N>) -> Boolean

typealias SolutionPredicate<N> = (node: N) -> Boolean

class Dijkstra<N>(val neighborNodes: (N) -> Collection<N>, val cost: (N, N) -> Int) {

    fun search(startNode: N, destNode: N?): Pair<Map<N, Int>, Map<N, N>> {
        val dist = mutableMapOf<N, Int>()
        val prev = mutableMapOf<N, N>()

        dist[startNode] = 0

        val queue = mutableSetOf(startNode)

        while (!queue.isEmpty()) {
            if (queue.size % 1000 == 0)
                println("Queue left: ${queue.size}")
            val u = queue.minBy { dist[it] ?: Int.MAX_VALUE }!!
            if (u == destNode) {
                return dist to prev
            }
            queue.remove(u)
            for (v in neighborNodes(u)) {
                val alt = dist[u]!! + cost(u, v)
                if (alt < dist.getOrPut(v) {
                        queue.add(v)
                        Int.MAX_VALUE }) {
                    dist[v] = alt
                    prev[v] = u
                }
            }
        }

        return dist to prev
    }

}

open class SearchEngineWithEdges<N, E>(
    private val edgesOfNode: (N) -> Iterable<E>,
    private val walkEdge: (N, E) -> N
) {

    var debugHandler: DebugHandler<N>? = null

    private inner class BfsSearch(val startNode: N, val isSolution: SolutionPredicate<N>) {
        private val nodesVisited = mutableSetOf<N>()
        private val nodesDiscoveredThrough = mutableMapOf<N, N>()

        private tailrec fun searchLevel(nodesOnLevel: Set<N>, level: Int = 0): N? {
            //println("Searching on level $level: ${nodesOnLevel.size} nodes.")
            if (debugHandler?.invoke(level, nodesOnLevel, nodesVisited) == true)
                return null
            val nodesOnNextLevel = mutableSetOf<N>()
            nodesOnLevel.forEach { currentNode ->
                nodesVisited.add(currentNode)
                edgesOfNode(currentNode).forEach { edge ->
                    val node = walkEdge(currentNode, edge)
                    if (!nodesVisited.contains(node) && !nodesOnLevel.contains(node)) {
                        nodesDiscoveredThrough[node] = currentNode
                        if (isSolution(node))
                            return node
                        else
                            nodesOnNextLevel.add(node)
                    }
                }
            }
            return if (nodesOnNextLevel.isEmpty())
                null
            else
                searchLevel(nodesOnNextLevel, level + 1)
        }

        private fun buildStack(node: N?): Stack<N> {
            //println("Building stack for solution node $node")
            val pathStack = Stack<N>()
            var nodeFoundThroughPrevious = node
            while (nodeFoundThroughPrevious != null) {
                pathStack.add(0, nodeFoundThroughPrevious)
                nodeFoundThroughPrevious = nodesDiscoveredThrough[nodeFoundThroughPrevious]
            }
            return pathStack
        }

        fun search() = buildStack(if (isSolution(startNode)) startNode else searchLevel(setOf(startNode)))

    }

    fun bfsSearch(startNode: N, isSolution: SolutionPredicate<N>): Stack<N> {
        return BfsSearch(startNode, isSolution).search()
    }

    fun completeAcyclicTraverse(startNode: N): Sequence<Pair<Int, Set<N>>> =
        sequence {
            var nodesOnPreviousLevel: Set<N>
            var nodesOnLevel = setOf<N>()
            var nodesOnNextLevel = setOf(startNode)
            var level = 0
            while (nodesOnNextLevel.isNotEmpty()) {
                nodesOnPreviousLevel = nodesOnLevel
                nodesOnLevel = nodesOnNextLevel
                yield(level++ to nodesOnLevel)
                nodesOnNextLevel = mutableSetOf()
                nodesOnLevel.forEach { node ->
                    nodesOnNextLevel.addAll(edgesOfNode(node).map { e -> walkEdge(node, e) }
                        .filter { neighbor ->
                            !nodesOnPreviousLevel.contains(neighbor) &&
                                    !nodesOnLevel.contains(neighbor)
                        })
                }
            }
        }

}

class SearchEngineWithNodes<N>(neighborNodes: (N) -> Collection<N>) :
    SearchEngineWithEdges<N, N>(neighborNodes, { _, edge -> edge })

fun <N, E> breadthFirstSearch(
    startNode: N,
    edgesOf: (N) -> Collection<E>,
    walkEdge: (N, E) -> N,
    isSolution: SolutionPredicate<N>
) =
    SearchEngineWithEdges(edgesOf, walkEdge).bfsSearch(startNode, isSolution)

fun <N> breadthFirstSearch(
    startNode: N,
    neighborNodes: (N) -> Collection<N>,
    isSolution: SolutionPredicate<N>
) =
    SearchEngineWithNodes(neighborNodes).bfsSearch(startNode, isSolution)

fun loggingDebugger(level: Int, nodesOnLevel: Collection<Any>, nodesVisited: Collection<Any>): Boolean {
    println("I am on level $level, searching through ${nodesOnLevel.size}. Visited so far: ${nodesVisited.size}")
    return false
}
