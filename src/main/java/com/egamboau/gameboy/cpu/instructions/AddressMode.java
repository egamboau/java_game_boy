package com.egamboau.gameboy.cpu.instructions;

/**
 * Enum representing the various addressing modes used in Game Boy CPU instructions.
 * Addressing modes define how operands are accessed or manipulated during instruction execution.
 */
public enum AddressMode {

    /**
     * Addressing mode where 16-bit data is loaded into a register.
     */
    DATA_16_BITS_TO_REGISTER,

    /**
     * Addressing mode where 8-bit data is loaded into a register.
     */
    DATA_8_BIT_TO_REGISTER,

    /**
     * Addressing mode where a register's value is stored in memory at an address
     * pointed to by another register, and the address is incremented after the operation.
     */
    REGISTER_TO_INCREMENT_16_BIT_MEMORY_ADDRESS,

    /**
     * Addressing mode where data is transferred between a memory address and a register.
     */
    MEMORY_ADDRESS_REGISTER_TO_REGISTER,

    /**
     * Addressing mode where operations are performed on an 8-bit register.
     */
    REGISTER_8_BIT,

    /**
     * Addressing mode where data is transferred from a register to a specific memory address.
     */
    REGISTER_TO_MEMORY_ADDRESS_DATA,

    /**
     * Addressing mode where data is transferred from a register to an address pointed to by another register.
     */
    REGISTER_TO_INDIRECT_REGISTER,

    /**
     * Addressing mode where operations are performed on a 16-bit register.
     */
    REGISTER_16_BIT,

    /**
     * Addressing mode where data is transferred between two registers.
     */
    REGISTER_TO_REGISTER,

    /**
     * Addressing mode where data is transferred between two 16-bit registers.
     */
    REGISTER_16_BIT_TO_REGISTER_16_BIT,

    /**
     * Addressing mode where data is transferred from a memory address pointed to by a register,
     * and the address is incremented after the operation.
     */
    INCREMENT_16_BIT_MEMORY_ADDRESS_REGISTER_TO_REGISTER,

    /**
     * Addressing mode where data is transferred from a memory address pointed to by a register,
     * and the address is decremented after the operation.
     */
    DECREMENT_16_BIT_MEMORY_ADDRESS_REGISTER_TO_REGISTER,

    /**
     * Addressing mode where data is transferred from a register to a memory address pointed to by another register,
     * and the address is decremented after the operation.
     */
    REGISTER_TO_DECREMENT_16_BIT_MEMORY_ADDRESS,

    /**
     * Addressing mode where 8-bit data is loaded into a memory address pointed to by a register.
     */
    DATA_8_BIT_TO_MEMORY_ADDRESS_REGISTER,

    /**
     * Addressing mode where a memory address is specified by a register.
     */
    MEMORY_ADDRESS_REGISTER,
}
