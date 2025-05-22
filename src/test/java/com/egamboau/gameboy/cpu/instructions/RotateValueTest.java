package com.egamboau.gameboy.cpu.instructions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.Map;
import org.junit.jupiter.api.Test;

import com.egamboau.gameboy.cpu.CPUTestBase;
import com.egamboau.test.TestUtils;

class RotateValueTest extends CPUTestBase{
    
    @Test
    void test_RLCA_CarryFlagSet() {
        /*
         * Rotate the contents of register A to the left. That is, the contents of bit 0 are copied to bit 1, 
         * and the previous contents of bit 1 (before the copy operation) are copied to bit 2. 
         * The same operation is repeated in sequence for the rest of the register. 
         * The contents of bit 7 are placed in both the CY flag and bit 0 of register A.
         * 
         * This test that the carry flag is set when needed
         */
        
        int registerData = 0xC9 & 0xFF;
        int expectedValue = 0x93 & 0xFF;

        runRlcaTest(registerData, expectedValue);
        //check the flags
        assertFalse(getCurrentCpu().getZero());
        assertFalse(getCurrentCpu().getSubtract());
        assertFalse(getCurrentCpu().getHalfCarry());
        assertTrue(getCurrentCpu().getCarry());
    }

    @Test
    void test_RLCA_CarryFlagUnset() {
        /*
         * Rotate the contents of register A to the left. That is, the contents of bit 0 are copied to bit 1, 
         * and the previous contents of bit 1 (before the copy operation) are copied to bit 2. 
         * The same operation is repeated in sequence for the rest of the register. 
         * The contents of bit 7 are placed in both the CY flag and bit 0 of register A.
         * 
         * This test that the carry flag is reset when needed
         */

        int registerData = 0x7F & 0xFF;
        int expectedValue = 0xFE & 0xFF;

        runRlcaTest(registerData, expectedValue);

        assertFalse(getCurrentCpu().getZero());
        assertFalse(getCurrentCpu().getSubtract());
        assertFalse(getCurrentCpu().getHalfCarry());
        assertFalse(getCurrentCpu().getCarry());

    }

    @Test
    void test_RLCA_Loop() {
        /*
         * Rotate the contents of register A to the left. That is, the contents of bit 0 are copied to bit 1, 
         * and the previous contents of bit 1 (before the copy operation) are copied to bit 2. 
         * The same operation is repeated in sequence for the rest of the register. 
         * The contents of bit 7 are placed in both the CY flag and bit 0 of register A.
         * 
         * This test 8 values in a loop and check results
         */

        int registerData = 0x01 & 0xFF;
        getCurrentCpu().setValueInRegister(registerData, RegisterType.A);
        int[] expectedValues = {0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x01};

        for (int expectedValue: expectedValues) {
            //use the actual register A for the test.
            runRlcaTest(getCurrentCpu().getValueFromRegister(RegisterType.A), expectedValue);
        }
    }

    @Test
    void test_RRCA_CarryFlagSet() {
        /*
         * Rotate the contents of register A to the left. That is, the contents of bit 0 are copied to bit 1, 
         * and the previous contents of bit 1 (before the copy operation) are copied to bit 2. 
         * The same operation is repeated in sequence for the rest of the register. 
         * The contents of bit 7 are placed in both the CY flag and bit 0 of register A.
         * 
         * This test that the carry flag is set when needed
         */
        
        int registerData = 0xF1 & 0xFF;
        int expectedValue = 0xF8 & 0xFF;

        runRrcaTest(registerData, expectedValue);
        //check the flags
        assertFalse(getCurrentCpu().getZero());
        assertFalse(getCurrentCpu().getSubtract());
        assertFalse(getCurrentCpu().getHalfCarry());
        assertTrue(getCurrentCpu().getCarry());
    }

    @Test
    void test_RRCA_CarryFlagUnset() {
        /*
         * Rotate the contents of register A to the left. That is, the contents of bit 0 are copied to bit 1, 
         * and the previous contents of bit 1 (before the copy operation) are copied to bit 2. 
         * The same operation is repeated in sequence for the rest of the register. 
         * The contents of bit 7 are placed in both the CY flag and bit 0 of register A.
         * 
         * This test that the carry flag is reset when needed
         */

        int registerData = 0xFE & 0xFF;
        int expectedValue = 0x7F & 0xFF;

        runRrcaTest(registerData, expectedValue);

        assertFalse(getCurrentCpu().getZero());
        assertFalse(getCurrentCpu().getSubtract());
        assertFalse(getCurrentCpu().getHalfCarry());
        assertFalse(getCurrentCpu().getCarry());

    }

