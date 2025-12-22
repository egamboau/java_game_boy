package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.InstructionCondition;
import com.egamboau.gameboy.cpu.instructions.RegisterType;

/**
 * One's complement instruction (COM or CPL): flips all bits of the operand.
 * This instruction clears the half-carry flag and sets the subtract flag to false.
 */
public class OneComplementInstruction extends Instruction {

    //region Constructors
    /**
     * Constructs a OneComplementInstruction.
     *
     * @param addressMode        The addressing mode of the instruction.
     * @param sourceRegister     The source register for the instruction.
     * @param destinationRegister The destination register for the instruction.
     * @param condition          The condition under which the instruction executes.
     * @param parameter          An additional parameter for the instruction.
     */
    public OneComplementInstruction(final AddressMode addressMode, final RegisterType sourceRegister,
            final RegisterType destinationRegister, final InstructionCondition condition, final Byte parameter) {
        super(addressMode, sourceRegister, destinationRegister, condition, parameter);
    }
    //endregion

    //region Execution
    @Override
    protected final void runInstructionLogic(final CPU currentCpu, final int[] data) {
        int value = currentCpu.getValueFromRegister(getSourceRegister());
        int result = (~value);
        currentCpu.setValueInRegister(result, getDestinationRegister());
        currentCpu.setSubtract(false);
        currentCpu.setHalfCarry(false);
    }
    //endregion

}
