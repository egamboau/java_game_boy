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

    private final boolean setZeroFromResult;

    /**
     * Constructs a RotateRightInstruction.
     *
     * @param addressMode The addressing mode of the instruction.
     * @param sourceRegister The source register for the operation.
     * @param destinationRegister The destination register for the result.
     */
    public RotateRightInstruction(final AddressMode addressMode, final RegisterType sourceRegister,
            final RegisterType destinationRegister) {
        this(addressMode, sourceRegister, destinationRegister, false);
    }

    /**
     * Constructs a RotateRightInstruction with configurable zero-flag behavior.
     *
     * @param addressMode the addressing mode of the instruction
     * @param sourceRegister the source register for the operation
     * @param destinationRegister the destination register for the result
     * @param shouldSetZeroFromResult whether to set the zero flag from the result
     */
    public RotateRightInstruction(final AddressMode addressMode, final RegisterType sourceRegister,
            final RegisterType destinationRegister, final boolean shouldSetZeroFromResult) {
        super(addressMode, sourceRegister, destinationRegister);
        this.setZeroFromResult = shouldSetZeroFromResult;
    }

    @Override
    @SuppressWarnings("checkstyle:magicnumber")
    protected final void runInstructionLogic(final CPU currentCpu, final int[] data) {
        int value = 0;
        if (getAddressMode() == AddressMode.MEMORY_ADDRESS_REGISTER_TO_MEMORY_ADRESS_REGISTER) {
            value = currentCpu.readByteFromAddress(currentCpu.getValueFromRegister(getSourceRegister()));
        } else {
            value = currentCpu.getValueFromRegister(getSourceRegister());
        }
        int futureCarry = value & 0x01;
        int previousCarry = (currentCpu.getCarry() ? 0x80 : 0);
        int result = (value  >> 1) | previousCarry;

        if (getAddressMode() == AddressMode.MEMORY_ADDRESS_REGISTER_TO_MEMORY_ADRESS_REGISTER) {
            currentCpu.writeByteToAddress(currentCpu.getValueFromRegister(getDestinationRegister()), result);
        } else {
            currentCpu.setValueInRegister(result, getDestinationRegister());
        }
        currentCpu.setCarry(futureCarry != 0);
        currentCpu.setZero(setZeroFromResult && result == 0);
        currentCpu.setSubtract(false);
        currentCpu.setHalfCarry(false);
    }

}
