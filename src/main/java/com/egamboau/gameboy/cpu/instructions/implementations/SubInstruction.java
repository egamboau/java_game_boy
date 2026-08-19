package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.InstructionCondition;
import com.egamboau.gameboy.cpu.instructions.RegisterType;
import com.egamboau.gameboy.memory.BitMasks;

public class SubInstruction extends Instruction {

    /**
     * Creates a subtraction instruction.
     *
     * @param currentAddressMode instruction addressing mode
     * @param currentSourceRegister source operand register
     * @param currentDestinationRegister destination register
     * @param currentCondition execution condition
     * @param currentParameter additional instruction parameter
     */
    public SubInstruction(final AddressMode currentAddressMode, final RegisterType currentSourceRegister,
            final RegisterType currentDestinationRegister, final InstructionCondition currentCondition,
            final Byte currentParameter) {
        super(currentAddressMode, currentSourceRegister, currentDestinationRegister, currentCondition,
                currentParameter);
    }

    @Override
    protected final void runInstructionLogic(final CPU currentCpu, final int[] data) {
        switch (getAddressMode()) {
            case REGISTER_TO_REGISTER:
                this.substractRegister(currentCpu);
                break;
            case REGISTER_16_BIT_TO_REGISTER_16_BIT:
                this.substractRegisterPairs(currentCpu);
                break;
            case MEMORY_ADDRESS_REGISTER_TO_REGISTER:
                this.substractIndirectRegisterData(currentCpu);
                break;
            default:
                throw new IllegalArgumentException(
                        "Address mode not supported for ADD instruction: " + getAddressMode());
        }
    }

    private void substractIndirectRegisterData(final CPU currentCpu) {
        int addressValue = currentCpu.getValueFromRegister(getSourceRegister());
        int destinationValue = currentCpu.getValueFromRegister(getDestinationRegister());
        int sourceValue = currentCpu.readByteFromAddress(addressValue);
        int result =  destinationValue - sourceValue;

        currentCpu.setValueInRegister(result, getDestinationRegister());
        currentCpu.setSubtract(true);
        currentCpu.setHalfCarry((result & BitMasks.HALF_CARRY_8_BIT_RESULT_DECREMENT) == BitMasks.HALF_CARRY_8_BIT_RESULT_DECREMENT);
        currentCpu.setCarry(destinationValue < sourceValue);
        currentCpu.setZero((result & BitMasks.MASK_8_BIT_DATA) == 0);
    }

    private void substractRegisterPairs(final CPU currentCpu) {
        int sourceValue = currentCpu.getValueFromRegister(getSourceRegister());
        int destinationValue = currentCpu.getValueFromRegister(getDestinationRegister());
        int result = sourceValue + destinationValue;
        int additionHalfBits = (sourceValue &  BitMasks.HALF_CARRY_16_BIT_RESULT) + (destinationValue &  BitMasks.HALF_CARRY_16_BIT_RESULT);

        currentCpu.setValueInRegister(result, getDestinationRegister());
        currentCpu.setSubtract(false);
        currentCpu.setHalfCarry(additionHalfBits > BitMasks.HALF_CARRY_16_BIT_RESULT);
        currentCpu.setCarry(result > BitMasks.CARRY_16_BIT_RESULTS);
    }

    private void substractRegister(final CPU currentCpu) {
        int sourceValue = currentCpu.getValueFromRegister(getSourceRegister());
        int destinationValue = currentCpu.getValueFromRegister(getDestinationRegister());
        int result = destinationValue - sourceValue;

        currentCpu.setValueInRegister(result, getDestinationRegister());
        currentCpu.setSubtract(true);
        currentCpu.setHalfCarry((result & BitMasks.HALF_CARRY_8_BIT_RESULT_DECREMENT) == BitMasks.HALF_CARRY_8_BIT_RESULT_DECREMENT);
        currentCpu.setCarry(destinationValue < sourceValue);
        currentCpu.setZero((result & BitMasks.MASK_8_BIT_DATA) == 0);
    }

}
