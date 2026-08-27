package com.egamboau.gameboy.cpu.instructions;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.implementations.AddInstruction;
import com.egamboau.gameboy.cpu.instructions.implementations.AddWithCarryInstruction;
import com.egamboau.gameboy.cpu.instructions.implementations.AndInstruction;
import com.egamboau.gameboy.cpu.instructions.implementations.CompareInstruction;
import com.egamboau.gameboy.cpu.instructions.implementations.ConditionalReturnInstruction;
import com.egamboau.gameboy.cpu.instructions.implementations.DecimalAdjustAccumulatorInstruction;
import com.egamboau.gameboy.cpu.instructions.implementations.DecrementInstruction;
import com.egamboau.gameboy.cpu.instructions.implementations.FlipCarryFlagInstruction;
import com.egamboau.gameboy.cpu.instructions.implementations.HaltInstruction;
import com.egamboau.gameboy.cpu.instructions.implementations.IncrementInstruction;
import com.egamboau.gameboy.cpu.instructions.implementations.JumpRelativeInstruction;
import com.egamboau.gameboy.cpu.instructions.implementations.LoadInstruction;
import com.egamboau.gameboy.cpu.instructions.implementations.NoopInstruction;
import com.egamboau.gameboy.cpu.instructions.implementations.OneComplementInstruction;
import com.egamboau.gameboy.cpu.instructions.implementations.OrInstruction;
import com.egamboau.gameboy.cpu.instructions.implementations.RotateLeftCircularInstruction;
import com.egamboau.gameboy.cpu.instructions.implementations.RotateLeftInstruction;
import com.egamboau.gameboy.cpu.instructions.implementations.RotateRightInstruction;
import com.egamboau.gameboy.cpu.instructions.implementations.RotateRigthCircularInstruction;
import com.egamboau.gameboy.cpu.instructions.implementations.SetCarryFlagInstruction;
import com.egamboau.gameboy.cpu.instructions.implementations.StopInstruction;
import com.egamboau.gameboy.cpu.instructions.implementations.SubInstruction;
import com.egamboau.gameboy.cpu.instructions.implementations.SubWithCarryInstruction;
import com.egamboau.gameboy.cpu.instructions.implementations.XorInstruction;

/**
 * Represents a single CPU instruction in the Game Boy emulator.
 *
 * <p>An Instruction encapsulates the addressing mode, the source and destination
 * registers (when applicable), an optional condition used for conditional
 * instructions, and an optional immediate parameter. Subclasses implement the
 * concrete behaviour by overriding {@link #runInstructionLogic(CPU, int[])}.</p>
 *
 * <p>Opcode decoding is provided via the static factory
 * {@link #geInstructionFromOpcode(int)} which returns a concrete Instruction
 * instance approximating the behaviour implied by the opcode bits.</p>
 */
public abstract class Instruction {

    /**
     * The addressing mode used by the instruction.
     */
    private AddressMode addressMode = null;

    /**
     * The source register involved in the instruction.
     */
    private RegisterType sourceRegister = null;

    /**
     * The destination register involved in the instruction.
     */
    private RegisterType destinationRegister = null;


    /**
     * Constructs an Instruction with the specified properties.
     *
     * @param currentAddressMode  the addressing mode for this instruction, may be null for some placeholders
     * @param currentSourceRegister the source register used by the instruction, may be null
     * @param currentDestinationRegister the destination register used by the instruction, may be null
     */
    protected Instruction(final AddressMode currentAddressMode, final RegisterType currentSourceRegister,
            final RegisterType currentDestinationRegister) {
        this.addressMode = currentAddressMode;
        this.sourceRegister = currentSourceRegister;
        this.destinationRegister = currentDestinationRegister;
    }

    /**
     * Returns the addressing mode for this instruction.
     *
     * @return the addressing mode, or null for placeholder/no-mode instructions
     */
    public final AddressMode getAddressMode() {
        return addressMode;
    }

    /**
     * Returns the source register of this instruction.
     *
     * The source register identifies the register that provides the operand consumed by
     * the instruction. For instructions that do not use a register as their source
     * operand, this method may return {@code null}.
     *
     * @return the source {@code RegisterType} used by the instruction, or {@code null}
     *         if the instruction has no source register
     */
    public final RegisterType getSourceRegister() {
        return sourceRegister;
    }


