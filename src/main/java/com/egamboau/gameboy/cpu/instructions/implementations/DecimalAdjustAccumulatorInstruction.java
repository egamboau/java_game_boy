package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.InstructionCondition;
import com.egamboau.gameboy.cpu.instructions.RegisterType;

public class DecimalAdjustAccumulatorInstruction extends Instruction{

    public DecimalAdjustAccumulatorInstruction(AddressMode addressMode, RegisterType sourceRegister,
            RegisterType destinationRegister, InstructionCondition condition, Byte parameter) {
        super(addressMode, sourceRegister, destinationRegister, condition, parameter);
    }

    @Override
    protected void run_instruction_logic(CPU currentCpu, int[] data) {
        int adjustment = 0;
        int registerValue = currentCpu.getValueFromRegister(getDestinationRegister());
        if (currentCpu.getSubtract()) {
            
            if (currentCpu.getHalfCarry()) {
                adjustment += 0x06;
            }
            if (currentCpu.getCarry()) {
                adjustment += 0x60;
            }
            registerValue -=  (adjustment & 0xFF);
        } else {
            
            if (currentCpu.getHalfCarry() || (registerValue & 0xF) > 0x9 ) {
                adjustment += 0x06;
            }
            if (currentCpu.getCarry() || registerValue > 0x99) {
                adjustment += 0x60;
                currentCpu.setCarry(true);
            }
            registerValue += (adjustment & 0xFF); 
        }

        currentCpu.setZero((registerValue & 0xFF) == 0);
        currentCpu.setHalfCarry(false);
        currentCpu.setValueInRegister(registerValue, getDestinationRegister());
    }

}
