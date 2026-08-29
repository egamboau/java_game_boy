package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.RegisterType;

public class PopInstruction extends Instruction {

    /**
     * Creates a pop instruction.
     *
     * @param currentAddressMode addressing mode
     * @param currentSourceRegister source register
     * @param currentDestinationRegister destination register
     */
    public PopInstruction(final AddressMode currentAddressMode, final RegisterType currentSourceRegister,
            final RegisterType currentDestinationRegister) {
        super(currentAddressMode, currentSourceRegister, currentDestinationRegister);
    }

    @Override
    protected void runInstructionLogic(final CPU currentCpu, final int[] data) {
        int newBytes = currentCpu.popWord();
        currentCpu.setValueInRegister(newBytes, getDestinationRegister());
    }

}