    /**
     * Returns the destination register for this instruction.
     *
     * The destination register is the register that will be written to when the
     * instruction is executed. For instructions that do not write to a register,
     * this method may return {@code null}.
     *
     * @return the destination {@link RegisterType}, or {@code null} if the instruction
     *         has no destination register
     */
    public final RegisterType getDestinationRegister() {
        return destinationRegister;
    }


    /**
     * Fetches any immediate or memory bytes required by this instruction before execution.
     *
     * <p>The number and meaning of fetched bytes depend on the addressing mode:
     * <ul>
     *   <li>DATA_8_BIT_* modes fetch a single byte from memory (PC increments by 1)</li>
     *   <li>DATA_16_BITS_TO_REGISTER and REGISTER_TO_MEMORY_ADDRESS_DATA fetch two bytes (little-endian)</li>
     *   <li>register-based addressing modes require no fetch and return an empty array</li>
     * </ul>
     * </p>
     *
     * @param currentCpu the CPU instance used to read bytes from memory/PC
     * @return an int array containing fetched bytes (each 0..255). Length is 0, 1 or 2 depending on addressing mode.
     * @throws IllegalArgumentException if the addressing mode is not supported
     */
    private int[] fetchData(final CPU currentCpu) {
        if (getAddressMode() == null) {
            return new int[0];
        } else {
            switch (getAddressMode()) {
                case REGISTER_TO_MEMORY_ADDRESS_DATA, DATA_16_BITS_TO_REGISTER:
                    // Read 2 bytes from memory.
                    int firstByte = currentCpu.getDataFromPCAndIncrement();
                    int secondByte = currentCpu.getDataFromPCAndIncrement();
                    return new int[] {firstByte, secondByte };
                case DATA_8_BIT_TO_REGISTER, DATA_8_BIT_TO_MEMORY_ADDRESS_REGISTER:
                    int data = currentCpu.getDataFromPCAndIncrement();
                    return new int[] {data };
                case REGISTER_8_BIT, REGISTER_TO_REGISTER, REGISTER_16_BIT, REGISTER_16_BIT_TO_REGISTER_16_BIT,
                        REGISTER_TO_INDIRECT_REGISTER,
                        MEMORY_ADDRESS_REGISTER_TO_REGISTER, REGISTER_TO_INCREMENT_16_BIT_MEMORY_ADDRESS,
                        INCREMENT_16_BIT_MEMORY_ADDRESS_REGISTER_TO_REGISTER,
                        REGISTER_TO_DECREMENT_16_BIT_MEMORY_ADDRESS, MEMORY_ADDRESS_REGISTER, DECREMENT_16_BIT_MEMORY_ADDRESS_REGISTER_TO_REGISTER:
                    // Data is on the register itself, so no data to fetch.
                    return new int[0];
                default:
                    throw new IllegalArgumentException("Address mode not supported: " + getAddressMode());
            }
        }
    }

    /**
     * Performs the full fetch-and-execute sequence for this instruction on the supplied CPU.
     *
     * <p>This method will fetch any required immediate bytes (via {@link #fetchData(CPU)})
     * then call {@link #runInstructionLogic(CPU, int[])} to perform the instruction.</p>
     *
     * @param currentCpu the CPU instance on which to execute the instruction
     * @return internal cycles consumed beyond memory accesses
     */
    public final int executeInstruction(final CPU currentCpu) {
        int[] data = fetchData(currentCpu);
        int internalCycles = getInternalCycles(currentCpu);
        runInstructionLogic(currentCpu, data);
        return internalCycles;
    }

    /**
     * Returns cycles consumed internally, excluding memory accesses.
     *
     * @param currentCpu CPU state before instruction logic runs
     * @return internal cycle count
     */
    protected int getInternalCycles(final CPU currentCpu) {
        return 0;
    }

