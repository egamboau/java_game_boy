package com.egamboau.gameboy.cpu.instructions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.implementations.AddInstruction;
import com.egamboau.gameboy.cpu.instructions.implementations.DecimalAdjustAccumulatorInstruction;
import com.egamboau.gameboy.cpu.instructions.implementations.DecrementInstruction;
import com.egamboau.gameboy.cpu.instructions.implementations.IncrementInstruction;
import com.egamboau.gameboy.cpu.instructions.implementations.JumpRelativeInstruction;
import com.egamboau.gameboy.cpu.instructions.implementations.LoadInstruction;
import com.egamboau.gameboy.cpu.instructions.implementations.NoopInstruction;
import com.egamboau.gameboy.cpu.instructions.implementations.OneComplementInstruction;
import com.egamboau.gameboy.cpu.instructions.implementations.RotateLeftCircularInstruction;
import com.egamboau.gameboy.cpu.instructions.implementations.RotateLeftInstruction;
import com.egamboau.gameboy.cpu.instructions.implementations.RotateRightInstruction;
import com.egamboau.gameboy.cpu.instructions.implementations.RotateRigthCircularInstruction;
import com.egamboau.gameboy.cpu.instructions.implementations.SetCarryFlagInstruction;
import com.egamboau.gameboy.cpu.instructions.implementations.StopInstruction;

/**
 * Abstract class representing a CPU instruction in the Game Boy emulator.
 * Each instruction has an addressing mode, source and destination registers,
 * an optional condition for execution, and an optional parameter.
 * Subclasses must implement the logic for executing the instruction.
 */
public abstract class Instruction {

    /**
     * Logger instance for logging messages related to the Instruction class.
     */
    private static final Logger LOGGER = LogManager.getLogger();

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
     * The condition under which the instruction is executed.
     */
    private InstructionCondition condition = null;

    /**
     * The parameter associated with the instruction.
     */
    private Byte parameter = 0;

    /**
     * Constructs an Instruction with the specified properties.
     *
     * @param currentAddressMode The addressing mode used by the instruction.
     * @param currentSourceRegister The source register involved in the instruction.
     * @param currentDestinationRegister The destination register involved in the instruction.
     * @param currentCondition The condition under which the instruction is executed.
     * @param currentParamter The parameter associated with the instruction.
     */
    protected Instruction(final AddressMode currentAddressMode, final RegisterType currentSourceRegister,
            final RegisterType currentDestinationRegister, final InstructionCondition currentCondition, final Byte currentParamter) {
        this.addressMode = currentAddressMode;
        this.sourceRegister = currentSourceRegister;
        this.destinationRegister = currentDestinationRegister;
        this.condition = currentCondition;
        this.parameter = currentParamter;
    }

    /**
     * Retrieves the addressing mode used by the instruction.
     *
     * @return The addressing mode.
     */
    public final AddressMode getAddressMode() {
        return addressMode;
    }

    /**
     * Retrieves the source register involved in the instruction.
     *
     * @return The source register.
     */
    public final RegisterType getSourceRegister() {
        return sourceRegister;
    }

    /**
     * Retrieves the destination register involved in the instruction.
     *
     * @return The destination register.
     */
    public final RegisterType getDestinationRegister() {
        return destinationRegister;
    }

    /**
     * Retrieves the condition under which the instruction is executed.
     *
     * @return The execution condition.
     */
    public final InstructionCondition getCondition() {
        return condition;
    }

    /**
     * Retrieves the parameter associated with the instruction.
     *
     * @return The parameter.
     */
    public final Byte getParameter() {
        return parameter;
    }

