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
 * Unit tests for ADC instructions implemented by the CPU.
 */
class AdcTest extends CPUTestBase {

    /** Opcode for ADC A,(HL). */
    private static final int OPCODE_ADC_A_INDIRECT_HL = 0x8E;
    /** Memory address used by the indirect-HL tests. */
    private static final int INDIRECT_SOURCE_ADDRESS = 0xC000;
    /** Cycle count for an ADC instruction with a register source. */
    private static final int REGISTER_CYCLES = 1;
    /** Cycle count for an ADC instruction with an indirect source. */
    private static final int INDIRECT_CYCLES = 2;

    /**
     * Verifies ADC with each register source across arithmetic and flag boundaries.
     *
     * @param opcode the ADC opcode under test
     * @param sourceRegister the source register encoded by the opcode
     * @param scenario the input and expected output values
     */
    @ParameterizedTest(name = "{index}: opcode {0}, source {1}, scenario {2}")
    @MethodSource("generateRegisterAdcArguments")
    void testAdcWithRegisterSource(final int opcode, final RegisterType sourceRegister,
            final AdcScenario scenario) {
        getCurrentCpu().setValueInRegister(scenario.accumulator(), RegisterType.A);
        if (sourceRegister != RegisterType.A) {
            getCurrentCpu().setValueInRegister(scenario.source(), sourceRegister);
        }
        getCurrentCpu().setCarry(scenario.carryIn());
        long cyclesBefore = getCurrentCpu().getCycles();
        int programCounterBefore = getCurrentCpu().getValueFromRegister(RegisterType.PC);
        stubOpcode(opcode);

        getCurrentCpu().cpuStep();

        assertAdcResult(scenario, programCounterBefore, cyclesBefore, REGISTER_CYCLES);
    }

    /**
     * Verifies ADC A,(HL) across arithmetic and flag boundaries.
     *
     * @param scenario the input and expected output values
     */
    @ParameterizedTest(name = "{index}: ADC A,(HL), scenario {0}")
    @MethodSource("generateStandardAdcScenarios")
    void testAdcWithIndirectHlSource(final AdcScenario scenario) {
        getCurrentCpu().setValueInRegister(scenario.accumulator(), RegisterType.A);
        getCurrentCpu().setValueInRegister(INDIRECT_SOURCE_ADDRESS, RegisterType.HL);
        getCurrentCpu().setCarry(scenario.carryIn());
        long cyclesBefore = getCurrentCpu().getCycles();
        int programCounterBefore = getCurrentCpu().getValueFromRegister(RegisterType.PC);
        stubOpcode(OPCODE_ADC_A_INDIRECT_HL);
        when(getCurrentBus().readByteFromAddress(INDIRECT_SOURCE_ADDRESS)).thenReturn(scenario.source());

        getCurrentCpu().cpuStep();

        assertAdcResult(scenario, programCounterBefore, cyclesBefore, INDIRECT_CYCLES);
        verify(getCurrentBus()).readByteFromAddress(INDIRECT_SOURCE_ADDRESS);
    }

    private void assertAdcResult(final AdcScenario scenario, final int programCounterBefore,
            final long cyclesBefore, final int expectedCycles) {
        assertEquals(scenario.result(), getCurrentCpu().getValueFromRegister(RegisterType.A));
        assertEquals(programCounterBefore + 1, getCurrentCpu().getValueFromRegister(RegisterType.PC));
        assertEquals(cyclesBefore + expectedCycles, getCurrentCpu().getCycles());
        assertEquals(scenario.zero(), getCurrentCpu().getZero());
        assertEquals(false, getCurrentCpu().getSubtract());
        assertEquals(scenario.halfCarry(), getCurrentCpu().getHalfCarry());
        assertEquals(scenario.carry(), getCurrentCpu().getCarry());
    }

    @SuppressWarnings("checkstyle:magicnumber")
    static Stream<Arguments> generateRegisterAdcArguments() {
        Stream<Arguments> registerArguments = Stream.of(
                Arguments.of(0x88, RegisterType.B),
                Arguments.of(0x89, RegisterType.C),
                Arguments.of(0x8A, RegisterType.D),
                Arguments.of(0x8B, RegisterType.E),
                Arguments.of(0x8C, RegisterType.H),
                Arguments.of(0x8D, RegisterType.L))
                .flatMap(instruction -> generateStandardAdcScenarios()
                        .map(scenario -> Arguments.of(
                                instruction.get()[0], instruction.get()[1], scenario)));

        Stream<Arguments> accumulatorArguments = generateAccumulatorAdcScenarios()
                .map(scenario -> Arguments.of(0x8F, RegisterType.A, scenario));
        return Stream.concat(registerArguments, accumulatorArguments);
    }

    @SuppressWarnings("checkstyle:magicnumber")
    static Stream<AdcScenario> generateStandardAdcScenarios() {
        return Stream.of(
                new AdcScenario("without carry-in", 0x01, 0x02, false, 0x03, false, false, false),
                new AdcScenario("with carry-in", 0x01, 0x02, true, 0x04, false, false, false),
                new AdcScenario("half carry", 0x0F, 0x00, true, 0x10, false, true, false),
                new AdcScenario("8-bit overflow", 0xFF, 0x00, true, 0x00, true, true, true),
                new AdcScenario("signed boundary", 0x7F, 0x01, false, 0x80, false, true, false));
    }

    @SuppressWarnings("checkstyle:magicnumber")
    private static Stream<AdcScenario> generateAccumulatorAdcScenarios() {
        return Stream.of(
                new AdcScenario("without carry-in", 0x01, 0x01, false, 0x02, false, false, false),
                new AdcScenario("with carry-in", 0x01, 0x01, true, 0x03, false, false, false),
                new AdcScenario("half carry", 0x0F, 0x0F, false, 0x1E, false, true, false),
                new AdcScenario("8-bit overflow", 0x80, 0x80, false, 0x00, true, false, true),
                new AdcScenario("maximum with carry-in", 0xFF, 0xFF, true, 0xFF, false, true, true));
    }

    /**
     * Input values and expected state for one ADC scenario.
     *
     * @param description scenario name used in parameterized test output
     * @param accumulator initial accumulator value
     * @param source source operand value
     * @param carryIn initial carry flag
     * @param result expected accumulator value
     * @param zero expected zero flag
     * @param halfCarry expected half-carry flag
     * @param carry expected carry flag
     */
    private record AdcScenario(String description, int accumulator, int source, boolean carryIn,
            int result, boolean zero, boolean halfCarry, boolean carry) {
        @Override
        public String toString() {
            return description;
        }
    }
}
