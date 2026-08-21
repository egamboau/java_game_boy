package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.RegisterType;

/**
 * STOP instruction. Puts the CPU into a stopped state. Full hardware behavior
 * is more complex; this implementation marks the CPU as stopped.
 */
public class StopInstruction extends Instruction {

    /**
     * Constructs a StopInstruction with the specified parameters.
     *
     * @param addressMode The addressing mode of the instruction.
     * @param sourceRegister The source register for the instruction.
     * @param destinationRegister The destination register for the instruction.
     */
    public StopInstruction(final AddressMode addressMode, final RegisterType sourceRegister,
            final RegisterType destinationRegister) {
        super(addressMode, sourceRegister, destinationRegister);
    }

    @Override
    protected final void runInstructionLogic(final CPU currentCpu, final int[] data) {
        // Stop behaves weird, but not all the hardware is actually implemented for that. For now, set it as stopped
        currentCpu.setStopped(true);
    }
}
