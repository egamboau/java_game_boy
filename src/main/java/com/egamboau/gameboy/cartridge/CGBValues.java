package com.egamboau.gameboy.cartridge;

public enum CGBValues {

    /**
     * The game supports CGB enhancements, but is backwards compatible.
     */
    CGB_ENHANCED((byte) 0x80),

    /**
     * The game works on CGB only.
     */
    CGB_ONLY((byte) 0xC0);

    /**
     * The code representing the CGB support type.
     */
    private final byte code;

    /**
     * A mapping of CGB support codes to their corresponding CGBValues enum.
     */


    CGBValues(final byte currentCode) {
        this.code = currentCode;
    }

    /**
     * Gets the code representing the CGB support type.
     *
     * @return the CGB support type code as a byte.
     */
    public byte getCode() {
        return code;
    }

    /**
     * Retrieves the CGBValues enum corresponding to the given code.
     *
     * @param code the byte code representing the CGB support type.
     * @return the CGBValues enum corresponding to the given code.
     * @throws IllegalArgumentException if the code does not match any CGBValues.
     */
    public static CGBValues fromByte(final byte code) {
        for (CGBValues support : values()) {
            if (support.code == code) {
                return support;
            }
        }
        throw new IllegalArgumentException("Unknown CGB support code: " + code);
    }
}
