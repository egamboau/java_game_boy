package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.InstructionCondition;
import com.egamboau.gameboy.cpu.instructions.RegisterType;


public class ConditionalReturnInstruction extends Instruction {

    private InstructionCondition instructionCondition;

    /**
     * Creates a conditional return instruction.
     *
     * @param currentAddressMode addressing mode
     * @param currentSourceRegister source register
     * @param currentDestinationRegister destination register
     */
    public ConditionalReturnInstruction(final AddressMode currentAddressMode,
            final RegisterType currentSourceRegister, final RegisterType currentDestinationRegister) {
        super(currentAddressMode, currentSourceRegister, currentDestinationRegister);
    }

    private boolean checkForCondition(final CPU currentCpu) {
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

    @Override
    protected void runInstructionLogic(final CPU currentCpu, final int[] data) {
        if (checkForCondition(currentCpu)) {
            /*If the Z flag is 0, control is returned to the source program by popping from the memory stack the program counter PC
            value that was pushed to the stack when the subroutine was called. */
            int newPcCount = currentCpu.popWord();
            currentCpu.setValueInRegister(newPcCount, RegisterType.PC);
        }
    }

    @Override
    protected final int getInternalCycles(final CPU currentCpu) {
        return checkForCondition(currentCpu) ? 2 : 1;
    }

    /**
     * Sets condition controlling return execution.
     *
     * @param condition instruction condition
     */
    public void setCondition(final InstructionCondition condition) {
        this.instructionCondition = condition;
    }

}
