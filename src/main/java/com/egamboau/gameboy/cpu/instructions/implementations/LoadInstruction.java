package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.RegisterType;
import com.egamboau.gameboy.memory.BitMasks;

/**
 * Load instruction implementation.
 *
 * <p>Handles the various load/addressing modes supported by the Game Boy CPU.
 * This class centralises the logic to move data between registers, memory and
 * immediate operands according to the instruction's addressing mode.</p>
 */
public class LoadInstruction extends Instruction {

    /**
     * Constructs a LoadInstruction with the specified parameters.
     *
     * @param addressMode The addressing mode of the instruction.
     * @param sourceRegister The source register for the operation.
     * @param destinationRegister The destination register for the operation.
     */
    public LoadInstruction(final AddressMode addressMode, final RegisterType sourceRegister,
            final RegisterType destinationRegister) {
        super(addressMode, sourceRegister, destinationRegister);
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
            case DECREMENT_16_BIT_MEMORY_ADDRESS_REGISTER_TO_REGISTER:
                storeMemoryDataintoRegister(currentCpu);
                currentCpu.decrementRegisterPair(getSourceRegister());
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
            case REGISTER_PAIR_TO_MEMORY_ADDRESS_DATA, REGISTER_TO_MEMORY_ADDRESS_DATA:
                storeRegistertoInmediateMemoryAddress(currentCpu, data);
                break;
            case REGISTER_TO_INDIRECT_REGISTER:
                storeRegisterDataInMemory(currentCpu);
                break;
            case DATA_8_BIT_TO_MEMORY_ADDRESS_REGISTER:
                storeDataInMemory(currentCpu, data);
                break;
            case REGISTER_TO_REGISTER:
                storeSourceRegisterDataInDestinationRegister(currentCpu);
                break;
            case REGISTER_TO_MEMORY_ADDRESS_DATA_LOWER_BYTE:
                storeRegistertoInmediateLowerByteMemoryAddress(currentCpu, data);
                break;
            case REGISTER_TO_INDIRECT_REGISTER_LOWER_BYTE:
                storeRegisterToLowerByteMemoryAddress(currentCpu);
                break;
            case MEMORY_ADDRESS_DATA_LOWER_BYTE_TO_REGISTER:
                storeMemoryDataintoRegisterWithOffset(currentCpu, data);
                break;
            case INDIRECT_REGISTER_LOWER_BYTE_TO_REGISTER:
                storeLowerByteMemoryAddressRegisterToRegister(currentCpu);
                break;
            case MEMORY_ADDRESS_DATA_TO_REGISTER:
                storeInmediateMemoryAddressDataToRegister(currentCpu, data);
                break;
            default:
                throw new IllegalArgumentException("Unknown address mode: " + getAddressMode());
        }
    }

    private void storeInmediateMemoryAddressDataToRegister(final CPU currentCpu, final int[] data) {
        int address = (data[1] << BitMasks.MASK_8_BIT_SHIFT) | data[0];
        currentCpu.setValueInRegister(currentCpu.readByteFromAddress(address), getDestinationRegister());
    }

    private void storeLowerByteMemoryAddressRegisterToRegister(final CPU currentCpu) {
        int address = BitMasks.LOAD_ADDRESS_OFFSET + currentCpu.getValueFromRegister(getSourceRegister());
        currentCpu.setValueInRegister(currentCpu.readByteFromAddress(address), getDestinationRegister());
    }

    private void storeMemoryDataintoRegisterWithOffset(final CPU currentCpu, final int[] data) {
        int address = BitMasks.LOAD_ADDRESS_OFFSET + data[0];
        currentCpu.setValueInRegister(currentCpu.readByteFromAddress(address), getDestinationRegister());
    }

    private void storeRegisterToLowerByteMemoryAddress(final CPU currentCpu) {
        int address = BitMasks.LOAD_ADDRESS_OFFSET + currentCpu.getValueFromRegister(getDestinationRegister());
        int registerValue = currentCpu.getValueFromRegister(getSourceRegister());
        currentCpu.writeByteToAddress(address, registerValue & BitMasks.MASK_8_BIT_DATA);
    }

    private void storeRegistertoInmediateLowerByteMemoryAddress(final CPU currentCpu, final int[] data) {
        // need to read to bytes from the data, and build an address from it
        int address = BitMasks.LOAD_ADDRESS_OFFSET + data[0];
        int registerValue = currentCpu.getValueFromRegister(getSourceRegister());
        currentCpu.writeByteToAddress(address, registerValue & BitMasks.MASK_8_BIT_DATA);
    }

    private void storeMemoryDataintoRegister(final CPU currentCpu) {
        int address = currentCpu.getValueFromRegister(getSourceRegister());
        int data = currentCpu.readByteFromAddress(address);
        currentCpu.setValueInRegister(data, getDestinationRegister());
    }

    private void storeDataInRegister(final CPU currentCpu, final int[] data) {
        int dataToSet = 0;
        if (data.length == 2) {
            dataToSet = (data[1] << BitMasks.MASK_8_BIT_SHIFT) + data[0];
        } else {
            dataToSet = data[0];
        }
        currentCpu.setValueInRegister(dataToSet, getDestinationRegister());
    }

    private void storeSourceRegisterDataInDestinationRegister(final CPU currentCpu) {
        currentCpu.setValueInRegister(currentCpu.getValueFromRegister(getSourceRegister()), getDestinationRegister());
    }

    @SuppressWarnings("checkstyle:magicnumber")
    private void storeRegistertoInmediateMemoryAddress(final CPU currentCpu, final int[] data) {
        // need to read to bytes from the data, and build an address from it
        int address = (data[1] << 8) | data[0];
        int registerValue = currentCpu.getValueFromRegister(getSourceRegister());
        if (getAddressMode() == AddressMode.REGISTER_PAIR_TO_MEMORY_ADDRESS_DATA) {
            currentCpu.writeByteToAddress(address, registerValue & BitMasks.MASK_8_BIT_DATA);
            currentCpu.writeByteToAddress(address + 1, registerValue >> 8);
        } else {
            currentCpu.writeByteToAddress(address, registerValue & BitMasks.MASK_8_BIT_DATA);
        }
    }

    private void storeRegisterDataInMemory(final CPU currentCpu) {
        int address = 0;
        int data = currentCpu.getValueFromRegister(getSourceRegister());
        address = currentCpu.getValueFromRegister(getDestinationRegister());
        currentCpu.writeByteToAddress(address, data);
    }

    private void storeDataInMemory(final CPU currentCpu, final int[] data) {
        int address = currentCpu.getValueFromRegister(getDestinationRegister());
        int dataToStore = data[0];
        currentCpu.writeByteToAddress(address, dataToStore);
    }

    @Override
    protected int getInternalCycles(final CPU currentCpu) {
        if (getAddressMode() == AddressMode.REGISTER_TO_REGISTER
            && getSourceRegister() == RegisterType.HL
            && getDestinationRegister() == RegisterType.SP) {
            return 1;
        }
        return  super.getInternalCycles(currentCpu);
    }
}
