package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.RegisterType;
import com.egamboau.gameboy.memory.BitMasks;

/**
 * Rotate left circular (RLC) instruction: rotates the value left by one bit
 * with the MSB moving into the LSB and into the carry flag.
 */
public class RotateLeftCircularInstruction extends Instruction {

    /**
     * Constructs a RotateLeftCircularInstruction with the specified parameters.
     *
     * @param addressMode        The addressing mode of the instruction.
     * @param sourceRegister     The source register for the operation.
     * @param destinationRegister The destination register for the operation.
     */
    public RotateLeftCircularInstruction(final AddressMode addressMode, final RegisterType sourceRegister,
            final RegisterType destinationRegister) {
        super(addressMode, sourceRegister, destinationRegister);
    }

    @Override
    @SuppressWarnings("checkstyle:magicnumber")
    public final void runInstructionLogic(final CPU currentCpu, final int[] data) {
        int value = currentCpu.getValueFromRegister(getSourceRegister());
        int carryOut = (value & BitMasks.CARRY_FLAG_FOR_ROTATE_LEFT) >> 7;
        int result = ((value << 1) | carryOut) & BitMasks.MASK_16_BIT_DATA;
        currentCpu.setValueInRegister(result, getDestinationRegister());
        currentCpu.setCarry(carryOut == 1);
        currentCpu.setZero(false);
        currentCpu.setSubtract(false);
        currentCpu.setHalfCarry(false);
    }

}
