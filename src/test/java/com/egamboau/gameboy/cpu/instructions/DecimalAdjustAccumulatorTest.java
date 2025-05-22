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

class DecimalAdjustAccumulatorTest extends CPUTestBase{

    @ParameterizedTest
    @MethodSource("generateArgumentsForTests")
    void testDInstruction(int registerValue, int expectedValue, boolean originalSubstract, boolean originalHalfCarry, boolean orignalCarry, boolean expectedZeroValue, boolean expectedHalfCarryValue, boolean expectedCarryValue) {
        this.getCurrentCpu().setSubtract(originalSubstract);
        this.getCurrentCpu().setHalfCarry(originalHalfCarry);
        this.getCurrentCpu().setCarry(orignalCarry);
        this.getCurrentCpu().setValueInRegister(registerValue, RegisterType.A);
        when(this.getCurrentBus().readByteFromAddress(anyInt())).thenReturn(
            0x27 //the opcode
        );

        Map<RegisterType, Integer> registerValues = this.getCpuRegisters(TestUtils.getPairForRegister(RegisterType.AF));
        long previousCycleCount = getCurrentCpu().getCycles();
        this.getCurrentCpu().cpuStep();
        long currentCycleCount = getCurrentCpu().getCycles();
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisters(TestUtils.getPairForRegister(RegisterType.AF));

        assertEquals(previousCycleCount + 1, currentCycleCount, "Cycle count not currently matching.");

        //other flags must be the same, and update the PC to be 1 byte more
        registerValues.computeIfPresent(RegisterType.PC, (t, u) -> u+1);
        assertEquals(registerValues, newRegisterValues, "CPU Register values did not match the previous state.");
        assertEquals(expectedValue, getCurrentCpu().getValueFromRegister(RegisterType.A), "Value on Register A did not match the expeced value.");

        assertEquals(expectedZeroValue, getCurrentCpu().getZero(), "Zero flag set incorrectly");
        assertEquals(expectedHalfCarryValue, getCurrentCpu().getHalfCarry(), "Half carry flag set incorrectly");
        assertEquals(expectedCarryValue, getCurrentCpu().getCarry(), "Carry flag set incorrectly");
    }

    static Stream<Arguments> generateArgumentsForTests(){
        return Stream.of(
            Arguments.of(0x0A, 0x10, false, false, false,  false, false, false),
            Arguments.of(0x9A, 0x00, false, false, false,  true, false, true),
            Arguments.of(0x12, 0x18, false, true, false,  false, false, false),
            Arguments.of(0x35, 0x95, false, false, true,  false, false, true),
            Arguments.of(0x31, 0x97, false, true, true,  false, false, true),
            Arguments.of(0x42, 0x42, true, false, false,  false, false, false),
            Arguments.of(0x45, 0x3F, true, true, false,  false, false, false),
            Arguments.of(0x85, 0x25, true, false, true,  false, false, true),
            Arguments.of(0xE5, 0x7F, true, true, true,  false, false, true)
        );
    }
}
