package com.egamboau.gameboy.cpu.instructions.implementations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.egamboau.gameboy.cpu.CPUTestBase;
import com.egamboau.gameboy.cpu.instructions.RegisterType;

/** Tests for the AND, XOR, OR, and CP opcode ranges. */
class LogicalOperationTest extends CPUTestBase {

    private static final int INDIRECT_SOURCE_ADDRESS = 0xC000;

    @ParameterizedTest(name = "opcode {0}: A={2}, source={3}, result={4}")
    @MethodSource("logicalOperationCases")
    void testLogicalOperation(final int opcode, final RegisterType sourceRegister,
            final int accumulator, final int source, final int result,
            final boolean zero, final boolean subtract, final boolean halfCarry) {
        getCurrentCpu().setValueInRegister(accumulator, RegisterType.A);
        if (sourceRegister == RegisterType.HL) {
            getCurrentCpu().setValueInRegister(INDIRECT_SOURCE_ADDRESS, RegisterType.HL);
        } else if (sourceRegister != RegisterType.A) {
            getCurrentCpu().setValueInRegister(source, sourceRegister);
        }
        getCurrentCpu().setSubtract(!subtract);
        getCurrentCpu().setHalfCarry(!halfCarry);
        getCurrentCpu().setCarry(true);
        long cyclesBefore = getCurrentCpu().getCycles();
        int programCounterBefore = getCurrentCpu().getValueFromRegister(RegisterType.PC);
        stubOpcode(opcode);
        if (sourceRegister == RegisterType.HL) {
            when(getCurrentBus().readByteFromAddress(INDIRECT_SOURCE_ADDRESS)).thenReturn(source);
        }

        getCurrentCpu().cpuStep();

        assertEquals(result, getCurrentCpu().getValueFromRegister(RegisterType.A));
        assertEquals(programCounterBefore + 1, getCurrentCpu().getValueFromRegister(RegisterType.PC));
        assertEquals(cyclesBefore + (sourceRegister == RegisterType.HL ? 2 : 1), getCurrentCpu().getCycles());
        assertEquals(zero, getCurrentCpu().getZero());
        assertEquals(subtract, getCurrentCpu().getSubtract());
        assertEquals(halfCarry, getCurrentCpu().getHalfCarry());
        assertEquals(false, getCurrentCpu().getCarry());
        if (sourceRegister == RegisterType.HL) {
            verify(getCurrentBus()).readByteFromAddress(INDIRECT_SOURCE_ADDRESS);
        }
    }

    @SuppressWarnings("checkstyle:magicnumber")
    static Stream<Arguments> logicalOperationCases() {
        RegisterType[] sources = {
            RegisterType.B, RegisterType.C, RegisterType.D, RegisterType.E,
            RegisterType.H, RegisterType.L, RegisterType.HL, RegisterType.A
        };
        return Stream.of(
                IntStream.range(0, sources.length)
                    .mapToObj(index -> Arguments.of(0xA0 + index, sources[index], 0x5A,
                            0x3C, sources[index] == RegisterType.A ? 0x5A : 0x18, false, false, true)),
                IntStream.range(0, sources.length)
                    .mapToObj(index -> Arguments.of(0xA8 + index, sources[index], 0x5A,
                            0x3C, sources[index] == RegisterType.A ? 0x00 : 0x66,
                            sources[index] == RegisterType.A, false, false)),
                IntStream.range(0, sources.length)
                    .mapToObj(index -> Arguments.of(0xB0 + index, sources[index], 0x5A,
                            0x3C, sources[index] == RegisterType.A ? 0x5A : 0x7E, false, false, false)),
                IntStream.range(0, sources.length)
                    .mapToObj(index -> Arguments.of(0xB8 + index, sources[index], 0x5A,
                            0x3C, 0x5A, sources[index] == RegisterType.A, true,
                            sources[index] != RegisterType.A)))
                .flatMap(stream -> stream);
    }
}
