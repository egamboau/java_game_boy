package com.egamboau.gameboy.cpu.instructions.implementations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.egamboau.gameboy.cpu.CPUTestBase;
import com.egamboau.gameboy.cpu.instructions.RegisterType;
import com.egamboau.test.TestUtils;

class JumpTest extends CPUTestBase {

    /** Opcode for an unconditional relative jump. */
    private static final int OPCODE_JR = 0x18;
    /** Opcode for a relative jump when the zero flag is not set. */
    private static final int OPCODE_JR_NZ = 0x20;
    /** Opcode for a relative jump when the zero flag is set. */
    private static final int OPCODE_JR_Z = 0x28;
    /** Opcode for a relative jump when the carry flag is not set. */
    private static final int OPCODE_JR_NC = 0x30;
    /** Opcode for a relative jump when the carry flag is set. */
    private static final int OPCODE_JR_C = 0x38;
    private static final int OPCODE_JP = 0xC3;
    private static final int OPCODE_JP_HL = 0xE9;
    private static final int OPCODE_JP_NZ = 0xC2;
    private static final int OPCODE_JP_Z = 0xCA;
    private static final int OPCODE_JP_NC = 0xD2;
    private static final int OPCODE_JP_C = 0xDA;
    private static final int JUMP_ADDRESS = 0x1234;

    /** Cycle count for an unconditional relative jump. */
    private static final int CYCLES_JR = 3;
    /** Cycle count for a conditional relative jump when its condition is met. */
    private static final int CYCLES_CONDITIONAL_TAKEN = 3;
    /** Cycle count for a conditional relative jump when its condition is not met. */
    private static final int CYCLES_CONDITIONAL_NOT_TAKEN = 2;
    private static final int CYCLES_JP_TAKEN = 4;
    private static final int CYCLES_JP_NOT_TAKEN = 3;
    private static final int CYCLES_JP_HL = 1;
    /** Size of a relative jump instruction in bytes. */
    private static final int INSTRUCTION_SIZE = 2;
    private static final int JP_INSTRUCTION_SIZE = 3;

    @SuppressWarnings({"checkstyle:magicnumber", "checkstyle:parameternumbercheck"})
    static Stream<Arguments> generateJrTestArguments() {
        return Stream.of(
            Arguments.of(OPCODE_JR, TestUtils.getRandomIntegerInRange(1, 127) & 0xFF),
            Arguments.of(OPCODE_JR, TestUtils.getRandomIntegerInRange(-127, -1) & 0xFF)
        );
    }

    @SuppressWarnings({"checkstyle:magicnumber", "checkstyle:parameternumbercheck"})
    static Stream<Arguments> generateJrNzTestArguments() {
        return Stream.of(
            Arguments.of(OPCODE_JR_NZ, TestUtils.getRandomIntegerInRange(1, 127) & 0xFF, false, CYCLES_CONDITIONAL_TAKEN),
            Arguments.of(OPCODE_JR_NZ, TestUtils.getRandomIntegerInRange(-127, -1) & 0xFF, true, CYCLES_CONDITIONAL_NOT_TAKEN),
            Arguments.of(OPCODE_JR_NZ, TestUtils.getRandomIntegerInRange(1, 127) & 0xFF, false, CYCLES_CONDITIONAL_TAKEN),
            Arguments.of(OPCODE_JR_NZ, TestUtils.getRandomIntegerInRange(-127, -1) & 0xFF, true, CYCLES_CONDITIONAL_NOT_TAKEN)
        );
    }

    @SuppressWarnings({"checkstyle:magicnumber", "checkstyle:parameternumbercheck"})
    static Stream<Arguments> generateJrZTestArguments() {
        return Stream.of(
            Arguments.of(OPCODE_JR_Z, TestUtils.getRandomIntegerInRange(1, 127) & 0xFF, false, CYCLES_CONDITIONAL_NOT_TAKEN),
            Arguments.of(OPCODE_JR_Z, TestUtils.getRandomIntegerInRange(-127, -1) & 0xFF, true, CYCLES_CONDITIONAL_TAKEN),
            Arguments.of(OPCODE_JR_Z, TestUtils.getRandomIntegerInRange(1, 127) & 0xFF, false, CYCLES_CONDITIONAL_NOT_TAKEN),
            Arguments.of(OPCODE_JR_Z, TestUtils.getRandomIntegerInRange(-127, -1) & 0xFF, true, CYCLES_CONDITIONAL_TAKEN)
        );
    }

