package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.InstructionCondition;
import com.egamboau.gameboy.cpu.instructions.RegisterType;

public class JumpRelativeInstruction extends Instruction{

    public JumpRelativeInstruction(AddressMode addressMode, RegisterType sourceRegister,
            RegisterType destinationRegister, InstructionCondition condition, Byte parameter) {
        super(addressMode, sourceRegister, destinationRegister, condition, parameter);
    }

    @Override
    protected void run_instruction_logic(CPU currentCpu, int[] data) {
        //get the data from the array. Cast to byte to get the sign correctly
        byte address = (byte)data[0];
        boolean shouldJump;
        if(this.getCondition() == null){
            shouldJump = true;
        } else {
            switch (getCondition()) {
                case Z_FLAG_NOT_SET:
                    shouldJump = !currentCpu.getZero();
                    break;
                case Z_FLAG_SET:
                    shouldJump = currentCpu.getZero();
                    break;
                default:
                    throw new IllegalArgumentException(String.format("Condition not supported for jump: %s", getCondition()));
            }
        }
        if (shouldJump) {
            currentCpu.incrementPCRegister(address);
        }
    }
    

}
