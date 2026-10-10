package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.RegisterType;

/**
 * BitInstruction
 */
public class BitInstruction extends Instruction {

    private int position;

    protected BitInstruction(final int bitPosition, final AddressMode currentAddressMode,
            final RegisterType currentSourceRegister,
                    final RegisterType currentDestinationRegister) {
        super(currentAddressMode, currentSourceRegister, currentDestinationRegister);
        this.position = bitPosition;
    }

    @Override
    protected void runInstructionLogic(final CPU currentCpu, final int[] data) {
        int mask = 1 << position;
        int toCompare = 0;
        if (getAddressMode() == AddressMode.MEMORY_ADDRESS_REGISTER_TO_REGISTER) {
            toCompare = currentCpu.readByteFromAddress(currentCpu.getValueFromRegister(getSourceRegister()));
        } else {
            toCompare = currentCpu.getValueFromRegister(getSourceRegister());
        }
        int comparison = toCompare & mask;
        currentCpu.setZero(comparison == 0);
        currentCpu.setSubtract(false);
        currentCpu.setHalfCarry(true);
    }

}
