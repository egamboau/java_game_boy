package com.egamboau.gameboy.cpu.instructions.implementations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.egamboau.gameboy.cpu.CPUTestBase;
import com.egamboau.gameboy.cpu.instructions.RegisterType;

/**
 * Unit tests for SUB and SBC instructions.
 */
class SubtractionTest extends CPUTestBase {

    /** Address used for indirect HL operands. */
    private static final int INDIRECT_SOURCE_ADDRESS = 0xC000;

    /**
     * Verifies subtraction results, flags, cycles, and operand access.
     *
     * @param instruction opcode and source register
     * @param scenario input and expected output values
     */
    @ParameterizedTest(name = "{index}: {0}, {1}")
    @MethodSource("generateSubtractionArguments")
    void testSubtraction(final InstructionCase instruction, final SubtractionScenario scenario) {
        getCurrentCpu().setValueInRegister(scenario.accumulator(), RegisterType.A);
        if (instruction.sourceRegister() == RegisterType.HL) {
            getCurrentCpu().setValueInRegister(INDIRECT_SOURCE_ADDRESS, RegisterType.HL);
        } else if (instruction.sourceRegister() != RegisterType.A) {
            getCurrentCpu().setValueInRegister(scenario.source(), instruction.sourceRegister());
        }
        getCurrentCpu().setCarry(scenario.carryIn());
        long cyclesBefore = getCurrentCpu().getCycles();
        int programCounterBefore = getCurrentCpu().getValueFromRegister(RegisterType.PC);
        stubOpcode(instruction.opcode());
        if (instruction.sourceRegister() == RegisterType.HL) {
            when(getCurrentBus().readByteFromAddress(INDIRECT_SOURCE_ADDRESS)).thenReturn(scenario.source());
        }

        getCurrentCpu().cpuStep();

        assertEquals(scenario.result(), getCurrentCpu().getValueFromRegister(RegisterType.A));
        assertEquals(programCounterBefore + 1, getCurrentCpu().getValueFromRegister(RegisterType.PC));
        assertEquals(cyclesBefore + instruction.cycles(), getCurrentCpu().getCycles());
        assertEquals(scenario.zero(), getCurrentCpu().getZero());
        assertEquals(true, getCurrentCpu().getSubtract());
        assertEquals(scenario.halfCarry(), getCurrentCpu().getHalfCarry());
        assertEquals(scenario.carry(), getCurrentCpu().getCarry());
        if (instruction.sourceRegister() == RegisterType.HL) {
            verify(getCurrentBus()).readByteFromAddress(INDIRECT_SOURCE_ADDRESS);
        }
    }

    @SuppressWarnings("checkstyle:magicnumber")
    static Stream<Arguments> generateSubtractionArguments() {
        return Stream.of(
                new InstructionCase(0x90, RegisterType.B, false),
                new InstructionCase(0x91, RegisterType.C, false),
                new InstructionCase(0x92, RegisterType.D, false),
                new InstructionCase(0x93, RegisterType.E, false),
                new InstructionCase(0x94, RegisterType.H, false),
                new InstructionCase(0x95, RegisterType.L, false),
                new InstructionCase(0x96, RegisterType.HL, false),
                new InstructionCase(0x97, RegisterType.A, false),
                new InstructionCase(0x98, RegisterType.B, true),
                new InstructionCase(0x99, RegisterType.C, true),
                new InstructionCase(0x9A, RegisterType.D, true),
                new InstructionCase(0x9B, RegisterType.E, true),
                new InstructionCase(0x9C, RegisterType.H, true),
                new InstructionCase(0x9D, RegisterType.L, true),
                new InstructionCase(0x9E, RegisterType.HL, true),
                new InstructionCase(0x9F, RegisterType.A, true))
                .flatMap(instruction -> scenariosFor(instruction)
                        .map(scenario -> Arguments.of(instruction, scenario)));
    }

    private static Stream<SubtractionScenario> scenariosFor(final InstructionCase instruction) {
        if (instruction.sourceRegister() == RegisterType.A) {
            return instruction.subtractWithCarry() ? accumulatorSbcScenarios() : accumulatorSubScenarios();
        }
        return instruction.subtractWithCarry() ? sbcScenarios() : subScenarios();
    }

    @SuppressWarnings("checkstyle:magicnumber")
    private static Stream<SubtractionScenario> subScenarios() {
        return Stream.of(
                new SubtractionScenario("no borrow", 0x03, 0x01, true, 0x02, false, false, false),
                new SubtractionScenario("zero", 0x01, 0x01, false, 0x00, true, false, false),
                new SubtractionScenario("half borrow", 0x10, 0x01, false, 0x0F, false, true, false),
                new SubtractionScenario("full borrow", 0x00, 0x01, false, 0xFF, false, true, true));
    }

    @SuppressWarnings("checkstyle:magicnumber")
    private static Stream<SubtractionScenario> accumulatorSubScenarios() {
        return Stream.of(
                new SubtractionScenario("zero", 0x01, 0x01, false, 0x00, true, false, false),
                new SubtractionScenario("ignores carry", 0x80, 0x80, true, 0x00, true, false, false));
    }

    @SuppressWarnings("checkstyle:magicnumber")
    private static Stream<SubtractionScenario> sbcScenarios() {
        return Stream.of(
                new SubtractionScenario("without borrow-in", 0x03, 0x01, false, 0x02, false, false, false),
                new SubtractionScenario("with borrow-in", 0x03, 0x01, true, 0x01, false, false, false),
                new SubtractionScenario("half borrow", 0x10, 0x00, true, 0x0F, false, true, false),
                new SubtractionScenario("full borrow", 0x00, 0x00, true, 0xFF, false, true, true),
                new SubtractionScenario("zero", 0x01, 0x00, true, 0x00, true, false, false));
    }

    @SuppressWarnings("checkstyle:magicnumber")
    private static Stream<SubtractionScenario> accumulatorSbcScenarios() {
        return Stream.of(
                new SubtractionScenario("without borrow-in", 0x80, 0x80, false, 0x00, true, false, false),
                new SubtractionScenario("with borrow-in", 0x80, 0x80, true, 0xFF, false, true, true));
    }

    /**
     * Opcode and source register for one subtraction instruction.
     *
     * @param opcode instruction opcode
     * @param sourceRegister source operand register
     * @param subtractWithCarry whether instruction includes carry as borrow
     */
    private record InstructionCase(int opcode, RegisterType sourceRegister, boolean subtractWithCarry) {
        private int cycles() {
            return sourceRegister == RegisterType.HL ? 2 : 1;
        }
    }

    /**
     * Input and expected output for one subtraction scenario.
     *
     * @param description scenario name
     * @param accumulator initial accumulator
     * @param source source operand
     * @param carryIn initial carry flag
     * @param result expected accumulator
     * @param zero expected zero flag
     * @param halfCarry expected half-carry flag
     * @param carry expected carry flag
     */
    private record SubtractionScenario(String description, int accumulator, int source, boolean carryIn,
            int result, boolean zero, boolean halfCarry, boolean carry) {
        @Override
        public String toString() {
            return description;
        }
    }
}
