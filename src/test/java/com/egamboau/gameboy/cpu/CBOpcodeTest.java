package com.egamboau.gameboy.cpu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.egamboau.gameboy.cpu.instructions.RegisterType;

/** Acceptance tests for issue #24, exercised through the CPU's opcode fetch path. */
@SuppressWarnings("checkstyle:magicnumber")
class CBOpcodeTest extends CPUTestBase {

    private static final RegisterType[] TARGETS = {
        RegisterType.B, RegisterType.C, RegisterType.D, RegisterType.E,
        RegisterType.H, RegisterType.L, RegisterType.HL, RegisterType.A
    };

    // Rows: operation, input, initial flags, expected value, expected flags.
    // Explicit vectors distinguish circular/through-carry and arithmetic/logical shifts.
    private static final int[][] ROTATE_SHIFT_CASES = {
        {0, 0x80, 0xE0, 0x01, 0x10}, {0, 0x00, 0x70, 0x00, 0x80},
        {0, 0x01, 0xF0, 0x02, 0x00},
        {1, 0x01, 0xE0, 0x80, 0x10}, {1, 0x00, 0x70, 0x00, 0x80},
        {1, 0x80, 0xF0, 0x40, 0x00},
        {2, 0x80, 0xE0, 0x00, 0x90}, {2, 0x80, 0xF0, 0x01, 0x10},
        {2, 0x00, 0xF0, 0x01, 0x00}, {2, 0x00, 0x60, 0x00, 0x80},
        {3, 0x01, 0xE0, 0x00, 0x90}, {3, 0x01, 0xF0, 0x80, 0x10},
        {3, 0x00, 0xF0, 0x80, 0x00}, {3, 0x00, 0x60, 0x00, 0x80},
        {4, 0x80, 0xF0, 0x00, 0x90}, {4, 0x01, 0xF0, 0x02, 0x00},
        {4, 0x00, 0x70, 0x00, 0x80},
        {5, 0x81, 0xE0, 0xC0, 0x10}, {5, 0x01, 0xF0, 0x00, 0x90},
        {5, 0x80, 0xF0, 0xC0, 0x00}, {5, 0x00, 0x70, 0x00, 0x80},
        {6, 0xA5, 0xF0, 0x5A, 0x00}, {6, 0x00, 0x70, 0x00, 0x80},
        {7, 0x81, 0xE0, 0x40, 0x10}, {7, 0x01, 0xF0, 0x00, 0x90},
        {7, 0x80, 0xF0, 0x40, 0x00}, {7, 0x00, 0x70, 0x00, 0x80}
    };

    static Stream<Arguments> cbCases() {
        List<Arguments> cases = new ArrayList<>();
        for (int target = 0; target < TARGETS.length; target++) {
            for (int[] vector : ROTATE_SHIFT_CASES) {
                cases.add(Arguments.of(vector[0] * 8 + target,
                    vector[1], vector[2], vector[3], vector[4]));
            }
            for (int bit = 0; bit < 8; bit++) {
                int mask = 1 << bit;
                for (int flags : new int[] {0x00, 0xF0}) {
                    for (int value : new int[] {mask, 0xFF ^ mask}) {
                        cases.add(Arguments.of(0x40 + bit * 8 + target, value, flags,
                            value, (flags & 0x10) | 0x20 | ((value & mask) == 0 ? 0x80 : 0)));
                        cases.add(Arguments.of(0x80 + bit * 8 + target, value, flags,
                            value & ~mask, flags));
                        cases.add(Arguments.of(0xC0 + bit * 8 + target, value, flags,
                            value | mask, flags));
                    }
                }
            }
        }
        return cases.stream();
    }

    @ParameterizedTest(name = "CB {0}: value={1}, flags={2}")
    @MethodSource("cbCases")
    void executesCbOpcode(final int opcode, final int input, final int flags,
            final int expected, final int expectedFlags) {
        var cpu = getCurrentCpu();
        var bus = getCurrentBus();
        int target = opcode & 7;
        boolean indirect = target == 6;
        boolean bitTest = opcode >= 0x40 && opcode < 0x80;
        for (RegisterType register : TARGETS) {
            cpu.setValueInRegister(0x35, register);
        }
        cpu.setValueInRegister(0xC123, RegisterType.HL);
        cpu.setValueInRegister(0xD000, RegisterType.SP);
        cpu.setValueInRegister(0x100, RegisterType.PC);
        cpu.setValueInRegister(0x3500 | flags, RegisterType.AF);
        if (indirect) {
            when(bus.readByteFromAddress(0xC123)).thenReturn(input);
        } else {
            cpu.setValueInRegister(input, TARGETS[target]);
        }
        var before = getCpuRegisters();
        when(bus.readByteFromAddress(0x100)).thenReturn(0xCB);
        when(bus.readByteFromAddress(0x101)).thenReturn(opcode);

        cpu.cpuStep();

        assertEquals(0x102, cpu.getValueFromRegister(RegisterType.PC), "two-byte fetch");
        // CPU counts machine cycles: 2 for registers, 3 for BIT (HL), 4 for other (HL).
        assertEquals(indirect ? (bitTest ? 3 : 4) : 2, cpu.getCycles(), "machine cycles");
        assertEquals(expectedFlags, cpu.getValueFromRegister(RegisterType.F), "Z/N/H/C flags");
        assertEquals(0xD000, cpu.getValueFromRegister(RegisterType.SP), "SP preserved");
        for (RegisterType register : TARGETS) {
            if (register != RegisterType.HL) {
                assertEquals(!indirect && register == TARGETS[target] ? expected : before.get(register),
                    cpu.getValueFromRegister(register), register.name());
            }
        }
        verify(bus).readByteFromAddress(0x100);
        verify(bus).readByteFromAddress(0x101);
        if (indirect) {
            verify(bus).readByteFromAddress(0xC123);
            if (!bitTest) {
                verify(bus).writeByteToAddress(expected, 0xC123);
            }
        }
        if (!indirect || bitTest) {
            verify(bus, never()).writeByteToAddress(anyInt(), anyInt());
        }
        verifyNoMoreInteractions(bus);
    }
}
