package com.egamboau.gameboy.cartridge;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class RomSizeTest {

    @Test
    void testFromByteWithValidValues() {
        // Test valid byte values for each ROM size
        assertEquals(RomSize.SIZE_32KB, RomSize.fromByte((byte) 0x00));
        assertEquals(RomSize.SIZE_64KB, RomSize.fromByte((byte) 0x01));
        assertEquals(RomSize.SIZE_128KB, RomSize.fromByte((byte) 0x02));
        assertEquals(RomSize.SIZE_256KB, RomSize.fromByte((byte) 0x03));
        assertEquals(RomSize.SIZE_512KB, RomSize.fromByte((byte) 0x04));
        assertEquals(RomSize.SIZE_1MB, RomSize.fromByte((byte) 0x05));
        assertEquals(RomSize.SIZE_2MB, RomSize.fromByte((byte) 0x06));
        assertEquals(RomSize.SIZE_4MB, RomSize.fromByte((byte) 0x07));
        assertEquals(RomSize.SIZE_8MB, RomSize.fromByte((byte) 0x08));
        assertEquals(RomSize.SIZE_1_1MB, RomSize.fromByte((byte) 0x52));
        assertEquals(RomSize.SIZE_1_2MB, RomSize.fromByte((byte) 0x53));
        assertEquals(RomSize.SIZE_1_5MB, RomSize.fromByte((byte) 0x54));
    }

    @Test
    void testFromByteWithInvalidValue() {
        // Test invalid byte value to ensure exception is thrown
        byte invalidValue = (byte) 0x10;
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            RomSize.fromByte(invalidValue);
        });
        assertEquals("Unknown ROM size code: 16", exception.getMessage());
    }

    @Test
    void testGetCode() {
        // Test getCode() method
        assertEquals((byte) 0x00, RomSize.SIZE_32KB.getCode());
        assertEquals((byte) 0x01, RomSize.SIZE_64KB.getCode());
    }

    @Test
    void testGetRomSize() {
        // Test getRomSize() method
        assertEquals("32 KiB", RomSize.SIZE_32KB.getRomSize());
        assertEquals("64 KiB", RomSize.SIZE_64KB.getRomSize());
    }

    @Test
    void testGetNumberOfBanks() {
        // Test getNumberOfBanks() method
        assertEquals(2, RomSize.SIZE_32KB.getNumberOfBanks());
        assertEquals(4, RomSize.SIZE_64KB.getNumberOfBanks());
    }
}