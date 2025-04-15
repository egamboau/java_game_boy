package com.egamboau.gameboy.cartridge;

import java.util.HashMap;
import java.util.Map;

public enum Roms {

    ROM_ONLY((byte)0x00),
    MBC1((byte) 0x01),
    MBC1_RAM((byte) 0x02),
    MBC1_RAM_BATTERY((byte) 0x03),
    MBC2((byte) 0x05),
    MBC2_BATTERY((byte) 0x06),
    ROM_RAM((byte) 0x08),
    ROM_RAM_BATTERY((byte) 0x09),
    MMM01((byte) 0x0B),
    MMM01_RAM((byte) 0x0C),
    MMM01_RAM_BATTERY((byte) 0x0D),
    MBC3_TIMER_BATTERY((byte) 0x0F),
    MBC3_TIMER_RAM_BATTERY((byte) 0x10),
    MBC3((byte) 0x11),
    MBC3_RAM((byte) 0x12),
    MBC3_RAM_BATTERY((byte) 0x13),
    MBC5((byte) 0x19),
    MBC5_RAM((byte) 0x1A),
    MBC5_RAM_BATTERY((byte) 0x1B),
    MBC5_RUMBLE((byte) 0x1C),
    MBC5_RUMBLE_RAM((byte) 0x1D),
    MBC5_RUMBLE_RAM_BATTERY((byte) 0x1E),
    MBC6((byte) 0x20),
    MBC7_SENSOR_RUMBLE_RAM_BATTERY((byte) 0x22),
    POCKET_CAMERA((byte) 0xFC),
    BANDAI_TAMA5((byte) 0xFD),
    HuC3((byte) 0xFE),
    HuC1_RAM_BATTERY((byte) 0xFF),
    
    UNKNOWN((byte) 0x4);

    public final byte byteValue;

    private Roms(byte value) {
        this.byteValue = value;
    }

    private static final Map<Byte, Roms> BY_CODE = new HashMap<>();

    static {
        for (Roms rom : values()) {
            BY_CODE.put(rom.byteValue, rom);
        }
    }

    public static Roms fromByte(byte value) {
        Roms rom = BY_CODE.get(value);
        if (rom == null) {
            return UNKNOWN;
        }
        return rom;
    }
}
