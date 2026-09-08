package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.RegisterType;

public class ReturnFromInterruptInstruction extends ReturnInstruction {

    /** Constructs RETI, which returns and enables interrupts immediately. */
    public ReturnFromInterruptInstruction() {
        super(null, null, RegisterType.PC);
    }

    @Override
    protected final void runInstructionLogic(final CPU currentCpu, final int[] data) {
        super.runInstructionLogic(currentCpu, data);
        currentCpu.setImeEnabled(true);
    }
}
