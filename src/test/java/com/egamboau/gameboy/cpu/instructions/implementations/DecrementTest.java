package com.egamboau.gameboy.cpu.instructions.implementations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.egamboau.gameboy.cpu.CPUTestBase;
import com.egamboau.gameboy.cpu.instructions.RegisterType;
import com.egamboau.test.TestUtils;

class DecrementTest extends CPUTestBase {

    @ParameterizedTest
    @MethodSource("generateTestArgumentsFor8BitTests")
    void testDecInstructionFor8BitRegisters(final int opcode, final int registerData, final RegisterType register,
            final boolean expectedSubstractFlag, final boolean expectedHalfCarryFlag, final boolean expctedZeroFlag) {
        when(this.getCurrentBus().readByteFromAddress(anyInt())).thenReturn(opcode);
        executeDecrementTest(registerData, register, false);

        // check if the registerValues are set accordingly
        assertEquals(expectedSubstractFlag, getCurrentCpu().getSubtract(), "Substract flag set incorrectly");
        assertEquals(expectedHalfCarryFlag, getCurrentCpu().getHalfCarry(), "Half Carry flag set incorrectly");
        assertEquals(expctedZeroFlag, getCurrentCpu().getZero(), "Carry flag set incorrectly");
    }

    @ParameterizedTest
    @MethodSource("generateTestArgumentsFor16BitTests")
    void testDetestDecInstructionFor16BitRegisterscBC(final int opcode, final int registerData,
            final RegisterType register) {
        when(this.getCurrentBus().readByteFromAddress(anyInt())).thenReturn(
                opcode);

        executeDecrementTest(registerData, register, true);
    }
    @SuppressWarnings({"checkstyle:magicnumber", "checkstyle:parameternumbercheck"})
    static Stream<Arguments> generateTestArgumentsFor8BitTests() {
        return Stream.of(
                Arguments.of(0x05, 0, RegisterType.B, true, true, false),
                Arguments.of(0x05, 0x10, RegisterType.B, true, true, false),
                Arguments.of(0x05, 0x0E, RegisterType.B, true, false, false),
                Arguments.of(0x05, 0x01, RegisterType.B, true, false, true),

                Arguments.of(0x0D, 0, RegisterType.C, true, true, false),
                Arguments.of(0x0D, 0x10, RegisterType.C, true, true, false),
                Arguments.of(0x0D, 0x0E, RegisterType.C, true, false, false),
                Arguments.of(0x0D, 0x01, RegisterType.C, true, false, true),

                Arguments.of(0x15, 0, RegisterType.D, true, true, false),
                Arguments.of(0x15, 0x10, RegisterType.D, true, true, false),
                Arguments.of(0x15, 0x0E, RegisterType.D, true, false, false),
                Arguments.of(0x15, 0x01, RegisterType.D, true, false, true),

                Arguments.of(0x1D, 0, RegisterType.E, true, true, false),
                Arguments.of(0x1D, 0x10, RegisterType.E, true, true, false),
                Arguments.of(0x1D, 0x0E, RegisterType.E, true, false, false),
                Arguments.of(0x1D, 0x01, RegisterType.E, true, false, true),

                Arguments.of(0x25, 0, RegisterType.H, true, true, false),
                Arguments.of(0x25, 0x10, RegisterType.H, true, true, false),
                Arguments.of(0x25, 0x0E, RegisterType.H, true, false, false),
                Arguments.of(0x25, 0x01, RegisterType.H, true, false, true),

                Arguments.of(0x2D, 0, RegisterType.L, true, true, false),
                Arguments.of(0x2D, 0x10, RegisterType.L, true, true, false),
                Arguments.of(0x2D, 0x0E, RegisterType.L, true, false, false),
                Arguments.of(0x2D, 0x01, RegisterType.L, true, false, true));
    }
    @SuppressWarnings({"checkstyle:magicnumber", "checkstyle:parameternumbercheck"})
    static Stream<Arguments> generateTestArgumentsFor16BitTests() {
        return Stream.of(
                Arguments.of(0x0B, TestUtils.getRandomIntegerInRange(0x00, 0xFFFF), RegisterType.BC),
                Arguments.of(0x1B, TestUtils.getRandomIntegerInRange(0x00, 0xFFFF), RegisterType.DE),
                Arguments.of(0x2B, TestUtils.getRandomIntegerInRange(0x00, 0xFFFF), RegisterType.HL));
    }
    @SuppressWarnings({"checkstyle:magicnumber", "checkstyle:parameternumbercheck"})
    private void executeDecrementTest(final int registerData, final RegisterType register, final boolean is16Bit) {
        int expectedValue;
        RegisterType[] filteredRegister;
        int expectedCycles;
        if (is16Bit) {
            expectedValue = (registerData - 1) & 0xFFFF;
            filteredRegister = TestUtils.getPairForRegister(register);
            expectedCycles = 2;
        } else {
            expectedValue = (registerData - 1) & 0xFF;
            filteredRegister = TestUtils.getPairForRegister(register, RegisterType.F);
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
        registerValues.computeIfPresent(RegisterType.PC, (t, u) -> u + 1);

        assertEquals(registerValues, newRegisterValues, "CPU Register values did not match the previous state.");
    }
}
