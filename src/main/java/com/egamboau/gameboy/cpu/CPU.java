package com.egamboau.gameboy.cpu;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.InstructionCondition;
import com.egamboau.gameboy.cpu.instructions.RegisterType;
import com.egamboau.gameboy.cpu.instructions.implementations.AddInstruction;
import com.egamboau.gameboy.cpu.instructions.implementations.DecimalAdjustAccumulatorInstruction;
import com.egamboau.gameboy.cpu.instructions.implementations.DecrementInstruction;
import com.egamboau.gameboy.cpu.instructions.implementations.IncrementInstruction;
import com.egamboau.gameboy.cpu.instructions.implementations.JumpRelativeInstruction;
import com.egamboau.gameboy.cpu.instructions.implementations.LoadInstruction;
import com.egamboau.gameboy.cpu.instructions.implementations.NoopInstruction;
import com.egamboau.gameboy.cpu.instructions.implementations.OneComplementInstruction;
import com.egamboau.gameboy.cpu.instructions.implementations.RotateLeftCircularInstruction;
import com.egamboau.gameboy.cpu.instructions.implementations.RotateLeftInstruction;
import com.egamboau.gameboy.cpu.instructions.implementations.RotateRightInstruction;
import com.egamboau.gameboy.cpu.instructions.implementations.RotateRigthCircularInstruction;
import com.egamboau.gameboy.memory.Bus;

public class CPU {

    private static final Logger LOGGER = LogManager.getLogger();

    private Register a;
    private Register b;
    private Register c;
    private Register d;
    private Register e;
    private FlagRegister f;
    private Register h;
    private Register l;
    private int spRegister;
    private int pcRegister;

    private boolean halted;

    private Instruction[] instructions = new Instruction[0x100];

    private Bus memoryBus;

    private long cycles;


    public CPU(Bus memoryBus) {
        this.memoryBus = memoryBus;
        this.initializeCPU();
        this.initializeInstructions();
    }

    public boolean isHalted() {
        return halted;
    }

    public void setHalted(boolean halted) {
        this.halted = halted;
    }

    /**
     * Increment the cycles count on the CPU
     * @param cycles
     */
    public void incrementCpuCycles(long cycles) {
        this.cycles += cycles;
    }

    /**
     * Executes one step on the CPU, basically reading and executing instructions one at a time
     */
    public void cpuStep(){
        Instruction instruction = this.fetchInstruction();
        instruction.executeInstruction(this);
    }

    /**
     * Fetch the next instruction from the memory bus, and maps it based on the op code provided. 
     * This method will also increment the PC register by 1 
     * @return the instruction based on the opcode read from memory
     */
    private Instruction fetchInstruction() {
        LOGGER.info("Fetching OPCODE instruction from memory");
        int opcode = this.memoryBus.readByteFromAddress(this.pcRegister++);
        incrementCpuCycles(1L);
        return this.instructions[opcode];
    }

    

    public void incrementRegisterPair(RegisterType register) {
        if(register == RegisterType.REGISTER_HL) {
            int value = Register.combine(h, l);
            value += 1;
            //split the value again
            Register.split(value, h, l);
        } else {
            throw new IllegalArgumentException(String.format("Register %s not supported for 16 bit increment instruction", register));
        }
    }

    public void decrementRegisterPair(RegisterType register) {
        if(register == RegisterType.REGISTER_HL) {
            int value = Register.combine(h, l);
            value -= 1;
            //split the value again
            Register.split(value, h, l);
        } else {
            throw new IllegalArgumentException(String.format("Register %s not supported for 16 bit increment instruction", register));
        }
    }

