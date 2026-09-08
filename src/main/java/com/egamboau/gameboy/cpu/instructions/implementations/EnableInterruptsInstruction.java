package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.Instruction;

public class EnableInterruptsInstruction extends Instruction {

    /** Constructs EI, which enables interrupts after the following instruction. */
    public EnableInterruptsInstruction() {
        super(null, null, null);
    }

    @Override
    protected final void runInstructionLogic(final CPU currentCpu, final int[] data) {
        currentCpu.scheduleImeEnable();
    }
}
