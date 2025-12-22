package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.InstructionCondition;
import com.egamboau.gameboy.cpu.instructions.RegisterType;
import com.egamboau.gameboy.memory.BitMasks;

/**
 * Rotate left through carry (RL) instruction. Shifts the value left by one,
 * filling LSB with previous carry and storing MSB into the carry flag.
 */
public class RotateLeftInstruction extends Instruction {

    //region Constructors
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
    //endregion

    //region Execution
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
    //endregion

}
