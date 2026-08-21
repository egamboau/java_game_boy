package com.egamboau.gameboy.cartridge;

public enum Destination {

    /**
     * Represents the destination as Japan.
     */
    JAPAN((byte) 0x00),

    /**
     * Represents the destination as Overseas.
     */
    OVERSEAS((byte) 0x01);

    /**
     * The code representing the destination.
     */
    private final byte code;

    /**
     * A map to associate destination codes with their corresponding enum values.
     */


    Destination(final byte currentCode) {
        this.code = currentCode;
    }

    /**
     * Retrieves the Destination enum corresponding to the given byte code.
     *
     * @param code the byte code representing a destination
     * @return the Destination enum associated with the given code
     * @throws IllegalArgumentException if the code does not match any Destination
     */
    public static Destination fromByte(final byte code) {
        for (Destination destination : values()) {
            if (destination.code == code) {
                return destination;
            }
        }
        throw new IllegalArgumentException("Unknown RAM size code: " + code);
    }
}
