package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.InstructionCondition;
import com.egamboau.gameboy.cpu.instructions.RegisterType;

/**
 * No-operation instruction (NOP). This instruction performs no state changes,
 * but exists to consume cycles or align program flow.
 */
public class NoopInstruction extends Instruction {

    //region Constructors
    /**
     * Constructs a NoopInstruction.
     *
     * @param addressMode The addressing mode of the instruction.
     * @param sourceRegister The source register for the instruction.
     * @param destinationRegister The destination register for the instruction.
     * @param condition The condition under which the instruction executes.
     * @param parameter Additional parameter for the instruction.
     */
    public NoopInstruction(final AddressMode addressMode, final RegisterType sourceRegister,
        final RegisterType destinationRegister,
        final InstructionCondition condition, final Byte parameter) {
      super(addressMode, sourceRegister, destinationRegister, condition, parameter);
    }
    //endregion

    //region Execution
    @Override
    public void runInstructionLogic(final CPU currentCpu, final int[] data) {
      // Instruction for the NOOP does nothing, thus leaving method empty.
    }
    //endregion
}
