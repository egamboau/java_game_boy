package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.InstructionCondition;
import com.egamboau.gameboy.cpu.instructions.RegisterType;
import com.egamboau.gameboy.memory.BitMasks;

public class LoadInstruction extends Instruction {

    /**
     * Constructs a LoadInstruction with the specified parameters.
     *
     * @param addressMode The addressing mode of the instruction.
     * @param sourceRegister The source register for the operation.
     * @param destinationRegister The destination register for the operation.
     * @param condition The condition under which the instruction executes.
     * @param parameter An optional parameter for the instruction.
     */
    public LoadInstruction(final AddressMode addressMode, final RegisterType sourceRegister,
            final RegisterType destinationRegister,
            final InstructionCondition condition, final Byte parameter) {
        super(addressMode, sourceRegister, destinationRegister, condition, parameter);
    }

    @Override
    public final void runInstructionLogic(final CPU currentCpu, final int[] data) {
        switch (getAddressMode()) {
            case MEMORY_ADDRESS_REGISTER_TO_REGISTER:
                storeMemoryDataintoRegister(currentCpu);
                break;
            case INCREMENT_16_BIT_MEMORY_ADDRESS_REGISTER_TO_REGISTER:
                storeMemoryDataintoRegister(currentCpu);
                currentCpu.incrementRegisterPair(getSourceRegister());
                break;
            case REGISTER_TO_INCREMENT_16_BIT_MEMORY_ADDRESS:
                storeRegisterDataInMemory(currentCpu);
                currentCpu.incrementRegisterPair(getDestinationRegister());
                break;
            case REGISTER_TO_DECREMENT_16_BIT_MEMORY_ADDRESS:
                storeRegisterDataInMemory(currentCpu);
                currentCpu.decrementRegisterPair(getDestinationRegister());
                break;
            case DATA_16_BITS_TO_REGISTER, DATA_8_BIT_TO_REGISTER:
                storeDataInRegister(currentCpu, data);
                break;
            case REGISTER_TO_MEMORY_ADDRESS_DATA:
                storeRegistertoInmediateMemoryAddress(currentCpu, data);
                break;
            case REGISTER_TO_INDIRECT_REGISTER:
                storeRegisterDataInMemory(currentCpu);
                break;
            default:
                throw new IllegalArgumentException("Unknown address mode: " + getAddressMode());
        }
    }

    private void storeMemoryDataintoRegister(final CPU currentCpu) {
        int address = currentCpu.getValueFromRegister(getSourceRegister());
        int data = currentCpu.readByteFromAddress(address);
        currentCpu.setValueInRegister(data, getDestinationRegister());
    }

    private void storeDataInRegister(final CPU currentCpu, final int[] data) {
        currentCpu.setValueInRegister(data, getDestinationRegister());
    }

    @SuppressWarnings("checkstyle:magicnumber")
    private void storeRegistertoInmediateMemoryAddress(final CPU currentCpu, final int[] data) {
        if (getSourceRegister() == RegisterType.SP) {
            // need to read to bytes from the data, and build an address from it
            int address = (data[1] << 8) | data[0];
            int registerValue = currentCpu.getValueFromRegister(getSourceRegister());
            currentCpu.writeByteToAddress(address, registerValue & BitMasks.MASK_8_BIT_DATA);
            currentCpu.writeByteToAddress(address + 1, registerValue >> 8);
        } else {
            throw new IllegalArgumentException("Unknown Source register: " + getSourceRegister());
        }
    }

    private void storeRegisterDataInMemory(final CPU currentCpu) {
        int address = 0;
        int data = currentCpu.getValueFromRegister(getSourceRegister());
        address = currentCpu.getValueFromRegister(getDestinationRegister());
        currentCpu.writeByteToAddress(address, data);
    }

}
