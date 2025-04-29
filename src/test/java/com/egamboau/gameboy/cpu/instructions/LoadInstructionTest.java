package com.egamboau.gameboy.cpu.instructions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;
import org.junit.jupiter.api.Test;

import com.egamboau.gameboy.cpu.CPUTest;
import com.egamboau.test.TestUtils;

public class LoadInstructionTest extends CPUTest{

    @Test
    void testLD_BC_d16() {
        /*
         * Load the 2 bytes of immediate data into register pair BC.
         * The first byte of immediate data is the lower byte (i.e., bits 0-7), 
         * and the second byte of immediate data is the higher byte (i.e., bits 8-15).
         */
        int lowerByte = TestUtils.getRandomIntegerInRange(0x00, 0xFF);
        int upperByte = TestUtils.getRandomIntegerInRange(0x00, 0xFF);
        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x01, //the opcode
            lowerByte, //lower byte of data
            upperByte //and the upper byte.
            );
        
        runLoadTestWithInmediateData(upperByte, lowerByte, RegisterType.REGISTER_BC);
    }

    @Test
    void testLD_DE_d16() {
        /*
         * Load the 2 bytes of immediate data into register pair DE.
         * The first byte of immediate data is the lower byte (i.e., bits 0-7), 
         * and the second byte of immediate data is the higher byte (i.e., bits 8-15).
         */
        int lowerByte = TestUtils.getRandomIntegerInRange(0x00, 0xFF);
        int upperByte = TestUtils.getRandomIntegerInRange(0x00, 0xFF);
        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x11, //the opcode
            lowerByte, //lower byte of data
            upperByte //and the upper byte.
            );
        
        runLoadTestWithInmediateData(upperByte, lowerByte, RegisterType.REGISTER_DE);
    }

    @Test
    void testLD_indirect_BC_A() {
        /*
         * Store the contents of register A in the memory location specified by register pair BC.
         */
        int registerData = TestUtils.getRandomIntegerInRange(0x00, 0xFF);
        int b_register_data = TestUtils.getRandomIntegerInRange(0x00, 0xFF);
        int c_register_data = TestUtils.getRandomIntegerInRange(0x00, 0xFF);

        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x02//the opcode
            );

        runLoadTestIndirectRegister(b_register_data, c_register_data,registerData, RegisterType.REGISTER_BC);
    }

    @Test
    void testLD_indirect_DE_A() {
        /*
         * Store the contents of register A in the memory location specified by register pair BC.
         */
        int registerData = TestUtils.getRandomIntegerInRange(0x00, 0xFF);
        int d_register_data = TestUtils.getRandomIntegerInRange(0x00, 0xFF);
        int e_register_data = TestUtils.getRandomIntegerInRange(0x00, 0xFF);

        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x12//the opcode
            );

        runLoadTestIndirectRegister(d_register_data, e_register_data,registerData, RegisterType.REGISTER_DE);
    }

    @Test
    void testLD_B_d8() {
        /*
         * Load the 8-bit immediate operand d8 into register B.
         */
        int data = TestUtils.getRandomIntegerInRange(0x00, 0xFF);
        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x06, //the opcode
            data //lower byte of data
            );
        runLoadTestWithInmediateData(data, RegisterType.REGISTER_B, false);

    }

    @Test
    void testLD_C_d8() {
        /*
         * Load the 8-bit immediate operand d8 into register C.
         */
        int data = TestUtils.getRandomIntegerInRange(0x00, 0xFF);
        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x0E, //the opcode
            data //lower byte of data
            );
        runLoadTestWithInmediateData(data, RegisterType.REGISTER_C, false);

    }

    private void runLoadTestWithInmediateData(int upperByte, int lowerByte, RegisterType register) {

        int expectedData = upperByte << 8 | lowerByte;

        runLoadTestWithInmediateData(expectedData, register, true);
    }

    private void runLoadTestWithInmediateData(int loadedData, RegisterType register, boolean is16Bit) {
        Map<RegisterType, Integer> registerValues = this.getCpuRegisterValues(TestUtils.getPairForRegister(register));
        long previousCycleCount = currentCpu.getCycles();
        this.currentCpu.cpu_step();
        long currentCycleCount = currentCpu.getCycles();
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisterValues(TestUtils.getPairForRegister(register));

        //update the expected values to the ones we want before the comparison
        if(is16Bit) {
            registerValues.computeIfPresent(RegisterType.REGISTER_PC, (t, u) -> u+3);
            previousCycleCount += 3;
        } else {
            registerValues.computeIfPresent(RegisterType.REGISTER_PC, (t, u) -> u+2);
            previousCycleCount += 2;
        }
        
        assertEquals(loadedData,currentCpu.getValueFromRegister(register));
        assertEquals(previousCycleCount, currentCycleCount, "Cycle count not currently matching.");

        assertEquals(registerValues, newRegisterValues);
    }

    private void runLoadTestIndirectRegister(int lowerAddressData, int upperAddressData, int registerData, RegisterType sourceRegisterType) {
        int expectedAddress = (((int) lowerAddressData) << 8) | upperAddressData;

        this.currentCpu.setValueInRegister(registerData, RegisterType.REGISTER_A);
        this.currentCpu.setValueInRegister(expectedAddress, sourceRegisterType);

        Map<RegisterType, Integer> registerValues = this.getCpuRegisterValues();
        long previousCycleCount = currentCpu.getCycles();
        this.currentCpu.cpu_step();
        long currentCycleCount = currentCpu.getCycles();
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisterValues();

        registerValues.computeIfPresent(RegisterType.REGISTER_PC, (t, u) -> u+1);
        assertEquals(registerValues, newRegisterValues);
        assertEquals(previousCycleCount+2, currentCycleCount);
        verify(this.currentBus, times(1)).writeByteToAddress(registerData, expectedAddress);
    }
}
