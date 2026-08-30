package com.egamboau.gameboy.cpu.instructions.implementations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.egamboau.gameboy.cpu.CPUTestBase;
import com.egamboau.gameboy.cpu.instructions.AddressMode;
import com.egamboau.gameboy.cpu.instructions.Instruction;
import com.egamboau.gameboy.cpu.instructions.RegisterType;

class UnsupportedAddressModeTest extends CPUTestBase {

    @ParameterizedTest
    @MethodSource("unsupportedInstructions")
    void rejectsUnsupportedAddressMode(final Instruction instruction, final String expectedMessage) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> instruction.executeInstruction(getCurrentCpu()));

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void loadRejectsUnsupportedSourceRegister() {
        LoadInstruction instruction = new LoadInstruction(
                AddressMode.REGISTER_TO_MEMORY_ADDRESS_DATA, RegisterType.B, null);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> instruction.executeInstruction(getCurrentCpu()));

        assertEquals("Unknown Source register: B", exception.getMessage());
    }

    static Stream<Arguments> unsupportedInstructions() {
        return Stream.of(
                Arguments.of(new AddInstruction(AddressMode.MEMORY_ADDRESS_REGISTER, RegisterType.B, RegisterType.A),
                        "Address mode not supported for ADD instruction: MEMORY_ADDRESS_REGISTER"),
                Arguments.of(new AddWithCarryInstruction(AddressMode.REGISTER_8_BIT, RegisterType.B, RegisterType.A),
                        "Address mode not supported for ADC instruction: REGISTER_8_BIT"),
                Arguments.of(new DecrementInstruction(AddressMode.REGISTER_TO_REGISTER, RegisterType.B, RegisterType.A),
                        "Address mode REGISTER_TO_REGISTER not supported"),
                Arguments.of(new LoadInstruction(AddressMode.REGISTER_8_BIT, RegisterType.B, RegisterType.A),
                        "Unknown address mode: REGISTER_8_BIT"),
                Arguments.of(new SubInstruction(AddressMode.REGISTER_8_BIT, RegisterType.B, RegisterType.A),
                        "Address mode not supported for SUB instruction: REGISTER_8_BIT"),
                Arguments.of(new SubWithCarryInstruction(AddressMode.REGISTER_8_BIT, RegisterType.B, RegisterType.A),
                        "Address mode not supported for SBC instruction: REGISTER_8_BIT"));
    }
}
