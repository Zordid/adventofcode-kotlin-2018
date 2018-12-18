package shared

typealias MapDefinition = (Coordinate) -> Boolean

fun floodFill(start: Coordinate, layout: MapDefinition): Sequence<Pair<Int, Set<Coordinate>>> =
    sequence {
        var nodesOnPreviousLevel: MutableSet<Coordinate>
        var nodesOnLevel = mutableSetOf<Coordinate>()
        var nodesOnNextLevel = mutableSetOf(start)
        var level = 0
        while (nodesOnNextLevel.isNotEmpty()) {
            nodesOnPreviousLevel = nodesOnLevel
            nodesOnLevel = nodesOnNextLevel
            yield(level++ to nodesOnLevel)
            nodesOnNextLevel = mutableSetOf()
            nodesOnLevel.forEach { coordinate ->
                nodesOnNextLevel.addAll(coordinate.manhattanNeighbors
                    .filter { coordinate ->
                        layout(coordinate) &&
                                !nodesOnPreviousLevel.contains(coordinate) &&
                                !nodesOnLevel.contains(coordinate)
                    }
                )
            }
        }
    }

fun Collection<Coordinate>.filterFirstReached(from: Coordinate, layout: MapDefinition): List<Coordinate> {
    if (isEmpty())
        return emptyList()

    val firstReached = floodFill(from, layout)
        .dropWhile { (_, nodesOnLevel) -> none { nodesOnLevel.contains(it) } }
        .firstOrNull()?.second ?: return emptyList()

    return filter { firstReached.contains(it) }
}