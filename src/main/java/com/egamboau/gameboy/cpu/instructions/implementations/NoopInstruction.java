package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.InstructionCondition;
import com.egamboau.gameboy.cpu.instructions.RegisterType;

public class NoopInstruction extends Instruction{

    public NoopInstruction(AddressMode addressMode, RegisterType sourceRegister, RegisterType destinationRegister,
            InstructionCondition condition, Byte parameter) {
        super(addressMode, sourceRegister, destinationRegister, condition, parameter);
    }

    @Override
    public void runInstructionLogic(CPU currentCpu, int[] data) {
      // Instruction for the NOOP does nothing, thus leaving method empty.
    }

    

}
