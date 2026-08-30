package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.RegisterType;
import com.egamboau.gameboy.memory.BitMasks;

/**
 * Increment instruction implementation.
 *
 * <p>Supports incrementing 8-bit registers, 16-bit register pairs and memory
 * locations referenced indirectly. Updates CPU flags according to the result
 * for 8-bit operations.</p>
 */
public class IncrementInstruction extends Instruction {

    /**
     * Constructs an IncrementInstruction with the specified parameters.
     *
     * @param addressMode        The addressing mode of the instruction.
     * @param sourceRegister     The source register for the operation.
     * @param destinationRegister The destination register for the operation.
     */
    public IncrementInstruction(final AddressMode addressMode, final RegisterType sourceRegister,
            final RegisterType destinationRegister) {
        super(addressMode, sourceRegister, destinationRegister);
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
            case MEMORY_ADDRESS_REGISTER:
                incrementIndirectAddress(currentCpu);
                break;
            default:
                throw new IllegalArgumentException(
                        String.format("Address mode %s not supported for Increment Instruction", getAddressMode()));
        }
    }

    @Override
    protected final int getInternalCycles(final CPU currentCpu) {
        return getAddressMode() == AddressMode.REGISTER_16_BIT ? 1 : 0;
    }

    private int getIncrementedRegisterData(final CPU currentCpu) {
        int originalValue = currentCpu.getValueFromRegister(getSourceRegister());
        return originalValue + 1;

    }

    private void incrementRegister(final CPU currentCpu) {
        int result = getIncrementedRegisterData(currentCpu) & BitMasks.MASK_8_BIT_DATA;
        currentCpu.setValueInRegister(result, getDestinationRegister());

        // based on the result, set the needed flags on the F register.
        setFlags(currentCpu, result);
    }

    private void incrementRegisterPair(final CPU currentCpu) {
        currentCpu.increment16BitRegister(getDestinationRegister());
    }

    private void incrementIndirectAddress(final CPU currentCpu) {
        int memoryAddress = currentCpu.getValueFromRegister(getSourceRegister());
        int data = currentCpu.readByteFromAddress(memoryAddress);
        int result = (data + 1) & BitMasks.MASK_8_BIT_DATA;

        // based on the result, set the needed flags on the F register.
        setFlags(currentCpu, result);

        currentCpu.writeByteToAddress(memoryAddress, result);
    }

    private void setFlags(final CPU currentCpu, final int result) {
        currentCpu.setZero(result == 0);
        currentCpu.setSubtract(false);
        currentCpu.setHalfCarry((result & BitMasks.HALF_CARRY_8_BIT_RESULT) == 0);
    }

}
