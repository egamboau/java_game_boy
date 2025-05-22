package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.InstructionCondition;
import com.egamboau.gameboy.cpu.instructions.RegisterType;
import com.egamboau.gameboy.memory.BitMasks;

public class RotateRigthCircularInstruction  extends Instruction {

    /**
     * Constructs a Rotate Right Circular Instruction.
     *
     * @param addressMode        The addressing mode of the instruction.
     * @param sourceRegister     The source register for the operation.
     * @param destinationRegister The destination register for the result.
     * @param condition          The condition under which the instruction executes.
     * @param parameter          Additional parameter for the instruction.
     */
    public RotateRigthCircularInstruction(final AddressMode addressMode, final RegisterType sourceRegister,
            final RegisterType destinationRegister, final InstructionCondition condition, final Byte parameter) {
        super(addressMode, sourceRegister, destinationRegister, condition, parameter);
    }

    @Override
    @SuppressWarnings("checkstyle:magicnumber")
    protected final void runInstructionLogic(final CPU currentCpu, final int[] data) {
        int value = currentCpu.getValueFromRegister(getSourceRegister());
        int carryOut = (value & 0x01) << 7;
        int result = ((value >> 1) | carryOut) & BitMasks.MASK_8_BIT_DATA;
        currentCpu.setValueInRegister(result, getDestinationRegister());
        currentCpu.setCarry(carryOut == BitMasks.CARRY_FLAG_FOR_ROTATE_RIGHT);
        currentCpu.setZero(false);
        currentCpu.setSubtract(false);
        currentCpu.setHalfCarry(false);
    }

}
