package com.egamboau.gameboy.cpu.instructions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.Test;

import com.egamboau.gameboy.cpu.CPUTest;
import com.egamboau.test.TestUtils;

public class DecimalAdjustAccumulatorTest extends CPUTest{

    @Test
    void testDAA_SubstractFlagNotSet_HalfCarryNotSet_CarryNotSet_LowerNibbleLessThanNine() {
        this.currentCpu.setSubtract(false);
        this.currentCpu.setHalfCarry(false);
        this.currentCpu.setCarry(false);
        this.currentCpu.setValueInRegister(0x0A, RegisterType.REGISTER_A);
        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x27 //the opcode
        );

        Map<RegisterType, Integer> registerValues = this.getCpuRegisterValues(TestUtils.getPairForRegister(RegisterType.REGISTER_AF));
        long previousCycleCount = currentCpu.getCycles();
        this.currentCpu.cpu_step();
        long currentCycleCount = currentCpu.getCycles();
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisterValues(TestUtils.getPairForRegister(RegisterType.REGISTER_AF));

        assertEquals(previousCycleCount + 1, currentCycleCount, "Cycle count not currently matching.");

        //other flags must be the same, and update the PC to be 1 byte more
        registerValues.computeIfPresent(RegisterType.REGISTER_PC, (t, u) -> u+1);
        assertEquals(registerValues, newRegisterValues);
        assertEquals(0x10, currentCpu.getValueFromRegister(RegisterType.REGISTER_A));

