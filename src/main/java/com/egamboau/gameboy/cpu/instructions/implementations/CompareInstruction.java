package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.RegisterType;
import com.egamboau.gameboy.memory.BitMasks;

public class CompareInstruction extends Instruction {

    /**
     * Creates a comparison that updates flags as if the source were subtracted from the destination.
     *
     * @param currentAddressMode how the source operand is addressed
     * @param currentSourceRegister register supplying the operand or memory address
     * @param currentDestinationRegister register compared without being modified
     */
    public CompareInstruction(final AddressMode currentAddressMode, final RegisterType currentSourceRegister,
            final RegisterType currentDestinationRegister) {
        super(currentAddressMode, currentSourceRegister, currentDestinationRegister);

    }

    @Override
    protected final void runInstructionLogic(final CPU currentCpu, final int[] data) {
        switch (getAddressMode()) {
            case REGISTER_TO_REGISTER:
                this.compareRegisters(currentCpu);
                break;
            case MEMORY_ADDRESS_REGISTER_TO_REGISTER:
                this.compareIndirectRegisterData(currentCpu);
                break;
            case DATA_8_BIT_TO_REGISTER:
                this.compareDirectDataToRegister(currentCpu, data);
                break;
            default:
                throw new IllegalArgumentException(
                        "Address mode not supported for COMPARE instruction: " + getAddressMode());
        }
    }

    private void compareDirectDataToRegister(final CPU currentCpu, final int[] data) {
        int sourceValue = data[0];
        int destinationValue = currentCpu.getValueFromRegister(getDestinationRegister());
        int result = destinationValue - sourceValue;

        setFlags(currentCpu, sourceValue, destinationValue, result);
    }

    private void compareIndirectRegisterData(final CPU currentCpu) {
        int addressValue = currentCpu.getValueFromRegister(getSourceRegister());
        int destinationValue = currentCpu.getValueFromRegister(getDestinationRegister());
        int sourceValue = currentCpu.readByteFromAddress(addressValue);
        int result =  destinationValue - sourceValue;

        setFlags(currentCpu, sourceValue, destinationValue, result);
    }

    private void compareRegisters(final CPU currentCpu) {
        int sourceValue = currentCpu.getValueFromRegister(getSourceRegister());
        int destinationValue = currentCpu.getValueFromRegister(getDestinationRegister());
        int result = destinationValue - sourceValue;

        setFlags(currentCpu, sourceValue, destinationValue, result);
    }

    private void setFlags(final CPU currentCpu, final int sourceValue,
            final int destinationValue, final int result) {
        currentCpu.setSubtract(true);
        currentCpu.setHalfCarry((destinationValue & BitMasks.HALF_CARRY_8_BIT_RESULT)
                < (sourceValue & BitMasks.HALF_CARRY_8_BIT_RESULT));
        currentCpu.setCarry(destinationValue < sourceValue);
        currentCpu.setZero((result & BitMasks.MASK_8_BIT_DATA) == 0);
    }

}
