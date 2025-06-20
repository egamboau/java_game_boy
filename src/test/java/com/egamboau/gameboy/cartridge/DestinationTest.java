package com.egamboau.gameboy.cartridge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;


class DestinationTest {

    @Test
    void testFromByteValidCode() {
        assertEquals(Destination.JAPAN, Destination.fromByte((byte) 0x00));
        assertEquals(Destination.OVERSEAS, Destination.fromByte((byte) 0x01));
    }

    @Test
    void testFromByteInvalidCode() {
        byte invalidCode = (byte) 0x02;
        Exception exception = assertThrows(IllegalArgumentException.class, () -> Destination.fromByte(invalidCode));
        assertEquals("Unknown RAM size code: 2", exception.getMessage());
    }
}
