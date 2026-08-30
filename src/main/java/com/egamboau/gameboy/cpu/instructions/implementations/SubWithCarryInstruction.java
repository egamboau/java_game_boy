package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.RegisterType;
import com.egamboau.gameboy.memory.BitMasks;

public class SubWithCarryInstruction extends Instruction {

    /**
     * Creates a subtraction-with-carry instruction.
     *
     * @param currentAddressMode instruction addressing mode
     * @param currentSourceRegister source operand register
     * @param currentDestinationRegister destination register
     */
    public SubWithCarryInstruction(final AddressMode currentAddressMode, final RegisterType currentSourceRegister,
            final RegisterType currentDestinationRegister) {
        super(currentAddressMode, currentSourceRegister, currentDestinationRegister);
    }

    @Override
    protected final void runInstructionLogic(final CPU currentCpu, final int[] data) {
        switch (getAddressMode()) {
            case REGISTER_TO_REGISTER:
                this.substractRegisters(currentCpu);
                break;
            case MEMORY_ADDRESS_REGISTER_TO_REGISTER:
                this.substractIndirectRegisterData(currentCpu);
                break;
            case DATA_8_BIT_TO_REGISTER:
                this.substractDirectDataToRegisterData(currentCpu, data);
                break;
            default:
                throw new IllegalArgumentException(
                        "Address mode not supported for SBC instruction: " + getAddressMode());
        }
    }

    private void substractDirectDataToRegisterData(final CPU currentCpu, final int[] data) {
        int sourceValue = data[0];
        int destinationValue = currentCpu.getValueFromRegister(getDestinationRegister());
        boolean carry = currentCpu.getCarry();
        int result = destinationValue - sourceValue;
        if (carry) {
            result -= 1;
        }

        currentCpu.setValueInRegister(result, getDestinationRegister());
        setFlags(currentCpu, sourceValue, destinationValue, result, carry);
    }

    private void substractIndirectRegisterData(final CPU currentCpu) {
        int addressValue = currentCpu.getValueFromRegister(getSourceRegister());
        int destinationValue = currentCpu.getValueFromRegister(getDestinationRegister());
        int sourceValue = currentCpu.readByteFromAddress(addressValue);
        int result = destinationValue - sourceValue;
        boolean carry = currentCpu.getCarry();
        if (carry) {
            result -= 1;
        }

        currentCpu.setValueInRegister(result, getDestinationRegister());
        setFlags(currentCpu, sourceValue, destinationValue, result, carry);
    }

    private void substractRegisters(final CPU currentCpu) {
        int sourceValue = currentCpu.getValueFromRegister(getSourceRegister());
        int destinationValue = currentCpu.getValueFromRegister(getDestinationRegister());
        boolean carry = currentCpu.getCarry();
        int result = destinationValue - sourceValue;
        if (carry) {
            result -= 1;
        }

        currentCpu.setValueInRegister(result, getDestinationRegister());
        setFlags(currentCpu, sourceValue, destinationValue, result, carry);
    }

    private void setFlags(final CPU currentCpu, final int sourceValue,
            final int destinationValue, final int result, final boolean carry) {
        int sourceWithCarry = sourceValue + (carry ? 1 : 0);
        currentCpu.setSubtract(true);
        currentCpu.setHalfCarry((destinationValue & BitMasks.HALF_CARRY_8_BIT_RESULT)
                < (sourceValue & BitMasks.HALF_CARRY_8_BIT_RESULT) + (carry ? 1 : 0));
        currentCpu.setCarry(destinationValue < sourceWithCarry);
        currentCpu.setZero((result & BitMasks.MASK_8_BIT_DATA) == 0);
    }

}
