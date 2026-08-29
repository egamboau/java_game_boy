package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.RegisterType;

public class PushInstruction extends Instruction {

    /**
     * Creates a push instruction.
     *
     * @param currentAddressMode addressing mode
     * @param currentSourceRegister source register
     * @param currentDestinationRegister destination register
     */
    public PushInstruction(final AddressMode currentAddressMode, final RegisterType currentSourceRegister,
            final RegisterType currentDestinationRegister) {
        super(currentAddressMode, currentSourceRegister, currentDestinationRegister);
    }

    @Override
    protected void runInstructionLogic(final CPU currentCpu, final int[] data) {
        currentCpu.pushWord(currentCpu.getValueFromRegister(getSourceRegister()));
    }

    @Override
    protected int getInternalCycles(final CPU currentCpu) {
        return 1;
    }

}
