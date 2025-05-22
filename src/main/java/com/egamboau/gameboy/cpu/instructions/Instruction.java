package com.egamboau.gameboy.cpu.instructions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.egamboau.gameboy.cpu.CPU;

/**
 * Abstract class representing a CPU instruction in the Game Boy emulator.
 * Each instruction has an addressing mode, source and destination registers,
 * an optional condition for execution, and an optional parameter.
 * Subclasses must implement the logic for executing the instruction.
 */
public abstract class Instruction {

    /**
     * Logger instance for logging messages related to the Instruction class.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * The addressing mode used by the instruction.
     */
    private AddressMode addressMode = null;

    /**
     * The source register involved in the instruction.
     */
    private RegisterType sourceRegister = null;

    /**
     * The destination register involved in the instruction.
     */
    private RegisterType destinationRegister = null;

    /**
     * The condition under which the instruction is executed.
     */
    private InstructionCondition condition = null;

    /**
     * The parameter associated with the instruction.
     */
    private Byte parameter = 0;

    /**
     * Constructs an Instruction with the specified properties.
     *
     * @param currentAddressMode The addressing mode used by the instruction.
     * @param currentSourceRegister The source register involved in the instruction.
     * @param currentDestinationRegister The destination register involved in the instruction.
     * @param currentCondition The condition under which the instruction is executed.
     * @param currentParamter The parameter associated with the instruction.
     */
    protected Instruction(final AddressMode currentAddressMode, final RegisterType currentSourceRegister,
            final RegisterType currentDestinationRegister, final InstructionCondition currentCondition, final Byte currentParamter) {
        this.addressMode = currentAddressMode;
        this.sourceRegister = currentSourceRegister;
        this.destinationRegister = currentDestinationRegister;
        this.condition = currentCondition;
        this.parameter = currentParamter;
    }

    /**
     * Retrieves the addressing mode used by the instruction.
     *
     * @return The addressing mode.
     */
    public final AddressMode getAddressMode() {
        return addressMode;
    }

    /**
     * Retrieves the source register involved in the instruction.
     *
     * @return The source register.
     */
    public final RegisterType getSourceRegister() {
        return sourceRegister;
    }

    /**
     * Retrieves the destination register involved in the instruction.
     *
     * @return The destination register.
     */
    public final RegisterType getDestinationRegister() {
        return destinationRegister;
    }

    /**
     * Retrieves the condition under which the instruction is executed.
     *
     * @return The execution condition.
     */
    public final InstructionCondition getCondition() {
        return condition;
    }

    /**
     * Retrieves the parameter associated with the instruction.
     *
     * @return The parameter.
     */
    public final Byte getParameter() {
        return parameter;
    }

    /**
     * Fetches the data required for the instruction based on its addressing mode.
     *
     * @param currentCpu The CPU instance from which data is fetched.
     * @return An array of integers representing the fetched data.
     * @throws IllegalArgumentException If the addressing mode is not supported.
     */
    private int[] fetchData(final CPU currentCpu) {
        LOGGER.info("Fetching data needed for instruction {}", this);
        if (getAddressMode() == null) {
            return new int[0];
        } else {
            switch (getAddressMode()) {
                case REGISTER_TO_MEMORY_ADDRESS_DATA, DATA_16_BITS_TO_REGISTER:
                    // Read 2 bytes from memory.
                    int firstByte = currentCpu.getDataFromPCAndIncrement();
                    int secondByte = currentCpu.getDataFromPCAndIncrement();
                    return new int[] {firstByte, secondByte };
                case DATA_8_BIT_TO_REGISTER:
                    int data = currentCpu.getDataFromPCAndIncrement();
                    return new int[] {data };
                case REGISTER_8_BIT, REGISTER_TO_REGISTER, REGISTER_16_BIT, REGISTER_16_BIT_TO_REGISTER_16_BIT,
                        REGISTER_TO_INDIRECT_REGISTER,
                        MEMORY_ADDRESS_REGISTER_TO_REGISTER, REGISTER_TO_INCREMENT_16_BIT_MEMORY_ADDRESS,
                        INCREMENT_16_BIT_MEMORY_ADDRESS_REGISTER_TO_REGISTER,
                        REGISTER_TO_DECREMENT_16_BIT_MEMORY_ADDRESS:
                    // Data is on the register itself, so no data to fetch.
                    return new int[0];
                default:
                    throw new IllegalArgumentException("Address mode not supported: " + getAddressMode());
            }
        }
    }

    /**
     * Executes the instruction on the provided CPU instance.
     *
     * @param currentCpu The CPU instance on which the instruction is executed.
     */
    public final void executeInstruction(final CPU currentCpu) {
        int[] data = fetchData(currentCpu);
        runInstructionLogic(currentCpu, data);
    }

    /**
     * Abstract method to define the logic for executing the instruction.
     * Subclasses must implement this method to provide specific behavior.
     *
     * @param currentCpu The CPU instance on which the instruction is executed.
     * @param data The data fetched for the instruction.
     */
    protected abstract void runInstructionLogic(CPU currentCpu, int[] data);
}
