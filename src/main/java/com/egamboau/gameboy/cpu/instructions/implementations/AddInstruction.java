package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.InstructionCondition;
import com.egamboau.gameboy.cpu.instructions.RegisterType;

public class AddInstruction extends Instruction{

    public AddInstruction(AddressMode addressMode, RegisterType sourceRegister, RegisterType destinationRegister,
            InstructionCondition condition, Byte parameter) {
        super(addressMode, sourceRegister, destinationRegister, condition, parameter);
    }

    @Override
    public void run_instruction_logic(CPU currentCpu, int[] data) {
        switch (getAddressMode()) {
            case REGISTER_TO_REGISTER:
                this.add_registers(currentCpu);
                break;
            default:
                throw new IllegalArgumentException("Address mode not supported for ADD instruction: " + getAddressMode());
        }
    }

    private void add_registers(CPU currentCpu) {
        int result = currentCpu.getValueFromRegister(getSourceRegister()) + currentCpu.getValueFromRegister(getDestinationRegister());
        currentCpu.setValueInRegister(result, getDestinationRegister());
        currentCpu.setSubtract(false);
        currentCpu.setHalfCarry((result & 0x0FFF) == 0);
        currentCpu.setCarry(result > 0xFFFF);
    }

}
