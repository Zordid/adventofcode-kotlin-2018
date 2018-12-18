package shared

private val positiveIntRegex = Regex("\\d+")
private val intRegex = Regex("-?\\d+")

fun String.extractAllPositiveInts() = positiveIntRegex.findAll(this).map { it.value.toInt() }
fun String.extractAllInts() = intRegex.findAll(this).map { it.value.toInt() }
fun String.extractCoordinate() = extractAllInts().toList().let { (x, y) -> Coordinate(x, y) }

