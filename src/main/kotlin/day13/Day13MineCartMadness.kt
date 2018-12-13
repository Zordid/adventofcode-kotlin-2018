package day13

import day11.allCoordinates
import shared.readPuzzle

enum class Heading(val dx: Int, val dy: Int, val symbol: Char) {
    N(0, -1, '^'), E(1, 0, '>'),
    S(0, 1, 'v'), W(-1, 0, '<');

    val left get() = values()[(ordinal - 1 + values().size) % values().size]
    val right get() = values()[(ordinal + 1) % values().size]

    companion object {
        fun fromChar(c: Char) = values().firstOrNull { it.symbol == c }
    }
}

data class Cart(var x: Int, var y: Int, var heading: Heading) : Comparable<Cart> {

    var crashed = false
    private var turnCycle = 0

    fun move(tracks: List<List<Char>>) {
        x += heading.dx
        y += heading.dy
        heading = newHeading(tracks)
    }

    private fun newHeading(tracks: List<List<Char>>) = when (tracks[y][x]) {
        '+' -> intersectionHandlers[turnCycle++ % 3](heading)
        '/' -> when (heading) {
            Heading.N, Heading.S -> heading.right
            Heading.E, Heading.W -> heading.left
        }
        '\\' -> when (heading) {
            Heading.N, Heading.S -> heading.left
            Heading.E, Heading.W -> heading.right
        }
        else -> heading
    }

    override fun compareTo(other: Cart) =
        if (y != other.y) y.compareTo(other.y) else x.compareTo(other.x)

    infix fun posEq(other: Cart) = this !== other && this.x == other.x && this.y == other.y

    fun position() = "$x,$y"

    companion object {
        val intersectionHandlers = arrayOf<(Heading) -> Heading>(
            { h -> h.left }, { h -> h }, { h -> h.right }
        )
    }

}

fun Char.toCart(x: Int, y: Int) = Heading.fromChar(this)?.let { Cart(x, y, it) }

fun part1(puzzle: List<String>, show: Boolean = false): Any {
    val tracks = extractTracks(puzzle)
    val carts = extractCarts(puzzle)

    if (show) printLayout(carts, tracks)
    while (true) {
        carts.sorted().forEach { cart ->
            cart.move(tracks)
            if (carts.any { it posEq cart }) {
                return cart.position()
            }
        }
        if (show) printLayout(carts, tracks)
    }
}

fun part2(puzzle: List<String>, show: Boolean = false): Any {
    val tracks = extractTracks(puzzle)
    var carts = extractCarts(puzzle)

    if (show) printLayout(carts, tracks)
    while (carts.size > 1) {
        carts.sorted().forEach { cart ->
            cart.move(tracks)
            carts.firstOrNull { !it.crashed && cart posEq it }?.let { crashedInto ->
                cart.crashed = true
                crashedInto.crashed = true
            }
        }
        carts = carts.filter { !it.crashed }
        if (show) printLayout(carts, tracks)
    }
    return carts.single().position()
}

fun extractCarts(puzzle: List<String>) =
    puzzle.mapIndexed { y, s -> s.mapIndexed { x, c -> c.toCart(x, y) }.filterNotNull() }.flatten()

fun extractTracks(puzzle: List<String>) =
    puzzle.map { row ->
        row.toCharArray().map {
            when (it) {
                '<', '>' -> '-'
                '^', 'v' -> '|'
                else -> it
            }
        }
    }

fun printLayout(carts: List<Cart>, tracks: List<List<Char>>) {
    allCoordinates((tracks.maxBy { it.size }!!.size), tracks.size, baseCol = 0, baseRow = 0)
        .forEach { (x, y) ->
            if (x == 0) println()
            val c = carts.filter { cart -> !cart.crashed && cart.x == x && cart.y == y }
            when {
                c.size == 1 -> print(c.single().heading.symbol)
                c.size > 1 -> print('X')
                else -> print(tracks[y].getOrElse(x) { ' ' })
            }
        }
    println()
}

fun main(args: Array<String>) {
    val puzzle = readPuzzle(13)

    println(part1(puzzle))
    println(part2(puzzle))
}