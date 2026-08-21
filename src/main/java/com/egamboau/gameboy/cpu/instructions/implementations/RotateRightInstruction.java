package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.RegisterType;

/**
 * Rotate right through carry (RR) instruction. Shifts the value right by one,
 * filling MSB with previous carry and storing LSB into the carry flag.
 */
public class RotateRightInstruction extends Instruction {

    /**
     * Constructs a RotateRightInstruction.
     *
     * @param addressMode The addressing mode of the instruction.
     * @param sourceRegister The source register for the operation.
     * @param destinationRegister The destination register for the result.
     */
    public RotateRightInstruction(final AddressMode addressMode, final RegisterType sourceRegister,
            final RegisterType destinationRegister) {
        super(addressMode, sourceRegister, destinationRegister);
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
