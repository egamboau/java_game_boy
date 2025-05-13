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
    public void runInstructionLogic(CPU currentCpu, int[] data) {
        switch (getAddressMode()) {
            case REGISTER_8_BIT:            
                decrementRegister(currentCpu);
                break;
            case REGISTER_16_BIT:
                decrementRegisterPair(currentCpu);
                break;        
            default:
                throw new IllegalArgumentException(String.format("Address mode %s not supported", getAddressMode()));
        }
    }


    private void decrementRegisterPair(CPU currentCpu) {
        int result = getDecrementedRegisterData(currentCpu) & 0xFFFF;
        currentCpu.setValueInRegister(result, getDestinationRegister());
    }

    private void decrementRegister(CPU currentCpu) {
        int result = getDecrementedRegisterData(currentCpu)  & 0xFF;
        currentCpu.setValueInRegister(result, getDestinationRegister());
        currentCpu.setZero(result == 0);
        currentCpu.setSubtract(true);
        currentCpu.setHalfCarry((result & 0x0F) == 0x0F);
    }

    private int getDecrementedRegisterData(CPU currentCpu) {
        int originalValue = currentCpu.getValueFromRegister(getSourceRegister());
        return originalValue -1;
    }

}
