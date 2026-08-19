package com.egamboau.gameboy.cpu.instructions.implementations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.egamboau.gameboy.cpu.CPUTestBase;
import com.egamboau.gameboy.cpu.instructions.RegisterType;
import com.egamboau.test.CpuSnapshot;
import com.egamboau.test.TestUtils;

class DecrementTest extends CPUTestBase {

    /** Number of cycles used for decrementing indirect memory through HL. */
    private static final int INDIRECT_MEMORY_DECREMENT_CYCLES = 3;
    /** Number of cycles used for decrementing a 16-bit register pair. */
    private static final int SIXTEEN_BIT_DECREMENT_CYCLES = 2;
    /** Number of cycles used for decrementing an 8-bit register. */
    private static final int EIGHT_BIT_DECREMENT_CYCLES = 1;

    @ParameterizedTest(name = "{index}: opcode {0}, DEC {2} from {1}")
    @MethodSource("generateTestArgumentsFor8BitTests")
    void testDecInstructionFor8BitRegisters(final int opcode, final int registerData, final RegisterType register,
            final boolean expectedSubtractFlag, final boolean expectedHalfCarryFlag, final boolean expectedZeroFlag) {
        stubOpcode(opcode);
        executeDecrementTest(registerData, register, false);
        assertExpectedFlags(expectedSubtractFlag, expectedHalfCarryFlag, expectedZeroFlag);
    }

    @ParameterizedTest(name = "{index}: opcode {0}, DEC {2} from {1}")
    @MethodSource("generateTestArgumentsFor16BitTests")
    void testDecInstructionFor16BitRegisters(final int opcode, final int registerData,
            final RegisterType register) {
        stubOpcode(opcode);
        executeDecrementTest(registerData, register, true);
    }

    @ParameterizedTest(name = "{index}: opcode {0}, DEC ({2}) from {1}")
    @SuppressWarnings("checkstyle:magicnumber")
    @MethodSource("generateTestArgumentsForIndirectDec")
    void testIndirectDecInstructionFor16BitRegisters(final int opcode, final int memoryData,
            final RegisterType register, final boolean expectedSubtractFlag, final boolean expectedHalfCarryFlag,
            final boolean expectedZeroFlag) {
        int registerData = TestUtils.getRandomIntegerInRange(0, MASK_INT_16_BIT) & MASK_INT_16_BIT;
        stubOpcode(opcode);
        when(getCurrentBus().readByteFromAddress(registerData)).thenReturn(memoryData);
        executeIndirectDecTest(registerData, register, memoryData, expectedZeroFlag, expectedSubtractFlag,
                expectedHalfCarryFlag);
    }

    @SuppressWarnings({"checkstyle:magicnumber", "checkstyle:parameternumbercheck"})
    static Stream<Arguments> generateTestArgumentsForIndirectDec() {
        return Stream.of(
                Arguments.of(0x35, 0, RegisterType.HL, true, true, false),
                Arguments.of(0x35, 0x10, RegisterType.HL, true, true, false),
                Arguments.of(0x35, 0x0E, RegisterType.HL, true, false, false),
                Arguments.of(0x35, 0x01, RegisterType.HL, true, false, true)
        );
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
                Arguments.of(0x2D, 0x01, RegisterType.L, true, false, true),

                Arguments.of(0x3D, 0, RegisterType.A, true, true, false),
                Arguments.of(0x3D, 0x10, RegisterType.A, true, true, false),
                Arguments.of(0x3D, 0x0E, RegisterType.A, true, false, false),
                Arguments.of(0x3D, 0x01, RegisterType.A, true, false, true)
            );
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
        int expectedValue = calculateExpectedValue(registerData, is16Bit);
        RegisterType[] filteredRegister = determineFilteredRegister(register, is16Bit);
        int expectedCycles = determineExpectedCycles(is16Bit);

        getCurrentCpu().setValueInRegister(registerData, register);
        CpuSnapshot before = captureCpuState(filteredRegister);
        getCurrentCpu().cpuStep();
        CpuSnapshot after = captureCpuState(filteredRegister);

        assertEquals(expectedValue, getCurrentCpu().getValueFromRegister(register), "Register value incorrect: " + register);
        assertEquals(before.cycles() + expectedCycles, after.cycles(), "Cycle count not currently matching.");
        assertRegistersAdvancedPcByOne(before.registers(), after.registers());
    }

    @SuppressWarnings({"checkstyle:magicnumber", "checkstyle:parameternumbercheck"})
    private void executeIndirectDecTest(final int registerData, final RegisterType registerType,
            final int memoryData, final boolean expectedZeroFlag, final boolean expectedSubtractFlag,
            final boolean expectedHalfCarryFlag) {

        getCurrentCpu().setValueInRegister(registerData, registerType);
        CpuSnapshot before = captureCpuState(TestUtils.getPairForRegister(registerType, RegisterType.F));
        getCurrentCpu().cpuStep();
        CpuSnapshot after = captureCpuState(TestUtils.getPairForRegister(registerType, RegisterType.F));

        assertRegistersAdvancedPcByOne(before.registers(), after.registers());
        assertEquals(before.cycles() + INDIRECT_MEMORY_DECREMENT_CYCLES, after.cycles(), "Cycle count not currently matching.");
        assertExpectedFlags(expectedSubtractFlag, expectedHalfCarryFlag, expectedZeroFlag);
        verify(getCurrentBus(), times(1)).writeByteToAddress(memoryData - 1, registerData);
    }

    private void assertExpectedFlags(final boolean expectedSubtractFlag, final boolean expectedHalfCarryFlag,
            final boolean expectedZeroFlag) {
        assertEquals(expectedSubtractFlag, getCurrentCpu().getSubtract(), "Subtract flag set incorrectly");
        assertEquals(expectedHalfCarryFlag, getCurrentCpu().getHalfCarry(), "Half Carry flag set incorrectly");
        assertEquals(expectedZeroFlag, getCurrentCpu().getZero(), "Zero flag set incorrectly");
    }

    private CpuSnapshot captureCpuState(final RegisterType... excludedRegisters) {
        return new CpuSnapshot(getCpuRegisters(excludedRegisters), getCurrentCpu().getCycles());
    }

    private void assertRegistersAdvancedPcByOne(final Map<RegisterType, Integer> before,
            final Map<RegisterType, Integer> after) {
        Map<RegisterType, Integer> expectedRegisters = new java.util.HashMap<>(before);
        expectedRegisters.computeIfPresent(RegisterType.PC, (register, value) -> value + 1);
        assertEquals(expectedRegisters, after, "CPU Register values did not match the previous state.");
    }

    private int calculateExpectedValue(final int registerData, final boolean is16Bit) {
        return is16Bit ? (registerData - 1) & MASK_INT_16_BIT : (registerData - 1) & MASK_INT_8_BIT;
    }

    private RegisterType[] determineFilteredRegister(final RegisterType register, final boolean is16Bit) {
        return is16Bit ? TestUtils.getPairForRegister(register)
                : TestUtils.getPairForRegister(register, RegisterType.F);
    }

    private int determineExpectedCycles(final boolean is16Bit) {
        return is16Bit ? SIXTEEN_BIT_DECREMENT_CYCLES : EIGHT_BIT_DECREMENT_CYCLES;
    }
}
