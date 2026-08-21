package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.RegisterType;

/**
 * Instruction that sets the carry flag and clears subtract and half-carry flags.
 */
public class SetCarryFlagInstruction extends Instruction {

    /**
     * Constructs a SetCarryFlagInstruction with the specified parameters.
     *
     * @param currentAddressMode the addressing mode to use
     * @param currentSourceRegister the source register
     * @param currentDestinationRegister the destination register
     */
    public SetCarryFlagInstruction(final AddressMode currentAddressMode, final RegisterType currentSourceRegister,
            final RegisterType currentDestinationRegister) {
        super(currentAddressMode, currentSourceRegister, currentDestinationRegister);
    }

    @Override
    protected final void runInstructionLogic(final CPU currentCpu, final int[] data) {
        currentCpu.setCarry(true);
        currentCpu.setSubtract(false);
        currentCpu.setHalfCarry(false);
    }

}
