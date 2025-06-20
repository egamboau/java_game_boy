package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.InstructionCondition;
import com.egamboau.gameboy.cpu.instructions.RegisterType;
import com.egamboau.gameboy.memory.BitMasks;

public class IncrementInstruction extends Instruction {

    /**
     * Constructs an IncrementInstruction with the specified parameters.
     *
     * @param addressMode        The addressing mode of the instruction.
     * @param sourceRegister     The source register for the operation.
     * @param destinationRegister The destination register for the operation.
     * @param condition          The condition under which the instruction executes.
     * @param parameter          Additional parameter for the instruction.
     */
    public IncrementInstruction(final AddressMode addressMode, final RegisterType sourceRegister,
            final RegisterType destinationRegister,
            final InstructionCondition condition, final Byte parameter) {
        super(addressMode, sourceRegister, destinationRegister, condition, parameter);
    }

    @Override
    public final void runInstructionLogic(final CPU currentCpu, final int[] data) {
        switch (getAddressMode()) {
            case REGISTER_8_BIT:
                incrementRegister(currentCpu);
                break;
            case REGISTER_16_BIT:
                incrementRegisterPair(currentCpu);
                break;
            default:
                throw new IllegalArgumentException(
                        String.format("Address mode %s not supported for Increment Instruction", getAddressMode()));
        }
    }

    private int getIncrementedRegisterData(final CPU currentCpu) {
        int originalValue = currentCpu.getValueFromRegister(getSourceRegister());
        return originalValue + 1;

    }

    private void incrementRegister(final CPU currentCpu) {
        int result = getIncrementedRegisterData(currentCpu) & BitMasks.MASK_8_BIT_DATA;
        currentCpu.setValueInRegister(result, getDestinationRegister());

        // based on the result, set the needed flags on the F register.
        currentCpu.setZero(result == 0);
        currentCpu.setSubtract(false);
        currentCpu.setHalfCarry((result & BitMasks.HALF_CARRY_8_BIT_RESULT) == 0);
    }

    private void incrementRegisterPair(final CPU currentCpu) {
        int result = getIncrementedRegisterData(currentCpu) & BitMasks.MASK_16_BIT_DATA;
        currentCpu.setValueInRegister(result, getDestinationRegister());
    }
}