    /**
     * Execute the concrete instruction behaviour.
     *
     * <p>Subclasses must override this method to implement the instruction's semantics.
     * The {@code data} parameter contains any bytes previously fetched by
     * {@link #fetchData(CPU)} and its meaning depends on the addressing mode.</p>
     *
     * @param currentCpu the CPU on which the instruction operates
     * @param data any immediate bytes or operands fetched prior to execution (0, 1 or 2 elements)
     */
    protected abstract void runInstructionLogic(CPU currentCpu, int[] data);

    /**
     * Decode a single opcode byte into a concrete Instruction instance.
     *
     * <p>The decoder interprets the opcode bitfields (x, y, z, p, q) and maps them
     * to the appropriate Instruction subclass. Not all opcodes are implemented;
     * unhandled opcodes produce IllegalArgumentException.</p>
     *
     * @param opcode the opcode byte (0..255) to decode
     * @return a new Instruction instance representing the decoded opcode
     * @throws IllegalArgumentException if the opcode is unimplemented or invalid
     */
    @SuppressWarnings("checkstyle:magicnumber")
    public static Instruction geInstructionFromOpcode(final int opcode) {

         int x = (opcode & 0300) >> 6;
         int y = (opcode & 070) >> 3;
         int z = (opcode & 07);
         int p = (opcode & 60) >> 4;
         int q = (opcode & 10) >> 3;

          switch (x) {
            case 0:
                switch (z) {
                    case 0:
                        return generateJumpAndAssortedInstructions(y);
                    case 1:
                        return generate16BitInmediateLoadInstruction(opcode, p, q);
                    case 2:
                        return generateIndirectLoadInstruction(p, q);
                    case 3:
                        return generate16BitIncrementAndDecrement(p, q);
                    case 4:
                        // 8-bit INC
                        RegisterType incrementRegister = RegisterType.getRegister(y);
                        if (incrementRegister == RegisterType.HL) {
                            //special case, this is an indirect register
                            return new IncrementInstruction(AddressMode.MEMORY_ADDRESS_REGISTER, incrementRegister, incrementRegister);
                        }
                        return new IncrementInstruction(AddressMode.REGISTER_8_BIT, incrementRegister, incrementRegister);
                    case 5:
                        // 8-bit DEC
                        RegisterType decrementRegister = RegisterType.getRegister(y);
                        if (decrementRegister == RegisterType.HL) {
                            //special case, this is an indirect register
                            return new DecrementInstruction(AddressMode.MEMORY_ADDRESS_REGISTER, decrementRegister, decrementRegister);
                        }
                        return new DecrementInstruction(AddressMode.REGISTER_8_BIT, decrementRegister, decrementRegister);
                    case 6:
                        // 8-bit load immediate
                        RegisterType loadRegister = RegisterType.getRegister(y);
                        if (loadRegister == RegisterType.HL) {
                            //special case, this is an indirect register
                            return new LoadInstruction(AddressMode.DATA_8_BIT_TO_MEMORY_ADDRESS_REGISTER, null, loadRegister);
                        }
                        return new LoadInstruction(AddressMode.DATA_8_BIT_TO_REGISTER, null, loadRegister);
                    case 7:
                        return generateFlagAndAccumulatorOperations(y);
                    default:
                        break;
                }
                break;
            case 1:
                if (z == 6 && y == 6) {
                    return new HaltInstruction();
                } else {
                    RegisterType sourceRegister = RegisterType.getRegister(z);
                    RegisterType destinationRegister = RegisterType.getRegister(y);
                    AddressMode addressMode = null;
                    if (destinationRegister.equals(RegisterType.HL)) {
                        addressMode = AddressMode.REGISTER_TO_INDIRECT_REGISTER;
                    } else if (sourceRegister.equals(RegisterType.HL)) {
                        addressMode = AddressMode.MEMORY_ADDRESS_REGISTER_TO_REGISTER;
                    } else {
                        addressMode = AddressMode.REGISTER_TO_REGISTER;
                    }
                    Instruction result = new LoadInstruction(addressMode, sourceRegister, destinationRegister);
                    return result;
                }
            case 2:
                return generateOperationOnAccumulatorAndRegisterOrMemory(y, z);
            case 3:
                if (z == 0) {
                    if (y >= 0 && y <= 3) {
                        ConditionalReturnInstruction instruction =  new ConditionalReturnInstruction(null, null, null);
                        instruction.setCondition(InstructionCondition.getInstructionConditionFromIndex(y));
                        return instruction;
                    }
                } else if (z == 1) {
                    if (q == 1) {
                        if (p == 0) {
                            return new ConditionalReturnInstruction(null, null, null);
                        }
                    }
                }
            default:
                throw new IllegalArgumentException(String.format("\"Opcode still not implemented: \": %02x", opcode));
         }

         throw new IllegalArgumentException(String.format("\"Opcode still not implemented: \": %02x", opcode));
    }

