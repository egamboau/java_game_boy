package com.egamboau.gameboy.cpu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

class CPUCycleTest extends CPUTestBase {

    @Test
    void readIncrementsCycles() {
        when(getCurrentBus().readByteFromAddress(0x1234)).thenReturn(0x56);

        assertEquals(0x56, getCurrentCpu().readByteFromAddress(0x1234));
        assertEquals(1, getCurrentCpu().getCycles());
        verify(getCurrentBus()).readByteFromAddress(0x1234);
    }

    @Test
    void writeIncrementsCycles() {
        getCurrentCpu().writeByteToAddress(0x1234, 0x56);

        assertEquals(1, getCurrentCpu().getCycles());
        verify(getCurrentBus()).writeByteToAddress(0x56, 0x1234);
    }
}
