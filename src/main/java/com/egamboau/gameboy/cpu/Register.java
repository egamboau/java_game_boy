package com.egamboau.gameboy.cpu;

import com.egamboau.gameboy.memory.BitMasks;

/**
 * The {@code Register} class represents an 8-bit register for the gameboy CPU.
 * It provides methods to get, set, and manipulate the value of the register, as well as
 * utility methods for combining and splitting values between registers.
 */
public class Register {

    /**
     * The value stored in the register as a signed byte.
     */
    private byte value;

    /**
     * Creates a new register initialized to 0.
     */
    public Register() {
        this.value = 0;
    }

    /**
     * Creates a new register initialized to the specified value.
     *
     * @param newValue The initial value of the register (0-255).
     */
    public Register(final int newValue) {
        set(newValue);
    }

    /**
     * Retrieves the value of the register as an unsigned 8-bit integer.
     *
     * @return The value of the register (0-255).
     */
    public int get() {
        return value & BitMasks.MASK_8_BIT_DATA;
    }

    /**
     * Sets the value of the register to the specified unsigned 8-bit integer.
     *
     * @param newValue The value to set (0-255).
     */
    public void set(final int newValue) {
        this.value = (byte) (newValue & BitMasks.MASK_8_BIT_DATA);
    }

    /**
     * Retrieves the raw signed byte value stored in the register.
     *
     * @return The raw signed byte value (-128 to 127).
     */
    public byte raw() {
        return value;
    }

    /**
     * Combines the values of two 8-bit registers into a single 16-bit integer.
     *
     * @param high The high byte register.
     * @param low  The low byte register.
     * @return A 16-bit integer combining the high and low register values.
     */
    @SuppressWarnings("checkstyle:magicnumber")
    public static int combine(final Register high, final Register low) {
        return (high.get() << 8) | low.get();
    }

    /**
     * Splits a 16-bit integer into two 8-bit values and stores them in the
     * specified registers.
     *
     * @param value The 16-bit integer to split.
     * @param high  The register to store the high byte.
     * @param low   The register to store the low byte.
     */
    @SuppressWarnings("checkstyle:magicnumber")
    public static void split(final int value, final Register high, final Register low) {
        high.set((value >> 8) & BitMasks.MASK_8_BIT_DATA);
        low.set(value & BitMasks.MASK_8_BIT_DATA);
    }

    /**
     * Increments the value of the register by 1.
     */
    public final void incrementValue() {
        set(get() + 1);
    }

    /**
     * Decrements the value of the register by 1.
     */
    public final void decrementValue() {
        set(get() - 1);
    }
}
