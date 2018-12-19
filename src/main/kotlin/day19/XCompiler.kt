package day19

import shared.extractAllInts
import shared.readPuzzle

typealias InstructionX86 = (a: Int, b: Int, c: Int, offset: Int) -> List<String>

fun main(args: Array<String>) {
    XCompiler(readPuzzle(19)).translate()
}

class XCompiler(input: List<String>) {

    private val program = input.filter { !it.startsWith('#') && !it.isBlank() }
    private val ipRegister = input.firstOrNull { it.startsWith("#ip") }?.extractAllInts()?.first()

    fun translate() {
        val asm = prolog +
                generateInitialization() +
                generateProgramSection() +
                generateJumpSection() +
                epilog

        asm.forEach { println(it) }
    }

    private fun generateInitialization(registers: IntArray = IntArray(6)): List<String> {
        return section("Initialize registers to start values") +
                r.mapIndexed { idx, n -> "; R$idx = ${registers[idx]} \n    mov  $n, ${registers[idx]}" }
    }

    private fun generateProgramSection(): List<String> {
        return section("Compiled program") + program.mapIndexed { offset, line ->
            translate(
                offset,
                line
            ).toList()
        }.flatten()
    }

    private fun translate(offset: Int, line: String): Sequence<String> = sequence {
        val mnemonic = line.substring(0, 4)
        val (a, b, c) = line.extractAllInts().toList()
        val compiled = instructionTable[mnemonic]!!(a, b, c, offset).map { "    $it" }
        yield("i$offset:")
        yield("; $line")
        if (ipRegister != null && compiled.take(compiled.size - 1).any { it.contains(r[ipRegister]) }) {
            yield("    mov  ${r[ipRegister]}, $offset")
        }
        yieldAll(compiled)
        if (ipRegister != null && c == ipRegister)
            yield("    jmp  jump")
    }

    private fun generateJumpSection(): Sequence<String> = sequence {
        if (ipRegister != null) {
            yieldAll(section("Generated jump table"))
            yield("jump:")
            yield("    inc  ${r[ipRegister]} ; don't forget to increment IP")
            program.indices.forEach {
                yield("    cmp  ${r[ipRegister]}, $it")
                yield("    je   i$it")
            }
        }
    }

