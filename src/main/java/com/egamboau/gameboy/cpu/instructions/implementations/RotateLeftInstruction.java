package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.InstructionCondition;
import com.egamboau.gameboy.cpu.instructions.RegisterType;
import com.egamboau.gameboy.memory.BitMasks;

public class RotateLeftInstruction extends Instruction {

    /**
     * Constructs a RotateLeftInstruction.
     *
     * @param addressMode        The addressing mode of the instruction.
     * @param sourceRegister     The source register for the operation.
     * @param destinationRegister The destination register for the result.
     * @param condition          The condition under which the instruction executes.
     * @param parameter          An additional parameter for the instruction.
     */
    public RotateLeftInstruction(final AddressMode addressMode, final RegisterType sourceRegister,
            final RegisterType destinationRegister,
            final InstructionCondition condition, final Byte parameter) {
        super(addressMode, sourceRegister, destinationRegister, condition, parameter);
    }

    @Override
    protected final void runInstructionLogic(final CPU currentCpu, final int[] data) {
        int value = currentCpu.getValueFromRegister(getSourceRegister());
        int currentCarry = currentCpu.getCarry() ? 1 : 0;

        int result = ((value << 1) | currentCarry);
        int carryResult = (result & BitMasks.CARRY_RESULT_ROTATE_LEFT);

        currentCpu.setValueInRegister(result, getDestinationRegister());
        currentCpu.setCarry(carryResult != 0);
        currentCpu.setZero(false);
        currentCpu.setSubtract(false);
        currentCpu.setHalfCarry(false);
    }

}
