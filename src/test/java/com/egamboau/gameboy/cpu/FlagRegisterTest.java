package com.egamboau.gameboy.cpu;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.egamboau.gameboy.cpu.instructions.RegisterType;

class FlagRegisterTest extends CPUTestBase {

    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    void lowerNibbleIsAlwaysZero() {
        FlagRegister register = new FlagRegister();

        for (int value = 0; value <= 0xFF; value++) {
            register.set(value);
            assertEquals(0, register.get() & 0x0F);
            assertEquals(0, register.raw() & 0x0F);
        }
    }

    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    void settingAfSetsAAndMasksF() {
        getCurrentCpu().setValueInRegister(0x12FF, RegisterType.AF);

        assertEquals(0x12, getCurrentCpu().getValueFromRegister(RegisterType.A));
        assertEquals(0xF0, getCurrentCpu().getValueFromRegister(RegisterType.F));
        assertEquals(0x12F0, getCurrentCpu().getValueFromRegister(RegisterType.AF));
    }
}
