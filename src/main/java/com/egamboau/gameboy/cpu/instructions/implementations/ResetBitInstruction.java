package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.RegisterType;

/**
 * ResetBitInstruction
 */
public class ResetBitInstruction extends Instruction {

    private final int mask;

    /**
     * Constructs an instruction that resets a bit in a register or memory value.
     *
     * @param bitPosition the bit to reset
     * @param currentAddressMode the addressing mode of the instruction
     * @param currentSourceRegister the source register
     * @param currentDestinationRegister the destination register
     */
    public ResetBitInstruction(final int bitPosition, final AddressMode currentAddressMode,
            final RegisterType currentSourceRegister,
            final RegisterType currentDestinationRegister) {
        super(currentAddressMode, currentSourceRegister, currentDestinationRegister);
        this.mask = ~(1 << bitPosition);
    }

    @Override
    protected void runInstructionLogic(final CPU currentCpu, final int[] data) {
        if (getAddressMode() == AddressMode.MEMORY_ADDRESS_REGISTER_TO_REGISTER) {
            int address = currentCpu.getValueFromRegister(getSourceRegister());
            currentCpu.writeByteToAddress(address, currentCpu.readByteFromAddress(address) & mask);
        } else {
            currentCpu.setValueInRegister(currentCpu.getValueFromRegister(getSourceRegister()) & mask,
                    getDestinationRegister());
        }
    }

}
