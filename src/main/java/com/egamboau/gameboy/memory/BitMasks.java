package com.egamboau.gameboy.memory;

/**
 * Utility container for commonly used bit masks in the emulator.
 * These masks are used to clamp values, detect carries/half-carries,
 * and extract specific bit fields.
 */
public final class BitMasks {

    //region Construction
    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private BitMasks() {

    }
    //endregion

    //region Common Masks (8-bit / 16-bit)
    /**
     * A mask for 8-bit data, representing the maximum value of an 8-bit number (255).
     */
    public static final int MASK_8_BIT_DATA = 0XFF;

    /**
     * A mask for 16-bit data, representing the maximum value of a 16-bit number (65535).
     */
    public static final int MASK_16_BIT_DATA = 0xFFFF;
    //endregion

    //region Carry / Half-carry Masks
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
     * A mask used to identify the half-carry condition in an 8-bit decrement operation.
     */
    public static final int HALF_CARRY_8_BIT_RESULT_DECREMENT = 0x0F;
    //endregion

    //region Flag / Rotate / Opcode Masks
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
    //endregion
}
