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

    private final boolean setZeroFromResult;

    /**
     * Constructs a RotateLeftCircularInstruction with the specified parameters.
     *
     * @param addressMode        The addressing mode of the instruction.
     * @param sourceRegister     The source register for the operation.
     * @param destinationRegister The destination register for the operation.
     */
    public RotateLeftCircularInstruction(final AddressMode addressMode, final RegisterType sourceRegister,
            final RegisterType destinationRegister) {
        this(addressMode, sourceRegister, destinationRegister, false);
    }

    /**
     * Constructs a RotateLeftCircularInstruction with configurable zero-flag behavior.
     *
     * @param addressMode the addressing mode of the instruction
     * @param sourceRegister the source register for the operation
     * @param destinationRegister the destination register for the operation
     * @param shouldSetZeroFromResult whether to set the zero flag from the result
     */
    public RotateLeftCircularInstruction(final AddressMode addressMode, final RegisterType sourceRegister,
            final RegisterType destinationRegister, final boolean shouldSetZeroFromResult) {
        super(addressMode, sourceRegister, destinationRegister);
        this.setZeroFromResult = shouldSetZeroFromResult;
    }

    @Override
    @SuppressWarnings("checkstyle:magicnumber")
    public final void runInstructionLogic(final CPU currentCpu, final int[] data) {
        int value = 0;
        if (getAddressMode() == AddressMode.MEMORY_ADDRESS_REGISTER_TO_MEMORY_ADRESS_REGISTER) {
            value = currentCpu.readByteFromAddress(currentCpu.getValueFromRegister(getSourceRegister()));
        } else {
            value = currentCpu.getValueFromRegister(getSourceRegister());
        }
        int carryOut = (value & BitMasks.CARRY_FLAG_FOR_ROTATE_LEFT) >> 7;
        int result = ((value << 1) | carryOut) & BitMasks.MASK_8_BIT_DATA;
        if (getAddressMode() == AddressMode.MEMORY_ADDRESS_REGISTER_TO_MEMORY_ADRESS_REGISTER) {
            currentCpu.writeByteToAddress(currentCpu.getValueFromRegister(getDestinationRegister()), result);
        } else {
            currentCpu.setValueInRegister(result, getDestinationRegister());
        }
        currentCpu.setCarry(carryOut == 1);
        currentCpu.setZero(setZeroFromResult && result == 0);
        currentCpu.setSubtract(false);
        currentCpu.setHalfCarry(false);
    }

}
