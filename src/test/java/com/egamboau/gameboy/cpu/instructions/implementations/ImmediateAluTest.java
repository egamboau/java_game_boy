package com.egamboau.gameboy.cpu.instructions.implementations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.egamboau.gameboy.cpu.CPUTestBase;
import com.egamboau.gameboy.cpu.instructions.RegisterType;

/** Tests for the immediate 8-bit ALU instructions. */
class ImmediateAluTest extends CPUTestBase {

    @ParameterizedTest(name = "{0}")
    @MethodSource("immediateAluCases")
    void testImmediateAluInstruction(final ImmediateAluCase testCase) {
        getCurrentCpu().setValueInRegister(testCase.accumulator(), RegisterType.A);
        getCurrentCpu().setCarry(testCase.carryIn());
        getCurrentCpu().setZero(!testCase.zero());
        getCurrentCpu().setSubtract(!testCase.subtract());
        getCurrentCpu().setHalfCarry(!testCase.halfCarry());
        long cyclesBefore = getCurrentCpu().getCycles();
        int programCounterBefore = getCurrentCpu().getValueFromRegister(RegisterType.PC);
        when(getCurrentBus().readByteFromAddress(anyInt()))
                .thenReturn(testCase.opcode(), testCase.operand());

        getCurrentCpu().cpuStep();

        assertEquals(testCase.result(), getCurrentCpu().getValueFromRegister(RegisterType.A));
        assertEquals(programCounterBefore + 2, getCurrentCpu().getValueFromRegister(RegisterType.PC));
        assertEquals(cyclesBefore + 2, getCurrentCpu().getCycles());
        assertEquals(testCase.zero(), getCurrentCpu().getZero());
        assertEquals(testCase.subtract(), getCurrentCpu().getSubtract());
        assertEquals(testCase.halfCarry(), getCurrentCpu().getHalfCarry());
        assertEquals(testCase.carry(), getCurrentCpu().getCarry());
    }

    @SuppressWarnings("checkstyle:magicnumber")
    static Stream<ImmediateAluCase> immediateAluCases() {
        return Stream.of(
                new ImmediateAluCase("ADD A,d8", 0xC6, 0x0F, 0x01, false,
                        0x10, false, false, true, false),
                new ImmediateAluCase("ADC A,d8", 0xCE, 0xFF, 0x00, true,
                        0x00, true, false, true, true),
                new ImmediateAluCase("ADC A,d8 without carry", 0xCE, 0x01, 0x01, false,
                        0x02, false, false, false, false),
                new ImmediateAluCase("SUB d8", 0xD6, 0x10, 0x01, false,
                        0x0F, false, true, true, false),
                new ImmediateAluCase("SBC A,d8", 0xDE, 0x00, 0x00, true,
                        0xFF, false, true, true, true),
                new ImmediateAluCase("SBC A,d8 without carry", 0xDE, 0x02, 0x01, false,
                        0x01, false, true, false, false),
                new ImmediateAluCase("AND d8", 0xE6, 0xF0, 0x0F, true,
                        0x00, true, false, true, false),
                new ImmediateAluCase("XOR d8", 0xEE, 0xAA, 0xFF, true,
                        0x55, false, false, false, false),
                new ImmediateAluCase("OR d8", 0xF6, 0x80, 0x01, true,
                        0x81, false, false, false, false),
                new ImmediateAluCase("CP d8", 0xFE, 0x10, 0x20, false,
                        0x10, false, true, false, true));
    }

    private record ImmediateAluCase(String description, int opcode, int accumulator,
            int operand, boolean carryIn, int result, boolean zero, boolean subtract,
            boolean halfCarry, boolean carry) {
        @Override
        public String toString() {
            return description;
        }
    }
}
