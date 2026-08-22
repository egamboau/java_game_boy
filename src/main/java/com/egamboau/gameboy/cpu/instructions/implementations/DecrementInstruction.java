package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.RegisterType;
import com.egamboau.gameboy.memory.BitMasks;

/**
 * Instruction that decrements a register, register pair or memory location.
 *
 * The instruction updates Zero, Subtract and Half-Carry flags as appropriate
 * for 8-bit decrement operations. 16-bit decrements only affect the register pair value.
 */
public class DecrementInstruction extends Instruction {


    /**
     * Constructs a DecrementInstruction with the specified parameters.
     *
     * @param addressMode The addressing mode of the instruction.
     * @param sourceRegister The source register for the instruction.
     * @param destinationRegister The destination register for the instruction.
     */
    public DecrementInstruction(final AddressMode addressMode, final RegisterType sourceRegister,
            final RegisterType destinationRegister) {
        super(addressMode, sourceRegister, destinationRegister);
    }



    @Override
    public final void runInstructionLogic(final CPU currentCpu, final int[] data) {
        switch (getAddressMode()) {
            case REGISTER_8_BIT:
                decrementRegister(currentCpu);
                break;
            case REGISTER_16_BIT:
                decrementRegisterPair(currentCpu);
                break;
            case MEMORY_ADDRESS_REGISTER:
                decrementIndirectAddress(currentCpu);
                break;
            default:
                throw new IllegalArgumentException(String.format("Address mode %s not supported", getAddressMode()));
        }
    }

    @Override
    protected final int getInternalCycles(final CPU currentCpu) {
        return getAddressMode() == AddressMode.REGISTER_16_BIT ? 1 : 0;
    }



    private void decrementRegisterPair(final CPU currentCpu) {
        currentCpu.decrement16BitRegister(getDestinationRegister());
    }

    private void decrementRegister(final CPU currentCpu) {
        int result = getDecrementedRegisterData(currentCpu) & BitMasks.MASK_8_BIT_DATA;
        currentCpu.setValueInRegister(result, getDestinationRegister());
        currentCpu.setZero(result == 0);
        currentCpu.setSubtract(true);
        currentCpu.setHalfCarry((result & BitMasks.HALF_CARRY_8_BIT_RESULT_DECREMENT) == BitMasks.HALF_CARRY_8_BIT_RESULT_DECREMENT);
    }

    private int getDecrementedRegisterData(final CPU currentCpu) {
        int originalValue = currentCpu.getValueFromRegister(getSourceRegister());
        return originalValue - 1;
    }

    private void decrementIndirectAddress(final CPU currentCpu) {
        int memoryAddress = currentCpu.getValueFromRegister(getSourceRegister());
        int data = currentCpu.readByteFromAddress(memoryAddress);
        int result = data - 1;

        // based on the result, set the needed flags on the F register.
        currentCpu.setZero(result == 0);
        currentCpu.setSubtract(true);
        currentCpu.setHalfCarry((result & BitMasks.HALF_CARRY_8_BIT_RESULT_DECREMENT) == BitMasks.HALF_CARRY_8_BIT_RESULT_DECREMENT);

        currentCpu.writeByteToAddress(memoryAddress, result);
    }


}
