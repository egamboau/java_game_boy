package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.InstructionCondition;
import com.egamboau.gameboy.cpu.instructions.RegisterType;
import com.egamboau.gameboy.memory.BitMasks;

public class AddInstruction extends Instruction {

    /**
     * Constructs an AddInstruction with the specified parameters.
     *
     * @param addressMode The addressing mode of the instruction.
     * @param sourceRegister The source register for the operation.
     * @param destinationRegister The destination register for the operation.
     * @param condition The condition under which the instruction is executed.
     * @param parameter An additional parameter for the instruction.
     */
    public AddInstruction(final AddressMode addressMode, final RegisterType sourceRegister,
            final RegisterType destinationRegister,
            final InstructionCondition condition, final Byte parameter) {
        super(addressMode, sourceRegister, destinationRegister, condition, parameter);
    }

    @Override
    public final void runInstructionLogic(final CPU currentCpu, final int[] data) {
        switch (getAddressMode()) {
            case REGISTER_TO_REGISTER:
                this.addRegisters(currentCpu);
                break;
            case REGISTER_16_BIT_TO_REGISTER_16_BIT:
                this.addRegisterPairs(currentCpu);
                break;
            default:
                throw new IllegalArgumentException(
                        "Address mode not supported for ADD instruction: " + getAddressMode());
        }
    }

    private void addRegisterPairs(final CPU currentCpu) {
        int result = currentCpu.getValueFromRegister(getSourceRegister())
                + currentCpu.getValueFromRegister(getDestinationRegister());
        currentCpu.setValueInRegister(result, getDestinationRegister());
        currentCpu.setSubtract(false);
        currentCpu.setHalfCarry((result & BitMasks.HALF_CARRY_16_BIT_RESULT) != 0);
        currentCpu.setCarry(result > BitMasks.CARRY_16_BIT_RESULTS);
    }

    private void addRegisters(final CPU currentCpu) {
        int result = currentCpu.getValueFromRegister(getSourceRegister())
                + currentCpu.getValueFromRegister(getDestinationRegister());
        currentCpu.setValueInRegister(result, getDestinationRegister());
        currentCpu.setSubtract(false);
        currentCpu.setHalfCarry((result & BitMasks.HALF_CARRY_8_BIT_RESULT) != 0);
        currentCpu.setCarry(result > BitMasks.CARRY_8_BIT_RESULTS);
    }

}