    @SuppressWarnings({"checkstyle:magicnumber", "checkstyle:parameternumbercheck"})
    static Stream<Arguments> generateJrNcTestArguments() {
        return Stream.of(
            Arguments.of(OPCODE_JR_NC, TestUtils.getRandomIntegerInRange(1, 127) & 0xFF, false, CYCLES_CONDITIONAL_TAKEN),
            Arguments.of(OPCODE_JR_NC, TestUtils.getRandomIntegerInRange(-127, -1) & 0xFF, true, CYCLES_CONDITIONAL_NOT_TAKEN),
            Arguments.of(OPCODE_JR_NC, TestUtils.getRandomIntegerInRange(1, 127) & 0xFF, false, CYCLES_CONDITIONAL_TAKEN),
            Arguments.of(OPCODE_JR_NC, TestUtils.getRandomIntegerInRange(-127, -1) & 0xFF, true, CYCLES_CONDITIONAL_NOT_TAKEN)
        );
    }

    @SuppressWarnings({"checkstyle:magicnumber", "checkstyle:parameternumbercheck"})
    static Stream<Arguments> generateJrcTestArguments() {
        return Stream.of(
            Arguments.of(OPCODE_JR_C, TestUtils.getRandomIntegerInRange(1, 127) & 0xFF, false, CYCLES_CONDITIONAL_NOT_TAKEN),
            Arguments.of(OPCODE_JR_C, TestUtils.getRandomIntegerInRange(-127, -1) & 0xFF, true, CYCLES_CONDITIONAL_TAKEN),
            Arguments.of(OPCODE_JR_C, TestUtils.getRandomIntegerInRange(1, 127) & 0xFF, false, CYCLES_CONDITIONAL_NOT_TAKEN),
            Arguments.of(OPCODE_JR_C, TestUtils.getRandomIntegerInRange(-127, -1) & 0xFF, true, CYCLES_CONDITIONAL_TAKEN)
        );
    }

    static Stream<Arguments> generateJpTestArguments() {
        return Stream.of(
            Arguments.of(OPCODE_JP_NZ, false, false, true),
            Arguments.of(OPCODE_JP_NZ, true, false, false),
            Arguments.of(OPCODE_JP_Z, false, false, false),
            Arguments.of(OPCODE_JP_Z, true, false, true),
            Arguments.of(OPCODE_JP_NC, false, false, true),
            Arguments.of(OPCODE_JP_NC, false, true, false),
            Arguments.of(OPCODE_JP_C, false, false, false),
            Arguments.of(OPCODE_JP_C, false, true, true)
        );
    }

    @ParameterizedTest(name = "{index}: JR offset {1}")
    @MethodSource("generateJrTestArguments")
    @SuppressWarnings({"checkstyle:magicnumber", "checkstyle:parameternumbercheck"})
    void testJR(final int opcode, final int offset) {
        stubNextInstructions(opcode, offset);

        JumpTestContext context = executeJumpInstruction();

        context.expectedPcOffset = INSTRUCTION_SIZE + (byte) offset;
        context.expectedCycles = CYCLES_JR;
        verifyJumpResult(context);
    }

    @ParameterizedTest(name = "{index}: JR NZ offset {1}, Z={2}")
    @MethodSource("generateJrNzTestArguments")
    void testJRNZ(final int opcode, final int offset, final boolean zeroFlagStatus, final int cycleCountOffset) {
        stubNextInstructions(opcode, offset);
        runConditionalFlagJumpTest(zeroFlagStatus, !zeroFlagStatus, cycleCountOffset, offset,
                () -> this.getCurrentCpu().setZero(zeroFlagStatus));
    }

    @ParameterizedTest(name = "{index}: JR Z offset {1}, Z={2}")
    @MethodSource("generateJrZTestArguments")
    void testJRZ(final int opcode, final int offset, final boolean zeroFlagStatus, final int cycleCountOffset) {
        stubNextInstructions(opcode, offset);
        runConditionalFlagJumpTest(zeroFlagStatus, zeroFlagStatus, cycleCountOffset, offset,
                () -> this.getCurrentCpu().setZero(zeroFlagStatus));
    }

    @ParameterizedTest(name = "{index}: JR NC offset {1}, C={2}")
    @MethodSource("generateJrNcTestArguments")
    void testJRNC(final int opcode, final int offset, final boolean carryFlagStatus, final int cycleCountOffset) {
        stubNextInstructions(opcode, offset);
        runConditionalFlagJumpTest(carryFlagStatus, !carryFlagStatus, cycleCountOffset, offset,
                () -> this.getCurrentCpu().setCarry(carryFlagStatus));
    }

    @ParameterizedTest(name = "{index}: JR C offset {1}, C={2}")
    @MethodSource("generateJrcTestArguments")
    void testJRC(final int opcode, final int offset, final boolean carryFlagStatus, final int cycleCountOffset) {
        stubNextInstructions(opcode, offset);
        runConditionalFlagJumpTest(carryFlagStatus, carryFlagStatus, cycleCountOffset, offset,
                () -> this.getCurrentCpu().setCarry(carryFlagStatus));
    }

