package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.InstructionCondition;
import com.egamboau.gameboy.cpu.instructions.RegisterType;

public class RotateRightInstruction extends Instruction{

    public RotateRightInstruction(AddressMode addressMode, RegisterType sourceRegister,
            RegisterType destinationRegister, InstructionCondition condition, Byte parameter) {
        super(addressMode, sourceRegister, destinationRegister, condition, parameter);
    }

    @Override
    protected void runInstructionLogic(CPU currentCpu, int[] data) {
        int value = currentCpu.getValueFromRegister(getSourceRegister());
        int futureCarry = value & 0x01;
        
        int result = ((value | (futureCarry << 8)) >> 1);

        currentCpu.setValueInRegister(result, getDestinationRegister());
        currentCpu.setCarry(futureCarry != 0);
        currentCpu.setZero(false);
        currentCpu.setSubtract(false);
        currentCpu.setHalfCarry(false);
    }



}
