package day13

import day11.allCoordinates
import shared.readPuzzle

enum class Heading(val dx: Int, val dy: Int) { N(0, -1), E(1, 0), S(0, 1), W(-1, 0) }
data class Cart(val x: Int, val y: Int, val heading: Heading, val turnCycle: Int = 0) {

    fun move(layout: List<List<Char>>): Cart {
        val ht = newHeading(x + heading.dx, y + heading.dy, layout)
        return Cart(x + heading.dx, y + heading.dy, ht.first, ht.second % 3)
    }

    private fun newHeading(x: Int, y: Int, layout: List<List<Char>>): Pair<Heading, Int> {
        return when (val element = layout[y][x]) {
            '+' -> when (turnCycle % 3) {
                0 -> Heading.values()[(heading.ordinal - 1 + Heading.values().size) % Heading.values().size] to turnCycle + 1
                1 -> heading to turnCycle + 1
                2 -> Heading.values()[(heading.ordinal + 1) % Heading.values().size] to turnCycle + 1
                else -> throw IllegalStateException()
            }
            '/' -> when (heading) {
                Heading.N -> Heading.E to turnCycle
                Heading.E -> Heading.N to turnCycle
                Heading.S -> Heading.W to turnCycle
                Heading.W -> Heading.S to turnCycle
            }
            '\\' -> when (heading) {
                Heading.N -> Heading.W to turnCycle
                Heading.E -> Heading.S to turnCycle
                Heading.S -> Heading.E to turnCycle
                Heading.W -> Heading.N to turnCycle
            }
            '|', '-' -> heading to turnCycle
            else -> throw IllegalStateException("$element when riding $heading")
        }
    }

}

fun Char.toHeading() = when (this) {
    '^' -> Heading.N
    '>' -> Heading.E
    'v' -> Heading.S
    '<' -> Heading.W
    else -> null
}

fun Char.toCart(x: Int, y: Int) = this.toHeading()?.let { Cart(x, y, it) }

fun part1(carts: List<Cart>, layout: List<List<Char>>): Any {
    var cs = carts
    while (cs.none { cart -> (cs - cart).any { it.x == cart.x && it.y == cart.y } }) {
        cs = cs.map { it.move(layout) }
        //print(cs, layout)
        //println()
    }

    val crashes = cs.filter { cart -> (cs - cart).any { it.x == cart.x && it.y == cart.y } }


    return crashes
}

fun part2(carts: List<Cart>, layout: List<List<Char>>): Any {
    var cs = carts
    while (true) {
        val crashes = (cs.filter { cart -> (cs - cart).any { it.x == cart.x && it.y == cart.y } }) +
                cs.filter { cart -> (cs - cart).any {
                    (it.move(layout).x == cart.x && it.move(layout).y == cart.y )
                    &&
                            (it.x == cart.move(layout).x && it.y == cart.move(layout).y )
                } }

        if (crashes.isNotEmpty()) {
            println("${crashes.size} carts crashed!")
            cs -= crashes
            println("${cs.size} remaining!")

            if (cs.size == 1) {
                print(cs+crashes, layout)
                println()
                println(cs)
                println(crashes)
                return "${cs.single().x},${cs.single().y}"
            }

        }

        cs = cs.map { it.move(layout) }
        //print(cs, layout)
        //println()
    }
}

fun print(carts: List<Cart>, layout: List<List<Char>>) {
    allCoordinates((layout.maxBy { it.size }!!.size), layout.size, baseCol = 0, baseRow = 0).forEach { (x, y) ->
        if (x == 0) println()
        val c = carts.filter { cart -> cart.x == x && cart.y == y }
        when {
            c.size == 1 -> print(c.single().heading)
            c.size > 1 -> print('X')
            else -> print(layout[y].getOrElse(x) { ' ' })
        }
    }
}

fun main(args: Array<String>) {
    val puzzle = readPuzzle(13)

    val layout = extractLayout(puzzle)
    val carts = extractCarts(puzzle)

    println(part1(carts, layout))
    println(part2(carts, layout))
}

fun extractCarts(puzzle: List<String>) =
    puzzle.mapIndexed { y, s -> s.mapIndexed { x, c -> c.toCart(x, y) } }.flatten().filterNotNull()

fun extractLayout(puzzle: List<String>): List<List<Char>> {
    return puzzle.map {
        it.toCharArray().map {
            when (it) {
                '<', '>' -> '-'
                '^', 'v' -> '|'
                else -> it
            }
        }
    }
}