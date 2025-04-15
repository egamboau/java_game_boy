package com.egamboau.gameboy.cartridge;

import java.util.HashMap;
import java.util.Map;

public enum CGBValues {

    CGB_ENHANCED((byte) 0x80, "The game supports CGB enhancements, but is backwards compatible with monochrome Game Boys"),
    CGB_ONLY((byte) 0xC0, "The game works on CGB only (the hardware ignores bit 6, so this really functions the same as $80)");

    private final byte code;
    private final String meaning;

    private static final Map<Byte, CGBValues> BY_CODE = new HashMap<>();

    static {
        for (CGBValues support : values()) {
            BY_CODE.put(support.code, support);
        }
    }

    CGBValues(byte code, String meaning) {
        this.code = code;
        this.meaning = meaning;
    }

    public byte getCode() {
        return code;
    }

    public String getMeaning() {
        return meaning;
    }

    public static CGBValues fromByte(byte code) {
        CGBValues support = BY_CODE.get(code);
        if (support == null) {
            throw new IllegalArgumentException("Unknown CGB support code: " + code);
        }
        return support;
    }
}