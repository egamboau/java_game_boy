package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.ConditionalInstruction;
import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.RegisterType;
import com.egamboau.gameboy.memory.BitMasks;


public class CallInstruction extends ConditionalInstruction {

    /**
     * Creates a call instruction.
     *
     * @param currentAddressMode address mode used by the instruction
     * @param currentSourceRegister source register
     * @param currentDestinationRegister destination register
     */
    public CallInstruction(final AddressMode currentAddressMode, final RegisterType currentSourceRegister,
                    final RegisterType currentDestinationRegister) {
        super(currentAddressMode, currentSourceRegister, currentDestinationRegister);
    }

    @Override
    protected void runInstructionLogic(final CPU currentCpu, final int[] data) {
        if (checkForCondition(currentCpu)) {
            currentCpu.pushWord(currentCpu.getValueFromRegister(getDestinationRegister()));
            int operand = data[0] + (data[1] << BitMasks.MASK_8_BIT_SHIFT);
            currentCpu.setValueInRegister(operand, getDestinationRegister());
        }
    }

    @Override
    protected int getInternalCycles(final CPU currentCpu) {
        return checkForCondition(currentCpu) ? 1 : 0;
    }

}
