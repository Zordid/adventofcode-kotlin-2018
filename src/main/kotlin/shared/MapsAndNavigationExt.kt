package shared

private val deltas = sequenceOf(0 to -1, 1 to 0, 0 to 1, -1 to 0)

fun floodFill(start: Pair<Int, Int>, layout: (Int, Int) -> Boolean): Sequence<Pair<Int, Set<Pair<Int, Int>>>> =
    sequence {
        var nodesOnPreviousLevel: MutableSet<Pair<Int, Int>>
        var nodesOnLevel = mutableSetOf<Pair<Int, Int>>()
        var nodesOnNextLevel = mutableSetOf(start)
        var level = 0
        while (nodesOnNextLevel.isNotEmpty()) {
            nodesOnPreviousLevel = nodesOnLevel
            nodesOnLevel = nodesOnNextLevel
            yield(level++ to nodesOnLevel)
            nodesOnNextLevel = mutableSetOf()
            nodesOnLevel.forEach { (x, y) ->
                nodesOnNextLevel.addAll(deltas.map { x + it.first to y + it.second }
                    .filter {
                        layout(it.first, it.second) &&
                                !nodesOnPreviousLevel.contains(it) && !nodesOnLevel.contains(it)
                    }
                )
            }
        }
    }

fun Collection<Pair<Int, Int>>.filterFirstReached(
    from: Pair<Int, Int>,
    layout: (Int, Int) -> Boolean
): List<Pair<Int, Int>> {
    if (this.isEmpty())
        return emptyList()
    val firstReached = floodFill(from, layout)
        .dropWhile { (level, nodesOnLevel) -> this.none { nodesOnLevel.contains(it) } }
        .firstOrNull()?.second ?: return emptyList()

    return this.filter { firstReached.contains(it) }
}