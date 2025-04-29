package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.InstructionCondition;
import com.egamboau.gameboy.cpu.instructions.RegisterType;

public class RotateRigthCircularInstruction  extends Instruction{

    public RotateRigthCircularInstruction(AddressMode addressMode, RegisterType sourceRegister,
            RegisterType destinationRegister, InstructionCondition condition, Byte parameter) {
        super(addressMode, sourceRegister, destinationRegister, condition, parameter);
    }

    @Override
    protected void run_instruction_logic(CPU currentCpu, int[] data) {
        int value = currentCpu.getValueFromRegister(getSourceRegister());
        int carryOut = (value & 0x01) << 7;
        int result = ((value >> 1) | carryOut) & 0xFF;
        currentCpu.setValueInRegister(result, getDestinationRegister());
        currentCpu.setCarry(carryOut == 0x80);
        currentCpu.setZero(false);
        currentCpu.setSubtract(false);
        currentCpu.setHalfCarry(false);
    }

}