    @ParameterizedTest(name = "{index}: JP opcode {0}, Z={1}, C={2}")
    @MethodSource("generateJpTestArguments")
    @SuppressWarnings("checkstyle:magicnumber")
    void conditionalJpUsesFlag(final int opcode, final boolean zero, final boolean carry, final boolean taken) {
        when(getCurrentBus().readByteFromAddress(anyInt()))
            .thenReturn(opcode, JUMP_ADDRESS & MASK_INT_8_BIT, JUMP_ADDRESS >> 8);
        getCurrentCpu().setZero(zero);
        getCurrentCpu().setCarry(carry);
        JumpTestContext context = executeJumpInstruction();

        context.expectedPcOffset = taken ? JUMP_ADDRESS : JP_INSTRUCTION_SIZE;
        context.expectedCycles = taken ? CYCLES_JP_TAKEN : CYCLES_JP_NOT_TAKEN;
        verifyJumpResult(context);
    }

    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    void jpLoadsPcFromImmediateAddress() {
        when(getCurrentBus().readByteFromAddress(anyInt()))
            .thenReturn(OPCODE_JP, JUMP_ADDRESS & MASK_INT_8_BIT, JUMP_ADDRESS >> 8);

        JumpTestContext context = executeJumpInstruction();

        context.expectedPcOffset = JUMP_ADDRESS;
        context.expectedCycles = CYCLES_JP_TAKEN;
        verifyJumpResult(context);
    }

    @Test
    void jpHlLoadsPcFromHl() {
        when(getCurrentBus().readByteFromAddress(anyInt())).thenReturn(OPCODE_JP_HL);
        getCurrentCpu().setValueInRegister(JUMP_ADDRESS, RegisterType.HL);

        JumpTestContext context = executeJumpInstruction();

        context.expectedPcOffset = JUMP_ADDRESS;
        context.expectedCycles = CYCLES_JP_HL;
        verifyJumpResult(context);
    }

    // Helper methods

    /**
     * Stubs the next opcode and offset bytes to be read from the bus.
     *
     * @param opcode the relative jump opcode
     * @param offset the signed relative jump offset encoded as an unsigned byte
     */
    private void stubNextInstructions(final int opcode, final int offset) {
        when(this.getCurrentBus().readByteFromAddress(anyInt())).thenReturn(opcode, offset);
    }

    /**
     * Executes a single CPU step and captures register and cycle state.
     *
     * @return the CPU state captured before and after the instruction
     */
    private JumpTestContext executeJumpInstruction() {
        JumpTestContext context = new JumpTestContext();
        context.oldRegisterValues = this.getCpuRegisters();
        context.previousCycleCount = getCurrentCpu().getCycles();
        this.getCurrentCpu().cpuStep();
        context.currentCycleCount = getCurrentCpu().getCycles();
        context.newRegisterValues = this.getCpuRegisters();
        return context;
    }

    /**
     * Verifies that the jump result matches expected PC offset and cycle count.
     *
     * @param context the captured and expected jump state
     */
    private void verifyJumpResult(final JumpTestContext context) {
        context.oldRegisterValues.computeIfPresent(RegisterType.PC,
                (t, u) -> (u + context.expectedPcOffset) & MASK_INT_16_BIT);
        assertEquals(context.previousCycleCount + context.expectedCycles, context.currentCycleCount);
        assertEquals(context.oldRegisterValues, context.newRegisterValues);
    }

    /**
     * Runs a conditional flag-based jump test with the given flag setter and condition.
     *
     * @param flagStatus the flag value to set before executing the instruction
     * @param jumpCondition whether the jump is expected to be taken
     * @param cycleCountOffset the expected number of cycles consumed
     * @param offset the signed relative jump offset encoded as an unsigned byte
     * @param flagSetter the operation that sets the tested CPU flag
     */
    @SuppressWarnings("checkstyle:parameternumbercheck")
    private void runConditionalFlagJumpTest(final boolean flagStatus, final boolean jumpCondition,
            final int cycleCountOffset, final int offset, final Runnable flagSetter) {
        flagSetter.run();
        JumpTestContext context = executeJumpInstruction();

        if (jumpCondition) {
            context.expectedPcOffset = INSTRUCTION_SIZE + (byte) offset;
        } else {
            context.expectedPcOffset = INSTRUCTION_SIZE;
        }
        context.expectedCycles = cycleCountOffset;
        verifyJumpResult(context);
    }

    /**
     * Helper class to encapsulate jump test state.
     */
    private static final class JumpTestContext {
        /** Register values captured before executing the instruction. */
        private Map<RegisterType, Integer> oldRegisterValues;
        /** Register values captured after executing the instruction. */
        private Map<RegisterType, Integer> newRegisterValues;
        /** CPU cycle count before executing the instruction. */
        private long previousCycleCount;
        /** CPU cycle count after executing the instruction. */
        private long currentCycleCount;
        /** Expected change to the program counter. */
        private int expectedPcOffset;
        /** Expected number of cycles consumed by the instruction. */
        private int expectedCycles;
    }
}
