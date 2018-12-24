package day24

import shared.extractAllInts
import shared.measureRuntime
import shared.readPuzzle

var verbose = false

var boost = 0

data class ImmuneGroup(
    val belongsTo: String,
    val id: Int,
    val initialUnits: Int,
    val hitPoints: Int,
    val attackDamageInitial: Int,
    val attackType: String,
    val immunities: Set<String>,
    val weaknesses: Set<String>,
    val initiative: Int
) : Comparable<ImmuneGroup> {
    override fun compareTo(other: ImmuneGroup) =
        if (effectivePower == other.effectivePower) initiative.compareTo(other.initiative) else effectivePower.compareTo(
            other.effectivePower
        )

    override fun equals(other: Any?): Boolean {
        return (other is ImmuneGroup && belongsTo == other.belongsTo && id == other.id)
    }

    override fun hashCode(): Int {
        return "$belongsTo $id".hashCode()
    }

    var attackDamage = attackDamageInitial
    var units = initialUnits

    fun reset() {
        attackDamage = attackDamageInitial + if (belongsTo == "Immune System") boost else 0
        units = initialUnits
    }

    fun chooseTarget(potentialTargets: List<ImmuneGroup>): ImmuneGroup? {
        val targetsWithDamage = potentialTargets.associateWith { attackDamage(it) }

        if (verbose)
            targetsWithDamage.forEach { (target, damage) ->
                println("$name would deal defending group ${target.id} $damage damage.")
            }

        val maxDamage = targetsWithDamage.maxBy { it.value }?.value ?: 0
        if (maxDamage == 0)
            return null

        val targets = targetsWithDamage.filter { it.value == maxDamage }.map { it.key }
        val maxEffectivePower = targets.maxBy { it.effectivePower }!!.effectivePower
        return targets.filter { it.effectivePower == maxEffectivePower }.sortedBy { it.initiative }.asReversed().first()
    }

    private fun attackDamage(target: ImmuneGroup): Int {
        val isImmune = target.immunities.contains(attackType)
        val isWeak = target.weaknesses.contains(attackType)

        return if (isImmune) 0 else if (isWeak) effectivePower * 2 else effectivePower
    }

    fun attack(target: ImmuneGroup) {
        val damage = attackDamage(target)
        val unitsLost = damage / target.hitPoints
        if (verbose)
            println("$name attacks defending group ${target.id} killing $unitsLost of ${target.units} units")
        target.units -= unitsLost
    }

    val effectivePower: Int get() = units * attackDamage

    val name = "$belongsTo group $id"

    companion object {
        //105 units each with 6988 hit points (weak to bludgeoning; immune to radiation) with an attack that does 616 radiation damage at initiative 17

        val weakRegex = Regex(".*weak to (\\w+[\\w, ]*).*")
        val immunityRegex = Regex("immune to (\\w+[\\w, ]*)")
        val attackTypeRegex = Regex("does \\d+ (\\w+) damage")

        fun fromString(s: String, belongsTo: String, id: Int): ImmuneGroup {
            val (units, hitPoints, attackDamage, initiative) = s.extractAllInts().toList()

            val weaknesses =
                weakRegex.findAll(s).map { it.groupValues[1] }.firstOrNull()?.split(", ")?.toSet() ?: emptySet()
            val immunities =
                immunityRegex.findAll(s).map { it.groupValues[1] }.firstOrNull()?.split(", ")?.toSet() ?: emptySet()
            val attackType = attackTypeRegex.findAll(s).map { it.groupValues[1] }.single()

            return ImmuneGroup(
                belongsTo,
                id,
                units,
                hitPoints,
                attackDamage,
                attackType,
                immunities,
                weaknesses,
                initiative
            )
        }
    }
}

fun extractArmy(puzzle: List<String>, type: String): List<ImmuneGroup> {
    var id = 1
    return puzzle.dropWhile { it != "$type:" }.drop(1).takeWhile { it.isNotEmpty() }
        .map { ImmuneGroup.fromString(it, type, id++) }
}

class Fight(val immuneSystem: List<ImmuneGroup>, val infection: List<ImmuneGroup>) {

    var allGroups = mutableListOf<ImmuneGroup>()

    fun targetSelection(): Map<ImmuneGroup, ImmuneGroup> {
        val all = allGroups.sorted().asReversed()
        val attackPlan = mutableMapOf<ImmuneGroup, ImmuneGroup>()

        all.forEach { group ->
            val potentialTargets = all.filter { (it.belongsTo != group.belongsTo) && !attackPlan.values.contains(it) }

            val target = group.chooseTarget(potentialTargets)
            if (target != null)
                attackPlan[group] = target
        }

        return attackPlan
    }

    fun attack(attackPlan: Map<ImmuneGroup, ImmuneGroup>): Boolean {
        val all = allGroups.sortedBy { it.initiative }.asReversed()
        var didSomething = false
        all.forEach { attacking ->
            if (attacking.units > 0) {
                val target = attackPlan[attacking]
                if (target != null && target.units > 0) {
                    attacking.attack(attackPlan[attacking]!!)
                    didSomething = true
                }
            }
        }

        allGroups.removeAll { it.units <= 0 }
        return didSomething
    }

    fun fight(boost: Int): Pair<String, Int>? {
        allGroups = (immuneSystem + infection).toMutableList()
        allGroups.forEach { it.reset() }

        var round = 1
        var prev = 0 to 0
        while (allGroups.distinctBy { it.belongsTo }.size == 2) {
            round ++
            if (!attack(targetSelection())) return null
            val immuneSystem = allGroups.filter { it.belongsTo == "Immune System" }.sumBy { it.units }
            val infection = allGroups.filter{ it.belongsTo == "Infection" }.sumBy { it.units }
            if (prev == immuneSystem to infection)
                return null
            prev = immuneSystem to infection
            if (true) {
                println("Round $round: $immuneSystem to $infection")
            }
        }
        val survivor = allGroups.distinctBy { it.belongsTo }.first().belongsTo
        val remaining = allGroups.sumBy { Math.max(0, it.units) }
        return survivor to remaining
    }

}

fun part1(puzzle: List<String>): Any? {
    val immuneSystem = extractArmy(puzzle, "Immune System")
    val infection = extractArmy(puzzle, "Infection")

    return Fight(immuneSystem, infection).fight(0)
}

fun part2(puzzle: List<String>): Any {
    val immuneSystem = extractArmy(puzzle, "Immune System")
    val infection = extractArmy(puzzle, "Infection")

    for (b in (30..Int.MAX_VALUE)) {
        boost = b
        println("Trying boost $b...")
        val result = Fight(immuneSystem, infection).fight(0)
        if (result != null && result.first != "Infection") {
            println("Boost $boost was needed!")
            println("Left with $result units!")
            return boost
        }
        println("Left with $result!")

    }

    return ""
}

fun main(args: Array<String>) {
    val puzzle = readPuzzle(24)

    measureRuntime {
        //println(part1(puzzle))
        println(part2(puzzle))
    }
}