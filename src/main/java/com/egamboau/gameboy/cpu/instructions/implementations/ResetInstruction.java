package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.RegisterType;
import com.egamboau.gameboy.memory.BitMasks;

public class ResetInstruction extends Instruction {



    private int vector;

    /**
     * Creates a reset instruction for the specified vector.
     *
     * @param currentVector reset vector address
     */
    public ResetInstruction(final int currentVector) {
        super(null, null, null);
        this.vector = currentVector;
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
