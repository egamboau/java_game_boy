package com.egamboau.gameboy.cpu.instructions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.Test;

import com.egamboau.gameboy.cpu.CPUTest;
import com.egamboau.test.TestUtils;

public class JumpTest extends CPUTest{

    @Test
    void testJR_PositiveOffset() {
        /*
         * Jump s8 steps from the current address in the program counter (PC). (Jump relative.)
         * Test for positive offset
         */
        int offset = TestUtils.getRandomIntegerInRange(0, 127) & 0xFF;
        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x18, //the opcode
            offset
            );
        
        Map<RegisterType, Integer> oldRegisterValues = this.getCpuRegisterValues();
        long previousCycleCount = currentCpu.getCycles();
        this.currentCpu.cpu_step();
        long currentCycleCount = currentCpu.getCycles();        
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisterValues();

        //pc should increment by 2 (the size of the instruction) + the encoded offset
        oldRegisterValues.computeIfPresent(RegisterType.REGISTER_PC, (t, u) -> (u + 2 + (byte) offset)&0xFFFF);
        assertEquals(previousCycleCount+3, currentCycleCount);
        assertEquals(oldRegisterValues, newRegisterValues);

    }

    @Test
    void testJR_NegativeOffset() {
        /*
         * Jump s8 steps from the current address in the program counter (PC). (Jump relative.)
         * Test for negative offset.
         */
        int offset = TestUtils.getRandomIntegerInRange(-128, -1) & 0xFF;
        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x18, //the opcode
            offset
            );
        
        Map<RegisterType, Integer> oldRegisterValues = this.getCpuRegisterValues();
        long previousCycleCount = currentCpu.getCycles();
        this.currentCpu.cpu_step();
        long currentCycleCount = currentCpu.getCycles();        
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisterValues();

        //pc should increment by 2 (the size of the instruction) + the encoded offset
        oldRegisterValues.computeIfPresent(RegisterType.REGISTER_PC, (t, u) -> (u + 2 + (byte) offset)&0xFFFF);
        assertEquals(previousCycleCount+3, currentCycleCount);
        assertEquals(oldRegisterValues, newRegisterValues);

    }
 
    @Test
    void testJR_NZ_PositiveOffset_ZeroFlagSet() {
        /*
         * If the Z flag is 0, jump s8 steps from the current address stored in the program counter (PC). 
         * If not, the instruction following the current JP instruction is executed (as usual).
         * Tests for positive offset, with the zero flag set
         */
        int offset = TestUtils.getRandomIntegerInRange(0, 127) & 0xFF;
        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x20, //the opcode
            offset
            );
        
        this.currentCpu.setZero(true);
        Map<RegisterType, Integer> oldRegisterValues = this.getCpuRegisterValues();
        long previousCycleCount = currentCpu.getCycles();
        this.currentCpu.cpu_step();
        long currentCycleCount = currentCpu.getCycles();        
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisterValues();

        //pc should increment by 2 (the size of the instruction) + the encoded offset
        oldRegisterValues.computeIfPresent(RegisterType.REGISTER_PC, (t, u) -> u + 2);
        assertEquals(previousCycleCount+2, currentCycleCount);
        assertEquals(oldRegisterValues, newRegisterValues);

    }

    @Test
    void testJR_NZ_NegativeOffset_ZeroFlagSet() {
        /*
         * If the Z flag is 0, jump s8 steps from the current address stored in the program counter (PC). 
         * If not, the instruction following the current JP instruction is executed (as usual).
         * Tests for negative offset, with the zero flag set
         */
        int offset = TestUtils.getRandomIntegerInRange(-128, -1) & 0xFF;
        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x20, //the opcode
            offset
            );
        
        this.currentCpu.setZero(true);
        Map<RegisterType, Integer> oldRegisterValues = this.getCpuRegisterValues();
        long previousCycleCount = currentCpu.getCycles();
        this.currentCpu.cpu_step();
        long currentCycleCount = currentCpu.getCycles();        
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisterValues();

        //pc should increment by 2 (the size of the instruction) + the encoded offset
        oldRegisterValues.computeIfPresent(RegisterType.REGISTER_PC, (t, u) -> u + 2);
        assertEquals(previousCycleCount+2, currentCycleCount);
        assertEquals(oldRegisterValues, newRegisterValues);

    }

    @Test
    void testJR_NZ_PositiveOffset_ZeroFlagNotSet() {
        /*
         * If the Z flag is 0, jump s8 steps from the current address stored in the program counter (PC). 
         * If not, the instruction following the current JP instruction is executed (as usual).
         * Tests for negative offset, with the zero flag not set
         */
        int offset = TestUtils.getRandomIntegerInRange(0, 127) & 0xFF;
        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x20, //the opcode
            offset
            );
        
        this.currentCpu.setZero(false);
        Map<RegisterType, Integer> oldRegisterValues = this.getCpuRegisterValues();
        long previousCycleCount = currentCpu.getCycles();
        this.currentCpu.cpu_step();
        long currentCycleCount = currentCpu.getCycles();        
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisterValues();

        //pc should increment by 2 (the size of the instruction) + the encoded offset
        oldRegisterValues.computeIfPresent(RegisterType.REGISTER_PC, (t, u) -> (u + 2 + (byte) offset)&0xFFFF);
        assertEquals(previousCycleCount+3, currentCycleCount);
        assertEquals(oldRegisterValues, newRegisterValues);

    }

    @Test
    void testJR_NZ_NegativeOffset_ZeroFlagNotSet() {
        /*
         * If the Z flag is 0, jump s8 steps from the current address stored in the program counter (PC). 
         * If not, the instruction following the current JP instruction is executed (as usual).
         * Tests for negative offset, with the zero flag not set
         */
        int offset = TestUtils.getRandomIntegerInRange(-128, -1) & 0xFF;
        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(
            0x20, //the opcode
            offset
            );
        
        this.currentCpu.setZero(false);
        Map<RegisterType, Integer> oldRegisterValues = this.getCpuRegisterValues();
        long previousCycleCount = currentCpu.getCycles();
        this.currentCpu.cpu_step();
        long currentCycleCount = currentCpu.getCycles();        
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisterValues();

        //pc should increment by 2 (the size of the instruction) + the encoded offset
        oldRegisterValues.computeIfPresent(RegisterType.REGISTER_PC, (t, u) -> (u + 2  + (byte)offset)&0xFFFF);
        assertEquals(previousCycleCount+3, currentCycleCount);
        assertEquals(oldRegisterValues, newRegisterValues);

    }
    
}
