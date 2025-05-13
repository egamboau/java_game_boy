package com.egamboau.gameboy.cartridge;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum representing the different types of ROMs used in Game Boy cartridges.
 * Each ROM type is associated with a unique byte value.
 */
public enum Roms {

    /**
     * ROM without any memory bank controller (MBC).
     */
    ROM_ONLY((byte) 0x00),

    /**
     * ROM with MBC1.
     */
    MBC1((byte) 0x01),

    /**
     * ROM with MBC1 and RAM.
     */
    MBC1_RAM((byte) 0x02),

    /**
     * ROM with MBC1, RAM, and a battery.
     */
    MBC1_RAM_BATTERY((byte) 0x03),

    /**
     * ROM with MBC2.
     */
    MBC2((byte) 0x05),

    /**
     * ROM with MBC2 and a battery.
     */
    MBC2_BATTERY((byte) 0x06),

    /**
     * ROM with additional RAM.
     */
    ROM_RAM((byte) 0x08),

    /**
     * ROM with additional RAM and a battery.
     */
    ROM_RAM_BATTERY((byte) 0x09),

    /**
     * ROM with MMM01 memory bank controller.
     */
    MMM01((byte) 0x0B),

    /**
     * ROM with MMM01 and RAM.
     */
    MMM01_RAM((byte) 0x0C),

    /**
     * ROM with MMM01, RAM, and a battery.
     */
    MMM01_RAM_BATTERY((byte) 0x0D),

    /**
     * ROM with MBC3 and a timer with a battery.
     */
    MBC3_TIMER_BATTERY((byte) 0x0F),

    /**
     * ROM with MBC3, a timer, RAM, and a battery.
     */
    MBC3_TIMER_RAM_BATTERY((byte) 0x10),

    /**
     * ROM with MBC3.
     */
    MBC3((byte) 0x11),

    /**
     * ROM with MBC3 and RAM.
     */
    MBC3_RAM((byte) 0x12),

    /**
     * ROM with MBC3, RAM, and a battery.
     */
    MBC3_RAM_BATTERY((byte) 0x13),

    /**
     * ROM with MBC5.
     */
    MBC5((byte) 0x19),

    /**
     * ROM with MBC5 and RAM.
     */
    MBC5_RAM((byte) 0x1A),

    /**
     * ROM with MBC5, RAM, and a battery.
     */
    MBC5_RAM_BATTERY((byte) 0x1B),

    /**
     * ROM with MBC5 and rumble support.
     */
    MBC5_RUMBLE((byte) 0x1C),

    /**
     * ROM with MBC5, rumble support, and RAM.
     */
    MBC5_RUMBLE_RAM((byte) 0x1D),

    /**
     * ROM with MBC5, rumble support, RAM, and a battery.
     */
    MBC5_RUMBLE_RAM_BATTERY((byte) 0x1E),

    /**
     * ROM with MBC6.
     */
    MBC6((byte) 0x20),

    /**
     * ROM with MBC7, sensor, rumble support, RAM, and a battery.
     */
    MBC7_SENSOR_RUMBLE_RAM_BATTERY((byte) 0x22),

    /**
     * ROM for the Pocket Camera accessory.
     */
    POCKET_CAMERA((byte) 0xFC),

    /**
     * ROM for the Bandai TAMA5 accessory.
     */
    BANDAI_TAMA5((byte) 0xFD),

    /**
     * ROM with HuC3 memory bank controller.
     */
    HuC3((byte) 0xFE),

    /**
     * ROM with HuC1 memory bank controller, RAM, and a battery.
     */
    HuC1_RAM_BATTERY((byte) 0xFF),

    /**
     * Unknown ROM type.
     */
    UNKNOWN((byte) 0x4);

    /**
     * The byte value associated with the ROM type.
     */
    private final byte byteValue;

    /**
     * Retrieves the byte value associated with the ROM type.
     *
     * @return The byte value of the ROM type.
     */
    public byte getByteValue() {
        return byteValue;
    }

    /**
     * A map for quick lookup of ROM types by their byte value.
     */
    private static final Map<Byte, Roms> BY_CODE = new HashMap<>();

    static {
        for (Roms rom : values()) {
            BY_CODE.put(rom.byteValue, rom);
        }
    }

    /**
     * Constructor for the enum.
     *
     * @param value The byte value associated with the ROM type.
     */
    Roms(final byte value) {
        this.byteValue = value;
    }

    /**
     * Retrieves the ROM type corresponding to the given byte value.
     *
     * @param value The byte value of the ROM type.
     * @return The corresponding {@link Roms} enum value.
     *         If the byte value does not match any known ROM type,
     *         {@link Roms#UNKNOWN} is returned.
     */
    public static Roms fromByte(final byte value) {
        Roms rom = BY_CODE.get(value);
        if (rom == null) {
            return UNKNOWN;
        }
        return rom;
    }
}
