package com.egamboau.gameboy.cartridge;

import java.util.HashMap;
import java.util.Map;

public enum RomSize {

    SIZE_32KB((byte) 0x00, "32 KiB", 2),
    SIZE_64KB((byte) 0x01, "64 KiB", 4),
    SIZE_128KB((byte) 0x02, "128 KiB", 8),
    SIZE_256KB((byte) 0x03, "256 KiB", 16),
    SIZE_512KB((byte) 0x04, "512 KiB", 32),
    SIZE_1MB((byte) 0x05, "1 MiB", 64),
    SIZE_2MB((byte) 0x06, "2 MiB", 128),
    SIZE_4MB((byte) 0x07, "4 MiB", 256),
    SIZE_8MB((byte) 0x08, "8 MiB", 512),
    SIZE_1_1MB((byte) 0x52, "1.1 MiB", 72),
    SIZE_1_2MB((byte) 0x53, "1.2 MiB", 80),
    SIZE_1_5MB((byte) 0x54, "1.5 MiB", 96);

    private final byte code;
    private final String romSize;
    private final int numberOfBanks;

    private static final Map<Byte, RomSize> BY_CODE = new HashMap<>();

    static {
        for (RomSize size : values()) {
            BY_CODE.put(size.code, size);
        }
    }

    RomSize(byte code, String romSize, int numberOfBanks) {
        this.code = code;
        this.romSize = romSize;
        this.numberOfBanks = numberOfBanks;
    }

    public byte getCode() {
        return code;
    }

    public String getRomSize() {
        return romSize;
    }

    public int getNumberOfBanks() {
        return numberOfBanks;
    }

    public static RomSize fromByte(byte code) {
        RomSize size = BY_CODE.get(code);
        if (size == null) {
            throw new IllegalArgumentException("Unknown ROM size code: " + code);
        }
        return size;
    }
}