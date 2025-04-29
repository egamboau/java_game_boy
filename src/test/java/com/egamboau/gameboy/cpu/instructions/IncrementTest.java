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

public class IncrementTest  extends CPUTest{

    @Test
    void testIncBC() {
        /*
         * Increment the contents of register pair BC by 1.
         */
        int b_register_data = TestUtils.getRandomIntegerInRange(0x00, 0xFF);
        int c_register_data = TestUtils.getRandomIntegerInRange(0x00, 0xFF);

        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x03//the opcode
        );
        executeIncrementTest(b_register_data, c_register_data, RegisterType.REGISTER_BC);
    }

    @Test
    void testIncDE() {
        /*
         * Increment the contents of register pair BC by 1.
         */
        int d_register_data = TestUtils.getRandomIntegerInRange(0x00, 0xFF);
        int e_register_data = TestUtils.getRandomIntegerInRange(0x00, 0xFF);

        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x13//the opcode
        );
        executeIncrementTest(d_register_data, e_register_data, RegisterType.REGISTER_DE);
    }
    
    @Test
    void testIncBWithZeroVAlue() {
        /*
         * Increment the contents of register B by 1.
         * Initializes the register to 0 first, and then test the result
         */
        int b_register_data = 0;
        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x04//the opcode
        );

        executeIncTestWithSingleRegister(b_register_data, RegisterType.REGISTER_B);

        assertFalse(currentCpu.getSubtract(), "N flag was not set correctly");
        assertFalse(currentCpu.getHalfCarry(), "Half Carry flag was not set correctly");
        assertFalse(currentCpu.getZero(), "Zero flag was not set correctly");

    }

    @Test
    void testIncB_HalfCarrySet() {
        /*
         * Increment the contents of register B by 1.
         * Checks if half carry is set as needed
         */
        int b_register_data = 0x0F;
        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x04//the opcode
        );

        executeIncTestWithSingleRegister(b_register_data, RegisterType.REGISTER_B);
        
        assertFalse(currentCpu.getSubtract(), "N flag was not set correctly");
        assertTrue(currentCpu.getHalfCarry(), "Half Carry flag was not set correctly");
        assertFalse(currentCpu.getZero(), "Zero flag was not set correctly");

    }

    @Test
    void testIncB_HalfCarryNotSet() {
        /*
         * Increment the contents of register B by 1.
         * Checks if half carry is not set after the addition
         */
        int b_register_data = 0x0E;
        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x04//the opcode
        );

        executeIncTestWithSingleRegister(b_register_data, RegisterType.REGISTER_B);

        assertFalse(currentCpu.getSubtract(), "N flag was not set correctly");
        assertFalse(currentCpu.getHalfCarry(), "Half Carry flag was not set correctly");
        assertFalse(currentCpu.getZero(), "Zero flag was not set correctly");
    }

    @Test
    void testIncB_ZeroFlagSet() {
        /*
         * Increment the contents of register B by 1.
         * Test if the Zero flag is set when result is 0
         */
        int b_register_data = 0xFF;
        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x04//the opcode
        );

        executeIncTestWithSingleRegister(b_register_data, RegisterType.REGISTER_B);
        assertFalse(currentCpu.getSubtract(), "N flag was not set correctly");
        assertTrue(currentCpu.getHalfCarry(), "Half Carry flag was not set correctly");
        assertTrue(currentCpu.getZero(), "Zero flag was not set correctly");
    }

    @Test
    void testIncCWithZeroVAlue() {
        /*
         * Increment the contents of register C by 1.
         * Initializes the register to 0 first, and then test the result
         */
        int c_register_data = 0;
        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x0C//the opcode
        );

        executeIncTestWithSingleRegister(c_register_data, RegisterType.REGISTER_C);

        assertFalse(currentCpu.getSubtract(), "N flag was not set correctly");
        assertFalse(currentCpu.getHalfCarry(), "Half Carry flag was not set correctly");
        assertFalse(currentCpu.getZero(), "Zero flag was not set correctly");

    }

    @Test
    void testIncC_HalfCarrySet() {
        /*
         * Increment the contents of register C by 1..
         * Checks if half carry is set as needed
         */
        int C_register_data = 0x0F;
        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x0C//the opcode
        );

        executeIncTestWithSingleRegister(C_register_data, RegisterType.REGISTER_C);
        
        assertFalse(currentCpu.getSubtract(), "N flag was not set correctly");
        assertTrue(currentCpu.getHalfCarry(), "Half Carry flag was not set correctly");
        assertFalse(currentCpu.getZero(), "Zero flag was not set correctly");

    }

    @Test
    void testIncC_HalfCarryNotSet() {
        /*
         * Increment the contents of register C by 1.
         * Checks if half carry is not set after the addition
         */
        int c_register_data = 0x0E;
        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x0C//the opcode
        );

        executeIncTestWithSingleRegister(c_register_data, RegisterType.REGISTER_C);

        assertFalse(currentCpu.getSubtract(), "N flag was not set correctly");
        assertFalse(currentCpu.getHalfCarry(), "Half Carry flag was not set correctly");
        assertFalse(currentCpu.getZero(), "Zero flag was not set correctly");
    }

    @Test
    void testIncC_ZeroFlagSet() {
        /*
         * Increment the contents of register B by 1.
         * Test if the Zero flag is set when result is 0
         */
        int b_register_data = 0xFF;
        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0xC//the opcode
        );

        executeIncTestWithSingleRegister(b_register_data, RegisterType.REGISTER_C);
        assertFalse(currentCpu.getSubtract(), "N flag was not set correctly");
        assertTrue(currentCpu.getHalfCarry(), "Half Carry flag was not set correctly");
        assertTrue(currentCpu.getZero(), "Zero flag was not set correctly");
    }

    private void executeIncrementTest(int lowerRegisterData, int upperRegisterData, RegisterType register) {
        int number = ((lowerRegisterData << 8) | upperRegisterData);
        int expected = (number + 1) & 0xFFFF;
        this.currentCpu.setValueInRegister(number, register);

        RegisterType[] filter = TestUtils.getPairForRegister(register, RegisterType.REGISTER_F);
        Map<RegisterType, Integer> registerValues = this.getCpuRegisterValues(filter);
        long previousCycleCount = currentCpu.getCycles();
        this.currentCpu.cpu_step();
        long currentCycleCount = currentCpu.getCycles();
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisterValues(filter);

        //verify the increment
        int result = currentCpu.getValueFromRegister(register);
        
        assertEquals(previousCycleCount+2, currentCycleCount);
        assertEquals(expected, result);
        registerValues.computeIfPresent(RegisterType.REGISTER_PC, (t, u) -> u+1);

        assertEquals(registerValues, newRegisterValues);
    }

    private void executeIncTestWithSingleRegister(int registerData, RegisterType registerType) {
        this.currentCpu.setValueInRegister(registerData, registerType);
        Map<RegisterType, Integer> registerValues = this.getCpuRegisterValues(TestUtils.getPairForRegister(registerType, RegisterType.REGISTER_F));
        this.currentCpu.cpu_step();
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisterValues(TestUtils.getPairForRegister(registerType, RegisterType.REGISTER_F));
        int expectedValue=(registerData + 1) & 0xFF;
        assertEquals(expectedValue, this.currentCpu.getValueFromRegister(registerType));
        registerValues.computeIfPresent(RegisterType.REGISTER_PC, (t, u) -> u+1);

        assertEquals(registerValues, newRegisterValues);
    }


}
