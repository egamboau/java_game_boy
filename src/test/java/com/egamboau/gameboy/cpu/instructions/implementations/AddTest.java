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

/**
 * Unit tests for ADD instructions implemented by the CPU.
 *
 * <p>
 * These tests verify register-to-register, register-to-memory (indirect), and
 * 16-bit register pair additions. Tests also assert CPU flag behavior where
 * applicable.
 * </p>
 */
class AddTest extends CPUTestBase {

    /**
     * Number of CPU cycles consumed by add instructions with an indirect source.
     */
    private static final int INDIRECT_MEMORY_CYCLE_INCREMENT = 2;
    /**
     * Number of CPU cycles consumed by 16-bit register pair addition instructions.
     */
    private static final int REGISTER_PAIR_CYCLE_INCREMENT = 2;
    /**
     * Number of CPU cycles consumed by single-register add instructions.
     */
    private static final int SINGLE_REGISTER_CYCLE_INCREMENT = 1;

    // region Test cases: basic add

    @ParameterizedTest(name = "{index}: opcode {0}, {1} -> {2}")
    @SuppressWarnings("checkstyle:magicnumber")
    @MethodSource("generateArgumentsForAdd")
    void testAddInstruction(final int opcode, final RegisterType sourceRegister,
            final RegisterType destinationRegister) {
        int sourceValue = TestUtils.getRandomIntegerInRange(0, MASK_INT_16_BIT) & MASK_INT_16_BIT;
        int destinationValue = sourceRegister == destinationRegister ? sourceValue
                : TestUtils.getRandomIntegerInRange(0, MASK_INT_16_BIT) & MASK_INT_16_BIT;

        stubOpcode(opcode);
        executeAddTest(sourceValue, destinationValue, sourceRegister, destinationRegister);
    }

    @ParameterizedTest(name = "{index}: opcode {0}, {1}={2}, {3}={4}")
    @SuppressWarnings({ "checkstyle:magicnumber", "checkstyle:parameternumber" })
    @MethodSource("generateArgumentsForAddWithFlags")
    void testAddInstructionWithFlagsCheck(
            final int opcode,
            final RegisterType sourceRegister,
            final int sourceValue,
            final RegisterType destinationRegister,
            final int destinationValue,
            final boolean expectedSubtractFlag,
            final boolean expectedHalfCarryFlag,
            final boolean expectedCarryFlag) {
        stubOpcode(opcode);

        executeAddTest(sourceValue, destinationValue, sourceRegister, destinationRegister);
        assertFlags(expectedSubtractFlag, expectedHalfCarryFlag, expectedCarryFlag, false);
    }

    @ParameterizedTest(name = "{index}: opcode {0}, {1}={2}, {3}={4}, Z={8}")
    @SuppressWarnings({ "checkstyle:magicnumber", "checkstyle:parameternumber" })
    @MethodSource("generateArgumentForAddWithFlagsIncludingZero")
    void testAddInstructionWithFlagsCheckOnSingleRegisters(
            final int opcode,
            final RegisterType sourceRegister,
            final int sourceValue,
            final RegisterType destinationRegister,
            final int destinationValue,
            final boolean expectedSubtractFlag,
            final boolean expectedHalfCarryFlag,
            final boolean expectedCarryFlag,
            final boolean expectedZeroFlag) {
        stubOpcode(opcode);

        executeAddTest(sourceValue, destinationValue, sourceRegister, destinationRegister);
        assertFlags(expectedSubtractFlag, expectedHalfCarryFlag, expectedCarryFlag, expectedZeroFlag);
    }

