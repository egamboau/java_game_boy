package com.egamboau.gameboy.cpu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.egamboau.gameboy.cpu.instructions.RegisterType;

class CPUStackTest extends CPUTestBase {

    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    void pushWordStoresBothBytesAndDecrementsStackPointer() {
        getCurrentCpu().setValueInRegister(0xC100, RegisterType.SP);

        getCurrentCpu().pushWord(0x1234);

        assertEquals(0xC0FE, getCurrentCpu().getValueFromRegister(RegisterType.SP));
        assertEquals(2, getCurrentCpu().getCycles());
        verify(getCurrentBus()).writeByteToAddress(0x12, 0xC0FF);
        verify(getCurrentBus()).writeByteToAddress(0x34, 0xC0FE);
    }

    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    void popWordReturnsBothBytesAndIncrementsStackPointer() {
        getCurrentCpu().setValueInRegister(0xC0FE, RegisterType.SP);
        when(getCurrentBus().readByteFromAddress(0xC0FE)).thenReturn(0x34);
        when(getCurrentBus().readByteFromAddress(0xC0FF)).thenReturn(0x12);

        assertEquals(0x1234, getCurrentCpu().popWord());
        assertEquals(0xC100, getCurrentCpu().getValueFromRegister(RegisterType.SP));
        assertEquals(2, getCurrentCpu().getCycles());
    }
}
