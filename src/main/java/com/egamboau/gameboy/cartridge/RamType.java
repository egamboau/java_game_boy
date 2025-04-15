package com.egamboau.gameboy.cartridge;

import java.util.HashMap;
import java.util.Map;

public enum RamType {

    NO_RAM((byte) 0x00, "No RAM", 0),
    UNUSED((byte) 0x01, "Unused", 0),
    SIZE_8KB((byte) 0x02, "8 KiB", 1),
    SIZE_32KB((byte) 0x03, "32 KiB", 4),
    SIZE_128KB((byte) 0x04, "128 KiB", 16),
    SIZE_64KB((byte) 0x05, "64 KiB", 8);

    private final byte code;
    private final String ramSize;
    private final int numberOfBanks;

    private static final Map<Byte, RamType> BY_CODE = new HashMap<>();

    static {
        for (RamType size : values()) {
            BY_CODE.put(size.code, size);
        }
    }

    RamType(byte code, String ramSize, int numberOfBanks) {
        this.code = code;
        this.ramSize = ramSize;
        this.numberOfBanks = numberOfBanks;
    }

    public byte getCode() {
        return code;
    }

    public String getRamSize() {
        return ramSize;
    }

    public int getNumberOfBanks() {
        return numberOfBanks;
    }

    public static RamType fromByte(byte code) {
        RamType size = BY_CODE.get(code);
        if (size == null) {
            throw new IllegalArgumentException("Unknown RAM size code: " + code);
        }
        return size;
    }
}