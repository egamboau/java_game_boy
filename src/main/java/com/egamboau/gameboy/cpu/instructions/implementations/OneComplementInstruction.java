package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.RegisterType;

/**
 * One's complement instruction (COM or CPL): flips all bits of the operand.
 * This instruction sets the half-carry and subtract flags.
 */
public class OneComplementInstruction extends Instruction {

    /**
     * Constructs a OneComplementInstruction.
     *
     * @param addressMode        The addressing mode of the instruction.
     * @param sourceRegister     The source register for the instruction.
     * @param destinationRegister The destination register for the instruction.
     */
    public OneComplementInstruction(final AddressMode addressMode, final RegisterType sourceRegister,
            final RegisterType destinationRegister) {
        super(addressMode, sourceRegister, destinationRegister);
    }

    @Override
    protected final void runInstructionLogic(final CPU currentCpu, final int[] data) {
        int value = currentCpu.getValueFromRegister(getSourceRegister());
        int result = (~value);
        currentCpu.setValueInRegister(result, getDestinationRegister());
        currentCpu.setSubtract(true);
        currentCpu.setHalfCarry(true);
    }

}
