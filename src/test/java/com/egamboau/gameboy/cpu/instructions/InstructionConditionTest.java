package com.egamboau.gameboy.cpu.instructions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;


class InstructionConditionTest {

    @Test
    void testEnumValues() {
        assertNotNull(InstructionCondition.CARRY_FLAG_NOT_SET);
        assertNotNull(InstructionCondition.CARRY_FLAG_SET);
        assertNotNull(InstructionCondition.Z_FLAG_NOT_SET);
        assertNotNull(InstructionCondition.Z_FLAG_SET);
    }

    @Test
    @SuppressWarnings({"checkstyle:magicnumber"})
    void testGetInstructionConditionFromIndex() {
        assertEquals(InstructionCondition.Z_FLAG_NOT_SET, InstructionCondition.getInstructionConditionFromIndex(0));
        assertEquals(InstructionCondition.Z_FLAG_SET, InstructionCondition.getInstructionConditionFromIndex(1));
        assertEquals(InstructionCondition.CARRY_FLAG_NOT_SET, InstructionCondition.getInstructionConditionFromIndex(2));
        assertEquals(InstructionCondition.CARRY_FLAG_SET, InstructionCondition.getInstructionConditionFromIndex(3));
    }

    @Test
    @SuppressWarnings({"checkstyle:magicnumber"})
    void testGetInstructionConditionFromIndexThrowsExceptionForInvalidIndex() {
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            InstructionCondition.getInstructionConditionFromIndex(-1);
        });
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            InstructionCondition.getInstructionConditionFromIndex(4);
        });
    }
}