    @Test
    void test_RRCA_Loop() {
        /*
         * Rotate the contents of register A to the left. That is, the contents of bit 0 are copied to bit 1, 
         * and the previous contents of bit 1 (before the copy operation) are copied to bit 2. 
         * The same operation is repeated in sequence for the rest of the register. 
         * The contents of bit 7 are placed in both the CY flag and bit 0 of register A.
         * 
         * This test 8 values in a loop and check results
         */

        int registerData = 0x01 & 0xFF;
        getCurrentCpu().setValueInRegister(registerData, RegisterType.A);
        int[] expectedValues = {0x80, 0x40, 0x20, 0x10, 0x08, 0x04, 0x02, 0x01};

        for (int expectedValue: expectedValues) {
            //use the actual register A for the test.
            runRrcaTest(getCurrentCpu().getValueFromRegister(RegisterType.A), expectedValue);
        }
    }

    @Test
    void test_RLA_CarryFlagSet() {
        /*
         * Rotate the contents of register A to the left, through the carry (CY) flag. 
         * That is, the contents of bit 0 are copied to bit 1, and the previous contents of bit 1 (before the copy operation) are copied to bit 2. 
         * The same operation is repeated in sequence for the rest of the register. The previous contents of the carry flag are copied to bit 0.
         * 
         * This test that the carry flag is set when needed
         */
        
        int registerData = 0x80 & 0xFF;
        int expectedValue = 0x00 & 0xFF;

        runRlaTest(registerData, expectedValue);
        //check the flags
        assertFalse(getCurrentCpu().getZero());
        assertFalse(getCurrentCpu().getSubtract());
        assertFalse(getCurrentCpu().getHalfCarry());
        assertTrue(getCurrentCpu().getCarry());
    }

    @Test
    void test_RLA_CarryFlagUnset() {
        /*
         * Rotate the contents of register A to the left, through the carry (CY) flag. 
         * That is, the contents of bit 0 are copied to bit 1, and the previous contents of bit 1 (before the copy operation) are copied to bit 2. 
         * The same operation is repeated in sequence for the rest of the register. The previous contents of the carry flag are copied to bit 0.
         * 
         * This test that the carry flag is reset when needed
         */

        int registerData = 0x40 & 0xFF;
        int expectedValue = 0x80 & 0xFF;

        getCurrentCpu().setCarry(true);

        runRlcaTest(registerData, expectedValue);

        assertFalse(getCurrentCpu().getZero());
        assertFalse(getCurrentCpu().getSubtract());
        assertFalse(getCurrentCpu().getHalfCarry());
        assertFalse(getCurrentCpu().getCarry());

    }

    @Test
    void test_RLA_Loop() {
        /*
         * Rotate the contents of register A to the left, through the carry (CY) flag. 
         * That is, the contents of bit 0 are copied to bit 1, and the previous contents of bit 1 (before the copy operation) are copied to bit 2. 
         * The same operation is repeated in sequence for the rest of the register. The previous contents of the carry flag are copied to bit 0.
         * 
         * This test 8 values in a loop and check results
         */

        int registerData = 0x01 & 0xFF;
        getCurrentCpu().setValueInRegister(registerData, RegisterType.A);
        int[] expectedValues = {0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x00};

        for (int expectedValue: expectedValues) {
            //use the actual register A for the test.
            runRlaTest(getCurrentCpu().getValueFromRegister(RegisterType.A), expectedValue);
        }
        assertTrue(getCurrentCpu().getCarry());
    }
    
    @Test
    void test_RRA_CarryFlagSet() {
        /*
         * Rotate the contents of register A to the right, through the carry (CY) flag. 
         * That is, the contents of bit 7 are copied to bit 6, and the previous contents of bit 6 (before the copy) are copied to bit 5. 
         * The same operation is repeated in sequence for the rest of the register. The previous contents of the carry flag are copied to bit 7.
         * 
         * This test that the carry flag is set when needed
         */
        
        int registerData = 0x01 & 0xFF;
        int expectedValue = 0x80 & 0xFF;

        runRraTest(registerData, expectedValue);
        //check the flags
        assertFalse(getCurrentCpu().getZero());
        assertFalse(getCurrentCpu().getSubtract());
        assertFalse(getCurrentCpu().getHalfCarry());
        assertTrue(getCurrentCpu().getCarry());
    }

    @Test
    void test_RRA_CarryFlagUnset() {
        /*
         * Rotate the contents of register A to the right, through the carry (CY) flag. 
         * That is, the contents of bit 7 are copied to bit 6, and the previous contents of bit 6 (before the copy) are copied to bit 5. 
         * The same operation is repeated in sequence for the rest of the register. The previous contents of the carry flag are copied to bit 7.
         * 
         * This test that the carry flag is reset when needed
         */

        int registerData = 0x40 & 0xFF;
        int expectedValue = 0x20 & 0xFF;

        getCurrentCpu().setCarry(true);

        runRraTest(registerData, expectedValue);

        assertFalse(getCurrentCpu().getZero());
        assertFalse(getCurrentCpu().getSubtract());
        assertFalse(getCurrentCpu().getHalfCarry());
        assertFalse(getCurrentCpu().getCarry());

    }

