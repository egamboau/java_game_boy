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

public class AddTest extends CPUTest{

    @Test
    void testADD_HL_BC() {
        /*
         * Add the contents of register pair BC to the contents of register pair HL, and store the results in register pair HL.
         */
        int lowerByteHL = TestUtils.getRandomIntegerInRange(0, 0xFF) & 0xFF;
        int upperByteHL = TestUtils.getRandomIntegerInRange(0, 0xFF) & 0xFF;

        int lowerByteBC = TestUtils.getRandomIntegerInRange(0, 0xFF) & 0xFF;
        int upperByteBC = TestUtils.getRandomIntegerInRange(0, 0xFF) & 0xFF;

        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x09 //the opcode
        );
        executeAddTest(lowerByteBC, upperByteBC, lowerByteHL, upperByteHL, RegisterType.REGISTER_BC, RegisterType.REGISTER_HL);
    }

    @Test
    void testADD_HL_BC_setCarryFlagCorrectly() {
        /*
         * Add the contents of register pair BC to the contents of register pair HL, and store the results in register pair HL.
         * Checks if the carry flag is set correctly
         */
        int lowerByteHL = 0xFF;
        int upperByteHL = 0xFF;

        int lowerByteBC = 1;
        int upperByteBC = 0;

        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x09 //the opcode
        );

        executeAddTest(lowerByteBC, upperByteBC, lowerByteHL, upperByteHL,  RegisterType.REGISTER_BC, RegisterType.REGISTER_HL);

        assertFalse(this.currentCpu.getSubtract());
        assertTrue(this.currentCpu.getHalfCarry());
        assertTrue(this.currentCpu.getCarry());
    }

    @Test
    void testADD_HL_BC_setHalfCarryFlagCorrectly() {
        /*
         * Add the contents of register pair BC to the contents of register pair HL, and store the results in register pair HL.
         * 
         * Checks if the half carry is set correctly
         */
        int lowerByteHL = 0xFF;
        int upperByteHL = 0x0F;

        int lowerByteBC = 1;
        int upperByteBC = 0;

        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x09 //the opcode
        );
        executeAddTest(lowerByteBC, upperByteBC, lowerByteHL, upperByteHL,  RegisterType.REGISTER_BC, RegisterType.REGISTER_HL);

        assertFalse(this.currentCpu.getSubtract());
        assertTrue(this.currentCpu.getHalfCarry());
        assertFalse(this.currentCpu.getCarry());
    }

    @Test
    void testADD_HL_BC_unsetFlagsCorrectly() {
        /*
         * Add the contents of register pair BC to the contents of register pair HL, and store the results in register pair HL.
         * 
         * Checks if the flags are unset correctly
         */
        int lowerByteHL = 0;
        int upperByteHL = 0;

        int lowerByteBC = 1;
        int upperByteBC = 0;
        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x09 //the opcode
        );
        executeAddTest(lowerByteBC, upperByteBC, lowerByteHL, upperByteHL, RegisterType.REGISTER_BC, RegisterType.REGISTER_HL);

        assertFalse(this.currentCpu.getSubtract());
        assertFalse(this.currentCpu.getHalfCarry());
        assertFalse(this.currentCpu.getCarry());
    }

    @Test
    void testADD_HL_DE() {
        /*
         * Add the contents of register pair DE to the contents of register pair HL, and store the results in register pair HL.
         */
        int lowerByteHL = TestUtils.getRandomIntegerInRange(0, 0xFF) & 0xFF;
        int upperByteHL = TestUtils.getRandomIntegerInRange(0, 0xFF) & 0xFF;

        int lowerByteDE = TestUtils.getRandomIntegerInRange(0, 0xFF) & 0xFF;
        int upperByteDE = TestUtils.getRandomIntegerInRange(0, 0xFF) & 0xFF;

        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x19 //the opcode
        );

        executeAddTest(lowerByteDE, upperByteDE, lowerByteHL, upperByteHL,  RegisterType.REGISTER_DE, RegisterType.REGISTER_HL);
    }

    @Test
    void testADD_HL_DE_setCarryFlagCorrectly() {
        /*
         * Add the contents of register pair DE to the contents of register pair HL, and store the results in register pair HL.
         * Checks if the carry flag is set correctly
         */
        int lowerByteHL = 0xFF;
        int upperByteHL = 0xFF;

        int lowerByteDE = 1;
        int upperByteDE = 0;

        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x19 //the opcode
        );

        executeAddTest(lowerByteDE, upperByteDE, lowerByteHL, upperByteHL,  RegisterType.REGISTER_DE, RegisterType.REGISTER_HL);

        assertFalse(this.currentCpu.getSubtract());
        assertTrue(this.currentCpu.getHalfCarry());
        assertTrue(this.currentCpu.getCarry());
    }

    @Test
    void testADD_HL_DE_setHalfCarryFlagCorrectly() {
        /*
         * Add the contents of register pair DE to the contents of register pair HL, and store the results in register pair HL.
         * 
         * Checks if the half carry is set correctly
         */
        int lowerByteHL = 0xFF;
        int upperByteHL = 0x0F;

        int lowerByteDE = 1;
        int upperByteDE = 0;

        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x19 //the opcode
        );

        executeAddTest(lowerByteDE, upperByteDE, lowerByteHL, upperByteHL,  RegisterType.REGISTER_DE, RegisterType.REGISTER_HL);

        assertFalse(this.currentCpu.getSubtract());
        assertTrue(this.currentCpu.getHalfCarry());
        assertFalse(this.currentCpu.getCarry());
    }

    @Test
    void testADD_HL_DE_unsetFlagsCorrectly() {
        /*
         * Add the contents of register pair DE to the contents of register pair HL, and store the results in register pair HL.
         * 
         * Checks if the flags are unset correctly
         */
        int lowerByteHL = 0;
        int upperByteHL = 0;

        int lowerByteDE = 1;
        int upperByteDE = 0;

        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x19 //the opcode
        );

        executeAddTest(lowerByteDE, upperByteDE, lowerByteHL, upperByteHL,  RegisterType.REGISTER_DE, RegisterType.REGISTER_HL);

        assertFalse(this.currentCpu.getSubtract());
        assertFalse(this.currentCpu.getHalfCarry());
        assertFalse(this.currentCpu.getCarry());
    }


    private void executeAddTest(int firstRegisterLowerByte, int firstRegisterUpperByte, int secondRegisterLowerByte, int secondRegisterUpperByte, RegisterType firstRegister, RegisterType secondRegister) {
        int firstRegisterValue = (firstRegisterUpperByte << 8 | firstRegisterLowerByte);
        int secondRegisterValue = (secondRegisterUpperByte << 8 | secondRegisterLowerByte);

        int expected_value =  firstRegisterValue + secondRegisterValue;

        this.currentCpu.setValueInRegister(firstRegisterValue, firstRegister);
        this.currentCpu.setValueInRegister(secondRegisterValue, secondRegister);

        Map<RegisterType, Integer> registerValues = this.getCpuRegisterValues(TestUtils.getPairForRegister(RegisterType.REGISTER_HL, RegisterType.REGISTER_F));
        long previousCycleCount = currentCpu.getCycles();
        this.currentCpu.cpu_step();
        long currentCycleCount = currentCpu.getCycles();
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisterValues(TestUtils.getPairForRegister(RegisterType.REGISTER_HL, RegisterType.REGISTER_F));

        // HL Value must be updated with the new value.
        assertEquals(expected_value & 0xFFFF, this.currentCpu.getValueFromRegister(secondRegister));
        //Cycle count must match
        assertEquals(previousCycleCount + 2, currentCycleCount, "Cycle count not currently matching.");

        //other flags must be the same, and update the PC to be 1 byte more
        registerValues.computeIfPresent(RegisterType.REGISTER_PC, (t, u) -> u+1);
        assertEquals(registerValues, newRegisterValues);
    }
}
