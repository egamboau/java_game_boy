package com.egamboau.gameboy.cartridge;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RamTypeTest {

    @Test
    void testFromByteWithValidValues() {
        // Test valid byte values for each RAM type
        assertEquals(RamType.NO_RAM, RamType.fromByte((byte) 0x00));
        assertEquals(RamType.UNUSED, RamType.fromByte((byte) 0x01));
        assertEquals(RamType.SIZE_8KB, RamType.fromByte((byte) 0x02));
        assertEquals(RamType.SIZE_32KB, RamType.fromByte((byte) 0x03));
        assertEquals(RamType.SIZE_128KB, RamType.fromByte((byte) 0x04));
        assertEquals(RamType.SIZE_64KB, RamType.fromByte((byte) 0x05));
    }

    @Test
    void testFromByteWithInvalidValue() {
        // Test invalid byte value to ensure exception is thrown
        byte invalidValue = (byte) 0x10;
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            RamType.fromByte(invalidValue);
        });
        assertEquals("Unknown RAM size code: 16", exception.getMessage());
    }
}