package com.egamboau.gameboy.cpu.instructions;

/**
 * Enum representing the various registers used in the Game Boy CPU.
 * Registers are small storage locations within the CPU that hold data for operations.
 */
public enum RegisterType {

    /**
     * The 8-bit accumulator register (A).
     * Used for arithmetic and logical operations.
     */
    A,

    /**
     * The 16-bit AF register pair.
     * Combines the accumulator (A) and the flag register (F).
     */
    AF,

    /**
     * The 8-bit general-purpose register (B).
     */
    B,

    /**
     * The 16-bit BC register pair.
     * Combines the B and C registers.
     */
    BC,

    /**
     * The 8-bit general-purpose register (C).
     */
    C,

    /**
     * The 8-bit general-purpose register (D).
     */
    D,

    /**
     * The 16-bit DE register pair.
     * Combines the D and E registers.
     */
    DE,

    /**
     * The 8-bit general-purpose register (E).
     */
    E,

    /**
     * The 8-bit flag register (F).
     * Stores the CPU flags (Zero, Subtract, Half-Carry, Carry).
     */
    F,

    /**
     * The 8-bit high register (H).
     */
    H,

    /**
     * The 16-bit HL register pair.
     * Combines the H and L registers and is often used as a memory pointer.
     */
    HL,

    /**
     * The 8-bit low register (L).
     */
    L,

    /**
     * The 16-bit program counter (PC).
     * Points to the next instruction to be executed.
     */
    PC,

    /**
     * The 16-bit stack pointer (SP).
     * Points to the top of the stack in memory.
     */
    SP
}
