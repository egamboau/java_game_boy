package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.RegisterType;

/**
 * No-operation instruction (NOP). This instruction performs no state changes,
 * but exists to consume cycles or align program flow.
 */
public class NoopInstruction extends Instruction {

    /**
     * Constructs a NoopInstruction.
     *
     * @param addressMode The addressing mode of the instruction.
     * @param sourceRegister The source register for the instruction.
     * @param destinationRegister The destination register for the instruction.
     */
    public NoopInstruction(final AddressMode addressMode, final RegisterType sourceRegister,
        final RegisterType destinationRegister) {
      super(addressMode, sourceRegister, destinationRegister);
    }

    @Override
    public void runInstructionLogic(final CPU currentCpu, final int[] data) {
      // Instruction for the NOOP does nothing, thus leaving method empty.
    }
}