    /**
     * Helper used by the opcode decoder to create ALU operations between the A register and a register/memory.
     *
     * @param y the y bitfield of the opcode (selects ALU op)
     * @param z the z bitfield of the opcode (selects register)
     * @return an Instruction implementing the ALU operation
     * @throws IllegalArgumentException if the selected ALU operation is not implemented
     */
    private static Instruction generateOperationOnAccumulatorAndRegisterOrMemory(final int y, final int z) {
        AluOperationType operationType = AluOperationType.getAluOperationType(y);
        RegisterType register = RegisterType.getRegister(z);
        AddressMode addressMode = register.equals(RegisterType.HL)
            ? AddressMode.MEMORY_ADDRESS_REGISTER_TO_REGISTER
            : AddressMode.REGISTER_TO_REGISTER;
        switch (operationType) {
            case ADD_A:
                return new AddInstruction(addressMode, register, RegisterType.A);
            case ADC_A:
                return new AddWithCarryInstruction(addressMode, register, RegisterType.A);
            case SUB:
                return new SubInstruction(addressMode, register, RegisterType.A);
            case SBC_A:
                return new SubWithCarryInstruction(addressMode, register, RegisterType.A);
            case AND:
                return new AndInstruction(addressMode, register, RegisterType.A);
            case XOR:
                return new XorInstruction(addressMode, register, RegisterType.A);
            case OR:
                return new OrInstruction(addressMode, register, RegisterType.A);
            case CP:
                return new CompareInstruction(addressMode, register, RegisterType.A);
            default:
                throw new IllegalArgumentException(String.format("\"Opcode still not implemented: \": %s", operationType));
        }
    }

    /**
     * Produces instruction instances for the "flags and accumulator" opcode group (y = 0..7 for z=7 group).
     *
     * @param y the sub-opcode selecting the specific flag/rotate instruction
     * @return a concrete Instruction implementing the selected behaviour
     */
    @SuppressWarnings("checkstyle:magicnumber")
    private static Instruction generateFlagAndAccumulatorOperations(final int y) {
        switch (y) {
            case 0:
                return new RotateLeftCircularInstruction(AddressMode.REGISTER_8_BIT, RegisterType.A, RegisterType.A);
            case 1:
                return new RotateRigthCircularInstruction(AddressMode.REGISTER_8_BIT, RegisterType.A, RegisterType.A);
            case 2:
                return new RotateLeftInstruction(AddressMode.REGISTER_8_BIT, RegisterType.A, RegisterType.A);
            case 3:
                return new RotateRightInstruction(AddressMode.REGISTER_8_BIT, RegisterType.A, RegisterType.A);
            case 4:
                return new DecimalAdjustAccumulatorInstruction(AddressMode.REGISTER_8_BIT, RegisterType.A, RegisterType.A);
            case 5:
                return new OneComplementInstruction(AddressMode.REGISTER_8_BIT, RegisterType.A, RegisterType.A);
            case 6:
                return new SetCarryFlagInstruction(AddressMode.REGISTER_8_BIT, RegisterType.A, RegisterType.A);
            case 7:
                return new FlipCarryFlagInstruction(AddressMode.REGISTER_8_BIT, RegisterType.A, RegisterType.A);
            default:
                return new StopInstruction(AddressMode.REGISTER_8_BIT, RegisterType.A, RegisterType.A);
        }
    }

