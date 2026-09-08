package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.Instruction;

public class DisableInterruptsInstruction extends Instruction {

    /** Constructs DI, which disables interrupts immediately. */
    public DisableInterruptsInstruction() {
        super(null, null, null);
    }

    @Override
    protected final void runInstructionLogic(final CPU currentCpu, final int[] data) {
        currentCpu.setImeEnabled(false);
    }
}
