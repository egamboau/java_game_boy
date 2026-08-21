package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.Instruction;

/**
 * HALT instruction implementation.
 *
 * <p>When executed the CPU enters a halted state and normal instruction fetch/execute
 * is suspended until an interrupt is serviced. This class represents the HALT opcode
 * and contains the minimal behaviour required by the emulator.</p>
 */
public class HaltInstruction extends Instruction {

    /**
     * Constructs a HALT instruction instance.
     *
     * This instruction models the Game Boy "HALT" opcode: when executed by the CPU
     * it places the processor into the halted state and suspends normal instruction
     * execution until an interrupt is serviced. The HALT instruction carries no
     * operands, so this constructor provides a parameterless representation used
     * by the emulator's instruction set.
     */
    public HaltInstruction() {
        super(null, null, null);
    }

    @Override
    protected final void runInstructionLogic(final CPU currentCpu, final int[] data) {
        currentCpu.setHalted(true);
    }

}
