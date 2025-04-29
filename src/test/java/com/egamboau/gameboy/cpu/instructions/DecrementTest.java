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
        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x05//the opcode
        );
        executeDecrementTest(b_register_data, RegisterType.REGISTER_B, false);

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
        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x05//the opcode
        );
        executeDecrementTest(b_register_data, RegisterType.REGISTER_B, false);

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
        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x05//the opcode
        );
        executeDecrementTest(b_register_data, RegisterType.REGISTER_B, false);

        assertTrue(currentCpu.getSubtract(), "N flag was not set correctly");
        assertFalse(currentCpu.getHalfCarry(), "Half Carry flag was not set correctly");
        assertFalse(currentCpu.getZero(), "Zero flag was not set correctly");

    }

    @Test
    void testDecB_ZeroFlagSet() {
         /*
         * Decrement the contents of register B by 1.
         * Checks if the 0 flag set the value correctly.
         */
        int b_register_data = 0x01;

        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x05//the opcode
        );
        executeDecrementTest(b_register_data, RegisterType.REGISTER_B, false);

        assertTrue(currentCpu.getSubtract(), "N flag was not set correctly");
        assertFalse(currentCpu.getHalfCarry(), "Half Carry flag was not set correctly");
        assertTrue(currentCpu.getZero(), "Zero flag was not set correctly");
        
    }

    @Test
    void testDecBC(){
        /*
         * Decrement the contents of register pair BC by 1.
         */
        int register_data = TestUtils.getRandomIntegerInRange(0x00, 0xFFFF);
        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x0B//the opcode
        );

        executeDecrementTest(register_data, RegisterType.REGISTER_BC, true);
    }
    
    @Test
    void testDecCWithZeroVAlue() {
        /*
         * Decrement the contents of register C by 1.
         * Initilizes with 0 and check the underflow
         */
        int c_register_data = 0;
        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x0D//the opcode
        );
        executeDecrementTest(c_register_data, RegisterType.REGISTER_C, false);

        //check if the registerValues are set accordingly
        assertTrue(currentCpu.getSubtract(), "N flag was not set correctly");
        assertTrue(currentCpu.getHalfCarry(), "Half Carry flag was not set correctly");
        assertFalse(currentCpu.getZero(), "Zero flag was not set correctly");
    }

    @Test
    void testDecC_HalfCarrySet() {
         /*
         * Decrement the contents of register C by 1.
         * Check if half carry is set accordingly
         */
        int c_register_data = 0x10;
        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x0D//the opcode
        );
        executeDecrementTest(c_register_data, RegisterType.REGISTER_C, false);

        assertTrue(currentCpu.getSubtract(), "N flag was not set correctly");
        assertTrue(currentCpu.getHalfCarry(), "Half Carry flag was not set correctly");
        assertFalse(currentCpu.getZero(), "Zero flag was not set correctly");

    }

    @Test
    void testDecC_HalfCarryNotSet() {
         /*
         * Decrement the contents of register C by 1.
         * Check if half carry is reset,accordingly
         */
        int c_register_data = 0x0E;
        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x0D//the opcode
        );
        executeDecrementTest(c_register_data, RegisterType.REGISTER_C, false);

        assertTrue(currentCpu.getSubtract(), "N flag was not set correctly");
        assertFalse(currentCpu.getHalfCarry(), "Half Carry flag was not set correctly");
        assertFalse(currentCpu.getZero(), "Zero flag was not set correctly");

    }

    @Test
    void testDecC_ZeroFlagSet() {
        /*
        * Decrement the contents of register C by 1.
        * Checks if the 0 flag set the value correctly.
        */
       int c_register_data = 0x01;

       when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
           0x0D//the opcode
       );
       executeDecrementTest(c_register_data, RegisterType.REGISTER_C, false);

       assertTrue(currentCpu.getSubtract(), "N flag was not set correctly");
       assertFalse(currentCpu.getHalfCarry(), "Half Carry flag was not set correctly");
       assertTrue(currentCpu.getZero(), "Zero flag was not set correctly");
       
   }
    
    private void executeDecrementTest(int register_data, RegisterType register, boolean is16Bit) {
        int expectedValue;
        RegisterType[] filteredRegister;
        int expectedCycles;
        if (is16Bit) {
            expectedValue = (register_data - 1) & 0xFFFF;
            filteredRegister = TestUtils.getPairForRegister(register);
            expectedCycles = 2;
        } else {
            expectedValue = (register_data - 1) & 0xFF;
            filteredRegister = TestUtils.getPairForRegister(register,RegisterType.REGISTER_F );
            expectedCycles = 1;
        }
        
        this.currentCpu.setValueInRegister(register_data, register);

        Map<RegisterType, Integer> registerValues = this.getCpuRegisterValues(filteredRegister);
        long previousCycleCount = currentCpu.getCycles();
        this.currentCpu.cpu_step();
        long currentCycleCount = currentCpu.getCycles();
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisterValues(filteredRegister);

        //Register must be correct
        assertEquals(expectedValue, this.currentCpu.getValueFromRegister(register));
        //cycles must be updated as required
        assertEquals(previousCycleCount + expectedCycles, currentCycleCount, "Cycle count not currently matching.");
        registerValues.computeIfPresent(RegisterType.REGISTER_PC, (t, u) -> u+1);

        assertEquals(registerValues, newRegisterValues);
    }
}
