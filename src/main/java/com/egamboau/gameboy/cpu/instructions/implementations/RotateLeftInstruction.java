package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.RegisterType;
import com.egamboau.gameboy.memory.BitMasks;

/**
 * Rotate left through carry (RL) instruction. Shifts the value left by one,
 * filling LSB with previous carry and storing MSB into the carry flag.
 */
public class RotateLeftInstruction extends Instruction {

    private final boolean setZeroFromResult;

    /**
     * Constructs a RotateLeftInstruction.
     *
     * @param addressMode        The addressing mode of the instruction.
     * @param sourceRegister     The source register for the operation.
     * @param destinationRegister The destination register for the result.
     */
    public RotateLeftInstruction(final AddressMode addressMode, final RegisterType sourceRegister,
            final RegisterType destinationRegister) {
        this(addressMode, sourceRegister, destinationRegister, false);
    }

    /**
     * Constructs a RotateLeftInstruction with configurable zero-flag behavior.
     *
     * @param addressMode the addressing mode of the instruction
     * @param sourceRegister the source register for the operation
     * @param destinationRegister the destination register for the result
     * @param shouldSetZeroFromResult whether to set the zero flag from the result
     */
    public RotateLeftInstruction(final AddressMode addressMode, final RegisterType sourceRegister,
            final RegisterType destinationRegister, final boolean shouldSetZeroFromResult) {
        super(addressMode, sourceRegister, destinationRegister);
        this.setZeroFromResult = shouldSetZeroFromResult;
    }

    @Override
    protected final void runInstructionLogic(final CPU currentCpu, final int[] data) {
        int value = 0;
        if (getAddressMode() == AddressMode.MEMORY_ADDRESS_REGISTER_TO_MEMORY_ADRESS_REGISTER) {
            value = currentCpu.readByteFromAddress(currentCpu.getValueFromRegister(getSourceRegister()));
        } else {
            value = currentCpu.getValueFromRegister(getSourceRegister());
        }
        int currentCarry = currentCpu.getCarry() ? 1 : 0;

        int result = ((value << 1) | currentCarry);
        int carryResult = (result & BitMasks.CARRY_RESULT_ROTATE_LEFT);

        if (getAddressMode() == AddressMode.MEMORY_ADDRESS_REGISTER_TO_MEMORY_ADRESS_REGISTER) {
            currentCpu.writeByteToAddress(currentCpu.getValueFromRegister(getDestinationRegister()), result & BitMasks.MASK_8_BIT_DATA);
        } else {
            currentCpu.setValueInRegister(result & BitMasks.MASK_8_BIT_DATA, getDestinationRegister());
        }
        currentCpu.setCarry(carryResult != 0);
        currentCpu.setZero(setZeroFromResult && (result & BitMasks.MASK_8_BIT_DATA) == 0);
        currentCpu.setSubtract(false);
        currentCpu.setHalfCarry(false);
    }

}
