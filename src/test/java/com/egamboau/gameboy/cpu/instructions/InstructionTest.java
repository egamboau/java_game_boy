package com.egamboau.gameboy.cpu.instructions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Set;

import org.junit.jupiter.api.Test;

class InstructionTest {

    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    void decodesEveryBaseOpcodeExceptTheElevenInvalidOpcodes() {
        Set<Integer> invalidOpcodes = Set.of(
                0xD3, 0xDB, 0xDD, 0xE3, 0xE4, 0xEB, 0xEC, 0xED, 0xF4, 0xFC, 0xFD);

        for (int opcode = 0; opcode <= 0xFF; opcode++) {
            int currentOpcode = opcode;
            if (invalidOpcodes.contains(opcode)) {
                IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                        () -> Instruction.geInstructionFromOpcode(currentOpcode));

                assertEquals(String.format("Invalid opcode: %02x", opcode), exception.getMessage());
            } else {
                assertDoesNotThrow(() -> Instruction.geInstructionFromOpcode(currentOpcode),
                        String.format("Opcode %02x", opcode));
            }
        }
    }
}
