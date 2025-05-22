package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.InstructionCondition;
import com.egamboau.gameboy.cpu.instructions.RegisterType;

public class RotateRightInstruction extends Instruction {

    /**
     * Constructs a RotateRightInstruction.
     *
     * @param addressMode The addressing mode of the instruction.
     * @param sourceRegister The source register for the operation.
     * @param destinationRegister The destination register for the result.
     * @param condition The condition under which the instruction executes.
     * @param parameter Additional parameter for the instruction.
     */
    public RotateRightInstruction(final AddressMode addressMode, final RegisterType sourceRegister,
            final RegisterType destinationRegister, final InstructionCondition condition, final Byte parameter) {
        super(addressMode, sourceRegister, destinationRegister, condition, parameter);
    }

    @Override
    @SuppressWarnings("checkstyle:magicnumber")
    protected final void runInstructionLogic(final CPU currentCpu, final int[] data) {
        int value = currentCpu.getValueFromRegister(getSourceRegister());
        int futureCarry = value & 0x01;

        int result = ((value | (futureCarry << 8)) >> 1);

        currentCpu.setValueInRegister(result, getDestinationRegister());
        currentCpu.setCarry(futureCarry != 0);
        currentCpu.setZero(false);
        currentCpu.setSubtract(false);
        currentCpu.setHalfCarry(false);
    }



}
