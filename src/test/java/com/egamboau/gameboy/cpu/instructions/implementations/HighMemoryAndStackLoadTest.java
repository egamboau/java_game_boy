package com.egamboau.gameboy.cpu.instructions.implementations;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.egamboau.gameboy.cpu.CPUTestBase;
import com.egamboau.gameboy.cpu.instructions.RegisterType;

class HighMemoryAndStackLoadTest extends CPUTestBase {

    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    void ldhImmediateAddressFromA() {
        stubInstruction(0xE0, 0x42);
        getCurrentCpu().setValueInRegister(0xAB, RegisterType.A);

        getCurrentCpu().cpuStep();

        verify(getCurrentBus()).writeByteToAddress(0xAB, 0xFF42);
        assertStep(2, 3);
    }

    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    void ldCAddressFromA() {
        stubInstruction(0xE2);
        getCurrentCpu().setValueInRegister(0x42, RegisterType.C);
        getCurrentCpu().setValueInRegister(0xAB, RegisterType.A);

        getCurrentCpu().cpuStep();

        verify(getCurrentBus()).writeByteToAddress(0xAB, 0xFF42);
        assertStep(1, 2);
    }

    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    void ldImmediateAddressFromA() {
        stubInstruction(0xEA, 0x34, 0x12);
        getCurrentCpu().setValueInRegister(0xAB, RegisterType.A);

        getCurrentCpu().cpuStep();

        verify(getCurrentBus()).writeByteToAddress(0xAB, 0x1234);
        assertStep(3, 4);
    }

    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    void ldhAFromImmediateAddress() {
        stubInstruction(0xF0, 0x42);
        when(getCurrentBus().readByteFromAddress(0xFF42)).thenReturn(0xAB);

        getCurrentCpu().cpuStep();

        assertEquals(0xAB, getCurrentCpu().getValueFromRegister(RegisterType.A));
        assertStep(2, 3);
    }

    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    void ldAFromCAddress() {
        stubInstruction(0xF2);
        getCurrentCpu().setValueInRegister(0x42, RegisterType.C);
        when(getCurrentBus().readByteFromAddress(0xFF42)).thenReturn(0xAB);

        getCurrentCpu().cpuStep();

        assertEquals(0xAB, getCurrentCpu().getValueFromRegister(RegisterType.A));
        assertStep(1, 2);
    }

    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    void ldAFromImmediateAddress() {
        stubInstruction(0xFA, 0x34, 0x12);
        when(getCurrentBus().readByteFromAddress(0x1234)).thenReturn(0xAB);

        getCurrentCpu().cpuStep();

        assertEquals(0xAB, getCurrentCpu().getValueFromRegister(RegisterType.A));
        assertStep(3, 4);
    }

    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    void addSignedImmediateToStackPointer() {
        stubInstruction(0xE8, 0x08);
        getCurrentCpu().setValueInRegister(0xFFF8, RegisterType.SP);
        getCurrentCpu().setZero(true);
        getCurrentCpu().setSubtract(true);

        getCurrentCpu().cpuStep();

        assertAll(
            () -> assertEquals(0, getCurrentCpu().getValueFromRegister(RegisterType.SP)),
            () -> assertEquals(false, getCurrentCpu().getZero()),
            () -> assertEquals(false, getCurrentCpu().getSubtract()),
            () -> assertEquals(true, getCurrentCpu().getHalfCarry()),
            () -> assertEquals(true, getCurrentCpu().getCarry())
        );
        assertStep(2, 4);
    }

    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    void addNegativeSignedImmediateToStackPointer() {
        stubInstruction(0xE8, 0xF8);
        getCurrentCpu().setValueInRegister(0x0008, RegisterType.SP);

        getCurrentCpu().cpuStep();

        assertAll(
            () -> assertEquals(0, getCurrentCpu().getValueFromRegister(RegisterType.SP)),
            () -> assertEquals(false, getCurrentCpu().getZero()),
            () -> assertEquals(false, getCurrentCpu().getSubtract()),
            () -> assertEquals(true, getCurrentCpu().getHalfCarry()),
            () -> assertEquals(true, getCurrentCpu().getCarry())
        );
        assertStep(2, 4);
    }

    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    void loadHlFromStackPointerPlusNegativeSignedImmediate() {
        stubInstruction(0xF8, 0xF8);
        getCurrentCpu().setValueInRegister(0x0008, RegisterType.SP);

        getCurrentCpu().cpuStep();

        assertAll(
            () -> assertEquals(0, getCurrentCpu().getValueFromRegister(RegisterType.HL)),
            () -> assertEquals(0x0008, getCurrentCpu().getValueFromRegister(RegisterType.SP)),
            () -> assertEquals(false, getCurrentCpu().getZero()),
            () -> assertEquals(false, getCurrentCpu().getSubtract()),
            () -> assertEquals(true, getCurrentCpu().getHalfCarry()),
            () -> assertEquals(true, getCurrentCpu().getCarry())
        );
        assertStep(2, 3);
    }

    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    void loadStackPointerFromHl() {
        stubInstruction(0xF9);
        getCurrentCpu().setValueInRegister(0x1234, RegisterType.HL);

        getCurrentCpu().cpuStep();

        assertEquals(0x1234, getCurrentCpu().getValueFromRegister(RegisterType.SP));
        assertStep(1, 2);
    }

    private void stubInstruction(final int opcode, final int... operands) {
        when(getCurrentBus().readByteFromAddress(0)).thenReturn(opcode);
        for (int index = 0; index < operands.length; index++) {
            when(getCurrentBus().readByteFromAddress(index + 1)).thenReturn(operands[index]);
        }
    }

    private void assertStep(final int expectedProgramCounter, final int expectedCycles) {
        assertAll(
            () -> assertEquals(expectedProgramCounter,
                    getCurrentCpu().getValueFromRegister(RegisterType.PC)),
            () -> assertEquals(expectedCycles, getCurrentCpu().getCycles())
        );
    }
}
