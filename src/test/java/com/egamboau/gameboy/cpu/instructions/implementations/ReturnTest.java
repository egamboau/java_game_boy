package com.egamboau.gameboy.cpu.instructions.implementations;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.egamboau.gameboy.cpu.CPUTestBase;
import com.egamboau.gameboy.cpu.instructions.RegisterType;

class ReturnTest extends CPUTestBase {

    private static final int RET_NZ_OPCODE = 0xC0;
    private static final int RET_OPCODE = 0xC9;
    private static final int STACK_POINTER = 0xC100;
    private static final int RETURN_ADDRESS = 0x1234;

    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    void retLoadsPcAndAdvancesStack() {
        when(getCurrentBus().readByteFromAddress(0)).thenReturn(RET_OPCODE);
        when(getCurrentBus().readByteFromAddress(STACK_POINTER)).thenReturn(RETURN_ADDRESS & MASK_INT_8_BIT);
        when(getCurrentBus().readByteFromAddress(STACK_POINTER + 1)).thenReturn(RETURN_ADDRESS >> 8);
        getCurrentCpu().setValueInRegister(STACK_POINTER, RegisterType.SP);
        getCurrentCpu().setZero(true);
        getCurrentCpu().setSubtract(true);
        getCurrentCpu().setHalfCarry(true);
        getCurrentCpu().setCarry(true);
        long cyclesBefore = getCurrentCpu().getCycles();

        getCurrentCpu().cpuStep();

        assertAll(
            () -> assertEquals(RETURN_ADDRESS, getCurrentCpu().getValueFromRegister(RegisterType.PC)),
            () -> assertEquals(STACK_POINTER + 2, getCurrentCpu().getValueFromRegister(RegisterType.SP)),
            () -> assertEquals(cyclesBefore + 4, getCurrentCpu().getCycles()),
            () -> assertTrue(getCurrentCpu().getZero()),
            () -> assertTrue(getCurrentCpu().getSubtract()),
            () -> assertTrue(getCurrentCpu().getHalfCarry()),
            () -> assertTrue(getCurrentCpu().getCarry())
        );
        verify(getCurrentBus()).readByteFromAddress(STACK_POINTER);
        verify(getCurrentBus()).readByteFromAddress(STACK_POINTER + 1);
    }

    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    void retNzLoadsPcAndAdvancesStackWhenZeroIsClear() {
        when(getCurrentBus().readByteFromAddress(0)).thenReturn(RET_NZ_OPCODE);
        when(getCurrentBus().readByteFromAddress(STACK_POINTER)).thenReturn(RETURN_ADDRESS & MASK_INT_8_BIT);
        when(getCurrentBus().readByteFromAddress(STACK_POINTER + 1)).thenReturn(RETURN_ADDRESS >> 8);
        getCurrentCpu().setValueInRegister(STACK_POINTER, RegisterType.SP);
        getCurrentCpu().setZero(false);
        getCurrentCpu().setSubtract(true);
        getCurrentCpu().setHalfCarry(true);
        getCurrentCpu().setCarry(true);
        long cyclesBefore = getCurrentCpu().getCycles();

        getCurrentCpu().cpuStep();

        assertAll(
            () -> assertEquals(RETURN_ADDRESS, getCurrentCpu().getValueFromRegister(RegisterType.PC)),
            () -> assertEquals(STACK_POINTER + 2, getCurrentCpu().getValueFromRegister(RegisterType.SP)),
            () -> assertEquals(cyclesBefore + 5, getCurrentCpu().getCycles()),
            () -> assertFalse(getCurrentCpu().getZero()),
            () -> assertTrue(getCurrentCpu().getSubtract()),
            () -> assertTrue(getCurrentCpu().getHalfCarry()),
            () -> assertTrue(getCurrentCpu().getCarry())
        );
        verify(getCurrentBus()).readByteFromAddress(STACK_POINTER);
        verify(getCurrentBus()).readByteFromAddress(STACK_POINTER + 1);
    }

    @Test
    void retNzLeavesStackUntouchedWhenZeroIsSet() {
        when(getCurrentBus().readByteFromAddress(0)).thenReturn(RET_NZ_OPCODE);
        getCurrentCpu().setValueInRegister(STACK_POINTER, RegisterType.SP);
        getCurrentCpu().setZero(true);
        long cyclesBefore = getCurrentCpu().getCycles();

        getCurrentCpu().cpuStep();

        assertAll(
            () -> assertEquals(1, getCurrentCpu().getValueFromRegister(RegisterType.PC)),
            () -> assertEquals(STACK_POINTER, getCurrentCpu().getValueFromRegister(RegisterType.SP)),
            () -> assertEquals(cyclesBefore + 2, getCurrentCpu().getCycles()),
            () -> assertTrue(getCurrentCpu().getZero())
        );
        verify(getCurrentBus(), never()).readByteFromAddress(STACK_POINTER);
        verify(getCurrentBus(), never()).readByteFromAddress(STACK_POINTER + 1);
    }
}
