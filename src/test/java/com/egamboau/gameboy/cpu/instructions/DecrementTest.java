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

public class DecrementTest extends CPUTest{

    @Test
    void testDecBWithZeroVAlue() {
        /*
         * Decrement the contents of register B by 1.
         * Initilizes with 0 and check the underflow
         */
        int b_register_data = 0;
        executeDecrementTest(b_register_data);

        //check if the registerValues are set accordingly
        assertTrue(currentCpu.getSubtract(), "N flag was not set correctly");
        assertTrue(currentCpu.getHalfCarry(), "Half Carry flag was not set correctly");
        assertFalse(currentCpu.getZero(), "Zero flag was not set correctly");
    }

    @Test
    void testDecB_HalfCarrySet() {
         /*
         * Decrement the contents of register B by 1.
         * Check if half carry is set accordingly
         */
        int b_register_data = 0x10;
        executeDecrementTest(b_register_data);

        assertTrue(currentCpu.getSubtract(), "N flag was not set correctly");
        assertTrue(currentCpu.getHalfCarry(), "Half Carry flag was not set correctly");
        assertFalse(currentCpu.getZero(), "Zero flag was not set correctly");

    }

    @Test
    void testDecB_HalfCarryNotSet() {
         /*
         * Decrement the contents of register B by 1.
         * Check if half carry is reset,accordingly
         */
        int b_register_data = 0x0E;
        executeDecrementTest(b_register_data);

        assertTrue(currentCpu.getSubtract(), "N flag was not set correctly");
        assertFalse(currentCpu.getHalfCarry(), "Half Carry flag was not set correctly");
        assertFalse(currentCpu.getZero(), "Zero flag was not set correctly");

    }

    @Test
    void testIncB_ZeroFlagSet() {
         /*
         * Decrement the contents of register B by 1.
         * Checks if the 0 flag set the value correctly.
         */
        int b_register_data = 0x01;

        executeDecrementTest(b_register_data);

        assertTrue(currentCpu.getSubtract(), "N flag was not set correctly");
        assertFalse(currentCpu.getHalfCarry(), "Half Carry flag was not set correctly");
        assertTrue(currentCpu.getZero(), "Zero flag was not set correctly");
        
    }


    private void executeDecrementTest(int register_data) {
        
        int expectedValue = (register_data - 1) & 0xFF;
        this.currentCpu.setValueInRegister(register_data, RegisterType.REGISTER_B);

        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x05//the opcode
        );

        Map<RegisterType, Integer> registerValues = this.getCpuRegisterValues(TestUtils.getPairForRegister(RegisterType.REGISTER_B, RegisterType.REGISTER_F ));
        long previousCycleCount = currentCpu.getCycles();
        this.currentCpu.cpu_step();
        long currentCycleCount = currentCpu.getCycles();
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisterValues(TestUtils.getPairForRegister(RegisterType.REGISTER_B, RegisterType.REGISTER_F ));

        //Register must be correct
        assertEquals(expectedValue, this.currentCpu.getValueFromRegister(RegisterType.REGISTER_B));
        //cycles must be updated as required
        assertEquals(previousCycleCount + 1, currentCycleCount, "Cycle count not currently matching.");
        registerValues.computeIfPresent(RegisterType.REGISTER_PC, (t, u) -> u+1);

        assertEquals(registerValues, newRegisterValues);
    }
}
