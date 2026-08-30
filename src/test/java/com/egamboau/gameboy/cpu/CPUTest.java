package com.egamboau.gameboy.cpu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

import com.egamboau.gameboy.cpu.instructions.RegisterType;

class CPUTest extends CPUTestBase {

    @Test
    void haltedCpuOnlyAdvancesCycles() {
        getCurrentCpu().setHalted(true);

        getCurrentCpu().cpuStep();

        assertEquals(1, getCurrentCpu().getCycles());
        verify(getCurrentBus(), never()).readByteFromAddress(0);
    }

    @Test
    void stoppedCpuOnlyAdvancesCycles() {
        getCurrentCpu().setStopped(true);

        getCurrentCpu().cpuStep();

        assertEquals(1, getCurrentCpu().getCycles());
        verify(getCurrentBus(), never()).readByteFromAddress(0);
    }

    @Test
    void incrementAndDecrementHlPair() {
        getCurrentCpu().setValueInRegister(0x1234, RegisterType.HL);

        getCurrentCpu().incrementRegisterPair(RegisterType.HL);
        assertEquals(0x1235, getCurrentCpu().getValueFromRegister(RegisterType.HL));

        getCurrentCpu().decrementRegisterPair(RegisterType.HL);
        assertEquals(0x1234, getCurrentCpu().getValueFromRegister(RegisterType.HL));
    }

    @Test
    void pairHelpersRejectUnsupportedRegister() {
        assertThrows(IllegalArgumentException.class,
                () -> getCurrentCpu().incrementRegisterPair(RegisterType.BC));
        assertThrows(IllegalArgumentException.class,
                () -> getCurrentCpu().decrementRegisterPair(RegisterType.BC));
    }

    @Test
    void rejectsDirectFlagRegisterWrite() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> getCurrentCpu().setValueInRegister(0xFF, RegisterType.F));

        assertEquals("Setting data not supported for this register F", exception.getMessage());
    }
}
