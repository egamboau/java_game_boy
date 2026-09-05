package com.egamboau.gameboy.memory;

/**
 * Utility container for commonly used bit masks in the emulator.
 * These masks are used to clamp values, detect carries/half-carries,
 * and extract specific bit fields.
 */
public final class BitMasks {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private BitMasks() {

    }

    /**
     * Mask to be used to shift a number 8 bits
     */
    public static final int MASK_8_BIT_SHIFT = 8;

    /**
     * A mask for 8-bit data, representing the maximum value of an 8-bit number (255).
     */
    public static final int MASK_8_BIT_DATA = 0XFF;

    /**
     * A mask for 16-bit data, representing the maximum value of a 16-bit number (65535).
     */
    public static final int MASK_16_BIT_DATA = 0xFFFF;

    /**
     * A mask used to identify the half-carry condition in a 16-bit operation.
     */
    public static final int HALF_CARRY_16_BIT_RESULT = 0x0FFF;

    /**
     * A mask used to identify the carry condition in a 16-bit operation.
     */
    public static final int CARRY_16_BIT_RESULTS = 0xFFFF;

    /**
     * A mask used to identify the half-carry condition in an 8-bit operation.
     */
    public static final int HALF_CARRY_8_BIT_RESULT = 0xF;

    /**
     * A mask used to identify the carry condition in an 8-bit operation.
     */
    public static final int CARRY_8_BIT_RESULTS = 0xFF;

    /**
     * Masks the carry from bit 3 when adding a signed 8-bit value to a 16-bit register.
     */
    public static final int SIGNED_8_BIT_HALF_CARRY = 0x10;

    /**
     * Masks the carry from bit 7 when adding a signed 8-bit value to a 16-bit register.
     */
    public static final int SIGNED_8_BIT_CARRY = 0x100;

    /**
     * A mask used to identify the half-carry condition in an 8-bit decrement operation.
     */
    public static final int HALF_CARRY_8_BIT_RESULT_DECREMENT = 0x0F;

    /**
     * A mask used to isolate the flag bytes in a specific operation.
     */
    public static final int FLAGS_BYTES_ONLY = 0xF0;

    /**
     * A mask used to identify the carry flag in an 8 bit number to be set on a rotate left application.
     */
    public static final int CARRY_FLAG_FOR_ROTATE_LEFT = 0x80;

    /**
     * A mask used to identify the carry result in an 8-bit number after a rotate left operation.
     */
    public static final int CARRY_RESULT_ROTATE_LEFT = 0x100;

    /**
     * A mask used to identify the carry flag in an 8-bit number to be set on a rotate right application.
     */
    public static final int CARRY_FLAG_FOR_ROTATE_RIGHT = 0x80;

    /**
     * A mask used to extract the instruction type bits from an opcode.
     */
    public static final int OPCODE_INSTRUCTION_TYPE_BITS = 0xC0;

    public static final int LOAD_ADDRESS_OFFSET = 0xFF00;

    public static final int FIRST_5_BYTES = 0x1F;
}
