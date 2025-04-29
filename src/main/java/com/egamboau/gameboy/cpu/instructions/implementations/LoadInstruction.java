package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.InstructionCondition;
import com.egamboau.gameboy.cpu.instructions.RegisterType;

public class LoadInstruction extends Instruction{

    public LoadInstruction(AddressMode addressMode, RegisterType sourceRegister, RegisterType destinationRegister,
            InstructionCondition condition, Byte parameter) {
        super(addressMode, sourceRegister, destinationRegister, condition, parameter);
    }

    @Override
    public void run_instruction_logic(CPU currentCpu, int[] data) {
        switch (getAddressMode()) {
            case MEMORY_ADDRESS_REGISTER_TO_REGISTER:
                storeMemoryDataintoRegister(currentCpu);
                break;
            case DATA_16_BITS_TO_REGISTER:
            case DATA_8_BIT_TO_REGISTER:
                storeDataInRegister(currentCpu, data);
                break;
            case REGISTER_TO_MEMORY_ADDRESS_DATA:
                storeRegistertoInmediateMemoryAddress(currentCpu, data);
                break;
            case REGISTER_TO_MEMORY_ADDRESS_REGISTER:
                storeRegisterDataInMemory(currentCpu);  
                break;
            default:
                throw new IllegalArgumentException("Unknown address mode: " + getAddressMode());
        }
    }

    private void storeMemoryDataintoRegister(CPU currentCpu) {
        int address = currentCpu.getValueFromRegister(getSourceRegister());
        int data = currentCpu.readByteFromAddress(address);
        currentCpu.setValueInRegister(data, getDestinationRegister());
    }

    private void storeDataInRegister(CPU currentCpu, int[] data) {
        currentCpu.setValueInRegister(data, getDestinationRegister());
    }

    private void storeRegistertoInmediateMemoryAddress(CPU currentCpu, int[] data) {
        switch (getSourceRegister()) {
            case REGISTER_SP:
                //need to read to bytes from the data, and build an address from it
                int address = (data[1] << 8 ) | data[0];
                int registerValue =currentCpu.getValueFromRegister(getSourceRegister());
                currentCpu.writeByteToAddress(address, registerValue & 0xFF);
                currentCpu.writeByteToAddress(address+1, registerValue >> 8);
                break;
            default:
                throw new IllegalArgumentException("Unknown Source register: " + getSourceRegister());
        }
    }

    private void storeRegisterDataInMemory(CPU currentCpu) {
        int address = 0;
        int data = currentCpu.getValueFromRegister(getSourceRegister());
        address = currentCpu.getValueFromRegister(getDestinationRegister());
        currentCpu.writeByteToAddress(address, data);
    }

}