    /**
     * Helper that decodes the indirect load / store instruction forms based on p and q.
     *
     * @param p opcode p field (bits 4-5)
     * @param q opcode q field (bit 3)
     * @return a LoadInstruction (or StopInstruction) representing the indirect form
     */
    @SuppressWarnings("checkstyle:magicnumber")
    private static Instruction generateIndirectLoadInstruction(final int p, final int q) {
        RegisterType register = switch (p) {
            case 0 -> RegisterType.BC;
            case 1 -> RegisterType.DE;
            default -> RegisterType.HL;
        };
        if (q == 0) {
            AddressMode mode = switch (p) {
                case 0, 1 -> AddressMode.REGISTER_TO_INDIRECT_REGISTER;
                case 2 -> AddressMode.REGISTER_TO_INCREMENT_16_BIT_MEMORY_ADDRESS;
                default -> AddressMode.REGISTER_TO_DECREMENT_16_BIT_MEMORY_ADDRESS;
            };
            return new LoadInstruction(mode, RegisterType.A, register);
        }
        AddressMode mode = switch (p) {
            case 0, 1 -> AddressMode.MEMORY_ADDRESS_REGISTER_TO_REGISTER;
            case 2 -> AddressMode.INCREMENT_16_BIT_MEMORY_ADDRESS_REGISTER_TO_REGISTER;
            default -> AddressMode.DECREMENT_16_BIT_MEMORY_ADDRESS_REGISTER_TO_REGISTER;
        };
        return new LoadInstruction(mode, register, RegisterType.A);
    }

    /**
     * Helper for 16-bit immediate loads and add-to-HL forms (group 01 with z==1).
     *
     * @param opcode original opcode byte (used only in error messages)
     * @param p the opcode p field selecting register pair
     * @param q the opcode q field selecting load or add
     * @return a LoadInstruction or AddInstruction representing the decoded opcode
     */
    private static Instruction generate16BitInmediateLoadInstruction(final int opcode, final int p, final int q) {
        // 16-bit load immediate/add
        switch (q) {
            case 0:
                return new LoadInstruction(
                    AddressMode.DATA_16_BITS_TO_REGISTER,
                    null,
                    RegisterType.getRegisterPairFeaturingSP(p));
            case 1:
                return new AddInstruction(
                    AddressMode.REGISTER_16_BIT_TO_REGISTER_16_BIT,
                    RegisterType.getRegisterPairFeaturingSP(p),
                    RegisterType.HL);
            default:
                throw new IllegalArgumentException("Invalid opcode for instruction " + opcode);
        }
    }

    /**
     * Generate the instructions used by the x=0, z=0 group (jumps, no-ops, load SP immediate, etc).
     *
     * @param y the opcode y field selecting the exact instruction/condition
     * @return an Instruction instance for the group
     */
    @SuppressWarnings("checkstyle:magicnumber")
    private static Instruction generateJumpAndAssortedInstructions(final int y) {
        // relative jumps and assorted instructions
        switch (y) {
            case 0:
                return new NoopInstruction(null, null, null);
            case 1:
                return new LoadInstruction(AddressMode.REGISTER_TO_MEMORY_ADDRESS_DATA, RegisterType.SP, null);
            case 2:
                return new StopInstruction(null, null, null);
            case 3:
                return new JumpRelativeInstruction(
                    AddressMode.DATA_8_BIT_TO_REGISTER,
                    null,
                    RegisterType.PC,
                    null);
            default:
                return new JumpRelativeInstruction(
                    AddressMode.DATA_8_BIT_TO_REGISTER, null,
                    null,
                    InstructionCondition.getInstructionConditionFromIndex(y - 4));
        }
    }

    /**
     * Create 16-bit increment / decrement instructions for the corresponding opcode group.
     *
     * @param p opcode p field selecting which 16-bit register pair
     * @param q opcode q field selecting INC (0) or DEC (1)
     * @return an IncrementInstruction, DecrementInstruction, or StopInstruction when invalid
     */
    private static Instruction generate16BitIncrementAndDecrement(final int p, final int q) {
        // 16-bit INC/DEC
        RegisterType register = RegisterType.getRegisterPairFeaturingSP(p);
        switch (q) {
            case 0:
                return new IncrementInstruction(AddressMode.REGISTER_16_BIT, register, register);
            case 1:
                return new DecrementInstruction(AddressMode.REGISTER_16_BIT, register, register);
            default:
                return new StopInstruction(null, null, null);
        }
    }
}
