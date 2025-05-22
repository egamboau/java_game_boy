package com.egamboau.gameboy.cartridge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class OldLicenseeTest {

    @Test
    @SuppressWarnings("magicnumber")
    void testFromByteValidCode() {
        assertEquals(OldLicensee.NINTENDO, OldLicensee.fromByte((byte) 0x01));
        assertEquals(OldLicensee.CAPCOM, OldLicensee.fromByte((byte) 0x08));
        assertEquals(OldLicensee.KONAMI, OldLicensee.fromByte((byte) 0x34));
        assertEquals(OldLicensee.LJN_AGAIN, OldLicensee.fromByte((byte) 0xFF));
    }

    @Test
    @SuppressWarnings("magicnumber")
    void testFromByteInvalidCode() {
        byte invalidCode = (byte) 0x7F;
        Exception exception = assertThrows(IllegalArgumentException.class, () -> OldLicensee.fromByte(invalidCode));
        assertEquals("Unknown licensee code: 127", exception.getMessage());
    }

    @Test
    @SuppressWarnings("magicnumber")
    void testGetCode() {
        assertEquals((byte) 0x01, OldLicensee.NINTENDO.getCode());
        assertEquals((byte) 0x08, OldLicensee.CAPCOM.getCode());
    }
}
