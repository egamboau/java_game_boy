package com.egamboau.gameboy.cpu;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.RegisterType;
import com.egamboau.gameboy.memory.BitMasks;
import com.egamboau.gameboy.memory.Bus;

public class CPU {

    /**
     * Logger instance for logging CPU-related information.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Register A used for arithmetic and logic operations.
     */
    private Register a;

    /**
     * Register B used for general-purpose operations.
     */
    private Register b;
    /**
     * Register C used for general-purpose operations.
     */
    private Register c;

    /**
     * Register D used for general-purpose operations.
     */
    private Register d;
    /**
     * Register E used for general-purpose operations.
     */
    private Register e;

    /**
     * Flag register used for storing CPU flags.
     */
    private FlagRegister f;
    /**
     * Register H used for general-purpose operations.
     */
    private Register h;

    /**
     * Register L used for general-purpose operations.
     */
    private Register l;
    /**
     * Stack Pointer register used to store the address of the top of the stack.
     */
    private int spRegister;

    /**
     * Program Counter register used to store the address of the next instruction to execute.
     */
    private int pcRegister;

    /**
     * Indicates whether the CPU is in a halted state.
     */
    private boolean halted;

    /**
     * Memory bus used for communication between the CPU and memory.
     */
    private Bus memoryBus;

    /**
     * The number of cycles executed by the CPU.
     */
    private long cycles;


    /**
     * Constructs a CPU instance and initializes it with the provided memory bus.
     *
     * @param bus The memory bus used for communication between the CPU and memory.
     */
    public CPU(final Bus bus) {
        this.memoryBus = bus;
        this.initializeCPU();
    }

    /**
     * Checks if the CPU is in a halted state.
     *
     * @return true if the CPU is halted, false otherwise.
     */
    public final boolean isHalted() {
        return halted;
    }

    /**
     * Sets the halted state of the CPU.
     *
     * @param isHalted true to halt the CPU, false to resume.
     */
    public final void setHalted(final boolean isHalted) {
        this.halted = isHalted;
    }

    /**
     * Increment the cycles count on the CPU.
     *
     * @param cyclesAdjustment
     */
    public void incrementCpuCycles(final long cyclesAdjustment) {
        this.cycles += cyclesAdjustment;
    }

    /**
     * Executes one step on the CPU, basically reading and executing instructions
     * one at a time.
     */
    public void cpuStep() {
        Instruction instruction = this.fetchInstruction();
        instruction.executeInstruction(this);
    }

    /**
     * Fetch the next instruction from the memory bus, and maps it based on the op
     * code provided.
     * This method will also increment the PC register by 1
     *
     * @return the instruction based on the opcode read from memory
     */
    private Instruction fetchInstruction() {
        LOGGER.info("Fetching OPCODE instruction from memory");
        int opcode = this.memoryBus.readByteFromAddress(this.pcRegister++);
        incrementCpuCycles(1L);
        return Instruction.geInstructionFromOpcode(opcode);
    }

    /**
     * Increments the value of a 16-bit register pair by 1.
     *
     * @param register The register pair to increment.
     * @throws IllegalArgumentException if the provided register is not supported.
     */
    public final void incrementRegisterPair(final RegisterType register) {
        if (register == RegisterType.HL) {
            int value = Register.combine(h, l);
            value += 1;
            // split the value again
            Register.split(value, h, l);
        } else {
            throw new IllegalArgumentException(
                    String.format(
                        "Register %s not supported for 16 bit increment instruction", register));
        }
    }

    /**
     * Decrements the value of a 16-bit register pair by 1.
     *
     * @param register The register pair to decrement.
     * @throws IllegalArgumentException if the provided register is not supported.
     */
    public final void decrementRegisterPair(final RegisterType register) {
        if (register == RegisterType.HL) {
            int value = Register.combine(h, l);
            value -= 1;
            // split the value again
            Register.split(value, h, l);
        } else {
            throw new IllegalArgumentException(
                    String.format(
                        "Register %s not supported for 16 bit increment instruction", register));
        }
    }

    private void initializeCPU() {
        LOGGER.info("Initializing CPU Registers to default values");
        this.a = new Register();
        this.b = new Register();
        this.c = new Register();
        this.d = new Register();
        this.e = new Register();
        this.f = new FlagRegister();
        this.h = new Register();
        this.l = new Register();
        this.spRegister = 0;
        this.pcRegister = 0;
        this.halted = false;
        this.cycles = 0;
    }

    /**
     * Reads a byte from the specified memory address and increments the CPU cycles.
     *
     * @param address The memory address to read from.
     * @return The byte value read from the specified address.
     */
    public final int readByteFromAddress(final int address) {
        this.incrementCpuCycles(1L);
        return memoryBus.readByteFromAddress(address);
    }

    /**
     * Writes a byte of data to the specified memory address and increments the CPU cycles.
     *
     * @param address The memory address to write to.
     * @param data The byte value to write to the specified address.
     */
    public final void writeByteToAddress(final int address, final int data) {
        memoryBus.writeByteToAddress(data, address);
        incrementCpuCycles(1L);
    }

