package com.egamboau.gameboy.cartridge;

import java.util.HashMap;
import java.util.Map;

public enum Destination {

    JAPAN((byte) 0x00),
    OVERSEAS((byte) 0x01);

    private final byte code;

    private static final Map<Byte, Destination> BY_CODE = new HashMap<>();

    static {
        for (Destination size : values()) {
            BY_CODE.put(size.code, size);
        }
    }

    Destination(byte code) {
        this.code = code;
    }

    public static Destination fromByte(byte code) {
        Destination size = BY_CODE.get(code);
        if (size == null) {
            throw new IllegalArgumentException("Unknown RAM size code: " + code);
        }
        return size;
    }
}
