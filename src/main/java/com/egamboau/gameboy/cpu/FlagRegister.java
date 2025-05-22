package com.egamboau.gameboy.cpu;

import com.egamboau.gameboy.memory.BitMasks;

/**
 * The FlagRegister class represents a specialized register that manages the CPU
 * flags for a Game Boy emulator. It extends the base Register class and provides
 * methods to manipulate and query the individual flags:
 * <ul>
 *   <li><b>Zero (Z)</b>: Indicates if the result of an operation is zero.</li>
 *   <li><b>Subtract (N)</b>: Indicates if the last operation was a subtraction.</li>
 *   <li><b>Half-Carry (H)</b>: Indicates if there was a carry from the lower nibble in the last operation.</li>
 *   <li><b>Carry (C)</b>: Indicates if there was a carry from the most significant bit in the last operation.</li>
 * </ul>
 *
 * The flags are stored in the upper 4 bits of the register, and the lower 4 bits are ignored.
 */
public class FlagRegister extends Register {

    /**
     * Bitmask for the Zero flag (Z).
     */
    @SuppressWarnings("checkstyle:magicnumber")
    private static final int ZERO_FLAG = 1 << 7;

    /**
     * Bitmask for the Subtract flag (N).
     */
    @SuppressWarnings("checkstyle:magicnumber")
    private static final int SUBTRACT_FLAG = 1 << 6;

    /**
     * Bitmask for the Half-Carry flag (H).
     */
    @SuppressWarnings("checkstyle:magicnumber")
    private static final int HALF_CARRY_FLAG = 1 << 5;

    /**
     * Bitmask for the Carry flag (C).
     */
    @SuppressWarnings("checkstyle:magicnumber")
    private static final int CARRY_FLAG = 1 << 4;

    /**
     * Retrieves the value of the flag register, masking out the lower 4 bits.
     *
     * @return The value of the flag register with only the upper 4 bits preserved.
     */
    @Override
    public final int get() {
        return super.get() & BitMasks.FLAGS_BYTES_ONLY;
    }

    /**
     * Constructs a new instance of the FlagRegister class with an initial value of 0.
     */
    public FlagRegister() {
        super();
    }

    /**
     * Constructs a new FlagRegister with the specified initial value.
     *
     * @param value The initial value of the flag register.
     */
    public FlagRegister(final int value) {
        super(value);
    }

    /**
     * Checks if the Zero flag (Z) is set in the flag register.
     *
     * @return {@code true} if the Zero flag is set, {@code false} otherwise.
     */
    public boolean getZero() {
        return isSet(ZERO_FLAG);
    }

    /**
     * Checks if the Subtract flag (N) is set in the flag register.
     *
     * @return {@code true} if the Subtract flag is set, {@code false} otherwise.
     */
    public boolean getSubtract() {
        return isSet(SUBTRACT_FLAG);
    }

    /**
     * Retrieves the state of the Half-Carry flag (H).
     * The Half-Carry flag indicates whether a carry occurred from the lower nibble (4 bits) during an arithmetic operation.
     *
     * @return {@code true} if the Half-Carry flag is set, {@code false} otherwise.
     */
    public boolean getHalfCarry() {
        return isSet(HALF_CARRY_FLAG);
    }

    /**
     * Retrieves the state of the Carry flag (C).
     *
     * @return {@code true} if the Carry flag is set, {@code false} otherwise.
     */
    public boolean getCarry() {
        return isSet(CARRY_FLAG);
    }

    /**
     * Sets the Zero flag (Z) in the flag register.
     * The Zero flag is used to indicate whether the result of the last operation was zero.
     *
     * @param value {@code true} to set the Zero flag, {@code false} to clear it.
     */
    public void setZero(final boolean value) {
        setFlag(ZERO_FLAG, value);
    }

    /**
     * Sets the Subtract flag (N) in the flag register.
     *
     * @param value {@code true} to set the Subtract flag, {@code false} to clear it.
     */
    public void setSubtract(final boolean value) {
        setFlag(SUBTRACT_FLAG, value);
    }

    /**
     * Sets the Half-Carry flag (H) in the flag register.
     *
     * @param value {@code true} to set the Half-Carry flag, {@code false} to clear it.
     */
    public void setHalfCarry(final boolean value) {
        setFlag(HALF_CARRY_FLAG, value);
    }

    /**
     * Sets the Carry flag (C) in the flag register.
     *
     * @param value {@code true} to set the Carry flag, {@code false} to clear it.
     */
    public void setCarry(final boolean value) {
        setFlag(CARRY_FLAG, value);
    }

    /**
     * Checks if a specific flag is set in the flag register.
     *
     * @param mask The bitmask representing the flag to check.
     * @return {@code true} if the flag represented by the mask is set, {@code false} otherwise.
     */
    private boolean isSet(final int mask) {
        return (get() & mask) != 0;
    }

    /**
     * Sets or clears a specific flag in the flag register.
     *
     * @param mask    The bitmask representing the flag to be modified.
     * @param enabled {@code true} to set the flag (turn the bit on), {@code false} to clear the flag (turn the bit off).
     *
     * Note: Only the upper 4 bits of the flag register are used and preserved.
     */
    private void setFlag(final int mask, final boolean enabled) {
        int f = get();
        if (enabled) {
            f |= mask;
        } else {
            f &= ~mask;
        }
        set(f & BitMasks.FLAGS_BYTES_ONLY);
    }
}
