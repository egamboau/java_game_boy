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
    SP;

    /**
     * Array of 16-bit register pairs including the stack pointer (SP).
     */
    private static RegisterType[] registerPairs = {BC, DE, HL, SP};

    /**
     * Array of 8-bit and 16-bit registers used for single register operations.
     */
    private static RegisterType[] singleRegisters = {B, C, D, E, H, L, HL, A};


    /**
     * Returns the 16-bit register pair (including SP) at the specified index.
     *
     * @param index the index of the register pair to retrieve
     * @return the RegisterType corresponding to the given index
     */
    public static RegisterType getRegisterPairFeaturingSP(final int index) {
        return registerPairs[index];
    }

    /**
     * Returns the register (8-bit or HL) at the specified index.
     *
     * @param index the index of the register to retrieve
     * @return the RegisterType corresponding to the given index
     */
    public static RegisterType getRegister(final int index) {
        return singleRegisters[index];
    }
}
