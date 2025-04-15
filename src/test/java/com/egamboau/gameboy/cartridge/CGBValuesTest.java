package com.egamboau.gameboy.cartridge;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CGBValuesTest {

    @Test
    void testFromByte_ValidCode() {
        assertEquals(CGBValues.CGB_ENHANCED, CGBValues.fromByte((byte) 0x80));
        assertEquals(CGBValues.CGB_ONLY, CGBValues.fromByte((byte) 0xC0));
    }

    @Test
    void testFromByte_InvalidCode() {
        byte invalidCode = (byte) 0x00;
        Exception exception = assertThrows(IllegalArgumentException.class, () -> CGBValues.fromByte(invalidCode));
        assertEquals("Unknown CGB support code: 0", exception.getMessage());
    }

    @Test
    void testGetCode() {
        assertEquals((byte) 0x80, CGBValues.CGB_ENHANCED.getCode());
        assertEquals((byte) 0xC0, CGBValues.CGB_ONLY.getCode());
    }

    @Test
    void testGetMeaning() {
        assertEquals("The game supports CGB enhancements, but is backwards compatible with monochrome Game Boys", CGBValues.CGB_ENHANCED.getMeaning());
        assertEquals("The game works on CGB only (the hardware ignores bit 6, so this really functions the same as $80)", CGBValues.CGB_ONLY.getMeaning());
    }
}