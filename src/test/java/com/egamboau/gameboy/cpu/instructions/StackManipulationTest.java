package com.egamboau.gameboy.cpu.instructions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;
import org.junit.jupiter.api.Test;

import com.egamboau.gameboy.cpu.CPUTestBase;

class StackManipulationTest extends CPUTestBase{

    @Test
    void testLD_n16_SP() {
        /*
         * Store the lower byte of stack pointer SP at the address specified by the 16-bit immediate operand a16, and store the upper byte of SP at address a16 + 1.
         */
        when(this.getCurrentBus().readByteFromAddress(anyInt())).thenReturn(
            0x08, //the opcode
            0x34, //lower byte of data
            0x12 //higher byte of data
            );

        this.getCurrentCpu().setValueInRegister(0xC001 & 0xFFFF, RegisterType.SP);

        Map<RegisterType, Integer> registerValues = this.getCpuRegisters();
        long previousCycleCount = getCurrentCpu().getCycles();
        this.getCurrentCpu().cpuStep();
        long currentCycleCount = getCurrentCpu().getCycles();
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisters();
        registerValues.computeIfPresent(RegisterType.PC, (t, u) -> u+3);
        assertEquals(registerValues, newRegisterValues);
        //verify that the memory address is correct

        assertEquals(previousCycleCount+5, currentCycleCount);
        verify(this.getCurrentBus(), times(1)).writeByteToAddress(0x01, 0x1234);
        verify(this.getCurrentBus(), times(1)).writeByteToAddress(0xC0, 0x1235);
    }


    @Test
    void testLD_SP_n16() {
        /*
         * Load the 2 bytes of immediate data into register pair SP.
         * The first byte of immediate data is the lower byte (i.e., bits 0-7), and the second byte of immediate data is the higher byte (i.e., bits 8-15).
         */
        when(this.getCurrentBus().readByteFromAddress(anyInt())).thenReturn(
            0x31, //the opcode
            0x34, //lower byte of data
            0x12 //higher byte of data
            );

        this.getCurrentCpu().setValueInRegister(0xC001 , RegisterType.SP);

        Map<RegisterType, Integer> registerValues = this.getCpuRegisters(RegisterType.SP);
        long previousCycleCount = getCurrentCpu().getCycles();
        this.getCurrentCpu().cpuStep();
        long currentCycleCount = getCurrentCpu().getCycles();
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisters(RegisterType.SP);
        
        registerValues.computeIfPresent(RegisterType.PC, (t, u) -> u+3);
        assertEquals(registerValues, newRegisterValues);
        assertEquals(0x1234, getCurrentCpu().getValueFromRegister(RegisterType.SP));
        assertEquals(previousCycleCount+3, currentCycleCount);
    }

}
