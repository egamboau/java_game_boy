package com.egamboau.gameboy.cpu.instructions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.egamboau.gameboy.cpu.CPU;

public abstract class Instruction {

    private static final Logger LOGGER = LogManager.getLogger();

    private AddressMode addressMode = null;
    private RegisterType sourceRegister = null;
    private RegisterType destinationRegister = null;
    private InstructionCondition condition = null;
    private Byte parameter = 0;

    protected Instruction(AddressMode addressMode, RegisterType sourceRegister,RegisterType destinationRegister, InstructionCondition condition, Byte parameter) {
        this.addressMode = addressMode;
        this.sourceRegister = sourceRegister;
        this.destinationRegister = destinationRegister;
        this.condition = condition;
        this.parameter = parameter;
    }

    public AddressMode getAddressMode() {
        return addressMode;
    }

    public RegisterType getSourceRegister() {
        return sourceRegister;
    }

    public RegisterType getDestinationRegister() {
        return destinationRegister;
    }

    public InstructionCondition getCondition() {
        return condition;
    }

    public Byte getParameter() {
        return parameter;
    }

    private int[] fetchData(CPU currentCpu) {
        LOGGER.info("Fetching data needed for instruction {}", this);
        if(getAddressMode() == null) {
            return new int[0];
        } else {
            switch (getAddressMode()) {
                case REGISTER_TO_MEMORY_ADDRESS_DATA,DATA_16_BITS_TO_REGISTER:
                    //read 2 bytes from memory.
                    int firstByte = currentCpu.getDataFromPCAndIncrement();
                    int secondByte = currentCpu.getDataFromPCAndIncrement();
                    return new int[] {firstByte, secondByte};
                case DATA_8_BIT_TO_REGISTER:
                    int data = currentCpu.getDataFromPCAndIncrement();
                    return new int[]{data};
                case REGISTER_8_BIT,REGISTER_TO_REGISTER,REGISTER_16_BIT,REGISTER_16_BIT_TO_REGISTER_16_BIT,REGISTER_TO_MEMORY_ADDRESS_REGISTER,
                MEMORY_ADDRESS_REGISTER_TO_REGISTER,REGISTER_TO_INCREMENT_16_BIT_MEMORY_ADDRESS,INCREMENT_16_BIT_MEMORY_ADDRESS_REGISTER_TO_REGISTER,
                REGISTER_TO_DECREMENT_16_BIT_MEMORY_ADDRESS:
                    //data is on the register itself, so no data to fetch
                    return new int[0];
                default:
                    throw new IllegalArgumentException("Address mode not supported: " + getAddressMode());
            }
        }
    }

    public void executeInstruction(CPU currentCpu) {
        int[] data = fetchData(currentCpu);
        runInstructionLogic(currentCpu, data);
    }

    protected abstract void runInstructionLogic(CPU currentCpu, int[] data);    
}
