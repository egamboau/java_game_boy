package com.egamboau.gameboy.cartridge;

import java.util.HashMap;
import java.util.Map;

public enum RamType {

    /**
     * Represents no RAM available.
     */
    NO_RAM((byte) 0x00, "No RAM", 0),

    /**
     * Represents an unused RAM type.
     */
    UNUSED((byte) 0x01, "Unused", 0),
    /**
     * Represents 8 KiB of RAM.
     */
    SIZE_8KB((byte) 0x02, "8 KiB", 1),

    /**
     * Represents 32 KiB of RAM.
     */
    SIZE_32KB((byte) 0x03, "32 KiB", 4),
    /**
     * Represents 128 KiB of RAM.
     */
    SIZE_128KB((byte) 0x04, "128 KiB", 16),

    /**
     * Represents 64 KiB of RAM.
     */
    SIZE_64KB((byte) 0x05, "64 KiB", 8);

    /**
     * The code representing the RAM type.
     */
    private final byte code;

    /**
     * The size of the RAM as a human-readable string.
     */
    private final String ramSize;
    /**
     * The number of banks available in this RAM type.
     */
    private final int numberOfBanks;

    /**
     * A map to associate RAM type codes with their corresponding {@link RamType} instances.
     */
    private static final Map<Byte, RamType> BY_CODE = new HashMap<>();

    static {
        for (RamType size : values()) {
            BY_CODE.put(size.code, size);
        }
    }

    RamType(final byte ramCode, final String ramSizeDescription, final int ramBanks) {
        this.code = ramCode;
        this.ramSize = ramSizeDescription;
        this.numberOfBanks = ramBanks;
    }

    /**
     * Gets the code representing the RAM type.
     *
     * @return the RAM type code as a byte.
     */
    public byte getCode() {
        return code;
    }

    /**
     * Gets the size of the RAM as a human-readable string.
     *
     * @return the RAM size description.
     */
    public String getRamSize() {
        return ramSize;
    }

    /**
     * Gets the number of banks available in this RAM type.
     *
     * @return the number of RAM banks.
     */
    public int getNumberOfBanks() {
        return numberOfBanks;
    }

    /**
     * Retrieves the {@link RamType} corresponding to the given code.
     *
     * @param code the RAM type code as a byte.
     * @return the corresponding {@link RamType}.
     * @throws IllegalArgumentException if the code does not match any RAM type.
     */
    public static RamType fromByte(final byte code) {
        RamType size = BY_CODE.get(code);
        if (size == null) {
            throw new IllegalArgumentException("Unknown RAM size code: " + code);
        }
        return size;
    }
}
