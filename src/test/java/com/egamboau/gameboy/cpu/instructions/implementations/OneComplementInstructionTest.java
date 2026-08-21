package com.egamboau.gameboy.cpu.instructions.implementations;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.egamboau.gameboy.cpu.CPUTestBase;
import com.egamboau.gameboy.cpu.instructions.RegisterType;
import com.egamboau.test.TestUtils;

class OneComplementInstructionTest extends CPUTestBase {

    @ParameterizedTest
    @ValueSource(ints = {0x00, 0xFF, 0x5A})
    @SuppressWarnings("checkstyle:magicnumber")
    void testInstruction(final int value) {
        stubOpcode(0x2F);
        getCurrentCpu().setValueInRegister(value, RegisterType.A);
        Map<RegisterType, Integer> registers = getCpuRegisters(
                TestUtils.getPairForRegister(RegisterType.A, RegisterType.F));
        long cycles = getCurrentCpu().getCycles();

        getCurrentCpu().cpuStep();

        registers.computeIfPresent(RegisterType.PC, (register, oldValue) -> oldValue + 1);
        assertEquals(registers, getCpuRegisters(TestUtils.getPairForRegister(RegisterType.A, RegisterType.F)));
        assertEquals(cycles + 1, getCurrentCpu().getCycles());
        assertEquals(~value & 0xFF, getCurrentCpu().getValueFromRegister(RegisterType.A));
        assertEquals(true, getCurrentCpu().getSubtract());
        assertEquals(true, getCurrentCpu().getHalfCarry());
    }
}
