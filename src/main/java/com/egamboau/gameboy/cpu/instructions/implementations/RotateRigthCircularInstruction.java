package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.RegisterType;
import com.egamboau.gameboy.memory.BitMasks;

/**
 * Rotate right circular (RRC) instruction: rotates the value right by one bit
 * with the LSB moving into the MSB and into the carry flag.
 */
public class RotateRigthCircularInstruction  extends Instruction {

    /**
     * Constructs a Rotate Right Circular Instruction.
     *
     * @param addressMode        The addressing mode of the instruction.
     * @param sourceRegister     The source register for the operation.
     * @param destinationRegister The destination register for the result.
     */
    public RotateRigthCircularInstruction(final AddressMode addressMode, final RegisterType sourceRegister,
            final RegisterType destinationRegister) {
        super(addressMode, sourceRegister, destinationRegister);
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
