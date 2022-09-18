package day24

import shared.extractAllInts
import shared.measureRuntime
import shared.readPuzzle

enum class OpponentType(private val s: String) {
    ImmuneSystem("Immune System"), Infection("Infection");

    override fun toString() = s
}

data class ImmuneGroup(
    val type: OpponentType,
    val id: Int,
    val initialUnits: Int,
    val hitPoints: Int,
    val attackDamage: Int,
    val attackType: String,
    val immunities: Set<String>,
    val weaknesses: Set<String>,
    val initiative: Int
) {
    val name = "$type group $id"

    fun boostedBy(boost: Int) = copy(attackDamage = attackDamage + boost)

    companion object {
        private val weakRegex = Regex("weak to (\\w+[\\w, ]*)")
        private val immunityRegex = Regex("immune to (\\w+[\\w, ]*)")
        private val attackTypeRegex = Regex("does \\d+ (\\w+) damage")

        fun fromString(s: String, type: OpponentType, id: Int): ImmuneGroup {
            val (units, hitPoints, attackDamage, initiative) = s.extractAllInts().toList()

            val weaknesses = weakRegex.findAll(s)
                .map { it.groupValues[1] }.singleOrNull()?.split(", ")?.toSet()
            val immunities = immunityRegex.findAll(s)
                .map { it.groupValues[1] }.singleOrNull()?.split(", ")?.toSet()
            val attackType = attackTypeRegex.findAll(s)
                .map { it.groupValues[1] }.single()

            return ImmuneGroup(
                type,
                id,
                units,
                hitPoints,
                attackDamage,
                attackType,
                immunities ?: emptySet(),
                weaknesses ?: emptySet(),
                initiative
            )
        }
    }
}

fun extractArmy(puzzle: List<String>, type: OpponentType) =
    puzzle.dropWhile { it != "$type:" }.drop(1).takeWhile { it.isNotEmpty() }
        .mapIndexed { idx, l -> ImmuneGroup.fromString(l, type, idx + 1) }

class ImmuneSystemSimulator(
    immuneSystem: List<ImmuneGroup>,
    infection: List<ImmuneGroup>,
    immuneBoost: Int = 0,
    private val verbose: Boolean = false
) {

    private val all = immuneSystem.map { it.boostedBy(immuneBoost) } + infection
    private val groupsWithUnits = mutableMapOf<ImmuneGroup, Int>()

    private val ImmuneGroup.units: Int get() = groupsWithUnits[this] ?: initialUnits
    private val ImmuneGroup.effectivePower: Int get() = units * attackDamage
    private val ImmuneGroup.isAlive: Boolean get() = units > 0

    private val targetComparator =
        compareBy<Map.Entry<ImmuneGroup, Int>>(
            { it.value },
            { it.key.effectivePower },
            { it.key.initiative }).reversed()!!

    private fun ImmuneGroup.chooseTarget(potentialTargets: List<ImmuneGroup>) =
        potentialTargets
            .associateWith { attackDamage(it) }
            .filter { it.value > 0 }.entries
            .onEach { (target, damage) ->
                if (verbose)
                    println("$name would deal defending group ${target.id} $damage damage")
            }
            .sortedWith(targetComparator)
            .firstOrNull()?.key

    private fun ImmuneGroup.attack(target: ImmuneGroup) {
        val damage = attackDamage(target)
        val unitsLost = damage / target.hitPoints
        if (verbose)
            println("$name attacks defending group ${target.id} killing $unitsLost units")
        groupsWithUnits[target] = target.units - unitsLost
    }

    private fun ImmuneGroup.attackDamage(target: ImmuneGroup): Int {
        val isImmune = target.immunities.contains(attackType)
        val isWeak = target.weaknesses.contains(attackType)
        return when {
            isImmune -> 0
            isWeak -> effectivePower * 2
            else -> effectivePower
        }
    }

    private val selectionComparator = compareBy<ImmuneGroup>({ it.effectivePower }, { it.initiative }).reversed()

    private val attackComparator = compareBy<Map.Entry<ImmuneGroup, ImmuneGroup>> { it.key.initiative }.reversed()

    private val alive: List<ImmuneGroup> get() = all.filter { it.isAlive }

    private fun targetSelection(): Map<ImmuneGroup, ImmuneGroup> {
        val attackTargets = mutableMapOf<ImmuneGroup, ImmuneGroup>()

        alive.sortedWith(selectionComparator).forEach { attacker ->
            val potentialTargets = alive.filter { (it.type != attacker.type) && !attackTargets.values.contains(it) }
            attacker.chooseTarget(potentialTargets)?.also { attackTargets[attacker] = it }
        }
        if (verbose)
            println()

        return attackTargets
    }

    private fun attack(attackTargets: Map<ImmuneGroup, ImmuneGroup>) {
        attackTargets.entries.sortedWith(attackComparator).forEach { (attacker, target) ->
            if (attacker.isAlive) {
                attacker.attack(target)
            }
        }
    }

    fun fight(): Pair<String, Int> {
        var statistics = buildStatistics()
        var round = 1
        while (statistics.keys.size > 1) {
            if (verbose) println("\nRound ${round++}")
            attack(targetSelection())

            val newStats = buildStatistics()
            if (statistics == newStats) {
                if (verbose) println("Tie after $round rounds")
                return "Tie" to 0
            }
            statistics = newStats
        }
        val survivor = statistics.keys.single()
        val remaining = statistics.values.single()
        if (verbose)
            println("\n$survivor wins with $remaining remaining units after $round rounds")
        return survivor.toString() to remaining
    }

    private fun buildStatistics() =
        alive.associateBy(
            { it.type },
            { k -> alive.filter { it.type == k.type }.sumOf { it.units } })

}

fun part1(puzzle: List<String>, verbose: Boolean = false): Int {
    val immuneSystem = extractArmy(puzzle, OpponentType.ImmuneSystem)
    val infection = extractArmy(puzzle, OpponentType.Infection)

    return ImmuneSystemSimulator(immuneSystem, infection, verbose = verbose).fight().second
}

fun part2(puzzle: List<String>): Any {
    val immuneSystem = extractArmy(puzzle, OpponentType.ImmuneSystem)
    val infection = extractArmy(puzzle, OpponentType.Infection)

    for (boost in (1..Int.MAX_VALUE)) {
        val result = ImmuneSystemSimulator(immuneSystem, infection, boost).fight()
        if (result.first == OpponentType.ImmuneSystem.toString()) {
            println("Boost $boost was needed!")
            return result.second
        }
    }

    return "Couldn't find a suitable boostedBy!"
}

fun main() {
    val puzzle = readPuzzle(24)

    measureRuntime {
        println(part1(puzzle))
        println(part2(puzzle))
    }
}