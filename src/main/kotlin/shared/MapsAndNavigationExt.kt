package shared

typealias MapDefinition = (Coordinate) -> Boolean

fun floodFill(start: Coordinate, layout: MapDefinition): Sequence<Pair<Int, Set<Coordinate>>> =
    SearchEngineWithNodes<Coordinate> { it.manhattanNeighbors.filter(layout) }.completeAcyclicTraverse(start)

fun Collection<Coordinate>.filterFirstReached(from: Coordinate, layout: MapDefinition): List<Coordinate> {
    if (isEmpty())
        return emptyList()

    val firstReached = floodFill(from, layout)
        .dropWhile { (_, nodesOnLevel) -> none { nodesOnLevel.contains(it) } }
        .firstOrNull()?.second ?: return emptyList()

    return filter { firstReached.contains(it) }
}