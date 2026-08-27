package com.egamboau.gameboy.cpu;

import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.InstructionCondition;
import com.egamboau.gameboy.cpu.instructions.RegisterType;

public abstract class ConditionalInstruction extends Instruction {

    private InstructionCondition instructionCondition;


    protected ConditionalInstruction(final AddressMode currentAddressMode, final RegisterType currentSourceRegister,
                            final RegisterType currentDestinationRegister) {
        super(currentAddressMode, currentSourceRegister, currentDestinationRegister);
    }

    protected boolean checkForCondition(final CPU currentCpu) {
        if (instructionCondition == null) {
            return true;
        }
        switch (instructionCondition) {
            case Z_FLAG_NOT_SET:
                return !currentCpu.getZero();
            case Z_FLAG_SET:
                return currentCpu.getZero();
            case CARRY_FLAG_NOT_SET:
                return !currentCpu.getCarry();
            case CARRY_FLAG_SET:
                return currentCpu.getCarry();
            default:
                throw new IllegalArgumentException("Invalid Condition " + instructionCondition);
        }
    }

     /**
     * Sets condition controlling return execution.
     *
     * @param condition instruction condition
     */
    public void setCondition(final InstructionCondition condition) {
        this.instructionCondition = condition;
    }

    protected InstructionCondition getCondition() {
        return this.instructionCondition;
    }

    @Override
    protected
    abstract void runInstructionLogic(CPU currentCpu, int[] data);

}
