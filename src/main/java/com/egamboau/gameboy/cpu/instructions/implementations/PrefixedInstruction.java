package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.RegisterType;
import com.egamboau.gameboy.cpu.instructions.RotationOperationType;

public class PrefixedInstruction extends Instruction {

    /**
     * Constructs a prefixed instruction.
     */
    public PrefixedInstruction() {
        super(AddressMode.MEMORY_ADDRESS_DATA_LOWER_BYTE_TO_REGISTER, null, null);
    }

    @Override
    @SuppressWarnings("checkstyle:magicnumber")

    protected void runInstructionLogic(final CPU currentCpu, final int[] data) {

        int x = (data[0] & 0300) >> 6;
        int y = (data[0] & 070) >> 3;
        int z = (data[0] & 07);

        switch (x) {
            case 0:
                Instruction rotationInstruction = getRotationInstructionFromPosition(y, z);
                rotationInstruction.executeInstruction(currentCpu);
                break;
            case 1:
                RegisterType sourceRegisterType = RegisterType.getRegister(z);
                AddressMode addressMode = getAddressFromRegister(sourceRegisterType);
                Instruction bitInstruction = new BitInstruction(y, addressMode, sourceRegisterType, sourceRegisterType);
                bitInstruction.executeInstruction(currentCpu);
                break;
            case 2:
                sourceRegisterType = RegisterType.getRegister(z);
                addressMode = getAddressFromRegister(sourceRegisterType);
                Instruction resetInstruction = new ResetBitInstruction(y, addressMode, sourceRegisterType, sourceRegisterType);
                resetInstruction.executeInstruction(currentCpu);
                break;
            case 3:
                sourceRegisterType = RegisterType.getRegister(z);
                addressMode = getAddressFromRegister(sourceRegisterType);
                Instruction setInstruction = new SetBitInstruction(y, addressMode, sourceRegisterType, sourceRegisterType);
                setInstruction.executeInstruction(currentCpu);
                break;
            default:
                throw new IllegalArgumentException(String.format("\"Opcode for CB instruction still not implemented: \": %02x", data[0]));
        }
    }

    private AddressMode getAddressFromRegister(final RegisterType sourceRegisterType) {
        AddressMode addressMode;
        if (sourceRegisterType == RegisterType.HL) {
            addressMode = AddressMode.MEMORY_ADDRESS_REGISTER_TO_REGISTER;
        } else {
            addressMode = AddressMode.REGISTER_8_BIT;
        }
        return addressMode;
    }

    private Instruction getRotationInstructionFromPosition(final int y, final int z) {
        RegisterType sourceRegisterType = RegisterType.getRegister(z);
        RotationOperationType type = RotationOperationType.getRotationOperationType(y);
        AddressMode addressMode = null;
        if (sourceRegisterType == RegisterType.HL) {
            addressMode = AddressMode.MEMORY_ADDRESS_REGISTER_TO_MEMORY_ADRESS_REGISTER;
        } else {
            addressMode = AddressMode.REGISTER_8_BIT;
        }
        return switch (type) {
            case RLC -> new RotateLeftCircularInstruction(addressMode, sourceRegisterType, sourceRegisterType, true);
            case RRC -> new RotateRigthCircularInstruction(addressMode, sourceRegisterType, sourceRegisterType, true);
            case RL -> new RotateLeftInstruction(addressMode, sourceRegisterType, sourceRegisterType, true);
            case RR -> new RotateRightInstruction(addressMode, sourceRegisterType, sourceRegisterType, true);
            case SLA -> new SwitchLeftArithmeticInstruction(addressMode, sourceRegisterType, sourceRegisterType);
            case SRA -> new SwitchRightArithmeticInstruction(addressMode, sourceRegisterType, sourceRegisterType);
            case SWAP -> new SwapInstruction(addressMode, sourceRegisterType, sourceRegisterType);
            case SRL -> new ShiftRightLogicalInstruction(addressMode, sourceRegisterType, sourceRegisterType);
        };
    }

}
