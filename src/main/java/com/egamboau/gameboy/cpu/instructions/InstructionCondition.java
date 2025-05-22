package com.egamboau.gameboy.cpu.instructions;

/**
 * Enum representing the conditions under which a Game Boy CPU instruction is executed.
 * These conditions are based on the state of specific flags in the CPU's flag register.
 */
public enum InstructionCondition {

    /**
     * Condition where the Carry flag (C) is not set.
     * The instruction will execute if the Carry flag is cleared (0).
     */
    CARRY_FLAG_NOT_SET,

    /**
     * Condition where the Carry flag (C) is set.
     * The instruction will execute if the Carry flag is set (1).
     */
    CARRY_FLAG_SET,

    /**
     * Condition where the Zero flag (Z) is not set.
     * The instruction will execute if the Zero flag is cleared (0).
     */
    Z_FLAG_NOT_SET,

    /**
     * Condition where the Zero flag (Z) is set.
     * The instruction will execute if the Zero flag is set (1).
     */
    Z_FLAG_SET,
}
