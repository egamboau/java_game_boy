package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.InstructionCondition;
import com.egamboau.gameboy.cpu.instructions.RegisterType;
import com.egamboau.gameboy.memory.BitMasks;

public class DecimalAdjustAccumulatorInstruction extends Instruction {

    /**
     * Constructs a DecimalAdjustAccumulatorInstruction.
     *
     * @param addressMode The addressing mode of the instruction.
     * @param sourceRegister The source register for the instruction.
     * @param destinationRegister The destination register for the instruction.
     * @param condition The condition under which the instruction executes.
     * @param parameter An additional parameter for the instruction.
     */
    public DecimalAdjustAccumulatorInstruction(final AddressMode addressMode, final RegisterType sourceRegister,
            final RegisterType destinationRegister, final InstructionCondition condition, final Byte parameter) {
        super(addressMode, sourceRegister, destinationRegister, condition, parameter);
    }

    @Override
    @SuppressWarnings("checkstyle:magicnumber")
    protected final void runInstructionLogic(final CPU currentCpu, final int[] data) {
        int adjustment = 0;
        int registerValue = currentCpu.getValueFromRegister(getDestinationRegister());
        if (currentCpu.getSubtract()) {

            if (currentCpu.getHalfCarry()) {
                adjustment += 0x06;
            }
            if (currentCpu.getCarry()) {
                adjustment += 0x60;
            }
            registerValue -=  (adjustment & BitMasks.MASK_8_BIT_DATA);
        } else {

            if (currentCpu.getHalfCarry() || (registerValue & 0xF) > 0x9) {
                adjustment += 0x06;
            }
            if (currentCpu.getCarry() || registerValue > 0x99) {
                adjustment += 0x60;
                currentCpu.setCarry(true);
            }
            registerValue += (adjustment & BitMasks.MASK_8_BIT_DATA);
        }

        currentCpu.setZero((registerValue & BitMasks.MASK_8_BIT_DATA) == 0);
        currentCpu.setHalfCarry(false);
        currentCpu.setValueInRegister(registerValue, getDestinationRegister());
    }

}