    @ParameterizedTest(name = "{index}: opcode {0}, ({1}) -> {2}")
    @SuppressWarnings({ "checkstyle:magicnumber", "checkstyle:parameternumbercheck" })
    @MethodSource("generateArgumentForAddIndirectRegister")
    void testAddInstructionWithIndirectSource(final int opcode, final RegisterType sourceRegister,
            final RegisterType destinationRegister) {
        int sourceValue = TestUtils.getRandomIntegerInRange(0, MASK_INT_16_BIT) & MASK_INT_16_BIT;
        int address = TestUtils.getRandomIntegerInRange(0, MASK_INT_16_BIT) & MASK_INT_16_BIT;
        int destinationValue = TestUtils.getRandomIntegerInRange(0, MASK_INT_16_BIT) & MASK_INT_16_BIT;

        getCurrentCpu().setValueInRegister(address, sourceRegister);
        stubOpcode(opcode);
        when(getCurrentBus().readByteFromAddress(address)).thenReturn(sourceValue);

        executeAddTestWithIndirectRegister(sourceValue, destinationValue, destinationRegister);
        verify(getCurrentBus(), times(1)).readByteFromAddress(address);
    }

    @ParameterizedTest(name = "{index}: opcode {0}, ({1})={2}, {3}={4}")
    @SuppressWarnings({ "checkstyle:magicnumber", "checkstyle:parameternumber" })
    @MethodSource("generateArgumentForAddWithFlagsIndirectRegister")
    void testAddInstructionWithIndirectSourceWithFlags(
            final int opcode,
            final RegisterType sourceRegister,
            final int sourceValue,
            final RegisterType destinationRegister,
            final int destinationValue,
            final boolean expectedSubtractFlag,
            final boolean expectedHalfCarryFlag,
            final boolean expectedCarryFlag,
            final boolean expectedZeroFlag) {
        int address = TestUtils.getRandomIntegerInRange(0, MASK_INT_16_BIT) & MASK_INT_16_BIT;

        getCurrentCpu().setValueInRegister(address, sourceRegister);
        stubOpcode(opcode);
        when(getCurrentBus().readByteFromAddress(address)).thenReturn(sourceValue);

        executeAddTestWithIndirectRegister(sourceValue, destinationValue, destinationRegister);
        verify(getCurrentBus(), times(1)).readByteFromAddress(address);
        assertFlags(expectedSubtractFlag, expectedHalfCarryFlag, expectedCarryFlag, expectedZeroFlag);
    }

    // endregion

    // region Helpers: execute tests

    private void executeAddTestWithIndirectRegister(final int sourceValue,
            final int destinationValue,
            final RegisterType destinationRegister) {
        int expectedValue = sourceValue + destinationValue;
        getCurrentCpu().setValueInRegister(destinationValue, destinationRegister);

        CpuSnapshot before = captureCurrentCpuState(destinationRegister);
        getCurrentCpu().cpuStep();
        CpuSnapshot after = captureCurrentCpuState(destinationRegister);

        assertRegisterValue(destinationRegister, expectedValue, true);
        assertCycleCount(after.cycles(), before.cycles(), INDIRECT_MEMORY_CYCLE_INCREMENT);
        assertProgramCounterIncrementedByOne(before.registers(), after.registers());
    }

    @SuppressWarnings("checkstyle:magicnumber")
    private void executeAddTest(final int sourceValue, final int destinationValue,
            final RegisterType sourceRegister,
            final RegisterType destinationRegister) {
        int expectedValue = sourceValue + destinationValue;

        getCurrentCpu().setValueInRegister(sourceValue, sourceRegister);
        getCurrentCpu().setValueInRegister(destinationValue, destinationRegister);

        CpuSnapshot before = captureCurrentCpuState(destinationRegister);
        getCurrentCpu().cpuStep();
        CpuSnapshot after = captureCurrentCpuState(destinationRegister);

        boolean isSingleRegister = TestUtils.isSingleRegister(destinationRegister);
        assertRegisterValue(destinationRegister, expectedValue, isSingleRegister);
        assertCycleCount(after.cycles(), before.cycles(), isSingleRegister ? SINGLE_REGISTER_CYCLE_INCREMENT
                : REGISTER_PAIR_CYCLE_INCREMENT);
        assertProgramCounterIncrementedByOne(before.registers(), after.registers());
    }

    private void assertRegisterValue(final RegisterType destinationRegister,
            final int expectedValue,
            final boolean isSingleRegister) {
        int mask = isSingleRegister ? MASK_INT_8_BIT : MASK_INT_16_BIT;
        assertEquals(expectedValue & mask,
                getCurrentCpu().getValueFromRegister(destinationRegister),
                "Register value not matching the expected value: " + destinationRegister);
    }

