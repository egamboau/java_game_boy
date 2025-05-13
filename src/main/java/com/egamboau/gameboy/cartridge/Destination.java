package com.egamboau.gameboy.cartridge;

import java.util.HashMap;
import java.util.Map;

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
    private static final Map<Byte, Destination> BY_CODE = new HashMap<>();

    static {
        for (Destination size : values()) {
            BY_CODE.put(size.code, size);
        }
    }

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
        Destination size = BY_CODE.get(code);
        if (size == null) {
            throw new IllegalArgumentException("Unknown RAM size code: " + code);
        }
        return size;
    }
}