    /**
     * Initlaizes all the opcodes and store them in array format. This is used to refer to the opcodes and know what is needed for the CPU to run.
     */
    private void initializeInstructions() {
        LOGGER.info("Initializing CPU instructions");
        //0x
        instructions[0x00] = new NoopInstruction(null, null, null, null, null);
        instructions[0x01] = new LoadInstruction(AddressMode.DATA_16_BITS_TO_REGISTER, null, RegisterType.REGISTER_BC, null, null);
        instructions[0x02] = new LoadInstruction(AddressMode.REGISTER_TO_MEMORY_ADDRESS_REGISTER, RegisterType.REGISTER_A, RegisterType.REGISTER_BC,  null, null);
        instructions[0x03] = new IncrementInstruction(AddressMode.REGISTER_16_BIT, RegisterType.REGISTER_BC, RegisterType.REGISTER_BC, null, null);
        instructions[0x04] = new IncrementInstruction(AddressMode.REGISTER_8_BIT, RegisterType.REGISTER_B, RegisterType.REGISTER_B, null, null);
        instructions[0x05] = new DecrementInstruction(AddressMode.REGISTER_8_BIT, RegisterType.REGISTER_B, RegisterType.REGISTER_B, null, null);
        instructions[0x06] = new LoadInstruction(AddressMode.DATA_8_BIT_TO_REGISTER, null,  RegisterType.REGISTER_B, null, null);
        instructions[0x07] = new RotateLeftCircularInstruction(AddressMode.REGISTER_8_BIT,RegisterType.REGISTER_A, RegisterType.REGISTER_A, null, null);
        instructions[0x08] = new LoadInstruction(AddressMode.REGISTER_TO_MEMORY_ADDRESS_DATA,RegisterType.REGISTER_SP, null, null, null);
        instructions[0x09] = new AddInstruction(AddressMode.REGISTER_16_BIT_TO_REGISTER_16_BIT,RegisterType.REGISTER_BC,RegisterType.REGISTER_HL,null, null);
        instructions[0x0A] = new LoadInstruction(AddressMode.MEMORY_ADDRESS_REGISTER_TO_REGISTER,RegisterType.REGISTER_BC,RegisterType.REGISTER_A , null, null);
        instructions[0x0B] = new DecrementInstruction(AddressMode.REGISTER_16_BIT, RegisterType.REGISTER_BC, RegisterType.REGISTER_BC, null, null);
        instructions[0x0C] = new IncrementInstruction(AddressMode.REGISTER_8_BIT, RegisterType.REGISTER_C, RegisterType.REGISTER_C, null, null);
        instructions[0x0D] = new DecrementInstruction(AddressMode.REGISTER_8_BIT, RegisterType.REGISTER_C, RegisterType.REGISTER_C, null, null);
        instructions[0x0E] = new LoadInstruction(AddressMode.DATA_8_BIT_TO_REGISTER, null, RegisterType.REGISTER_C, null, null);
        instructions[0x0F] = new RotateRigthCircularInstruction(AddressMode.REGISTER_8_BIT,RegisterType.REGISTER_A, RegisterType.REGISTER_A,null, null);
        //1x
        instructions[0x10] = new StopInstruction(null, null, null, null, null);
        instructions[0x11] = new LoadInstruction(AddressMode.DATA_16_BITS_TO_REGISTER, null, RegisterType.REGISTER_DE, null, null);
        instructions[0x12] = new LoadInstruction(AddressMode.REGISTER_TO_MEMORY_ADDRESS_REGISTER, RegisterType.REGISTER_A, RegisterType.REGISTER_DE,  null, null);
        instructions[0x13] = new IncrementInstruction(AddressMode.REGISTER_16_BIT, RegisterType.REGISTER_DE, RegisterType.REGISTER_DE, null, null);
        instructions[0x14] = new IncrementInstruction(AddressMode.REGISTER_8_BIT, RegisterType.REGISTER_D, RegisterType.REGISTER_D, null, null);
        instructions[0x15] = new DecrementInstruction(AddressMode.REGISTER_8_BIT, RegisterType.REGISTER_D, RegisterType.REGISTER_D, null, null);
        instructions[0x16] = new LoadInstruction(AddressMode.DATA_8_BIT_TO_REGISTER, null, RegisterType.REGISTER_D, null, null);
        instructions[0x17] = new RotateLeftInstruction(AddressMode.REGISTER_8_BIT, RegisterType.REGISTER_A,RegisterType.REGISTER_A, null, null);
        instructions[0x18] = new JumpRelativeInstruction(AddressMode.DATA_8_BIT_TO_REGISTER, null, RegisterType.REGISTER_PC, null, null);
        instructions[0x19] = new AddInstruction(AddressMode.REGISTER_16_BIT_TO_REGISTER_16_BIT,RegisterType.REGISTER_DE, RegisterType.REGISTER_HL,  null, null);
        instructions[0x1A] = new LoadInstruction(AddressMode.MEMORY_ADDRESS_REGISTER_TO_REGISTER, RegisterType.REGISTER_DE, RegisterType.REGISTER_A, null, null);
        instructions[0x1B] = new DecrementInstruction( AddressMode.REGISTER_16_BIT, RegisterType.REGISTER_DE, RegisterType.REGISTER_DE, null, null);
        instructions[0x1C] = new IncrementInstruction(AddressMode.REGISTER_8_BIT, RegisterType.REGISTER_E, RegisterType.REGISTER_E, null, null);
        instructions[0x1D] = new DecrementInstruction(AddressMode.REGISTER_8_BIT, RegisterType.REGISTER_E, RegisterType.REGISTER_E, null, null);
        instructions[0x1E] = new LoadInstruction(AddressMode.DATA_8_BIT_TO_REGISTER, null, RegisterType.REGISTER_E, null, null);
        instructions[0x1F] = new RotateRightInstruction(AddressMode.REGISTER_8_BIT, RegisterType.REGISTER_A,RegisterType.REGISTER_A, null, null);
        //2x
        instructions[0x20] = new JumpRelativeInstruction(AddressMode.DATA_8_BIT_TO_REGISTER, null, RegisterType.REGISTER_PC, InstructionCondition.Z_FLAG_NOT_SET, null);
        instructions[0x21] = new LoadInstruction(AddressMode.DATA_16_BITS_TO_REGISTER,  null, RegisterType.REGISTER_HL, null, null);
        instructions[0x22] = new LoadInstruction(AddressMode.REGISTER_TO_INCREMENT_16_BIT_MEMORY_ADDRESS,  RegisterType.REGISTER_A,RegisterType.REGISTER_HL, null, null);
        instructions[0x23] = new IncrementInstruction(AddressMode.REGISTER_16_BIT, RegisterType.REGISTER_HL, RegisterType.REGISTER_HL, null, null);
        instructions[0x24] = new IncrementInstruction( AddressMode.REGISTER_8_BIT, RegisterType.REGISTER_H, RegisterType.REGISTER_H, null, null);
        instructions[0x25] = new DecrementInstruction(AddressMode.REGISTER_8_BIT, RegisterType.REGISTER_H, RegisterType.REGISTER_H, null, null);
        instructions[0x26] = new LoadInstruction( AddressMode.DATA_8_BIT_TO_REGISTER, null, RegisterType.REGISTER_H, null, null);
        instructions[0x27] = new DecimalAdjustAccumulatorInstruction(AddressMode.REGISTER_8_BIT,null, RegisterType.REGISTER_A, null, null);
        instructions[0x28] = new JumpRelativeInstruction(AddressMode.DATA_8_BIT_TO_REGISTER, null, RegisterType.REGISTER_PC, InstructionCondition.Z_FLAG_SET, null);
        instructions[0x29] = new AddInstruction(AddressMode.REGISTER_16_BIT_TO_REGISTER_16_BIT,RegisterType.REGISTER_HL, RegisterType.REGISTER_HL, null, null);
        instructions[0x2A] = new LoadInstruction(AddressMode.INCREMENT_16_BIT_MEMORY_ADDRESS_REGISTER_TO_REGISTER,RegisterType.REGISTER_HL, RegisterType.REGISTER_A, null, null);
        instructions[0x2B] = new DecrementInstruction(AddressMode.REGISTER_16_BIT, RegisterType.REGISTER_HL, RegisterType.REGISTER_HL, null, null);
        instructions[0x2C] = new IncrementInstruction(AddressMode.REGISTER_8_BIT, RegisterType.REGISTER_L, RegisterType.REGISTER_L, null, null);
        instructions[0x2D] = new DecrementInstruction(AddressMode.REGISTER_8_BIT, RegisterType.REGISTER_L, RegisterType.REGISTER_L, null, null);
        instructions[0x2E] = new LoadInstruction(AddressMode.DATA_8_BIT_TO_REGISTER,  null, RegisterType.REGISTER_L, null, null);
        instructions[0x2F] = new OneComplementInstruction(AddressMode.REGISTER_8_BIT,RegisterType.REGISTER_A, RegisterType.REGISTER_A, null, null);
        //3x
        instructions[0x30] = new JumpRelativeInstruction(AddressMode.DATA_8_BIT_TO_REGISTER, null, RegisterType.REGISTER_PC, InstructionCondition.CARRY_FLAG_NOT_SET, null);
        instructions[0x31] = new LoadInstruction(AddressMode.DATA_16_BITS_TO_REGISTER, null, RegisterType.REGISTER_SP, null, null);
        instructions[0x32] = new LoadInstruction(AddressMode.REGISTER_TO_DECREMENT_16_BIT_MEMORY_ADDRESS, RegisterType.REGISTER_A, RegisterType.REGISTER_HL,  null, null);

        /*
        
        instructions[0x33] = new Instruction(InstrtuctionType.INCREMENT, AddressMode.REGISTER, RegisterType.REGISTER_SP, null, null, null);
        instructions[0x34] = new Instruction(InstrtuctionType.INCREMENT, AddressMode.MEMORY_ADDRESS, RegisterType.REGISTER_HL, null, null, null);
        instructions[0x35] = new Instruction(InstrtuctionType.DECREMENT, AddressMode.MEMORY_ADDRESS, RegisterType.REGISTER_HL, null, null, null);
        instructions[0x36] = new Instruction(InstrtuctionType.LOAD, AddressMode.MEMORY_ADDRESS, RegisterType.REGISTER_HL, null, null, null);
        instructions[0x37] = new Instruction(InstrtuctionType.SET_CARRY_FLAG, null,null, null, null, null);
        instructions[0x38] = new Instruction(InstrtuctionType.JUMP_RELATIVE, AddressMode.DATA_8_BIT, null, null, InstructionCondition.CARRY_FLAG_SET, null);
        instructions[0x39] = new Instruction(InstrtuctionType.ADD, AddressMode.REGISTER_TO_REGISTER,RegisterType.REGISTER_HL, RegisterType.REGISTER_SP, null, null);
        instructions[0x3A] = new Instruction(InstrtuctionType.LOAD, AddressMode.DECREMENT_HL_REGISTER_TO_MEMORY,RegisterType.REGISTER_HL, RegisterType.REGISTER_A, null, null);
        instructions[0x3B] = new Instruction(InstrtuctionType.DECREMENT, AddressMode.REGISTER, RegisterType.REGISTER_SP, null, null, null);
        instructions[0x3C] = new Instruction(InstrtuctionType.INCREMENT, AddressMode.REGISTER, RegisterType.REGISTER_A, null, null, null);
        instructions[0x3D] = new Instruction(InstrtuctionType.DECREMENT, AddressMode.REGISTER, RegisterType.REGISTER_A, null, null, null);
        instructions[0x3E] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER_TO_8_BIT_DATA, RegisterType.REGISTER_A, null, null, null);
        instructions[0x2F] = new Instruction(InstrtuctionType.FLIP_CARRY_FLAG, AddressMode.REGISTER,RegisterType.REGISTER_A, null, null, null);

        //4x
        instructions[0x40] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER, RegisterType.REGISTER_B, RegisterType.REGISTER_B, null, null);
        instructions[0x41] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER, RegisterType.REGISTER_B, RegisterType.REGISTER_C, null, null);
        instructions[0x42] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER, RegisterType.REGISTER_B, RegisterType.REGISTER_D, null, null);
        instructions[0x43] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER, RegisterType.REGISTER_B, RegisterType.REGISTER_E, null, null);
        instructions[0x44] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER, RegisterType.REGISTER_B, RegisterType.REGISTER_H, null, null);
        instructions[0x45] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER, RegisterType.REGISTER_B, RegisterType.REGISTER_L, null, null);
        instructions[0x46] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER_TO_MEMORY_ADDRESS_REGISTER, RegisterType.REGISTER_B, RegisterType.REGISTER_HL, null, null);
        instructions[0x47] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER, RegisterType.REGISTER_B, RegisterType.REGISTER_A, null, null);
        instructions[0x48] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER, RegisterType.REGISTER_C, RegisterType.REGISTER_B, null, null);
        instructions[0x49] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER, RegisterType.REGISTER_C, RegisterType.REGISTER_C, null, null);
        instructions[0x4A] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER, RegisterType.REGISTER_C, RegisterType.REGISTER_D, null, null);
        instructions[0x4B] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER, RegisterType.REGISTER_C, RegisterType.REGISTER_E, null, null);
        instructions[0x4C] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER, RegisterType.REGISTER_C, RegisterType.REGISTER_H, null, null);
        instructions[0x4D] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER, RegisterType.REGISTER_C, RegisterType.REGISTER_L, null, null);
        instructions[0x4E] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER_TO_MEMORY_ADDRESS_REGISTER, RegisterType.REGISTER_C, RegisterType.REGISTER_HL, null, null);
        instructions[0x4F] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER, RegisterType.REGISTER_C, RegisterType.REGISTER_A, null, null);

        //5x
        instructions[0x50] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER, RegisterType.REGISTER_D, RegisterType.REGISTER_B, null, null);
        instructions[0x51] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER, RegisterType.REGISTER_D, RegisterType.REGISTER_C, null, null);
        instructions[0x52] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER, RegisterType.REGISTER_D, RegisterType.REGISTER_D, null, null);
        instructions[0x53] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER, RegisterType.REGISTER_D, RegisterType.REGISTER_E, null, null);
        instructions[0x54] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER, RegisterType.REGISTER_D, RegisterType.REGISTER_H, null, null);
        instructions[0x55] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER, RegisterType.REGISTER_D, RegisterType.REGISTER_L, null, null);
        instructions[0x56] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER_TO_MEMORY_ADDRESS_REGISTER, RegisterType.REGISTER_D, RegisterType.REGISTER_HL, null, null);
        instructions[0x57] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER, RegisterType.REGISTER_D, RegisterType.REGISTER_A, null, null);
        instructions[0x58] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER, RegisterType.REGISTER_E, RegisterType.REGISTER_B, null, null);
        instructions[0x59] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER, RegisterType.REGISTER_E, RegisterType.REGISTER_C, null, null);
        instructions[0x5A] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER, RegisterType.REGISTER_E, RegisterType.REGISTER_D, null, null);
        instructions[0x5B] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER, RegisterType.REGISTER_E, RegisterType.REGISTER_E, null, null);
        instructions[0x5C] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER, RegisterType.REGISTER_E, RegisterType.REGISTER_H, null, null);
        instructions[0x5D] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER, RegisterType.REGISTER_E, RegisterType.REGISTER_L, null, null);
        instructions[0x5E] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER_TO_MEMORY_ADDRESS_REGISTER, RegisterType.REGISTER_E, RegisterType.REGISTER_HL, null, null);
        instructions[0x5F] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER, RegisterType.REGISTER_E, RegisterType.REGISTER_A, null, null);

        //6x
        instructions[0x60] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER, RegisterType.REGISTER_H, RegisterType.REGISTER_B, null, null);
        instructions[0x61] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER, RegisterType.REGISTER_H, RegisterType.REGISTER_C, null, null);
        instructions[0x62] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER, RegisterType.REGISTER_H, RegisterType.REGISTER_D, null, null);
        instructions[0x63] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER, RegisterType.REGISTER_H, RegisterType.REGISTER_E, null, null);
        instructions[0x64] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER, RegisterType.REGISTER_H, RegisterType.REGISTER_H, null, null);
        instructions[0x65] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER, RegisterType.REGISTER_H, RegisterType.REGISTER_L, null, null);
        instructions[0x66] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER_TO_MEMORY_ADDRESS_REGISTER, RegisterType.REGISTER_H, RegisterType.REGISTER_HL, null, null);
        instructions[0x67] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER, RegisterType.REGISTER_H, RegisterType.REGISTER_A, null, null);
        instructions[0x68] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER, RegisterType.REGISTER_L, RegisterType.REGISTER_B, null, null);
        instructions[0x69] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER, RegisterType.REGISTER_L, RegisterType.REGISTER_C, null, null);
        instructions[0x6A] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER, RegisterType.REGISTER_L, RegisterType.REGISTER_D, null, null);
        instructions[0x6B] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER, RegisterType.REGISTER_L, RegisterType.REGISTER_E, null, null);
        instructions[0x6C] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER, RegisterType.REGISTER_L, RegisterType.REGISTER_H, null, null);
        instructions[0x6D] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER, RegisterType.REGISTER_L, RegisterType.REGISTER_L, null, null);
        instructions[0x6E] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER_TO_MEMORY_ADDRESS_REGISTER, RegisterType.REGISTER_L, RegisterType.REGISTER_HL, null, null);
        instructions[0x6F] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER, RegisterType.REGISTER_L, RegisterType.REGISTER_A, null, null);

        //7x
        instructions[0x70] = new Instruction(InstrtuctionType.LOAD, AddressMode.MEMORY_ADDRESS_REGISTER_TO_REGISTER, RegisterType.REGISTER_HL, RegisterType.REGISTER_B, null, null);
        instructions[0x71] = new Instruction(InstrtuctionType.LOAD, AddressMode.MEMORY_ADDRESS_REGISTER_TO_REGISTER, RegisterType.REGISTER_HL, RegisterType.REGISTER_C, null, null);
        instructions[0x72] = new Instruction(InstrtuctionType.LOAD, AddressMode.MEMORY_ADDRESS_REGISTER_TO_REGISTER, RegisterType.REGISTER_HL, RegisterType.REGISTER_D, null, null);
        instructions[0x73] = new Instruction(InstrtuctionType.LOAD, AddressMode.MEMORY_ADDRESS_REGISTER_TO_REGISTER, RegisterType.REGISTER_HL, RegisterType.REGISTER_E, null, null);
        instructions[0x74] = new Instruction(InstrtuctionType.LOAD, AddressMode.MEMORY_ADDRESS_REGISTER_TO_REGISTER, RegisterType.REGISTER_HL, RegisterType.REGISTER_H, null, null);
        instructions[0x75] = new Instruction(InstrtuctionType.LOAD, AddressMode.MEMORY_ADDRESS_REGISTER_TO_REGISTER, RegisterType.REGISTER_HL, RegisterType.REGISTER_L, null, null);
        instructions[0x76] = new Instruction(InstrtuctionType.HALT, null, null,null, null, null);
        instructions[0x77] = new Instruction(InstrtuctionType.LOAD, AddressMode.MEMORY_ADDRESS_REGISTER_TO_REGISTER, RegisterType.REGISTER_HL, RegisterType.REGISTER_A, null, null);
        instructions[0x78] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER, RegisterType.REGISTER_A, RegisterType.REGISTER_B, null, null);
        instructions[0x79] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER, RegisterType.REGISTER_A, RegisterType.REGISTER_C, null, null);
        instructions[0x7A] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER, RegisterType.REGISTER_A, RegisterType.REGISTER_D, null, null);
        instructions[0x7B] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER, RegisterType.REGISTER_A, RegisterType.REGISTER_E, null, null);
        instructions[0x7C] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER, RegisterType.REGISTER_A, RegisterType.REGISTER_H, null, null);
        instructions[0x7D] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER, RegisterType.REGISTER_A, RegisterType.REGISTER_L, null, null);
        instructions[0x7E] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER_TO_MEMORY_ADDRESS_REGISTER, RegisterType.REGISTER_A, RegisterType.REGISTER_HL, null, null);
        instructions[0x7F] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER, RegisterType.REGISTER_A, RegisterType.REGISTER_A, null, null);

        //8x
        instructions[0x80] = new Instruction(InstrtuctionType.ADD, AddressMode.REGISTER, RegisterType.REGISTER_A, RegisterType.REGISTER_B, null, null);
        instructions[0x81] = new Instruction(InstrtuctionType.ADD, AddressMode.REGISTER, RegisterType.REGISTER_A, RegisterType.REGISTER_C, null, null);
        instructions[0x82] = new Instruction(InstrtuctionType.ADD, AddressMode.REGISTER, RegisterType.REGISTER_A, RegisterType.REGISTER_D, null, null);
        instructions[0x83] = new Instruction(InstrtuctionType.ADD, AddressMode.REGISTER, RegisterType.REGISTER_A, RegisterType.REGISTER_E, null, null);
        instructions[0x84] = new Instruction(InstrtuctionType.ADD, AddressMode.REGISTER, RegisterType.REGISTER_A, RegisterType.REGISTER_H, null, null);
        instructions[0x85] = new Instruction(InstrtuctionType.ADD, AddressMode.REGISTER, RegisterType.REGISTER_A, RegisterType.REGISTER_L, null, null);
        instructions[0x86] = new Instruction(InstrtuctionType.ADD, AddressMode.REGISTER_TO_MEMORY_ADDRESS_REGISTER, RegisterType.REGISTER_A, RegisterType.REGISTER_HL, null, null);
        instructions[0x87] = new Instruction(InstrtuctionType.ADD, AddressMode.REGISTER, RegisterType.REGISTER_A, RegisterType.REGISTER_A, null, null);
        instructions[0x88] = new Instruction(InstrtuctionType.ADD_WITH_CARRY, AddressMode.REGISTER, RegisterType.REGISTER_A, RegisterType.REGISTER_B, null, null);
        instructions[0x89] = new Instruction(InstrtuctionType.ADD_WITH_CARRY, AddressMode.REGISTER, RegisterType.REGISTER_A, RegisterType.REGISTER_C, null, null);
        instructions[0x8A] = new Instruction(InstrtuctionType.ADD_WITH_CARRY, AddressMode.REGISTER, RegisterType.REGISTER_A, RegisterType.REGISTER_D, null, null);
        instructions[0x8B] = new Instruction(InstrtuctionType.ADD_WITH_CARRY, AddressMode.REGISTER, RegisterType.REGISTER_A, RegisterType.REGISTER_E, null, null);
        instructions[0x8C] = new Instruction(InstrtuctionType.ADD_WITH_CARRY, AddressMode.REGISTER, RegisterType.REGISTER_A, RegisterType.REGISTER_H, null, null);
        instructions[0x8D] = new Instruction(InstrtuctionType.ADD_WITH_CARRY, AddressMode.REGISTER, RegisterType.REGISTER_A, RegisterType.REGISTER_L, null, null);
        instructions[0x8E] = new Instruction(InstrtuctionType.ADD_WITH_CARRY, AddressMode.REGISTER_TO_MEMORY_ADDRESS_REGISTER, RegisterType.REGISTER_A, RegisterType.REGISTER_HL, null, null);
        instructions[0x8F] = new Instruction(InstrtuctionType.ADD_WITH_CARRY, AddressMode.REGISTER, RegisterType.REGISTER_A, RegisterType.REGISTER_A, null, null);

        //9x
        instructions[0x90] = new Instruction(InstrtuctionType.SUBSTRACT, AddressMode.REGISTER, RegisterType.REGISTER_B, null, null, null);
        instructions[0x91] = new Instruction(InstrtuctionType.SUBSTRACT, AddressMode.REGISTER, RegisterType.REGISTER_C, null, null, null);
        instructions[0x92] = new Instruction(InstrtuctionType.SUBSTRACT, AddressMode.REGISTER, RegisterType.REGISTER_D, null, null, null);
        instructions[0x93] = new Instruction(InstrtuctionType.SUBSTRACT, AddressMode.REGISTER, RegisterType.REGISTER_E, null, null, null);
        instructions[0x94] = new Instruction(InstrtuctionType.SUBSTRACT, AddressMode.REGISTER, RegisterType.REGISTER_H, null, null, null);
        instructions[0x95] = new Instruction(InstrtuctionType.SUBSTRACT, AddressMode.REGISTER, RegisterType.REGISTER_L, null, null, null);
        instructions[0x96] = new Instruction(InstrtuctionType.SUBSTRACT, AddressMode.REGISTER_TO_MEMORY_ADDRESS_REGISTER, null, RegisterType.REGISTER_HL, null, null);
        instructions[0x97] = new Instruction(InstrtuctionType.SUBSTRACT, AddressMode.REGISTER, RegisterType.REGISTER_A, null, null, null);
        instructions[0x98] = new Instruction(InstrtuctionType.SUBSTRACT_WITH_CARRY, AddressMode.REGISTER, RegisterType.REGISTER_A, RegisterType.REGISTER_B, null, null);
        instructions[0x99] = new Instruction(InstrtuctionType.SUBSTRACT_WITH_CARRY, AddressMode.REGISTER, RegisterType.REGISTER_A, RegisterType.REGISTER_C, null, null);
        instructions[0x9A] = new Instruction(InstrtuctionType.SUBSTRACT_WITH_CARRY, AddressMode.REGISTER, RegisterType.REGISTER_A, RegisterType.REGISTER_D, null, null);
        instructions[0x9B] = new Instruction(InstrtuctionType.SUBSTRACT_WITH_CARRY, AddressMode.REGISTER, RegisterType.REGISTER_A, RegisterType.REGISTER_E, null, null);
        instructions[0x9C] = new Instruction(InstrtuctionType.SUBSTRACT_WITH_CARRY, AddressMode.REGISTER, RegisterType.REGISTER_A, RegisterType.REGISTER_H, null, null);
        instructions[0x9D] = new Instruction(InstrtuctionType.SUBSTRACT_WITH_CARRY, AddressMode.REGISTER, RegisterType.REGISTER_A, RegisterType.REGISTER_L, null, null);
        instructions[0x9E] = new Instruction(InstrtuctionType.SUBSTRACT_WITH_CARRY, AddressMode.REGISTER_TO_MEMORY_ADDRESS_REGISTER, RegisterType.REGISTER_A, RegisterType.REGISTER_HL, null, null);
        instructions[0x9F] = new Instruction(InstrtuctionType.SUBSTRACT_WITH_CARRY, AddressMode.REGISTER, RegisterType.REGISTER_A, RegisterType.REGISTER_A, null, null);

        //Ax
        instructions[0xA0] = new Instruction(InstrtuctionType.AND, AddressMode.REGISTER, RegisterType.REGISTER_B, null, null, null);
        instructions[0xA1] = new Instruction(InstrtuctionType.AND, AddressMode.REGISTER, RegisterType.REGISTER_C, null, null, null);
        instructions[0xA2] = new Instruction(InstrtuctionType.AND, AddressMode.REGISTER, RegisterType.REGISTER_D, null, null, null);
        instructions[0xA3] = new Instruction(InstrtuctionType.AND, AddressMode.REGISTER, RegisterType.REGISTER_E, null, null, null);
        instructions[0xA4] = new Instruction(InstrtuctionType.AND, AddressMode.REGISTER, RegisterType.REGISTER_H, null, null, null);
        instructions[0xA5] = new Instruction(InstrtuctionType.AND, AddressMode.REGISTER, RegisterType.REGISTER_L, null, null, null);
        instructions[0xA6] = new Instruction(InstrtuctionType.AND, AddressMode.REGISTER_TO_MEMORY_ADDRESS_REGISTER, null, RegisterType.REGISTER_HL, null, null);
        instructions[0xA7] = new Instruction(InstrtuctionType.AND, AddressMode.REGISTER, RegisterType.REGISTER_A, null, null, null);
        instructions[0xA8] = new Instruction(InstrtuctionType.XOR, AddressMode.REGISTER, RegisterType.REGISTER_B, null, null, null);
        instructions[0xA9] = new Instruction(InstrtuctionType.XOR, AddressMode.REGISTER, RegisterType.REGISTER_C, null, null, null);
        instructions[0xAA] = new Instruction(InstrtuctionType.XOR, AddressMode.REGISTER, RegisterType.REGISTER_D, null, null, null);
        instructions[0xAB] = new Instruction(InstrtuctionType.XOR, AddressMode.REGISTER, RegisterType.REGISTER_E, null, null, null);
        instructions[0xAC] = new Instruction(InstrtuctionType.XOR, AddressMode.REGISTER, RegisterType.REGISTER_H, null, null, null);
        instructions[0xAD] = new Instruction(InstrtuctionType.XOR, AddressMode.REGISTER, RegisterType.REGISTER_L, null, null, null);
        instructions[0xAE] = new Instruction(InstrtuctionType.XOR, AddressMode.REGISTER_TO_MEMORY_ADDRESS_REGISTER, null, RegisterType.REGISTER_HL, null, null);
        instructions[0xAF] = new Instruction(InstrtuctionType.XOR, AddressMode.REGISTER, RegisterType.REGISTER_A, null, null, null);

        //Bx
        instructions[0xB0] = new Instruction(InstrtuctionType.OR, AddressMode.REGISTER, RegisterType.REGISTER_B, null, null, null);
        instructions[0xB1] = new Instruction(InstrtuctionType.OR, AddressMode.REGISTER, RegisterType.REGISTER_C, null, null, null);
        instructions[0xB2] = new Instruction(InstrtuctionType.OR, AddressMode.REGISTER, RegisterType.REGISTER_D, null, null, null);
        instructions[0xB3] = new Instruction(InstrtuctionType.OR, AddressMode.REGISTER, RegisterType.REGISTER_E, null, null, null);
        instructions[0xB4] = new Instruction(InstrtuctionType.OR, AddressMode.REGISTER, RegisterType.REGISTER_H, null, null, null);
        instructions[0xB5] = new Instruction(InstrtuctionType.OR, AddressMode.REGISTER, RegisterType.REGISTER_L, null, null, null);
        instructions[0xB6] = new Instruction(InstrtuctionType.OR, AddressMode.REGISTER_TO_MEMORY_ADDRESS_REGISTER, null, RegisterType.REGISTER_HL, null, null);
        instructions[0xB7] = new Instruction(InstrtuctionType.OR, AddressMode.REGISTER, RegisterType.REGISTER_A, null, null, null);
        instructions[0xB8] = new Instruction(InstrtuctionType.COMPARE, AddressMode.REGISTER, RegisterType.REGISTER_B, null, null, null);
        instructions[0xB9] = new Instruction(InstrtuctionType.COMPARE, AddressMode.REGISTER, RegisterType.REGISTER_C, null, null, null);
        instructions[0xBA] = new Instruction(InstrtuctionType.COMPARE, AddressMode.REGISTER, RegisterType.REGISTER_D, null, null, null);
        instructions[0xBB] = new Instruction(InstrtuctionType.COMPARE, AddressMode.REGISTER, RegisterType.REGISTER_E, null, null, null);
        instructions[0xBC] = new Instruction(InstrtuctionType.COMPARE, AddressMode.REGISTER, RegisterType.REGISTER_H, null, null, null);
        instructions[0xBD] = new Instruction(InstrtuctionType.COMPARE, AddressMode.REGISTER, RegisterType.REGISTER_L, null, null, null);
        instructions[0xBE] = new Instruction(InstrtuctionType.COMPARE, AddressMode.REGISTER_TO_MEMORY_ADDRESS_REGISTER, null, RegisterType.REGISTER_HL, null, null);
        instructions[0xBF] = new Instruction(InstrtuctionType.COMPARE, AddressMode.REGISTER, RegisterType.REGISTER_A, null, null, null);

        //Cx
        instructions[0xC0] = new Instruction(InstrtuctionType.RETURN, null, null, null, InstructionCondition.Z_FLAG_NOT_SET, null);
        instructions[0xC1] = new Instruction(InstrtuctionType.POP, AddressMode.REGISTER, RegisterType.REGISTER_BC, null, null, null);
        instructions[0xC2] = new Instruction(InstrtuctionType.JUMP, AddressMode.DATA_16_BITS, null, null, InstructionCondition.Z_FLAG_NOT_SET, null);
        instructions[0xC3] = new Instruction(InstrtuctionType.JUMP, AddressMode.DATA_16_BITS, null, null, null, null);
        instructions[0xC4] = new Instruction(InstrtuctionType.CALL, AddressMode.DATA_16_BITS, null, null, InstructionCondition.Z_FLAG_NOT_SET, null);
        instructions[0xC5] = new Instruction(InstrtuctionType.PUSH, AddressMode.REGISTER, RegisterType.REGISTER_BC, null, null, null);
        instructions[0xC6] = new Instruction(InstrtuctionType.ADD, AddressMode.REGISTER_TO_8_BIT_DATA, RegisterType.REGISTER_A, null, null, null);
        instructions[0xC7] = new Instruction(InstrtuctionType.RESET, null, null, null, null, (byte)0x00);
        instructions[0xC8] = new Instruction(InstrtuctionType.RETURN, null, null, null, InstructionCondition.Z_FLAG_SET, null);
        instructions[0xC9] = new Instruction(InstrtuctionType.RETURN, null, null, null, null, null);
        instructions[0xCA] = new Instruction(InstrtuctionType.JUMP, null, null, null, InstructionCondition.Z_FLAG_SET, null);
        instructions[0xCB] = new Instruction(InstrtuctionType.CB_OPCODE_INSTRUCTION, AddressMode.DATA_8_BIT, null, null, null, null);
        instructions[0xCC] = new Instruction(InstrtuctionType.CALL, AddressMode.DATA_16_BITS, null, null, InstructionCondition.Z_FLAG_SET, null);
        instructions[0xCD] = new Instruction(InstrtuctionType.CALL, AddressMode.DATA_16_BITS, null, null, null, null);
        instructions[0xCE] = new Instruction(InstrtuctionType.ADD_WITH_CARRY, AddressMode.REGISTER_TO_8_BIT_DATA, RegisterType.REGISTER_A, null, null, null);
        instructions[0xCF] = new Instruction(InstrtuctionType.RESET, null, null, null, null, (byte)0x01);

        //Dx
        instructions[0xD0] = new Instruction(InstrtuctionType.RETURN, null, null, null, InstructionCondition.CARRY_FLAG_NOT_SET, null);
        instructions[0xD1] = new Instruction(InstrtuctionType.POP, AddressMode.REGISTER, RegisterType.REGISTER_DE, null, null, null);
        instructions[0xD2] = new Instruction(InstrtuctionType.JUMP, AddressMode.DATA_16_BITS, null, null, InstructionCondition.CARRY_FLAG_NOT_SET, null);
        //D3 is not a valid instruction opcode
        instructions[0xD4] = new Instruction(InstrtuctionType.CALL, AddressMode.DATA_16_BITS, null, null, InstructionCondition.CARRY_FLAG_NOT_SET, null);
        instructions[0xD5] = new Instruction(InstrtuctionType.PUSH, AddressMode.REGISTER, RegisterType.REGISTER_DE, null, null, null);
        instructions[0xD6] = new Instruction(InstrtuctionType.SUBSTRACT, AddressMode.REGISTER_TO_8_BIT_DATA, RegisterType.REGISTER_A, null, null, null);
        instructions[0xD7] = new Instruction(InstrtuctionType.RESET, null, null, null, null, (byte)0x10);
        instructions[0xD8] = new Instruction(InstrtuctionType.RETURN, null, null, null, InstructionCondition.CARRY_FLAG_SET, null);
        instructions[0xD9] = new Instruction(InstrtuctionType.RETURN_INTERUPT, null, null, null, InstructionCondition.CARRY_FLAG_SET, null);
        instructions[0xDA] = new Instruction(InstrtuctionType.JUMP, null, null, null, InstructionCondition.CARRY_FLAG_SET, null);
        //DB is not a valid instruction opcode
        instructions[0xDC] = new Instruction(InstrtuctionType.CALL, AddressMode.DATA_16_BITS, null, null, InstructionCondition.CARRY_FLAG_SET, null);
        //DD is not a valid instruction opcode
        instructions[0xDE] = new Instruction(InstrtuctionType.SUBSTRACT_WITH_CARRY, AddressMode.REGISTER_TO_8_BIT_DATA, RegisterType.REGISTER_A, null, null, null);
        instructions[0xDF] = new Instruction(InstrtuctionType.RESET, null, null, null, null, (byte)0x18);

        //Ex
        instructions[0xE0] = new Instruction(InstrtuctionType.LOAD, AddressMode.ADDRESS_8_BIT_TO_REGISTER, null, RegisterType.REGISTER_A, InstructionCondition.CARRY_FLAG_NOT_SET, null);
        instructions[0xE1] = new Instruction(InstrtuctionType.POP, AddressMode.REGISTER, RegisterType.REGISTER_HL, null, null, null);
        instructions[0xE2] = new Instruction(InstrtuctionType.JUMP, AddressMode.DATA_16_BITS, null, null, InstructionCondition.CARRY_FLAG_NOT_SET, null);
        //E3 is not a valid instruction opcode
        //E4 is not a valid instruction opcode
        instructions[0xE5] = new Instruction(InstrtuctionType.PUSH, AddressMode.REGISTER, RegisterType.REGISTER_HL, null, null, null);
        instructions[0xE6] = new Instruction(InstrtuctionType.AND, AddressMode.REGISTER_TO_8_BIT_DATA, RegisterType.REGISTER_A, null, null, null);
        instructions[0xE7] = new Instruction(InstrtuctionType.RESET, null, null, null, null, (byte)0x20);
        instructions[0xE8] = new Instruction(InstrtuctionType.ADD, AddressMode.REGISTER_TO_8_BIT_DATA, RegisterType.REGISTER_SP, null, null, null);
        instructions[0xE9] = new Instruction(InstrtuctionType.JUMP, null, RegisterType.REGISTER_HL, null, null, null);
        instructions[0xEA] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER_TO_16_BIT_DATA, null, RegisterType.REGISTER_A, null, null);
        //EB is not a valid instruction opcode
        //EC is not a valid instruction opcode
        //ED is not a valid instruction opcode
        instructions[0xEE] = new Instruction(InstrtuctionType.XOR, AddressMode.DATA_8_BIT, null, RegisterType.REGISTER_A, null, null);
        instructions[0xEF] = new Instruction(InstrtuctionType.RESET, null, null, null, null, (byte)0x28);

        //Fx
        instructions[0xF0] = new Instruction(InstrtuctionType.LOAD, AddressMode.ADDRESS_8_BIT_TO_REGISTER, RegisterType.REGISTER_A, null,null, null);
        instructions[0xF1] = new Instruction(InstrtuctionType.POP, AddressMode.REGISTER, RegisterType.REGISTER_AF, null, null, null);
        instructions[0xF2] = new Instruction(InstrtuctionType.LOAD, AddressMode.MEMORY_ADDRESS_REGISTER_TO_REGISTER, RegisterType.REGISTER_A, null,null, null);
        instructions[0xF3] = new Instruction(InstrtuctionType.RESET_INTERRUPT_MASTER, null, null, null,null, null);
        //F4 is not a valid instruction opcode
        instructions[0xF5] = new Instruction(InstrtuctionType.PUSH, AddressMode.REGISTER, RegisterType.REGISTER_AF, null, null, null);
        instructions[0xF6] = new Instruction(InstrtuctionType.OR, AddressMode.REGISTER_TO_8_BIT_DATA, RegisterType.REGISTER_A, null, null, null);
        instructions[0xF7] = new Instruction(InstrtuctionType.RESET, null, null, null, null, (byte)0x30);
        instructions[0xF8] = new Instruction(InstrtuctionType.LOAD, AddressMode.HL_TO_SP_REGISTER, RegisterType.REGISTER_HL, RegisterType.REGISTER_SP, null, null);
        instructions[0xF9] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER_TO_REGISTER, RegisterType.REGISTER_SP, RegisterType.REGISTER_HL, null, null);
        instructions[0xFA] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER_TO_16_BIT_DATA, RegisterType.REGISTER_A, null, null, null);
        instructions[0xFB] = new Instruction(InstrtuctionType.SETINTERRUPT_MASTER, null, null, null, null, null);
        //FC is not a valid instruction opcode
        //FD is not a valid instruction opcode
        instructions[0xFE] = new Instruction(InstrtuctionType.COMPARE, AddressMode.DATA_8_BIT, null, null, null, null);
        instructions[0xFF] = new Instruction(InstrtuctionType.RESET, null, null, null, null, (byte)0x38);
        */
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

    public int readByteFromAddress(int address) {
        this.incrementCpuCycles(1L);
        return memoryBus.readByteFromAddress(address);
    }

    public void writeByteToAddress(int address, int data) {
        memoryBus.writeByteToAddress(data, address);
        incrementCpuCycles(1L);
    }

    public int getValueFromRegister(RegisterType register) {
        int result;
        switch (register) {
            case REGISTER_A:
                result = a.get();
                break;
            case REGISTER_AF:
                result =  Register.combine(a, f);
                break;
            case REGISTER_B:
                result =  b.get();
                break;
            case REGISTER_BC:
                result =  Register.combine(b, c);
                break;
            case REGISTER_C:
                result =  c.get();
                break;
            case REGISTER_D:
                result =  d.get();
                break;
            case REGISTER_DE:
                result =  Register.combine(d, e);
                break;
            case REGISTER_E:
                result =  e.get();
                break;
            case REGISTER_F:
                result =  f.get();
                break;
            case REGISTER_H:
                result =  h.get();
                break;
            case REGISTER_HL:
                result =  Register.combine(h, l);
                break;
            case REGISTER_L:
                result =  l.get();
                break;
            case REGISTER_PC:
                result =  pcRegister & 0xFFFF;
                break;
            case REGISTER_SP:
                result =  spRegister & 0xFFFF;
                break;
            default:
                throw new IllegalArgumentException("Retrieving data not supported for this register " + register);
        }
        return result;
    }

    public void setSubtract(boolean value) {
        f.setSubtract(value);
    }

    public void setHalfCarry(boolean value) {
        f.setHalfCarry(value);
    }

    public void setCarry(boolean value) {
        f.setCarry(value);
    }

    
    public void setValueInRegister(int data, RegisterType registerType) {
        switch (registerType) {
            case REGISTER_A:
                a.set(data);
                break;
            case REGISTER_B:
                b.set(data);
                break;
            case REGISTER_C:
                c.set(data);
                break;
            case REGISTER_D:
                d.set(data);
                break;
            case REGISTER_E:
                e.set(data);
                break;
            case REGISTER_H:
                h.set(data);
                break;
            case REGISTER_L:
                l.set(data);
                break;
            case REGISTER_SP:
                spRegister = data & 0xFFFF;
                break;
            case REGISTER_HL:
                Register.split(data, h , l);
                incrementCpuCycles(1L);
                break;
            case REGISTER_BC:
                Register.split(data, b, c);
                incrementCpuCycles(1L);
                break;
            case REGISTER_DE:
                Register.split(data, d, e);
                incrementCpuCycles(1L);
                break;
            default:
                throw new IllegalArgumentException("Setting data not supported for this register " + registerType);
        }
    }

    public void setZero(boolean value) {
        f.setZero(value);
    }

    public int getDataFromPCAndIncrement() {
        return readByteFromAddress(pcRegister++);
    }

    public void setValueInRegister(int[] data, RegisterType destinationRegister) {
        switch (destinationRegister) {
            case REGISTER_B:
                b.set(data[0]);
                break;
            case REGISTER_C:
                c.set(data[0]);
                break;
            case REGISTER_D:
                d.set(data[0]);
                break;
            case REGISTER_E:
                e.set(data[0]);
                break;
            case REGISTER_H:
                h.set(data[0]);
                break;
            case REGISTER_L:
                l.set(data[0]);
                break;
            case REGISTER_BC:
                c.set(data[0]);
                b.set(data[1]);
                break;
            case REGISTER_DE:
                e.set(data[0]);
                d.set(data[1]);
                break;
            case REGISTER_HL:
                l.set(data[0]);
                h.set(data[1]);
                break;
            case REGISTER_SP:
                this.spRegister = ((data[1]<<8) | data[0]) & 0xFFFF;
                break;
            default:
                throw new IllegalArgumentException("Unknown destination register: " + destinationRegister);
        }
    }

    public long getCycles() {
        return cycles;
    }

    public boolean getSubtract() {
        return f.getSubtract();
    }

    public boolean getHalfCarry() {
        return f.getHalfCarry();
    }

    public boolean getCarry() {
        return f.getCarry();
    }

    public boolean getZero() {
        return f.getZero();
    }

    public void incrementPCRegister(byte address) {
        this.pcRegister += address;
        this.incrementCpuCycles(1);
    }
}