    private void assertCycleCount(final long actualCycles,
            final long expectedCycles,
            final int increment) {
        assertEquals(expectedCycles + increment, actualCycles, "Cycle count not correctly matching.");
    }

    private void assertProgramCounterIncrementedByOne(final Map<RegisterType, Integer> before,
            final Map<RegisterType, Integer> after) {
        Map<RegisterType, Integer> expectedRegisters = Map.copyOf(before);
        expectedRegisters = this.addOneToProgramCounter(expectedRegisters);
        assertEquals(expectedRegisters, after, "CPU Register values did not match the previous state.");
    }

    private Map<RegisterType, Integer> addOneToProgramCounter(final Map<RegisterType, Integer> registers) {
        var updated = new java.util.HashMap<>(registers);
        updated.computeIfPresent(RegisterType.PC, (register, value) -> value + 1);
        return updated;
    }

    private void assertFlags(final boolean expectedSubtractFlag, final boolean expectedHalfCarryFlag,
            final boolean expectedCarryFlag, final boolean expectedZeroFlag) {
        assertEquals(expectedSubtractFlag, getCurrentCpu().getSubtract(), "Subtract flag set incorrectly");
        assertEquals(expectedHalfCarryFlag, getCurrentCpu().getHalfCarry(), "Half Carry flag set incorrectly");
        assertEquals(expectedCarryFlag, getCurrentCpu().getCarry(), "Carry flag set incorrectly");
        assertEquals(expectedZeroFlag, getCurrentCpu().getZero(), "Zero flag was set incorrectly");
    }

    private CpuSnapshot captureCurrentCpuState(final RegisterType destinationRegister) {
        return new CpuSnapshot(
                getCpuRegisters(TestUtils.getPairForRegister(destinationRegister, RegisterType.F)),
                getCurrentCpu().getCycles());
    }

    // endregion

    // region Argument providers

    @SuppressWarnings("checkstyle:magicnumber")
    static Stream<Arguments> generateArgumentsForAdd() {
        return Stream.of(
                Arguments.of(0x09, RegisterType.BC, RegisterType.HL),
                Arguments.of(0x19, RegisterType.DE, RegisterType.HL),
                Arguments.of(0x29, RegisterType.HL, RegisterType.HL),
                Arguments.of(0x39, RegisterType.SP, RegisterType.HL),
                Arguments.of(0x80, RegisterType.B, RegisterType.A),
                Arguments.of(0x81, RegisterType.C, RegisterType.A),
                Arguments.of(0x82, RegisterType.D, RegisterType.A),
                Arguments.of(0x83, RegisterType.E, RegisterType.A),
                Arguments.of(0x84, RegisterType.H, RegisterType.A),
                Arguments.of(0x85, RegisterType.L, RegisterType.A),
                Arguments.of(0x87, RegisterType.A, RegisterType.A));
    }

