package com.egamboau.gameboy.cpu;

import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.InstrtuctionType;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.InstructionCondition;
import com.egamboau.gameboy.cpu.instructions.RegisterType;

public class CPU {

    private byte a;
    private byte b;
    private byte c;
    private byte d;
    private byte e;
    private byte f;
    private byte h;
    private byte l;
    private int SP;
    private int PC;

    private boolean halted;
    private boolean stepping;

    private Instruction instructions[] = new Instruction[0x100];
    
    public CPU() {
        this.initializeCPU();
        this.initializeInstructions();
    }

    private void initializeInstructions() {
        //0x
        instructions[0x00] = new Instruction(InstrtuctionType.NOOP, null, null, null, null, null);
        instructions[0x01] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER_TO_16_BIT_DATA, RegisterType.REGISTER_BC, null, null, null);
        instructions[0x02] = new Instruction(InstrtuctionType.LOAD, AddressMode.MEMORY_ADDRESS_REGISTER_TO_REGISTER, RegisterType.REGISTER_BC, RegisterType.REGISTER_A, null, null);
        instructions[0x03] = new Instruction(InstrtuctionType.INCREMENT, AddressMode.REGISTER, RegisterType.REGISTER_BC, null, null, null);
        instructions[0x04] = new Instruction(InstrtuctionType.INCREMENT, AddressMode.REGISTER, RegisterType.REGISTER_B, null, null, null);
        instructions[0x05] = new Instruction(InstrtuctionType.DECREMENT, AddressMode.REGISTER, RegisterType.REGISTER_B, null, null, null);
        instructions[0x06] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER_TO_8_BIT_DATA, RegisterType.REGISTER_B, null, null, null);
        instructions[0x07] = new Instruction(InstrtuctionType.ROTATE_LEFT_CARRY_A, null,null, null, null, null);
        instructions[0x08] = new Instruction(InstrtuctionType.LOAD, AddressMode.MEMORY_ADDRESS_DATA_TO_REGISTER,RegisterType.REGISTER_NONE, RegisterType.REGISTER_SP, null, null);
        instructions[0x09] = new Instruction(InstrtuctionType.ADD, AddressMode.REGISTER_TO_REGISTER,RegisterType.REGISTER_BC, RegisterType.REGISTER_HL, null, null);
        instructions[0x0A] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER_TO_MEMORY_ADDRESS_REGISTER,RegisterType.REGISTER_A, RegisterType.REGISTER_BC, null, null);
        instructions[0x0B] = new Instruction(InstrtuctionType.DECREMENT, AddressMode.REGISTER, RegisterType.REGISTER_BC, null, null, null);
        instructions[0x0C] = new Instruction(InstrtuctionType.INCREMENT, AddressMode.REGISTER, RegisterType.REGISTER_C, null, null, null);
        instructions[0x0D] = new Instruction(InstrtuctionType.DECREMENT, AddressMode.REGISTER, RegisterType.REGISTER_C, null, null, null);
        instructions[0x0E] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER_TO_8_BIT_DATA, RegisterType.REGISTER_C, null, null, null);
        instructions[0x0F] = new Instruction(InstrtuctionType.ROTATE_RIGTH_CARRY_A, null,null, null, null, null);

        //1x
        instructions[0x10] = new Instruction(InstrtuctionType.STOP, null, null, null, null, null);
        instructions[0x11] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER_TO_16_BIT_DATA, RegisterType.REGISTER_DE, null, null, null);
        instructions[0x12] = new Instruction(InstrtuctionType.LOAD, AddressMode.MEMORY_ADDRESS_REGISTER_TO_REGISTER, RegisterType.REGISTER_DE, RegisterType.REGISTER_A, null, null);
        instructions[0x13] = new Instruction(InstrtuctionType.INCREMENT, AddressMode.REGISTER, RegisterType.REGISTER_DE, null, null, null);
        instructions[0x14] = new Instruction(InstrtuctionType.INCREMENT, AddressMode.REGISTER, RegisterType.REGISTER_D, null, null, null);
        instructions[0x15] = new Instruction(InstrtuctionType.DECREMENT, AddressMode.REGISTER, RegisterType.REGISTER_D, null, null, null);
        instructions[0x16] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER_TO_8_BIT_DATA, RegisterType.REGISTER_D, null, null, null);
        instructions[0x17] = new Instruction(InstrtuctionType.ROTATE_LEFT_A, null,null, null, null, null);
        instructions[0x18] = new Instruction(InstrtuctionType.JUMP_RELATIVE, AddressMode.DATA_8_BIT, null, null, null, null);
        instructions[0x19] = new Instruction(InstrtuctionType.ADD, AddressMode.REGISTER_TO_REGISTER,RegisterType.REGISTER_HL, RegisterType.REGISTER_DE, null, null);
        instructions[0x1A] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER_TO_MEMORY_ADDRESS_REGISTER,RegisterType.REGISTER_A, RegisterType.REGISTER_DE, null, null);
        instructions[0x1B] = new Instruction(InstrtuctionType.DECREMENT, AddressMode.REGISTER, RegisterType.REGISTER_DE, null, null, null);
        instructions[0x1C] = new Instruction(InstrtuctionType.INCREMENT, AddressMode.REGISTER, RegisterType.REGISTER_E, null, null, null);
        instructions[0x1D] = new Instruction(InstrtuctionType.DECREMENT, AddressMode.REGISTER, RegisterType.REGISTER_E, null, null, null);
        instructions[0x1E] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER_TO_8_BIT_DATA, RegisterType.REGISTER_E, null, null, null);
        instructions[0x1F] = new Instruction(InstrtuctionType.ROTATE_RIGTH_A, null,null, null, null, null);

        //2x
        instructions[0x20] = new Instruction(InstrtuctionType.JUMP_RELATIVE, AddressMode.DATA_8_BIT, null, null, InstructionCondition.Z_FLAG_NOT_SET, null);
        instructions[0x21] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER_TO_16_BIT_DATA, RegisterType.REGISTER_HL, null, null, null);
        instructions[0x22] = new Instruction(InstrtuctionType.LOAD, AddressMode.INCREMENT_HL_REGISTER_TO_MEMORY, RegisterType.REGISTER_HL, RegisterType.REGISTER_A, null, null);
        instructions[0x23] = new Instruction(InstrtuctionType.INCREMENT, AddressMode.REGISTER, RegisterType.REGISTER_HL, null, null, null);
        instructions[0x24] = new Instruction(InstrtuctionType.INCREMENT, AddressMode.REGISTER, RegisterType.REGISTER_H, null, null, null);
        instructions[0x25] = new Instruction(InstrtuctionType.DECREMENT, AddressMode.REGISTER, RegisterType.REGISTER_H, null, null, null);
        instructions[0x26] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER_TO_8_BIT_DATA, RegisterType.REGISTER_H, null, null, null);
        instructions[0x27] = new Instruction(InstrtuctionType.DECIMAL_ACUMULATOR_ADJUSTMENT, AddressMode.REGISTER,RegisterType.REGISTER_A, null, null, null);
        instructions[0x28] = new Instruction(InstrtuctionType.JUMP_RELATIVE, AddressMode.DATA_8_BIT, null, null, InstructionCondition.Z_FLAG_SET, null);
        instructions[0x29] = new Instruction(InstrtuctionType.ADD, AddressMode.REGISTER_TO_REGISTER,RegisterType.REGISTER_HL, RegisterType.REGISTER_HL, null, null);
        instructions[0x2A] = new Instruction(InstrtuctionType.LOAD, AddressMode.INCREMENT_HL_REGISTER_TO_REGISTER,RegisterType.REGISTER_HL, RegisterType.REGISTER_A, null, null);
        instructions[0x2B] = new Instruction(InstrtuctionType.DECREMENT, AddressMode.REGISTER, RegisterType.REGISTER_HL, null, null, null);
        instructions[0x2C] = new Instruction(InstrtuctionType.INCREMENT, AddressMode.REGISTER, RegisterType.REGISTER_L, null, null, null);
        instructions[0x2D] = new Instruction(InstrtuctionType.DECREMENT, AddressMode.REGISTER, RegisterType.REGISTER_L, null, null, null);
        instructions[0x2E] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER_TO_8_BIT_DATA, RegisterType.REGISTER_L, null, null, null);
        instructions[0x2F] = new Instruction(InstrtuctionType.ONE_COMPLEMENT, AddressMode.REGISTER,RegisterType.REGISTER_A, null, null, null);

        //3x
        instructions[0x30] = new Instruction(InstrtuctionType.JUMP_RELATIVE, AddressMode.DATA_8_BIT, null, null, InstructionCondition.CARRY_FLAG_NOT_SET, null);
        instructions[0x31] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER_TO_16_BIT_DATA, RegisterType.REGISTER_SP, null, null, null);
        instructions[0x32] = new Instruction(InstrtuctionType.LOAD, AddressMode.DECREMENT_HL_REGISTER_TO_MEMORY, RegisterType.REGISTER_HL, RegisterType.REGISTER_A, null, null);
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
        instructions[0x40] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER, RegisterType.REGISTER_B, RegisterType.REGISTER_B, InstructionCondition.CARRY_FLAG_NOT_SET, null);
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
        instructions[0x50] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER, RegisterType.REGISTER_D, RegisterType.REGISTER_B, InstructionCondition.CARRY_FLAG_NOT_SET, null);
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
        instructions[0x60] = new Instruction(InstrtuctionType.LOAD, AddressMode.REGISTER, RegisterType.REGISTER_H, RegisterType.REGISTER_B, InstructionCondition.CARRY_FLAG_NOT_SET, null);
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
        instructions[0x70] = new Instruction(InstrtuctionType.LOAD, AddressMode.MEMORY_ADDRESS_REGISTER_TO_REGISTER, RegisterType.REGISTER_HL, RegisterType.REGISTER_B, InstructionCondition.CARRY_FLAG_NOT_SET, null);
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
        instructions[0x80] = new Instruction(InstrtuctionType.ADD, AddressMode.REGISTER, RegisterType.REGISTER_A, RegisterType.REGISTER_B, InstructionCondition.CARRY_FLAG_NOT_SET, null);
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

        

    }

    private void initializeCPU() {
        this.a = 0;
        this.b = 0;
        this.c = 0;
        this.d = 0;
        this.e = 0;
        this.f = 0;
        this.h = 0;
        this.l = 0;
        this.SP = 0;
        this.PC = 0;
        this.halted = false;
        this.stepping = false;
    }
}