    /**
     * Retrieves the value stored in the specified register.
     *
     * @param register The register from which to retrieve the value.
     * @return The value stored in the specified register.
     * @throws IllegalArgumentException if the register type is not supported.
     */
    public final int getValueFromRegister(final RegisterType register) {
        int result;
        switch (register) {
            case A:
                result = a.get();
                break;
            case AF:
                result = Register.combine(a, f);
                break;
            case B:
                result = b.get();
                break;
            case BC:
                result = Register.combine(b, c);
                break;
            case C:
                result = c.get();
                break;
            case D:
                result = d.get();
                break;
            case DE:
                result = Register.combine(d, e);
                break;
            case E:
                result = e.get();
                break;
            case F:
                result = f.get();
                break;
            case H:
                result = h.get();
                break;
            case HL:
                result = Register.combine(h, l);
                break;
            case L:
                result = l.get();
                break;
            case PC:
                result = pcRegister & BitMasks.MASK_16_BIT_DATA;
                break;
            case SP:
                result = spRegister & BitMasks.MASK_16_BIT_DATA;
                break;
            default:
                throw new IllegalArgumentException("Retrieving data not supported for this register " + register);
        }
        return result;
    }

    /**
     * Sets the subtract flag in the flag register.
     *
     * @param value true to set the subtract flag, false to clear it.
     */
    public final void setSubtract(final boolean value) {
        f.setSubtract(value);
    }

    /**
     * Sets the half-carry flag in the flag register.
     *
     * @param value true to set the half-carry flag, false to clear it.
     */
    public final void setHalfCarry(final boolean value) {
        f.setHalfCarry(value);
    }

    /**
     * Sets the carry flag in the flag register.
     *
     * @param value true to set the carry flag, false to clear it.
     */
    public final void setCarry(final boolean value) {
        f.setCarry(value);
    }

    /**
     * Sets the value in the specified register.
     *
     * @param data The value to set in the register.
     * @param registerType The type of register where the value will be set.
     * @throws IllegalArgumentException if the register type is not supported.
     */
    public final void setValueInRegister(final int data, final RegisterType registerType) {
        switch (registerType) {
            case A:
                a.set(data);
                break;
            case B:
                b.set(data);
                break;
            case C:
                c.set(data);
                break;
            case D:
                d.set(data);
                break;
            case E:
                e.set(data);
                break;
            case H:
                h.set(data);
                break;
            case L:
                l.set(data);
                break;
            case SP:
                spRegister = data & BitMasks.MASK_16_BIT_DATA;
                incrementCpuCycles(1L);
                break;
            case HL:
                Register.split(data, h, l);
                incrementCpuCycles(1L);
                break;
            case BC:
                Register.split(data, b, c);
                incrementCpuCycles(1L);
                break;
            case DE:
                Register.split(data, d, e);
                incrementCpuCycles(1L);
                break;
            default:
                throw new IllegalArgumentException("Setting data not supported for this register " + registerType);
        }
    }

    /**
     * Sets the zero flag in the flag register.
     *
     * @param value true to set the zero flag, false to clear it.
     */
    public final void setZero(final boolean value) {
        f.setZero(value);
    }

    /**
     * Reads a byte from the memory address pointed to by the program counter (PC),
     * increments the PC, and returns the byte read.
     *
     * @return The byte value read from the memory address pointed to by the PC.
     */
    public final int getDataFromPCAndIncrement() {
        return readByteFromAddress(pcRegister++);
    }

    /**
     * Sets the value in the specified register or register pair using an array of data.
     *
     * @param data The array of data to set in the register. For 16-bit registers, the array should contain two elements.
     * @param destinationRegister The type of register where the value will be set.
     * @throws IllegalArgumentException if the register type is not supported.
     */
    @SuppressWarnings("checkstyle:magicnumber")
    public final void setValueInRegister(final int[] data, final RegisterType destinationRegister) {
        switch (destinationRegister) {
            case B:
                b.set(data[0]);
                break;
            case C:
                c.set(data[0]);
                break;
            case D:
                d.set(data[0]);
                break;
            case E:
                e.set(data[0]);
                break;
            case H:
                h.set(data[0]);
                break;
            case L:
                l.set(data[0]);
                break;
            case BC:
                c.set(data[0]);
                b.set(data[1]);
                break;
            case DE:
                e.set(data[0]);
                d.set(data[1]);
                break;
            case HL:
                l.set(data[0]);
                h.set(data[1]);
                break;
            case SP:
                this.spRegister = ((data[1] << 8) | data[0]) & BitMasks.MASK_16_BIT_DATA;
                break;
            default:
                throw new IllegalArgumentException("Unknown destination register: " + destinationRegister);
        }
    }

    /**
     * Retrieves the total number of cycles executed by the CPU.
     *
     * @return The total number of CPU cycles.
     */
    public final long getCycles() {
        return cycles;
    }

    /**
     * Retrieves the subtract flag from the flag register.
     *
     * @return true if the subtract flag is set, false otherwise.
     */
    public final boolean getSubtract() {
        return f.getSubtract();
    }

    /**
     * Retrieves the half carry flag from the flag register.
     *
     * @return true if the subtract flag is set, false otherwise.
     */
    public final boolean getHalfCarry() {
        return f.getHalfCarry();
    }

    /**
     * Retrieves the carry flag from the flag register.
     *
     * @return true if the subtract flag is set, false otherwise.
     */
    public final boolean getCarry() {
        return f.getCarry();
    }

    /**
     * Retrieves the zero flag from the flag register.
     *
     * @return true if the subtract flag is set, false otherwise.
     */
    public final boolean getZero() {
        return f.getZero();
    }

    /**
     * Increments the program counter (PC) register by the specified address value.
     *
     * @param address The value to increment the PC register by.
     */
    public final void incrementPCRegister(final byte address) {
        this.pcRegister += address;
        this.incrementCpuCycles(1);
    }
}
