package com.egamboau.gameboy.cpu.instructions.implementations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.egamboau.gameboy.cpu.CPUTestBase;
import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.RegisterType;

class XorInstructionTest extends CPUTestBase {

    @Test
    void setsZeroFlagForZeroResult() {
        getCurrentCpu().setValueInRegister(0x01, RegisterType.A);
        getCurrentCpu().setValueInRegister(0x01, RegisterType.B);

        new XorInstruction(AddressMode.REGISTER_TO_REGISTER, RegisterType.B, RegisterType.A)
                .executeInstruction(getCurrentCpu());

        assertEquals(0, getCurrentCpu().getValueFromRegister(RegisterType.A));
        assertTrue(getCurrentCpu().getZero());
    }

    @Test
    void clearsZeroFlagForNonZeroResult() {
        getCurrentCpu().setValueInRegister(0x03, RegisterType.A);
        getCurrentCpu().setValueInRegister(0x01, RegisterType.B);

        new XorInstruction(AddressMode.REGISTER_TO_REGISTER, RegisterType.B, RegisterType.A)
                .executeInstruction(getCurrentCpu());

        assertEquals(0x02, getCurrentCpu().getValueFromRegister(RegisterType.A));
        assertFalse(getCurrentCpu().getZero());
    }

    @Test
    void rejectsUnsupportedAddressMode() {
        XorInstruction instruction = new XorInstruction(
                AddressMode.REGISTER_8_BIT, RegisterType.B, RegisterType.A);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> instruction.executeInstruction(getCurrentCpu()));

        assertEquals("Address mode not supported for XOR instruction: REGISTER_8_BIT",
                exception.getMessage());
    }
}
