package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.RegisterType;
import com.egamboau.gameboy.memory.BitMasks;

public class LoadHlFromStackPointerOffsetInstruction extends Instruction {

    /**
     * Creates an instruction that loads HL with SP plus a signed immediate value.
     *
     * @param currentAddressMode addressing mode
     * @param currentSourceRegister source register
     * @param currentDestinationRegister destination register
     */
    public LoadHlFromStackPointerOffsetInstruction(final AddressMode currentAddressMode,
            final RegisterType currentSourceRegister, final RegisterType currentDestinationRegister) {
        super(currentAddressMode, currentSourceRegister, currentDestinationRegister);
    }

    @Override
    protected final void runInstructionLogic(final CPU currentCpu, final int[] data) {
        int sp = currentCpu.getValueFromRegister(RegisterType.SP);
        int offset = (byte) data[0];
        int result = sp + offset;

        currentCpu.setValueInRegister(result, RegisterType.HL);
        currentCpu.setZero(false);
        currentCpu.setSubtract(false);
        currentCpu.setHalfCarry(((sp ^ offset ^ result) & BitMasks.SIGNED_8_BIT_HALF_CARRY) != 0);
        currentCpu.setCarry(((sp ^ offset ^ result) & BitMasks.SIGNED_8_BIT_CARRY) != 0);
    }

    @Override
    protected final int getInternalCycles(final CPU currentCpu) {
        return 1;
    }
}
