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

public class LoadTest extends CPUTest{

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

        int expectedData = upperByte << 8 | lowerByte;
        runLoadInmediateDataToRegister(expectedData, RegisterType.REGISTER_BC, true);
    }

    @Test
    void testLD_indirect_BC_A() {
        /*
         * Store the contents of register A in the memory location specified by register pair BC.
         */
        int registerData = TestUtils.getRandomIntegerInRange(0x00, 0xFF);

        int b_register_data = TestUtils.getRandomIntegerInRange(0x00, 0xFF);
        int c_register_data = TestUtils.getRandomIntegerInRange(0x00, 0xFF);

        int address = b_register_data << 8 | c_register_data;

        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x02//the opcode
            );

        runLoadRegisterDataIntoIndirectAddress(address, RegisterType.REGISTER_BC, registerData, RegisterType.REGISTER_A);
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
        runLoadInmediateDataToRegister(data, RegisterType.REGISTER_B, false);

    }

    @Test
    void testLD_A_indirectBC() {
        /*
         * Load the 8-bit contents of memory specified by register pair BC into register A.
         */
        
        int b_register_data = TestUtils.getRandomIntegerInRange(0x00, 0xFF);
        int c_register_data = TestUtils.getRandomIntegerInRange(0x00, 0xFF);

        int expectedData = TestUtils.getRandomIntegerInRange(0x00, 0xFF);

        int address = (b_register_data << 8 ) | c_register_data;
        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x0A//the opcode
            );

        when(this.currentBus.readByteFromAddress(address)).thenReturn(
            expectedData
        );

        runLoadMemoryDataIntoRegister(address, RegisterType.REGISTER_BC, expectedData, RegisterType.REGISTER_A);
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
        runLoadInmediateDataToRegister(data, RegisterType.REGISTER_C, false);

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
        
        int expectedData = upperByte << 8 | lowerByte;
        runLoadInmediateDataToRegister(expectedData, RegisterType.REGISTER_DE, true);
    }
 
    @Test
    void testLD_indirect_DE_A() {
        /*
         * Store the contents of register A in the memory location specified by register pair DE.
         */
        int registerData = TestUtils.getRandomIntegerInRange(0x00, 0xFF);
        int d_register_data = TestUtils.getRandomIntegerInRange(0x00, 0xFF);
        int e_register_data = TestUtils.getRandomIntegerInRange(0x00, 0xFF);

        int address = (d_register_data << 8) | e_register_data;

        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x12//the opcode
            );

        runLoadRegisterDataIntoIndirectAddress(address, RegisterType.REGISTER_DE, registerData, RegisterType.REGISTER_A);
    }

    @Test
    void testLD_D_d8() {
        /*
         * Load the 8-bit immediate operand d8 into register D.
         */
        int data = TestUtils.getRandomIntegerInRange(0x00, 0xFF);
        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x16, //the opcode
            data //lower byte of data
            );
        runLoadInmediateDataToRegister(data, RegisterType.REGISTER_D, false);

    }

    @Test
    void testLD_A_indirectDE() {
        /*
         * Load the 8-bit contents of memory specified by register pair DE into register A.
         */
        
        int d_register_data = TestUtils.getRandomIntegerInRange(0x00, 0xFF);
        int e_register_data = TestUtils.getRandomIntegerInRange(0x00, 0xFF);

        int expectedData = TestUtils.getRandomIntegerInRange(0x00, 0xFF);

        int address = (d_register_data << 8 ) | e_register_data;
        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x1A//the opcode
            );

        when(this.currentBus.readByteFromAddress(address)).thenReturn(
            expectedData
        );

        runLoadMemoryDataIntoRegister(address, RegisterType.REGISTER_DE, expectedData, RegisterType.REGISTER_A);
    }

    @Test
    void testLD_E_d8() {
        /*
         * Load the 8-bit immediate operand d8 into register E.
         */
        int data = TestUtils.getRandomIntegerInRange(0x00, 0xFF);
        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x1E, //the opcode
            data //lower byte of data
            );
        runLoadInmediateDataToRegister(data, RegisterType.REGISTER_E, false);

    }

    @Test
    void testLD_HL_d16() {
        /*
         * Load the 2 bytes of immediate data into register pair HL.
         * The first byte of immediate data is the lower byte (i.e., bits 0-7), 
         * and the second byte of immediate data is the higher byte (i.e., bits 8-15).
         */
        int lowerByte = TestUtils.getRandomIntegerInRange(0x00, 0xFF);
        int upperByte = TestUtils.getRandomIntegerInRange(0x00, 0xFF);
        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x21, //the opcode
            lowerByte, //lower byte of data
            upperByte //and the upper byte.
            );
        
        int expectedData = (upperByte << 8 | lowerByte);
        runLoadInmediateDataToRegister(expectedData, RegisterType.REGISTER_HL, true);
    }
 
    @Test
    void testLD_indirect_HLI_A() {
        /*
         * Store the contents of register A into the memory location specified by register pair HL, and simultaneously increment the contents of HL.
         */
        int registerData = TestUtils.getRandomIntegerInRange(0x00, 0xFF);
        int d_register_data = TestUtils.getRandomIntegerInRange(0x00, 0xFF);
        int e_register_data = TestUtils.getRandomIntegerInRange(0x00, 0xFF);

        int address = (d_register_data << 8) | e_register_data;

        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x22//the opcode
            );

            runLoadRegisterDataIntoIndirectAddressWithSourceIncrement(address, RegisterType.REGISTER_HL, registerData, RegisterType.REGISTER_A);
    }

    @Test
    void testLD_H_d8() {
        /*
         * Load the 8-bit immediate operand d8 into register H.
         */
        int data = TestUtils.getRandomIntegerInRange(0x00, 0xFF);
        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x26, //the opcode
            data //lower byte of data
            );
        runLoadInmediateDataToRegister(data, RegisterType.REGISTER_H, false);

    }

    @Test
    void testLD_A_indirectHLI() {
        /*
         * Load the contents of memory specified by register pair HL into register A, and simultaneously increment the contents of HL.
         */
        
        int h_register_data = TestUtils.getRandomIntegerInRange(0x00, 0xFF);
        int l_register_data = TestUtils.getRandomIntegerInRange(0x00, 0xFF);

        int expectedData = TestUtils.getRandomIntegerInRange(0x00, 0xFF);

        int address = (h_register_data << 8 ) | l_register_data;
        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x2A//the opcode
            );

        when(this.currentBus.readByteFromAddress(address)).thenReturn(
            expectedData
        );

        runLoadMemoryDataIntoRegisterWithSourceIncremenet(address, RegisterType.REGISTER_HL, expectedData, RegisterType.REGISTER_A);
    }

    @Test
    void testLD_L_d8() {
        /*
         * Load the 8-bit immediate operand d8 into register E.
         */
        int data = TestUtils.getRandomIntegerInRange(0x00, 0xFF);
        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x2E, //the opcode
            data //lower byte of data
            );
        runLoadInmediateDataToRegister(data, RegisterType.REGISTER_L, false);

    }


    private void runLoadInmediateDataToRegister(int loadedData, RegisterType register, boolean is16Bit) {
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

    private void runLoadRegisterDataIntoIndirectAddress(int address, RegisterType addressRegister,  int registerData, RegisterType sourceRegister) {

        this.currentCpu.setValueInRegister(registerData, sourceRegister);
        this.currentCpu.setValueInRegister(address, addressRegister);

        Map<RegisterType, Integer> registerValues = this.getCpuRegisterValues();
        long previousCycleCount = currentCpu.getCycles();
        this.currentCpu.cpu_step();
        long currentCycleCount = currentCpu.getCycles();
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisterValues();

        registerValues.computeIfPresent(RegisterType.REGISTER_PC, (t, u) -> u+1);
        assertEquals(registerValues, newRegisterValues);
        assertEquals(previousCycleCount+2, currentCycleCount);
        verify(this.currentBus, times(1)).writeByteToAddress(registerData, address);
    }

    private void runLoadMemoryDataIntoRegister(int address, RegisterType addressRegister,  int registerData, RegisterType destinationRegister) {

        this.currentCpu.setValueInRegister(address, addressRegister);

        Map<RegisterType, Integer> registerValues = this.getCpuRegisterValues(TestUtils.getPairForRegister(destinationRegister));
        long previousCycleCount = currentCpu.getCycles();
        this.currentCpu.cpu_step();
        long currentCycleCount = currentCpu.getCycles();
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisterValues(TestUtils.getPairForRegister(destinationRegister));

        registerValues.computeIfPresent(RegisterType.REGISTER_PC, (t, u) -> u+1);
        assertEquals(registerValues, newRegisterValues);
        assertEquals(previousCycleCount+2, currentCycleCount);
        assertEquals(registerData, currentCpu.getValueFromRegister(destinationRegister));
    }

    private void runLoadRegisterDataIntoIndirectAddressWithSourceIncrement(int address, RegisterType addressRegister,  int registerData, RegisterType sourceRegister) {

        this.currentCpu.setValueInRegister(registerData, sourceRegister);
        this.currentCpu.setValueInRegister(address, addressRegister);

        Map<RegisterType, Integer> registerValues = this.getCpuRegisterValues(TestUtils.getPairForRegister(addressRegister));
        long previousCycleCount = currentCpu.getCycles();
        this.currentCpu.cpu_step();
        long currentCycleCount = currentCpu.getCycles();
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisterValues(TestUtils.getPairForRegister(addressRegister));

        registerValues.computeIfPresent(RegisterType.REGISTER_PC, (t, u) -> u+1);
        registerValues.computeIfPresent(addressRegister, (t, u) -> u+1);
        assertEquals(registerValues, newRegisterValues);
        assertEquals(previousCycleCount+2, currentCycleCount);
        assertEquals(address+1, currentCpu.getValueFromRegister(addressRegister));
        verify(this.currentBus, times(1)).writeByteToAddress(registerData, address);
    }

    private void runLoadMemoryDataIntoRegisterWithSourceIncremenet(int address, RegisterType addressRegister, int expectedData, RegisterType destinationRegister) {
        this.currentCpu.setValueInRegister(address, addressRegister);

        Map<RegisterType, Integer> registerValues = this.getCpuRegisterValues(TestUtils.getPairForRegister(destinationRegister, addressRegister));
        long previousCycleCount = currentCpu.getCycles();
        this.currentCpu.cpu_step();
        long currentCycleCount = currentCpu.getCycles();
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisterValues(TestUtils.getPairForRegister(destinationRegister, addressRegister));

        registerValues.computeIfPresent(RegisterType.REGISTER_PC, (t, u) -> u+1);
        assertEquals(registerValues, newRegisterValues);
        assertEquals(previousCycleCount+2, currentCycleCount);
        assertEquals(address+1, currentCpu.getValueFromRegister(addressRegister));
        assertEquals(expectedData, currentCpu.getValueFromRegister(destinationRegister));
        verify(this.currentBus, times(1)).readByteFromAddress(address);
    }
}
