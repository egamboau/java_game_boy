package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.InstructionCondition;
import com.egamboau.gameboy.cpu.instructions.RegisterType;

public class JumpRelativeInstruction extends Instruction {

    /**
     * Constructs a JumpRelativeInstruction.
     *
     * @param addressMode The addressing mode of the instruction.
     * @param sourceRegister The source register for the instruction.
     * @param destinationRegister The destination register for the instruction.
     * @param condition The condition under which the jump occurs.
     * @param parameter The parameter for the instruction.
     */
    public JumpRelativeInstruction(final AddressMode addressMode, final RegisterType sourceRegister,
            final RegisterType destinationRegister, final InstructionCondition condition, final Byte parameter) {
        super(addressMode, sourceRegister, destinationRegister, condition, parameter);
    }

    @Override
    protected final void runInstructionLogic(final CPU currentCpu, final int[] data) {
        //get the data from the array. Cast to byte to get the sign correctly
        byte address = (byte) data[0];
        boolean shouldJump;
        if (this.getCondition() == null) {
            shouldJump = true;
        } else {
            switch (getCondition()) {
                case Z_FLAG_NOT_SET:
                    shouldJump = !currentCpu.getZero();
                    break;
                case Z_FLAG_SET:
                    shouldJump = currentCpu.getZero();
                    break;
                case CARRY_FLAG_NOT_SET:
                    shouldJump = !currentCpu.getCarry();
                    break;
                default:
                    throw new IllegalArgumentException(String.format("Condition not supported for jump: %s", getCondition()));
            }
        }
        if (shouldJump) {
            currentCpu.incrementPCRegister(address);
        }
    }
}
