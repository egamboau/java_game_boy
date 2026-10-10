package com.egamboau.gameboy.cpu.instructions.implementations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.Test;

import com.egamboau.gameboy.cpu.CPUTestBase;
import com.egamboau.gameboy.cpu.instructions.RegisterType;

class StopTest extends CPUTestBase {

    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    void stopAdvancesPcWithoutChangingRegistersOrMemory() {
        when(this.getCurrentBus().readByteFromAddress(0)).thenReturn(0x10);
        Map<RegisterType, Integer> oldRegisterValues = this.getCpuRegisters();

        this.getCurrentCpu().cpuStep();
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisters();

        oldRegisterValues.computeIfPresent(RegisterType.PC, (t, u) -> u + 1);
        assertEquals(1, getCurrentCpu().getCycles());
        assertEquals(oldRegisterValues, newRegisterValues);
        assertTrue(getCurrentCpu().isStopped());
        verify(getCurrentBus(), never()).writeByteToAddress(org.mockito.ArgumentMatchers.anyInt(),
                org.mockito.ArgumentMatchers.anyInt());
    }

    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    void haltedCpuPreservesRegisters() {
        when(this.getCurrentBus().readByteFromAddress(0)).thenReturn(0x76);
        Map<RegisterType, Integer> oldRegisterValues = this.getCpuRegisters();

        this.getCurrentCpu().cpuStep();
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisters();

        oldRegisterValues.computeIfPresent(RegisterType.PC, (t, u) -> u + 1);
        assertEquals(1, getCurrentCpu().getCycles());
        assertEquals(oldRegisterValues, newRegisterValues);
        assertTrue(getCurrentCpu().isHalted());
    }

    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    void stoppedCpuDoesNotFetchOrServiceInterruptsUntilExplicitlyResumed() {
        when(this.getCurrentBus().readByteFromAddress(0)).thenReturn(0x10);
        getCurrentCpu().cpuStep();
        clearInvocations(getCurrentBus());
        getCurrentCpu().setImeEnabled(true);

        getCurrentCpu().cpuStep();
        getCurrentCpu().cpuStep();

        assertEquals(3, getCurrentCpu().getCycles());
        assertEquals(1, getCurrentCpu().getValueFromRegister(RegisterType.PC));
        verifyNoInteractions(getCurrentBus());

        when(this.getCurrentBus().readByteFromAddress(1)).thenReturn(0x04);
        getCurrentCpu().setStopped(false);
        getCurrentCpu().cpuStep();

        assertFalse(getCurrentCpu().isStopped());
        assertEquals(1, getCurrentCpu().getValueFromRegister(RegisterType.B));
        assertEquals(2, getCurrentCpu().getValueFromRegister(RegisterType.PC));
    }

}
