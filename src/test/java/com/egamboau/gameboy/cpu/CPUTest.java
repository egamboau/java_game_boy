package com.egamboau.gameboy.cpu;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.egamboau.gameboy.cpu.instructions.RegisterType;
import com.egamboau.gameboy.memory.MemoryMapConstants;

class CPUTest extends CPUTestBase {

    @Test
    void imeIsDisabledByDefault() {
        assertFalse(getCurrentCpu().isImeEnabled());
    }

    @Test
    void imeCanBeEnabledAndDisabled() {
        getCurrentCpu().setImeEnabled(true);
        assertTrue(getCurrentCpu().isImeEnabled());

        getCurrentCpu().setImeEnabled(false);
        assertFalse(getCurrentCpu().isImeEnabled());
    }

    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    void detectsPendingInterruptWhenIeAndIfShareASetBit() {
        when(getCurrentBus().readByteFromAddress(MemoryMapConstants.INTERRUPT_ENABLE_REGISTER))
                .thenReturn(0b00101);
        when(getCurrentBus().readByteFromAddress(MemoryMapConstants.INTERRUPT_FLAG_REGISTER))
                .thenReturn(0b00100);

        assertTrue(getCurrentCpu().hasPendingInterrupt());
    }

    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    void ignoresInterruptsWithoutACommonIeAndIfBit() {
        when(getCurrentBus().readByteFromAddress(MemoryMapConstants.INTERRUPT_ENABLE_REGISTER))
                .thenReturn(0b00101);
        when(getCurrentBus().readByteFromAddress(MemoryMapConstants.INTERRUPT_FLAG_REGISTER))
                .thenReturn(0b11010);

        assertFalse(getCurrentCpu().hasPendingInterrupt());
    }

    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    void selectsHighestPriorityPendingInterruptVector() {
        assertEquals(0x0040, getCurrentCpu().getPendingInterruptVector(0b00001));
        assertEquals(0x0048, getCurrentCpu().getPendingInterruptVector(0b00010));
        assertEquals(0x0050, getCurrentCpu().getPendingInterruptVector(0b00100));
        assertEquals(0x0058, getCurrentCpu().getPendingInterruptVector(0b01000));
        assertEquals(0x0060, getCurrentCpu().getPendingInterruptVector(0b10000));
    }

    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    void servicesHighestPriorityPendingInterruptFirst() {
        when(getCurrentBus().readByteFromAddress(MemoryMapConstants.INTERRUPT_ENABLE_REGISTER))
                .thenReturn(0b11111);
        when(getCurrentBus().readByteFromAddress(MemoryMapConstants.INTERRUPT_FLAG_REGISTER))
                .thenReturn(0b11111);
        getCurrentCpu().setValueInRegister(0xC100, RegisterType.SP);
        getCurrentCpu().setImeEnabled(true);

        getCurrentCpu().cpuStep();

        assertEquals(0x0040, getCurrentCpu().getValueFromRegister(RegisterType.PC));
        verify(getCurrentBus()).writeByteToAddress(
                0b11110, MemoryMapConstants.INTERRUPT_FLAG_REGISTER);
    }

    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    void clearsOnlyTheServicedInterruptFlag() {
        when(getCurrentBus().readByteFromAddress(MemoryMapConstants.INTERRUPT_FLAG_REGISTER))
                .thenReturn(0b11111, 0b11110, 0b11100, 0b11000, 0b10000);

        getCurrentCpu().servicePendingInterrupt(0b00001);
        getCurrentCpu().servicePendingInterrupt(0b00010);
        getCurrentCpu().servicePendingInterrupt(0b00100);
        getCurrentCpu().servicePendingInterrupt(0b01000);
        getCurrentCpu().servicePendingInterrupt(0b10000);

        verify(getCurrentBus()).writeByteToAddress(
                0b11110, MemoryMapConstants.INTERRUPT_FLAG_REGISTER);
        verify(getCurrentBus()).writeByteToAddress(
                0b11100, MemoryMapConstants.INTERRUPT_FLAG_REGISTER);
        verify(getCurrentBus()).writeByteToAddress(
                0b11000, MemoryMapConstants.INTERRUPT_FLAG_REGISTER);
        verify(getCurrentBus()).writeByteToAddress(
                0b10000, MemoryMapConstants.INTERRUPT_FLAG_REGISTER);
        verify(getCurrentBus()).writeByteToAddress(
                0b00000, MemoryMapConstants.INTERRUPT_FLAG_REGISTER);
    }

    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    void servicesPendingInterrupt() {
        when(getCurrentBus().readByteFromAddress(MemoryMapConstants.INTERRUPT_ENABLE_REGISTER))
                .thenReturn(0b00001);
        when(getCurrentBus().readByteFromAddress(MemoryMapConstants.INTERRUPT_FLAG_REGISTER))
                .thenReturn(0b00001);
        getCurrentCpu().setValueInRegister(0x1234, RegisterType.PC);
        getCurrentCpu().setValueInRegister(0xC100, RegisterType.SP);
        getCurrentCpu().setImeEnabled(true);
        getCurrentCpu().setHalted(true);

        getCurrentCpu().cpuStep();

        assertAll(
                () -> assertEquals(0x0040, getCurrentCpu().getValueFromRegister(RegisterType.PC)),
                () -> assertEquals(0xC0FE, getCurrentCpu().getValueFromRegister(RegisterType.SP)),
                () -> assertFalse(getCurrentCpu().isImeEnabled()),
                () -> assertFalse(getCurrentCpu().isHalted()),
                () -> assertEquals(5, getCurrentCpu().getCycles()));
        verify(getCurrentBus()).writeByteToAddress(
                0b00000, MemoryMapConstants.INTERRUPT_FLAG_REGISTER);
        verify(getCurrentBus()).writeByteToAddress(0x12, 0xC0FF);
        verify(getCurrentBus()).writeByteToAddress(0x34, 0xC0FE);
    }

