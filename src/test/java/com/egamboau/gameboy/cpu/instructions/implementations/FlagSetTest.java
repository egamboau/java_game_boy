package com.egamboau.gameboy.cpu.instructions.implementations;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.egamboau.gameboy.cpu.CPUTestBase;
import com.egamboau.gameboy.cpu.instructions.RegisterType;
import com.egamboau.test.TestUtils;

public class FlagSetTest extends CPUTestBase {

    /** Opcode for setting the carry flag. */
    private static final int SET_CARRY_OPCODE = 0x37;

    /** Opcode for complementing (flipping) the carry flag. */
    private static final int COMPLEMENT_CARRY_OPCODE = 0x3F;

    @Test
    void testSetCarryFlag() {
        executeOpcodeAndVerifyFlags(SET_CARRY_OPCODE, false, true);
    }

    @Test
    void testFlipCarryFlagToTrue() {
        this.getCurrentCpu().setCarry(false);
        executeOpcodeAndVerifyFlags(COMPLEMENT_CARRY_OPCODE, false, true);
    }

    @Test
    void testFlipCarryFlagToFalse() {
        this.getCurrentCpu().setCarry(true);
        executeOpcodeAndVerifyFlags(COMPLEMENT_CARRY_OPCODE, true, false);
    }

    private void executeOpcodeAndVerifyFlags(final int opcode,
            final boolean initialCarry,
            final boolean expectedCarry) {
        stubOpcode(opcode);
        this.getCurrentCpu().setCarry(initialCarry);

        Map<RegisterType, Integer> oldRegisterValues = this.getCpuRegisters(
            TestUtils.getPairForRegister(RegisterType.F));
        long previousCycleCount = getCurrentCpu().getCycles();

        this.getCurrentCpu().cpuStep();

        assertEquals(previousCycleCount + 1, getCurrentCpu().getCycles(), "Cycle count not matching.");

        oldRegisterValues.computeIfPresent(RegisterType.PC, (register, value) -> value + 1);
        assertEquals(oldRegisterValues, this.getCpuRegisters(TestUtils.getPairForRegister(RegisterType.F)));

        assertAll(
            () -> assertEquals(expectedCarry, this.getCurrentCpu().getCarry(), "Unexpected carry flag state."),
            () -> assertFalse(this.getCurrentCpu().getSubtract(), "Subtract flag should remain false."),
            () -> assertFalse(this.getCurrentCpu().getHalfCarry(), "Half-carry flag should remain false.")
        );
    }
}