    companion object {

        private val r = Array(6) { n -> "r${n + 8}d" }
        private val instructionTable = mapOf<String, InstructionX86>(
            "addr" to { a, b, c, _ ->
                listOf(
                    "mov  eax, ${r[a]}",
                    "add  eax, ${r[b]}",
                    "mov  ${r[c]}, eax"
                )
            },
            "addi" to { a, b, c, _ ->
                listOf(
                    "mov  eax, ${r[a]}",
                    "add  eax, $b",
                    "mov  ${r[c]}, eax"
                )
            },
            "mulr" to { a, b, c, _ ->
                listOf(
                    "mov  eax, ${r[a]}",
                    "mul  ${r[b]}",
                    "mov  ${r[c]}, eax"
                )
            },
            "muli" to { a, b, c, _ ->
                listOf(
                    "mov  eax, ${r[a]}",
                    "mov  ebx, $b",
                    "mul  ebx",
                    "mov  ${r[c]}, eax"
                )
            },
            "banr" to { a, b, c, _ ->
                listOf(
                    "mov  eax, ${r[a]}",
                    "and  eax, ${r[b]}",
                    "mov  ${r[c]}, eax"
                )
            },
            "bani" to { a, b, c, _ ->
                listOf(
                    "mov  eax, ${r[a]}",
                    "and  eax, $b",
                    "mov  ${r[c]}, eax"
                )
            },
            "borr" to { a, b, c, _ ->
                listOf(
                    "mov  eax, ${r[a]}",
                    "or   eax, ${r[b]}",
                    "mov  ${r[c]}, eax"
                )
            },
            "bori" to { a, b, c, _ ->
                listOf(
                    "mov  eax, ${r[a]}",
                    "or   eax, $b",
                    "mov  ${r[c]}, eax"
                )
            },
            "setr" to { a, _, c, _ ->
                listOf(
                    "mov  ${r[c]}, ${r[a]}"
                )
            },
            "seti" to { a, _, c, _ ->
                listOf(
                    "mov  ${r[c]}, $a"
                )
            },
            "gtir" to { a, b, c, offset ->
                listOf(
                    "mov  ecx, 0",
                    "cmp  ${r[b]}, $a",
                    "ja   inner$offset",
                    "inc  ecx",
                    "inner$offset:",
                    "mov  ${r[c]}, ecx"

                )
            },
            "gtri" to { a, b, c, offset ->
                listOf(
                    "mov  ecx, 1",
                    "cmp  ${r[a]}, $b",
                    "ja   inner$offset",
                    "dec  ecx",
                    "inner$offset:",
                    "mov  ${r[c]}, ecx"
                )
            },
            "gtrr" to { a, b, c, offset ->
                listOf(
                    "mov  ecx, 1",
                    "cmp  ${r[a]}, ${r[b]}",
                    "ja   inner$offset",
                    "dec  ecx",
                    "inner$offset:",
                    "mov  ${r[c]}, ecx"
                )
            },
            "eqir" to { a, b, c, offset ->
                listOf(
                    "mov  ecx, 1",
                    "cmp  ${r[b]}, $a",
                    "je   inner$offset",
                    "dec  ecx",
                    "inner$offset:",
                    "mov  ${r[c]}, ecx"
                )
            },
            "eqri" to { a, b, c, offset ->
                listOf(
                    "mov  ecx, 1",
                    "cmp  ${r[a]}, $b",
                    "je   inner$offset",
                    "dec  ecx",
                    "inner$offset:",
                    "mov  ${r[c]}, ecx"
                )
            },
            "eqrr" to { a, b, c, offset ->
                listOf(
                    "mov  ecx, 1",
                    "cmp  ${r[a]}, ${r[b]}",
                    "je   inner$offset",
                    "dec  ecx",
                    "inner$offset:",
                    "mov  ${r[c]}, ecx"
                )
            })

        private fun section(name: String): List<String> {
            return listOf(
                "",
                "; ---------------------------------------",
                "; $name"
            )
        }

        private val prolog = """
; ************************************************
; AoC 2018 Day 19
; Puzzle code transpiled to x86 Assembler!
; ************************************************
; Some basic data declarations
section .data
; -----
; Define constants
NULL            equ  0 ; end of string
EXIT_SUCCESS    equ  0 ; successful operation
STDOUT          equ  1 ; standard output
SYS_exit        equ 60 ; call code for terminate
SYS_write       equ  1 ; call code for write

doneMsg         db "Done! Your result is: "
doneMsgLen      dq 22
lf              db 10

section .bss
decimal         resb 32

; *************************************************************
; Code Section
section .text
global _start
_start:
        """.trimIndent().split("\n")

        private val epilog = """
; ************************************************************
; Done, print result and terminate program.
last:
    mov   rax, SYS_write
    mov   rdi, STDOUT
    mov   rsi, doneMsg ; msg address
    mov   rdx, qword [doneMsgLen] ; length value
    syscall

    mov   eax, r8d
    call  OutputResult

    mov   rax, SYS_exit ; Call code for exit
    mov   rdi, EXIT_SUCCESS ; Exit program with success
    syscall

OutputResult:
    mov   rdi, decimal
    mov   rsi, rax
    call  IntegerToDecimal

    mov   eax, 1        ; sys_write
    mov   edi, 1        ; STDOUT
    mov   rsi, decimal  ; String address
    mov   edx, 32       ; Max. string length
    syscall

    mov   eax, 1        ; sys_write
    mov   edi, 1        ; STDOUT
    mov   rsi, lf       ; Line Feed address
    mov   edx, 1        ; Max. string length
    syscall

    ret

IntegerToDecimal:
    mov rax, rsi
    mov ebx, 10         ; Divisor
    xor ecx, ecx        ; RCX=0 (number of digits)
  .Loop_1:
    xor edx, edx
    div rbx             ; RDX:RAX / RBX = RAX Remainder RDX
    push dx             ; LIFO
    add cl, 1
    or  rax, rax        ; RAX == 0?
    jnz .Loop_1         ; no: once more
  .Loop_2:
    pop ax              ; Get back pushed digits
    or al, 00110000b    ; Convert to ASCII
    mov [rdi], al       ; Store character
    add rdi, 1          ; Increment target address
    loop .Loop_2        ; Until there are no digits left
    mov byte [rdi], 0   ; ASCIIZ-null-terminator

    ret
        """.trimIndent()
    }

}




