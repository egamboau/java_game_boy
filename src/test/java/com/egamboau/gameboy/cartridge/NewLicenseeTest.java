package com.egamboau.gameboy.cartridge;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NewLicenseeTest {

    @Test
    void testFromCode_ValidCode() {
        assertEquals(NewLicensee.NINTENDO, NewLicensee.fromCode("31"));
        assertEquals(NewLicensee.CAPCOM, NewLicensee.fromCode("08"));
    }

    @Test
    void testFromCode_InvalidCode() {
        assertEquals(NewLicensee.UNKNOWN, NewLicensee.fromCode("ZZ"));
    }

    @Test
    void testGetCode() {
        assertEquals("31", NewLicensee.NINTENDO.getCode());
        assertEquals("08", NewLicensee.CAPCOM.getCode());
    }
}