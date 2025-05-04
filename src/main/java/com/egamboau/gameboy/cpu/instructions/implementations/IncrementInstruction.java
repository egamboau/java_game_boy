package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.InstructionCondition;
import com.egamboau.gameboy.cpu.instructions.RegisterType;

public class IncrementInstruction extends Instruction{

    public IncrementInstruction(AddressMode addressMode, RegisterType sourceRegister, RegisterType destinationRegister,
            InstructionCondition condition, Byte parameter) {
        super(addressMode, sourceRegister, destinationRegister, condition, parameter);
    }

    @Override
    public void run_instruction_logic(CPU currentCpu, int[] data) {
        switch (getAddressMode()) {
            case REGISTER_8_BIT:
                incrementRegister(currentCpu);
                break;
            case REGISTER_16_BIT:
                incrementRegisterPair(currentCpu);
                break;
            default:
                throw new IllegalArgumentException(String.format("Address mode %s not supported for instruction type %s", getAddressMode()));
        }
    }
    private int getIncrementedRegisterData(CPU currentCpu) {
        int originalValue = currentCpu.getValueFromRegister(getSourceRegister());
        return originalValue +1;
        
    }

    private void incrementRegister(CPU currentCpu) {
        int result = getIncrementedRegisterData(currentCpu) & 0xFF;
        currentCpu.setValueInRegister(result, getDestinationRegister());

        //based on the result, set the needed flags on the F register.
        currentCpu.setZero(result == 0);
        currentCpu.setSubtract(false);
        currentCpu.setHalfCarry((result & 0x000F) == 0);
    }
    
    private void incrementRegisterPair(CPU currentCpu) {
        int result = getIncrementedRegisterData(currentCpu) & 0xFFFF;
        currentCpu.setValueInRegister(result, getDestinationRegister());
    }
}