    @Test
    void test_RRA_Loop() {
        /*
         * Rotate the contents of register A to the right, through the carry (CY) flag. 
         * That is, the contents of bit 7 are copied to bit 6, and the previous contents of bit 6 (before the copy) are copied to bit 5. 
         * The same operation is repeated in sequence for the rest of the register. The previous contents of the carry flag are copied to bit 7.
         * 
         * This test 8 values in a loop and check results
         */

        int registerData = 0x01 & 0xFF;
        getCurrentCpu().setValueInRegister(registerData, RegisterType.A);
        int[] expectedValues = {0x80, 0x40, 0x20, 0x10, 0x08, 0x04, 0x02, 0x01};

        for (int expectedValue: expectedValues) {
            //use the actual register A for the test.
            runRraTest(getCurrentCpu().getValueFromRegister(RegisterType.A), expectedValue);
        }
        assertFalse(getCurrentCpu().getCarry());
    }
    

    private void runRlcaTest(int registerData, int expectedValue) {
        when(this.getCurrentBus().readByteFromAddress(anyInt())).thenReturn(
            0x07 //the opcode
            );

        this.getCurrentCpu().setValueInRegister(registerData, RegisterType.A);
        Map<RegisterType, Integer> oldRegisterValues = this.getCpuRegisters(TestUtils.getPairForRegister(RegisterType.AF));
        long previousCycleCount = getCurrentCpu().getCycles();
        this.getCurrentCpu().cpuStep();
        long currentCycleCount = getCurrentCpu().getCycles();        
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisters(TestUtils.getPairForRegister(RegisterType.AF));

        oldRegisterValues.computeIfPresent(RegisterType.PC, (t, u) -> u+1);
        assertEquals(previousCycleCount+1, currentCycleCount);
        assertEquals(oldRegisterValues, newRegisterValues);
        assertEquals(expectedValue, getCurrentCpu().getValueFromRegister(RegisterType.A));
        
    }

    private void runRrcaTest(int registerData, int expectedValue) {
        when(this.getCurrentBus().readByteFromAddress(anyInt())).thenReturn(
            0x0F //the opcode
            );

        this.getCurrentCpu().setValueInRegister(registerData, RegisterType.A);
        Map<RegisterType, Integer> oldRegisterValues = this.getCpuRegisters(TestUtils.getPairForRegister(RegisterType.AF));
        long previousCycleCount = getCurrentCpu().getCycles();
        this.getCurrentCpu().cpuStep();
        long currentCycleCount = getCurrentCpu().getCycles();        
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisters(TestUtils.getPairForRegister(RegisterType.AF));

        oldRegisterValues.computeIfPresent(RegisterType.PC, (t, u) -> u+1);
        assertEquals(previousCycleCount+1, currentCycleCount);
        assertEquals(oldRegisterValues, newRegisterValues);
        assertEquals(expectedValue, getCurrentCpu().getValueFromRegister(RegisterType.A));
    }

    private void runRlaTest(int registerData, int expectedValue) {
        when(this.getCurrentBus().readByteFromAddress(anyInt())).thenReturn(
            0x17 //the opcode
            );
        this.getCurrentCpu().setValueInRegister(registerData, RegisterType.A);
        Map<RegisterType, Integer> oldRegisterValues = this.getCpuRegisters(TestUtils.getPairForRegister(RegisterType.AF));
        long previousCycleCount = getCurrentCpu().getCycles();
        this.getCurrentCpu().cpuStep();
        long currentCycleCount = getCurrentCpu().getCycles();        
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisters(TestUtils.getPairForRegister(RegisterType.AF));

        oldRegisterValues.computeIfPresent(RegisterType.PC, (t, u) -> u+1);
        assertEquals(previousCycleCount+1, currentCycleCount);
        assertEquals(oldRegisterValues, newRegisterValues);
        assertEquals(expectedValue, getCurrentCpu().getValueFromRegister(RegisterType.A));
    }

    private void runRraTest(int registerData, int expectedValue) {
        when(this.getCurrentBus().readByteFromAddress(anyInt())).thenReturn(
            0x1F //the opcode
        );
        this.getCurrentCpu().setValueInRegister(registerData, RegisterType.A);
        Map<RegisterType, Integer> oldRegisterValues = this.getCpuRegisters(TestUtils.getPairForRegister(RegisterType.AF));
        long previousCycleCount = getCurrentCpu().getCycles();
        this.getCurrentCpu().cpuStep();
        long currentCycleCount = getCurrentCpu().getCycles();        
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisters(TestUtils.getPairForRegister(RegisterType.AF));

        oldRegisterValues.computeIfPresent(RegisterType.PC, (t, u) -> u+1);
        assertEquals(previousCycleCount+1, currentCycleCount);
        assertEquals(oldRegisterValues, newRegisterValues);
        assertEquals(expectedValue, getCurrentCpu().getValueFromRegister(RegisterType.A));
    }
}
