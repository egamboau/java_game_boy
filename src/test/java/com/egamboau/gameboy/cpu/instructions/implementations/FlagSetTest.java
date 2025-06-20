package com.egamboau.gameboy.cpu.instructions.implementations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.Test;

import com.egamboau.gameboy.cpu.CPUTestBase;
import com.egamboau.gameboy.cpu.instructions.RegisterType;
import com.egamboau.test.TestUtils;

public class FlagSetTest extends CPUTestBase {

    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    void testSetCarryFlag() {
        /*
         * Set the carry flag CY.
         */
        when(this.getCurrentBus().readByteFromAddress(anyInt())).thenReturn(0x37);
        Map<RegisterType, Integer> oldRegisterValues = this.getCpuRegisters(TestUtils.getPairForRegister(RegisterType.F));
        long previousCycleCount = getCurrentCpu().getCycles();
        this.getCurrentCpu().cpuStep();
        long currentCycleCount = getCurrentCpu().getCycles();
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisters(TestUtils.getPairForRegister(RegisterType.F));

        //PC should be incremented by one on the old, so it possible to verify the new one
        oldRegisterValues.computeIfPresent(RegisterType.PC, (t, u) -> u + 1);
        assertEquals(previousCycleCount + 1, currentCycleCount, "Cycle count not matching.");
        assertEquals(oldRegisterValues, newRegisterValues);
        assertTrue(this.getCurrentCpu().getCarry());
    }

}
