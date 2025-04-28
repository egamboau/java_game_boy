package com.egamboau.gameboy.cpu;

/**
 * The FlagRegister class represents a specialized register that manages the CPU
 * flags
 * for a Game Boy emulator. It extends the base Register class and provides
 * methods
 * to manipulate and query the individual flags:.
 * - Zero (Z): Indicates if the result of an operation is zero.
 * - Subtract (N): Indicates if the last operation was a subtraction.
 * - Half-Carry (H): Indicates if there was a carry from the lower nibble in the
 * last operation.
 * - Carry (C): Indicates if there was a carry from the most significant bit in
 * the last operation.
 * 
 * The flags are stored in the upper 4 bits of the register, and the lower 4
 * bits are ignored.
 */
public class FlagRegister extends Register {

    private static final int ZERO_FLAG = 1 << 7;
    private static final int SUBTRACT_FLAG = 1 << 6;
    private static final int HALF_CARRY_FLAG = 1 << 5;
    private static final int CARRY_FLAG = 1 << 4;
    
    @Override
    public int get() {
        return super.get() & 0xF0;
    }
    /**
     * Constructs a new instance of the FlagRegister class.
     */
    public FlagRegister() {
        super();
    }

    /**
     * Constructs a new FlagRegister with the specified initial value.
     *
     * @param value the initial value of the flag register.
     */
    public FlagRegister(int value) {
        super(value);
    }

    /**
     * Checks if the Zero flag is set in the flag register.
     *
     * @return {@code true} if the Zero flag is set, {@code false} otherwise.
     */
    public boolean getZero() {
        return isSet(ZERO_FLAG);
    }

    /**
     * Checks if the subtract flag is set in the flag register.
     *
     * @return {@code true} if the subtract flag is set, {@code false} otherwise.
     */
    public boolean getSubtract() {
        return isSet(SUBTRACT_FLAG);
    }

    /**
     * Retrieves the state of the Half Carry flag.
     * The Half Carry flag is used to indicate whether a carry occurred
     * from the lower nibble (4 bits) during an arithmetic operation.
     *
     * @return {@code true} if the Half Carry flag is set, {@code false} otherwise.
     */
    public boolean getHalfCarry() {
        return isSet(HALF_CARRY_FLAG);
    }

    /**
     * Retrieves the state of the Carry flag.
     *
     * @return {@code true} if the Carry flag is set, {@code false} otherwise.
     */
    public boolean getCarry() {
        return isSet(CARRY_FLAG);
    }

    /**
     * Sets the Zero flag in the flag register.
     * 
     * The Zero flag is used to indicate whether the result of the last operation
     * was zero.
     *
     * @param value true to set the Zero flag, false to clear it.
     */
    public void setZero(boolean value) {
        setFlag(ZERO_FLAG, value);
    }

    /**
     * Sets the state of the Subtract flag in the flag register.
     *
     * @param value true to set the Subtract flag, false to clear it.
     */
    public void setSubtract(boolean value) {
        setFlag(SUBTRACT_FLAG, value);
    }

    /**
     * Sets the Half Carry flag in the register.
     * @param value {@code true} to set the Half Carry flag, {@code false} to clear it.
     */
    public void setHalfCarry(boolean value) {
        setFlag(HALF_CARRY_FLAG, value);
    }

    /**
     * Sets the Carry flag in the flag register.
     *
     * @param value the boolean value to set the Carry flag to.
     */
    public void setCarry(boolean value) {
        setFlag(CARRY_FLAG, value);
    }

    /**
     * Checks if a specific flag is set in the register.
     *
     * @param mask The bitmask representing the flag to check.
     * @return {@code true} if the flag represented by the mask is set, {@code false} otherwise.
     */
    private boolean isSet(int mask) {
        return (get() & mask) != 0;
    }

    /**
     * Sets or clears a specific flag in the flag register.
     *
     * @param mask    The bitmask representing the flag to be modified.
     * @param enabled If true, the flag is set (bit is turned on); 
     *                if false, the flag is cleared (bit is turned off).
     * 
     * Note: Only the upper 4 bits of the flag register are used and preserved.
     */
    private void setFlag(int mask, boolean enabled) {
        int f = get();
        if (enabled) {
            f |= mask;
        } else {
            f &= ~mask;
        }
        set(f & 0xF0); // Only upper 4 bits should be used in F
    }
}
