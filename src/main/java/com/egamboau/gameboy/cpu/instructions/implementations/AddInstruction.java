package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.RegisterType;
import com.egamboau.gameboy.memory.BitMasks;

public class AddInstruction extends Instruction {

    /**
     * Constructs an AddInstruction with the specified parameters.
     *
     * @param addressMode The addressing mode of the instruction.
     * @param sourceRegister The source register for the operation.
     * @param destinationRegister The destination register for the operation.
     */
    public AddInstruction(final AddressMode addressMode, final RegisterType sourceRegister,
            final RegisterType destinationRegister) {
        super(addressMode, sourceRegister, destinationRegister);
    }

    @Override
    public final void runInstructionLogic(final CPU currentCpu, final int[] data) {
        switch (getAddressMode()) {
            case REGISTER_TO_REGISTER:
                this.addRegisters(currentCpu);
                break;
            case REGISTER_16_BIT_TO_REGISTER_16_BIT:
                this.addRegisterPairs(currentCpu);
                break;
            case MEMORY_ADDRESS_REGISTER_TO_REGISTER:
                this.addIndirectRegisterData(currentCpu);
                break;
            default:
                throw new IllegalArgumentException(
                        "Address mode not supported for ADD instruction: " + getAddressMode());
        }
    }

    @Override
    protected final int getInternalCycles(final CPU currentCpu) {
        return getAddressMode() == AddressMode.REGISTER_16_BIT_TO_REGISTER_16_BIT ? 1 : 0;
    }

    private void addRegisterPairs(final CPU currentCpu) {
        currentCpu.add16BitRegisters(getSourceRegister(), getDestinationRegister());
    }

    private void addRegisters(final CPU currentCpu) {
        int sourceValue = currentCpu.getValueFromRegister(getSourceRegister());
        int destinationValue = currentCpu.getValueFromRegister(getDestinationRegister());
        int result = sourceValue + destinationValue;
        int additionHalfBits = (sourceValue &  BitMasks.HALF_CARRY_8_BIT_RESULT) + (destinationValue &  BitMasks.HALF_CARRY_8_BIT_RESULT);

        currentCpu.setValueInRegister(result, getDestinationRegister());
        currentCpu.setSubtract(false);
        currentCpu.setHalfCarry(additionHalfBits > BitMasks.HALF_CARRY_8_BIT_RESULT);
        currentCpu.setCarry(result  > BitMasks.CARRY_8_BIT_RESULTS);
        currentCpu.setZero((result & BitMasks.MASK_8_BIT_DATA) == 0);
    }

    private void addIndirectRegisterData(final CPU currentCpu) {
        int addressValue = currentCpu.getValueFromRegister(getSourceRegister());
        int destinationValue = currentCpu.getValueFromRegister(getDestinationRegister());
        int sourceValue = currentCpu.readByteFromAddress(addressValue);
        int result = sourceValue + destinationValue;

        int additionHalfBits = (sourceValue &  BitMasks.HALF_CARRY_8_BIT_RESULT) + (destinationValue &  BitMasks.HALF_CARRY_8_BIT_RESULT);
        currentCpu.setValueInRegister(result, getDestinationRegister());
        currentCpu.setSubtract(false);
        currentCpu.setHalfCarry(additionHalfBits > BitMasks.HALF_CARRY_8_BIT_RESULT);
        currentCpu.setCarry(result  > BitMasks.CARRY_8_BIT_RESULTS);
        currentCpu.setZero((result & BitMasks.MASK_8_BIT_DATA) == 0);
    }

}
