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

; ---------------------------------------
; Initialize registers to start values
; R0 = 0/1 (change here to make your CPU burn)
    mov  r8d, 0
; R1 = 0
    mov  r9d, 0
; R2 = 0
    mov  r10d, 0
; R3 = 0
    mov  r11d, 0
; R4 = IP = 0
    mov  r12d, 0
; R5 = 0
    mov  r13d, 0

; ---------------------------------------
; The puzzle program, let the show begin
i0:
; addi 4 16 4
    mov  r12d, 0
    mov  eax, r12d
    add  eax, 16
    mov  r12d, eax
    jmp  jump
i1:
; seti 1 7 2
    mov  r10d, 1
i2:
; seti 1 1 5
    mov  r13d, 1
i3:
; mulr 2 5 3
    mov  eax, r10d
    mul  r13d
    mov  r11d, eax
i4:
; eqrr 3 1 3
    mov  ecx, 1
    cmp  r11d, r9d
    je   inner4
    dec  ecx
    inner4:
    mov  r11d, ecx
i5:
; addr 3 4 4
    mov  r12d, 5
    mov  eax, r11d
    add  eax, r12d
    mov  r12d, eax
    jmp  jump
i6:
; addi 4 1 4
    mov  r12d, 6
    mov  eax, r12d
    add  eax, 1
    mov  r12d, eax
    jmp  jump
i7:
; addr 2 0 0
    mov  eax, r10d
    add  eax, r8d
    mov  r8d, eax
i8:
; addi 5 1 5
    mov  eax, r13d
    add  eax, 1
    mov  r13d, eax
i9:
; gtrr 5 1 3
    mov  ecx, 1
    cmp  r13d, r9d
    ja   inner9
    dec  ecx
    inner9:
    mov  r11d, ecx
i10:
; addr 4 3 4
    mov  r12d, 10
    mov  eax, r12d
    add  eax, r11d
    mov  r12d, eax
    jmp  jump
i11:
; seti 2 7 4
    mov  r12d, 2
    jmp  jump
i12:
; addi 2 1 2
    mov  eax, r10d
    add  eax, 1
    mov  r10d, eax
i13:
; gtrr 2 1 3
    mov  ecx, 1
    cmp  r10d, r9d
    ja   inner13
    dec  ecx
    inner13:
    mov  r11d, ecx
i14:
; addr 3 4 4
    mov  r12d, 14
    mov  eax, r11d
    add  eax, r12d
    mov  r12d, eax
    jmp  jump
i15:
; seti 1 3 4
    mov  r12d, 1
    jmp  jump
i16:
; mulr 4 4 4
    mov  r12d, 16
    mov  eax, r12d
    mul  r12d
    mov  r12d, eax
    jmp  jump
i17:
; addi 1 2 1
    mov  eax, r9d
    add  eax, 2
    mov  r9d, eax
i18:
; mulr 1 1 1
    mov  eax, r9d
    mul  r9d
    mov  r9d, eax
i19:
; mulr 4 1 1
    mov  r12d, 19
    mov  eax, r12d
    mul  r9d
    mov  r9d, eax
i20:
; muli 1 11 1
    mov  eax, r9d
    mov  ebx, 11
    mul  ebx
    mov  r9d, eax
i21:
; addi 3 3 3
    mov  eax, r11d
    add  eax, 3
    mov  r11d, eax
i22:
; mulr 3 4 3
    mov  r12d, 22
    mov  eax, r11d
    mul  r12d
    mov  r11d, eax
i23:
; addi 3 9 3
    mov  eax, r11d
    add  eax, 9
    mov  r11d, eax
i24:
; addr 1 3 1
    mov  eax, r9d
    add  eax, r11d
    mov  r9d, eax
i25:
; addr 4 0 4
    mov  r12d, 25
    mov  eax, r12d
    add  eax, r8d
    mov  r12d, eax
    jmp  jump
i26:
; seti 0 1 4
    mov  r12d, 0
    jmp  jump
i27:
; setr 4 9 3
    mov  r11d, r12d
i28:
; mulr 3 4 3
    mov  r12d, 28
    mov  eax, r11d
    mul  r12d
    mov  r11d, eax
i29:
; addr 4 3 3
    mov  r12d, 29
    mov  eax, r12d
    add  eax, r11d
    mov  r11d, eax
i30:
; mulr 4 3 3
    mov  r12d, 30
    mov  eax, r12d
    mul  r11d
    mov  r11d, eax
i31:
; muli 3 14 3
    mov  eax, r11d
    mov  ebx, 14
    mul  ebx
    mov  r11d, eax
i32:
; mulr 3 4 3
    mov  r12d, 32
    mov  eax, r11d
    mul  r12d
    mov  r11d, eax
i33:
; addr 1 3 1
    mov  eax, r9d
    add  eax, r11d
    mov  r9d, eax
i34:
; seti 0 6 0
    mov  r8d, 0
i35:
; seti 0 7 4
    mov  r12d, 0
    jmp  jump

; ---------------------------------------
; Generated jump table
jump:
    inc  r12d ; don't forget to increment IP
    cmp  r12d, 0
    je   i0
    cmp  r12d, 1
    je   i1
    cmp  r12d, 2
    je   i2
    cmp  r12d, 3
    je   i3
    cmp  r12d, 4
    je   i4
    cmp  r12d, 5
    je   i5
    cmp  r12d, 6
    je   i6
    cmp  r12d, 7
    je   i7
    cmp  r12d, 8
    je   i8
    cmp  r12d, 9
    je   i9
    cmp  r12d, 10
    je   i10
    cmp  r12d, 11
    je   i11
    cmp  r12d, 12
    je   i12
    cmp  r12d, 13
    je   i13
    cmp  r12d, 14
    je   i14
    cmp  r12d, 15
    je   i15
    cmp  r12d, 16
    je   i16
    cmp  r12d, 17
    je   i17
    cmp  r12d, 18
    je   i18
    cmp  r12d, 19
    je   i19
    cmp  r12d, 20
    je   i20
    cmp  r12d, 21
    je   i21
    cmp  r12d, 22
    je   i22
    cmp  r12d, 23
    je   i23
    cmp  r12d, 24
    je   i24
    cmp  r12d, 25
    je   i25
    cmp  r12d, 26
    je   i26
    cmp  r12d, 27
    je   i27
    cmp  r12d, 28
    je   i28
    cmp  r12d, 29
    je   i29
    cmp  r12d, 30
    je   i30
    cmp  r12d, 31
    je   i31
    cmp  r12d, 32
    je   i32
    cmp  r12d, 33
    je   i33
    cmp  r12d, 34
    je   i34
    cmp  r12d, 35
    je   i35

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

