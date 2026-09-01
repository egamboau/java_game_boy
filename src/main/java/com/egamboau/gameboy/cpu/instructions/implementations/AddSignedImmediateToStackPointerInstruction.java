package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.RegisterType;
import com.egamboau.gameboy.memory.BitMasks;

public class AddSignedImmediateToStackPointerInstruction extends Instruction {

    /**
     * Creates an instruction that adds a signed immediate value to SP.
     *
     * @param currentAddressMode addressing mode
     * @param currentSourceRegister source register
     * @param currentDestinationRegister destination register
     */
    public AddSignedImmediateToStackPointerInstruction(final AddressMode currentAddressMode,
            final RegisterType currentSourceRegister, final RegisterType currentDestinationRegister) {
        super(currentAddressMode, currentSourceRegister, currentDestinationRegister);

    }

    @Override
    protected void runInstructionLogic(final CPU currentCpu, final int[] data) {
        int sp = currentCpu.getValueFromRegister(RegisterType.SP);
        int offset = (byte) data[0];
        int result = sp + offset;

        currentCpu.setValueInRegister(result, RegisterType.SP);
        currentCpu.setZero(false);
        currentCpu.setSubtract(false);
        currentCpu.setHalfCarry(((sp ^ offset ^ result) & BitMasks.SIGNED_8_BIT_HALF_CARRY) != 0);
        currentCpu.setCarry(((sp ^ offset ^ result) & BitMasks.SIGNED_8_BIT_CARRY) != 0);
    }

    @Override
    protected int getInternalCycles(final CPU cpu) {
        return 2;
    }
}
