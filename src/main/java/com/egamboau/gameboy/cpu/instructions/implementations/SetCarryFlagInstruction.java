package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.InstructionCondition;
import com.egamboau.gameboy.cpu.instructions.RegisterType;

public class SetCarryFlagInstruction extends Instruction {

    /**
     * Constructs a SetCarryFlagInstruction with the specified parameters.
     *
     * @param currentAddressMode the addressing mode to use
     * @param currentSourceRegister the source register
     * @param currentDestinationRegister the destination register
     * @param currentCondition the instruction condition
     * @param currentParamter the instruction parameter
     */
    public SetCarryFlagInstruction(final AddressMode currentAddressMode, final RegisterType currentSourceRegister,
            final RegisterType currentDestinationRegister, final InstructionCondition currentCondition,
            final Byte currentParamter) {
        super(currentAddressMode, currentSourceRegister, currentDestinationRegister, currentCondition, currentParamter);
    }

    @Override
    protected final void runInstructionLogic(final CPU currentCpu, final int[] data) {
        currentCpu.setCarry(true);
    }

}
