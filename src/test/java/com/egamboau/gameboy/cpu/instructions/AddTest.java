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

        executeAddTest(lowerByteHL, upperByteHL, lowerByteBC, upperByteBC);
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

        executeAddTest(lowerByteHL, upperByteHL, lowerByteBC, upperByteBC);

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

        executeAddTest(lowerByteHL, upperByteHL, lowerByteBC, upperByteBC);

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

        executeAddTest(lowerByteHL, upperByteHL, lowerByteBC, upperByteBC);

        assertFalse(this.currentCpu.getSubtract());
        assertFalse(this.currentCpu.getHalfCarry());
        assertFalse(this.currentCpu.getCarry());
    }


    private void executeAddTest(int lowerByteHL, int upperByteHL, int lowerByteBC, int upperByteBC) {
        int expected_value = (upperByteHL << 8 | lowerByteHL) + (upperByteBC << 8 | lowerByteBC);

        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x09 //the opcode
        );
        this.currentCpu.setValueInRegister(upperByteHL, RegisterType.REGISTER_H);
        this.currentCpu.setValueInRegister(lowerByteHL, RegisterType.REGISTER_L);

        this.currentCpu.setValueInRegister(upperByteBC, RegisterType.REGISTER_B);
        this.currentCpu.setValueInRegister(lowerByteBC, RegisterType.REGISTER_C);

        Map<RegisterType, Integer> registerValues = this.getCpuRegisterValues(TestUtils.getPairForRegister(RegisterType.REGISTER_HL, RegisterType.REGISTER_F));
        long previousCycleCount = currentCpu.getCycles();
        this.currentCpu.cpu_step();
        long currentCycleCount = currentCpu.getCycles();
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisterValues(TestUtils.getPairForRegister(RegisterType.REGISTER_HL, RegisterType.REGISTER_F));

        // HL Value must be updated with the new value.
        assertEquals(expected_value & 0xFFFF, this.currentCpu.getValueFromRegister(RegisterType.REGISTER_HL));
        //Cycle count must match
        assertEquals(previousCycleCount + 2, currentCycleCount, "Cycle count not currently matching.");

        //other flags must be the same, and update the PC to be 1 byte more
        registerValues.computeIfPresent(RegisterType.REGISTER_PC, (t, u) -> u+1);
        assertEquals(registerValues, newRegisterValues);
    }
}
