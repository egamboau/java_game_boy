package com.egamboau.gameboy.cpu;

import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.InstructionCondition;
import com.egamboau.gameboy.cpu.instructions.RegisterType;

public class StopInstruction extends Instruction{

    public StopInstruction(AddressMode addressMode, RegisterType sourceRegister, RegisterType destinationRegister,
            InstructionCondition condition, Byte parameter) {
        super(addressMode, sourceRegister, destinationRegister, condition, parameter);
    }

    @Override
    protected void runInstructionLogic(CPU currentCpu, int[] data) {
        // TODO Stop behaves weird, but not all the hardware is actually implemented for that. For now, set it as stopped
        currentCpu.setHalted(true);
    }
    
}
