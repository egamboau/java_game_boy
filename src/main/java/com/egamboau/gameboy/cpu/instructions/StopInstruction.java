package com.egamboau.gameboy.cpu.instructions;

import com.egamboau.gameboy.cpu.CPU;

public class StopInstruction extends Instruction {

    /**
     * Constructs a StopInstruction with the specified parameters.
     *
     * @param addressMode The addressing mode of the instruction.
     * @param sourceRegister The source register for the instruction.
     * @param destinationRegister The destination register for the instruction.
     * @param condition The condition under which the instruction executes.
     * @param parameter An additional parameter for the instruction.
     */
    public StopInstruction(final AddressMode addressMode, final RegisterType sourceRegister,
            final RegisterType destinationRegister,
            final InstructionCondition condition, final Byte parameter) {
        super(addressMode, sourceRegister, destinationRegister, condition, parameter);
    }

    @Override
    protected final void runInstructionLogic(final CPU currentCpu, final int[] data) {
        // TODO Stop behaves weird, but not all the hardware is actually implemented for that. For now, set it as stopped
        currentCpu.setHalted(true);
    }
}
