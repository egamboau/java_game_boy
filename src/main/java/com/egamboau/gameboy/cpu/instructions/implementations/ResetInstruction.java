package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.RegisterType;
import com.egamboau.gameboy.memory.BitMasks;

public class ResetInstruction extends Instruction {



    private int vector;

    public ResetInstruction(final int vector) {
        super(null, null, null);
        this.vector = vector;
    }

    @Override
    protected void runInstructionLogic(final CPU currentCpu, final int[] data) {
        currentCpu.pushWord(currentCpu.getValueFromRegister(RegisterType.PC));
        currentCpu.setValueInRegister(vector & BitMasks.MASK_8_BIT_DATA, RegisterType.PC);
    }

    @Override
    protected int getInternalCycles(final CPU currentCpu) {
        return 1;
    }
}
