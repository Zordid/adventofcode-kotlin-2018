package shared

typealias Instruction = (a: Int, b: Int, c: Int, r: IntArray) -> Unit

class ElfDeviceCpu(program: List<String>) {

    val ipRegister = program.single { it.startsWith("#ip") }.substring(4).toInt()
    val code = program.filter { !it.startsWith("#") }.map {
        it.substring(0, 4) to it.extractAllPositiveInts().toList()
    }

    var instructionsCounter = 0L
    var ip = 0
    var registers = IntArray(6)

    fun reset() {
        instructionsCounter = 0
        ip = 0
        registers = IntArray(6)
    }

    fun step(debug: Boolean = false): Boolean {
        if (ip !in code.indices) return false

        registers[ipRegister] = ip
        val (mnemonic, operands) = code[ip]
        val (a, b, c) = operands

        if (debug)
            print("ip=$ip [${registers.joinToString()}] $mnemonic ${operands.joinToString(" ")} ")
        instructionSet[mnemonic]!!(a, b, c, registers)
        if (debug)
            println("[${registers.joinToString()}]")
        ip = registers[ipRegister]

        ip++
        instructionsCounter++
        return (ip in code.indices)
    }

    fun run(debug: Boolean = false): Sequence<Pair<IntArray, Int>> = sequence {
        yield(registers to ip)
        while (step(debug))
            yield(registers to ip)
        yield(registers to ip)
    }

    companion object {
        val instructionSet = mapOf<String, Instruction>(
            "addr" to { a, b, c, r -> r[c] = r[a] + r[b] },
            "addi" to { a, b, c, r -> r[c] = r[a] + b },
            "mulr" to { a, b, c, r -> r[c] = r[a] * r[b] },
            "muli" to { a, b, c, r -> r[c] = r[a] * b },
            "banr" to { a, b, c, r -> r[c] = r[a] and r[b] },
            "bani" to { a, b, c, r -> r[c] = r[a] and b },
            "borr" to { a, b, c, r -> r[c] = r[a] or r[b] },
            "bori" to { a, b, c, r -> r[c] = r[a] or b },
            "setr" to { a, _, c, r -> r[c] = r[a] },
            "seti" to { a, _, c, r -> r[c] = a },
            "gtir" to { a, b, c, r -> r[c] = if (a > r[b]) 1 else 0 },
            "gtri" to { a, b, c, r -> r[c] = if (r[a] > b) 1 else 0 },
            "gtrr" to { a, b, c, r -> r[c] = if (r[a] > r[b]) 1 else 0 },
            "eqir" to { a, b, c, r -> r[c] = if (a == r[b]) 1 else 0 },
            "eqri" to { a, b, c, r -> r[c] = if (r[a] == b) 1 else 0 },
            "eqrr" to { a, b, c, r -> r[c] = if (r[a] == r[b]) 1 else 0 }
        )
    }

    fun state(): String {
        return "IP=$ip [${registers.mapIndexed { idx, v -> "R$idx: $v" }.joinToString(" ")}]" +
                " (processed $instructionsCounter instructions so far)"
    }

    fun visualizeInstruction(idx: Int): String {
        val (instruction, operands) = code[idx]
        val (a, b, c) = operands
        val result = StringBuilder()
        result.append(idx.toString().padStart(2)).append(": ")
        result.append("$instruction ${operands.joinToString(" ")}".padEnd(30))
        val s1I = a
        val s1R = if (a == ipRegister) idx else "R$a"
        val s2I = b
        val s2R = if (b == ipRegister) idx else "R$b"

        if (c != ipRegister) {
            val dest = "R$c"
            val source = when (instruction) {
                "addr" -> "$s1R + $s2R"
                "addi" -> "$s1R + $s2I"
                "mulr" -> "$s1R * $s2R"
                "muli" -> "$s1R * $s2I"
                "banr" -> "$s1R and $s2R"
                "bani" -> "$s1R and $s2I"
                "borr" -> "$s1R or $s2R"
                "bori" -> "$s1R or $s2I"
                "setr" -> s1R
                "seti" -> s1I
                "gtir" -> "if $s1I > $s2R then 1 else 0"
                "gtri" -> "if $s1R > $s2I then 1 else 0"
                "gtrr" -> "if $s1R > $s2R then 1 else 0"
                "eqir" -> "if $s1I == $s2R then 1 else 0"
                "eqri" -> "if $s1R == $s2I then 1 else 0"
                "eqrr" -> "if $s1R == $s2R then 1 else 0"
                else -> ""
            }
            result.append("$dest = $source")
        } else {
            val dest = when (instruction) {
                "addr" -> when (ipRegister) {
                    a -> "relative $s2R+1"
                    b -> "relative $s1R+1"
                    else -> "absolute $s1R+$s2R+1"
                }

                "addi" -> if (a == ipRegister) "relative ${b + 1}" else "absolute $s1R+$s2I+1"
                "seti" -> "absolute ${a + 1}"
                else -> ""
            }
            result.append("jump $dest")
        }
        return result.toString()
    }

    override fun toString() = List(code.size) { idx -> visualizeInstruction(idx) }.joinToString("\n")

}