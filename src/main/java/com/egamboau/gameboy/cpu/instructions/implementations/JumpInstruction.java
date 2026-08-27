package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.ConditionalInstruction;
import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.RegisterType;
import com.egamboau.gameboy.memory.BitMasks;

/**
 * ConditionalJumpInstruction
 */
public class JumpInstruction extends ConditionalInstruction {

    public JumpInstruction(final AddressMode currentAddressMode, final RegisterType currentSourceRegister,
                            final RegisterType currentDestinationRegister) {
        super(currentAddressMode, currentSourceRegister, currentDestinationRegister);
    }

    @Override
    protected void runInstructionLogic(final CPU currentCpu, final int[] data) {
        if (checkForCondition(currentCpu)) {
            int newPC = 0;
            if (getAddressMode() == AddressMode.REGISTER_16_BIT_TO_REGISTER_16_BIT) {
                newPC = currentCpu.getValueFromRegister(getSourceRegister());
            } else {
                newPC = data[0] + (data[1] << BitMasks.MASK_8_BIT_SHIFT);
            }
            currentCpu.setValueInRegister(newPC, getDestinationRegister());
        }
    }

    @Override
    protected int getInternalCycles(final CPU currentCpu) {
        if (getAddressMode() == AddressMode.REGISTER_16_BIT_TO_REGISTER_16_BIT) {
            return 0;
        }
        return checkForCondition(currentCpu) ? 1 : 0;
    }

}
