package com.egamboau.gameboy.cpu.instructions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.Test;

import com.egamboau.gameboy.cpu.CPUTestBase;
import com.egamboau.test.TestUtils;

class OneComplementInstructionTest extends CPUTestBase{


    @Test
    void testInstructionWithAllZeroValue() {
        int registerValue = 0;
        when(this.getCurrentBus().readByteFromAddress(anyInt())).thenReturn(
            0x2F //the opcode
        );

        this.getCurrentCpu().setValueInRegister(registerValue, RegisterType.REGISTER_A);

        Map<RegisterType, Integer> registerValues = this.getCpuRegisters(TestUtils.getPairForRegister(RegisterType.REGISTER_A));
        long previousCycleCount = getCurrentCpu().getCycles();
        this.getCurrentCpu().cpuStep();
        long currentCycleCount = getCurrentCpu().getCycles();
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisters(TestUtils.getPairForRegister(RegisterType.REGISTER_A));

        //Cycle count must match
        assertEquals(previousCycleCount + 1, currentCycleCount, "Cycle count not currently matching.");

        //other flags must be the same, and update the PC to be 1 byte more
        registerValues.computeIfPresent(RegisterType.REGISTER_PC, (t, u) -> u+1);
        assertEquals(registerValues, newRegisterValues);

        assertEquals(255, getCurrentCpu().getValueFromRegister(RegisterType.REGISTER_A));
    }

    @Test
    void testInstructionWithAllOnesValue() {
        int registerValue = 255;
        when(this.getCurrentBus().readByteFromAddress(anyInt())).thenReturn(
            0x2F //the opcode
        );

        this.getCurrentCpu().setValueInRegister(registerValue, RegisterType.REGISTER_A);

        Map<RegisterType, Integer> registerValues = this.getCpuRegisters(TestUtils.getPairForRegister(RegisterType.REGISTER_A));
        long previousCycleCount = getCurrentCpu().getCycles();
        this.getCurrentCpu().cpuStep();
        long currentCycleCount = getCurrentCpu().getCycles();
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisters(TestUtils.getPairForRegister(RegisterType.REGISTER_A));

        //Cycle count must match
        assertEquals(previousCycleCount + 1, currentCycleCount, "Cycle count not currently matching.");

        //other flags must be the same, and update the PC to be 1 byte more
        registerValues.computeIfPresent(RegisterType.REGISTER_PC, (t, u) -> u+1);
        assertEquals(registerValues, newRegisterValues);

        assertEquals(0, getCurrentCpu().getValueFromRegister(RegisterType.REGISTER_A));
    }


    @Test
    void testInstructionWithRandomValue() {
        int registerValue = TestUtils.getRandomIntegerInRange(0, 0xFF) & 0xFF;
        when(this.getCurrentBus().readByteFromAddress(anyInt())).thenReturn(
            0x2F //the opcode
        );

        this.getCurrentCpu().setValueInRegister(registerValue, RegisterType.REGISTER_A);

        Map<RegisterType, Integer> registerValues = this.getCpuRegisters(TestUtils.getPairForRegister(RegisterType.REGISTER_A));
        long previousCycleCount = getCurrentCpu().getCycles();
        this.getCurrentCpu().cpuStep();
        long currentCycleCount = getCurrentCpu().getCycles();
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisters(TestUtils.getPairForRegister(RegisterType.REGISTER_A));

        //Cycle count must match
        assertEquals(previousCycleCount + 1, currentCycleCount, "Cycle count not currently matching.");

        //other flags must be the same, and update the PC to be 1 byte more
        registerValues.computeIfPresent(RegisterType.REGISTER_PC, (t, u) -> u+1);
        assertEquals(registerValues, newRegisterValues);

        assertEquals((~registerValue & 0xFF), getCurrentCpu().getValueFromRegister(RegisterType.REGISTER_A));
    }

}
