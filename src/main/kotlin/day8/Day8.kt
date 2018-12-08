package day8

import shared.extractAllPositiveInts
import shared.readPuzzle

var treeCounter = 0

data class TreeDef(val id: Int, val childrenSize: Int, val metaDataSize: Int) {
    var parent: TreeDef? = null
    var children = mutableListOf<TreeDef>()
    val meta = mutableListOf<Int>()
    val metaDataSum: Int get() = meta.sum() + children.sumBy { it.metaDataSum }
    val value: Int
        get() {
            if (childrenSize == 0)
                return meta.sum()

            return meta.sumBy { children.getOrNull(it - 1)?.value ?: 0 }
        }
}

fun createTree(parent: TreeDef?, data: Iterator<Int>): TreeDef {
    val tree = TreeDef(treeCounter++, data.next(), data.next())
    tree.parent = parent
    (0 until tree.childrenSize).forEach { tree.children.add(createTree(tree, data)) }
    (0 until tree.metaDataSize).forEach { tree.meta += data.next() }
    return tree
}

fun part1(puzzle: String): Any {
    val treeData = puzzle.extractAllPositiveInts()

    val trees = createTree(null, treeData.iterator())


    return trees.metaDataSum
}

fun part2(puzzle: String): Any {
    val treeData = puzzle.extractAllPositiveInts()

    val trees = createTree(null, treeData.iterator())


    return trees.value
}

fun main(args: Array<String>) {
    val puzzle = readPuzzle(8).single()

    println(part1(puzzle))
    println(part2(puzzle))
}