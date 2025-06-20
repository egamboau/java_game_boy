package com.egamboau.gameboy.cartridge;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;


class NewLicenseeTest {

    @Test
    void testFromCodeValidCode() {
        assertEquals(NewLicensee.NINTENDO, NewLicensee.fromCode("31"));
        assertEquals(NewLicensee.CAPCOM, NewLicensee.fromCode("08"));
    }

    @Test
    void testFromCodeInvalidCode() {
        assertEquals(NewLicensee.UNKNOWN, NewLicensee.fromCode("ZZ"));
    }

    @Test
    void testGetCode() {
        assertEquals("31", NewLicensee.NINTENDO.getCode());
        assertEquals("08", NewLicensee.CAPCOM.getCode());
    }
}
