package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.RegisterType;

/**
 * SetBitInstruction
 */
public class SetBitInstruction extends Instruction {

    private int mask;

    protected SetBitInstruction(final int bitPosition, final AddressMode currentAddressMode, final RegisterType currentSourceRegister,
            final RegisterType currentDestinationRegister) {
        super(currentAddressMode, currentSourceRegister, currentDestinationRegister);
        this.mask = (1 << bitPosition);
    }

    @Override
    protected void runInstructionLogic(final CPU currentCpu, final int[] data) {
        if (getAddressMode() == AddressMode.MEMORY_ADDRESS_REGISTER_TO_REGISTER) {
            int address = currentCpu.getValueFromRegister(getSourceRegister());
            currentCpu.writeByteToAddress(address, currentCpu.readByteFromAddress(address) | mask);
        } else {
            currentCpu.setValueInRegister(currentCpu.getValueFromRegister(getSourceRegister()) | mask,
                    getDestinationRegister());
        }
    }

}
