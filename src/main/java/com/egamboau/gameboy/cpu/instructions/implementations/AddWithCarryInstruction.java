package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.RegisterType;
import com.egamboau.gameboy.memory.BitMasks;

public class AddWithCarryInstruction extends Instruction {

    /**
     * Creates an add-with-carry instruction.
     *
     * @param addressMode instruction addressing mode
     * @param sourceRegister source operand register
     * @param destinationRegister destination register
     */
    public AddWithCarryInstruction(final AddressMode addressMode, final RegisterType sourceRegister,
            final RegisterType destinationRegister) {
        super(addressMode, sourceRegister, destinationRegister);
    }

    @Override
    protected final void runInstructionLogic(final CPU currentCpu, final int[] data) {
        switch (getAddressMode()) {
            case REGISTER_TO_REGISTER:
                this.addRegisters(currentCpu);
                break;
            case MEMORY_ADDRESS_REGISTER_TO_REGISTER:
                this.addIndirectRegisterData(currentCpu);
                break;
            case DATA_8_BIT_TO_REGISTER:
                this.addDirectDataToRegister(currentCpu, data);
                break;
            default:
                throw new IllegalArgumentException(
                        "Address mode not supported for ADC instruction: " + getAddressMode());
        }
    }

    private void addDirectDataToRegister(final CPU currentCpu, final int[] data) {
        int sourceValue = data[0];
        int destinationValue = currentCpu.getValueFromRegister(getDestinationRegister());
        boolean carry = currentCpu.getCarry();
        int result = sourceValue + destinationValue;
        int additionHalfBits = (sourceValue &  BitMasks.HALF_CARRY_8_BIT_RESULT) + (destinationValue &  BitMasks.HALF_CARRY_8_BIT_RESULT);
        if (carry) {
            result += 1;
            additionHalfBits += 1;
        }

        currentCpu.setValueInRegister(result, getDestinationRegister());
        setFlags(currentCpu, result, additionHalfBits);
    }

    private void addRegisters(final CPU currentCpu) {
        int sourceValue = currentCpu.getValueFromRegister(getSourceRegister());
        int destinationValue = currentCpu.getValueFromRegister(getDestinationRegister());
        boolean carry = currentCpu.getCarry();
        int result = sourceValue + destinationValue;
        int additionHalfBits = (sourceValue &  BitMasks.HALF_CARRY_8_BIT_RESULT) + (destinationValue &  BitMasks.HALF_CARRY_8_BIT_RESULT);
        if (carry) {
            result += 1;
            additionHalfBits += 1;
        }

        currentCpu.setValueInRegister(result, getDestinationRegister());
        setFlags(currentCpu, result, additionHalfBits);

    }


    private void addIndirectRegisterData(final CPU currentCpu) {
        int addressValue = currentCpu.getValueFromRegister(getSourceRegister());
        int destinationValue = currentCpu.getValueFromRegister(getDestinationRegister());
        int sourceValue = currentCpu.readByteFromAddress(addressValue);
        int result = sourceValue + destinationValue;
        boolean carry = currentCpu.getCarry();
        int additionHalfBits = (sourceValue &  BitMasks.HALF_CARRY_8_BIT_RESULT) + (destinationValue &  BitMasks.HALF_CARRY_8_BIT_RESULT);
        if (carry) {
            result += 1;
            additionHalfBits += 1;
        }

        currentCpu.setValueInRegister(result, getDestinationRegister());
        setFlags(currentCpu, result, additionHalfBits);
    }

    private void setFlags(final CPU currentCpu, final int result, final int additionHalfBits) {
        currentCpu.setSubtract(false);
        currentCpu.setHalfCarry(additionHalfBits > BitMasks.HALF_CARRY_8_BIT_RESULT);
        currentCpu.setCarry(result > BitMasks.CARRY_8_BIT_RESULTS);
        currentCpu.setZero((result & BitMasks.MASK_8_BIT_DATA) == 0);
    }

}
