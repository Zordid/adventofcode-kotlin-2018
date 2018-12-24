package day24

import shared.extractAllInts
import shared.measureRuntime
import shared.readPuzzle

var verbose = false

enum class OpponentType(private val s: String) {
    ImmuneSystem("Immune System"), Infection("Infection");

    override fun toString() = s
}

data class ImmuneGroup(
    val type: OpponentType,
    val id: Int,
    val initialUnits: Int,
    val hitPoints: Int,
    val attackDamageInitial: Int,
    val attackType: String,
    val immunities: Set<String>,
    val weaknesses: Set<String>,
    val initiative: Int
) : Comparable<ImmuneGroup> {
    var units = initialUnits
    val isAlive: Boolean get() = units > 0
    private var attackDamage = attackDamageInitial
    private val effectivePower: Int get() = units * attackDamage
    private val name = "$type group $id"

    fun resetAndAddBoost(boost: Int) {
        attackDamage = attackDamageInitial + if (type == OpponentType.ImmuneSystem) boost else 0
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

    fun attack(target: ImmuneGroup) {
        val damage = attackDamage(target)
        val unitsLost = damage / target.hitPoints
        if (verbose)
            println("$name attacks defending group ${target.id} killing $unitsLost of ${target.units} units")
        target.units -= unitsLost
    }

    private fun attackDamage(target: ImmuneGroup): Int {
        val isImmune = target.immunities.contains(attackType)
        val isWeak = target.weaknesses.contains(attackType)

        return if (isImmune) 0 else if (isWeak) effectivePower * 2 else effectivePower
    }

    override fun compareTo(other: ImmuneGroup) =
        if (effectivePower == other.effectivePower) initiative.compareTo(other.initiative)
        else effectivePower.compareTo(other.effectivePower)

    override fun equals(other: Any?) =
        (other is ImmuneGroup && type == other.type && id == other.id)

    override fun hashCode() = "$type $id".hashCode()

    companion object {
        //105 units each with 6988 hit points (weak to bludgeoning; immune to radiation) with an attack that does 616 radiation damage at initiative 17

        private val weakRegex = Regex(".*weak to (\\w+[\\w, ]*).*")
        private val immunityRegex = Regex("immune to (\\w+[\\w, ]*)")
        private val attackTypeRegex = Regex("does \\d+ (\\w+) damage")

        fun fromString(s: String, type: OpponentType, id: Int): ImmuneGroup {
            val (units, hitPoints, attackDamage, initiative) = s.extractAllInts().toList()

            val weaknesses =
                weakRegex.findAll(s).map { it.groupValues[1] }.firstOrNull()?.split(", ")?.toSet() ?: emptySet()
            val immunities =
                immunityRegex.findAll(s).map { it.groupValues[1] }.firstOrNull()?.split(", ")?.toSet() ?: emptySet()
            val attackType = attackTypeRegex.findAll(s).map { it.groupValues[1] }.single()

            return ImmuneGroup(
                type,
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

fun extractArmy(puzzle: List<String>, type: OpponentType): List<ImmuneGroup> {
    var id = 1
    return puzzle.dropWhile { it != "$type:" }.drop(1).takeWhile { it.isNotEmpty() }
        .map { ImmuneGroup.fromString(it, type, id++) }
}

class Fight(immuneSystem: List<ImmuneGroup>, infection: List<ImmuneGroup>, boost: Int = 0) {

    private var aliveGroups: MutableList<ImmuneGroup> = (immuneSystem + infection).toMutableList()

    init {
        aliveGroups.forEach { it.resetAndAddBoost(boost) }
    }

    private fun targetSelection(): Map<ImmuneGroup, ImmuneGroup> {
        val attackTargets = mutableMapOf<ImmuneGroup, ImmuneGroup>()

        aliveGroups.sorted().asReversed().forEach { group ->
            val potentialTargets = aliveGroups.filter { (it.type != group.type) && !attackTargets.values.contains(it) }
            group.chooseTarget(potentialTargets)?.also { attackTargets[group] = it }
        }

        return attackTargets
    }

    private fun attack(attackTargets: Map<ImmuneGroup, ImmuneGroup>) {
        val attackers = attackTargets.keys.sortedBy { it.initiative }.asReversed()
        attackers.forEach { attacker ->
            if (attacker.isAlive) {
                attacker.attack(attackTargets[attacker]!!)
            }
        }
        aliveGroups.removeAll { !it.isAlive }
    }

    fun fight(): Pair<String, Int> {
        var statistics = buildStatistics()
        while (statistics.keys.size > 1) {
            attack(targetSelection())

            val newStats = buildStatistics()
            if (statistics == newStats)
                return "Tie" to 0
            statistics = newStats
        }
        val survivor = statistics.keys.single()
        val remaining = statistics.values.single()
        return survivor.toString() to remaining
    }

    private fun buildStatistics() =
        aliveGroups.associateBy(
            { it.type },
            { k -> aliveGroups.filter { it.type == k.type }.sumBy { it.units } })

}

fun part1(puzzle: List<String>): Int {
    val immuneSystem = extractArmy(puzzle, OpponentType.ImmuneSystem)
    val infection = extractArmy(puzzle, OpponentType.Infection)

    return Fight(immuneSystem, infection).fight().second
}

fun part2(puzzle: List<String>): Any {
    val immuneSystem = extractArmy(puzzle, OpponentType.ImmuneSystem)
    val infection = extractArmy(puzzle, OpponentType.Infection)

    for (boost in (1..Int.MAX_VALUE)) {
        val result = Fight(immuneSystem, infection, boost).fight()
        if (result.first == OpponentType.ImmuneSystem.toString()) {
            println("Boost $boost was needed!")
            return result.second
        }
    }

    return "Couldn't find a suitable boost!"
}

fun main(args: Array<String>) {
    val puzzle = readPuzzle(24)

    measureRuntime {
        println(part1(puzzle))
        println(part2(puzzle))
    }
}