    /**
     * Fetches the data required for the instruction based on its addressing mode.
     *
     * @param currentCpu The CPU instance from which data is fetched.
     * @return An array of integers representing the fetched data.
     * @throws IllegalArgumentException If the addressing mode is not supported.
     */
    private int[] fetchData(final CPU currentCpu) {
        LOGGER.info("Fetching data needed for instruction {}", this);
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
                        REGISTER_TO_DECREMENT_16_BIT_MEMORY_ADDRESS, MEMORY_ADDRESS_REGISTER:
                    // Data is on the register itself, so no data to fetch.
                    return new int[0];
                default:
                    throw new IllegalArgumentException("Address mode not supported: " + getAddressMode());
            }
        }
    }

    /**
     * Executes the instruction on the provided CPU instance.
     *
     * @param currentCpu The CPU instance on which the instruction is executed.
     */
    public final void executeInstruction(final CPU currentCpu) {
        int[] data = fetchData(currentCpu);
        runInstructionLogic(currentCpu, data);
    }

    /**
     * Abstract method to define the logic for executing the instruction.
     * Subclasses must implement this method to provide specific behavior.
     *
     * @param currentCpu The CPU instance on which the instruction is executed.
     * @param data The data fetched for the instruction.
     */
    protected abstract void runInstructionLogic(CPU currentCpu, int[] data);

    /**
     * Decodes the opcode and returns the corresponding Instruction instance.
     *
     * @param opcode The opcode to decode.
     * @return The corresponding Instruction, or null if not recognized.
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
                            return new IncrementInstruction(AddressMode.MEMORY_ADDRESS_REGISTER, incrementRegister, incrementRegister, null, null);
                        }
                        return new IncrementInstruction(AddressMode.REGISTER_8_BIT, incrementRegister, incrementRegister, null, null);
                    case 5:
                        // 8-bit DEC
                        RegisterType decrementRegister = RegisterType.getRegister(y);
                        if (decrementRegister == RegisterType.HL) {
                            //special case, this is an indirect register
                            return new DecrementInstruction(AddressMode.MEMORY_ADDRESS_REGISTER, decrementRegister, decrementRegister, null, null);
                        }
                        return new DecrementInstruction(AddressMode.REGISTER_8_BIT, decrementRegister, decrementRegister, null, null);
                    case 6:
                        // 8-bit load immediate
                        RegisterType loadRegister = RegisterType.getRegister(y);
                        if (loadRegister == RegisterType.HL) {
                            //special case, this is an indirect register
                            return new LoadInstruction(AddressMode.DATA_8_BIT_TO_MEMORY_ADDRESS_REGISTER, null, loadRegister, null, null);
                        }
                        return new LoadInstruction(AddressMode.DATA_8_BIT_TO_REGISTER, null, loadRegister, null, null);
                    case 7:
                        return generateFlagAndAccumulatorOperations(y);
                    default:
                        break;
                }
                break;
            case 1:
                if (z == 6 && y == 6) {
                    throw new IllegalArgumentException(String.format("\"Opcode still not implemented: \": %02x", opcode));
                } else {
                    RegisterType destinationRegister = RegisterType.getRegister(y);
                    RegisterType sourceRegister = RegisterType.getRegister(x);
                    Instruction result = new LoadInstruction(AddressMode.REGISTER_TO_REGISTER, sourceRegister, destinationRegister, null, null);
                    return result;
                }
            default:
                throw new IllegalArgumentException(String.format("\"Opcode still not implemented: \": %02x", opcode));
         }

         throw new IllegalArgumentException(String.format("\"Opcode still not implemented: \": %02x", opcode));
    }

    @SuppressWarnings("checkstyle:magicnumber")
    private static Instruction generateFlagAndAccumulatorOperations(final int y) {
        switch (y) {
            case 0:
                return new RotateLeftCircularInstruction(AddressMode.REGISTER_8_BIT, RegisterType.A, RegisterType.A, null, null);
            case 1:
                return new RotateRigthCircularInstruction(AddressMode.REGISTER_8_BIT, RegisterType.A, RegisterType.A, null, null);
            case 2:
                return new RotateLeftInstruction(AddressMode.REGISTER_8_BIT, RegisterType.A, RegisterType.A, null, null);
            case 3:
                return new RotateRightInstruction(AddressMode.REGISTER_8_BIT, RegisterType.A, RegisterType.A, null, null);
            case 4:
                return new DecimalAdjustAccumulatorInstruction(AddressMode.REGISTER_8_BIT, RegisterType.A, RegisterType.A, null, null);
            case 5:
                return new OneComplementInstruction(AddressMode.REGISTER_8_BIT, RegisterType.A, RegisterType.A, null, null);
            case 6:
                return new SetCarryFlagInstruction(AddressMode.REGISTER_8_BIT, RegisterType.A, RegisterType.A, null, null);
            default:
                return new StopInstruction(AddressMode.REGISTER_8_BIT, RegisterType.A, RegisterType.A, null, null);
        }
    }

    @SuppressWarnings("checkstyle:magicnumber")
    private static Instruction generateIndirectLoadInstruction(final int p, final int q) {
        // Indirect loading
        Instruction toReturn = new LoadInstruction(null, null, null, null, null);

        switch (q) {
            case 0:
                switch (p) {
                    case 0:
                        toReturn.addressMode = AddressMode.REGISTER_TO_INDIRECT_REGISTER;
                        toReturn.sourceRegister = RegisterType.A;
                        toReturn.destinationRegister = RegisterType.BC;
                        break;
                    case 1:
                        toReturn.addressMode = AddressMode.REGISTER_TO_INDIRECT_REGISTER;
                        toReturn.sourceRegister = RegisterType.A;
                        toReturn.destinationRegister = RegisterType.DE;
                        break;
                    case 2:
                        toReturn.addressMode = AddressMode.REGISTER_TO_INCREMENT_16_BIT_MEMORY_ADDRESS;
                        toReturn.sourceRegister = RegisterType.A;
                        toReturn.destinationRegister = RegisterType.HL;
                        break;
                    case 3:
                        toReturn.addressMode = AddressMode.REGISTER_TO_DECREMENT_16_BIT_MEMORY_ADDRESS;
                        toReturn.sourceRegister = RegisterType.A;
                        toReturn.destinationRegister = RegisterType.HL;
                        break;
                    default:
                        return new StopInstruction(null, null, null, null, null);
                }
                break;
            case 1:
                switch (p) {
                    case 0:
                        toReturn.addressMode = AddressMode.MEMORY_ADDRESS_REGISTER_TO_REGISTER;
                        toReturn.sourceRegister = RegisterType.BC;
                        toReturn.destinationRegister = RegisterType.A;
                        break;
                    case 1:
                        toReturn.addressMode = AddressMode.MEMORY_ADDRESS_REGISTER_TO_REGISTER;
                        toReturn.sourceRegister = RegisterType.DE;
                        toReturn.destinationRegister = RegisterType.A;
                        break;
                    case 2:
                        toReturn.addressMode = AddressMode.INCREMENT_16_BIT_MEMORY_ADDRESS_REGISTER_TO_REGISTER;
                        toReturn.sourceRegister = RegisterType.HL;
                        toReturn.destinationRegister = RegisterType.A;
                        break;
                    case 3:
                        toReturn.addressMode = AddressMode.DECREMENT_16_BIT_MEMORY_ADDRESS_REGISTER_TO_REGISTER;
                        toReturn.sourceRegister = RegisterType.HL;
                        toReturn.destinationRegister = RegisterType.A;
                        break;
                    default:
                        return new StopInstruction(null, null, null, null, null);
                }
                break;
            default:
                break;
        }
        return toReturn;
    }

    private static Instruction generate16BitInmediateLoadInstruction(final int opcode, final int p, final int q) {
        // 16-bit load immediate/add
        switch (q) {
            case 0:
                return new LoadInstruction(
                    AddressMode.DATA_16_BITS_TO_REGISTER,
                    null,
                    RegisterType.getRegisterPairFeaturingSP(p),
                    null,
                    null);
            case 1:
                return new AddInstruction(
                    AddressMode.REGISTER_16_BIT_TO_REGISTER_16_BIT,
                    RegisterType.getRegisterPairFeaturingSP(p),
                    RegisterType.HL,
                    null,
                    null);
            default:
                throw new IllegalArgumentException("Invalid opcode for instruction " + opcode);
        }
    }

    @SuppressWarnings("checkstyle:magicnumber")
    private static Instruction generateJumpAndAssortedInstructions(final int y) {
        // relative jumps and assorted instructions
        switch (y) {
            case 0:
                return new NoopInstruction(null, null, null, null, null);
            case 1:
                return new LoadInstruction(AddressMode.REGISTER_TO_MEMORY_ADDRESS_DATA, RegisterType.SP, null, null, null);
            case 2:
                return new StopInstruction(null, null, null, null, null);
            case 3:
                return new JumpRelativeInstruction(
                    AddressMode.DATA_8_BIT_TO_REGISTER,
                    null,
                    RegisterType.PC,
                    null,
                    null);
            default:
                return new JumpRelativeInstruction(
                    AddressMode.DATA_8_BIT_TO_REGISTER, null,
                    null,
                    InstructionCondition.getInstructionConditionFromIndex(y - 4),
                    null);
        }
    }

    private static Instruction generate16BitIncrementAndDecrement(final int p, final int q) {
        // 16-bit INC/DEC
        RegisterType register = RegisterType.getRegisterPairFeaturingSP(p);
        switch (q) {
            case 0:
                return new IncrementInstruction(AddressMode.REGISTER_16_BIT, register, register, null, null);
            case 1:
                return new DecrementInstruction(AddressMode.REGISTER_16_BIT, register, register, null, null);
            default:
                return new StopInstruction(null, null, null, null, null);
        }
    }
}
