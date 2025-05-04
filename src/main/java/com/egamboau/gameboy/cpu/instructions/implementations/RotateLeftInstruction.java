package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.InstructionCondition;
import com.egamboau.gameboy.cpu.instructions.RegisterType;

public class RotateLeftInstruction extends Instruction{

    public RotateLeftInstruction(AddressMode addressMode, RegisterType sourceRegister, RegisterType destinationRegister,
            InstructionCondition condition, Byte parameter) {
        super(addressMode, sourceRegister, destinationRegister, condition, parameter);
    }

    @Override
    protected void run_instruction_logic(CPU currentCpu, int[] data) {
        int value = currentCpu.getValueFromRegister(getSourceRegister());
        int currentCarry = currentCpu.getCarry()?1:0;

        int result = ((value << 1) | currentCarry);
        int carryResult = (result & 0x100);

        currentCpu.setValueInRegister(result, getDestinationRegister());
        currentCpu.setCarry(carryResult != 0);
        currentCpu.setZero(false);
        currentCpu.setSubtract(false);
        currentCpu.setHalfCarry(false);
    }

}
