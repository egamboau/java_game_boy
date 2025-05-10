package com.egamboau.gameboy.cpu.instructions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.Test;

import com.egamboau.gameboy.cpu.CPUTest;
import com.egamboau.test.TestUtils;

public class OneComplementInstructionTest extends CPUTest{

    @Test
    void testInstructionWithAllZeroValue() {
        int registerValue = 0;
        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x2F //the opcode
        );

        this.currentCpu.setValueInRegister(registerValue, RegisterType.REGISTER_A);

        Map<RegisterType, Integer> registerValues = this.getCpuRegisterValues(TestUtils.getPairForRegister(RegisterType.REGISTER_A));
        long previousCycleCount = currentCpu.getCycles();
        this.currentCpu.cpu_step();
        long currentCycleCount = currentCpu.getCycles();
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisterValues(TestUtils.getPairForRegister(RegisterType.REGISTER_A));

        //Cycle count must match
        assertEquals(previousCycleCount + 1, currentCycleCount, "Cycle count not currently matching.");

        //other flags must be the same, and update the PC to be 1 byte more
        registerValues.computeIfPresent(RegisterType.REGISTER_PC, (t, u) -> u+1);
        assertEquals(registerValues, newRegisterValues);

        assertEquals(255, currentCpu.getValueFromRegister(RegisterType.REGISTER_A));
    }

    @Test
    void testInstructionWithAllOnesValue() {
        int registerValue = 255;
        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x2F //the opcode
        );

        this.currentCpu.setValueInRegister(registerValue, RegisterType.REGISTER_A);

        Map<RegisterType, Integer> registerValues = this.getCpuRegisterValues(TestUtils.getPairForRegister(RegisterType.REGISTER_A));
        long previousCycleCount = currentCpu.getCycles();
        this.currentCpu.cpu_step();
        long currentCycleCount = currentCpu.getCycles();
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisterValues(TestUtils.getPairForRegister(RegisterType.REGISTER_A));

        //Cycle count must match
        assertEquals(previousCycleCount + 1, currentCycleCount, "Cycle count not currently matching.");

        //other flags must be the same, and update the PC to be 1 byte more
        registerValues.computeIfPresent(RegisterType.REGISTER_PC, (t, u) -> u+1);
        assertEquals(registerValues, newRegisterValues);

        assertEquals(0, currentCpu.getValueFromRegister(RegisterType.REGISTER_A));
    }


    @Test
    void testInstructionWithRandomValue() {
        int registerValue = TestUtils.getRandomIntegerInRange(0, 0xFF) & 0xFF;;
        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x2F //the opcode
        );

        this.currentCpu.setValueInRegister(registerValue, RegisterType.REGISTER_A);

        Map<RegisterType, Integer> registerValues = this.getCpuRegisterValues(TestUtils.getPairForRegister(RegisterType.REGISTER_A));
        long previousCycleCount = currentCpu.getCycles();
        this.currentCpu.cpu_step();
        long currentCycleCount = currentCpu.getCycles();
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisterValues(TestUtils.getPairForRegister(RegisterType.REGISTER_A));

        //Cycle count must match
        assertEquals(previousCycleCount + 1, currentCycleCount, "Cycle count not currently matching.");

        //other flags must be the same, and update the PC to be 1 byte more
        registerValues.computeIfPresent(RegisterType.REGISTER_PC, (t, u) -> u+1);
        assertEquals(registerValues, newRegisterValues);

        assertEquals((~registerValue & 0xFF), currentCpu.getValueFromRegister(RegisterType.REGISTER_A));
    }

}
