package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.InstructionCondition;
import com.egamboau.gameboy.cpu.instructions.RegisterType;

public class DecrementInstruction extends Instruction{

    public DecrementInstruction(AddressMode addressMode, RegisterType sourceRegister, RegisterType destinationRegister,
            InstructionCondition condition, Byte parameter) {
        super(addressMode, sourceRegister, destinationRegister, condition, parameter);
    }

    @Override
    public void run_instruction_logic(CPU currentCpu, int[] data) {
        switch (getAddressMode()) {
            case REGISTER:
                decrementRegister(currentCpu);
                break;            
            default:
                throw new IllegalArgumentException(String.format("Address mode %s not supported", getAddressMode()));
        }
    }

    private void decrementRegister(CPU currentCpu) {
        int original_value = currentCpu.getValueFromRegister(getSourceRegister());
        int result = original_value -1;
        currentCpu.setValueInRegister(result, getSourceRegister());
        currentCpu.setZero(result == 0);
        currentCpu.setSubtract(true);
        currentCpu.setHalfCarry((result & 0x0F) == 0x0F);
    }

}