        assertFalse(currentCpu.getZero());
        assertFalse(currentCpu.getHalfCarry());
        assertFalse(currentCpu.getCarry());
    }

    @Test
    void testDAA_SubstractFlagNotSet_HalfCarryNotSet_CarryNotSet_ResultBiggerThanNineNine() {
        this.currentCpu.setSubtract(false);
        this.currentCpu.setHalfCarry(false);
        this.currentCpu.setCarry(false);
        this.currentCpu.setValueInRegister(0x9A, RegisterType.REGISTER_A);
        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x27 //the opcode
        );

        Map<RegisterType, Integer> registerValues = this.getCpuRegisterValues(TestUtils.getPairForRegister(RegisterType.REGISTER_AF));
        long previousCycleCount = currentCpu.getCycles();
        this.currentCpu.cpu_step();
        long currentCycleCount = currentCpu.getCycles();
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisterValues(TestUtils.getPairForRegister(RegisterType.REGISTER_AF));

        assertEquals(previousCycleCount + 1, currentCycleCount, "Cycle count not currently matching.");

        //other flags must be the same, and update the PC to be 1 byte more
        registerValues.computeIfPresent(RegisterType.REGISTER_PC, (t, u) -> u+1);
        assertEquals(registerValues, newRegisterValues);
        assertEquals(0x00, currentCpu.getValueFromRegister(RegisterType.REGISTER_A));

        assertTrue(currentCpu.getZero());
        assertFalse(currentCpu.getHalfCarry());
        assertTrue(currentCpu.getCarry());
    }

    @Test
    void testDAA_SubstractFlagNotSet_HalfCarrySet_CarryNotSet() {
        this.currentCpu.setSubtract(false);
        this.currentCpu.setHalfCarry(true);
        this.currentCpu.setCarry(false);
        this.currentCpu.setValueInRegister(0x12, RegisterType.REGISTER_A);
        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x27 //the opcode
        );

        Map<RegisterType, Integer> registerValues = this.getCpuRegisterValues(TestUtils.getPairForRegister(RegisterType.REGISTER_AF));
        long previousCycleCount = currentCpu.getCycles();
        this.currentCpu.cpu_step();
        long currentCycleCount = currentCpu.getCycles();
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisterValues(TestUtils.getPairForRegister(RegisterType.REGISTER_AF));

        assertEquals(previousCycleCount + 1, currentCycleCount, "Cycle count not currently matching.");

        //other flags must be the same, and update the PC to be 1 byte more
        registerValues.computeIfPresent(RegisterType.REGISTER_PC, (t, u) -> u+1);
        assertEquals(registerValues, newRegisterValues);
        assertEquals(0x18, currentCpu.getValueFromRegister(RegisterType.REGISTER_A));

        assertFalse(currentCpu.getZero());
        assertFalse(currentCpu.getHalfCarry());
        assertFalse(currentCpu.getCarry());
    }

    @Test
    void testDAA_SubstractFlagNotSet_HalfCarryNotSet_CarrySet() {
        this.currentCpu.setSubtract(false);
        this.currentCpu.setHalfCarry(false);
        this.currentCpu.setCarry(true);
        this.currentCpu.setValueInRegister(0x35, RegisterType.REGISTER_A);
        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x27 //the opcode
        );

        Map<RegisterType, Integer> registerValues = this.getCpuRegisterValues(TestUtils.getPairForRegister(RegisterType.REGISTER_AF));
        long previousCycleCount = currentCpu.getCycles();
        this.currentCpu.cpu_step();
        long currentCycleCount = currentCpu.getCycles();
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisterValues(TestUtils.getPairForRegister(RegisterType.REGISTER_AF));

        assertEquals(previousCycleCount + 1, currentCycleCount, "Cycle count not currently matching.");

        //other flags must be the same, and update the PC to be 1 byte more
        registerValues.computeIfPresent(RegisterType.REGISTER_PC, (t, u) -> u+1);
        assertEquals(registerValues, newRegisterValues);
        assertEquals(0x95, currentCpu.getValueFromRegister(RegisterType.REGISTER_A));

        assertFalse(currentCpu.getZero());
        assertFalse(currentCpu.getHalfCarry());
        assertTrue(currentCpu.getCarry());
    }

    @Test
    void testDAA_SubstractFlagNotSet_HalfCarrySet_CarrySet() {
        this.currentCpu.setSubtract(false);
        this.currentCpu.setHalfCarry(true);
        this.currentCpu.setCarry(true);
        this.currentCpu.setValueInRegister(0x31, RegisterType.REGISTER_A);
        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x27 //the opcode
        );

        Map<RegisterType, Integer> registerValues = this.getCpuRegisterValues(TestUtils.getPairForRegister(RegisterType.REGISTER_AF));
        long previousCycleCount = currentCpu.getCycles();
        this.currentCpu.cpu_step();
        long currentCycleCount = currentCpu.getCycles();
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisterValues(TestUtils.getPairForRegister(RegisterType.REGISTER_AF));

        assertEquals(previousCycleCount + 1, currentCycleCount, "Cycle count not currently matching.");

        //other flags must be the same, and update the PC to be 1 byte more
        registerValues.computeIfPresent(RegisterType.REGISTER_PC, (t, u) -> u+1);
        assertEquals(registerValues, newRegisterValues);
        assertEquals(0x97, currentCpu.getValueFromRegister(RegisterType.REGISTER_A));

        assertFalse(currentCpu.getZero());
        assertFalse(currentCpu.getHalfCarry());
        assertTrue(currentCpu.getCarry());
    }

    @Test
    void testDAA_SubstractFlagSet_HalfCarryNotSet_CarryNotSet() {
        this.currentCpu.setSubtract(true);
        this.currentCpu.setHalfCarry(false);
        this.currentCpu.setCarry(false);
        this.currentCpu.setValueInRegister(0x42, RegisterType.REGISTER_A);
        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x27 //the opcode
        );

        Map<RegisterType, Integer> registerValues = this.getCpuRegisterValues(TestUtils.getPairForRegister(RegisterType.REGISTER_AF));
        long previousCycleCount = currentCpu.getCycles();
        this.currentCpu.cpu_step();
        long currentCycleCount = currentCpu.getCycles();
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisterValues(TestUtils.getPairForRegister(RegisterType.REGISTER_AF));

        assertEquals(previousCycleCount + 1, currentCycleCount, "Cycle count not currently matching.");

        //other flags must be the same, and update the PC to be 1 byte more
        registerValues.computeIfPresent(RegisterType.REGISTER_PC, (t, u) -> u+1);
        assertEquals(registerValues, newRegisterValues);
        assertEquals(0x42, currentCpu.getValueFromRegister(RegisterType.REGISTER_A));

        assertFalse(currentCpu.getZero());
        assertFalse(currentCpu.getHalfCarry());
        assertFalse(currentCpu.getCarry());
    }

    @Test
    void testDAA_SubstractFlagSet_HalfCarrySet_CarryNotSet() {
        this.currentCpu.setSubtract(true);
        this.currentCpu.setHalfCarry(true);
        this.currentCpu.setCarry(false);
        this.currentCpu.setValueInRegister(0x45, RegisterType.REGISTER_A);
        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x27 //the opcode
        );

        Map<RegisterType, Integer> registerValues = this.getCpuRegisterValues(TestUtils.getPairForRegister(RegisterType.REGISTER_AF));
        long previousCycleCount = currentCpu.getCycles();
        this.currentCpu.cpu_step();
        long currentCycleCount = currentCpu.getCycles();
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisterValues(TestUtils.getPairForRegister(RegisterType.REGISTER_AF));

        assertEquals(previousCycleCount + 1, currentCycleCount, "Cycle count not currently matching.");

        //other flags must be the same, and update the PC to be 1 byte more
        registerValues.computeIfPresent(RegisterType.REGISTER_PC, (t, u) -> u+1);
        assertEquals(registerValues, newRegisterValues);
        assertEquals(0x3F, currentCpu.getValueFromRegister(RegisterType.REGISTER_A));

        assertFalse(currentCpu.getZero());
        assertFalse(currentCpu.getHalfCarry());
        assertFalse(currentCpu.getCarry());
    }

    @Test
    void testDAA_SubstractFlagSet_HalfCarryNotSet_CarrySet() {
        this.currentCpu.setSubtract(true);
        this.currentCpu.setHalfCarry(false);
        this.currentCpu.setCarry(true);
        this.currentCpu.setValueInRegister(0x85, RegisterType.REGISTER_A);
        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x27 //the opcode
        );

        Map<RegisterType, Integer> registerValues = this.getCpuRegisterValues(TestUtils.getPairForRegister(RegisterType.REGISTER_AF));
        long previousCycleCount = currentCpu.getCycles();
        this.currentCpu.cpu_step();
        long currentCycleCount = currentCpu.getCycles();
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisterValues(TestUtils.getPairForRegister(RegisterType.REGISTER_AF));

        assertEquals(previousCycleCount + 1, currentCycleCount, "Cycle count not currently matching.");

        //other flags must be the same, and update the PC to be 1 byte more
        registerValues.computeIfPresent(RegisterType.REGISTER_PC, (t, u) -> u+1);
        assertEquals(registerValues, newRegisterValues);
        assertEquals(0x25, currentCpu.getValueFromRegister(RegisterType.REGISTER_A));

        assertFalse(currentCpu.getZero());
        assertFalse(currentCpu.getHalfCarry());
        assertTrue(currentCpu.getCarry());
    }
    
    @Test
    void testDAA_SubstractFlagSet_HalfCarrySet_CarrySet() {
        this.currentCpu.setSubtract(true);
        this.currentCpu.setHalfCarry(true);
        this.currentCpu.setCarry(true);
        this.currentCpu.setValueInRegister(0xE5, RegisterType.REGISTER_A);
        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x27 //the opcode
        );

        Map<RegisterType, Integer> registerValues = this.getCpuRegisterValues(TestUtils.getPairForRegister(RegisterType.REGISTER_AF));
        long previousCycleCount = currentCpu.getCycles();
        this.currentCpu.cpu_step();
        long currentCycleCount = currentCpu.getCycles();
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisterValues(TestUtils.getPairForRegister(RegisterType.REGISTER_AF));

        assertEquals(previousCycleCount + 1, currentCycleCount, "Cycle count not currently matching.");

        //other flags must be the same, and update the PC to be 1 byte more
        registerValues.computeIfPresent(RegisterType.REGISTER_PC, (t, u) -> u+1);
        assertEquals(registerValues, newRegisterValues);
        assertEquals(0x7F, currentCpu.getValueFromRegister(RegisterType.REGISTER_A));

        assertFalse(currentCpu.getZero());
        assertFalse(currentCpu.getHalfCarry());
        assertTrue(currentCpu.getCarry());
    }
}
