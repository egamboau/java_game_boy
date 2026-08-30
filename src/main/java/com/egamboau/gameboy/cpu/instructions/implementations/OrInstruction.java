package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.RegisterType;

public class OrInstruction extends Instruction {

    /**
     * Creates an OR operation between the source operand and destination register.
     *
     * @param currentAddressMode how the source operand is addressed
     * @param currentSourceRegister register supplying the operand or memory address
     * @param currentDestinationRegister register receiving the result
     */
    public OrInstruction(final AddressMode currentAddressMode, final RegisterType currentSourceRegister,
            final RegisterType currentDestinationRegister) {
        super(currentAddressMode, currentSourceRegister, currentDestinationRegister);
    }

    @Override
    protected final void runInstructionLogic(final CPU currentCpu, final int[] data) {
        switch (getAddressMode()) {
            case REGISTER_TO_REGISTER:
                this.orRegisters(currentCpu);
                break;
            case MEMORY_ADDRESS_REGISTER_TO_REGISTER:
                this.orIndirectRegisterData(currentCpu);
                break;
            case DATA_8_BIT_TO_REGISTER:
                this.orDirectDataToRegister(currentCpu, data);
                break;
            default:
                throw new IllegalArgumentException(
                        "Address mode not supported for OR instruction: " + getAddressMode());
        }
    }

    private void orDirectDataToRegister(final CPU currentCpu, final int[] data) {
        int sourceValue = data[0];
        int destinationValue = currentCpu.getValueFromRegister(getDestinationRegister());

        int result = sourceValue | destinationValue;
        currentCpu.setValueInRegister(result, getDestinationRegister());
        setFlags(currentCpu, result);
    }

    private void orIndirectRegisterData(final CPU currentCpu) {
        int addressValue = currentCpu.getValueFromRegister(getSourceRegister());
        int destinationValue = currentCpu.getValueFromRegister(getDestinationRegister());
        int sourceValue = currentCpu.readByteFromAddress(addressValue);
        int result = sourceValue | destinationValue;

        currentCpu.setValueInRegister(result, getDestinationRegister());
        setFlags(currentCpu, result);
    }

    private void orRegisters(final CPU currentCpu) {
        int sourceValue = currentCpu.getValueFromRegister(getSourceRegister());
        int destinationValue = currentCpu.getValueFromRegister(getDestinationRegister());

        int result = sourceValue | destinationValue;
        currentCpu.setValueInRegister(result, getDestinationRegister());
        setFlags(currentCpu, result);
    }

    private void setFlags(final CPU currentCpu, final int result) {
        currentCpu.setZero(result == 0);
        currentCpu.setSubtract(false);
        currentCpu.setHalfCarry(false);
        currentCpu.setCarry(false);
    }
}
