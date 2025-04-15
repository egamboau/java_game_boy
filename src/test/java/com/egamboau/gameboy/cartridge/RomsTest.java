package com.egamboau.gameboy.cartridge;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RomsTest {

    @Test
    void testFromByteWithValidValues() {
        /**
         * Test valid byte values for each cartridge type
         */
        assertEquals(Roms.ROM_ONLY, Roms.fromByte((byte) 0x00));
        assertEquals(Roms.MBC1, Roms.fromByte((byte) 0x01));
        assertEquals(Roms.MBC1_RAM, Roms.fromByte((byte) 0x02));
        assertEquals(Roms.MBC1_RAM_BATTERY, Roms.fromByte((byte) 0x03));
        assertEquals(Roms.MBC2, Roms.fromByte((byte) 0x05));
        assertEquals(Roms.MBC2_BATTERY, Roms.fromByte((byte) 0x06));
        assertEquals(Roms.ROM_RAM, Roms.fromByte((byte) 0x08));
        assertEquals(Roms.ROM_RAM_BATTERY, Roms.fromByte((byte) 0x09));
        assertEquals(Roms.MMM01, Roms.fromByte((byte) 0x0B));
        assertEquals(Roms.MMM01_RAM, Roms.fromByte((byte) 0x0C));
        assertEquals(Roms.MMM01_RAM_BATTERY, Roms.fromByte((byte) 0x0D));
        assertEquals(Roms.MBC3_TIMER_BATTERY, Roms.fromByte((byte) 0x0F));
        assertEquals(Roms.MBC3_TIMER_RAM_BATTERY, Roms.fromByte((byte) 0x10));
        assertEquals(Roms.MBC3, Roms.fromByte((byte) 0x11));
        assertEquals(Roms.MBC3_RAM, Roms.fromByte((byte) 0x12));
        assertEquals(Roms.MBC3_RAM_BATTERY, Roms.fromByte((byte) 0x13));
        assertEquals(Roms.MBC5, Roms.fromByte((byte) 0x19));
        assertEquals(Roms.MBC5_RAM, Roms.fromByte((byte) 0x1A));
        assertEquals(Roms.MBC5_RAM_BATTERY, Roms.fromByte((byte) 0x1B));
        assertEquals(Roms.MBC5_RUMBLE, Roms.fromByte((byte) 0x1C));
        assertEquals(Roms.MBC5_RUMBLE_RAM, Roms.fromByte((byte) 0x1D));
        assertEquals(Roms.MBC5_RUMBLE_RAM_BATTERY, Roms.fromByte((byte) 0x1E));
        assertEquals(Roms.MBC6, Roms.fromByte((byte) 0x20));
        assertEquals(Roms.MBC7_SENSOR_RUMBLE_RAM_BATTERY, Roms.fromByte((byte) 0x22));
        assertEquals(Roms.POCKET_CAMERA, Roms.fromByte((byte) 0xFC));
        assertEquals(Roms.BANDAI_TAMA5, Roms.fromByte((byte) 0xFD));
        assertEquals(Roms.HuC3, Roms.fromByte((byte) 0xFE));
        assertEquals(Roms.HuC1_RAM_BATTERY, Roms.fromByte((byte) 0xFF));
    }

    @Test
    void testFromByteWithInvalidValue() {
        /**
         * Test if an invalid byte return an Unknown Value
         */
        byte invalidValue = (byte) 0x04;
        Roms value = Roms.fromByte(invalidValue);
        assertEquals(Roms.UNKNOWN, value);
    }
}