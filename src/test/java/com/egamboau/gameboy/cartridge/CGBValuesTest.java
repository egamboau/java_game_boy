package com.egamboau.gameboy.cartridge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;



class CGBValuesTest {

    @Test
    @SuppressWarnings("magicnumber")
    void testFromByteValidCode() {
        assertEquals(CGBValues.CGB_ENHANCED, CGBValues.fromByte((byte) 0x80));
        assertEquals(CGBValues.CGB_ONLY, CGBValues.fromByte((byte) 0xC0));
    }

    @Test
    @SuppressWarnings("magicnumber")
    void testFromByteInvalidCode() {
        byte invalidCode = (byte) 0x00;
        Exception exception = assertThrows(IllegalArgumentException.class, () -> CGBValues.fromByte(invalidCode));
        assertEquals("Unknown CGB support code: 0", exception.getMessage());
    }

    @Test
    @SuppressWarnings("magicnumber")
    void testGetCode() {
        assertEquals((byte) 0x80, CGBValues.CGB_ENHANCED.getCode());
        assertEquals((byte) 0xC0, CGBValues.CGB_ONLY.getCode());
    }
}