    @SuppressWarnings("checkstyle:magicnumber")
    static Stream<Arguments> generateArgumentForAddWithFlagsIncludingZero() {
        return Stream.of(
                Arguments.of(0x80, RegisterType.B, 0xFF, RegisterType.A, 0x01, false, true, true, true),
                Arguments.of(0x80, RegisterType.B, 0x01, RegisterType.A, 0x0F, false, true, false, false),
                Arguments.of(0x80, RegisterType.B, 0x01, RegisterType.A, 0x01, false, false, false, false),
                Arguments.of(0x81, RegisterType.C, 0xFF, RegisterType.A, 0x01, false, true, true, true),
                Arguments.of(0x81, RegisterType.C, 0x01, RegisterType.A, 0x0F, false, true, false, false),
                Arguments.of(0x81, RegisterType.C, 0x01, RegisterType.A, 0x01, false, false, false, false),
                Arguments.of(0x82, RegisterType.D, 0xFF, RegisterType.A, 0x01, false, true, true, true),
                Arguments.of(0x82, RegisterType.D, 0x01, RegisterType.A, 0x0F, false, true, false, false),
                Arguments.of(0x82, RegisterType.D, 0x01, RegisterType.A, 0x01, false, false, false, false),
                Arguments.of(0x83, RegisterType.E, 0xFF, RegisterType.A, 0x01, false, true, true, true),
                Arguments.of(0x83, RegisterType.E, 0x01, RegisterType.A, 0x0F, false, true, false, false),
                Arguments.of(0x83, RegisterType.E, 0x01, RegisterType.A, 0x01, false, false, false, false),
                Arguments.of(0x84, RegisterType.H, 0xFF, RegisterType.A, 0x01, false, true, true, true),
                Arguments.of(0x84, RegisterType.H, 0x01, RegisterType.A, 0x0F, false, true, false, false),
                Arguments.of(0x84, RegisterType.H, 0x01, RegisterType.A, 0x01, false, false, false, false),
                Arguments.of(0x85, RegisterType.L, 0xFF, RegisterType.A, 0x01, false, true, true, true),
                Arguments.of(0x85, RegisterType.L, 0x01, RegisterType.A, 0x0F, false, true, false, false),
                Arguments.of(0x85, RegisterType.L, 0x01, RegisterType.A, 0x01, false, false, false, false),
                Arguments.of(0x87, RegisterType.A, 0x80, RegisterType.A, 0x80, false, false, true, true),
                Arguments.of(0x87, RegisterType.A, 0x0F, RegisterType.A, 0x0F, false, true, false, false),
                Arguments.of(0x87, RegisterType.A, 0x01, RegisterType.A, 0x01, false, false, false, false));
    }

    @SuppressWarnings("checkstyle:magicnumber")
    static Stream<Arguments> generateArgumentsForAddWithFlags() {
        return Stream.of(
                Arguments.of(0x09, RegisterType.BC, 0xFFFF, RegisterType.HL, 0x0001, false, true, true),
                Arguments.of(0x09, RegisterType.BC, 0x0001, RegisterType.HL, 0x0FFF, false, true, false),
                Arguments.of(0x09, RegisterType.BC, 0x0001, RegisterType.HL, 0x0001, false, false, false),
                Arguments.of(0x19, RegisterType.DE, 0xFFFF, RegisterType.HL, 0x0001, false, true, true),
                Arguments.of(0x19, RegisterType.DE, 0x0001, RegisterType.HL, 0x0FFF, false, true, false),
                Arguments.of(0x19, RegisterType.DE, 0x0001, RegisterType.HL, 0x0001, false, false, false),
                Arguments.of(0x29, RegisterType.HL, 0xFFFF, RegisterType.HL, 0xFFFF, false, true, true),
                Arguments.of(0x29, RegisterType.HL, 0x0001, RegisterType.HL, 0x0001, false, false, false),
                Arguments.of(0x39, RegisterType.SP, 0xFFFF, RegisterType.HL, 0x0001, false, true, true),
                Arguments.of(0x39, RegisterType.SP, 0x0001, RegisterType.HL, 0x0FFF, false, true, false),
                Arguments.of(0x39, RegisterType.SP, 0x0001, RegisterType.HL, 0x0001, false, false, false));
    }

    @SuppressWarnings("checkstyle:magicnumber")
    static Stream<Arguments> generateArgumentForAddIndirectRegister() {
        return Stream.of(Arguments.of(0x86, RegisterType.HL, RegisterType.A));
    }

    @SuppressWarnings("checkstyle:magicnumber")
    static Stream<Arguments> generateArgumentForAddWithFlagsIndirectRegister() {
        return Stream.of(
                Arguments.of(0x86, RegisterType.HL, 0xFF, RegisterType.A, 0x01, false, true, true, true),
                Arguments.of(0x86, RegisterType.HL, 0x01, RegisterType.A, 0x0F, false, true, false, false),
                Arguments.of(0x86, RegisterType.HL, 0x01, RegisterType.A, 0x01, false, false, false, false));
    }

    // endregion
}
