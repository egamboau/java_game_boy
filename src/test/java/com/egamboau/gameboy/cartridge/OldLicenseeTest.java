package com.egamboau.gameboy.cartridge;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OldLicenseeTest {

    @Test
    void testFromByte_ValidCode() {
        assertEquals(OldLicensee.NINTENDO, OldLicensee.fromByte((byte) 0x01));
        assertEquals(OldLicensee.CAPCOM, OldLicensee.fromByte((byte) 0x08));
        assertEquals(OldLicensee.KONAMI, OldLicensee.fromByte((byte) 0x34));
        assertEquals(OldLicensee.LJN_AGAIN, OldLicensee.fromByte((byte) 0xFF));
    }

    @Test
    void testFromByte_InvalidCode() {
        byte invalidCode = (byte) 0x7F;
        Exception exception = assertThrows(IllegalArgumentException.class, () -> OldLicensee.fromByte(invalidCode));
        assertEquals("Unknown licensee code: 127", exception.getMessage());
    }

    @Test
    void testGetCode() {
        assertEquals((byte) 0x01, OldLicensee.NINTENDO.getCode());
        assertEquals((byte) 0x08, OldLicensee.CAPCOM.getCode());
    }

    @Test
    void testGetLicensee() {
        assertEquals("Nintendo", OldLicensee.NINTENDO.getLicensee());
        assertEquals("Capcom", OldLicensee.CAPCOM.getLicensee());
    }
}