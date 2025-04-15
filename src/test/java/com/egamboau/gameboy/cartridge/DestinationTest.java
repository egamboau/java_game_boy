package com.egamboau.gameboy.cartridge;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DestinationTest {

    @Test
    void testFromByte_ValidCode() {
        assertEquals(Destination.JAPAN, Destination.fromByte((byte) 0x00));
        assertEquals(Destination.OVERSEAS, Destination.fromByte((byte) 0x01));
    }

    @Test
    void testFromByte_InvalidCode() {
        byte invalidCode = (byte) 0x02;
        Exception exception = assertThrows(IllegalArgumentException.class, () -> Destination.fromByte(invalidCode));
        assertEquals("Unknown RAM size code: 2", exception.getMessage());
    }
}