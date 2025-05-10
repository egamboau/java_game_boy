package com.egamboau.gameboy.cpu.instructions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.Map;
import org.junit.jupiter.api.Test;

import com.egamboau.gameboy.cpu.CPUTest;

public class NoopTest extends CPUTest {
    
    
    @Test
    void testNOP() {
        /*
         * Only advances the program counter by 1. Performs no other operations that would have an effect.
         */
        when(this.currentBus.readByteFromAddress(anyInt())).thenReturn(0x00);
        Map<RegisterType, Integer> oldRegisterValues = this.getCpuRegisterValues();
        long previousCycleCount = currentCpu.getCycles();
        this.currentCpu.cpu_step();
        long currentCycleCount = currentCpu.getCycles();        
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisterValues();

        //PC should be incremented by one on the old, so it possible to verify the new one
        oldRegisterValues.computeIfPresent(RegisterType.REGISTER_PC, (t, u) -> u+1);
        assertEquals(previousCycleCount + 1, currentCycleCount, "Cycle count not matching.");
        assertEquals(oldRegisterValues, newRegisterValues);

    }
}
