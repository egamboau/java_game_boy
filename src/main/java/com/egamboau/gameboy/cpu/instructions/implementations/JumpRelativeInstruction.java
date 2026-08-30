package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.InstructionCondition;
import com.egamboau.gameboy.cpu.instructions.RegisterType;

/**
 * Jump (relative) instruction implementation.
 *
 * <p>Performs a signed relative branch of the program counter optionally
 * conditioned on CPU flags.</p>
 */
public class JumpRelativeInstruction extends Instruction {

    /** Optional condition controlling whether the relative jump is taken. */
    private final InstructionCondition condition;

    /**
     * Constructs a JumpRelativeInstruction.
     *
     * @param addressMode The addressing mode of the instruction.
     * @param sourceRegister The source register for the instruction.
     * @param destinationRegister The destination register for the instruction.
     * @param currentCondition The condition under which the jump occurs, or null for an unconditional jump.
     */
    public JumpRelativeInstruction(final AddressMode addressMode, final RegisterType sourceRegister,
            final RegisterType destinationRegister, final InstructionCondition currentCondition) {
        super(addressMode, sourceRegister, destinationRegister);
        this.condition = currentCondition;
    }

    @Override
    protected final void runInstructionLogic(final CPU currentCpu, final int[] data) {
        //get the data from the array. Cast to byte to get the sign correctly
        byte address = (byte) data[0];
        boolean shouldJump;
        if (condition == null) {
            shouldJump = true;
        } else {
            shouldJump = switch (condition) {
                case Z_FLAG_NOT_SET -> !currentCpu.getZero();
                case Z_FLAG_SET -> currentCpu.getZero();
                case CARRY_FLAG_NOT_SET -> !currentCpu.getCarry();
                case CARRY_FLAG_SET -> currentCpu.getCarry();
            };
        }
        if (shouldJump) {
            currentCpu.incrementPCRegister(address);
        }
    }

}
