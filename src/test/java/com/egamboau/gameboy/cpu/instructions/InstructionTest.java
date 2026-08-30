package com.egamboau.gameboy.cpu.instructions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class InstructionTest {

    @ParameterizedTest
    @ValueSource(ints = {
        0xCB, 0xD3, 0xD9, 0xDB, 0xDD, 0xE0, 0xE2, 0xE3, 0xE4, 0xEB, 0xEC, 0xED, 0xF4, 0xFC, 0xFD
    })
    @SuppressWarnings("checkstyle:magicnumber")
    void rejectsUnimplementedOpcode(final int opcode) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> Instruction.geInstructionFromOpcode(opcode));

        assertEquals(String.format("\"Opcode still not implemented: \": %02x", opcode), exception.getMessage());
    }
}
