package com.egamboau.gameboy.cpu.instructions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.egamboau.gameboy.cpu.CPUTestBase;
import com.egamboau.test.TestUtils;

class DecrementTest extends CPUTestBase {

    @ParameterizedTest
    @MethodSource("generateTestArgumentsFor8BitTests")
    void testDecInstructionFor8BitRegisters(int opcode, int registerData, RegisterType register, boolean expectedSubstractFlag, boolean expectedHalfCarryFlag, boolean expctedZeroFlag) {
        when(this.getCurrentBus().readByteFromAddress(anyInt())).thenReturn(opcode);
        executeDecrementTest(registerData, register, false);

        // check if the registerValues are set accordingly
        assertEquals(expectedSubstractFlag, getCurrentCpu().getSubtract(), "Substract flag set incorrectly");
        assertEquals(expectedHalfCarryFlag, getCurrentCpu().getHalfCarry(), "Half Carry flag set incorrectly");
        assertEquals(expctedZeroFlag, getCurrentCpu().getZero(), "Carry flag set incorrectly");
    }

    @ParameterizedTest
    @MethodSource("generateTestArgumentsFor16BitTests")
    void testDetestDecInstructionFor16BitRegisterscBC(int opcode, int registerData, RegisterType register) {
        when(this.getCurrentBus().readByteFromAddress(anyInt())).thenReturn(
            opcode
        );

        executeDecrementTest(registerData, register, true);
    }

    static Stream<Arguments> generateTestArgumentsFor8BitTests() {
        return Stream.of(
            Arguments.of(0x05, 0, RegisterType.REGISTER_B, true, true, false),
            Arguments.of(0x05, 0x10, RegisterType.REGISTER_B, true, true, false),
            Arguments.of(0x05, 0x0E, RegisterType.REGISTER_B, true, false, false),
            Arguments.of(0x05, 0x01, RegisterType.REGISTER_B, true, false, true),
            
            Arguments.of(0x0D, 0, RegisterType.REGISTER_C, true, true, false),
            Arguments.of(0x0D, 0x10, RegisterType.REGISTER_C, true, true, false),
            Arguments.of(0x0D, 0x0E, RegisterType.REGISTER_C, true, false, false),
            Arguments.of(0x0D, 0x01, RegisterType.REGISTER_C, true, false, true),

            Arguments.of(0x15, 0, RegisterType.REGISTER_D, true, true, false),
            Arguments.of(0x15, 0x10, RegisterType.REGISTER_D, true, true, false),
            Arguments.of(0x15, 0x0E, RegisterType.REGISTER_D, true, false, false),
            Arguments.of(0x15, 0x01, RegisterType.REGISTER_D, true, false, true),

            Arguments.of(0x1D, 0, RegisterType.REGISTER_E, true, true, false),
            Arguments.of(0x1D, 0x10, RegisterType.REGISTER_E, true, true, false),
            Arguments.of(0x1D, 0x0E, RegisterType.REGISTER_E, true, false, false),
            Arguments.of(0x1D, 0x01, RegisterType.REGISTER_E, true, false, true),

            Arguments.of(0x25, 0, RegisterType.REGISTER_H, true, true, false),
            Arguments.of(0x25, 0x10, RegisterType.REGISTER_H, true, true, false),
            Arguments.of(0x25, 0x0E, RegisterType.REGISTER_H, true, false, false),
            Arguments.of(0x25, 0x01, RegisterType.REGISTER_H, true, false, true),

            Arguments.of(0x2D, 0, RegisterType.REGISTER_L, true, true, false),
            Arguments.of(0x2D, 0x10, RegisterType.REGISTER_L, true, true, false),
            Arguments.of(0x2D, 0x0E, RegisterType.REGISTER_L, true, false, false),
            Arguments.of(0x2D, 0x01, RegisterType.REGISTER_L, true, false, true)
        );
    }

    static Stream<Arguments> generateTestArgumentsFor16BitTests() {
        return Stream.of(
            Arguments.of(0x0B, TestUtils.getRandomIntegerInRange(0x00, 0xFFFF), RegisterType.REGISTER_BC),
            Arguments.of(0x1B, TestUtils.getRandomIntegerInRange(0x00, 0xFFFF), RegisterType.REGISTER_DE),
            Arguments.of(0x2B, TestUtils.getRandomIntegerInRange(0x00, 0xFFFF), RegisterType.REGISTER_HL)
        );
    }

    private void executeDecrementTest(int registerData, RegisterType register, boolean is16Bit) {
        int expectedValue;
        RegisterType[] filteredRegister;
        int expectedCycles;
        if (is16Bit) {
            expectedValue = (registerData - 1) & 0xFFFF;
            filteredRegister = TestUtils.getPairForRegister(register);
            expectedCycles = 2;
        } else {
            expectedValue = (registerData - 1) & 0xFF;
            filteredRegister = TestUtils.getPairForRegister(register, RegisterType.REGISTER_F);
            expectedCycles = 1;
        }

        this.getCurrentCpu().setValueInRegister(registerData, register);

        Map<RegisterType, Integer> registerValues = this.getCpuRegisters(filteredRegister);
        long previousCycleCount = getCurrentCpu().getCycles();
        this.getCurrentCpu().cpuStep();
        long currentCycleCount = getCurrentCpu().getCycles();
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisters(filteredRegister);

        // Register must be correct
        assertEquals(expectedValue, this.getCurrentCpu().getValueFromRegister(register), "Register value incorrect: " + register);
        // cycles must be updated as required
        assertEquals(previousCycleCount + expectedCycles, currentCycleCount, "Cycle count not currently matching.");
        registerValues.computeIfPresent(RegisterType.REGISTER_PC, (t, u) -> u + 1);

        assertEquals(registerValues, newRegisterValues, "CPU Register values did not match the previous state.");
    }
}
