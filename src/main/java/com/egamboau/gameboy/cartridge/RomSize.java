package com.egamboau.gameboy.cartridge;

import java.util.HashMap;
import java.util.Map;

public enum RomSize {

    /**
     * Represents a ROM size of 32 KiB with 2 banks.
     */
    SIZE_32KB((byte) 0x00, "32 KiB", 2),

    /**
     * Represents a ROM size of 64 KiB with 4 banks.
     */
    SIZE_64KB((byte) 0x01, "64 KiB", 4),
    /**
     * Represents a ROM size of 128 KiB with 8 banks.
     */
    SIZE_128KB((byte) 0x02, "128 KiB", 8),

    /**
     * Represents a ROM size of 256 KiB with 16 banks.
     */
    SIZE_256KB((byte) 0x03, "256 KiB", 16),
    /**
     * Represents a ROM size of 512 KiB with 32 banks.
     */
    SIZE_512KB((byte) 0x04, "512 KiB", 32),

    /**
     * Represents a ROM size of 1 MiB with 64 banks.
     */
    SIZE_1MB((byte) 0x05, "1 MiB", 64),
    /**
     * Represents a ROM size of 2 MiB with 128 banks.
     */
    SIZE_2MB((byte) 0x06, "2 MiB", 128),

    /**
     * Represents a ROM size of 4 MiB with 256 banks.
     */
    SIZE_4MB((byte) 0x07, "4 MiB", 256),
    /**
     * Represents a ROM size of 8 MiB with 512 banks.
     */
    SIZE_8MB((byte) 0x08, "8 MiB", 512),

    /**
     * Represents a ROM size of 1.1 MiB with 72 banks.
     */
    SIZE_1_1MB((byte) 0x52, "1.1 MiB", 72),
    /**
     * Represents a ROM size of 1.2 MiB with 80 banks.
     */
    SIZE_1_2MB((byte) 0x53, "1.2 MiB", 80),

    /**
     * Represents a ROM size of 1.5 MiB with 96 banks.
     */
    SIZE_1_5MB((byte) 0x54, "1.5 MiB", 96);

    /**
     * The unique code representing the ROM size.
     */
    private final byte code;

    /**
     * The human-readable representation of the ROM size.
     */
    private final String romSize;
    /**
     * The number of banks available in the ROM size.
     */
    private final int numberOfBanks;

    /**
     * A mapping of ROM size codes to their corresponding {@link RomSize} instances.
     */
    private static final Map<Byte, RomSize> BY_CODE = new HashMap<>();

    static {
        for (RomSize size : values()) {
            BY_CODE.put(size.code, size);
        }
    }

    RomSize(final byte codeValue, final String romSizeValue, final int numberOfBanksValue) {
        this.code = codeValue;
        this.romSize = romSizeValue;
        this.numberOfBanks = numberOfBanksValue;
    }

    /**
     * Gets the unique code representing the ROM size.
     *
     * @return the ROM size code as a byte.
     */
    public byte getCode() {
        return code;
    }

    /**
     * Gets the human-readable representation of the ROM size.
     *
     * @return the ROM size as a string.
     */
    public String getRomSize() {
        return romSize;
    }

    /**
     * Gets the number of banks available in the ROM size.
     *
     * @return the number of banks as an integer.
     */
    public int getNumberOfBanks() {
        return numberOfBanks;
    }

    /**
     * Retrieves the {@link RomSize} corresponding to the given code.
     *
     * @param code the byte code representing the ROM size.
     * @return the corresponding {@link RomSize} instance.
     * @throws IllegalArgumentException if the code does not match any ROM size.
     */
    public static RomSize fromByte(final byte code) {
        RomSize size = BY_CODE.get(code);
        if (size == null) {
            throw new IllegalArgumentException("Unknown ROM size code: " + code);
        }
        return size;
    }
}
