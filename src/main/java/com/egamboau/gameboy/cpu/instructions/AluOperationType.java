package com.egamboau.gameboy.cpu.instructions;

/**
 * Enumeration of ALU (Arithmetic Logic Unit) operations supported by the GameBoy CPU.
 * Each constant represents an 8-bit operation typically applied to the A register
 * (accumulator) or used for comparison/combination of values.
 */
public enum AluOperationType {

    //region Enum entries
    /** Addition: A <- A + value. */
    ADD_A,
    /** Addition with carry: A <- A + value + carry. */
    ADC_A,
    /** Subtraction: A <- A - value. */
    SUB,
    /** Subtraction with carry/borrow: A <- A - value - carry. */
    SBC_A,
    /** Logical AND: A <- A & value. */
    AND,
    /** Logical XOR: A <- A ^ value. */
    XOR,
    /** Logical OR: A <- A | value. */
    OR,
    /** Compare: performs A - value to set flags, without storing the result in A. */
    CP;
    //endregion

    //region Utilities
    /**
     * Returns the ALU operation type for the given ordinal index.
     *
     * @param index the ordinal index (0-based) of the operation in this enumeration
     * @return the corresponding {@link AluOperationType}
     * @throws ArrayIndexOutOfBoundsException if {@code index} is outside the valid range
     */
    public static AluOperationType getAluOperationType(final int index) {
        return AluOperationType.values()[index];
    }
    //endregion

}
