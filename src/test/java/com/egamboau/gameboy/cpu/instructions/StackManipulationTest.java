package com.egamboau.gameboy.cpu.instructions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;
import org.junit.jupiter.api.Test;

import com.egamboau.gameboy.cpu.CPUTest;

public class StackManipulationTest extends CPUTest{

    @Test
    void testLD_n16_SP() {
        /*
         * Store the lower byte of stack pointer SP at the address specified by the 16-bit immediate operand a16, and store the upper byte of SP at address a16 + 1.
         */
        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x08, //the opcode
            0x34, //lower byte of data
            0x12 //higher byte of data
            );

        this.currentCpu.setValueInRegister(0xC001 & 0xFFFF, RegisterType.REGISTER_SP);

        Map<RegisterType, Integer> registerValues = this.getCpuRegisterValues();
        long previousCycleCount = currentCpu.getCycles();
        this.currentCpu.cpu_step();
        long currentCycleCount = currentCpu.getCycles();
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisterValues();
        registerValues.computeIfPresent(RegisterType.REGISTER_PC, (t, u) -> u+3);
        assertEquals(registerValues, newRegisterValues);
        //verify that the memory address is correct

        assertEquals(previousCycleCount+5, currentCycleCount);
        verify(this.currentBus, times(1)).writeByteToAddress(0x01, 0x1234);
        verify(this.currentBus, times(1)).writeByteToAddress(0xC0, 0x1235);
    }

}
