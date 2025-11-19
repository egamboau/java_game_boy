package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.InstructionCondition;
import com.egamboau.gameboy.cpu.instructions.RegisterType;

public class FlipCarryFlagInstruction extends Instruction {

    /**
     * Creates a FlipCarryFlagInstruction configured with the supplied addressing mode, registers,
     * execution condition, and optional parameter.
     *
     * <p>This instruction represents an operation that toggles (flips) the CPU carry flag when
     * executed. The constructor simply records the configuration needed by the instruction
     * implementation (addressing mode, source and destination registers, conditional execution
     * information, and an optional byte parameter) so the execution logic can use them at runtime.
     *
     * @param currentAddressMode the addressing mode used by this instruction (may be null if not applicable)
     * @param currentSourceRegister the source register for this instruction (may be null if not applicable)
     * @param currentDestinationRegister the destination register for this instruction (may be null if not applicable)
     * @param currentCondition the execution condition for this instruction (may be null for unconditional execution)
     * @param currentParameter an optional byte parameter (for example an immediate value or offset); may be null if unused
     */
    public FlipCarryFlagInstruction(final AddressMode currentAddressMode, final RegisterType currentSourceRegister,
            final RegisterType currentDestinationRegister, final InstructionCondition currentCondition,
            final Byte currentParameter) {
        super(currentAddressMode, currentSourceRegister, currentDestinationRegister, currentCondition, currentParameter);
    }

    @Override
    protected final void runInstructionLogic(final CPU currentCpu, final int[] data) {
        currentCpu.setCarry(!currentCpu.getCarry());
        currentCpu.setSubtract(false);
        currentCpu.setHalfCarry(false);
    }

}
