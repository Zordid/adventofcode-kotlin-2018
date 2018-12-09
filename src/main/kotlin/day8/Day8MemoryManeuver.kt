package day8

import shared.extractAllPositiveInts
import shared.readPuzzle

data class TreeDef(val childrenSize: Int, val metadataSize: Int) {
    val children = mutableListOf<TreeDef>()
    val metadata = mutableListOf<Int>()
    val metadataSum: Int get() = metadata.sum() + children.sumBy { it.metadataSum }
    val value: Int
        get() =
            if (childrenSize == 0) metadata.sum()
            else metadata.sumBy { children.getOrNull(it - 1)?.value ?: 0 }
}

fun createTree(data: Iterator<Int>): TreeDef =
    TreeDef(data.next(), data.next()).also { tree ->
        repeat(tree.childrenSize) { tree.children.add(createTree(data)) }
        repeat(tree.metadataSize) { tree.metadata += data.next() }
    }

fun part1(puzzle: String): Any {
    val treeData = puzzle.extractAllPositiveInts()

    val trees = createTree(treeData.iterator())
    return trees.metadataSum
}

fun part2(puzzle: String): Any {
    val treeData = puzzle.extractAllPositiveInts()

    val trees = createTree(treeData.iterator())
    return trees.value
}

fun main(args: Array<String>) {
    val puzzle = readPuzzle(8).single()

    println(part1(puzzle))
    println(part2(puzzle))
}