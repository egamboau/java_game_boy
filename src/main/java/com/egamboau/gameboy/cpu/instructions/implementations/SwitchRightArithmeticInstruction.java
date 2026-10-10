package com.egamboau.gameboy.cpu.instructions.implementations;

import com.egamboau.gameboy.cpu.CPU;
import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.RegisterType;
import com.egamboau.gameboy.memory.BitMasks;

public class SwitchRightArithmeticInstruction extends Instruction {

    protected SwitchRightArithmeticInstruction(final AddressMode currentAddressMode,
            final RegisterType currentSourceRegister,
            final RegisterType currentDestinationRegister) {
        super(currentAddressMode, currentSourceRegister, currentDestinationRegister);
    }

    @Override
    protected void runInstructionLogic(final CPU currentCpu, final int[] data) {
        int value = 0;
        if (getAddressMode() == AddressMode.MEMORY_ADDRESS_REGISTER_TO_MEMORY_ADRESS_REGISTER) {
            value = currentCpu.readByteFromAddress(currentCpu.getValueFromRegister(getSourceRegister()));
        } else {
            value = currentCpu.getValueFromRegister(getSourceRegister());
        }
        int futureCarry = value & 0x01;
        int lastBit = value & BitMasks.GET_SEVENTH_BIT;
        int result = (value >> 1) | lastBit;

        if (getAddressMode() == AddressMode.MEMORY_ADDRESS_REGISTER_TO_MEMORY_ADRESS_REGISTER) {
            currentCpu.writeByteToAddress(currentCpu.getValueFromRegister(getDestinationRegister()), result);
        } else {
            currentCpu.setValueInRegister(result, getDestinationRegister());
        }
        currentCpu.setCarry(futureCarry != 0);
        currentCpu.setZero(result == 0);
        currentCpu.setSubtract(false);
        currentCpu.setHalfCarry(false);
    }

}
