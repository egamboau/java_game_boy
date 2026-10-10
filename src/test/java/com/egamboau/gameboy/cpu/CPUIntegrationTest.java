package com.egamboau.gameboy.cpu;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

import com.egamboau.gameboy.cartridge.Cartridge;
import com.egamboau.gameboy.cpu.instructions.RegisterType;
import com.egamboau.gameboy.memory.Bus;
import com.egamboau.gameboy.memory.MemoryMapConstants;

class CPUIntegrationTest {

    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    void executesCallInterruptAndReturnFlowOnRealBus() {
        byte[] rom = new byte[0x8000];
        rom[0x0100] = (byte) 0xCD; // CALL 0110
        rom[0x0101] = 0x10;
        rom[0x0102] = 0x01;
        rom[0x0103] = (byte) 0xFB; // EI
        rom[0x0104] = 0x76; // HALT
        rom[0x0110] = 0x04; // INC B
        rom[0x0111] = (byte) 0xC9; // RET
        rom[0x0040] = 0x0C; // INC C
        rom[0x0041] = (byte) 0xD9; // RETI
        Cartridge cartridge = mock(Cartridge.class);
        doAnswer(invocation -> rom[(int) invocation.getArgument(0)] & 0xFF)
                .when(cartridge).readByteFromAddress(anyInt());
        Bus bus = new Bus(cartridge);
        CPU cpu = new CPU(bus);
        cpu.setValueInRegister(0x0100, RegisterType.PC);
        cpu.setValueInRegister(0xC100, RegisterType.SP);
        bus.writeByteToAddress(1, MemoryMapConstants.INTERRUPT_ENABLE_REGISTER);
        bus.writeByteToAddress(1, MemoryMapConstants.INTERRUPT_FLAG_REGISTER);

        for (int step = 0; step < 9; step++) {
            cpu.cpuStep();
        }

        assertAll(
                () -> assertEquals(1, cpu.getValueFromRegister(RegisterType.B)),
                () -> assertEquals(1, cpu.getValueFromRegister(RegisterType.C)),
                () -> assertEquals(0x0105, cpu.getValueFromRegister(RegisterType.PC)),
                () -> assertEquals(0xC100, cpu.getValueFromRegister(RegisterType.SP)),
                () -> assertEquals(0x04, bus.readByteFromAddress(0xC0FE)),
                () -> assertEquals(0x01, bus.readByteFromAddress(0xC0FF)),
                () -> assertEquals(0, bus.readByteFromAddress(
                        MemoryMapConstants.INTERRUPT_FLAG_REGISTER)),
                () -> assertTrue(cpu.isImeEnabled()),
                () -> assertTrue(cpu.isHalted()));
    }
}
