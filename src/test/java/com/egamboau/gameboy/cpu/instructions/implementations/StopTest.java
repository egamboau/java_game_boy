package com.egamboau.gameboy.cpu.instructions.implementations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.Test;

import com.egamboau.gameboy.cpu.CPUTestBase;
import com.egamboau.gameboy.cpu.instructions.RegisterType;

class StopTest extends CPUTestBase {

    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    void testStop() {
        /*
         * Execution of a STOP instruction stops both the system clock and oscillator circuit.
         * STOP mode is entered and the LCD controller also stops. However, the status of the internal RAM register ports remains unchanged.
         * STOP mode can be cancelled by a reset signal.
         * If the RESET terminal goes LOW in STOP mode, it becomes that of a normal reset status.
         * The following conditions should be met before a STOP instruction is executed and stop mode is entered:
         * - All interrupt-enable (IE) flags are reset.
         * - Input to P10-P13 is LOW for all.
         */
        when(this.getCurrentBus().readByteFromAddress(anyInt())).thenReturn(0x10);
        Map<RegisterType, Integer> oldRegisterValues = this.getCpuRegisters();
        long previousCycleCount = getCurrentCpu().getCycles();
        this.getCurrentCpu().cpuStep();
        long currentCycleCount = getCurrentCpu().getCycles();
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisters();

        //PC should be incremented by one on the old, so it possible to verify the new one
        oldRegisterValues.computeIfPresent(RegisterType.PC, (t, u) -> u + 1);
        assertEquals(previousCycleCount + 1, currentCycleCount, "Cycle count not matching.");
        assertEquals(oldRegisterValues, newRegisterValues);
        assertTrue(getCurrentCpu().isHalted());
    }

}
