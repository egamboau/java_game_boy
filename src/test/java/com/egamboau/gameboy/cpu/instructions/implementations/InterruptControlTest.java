package com.egamboau.gameboy.cpu.instructions.implementations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.egamboau.gameboy.cpu.CPUTestBase;
import com.egamboau.gameboy.cpu.instructions.RegisterType;
import com.egamboau.gameboy.memory.MemoryMapConstants;

@SuppressWarnings("checkstyle:magicnumber")
class InterruptControlTest extends CPUTestBase {

    @Test
    void diDisablesImeImmediatelyAndPreservesRegisters() {
        when(getCurrentBus().readByteFromAddress(0)).thenReturn(0xF3);
        getCurrentCpu().setImeEnabled(true);
        getCurrentCpu().setValueInRegister(0x12F0, RegisterType.AF);
        var registers = getCpuRegisters(RegisterType.PC);

        getCurrentCpu().cpuStep();

        assertFalse(getCurrentCpu().isImeEnabled());
        assertEquals(1, getCurrentCpu().getValueFromRegister(RegisterType.PC));
        assertEquals(1, getCurrentCpu().getCycles());
        assertEquals(registers, getCpuRegisters(RegisterType.PC));
        when(getCurrentBus().readByteFromAddress(MemoryMapConstants.INTERRUPT_ENABLE_REGISTER)).thenReturn(1);
        when(getCurrentBus().readByteFromAddress(MemoryMapConstants.INTERRUPT_FLAG_REGISTER)).thenReturn(1);
        getCurrentCpu().cpuStep();
        assertEquals(2, getCurrentCpu().getValueFromRegister(RegisterType.PC));
        assertFalse(getCurrentCpu().isImeEnabled());
    }

    @Test
    void eiEnablesImeOnlyAfterFollowingInstructionCompletes() {
        when(getCurrentBus().readByteFromAddress(0)).thenReturn(0xFB);
        when(getCurrentBus().readByteFromAddress(1)).thenReturn(0x06); // LD B,d8
        when(getCurrentBus().readByteFromAddress(2)).thenReturn(0x42);
        when(getCurrentBus().readByteFromAddress(MemoryMapConstants.INTERRUPT_ENABLE_REGISTER)).thenReturn(1);
        when(getCurrentBus().readByteFromAddress(MemoryMapConstants.INTERRUPT_FLAG_REGISTER)).thenReturn(1);
        getCurrentCpu().setValueInRegister(0xC100, RegisterType.SP);
        getCurrentCpu().setValueInRegister(0x12F0, RegisterType.AF);
        var registers = getCpuRegisters(RegisterType.PC);

        getCurrentCpu().cpuStep();

        assertFalse(getCurrentCpu().isImeEnabled());
        assertEquals(1, getCurrentCpu().getValueFromRegister(RegisterType.PC));
        assertEquals(1, getCurrentCpu().getCycles());
        assertEquals(registers, getCpuRegisters(RegisterType.PC));

        getCurrentCpu().cpuStep();

        assertTrue(getCurrentCpu().isImeEnabled());
        assertEquals(0x42, getCurrentCpu().getValueFromRegister(RegisterType.B));
        assertEquals(3, getCurrentCpu().getValueFromRegister(RegisterType.PC));
        assertEquals(3, getCurrentCpu().getCycles());

        getCurrentCpu().cpuStep();

        assertEquals(0x40, getCurrentCpu().getValueFromRegister(RegisterType.PC));
        assertFalse(getCurrentCpu().isImeEnabled());
        verify(getCurrentBus()).writeByteToAddress(3, 0xC0FE);
        getCurrentCpu().cpuStep();
        assertFalse(getCurrentCpu().isImeEnabled());
    }

    @Test
    void diCancelsPendingEiEnable() {
        when(getCurrentBus().readByteFromAddress(0)).thenReturn(0xFB);
        when(getCurrentBus().readByteFromAddress(1)).thenReturn(0xF3);

        getCurrentCpu().cpuStep();
        assertFalse(getCurrentCpu().isImeEnabled());
        getCurrentCpu().cpuStep();
        assertFalse(getCurrentCpu().isImeEnabled());
        getCurrentCpu().cpuStep();
        assertFalse(getCurrentCpu().isImeEnabled());
    }

    @Test
    void consecutiveEiDoesNotPostponeFirstEnable() {
        when(getCurrentBus().readByteFromAddress(0)).thenReturn(0xFB);
        when(getCurrentBus().readByteFromAddress(1)).thenReturn(0xFB);

        getCurrentCpu().cpuStep();
        assertFalse(getCurrentCpu().isImeEnabled());
        getCurrentCpu().cpuStep();
        assertTrue(getCurrentCpu().isImeEnabled());
    }

    @Test
    void eiLeavesAlreadyEnabledImeEnabled() {
        when(getCurrentBus().readByteFromAddress(0)).thenReturn(0xFB);
        getCurrentCpu().setImeEnabled(true);

        getCurrentCpu().cpuStep();

        assertTrue(getCurrentCpu().isImeEnabled());
    }

    @Test
    void retiRestoresPcAndEnablesImeImmediately() {
        when(getCurrentBus().readByteFromAddress(0)).thenReturn(0xD9);
        when(getCurrentBus().readByteFromAddress(0xC100)).thenReturn(0x34);
        when(getCurrentBus().readByteFromAddress(0xC101)).thenReturn(0x12);
        when(getCurrentBus().readByteFromAddress(MemoryMapConstants.INTERRUPT_ENABLE_REGISTER)).thenReturn(1);
        when(getCurrentBus().readByteFromAddress(MemoryMapConstants.INTERRUPT_FLAG_REGISTER)).thenReturn(1);
        getCurrentCpu().setValueInRegister(0xC100, RegisterType.SP);
        getCurrentCpu().setValueInRegister(0x12F0, RegisterType.AF);
        var registers = getCpuRegisters(RegisterType.PC, RegisterType.SP);

        getCurrentCpu().cpuStep();

        assertEquals(0x1234, getCurrentCpu().getValueFromRegister(RegisterType.PC));
        assertEquals(0xC102, getCurrentCpu().getValueFromRegister(RegisterType.SP));
        assertTrue(getCurrentCpu().isImeEnabled());
        assertEquals(4, getCurrentCpu().getCycles());
        assertEquals(registers, getCpuRegisters(RegisterType.PC, RegisterType.SP));

        getCurrentCpu().cpuStep();

        assertEquals(0x40, getCurrentCpu().getValueFromRegister(RegisterType.PC));
        assertFalse(getCurrentCpu().isImeEnabled());
        verify(getCurrentBus()).writeByteToAddress(0x12, 0xC101);
        verify(getCurrentBus()).writeByteToAddress(0x34, 0xC100);
    }
}
