package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.InstructionCondition;
import com.egamboau.gameboy.cpu.instructions.RegisterType;

public class OneComplementInstruction extends Instruction{

    public OneComplementInstruction(AddressMode addressMode, RegisterType sourceRegister,
            RegisterType destinationRegister, InstructionCondition condition, Byte parameter) {
        super(addressMode, sourceRegister, destinationRegister, condition, parameter);
    }

    @Override
    protected void run_instruction_logic(CPU currentCpu, int[] data) {
        int value = currentCpu.getValueFromRegister(getSourceRegister());
        int result = (~value);
        currentCpu.setValueInRegister(result, getDestinationRegister());
        currentCpu.setSubtract(false);
        currentCpu.setHalfCarry(false);
    }

}
