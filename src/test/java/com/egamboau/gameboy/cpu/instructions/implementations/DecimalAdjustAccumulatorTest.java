package com.egamboau.gameboy.cpu.instructions.implementations;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.egamboau.gameboy.cpu.CPUTestBase;
import com.egamboau.gameboy.cpu.instructions.RegisterType;
import com.egamboau.test.TestUtils;
import com.egamboau.test.CpuSnapshot;

class DecimalAdjustAccumulatorTest extends CPUTestBase {

    /** Opcode for the decimal adjust accumulator instruction. */
    private static final int DAA_OPCODE = 0x27;

    @ParameterizedTest(name = "{index}: A={0} -> {1}, N={2}, H={3}, C={4}")
    @MethodSource("generateArgumentsForTests")
    @SuppressWarnings({"checkstyle:magicnumber", "checkstyle:parameternumber"})
    void testDecimalAdjustAccumulatorInstruction(final int registerValue, final int expectedValue,
            final boolean originalSubtract, final boolean originalHalfCarry, final boolean originalCarry,
            final boolean expectedZeroValue, final boolean expectedHalfCarryValue,
            final boolean expectedCarryValue) {
        prepareCpuForDecimalAdjustAccumulator(registerValue, originalSubtract, originalHalfCarry, originalCarry);
        stubOpcode(DAA_OPCODE);

        CpuSnapshot before = captureCurrentCpuState();
        executeNextInstruction();
        CpuSnapshot after = captureCurrentCpuState();

        assertEquals(before.cycles() + 1, after.cycles(), "Cycle count not currently matching.");
        assertExpectedRegistersAfterStep(before.registers(), after.registers());
        assertEquals(expectedValue, getCurrentCpu().getValueFromRegister(RegisterType.A),
                "Value on Register A did not match the expected value.");
        assertExpectedFlags(expectedZeroValue, expectedHalfCarryValue, expectedCarryValue);
    }

    private void prepareCpuForDecimalAdjustAccumulator(final int registerValue, final boolean subtract,
            final boolean halfCarry, final boolean carry) {
        getCurrentCpu().setSubtract(subtract);
        getCurrentCpu().setHalfCarry(halfCarry);
        getCurrentCpu().setCarry(carry);
        getCurrentCpu().setValueInRegister(registerValue, RegisterType.A);
    }

    private CpuSnapshot captureCurrentCpuState() {
        return new CpuSnapshot(getCpuRegisters(TestUtils.getPairForRegister(RegisterType.AF)), getCurrentCpu().getCycles());
    }

    private void executeNextInstruction() {
        getCurrentCpu().cpuStep();
    }

    private void assertExpectedRegistersAfterStep(final Map<RegisterType, Integer> before,
            final Map<RegisterType, Integer> after) {
        Map<RegisterType, Integer> expectedRegisters = new HashMap<>(before);
        expectedRegisters.computeIfPresent(RegisterType.PC, (register, value) -> value + 1);
        assertEquals(expectedRegisters, after, "CPU Register values did not match the previous state.");
    }

    private void assertExpectedFlags(final boolean expectedZeroValue, final boolean expectedHalfCarryValue,
            final boolean expectedCarryValue) {
        assertEquals(expectedZeroValue, getCurrentCpu().getZero(), "Zero flag set incorrectly");
        assertEquals(expectedHalfCarryValue, getCurrentCpu().getHalfCarry(), "Half carry flag set incorrectly");
        assertEquals(expectedCarryValue, getCurrentCpu().getCarry(), "Carry flag set incorrectly");
    }


    @SuppressWarnings({"checkstyle:magicnumber", "checkstyle:parameternumbercheck"})
    static Stream<Arguments> generateArgumentsForTests() {
        return Stream.of(
            Arguments.of(0x0A, 0x10, false, false, false, false, false, false),
            Arguments.of(0x9A, 0x00, false, false, false, true, false, true),
            Arguments.of(0x12, 0x18, false, true, false, false, false, false),
            Arguments.of(0x35, 0x95, false, false, true, false, false, true),
            Arguments.of(0x31, 0x97, false, true, true, false, false, true),
            Arguments.of(0x42, 0x42, true, false, false, false, false, false),
            Arguments.of(0x45, 0x3F, true, true, false, false, false, false),
            Arguments.of(0x85, 0x25, true, false, true, false, false, true),
            Arguments.of(0xE5, 0x7F, true, true, true, false, false, true)
        );
    }
}
