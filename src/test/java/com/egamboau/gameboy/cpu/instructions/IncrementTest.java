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

class IncrementTest  extends CPUTestBase{

    @ParameterizedTest
    @MethodSource("generateTestArgumentsFor8BitTests")
    void testDecInstructionFor8BitRegisters(int opcode, int registerData, RegisterType register, boolean expectedSubstractFlag, boolean expectedHalfCarryFlag, boolean expctedZeroFlag) {
        when(this.getCurrentBus().readByteFromAddress(anyInt())).thenReturn(opcode);
        executeIncTestWithSingleRegister(registerData, register);

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

        executeIncrementTest(registerData, register);
    }
    
    static Stream<Arguments> generateTestArgumentsFor8BitTests() {
        return Stream.of(
            Arguments.of(0x04, 0, RegisterType.B, false, false, false),
            Arguments.of(0x04, 0x10, RegisterType.B, false, false, false),
            Arguments.of(0x04, 0x0E, RegisterType.B, false, false, false),
            Arguments.of(0x04, 0xFF, RegisterType.B, false, true, true),

            Arguments.of(0x0C, 0, RegisterType.C, false, false, false),
            Arguments.of(0x0C, 0x10, RegisterType.C, false, false, false),
            Arguments.of(0x0C, 0x0E, RegisterType.C, false, false, false),
            Arguments.of(0x0C, 0xFF, RegisterType.C, false, true, true),

            Arguments.of(0x14, 0, RegisterType.D, false, false, false),
            Arguments.of(0x14, 0x10, RegisterType.D, false, false, false),
            Arguments.of(0x14, 0x0E, RegisterType.D, false, false, false),
            Arguments.of(0x14, 0xFF, RegisterType.D, false, true, true),

            Arguments.of(0x1C, 0, RegisterType.E, false, false, false),
            Arguments.of(0x1C, 0x10, RegisterType.E, false, false, false),
            Arguments.of(0x1C, 0x0E, RegisterType.E, false, false, false),
            Arguments.of(0x1C, 0xFF, RegisterType.E, false, true, true),

            Arguments.of(0x24, 0, RegisterType.H, false, false, false),
            Arguments.of(0x24, 0x10, RegisterType.H, false, false, false),
            Arguments.of(0x24, 0x0E, RegisterType.H, false, false, false),
            Arguments.of(0x24, 0xFF, RegisterType.H, false, true, true),

            Arguments.of(0x2C, 0, RegisterType.L, false, false, false),
            Arguments.of(0x2C, 0x10, RegisterType.L, false, false, false),
            Arguments.of(0x2C, 0x0E, RegisterType.L, false, false, false),
            Arguments.of(0x2C, 0xFF, RegisterType.L, false, true, true)
            
            
        );
    }

    static Stream<Arguments> generateTestArgumentsFor16BitTests() {
        return Stream.of(
            Arguments.of(0x03, TestUtils.getRandomIntegerInRange(0x00, 0xFFFF), RegisterType.BC),
            Arguments.of(0x13, TestUtils.getRandomIntegerInRange(0x00, 0xFFFF), RegisterType.DE),
            Arguments.of(0x23, TestUtils.getRandomIntegerInRange(0x00, 0xFFFF), RegisterType.HL)
        );
    }

    
    private void executeIncrementTest(int registerData, RegisterType register) {
        int expected = (registerData + 1) & 0xFFFF;
        this.getCurrentCpu().setValueInRegister(registerData, register);

        RegisterType[] filter = TestUtils.getPairForRegister(register, RegisterType.F);
        Map<RegisterType, Integer> registerValues = this.getCpuRegisters(filter);
        long previousCycleCount = getCurrentCpu().getCycles();
        this.getCurrentCpu().cpuStep();
        long currentCycleCount = getCurrentCpu().getCycles();
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisters(filter);

        //verify the increment
        int result = getCurrentCpu().getValueFromRegister(register);
        
        assertEquals(previousCycleCount+2, currentCycleCount, "Register value incorrect: " + register);
        assertEquals(expected, result, "Cycle count not currently matching.");
        registerValues.computeIfPresent(RegisterType.PC, (t, u) -> u+1);

        assertEquals(registerValues, newRegisterValues,"CPU Register values did not match the previous state.");
    }

    private void executeIncTestWithSingleRegister(int registerData, RegisterType registerType) {
        this.getCurrentCpu().setValueInRegister(registerData, registerType);
        Map<RegisterType, Integer> registerValues = this.getCpuRegisters(TestUtils.getPairForRegister(registerType, RegisterType.F));
        this.getCurrentCpu().cpuStep();
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisters(TestUtils.getPairForRegister(registerType, RegisterType.F));
        int expectedValue=(registerData + 1) & 0xFF;
        assertEquals(expectedValue, this.getCurrentCpu().getValueFromRegister(registerType));
        registerValues.computeIfPresent(RegisterType.PC, (t, u) -> u+1);

        assertEquals(registerValues, newRegisterValues);
    }

}
