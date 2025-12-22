package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.InstructionCondition;
import com.egamboau.gameboy.cpu.instructions.RegisterType;

/**
 * Instruction that flips (toggles) the CPU carry flag.
 *
 * The instruction clears the Subtract and Half-Carry flags after flipping the Carry flag.
 */
public class FlipCarryFlagInstruction extends Instruction {

    //region Constructors

    /**
     * Creates a FlipCarryFlagInstruction configured with the supplied addressing mode, registers,
     * execution condition, and optional parameter.
     *
     * @param currentAddressMode the addressing mode used by this instruction (may be null if not applicable)
     * @param currentSourceRegister the source register for this instruction (may be null if not applicable)
     * @param currentDestinationRegister the destination register for this instruction (may be null if not applicable)
     * @param currentCondition the execution condition for this instruction (may be null for unconditional execution)
     * @param currentParameter an optional byte parameter; may be null if unused
     */
    public FlipCarryFlagInstruction(final AddressMode currentAddressMode, final RegisterType currentSourceRegister,
            final RegisterType currentDestinationRegister, final InstructionCondition currentCondition,
            final Byte currentParameter) {
        super(currentAddressMode, currentSourceRegister, currentDestinationRegister, currentCondition, currentParameter);
    }

    //endregion

    //region Instruction execution

    @Override
    protected final void runInstructionLogic(final CPU currentCpu, final int[] data) {
        currentCpu.setCarry(!currentCpu.getCarry());
        currentCpu.setSubtract(false);
        currentCpu.setHalfCarry(false);
    }

    //endregion

}