    @Test
    void pendingInterruptWakesHaltedCpuWhenImeIsDisabled() {
        when(getCurrentBus().readByteFromAddress(MemoryMapConstants.INTERRUPT_ENABLE_REGISTER))
                .thenReturn(0b00001);
        when(getCurrentBus().readByteFromAddress(MemoryMapConstants.INTERRUPT_FLAG_REGISTER))
                .thenReturn(0b00001);
        getCurrentCpu().setHalted(true);

        getCurrentCpu().cpuStep();

        assertFalse(getCurrentCpu().isHalted());
    }

    @Test
    void haltedCpuOnlyAdvancesCycles() {
        getCurrentCpu().setHalted(true);

        getCurrentCpu().cpuStep();

        assertEquals(1, getCurrentCpu().getCycles());
        assertTrue(getCurrentCpu().isHalted());
        verify(getCurrentBus(), never()).readByteFromAddress(0);
    }

    @Test
    void stoppedCpuOnlyAdvancesCycles() {
        getCurrentCpu().setStopped(true);

        getCurrentCpu().cpuStep();

        assertEquals(1, getCurrentCpu().getCycles());
        verify(getCurrentBus(), never()).readByteFromAddress(0);
    }

    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    void incrementAndDecrementHlPair() {
        getCurrentCpu().setValueInRegister(0x1234, RegisterType.HL);

        getCurrentCpu().incrementRegisterPair(RegisterType.HL);
        assertEquals(0x1235, getCurrentCpu().getValueFromRegister(RegisterType.HL));

        getCurrentCpu().decrementRegisterPair(RegisterType.HL);
        assertEquals(0x1234, getCurrentCpu().getValueFromRegister(RegisterType.HL));
    }

    @Test
    void pairHelpersRejectUnsupportedRegister() {
        assertThrows(IllegalArgumentException.class,
                () -> getCurrentCpu().incrementRegisterPair(RegisterType.BC));
        assertThrows(IllegalArgumentException.class,
                () -> getCurrentCpu().decrementRegisterPair(RegisterType.BC));
    }

    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    void rejectsDirectFlagRegisterWrite() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> getCurrentCpu().setValueInRegister(0xFF, RegisterType.F));

        assertEquals("Setting data not supported for this register F", exception.getMessage());
    }
}
