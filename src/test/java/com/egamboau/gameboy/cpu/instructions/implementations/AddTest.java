package com.egamboau.gameboy.cpu.instructions.implementations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.egamboau.gameboy.cpu.CPUTestBase;
import com.egamboau.gameboy.cpu.instructions.RegisterType;
import com.egamboau.test.TestUtils;

class AddTest extends CPUTestBase {

    @ParameterizedTest
    @SuppressWarnings("checkstyle:magicnumber")
    @MethodSource("generateArgumentsForAdd")
    void testAddInstruction(final int opcode, final RegisterType sourceRegister, final RegisterType destinationRegister) {
        /*
         * Add the contents of register pair BC to the contents of register pair HL, and
         * store the results in register pair HL.
         */
        int sourceValue = TestUtils.getRandomIntegerInRange(0, 0xFFFF) & 0xFFFF;
        int destinationValue;
        if (sourceRegister == destinationRegister) {
            destinationValue = sourceValue;
        } else {
            destinationValue = TestUtils.getRandomIntegerInRange(0, 0xFFFF) & 0xFFFF;
        }
        when(this.getCurrentBus().readByteFromAddress(anyInt())).thenReturn(
                opcode);
        executeAddTest(sourceValue, destinationValue, sourceRegister, destinationRegister);
    }

    @ParameterizedTest
    @SuppressWarnings({"checkstyle:magicnumber", "checkstyle:parameternumbercheck"})
    @MethodSource("generateArgumentsForAddWithFlags")
    void testAddInstructionWithFlagsCheck(
            final int opcode,
            final RegisterType sourcRegister,
            final int sourceValue,
            final RegisterType destinationRegister,
            final int destinationValue,
            final boolean expectedSubstractFlag,
            final boolean expectedHalfCarryFlag,
            final boolean expectedCarryFlag) {
        when(this.getCurrentBus().readByteFromAddress(anyInt())).thenReturn(
                opcode // the opcode
        );

        executeAddTest(sourceValue, destinationValue, sourcRegister, destinationRegister);

        assertEquals(expectedSubstractFlag, this.getCurrentCpu().getSubtract(), "Substract flag set incorrectly");
        assertEquals(expectedHalfCarryFlag, this.getCurrentCpu().getHalfCarry(), "Half Carry flag set incorrectly");
        assertEquals(expectedCarryFlag, this.getCurrentCpu().getCarry(), "Carry flag set incorrectly");
    }

    @ParameterizedTest
    @SuppressWarnings({"checkstyle:magicnumber", "checkstyle:parameternumbercheck"})
    @MethodSource("generateArgumentForAddWithFlagsIncludingZero")
    void testAddInstructionWithFlagsCheckOnSingleRegisters(
            final int opcode,
            final RegisterType sourceRegister,
            final int sourceValue,
            final RegisterType destinationRegister,
            final int destinationValue,
            final boolean expectedSubstractFlag,
            final boolean expectedHalfCarryFlag,
            final boolean expectedCarryFlag,
            final boolean expectedZeroFlag) {
        when(this.getCurrentBus().readByteFromAddress(anyInt())).thenReturn(
                opcode // the opcode
        );

        executeAddTest(sourceValue, destinationValue, sourceRegister, destinationRegister);

        assertEquals(expectedSubstractFlag, this.getCurrentCpu().getSubtract(), "Substract flag set incorrectly");
        assertEquals(expectedHalfCarryFlag, this.getCurrentCpu().getHalfCarry(), "Half Carry flag set incorrectly");
        assertEquals(expectedCarryFlag, this.getCurrentCpu().getCarry(), "Carry flag set incorrectly");
        assertEquals(expectedZeroFlag, this.getCurrentCpu().getZero(), "Zero flag was set incorrectly");
    }

    @SuppressWarnings("checkstyle:magicnumber")
    private void executeAddTest(final int sourceValue, final int destinationValue, final RegisterType sourceRegister,
        final RegisterType destinationRegister) {
        int expectedValue = sourceValue + destinationValue;

        this.getCurrentCpu().setValueInRegister(sourceValue, sourceRegister);
        this.getCurrentCpu().setValueInRegister(destinationValue, destinationRegister);

        Map<RegisterType, Integer> registerValues = this
                .getCpuRegisters(TestUtils.getPairForRegister(destinationRegister, RegisterType.F));
        long previousCycleCount = getCurrentCpu().getCycles();
        this.getCurrentCpu().cpuStep();
        long currentCycleCount = getCurrentCpu().getCycles();
        Map<RegisterType, Integer> newRegisterValues = this
                .getCpuRegisters(TestUtils.getPairForRegister(destinationRegister, RegisterType.F));

        // Register must be updated with the new value.
        if (TestUtils.isSingleRegister(destinationRegister)) {
                assertEquals(expectedValue & MASK_INT_8_BIT,
                        this.getCurrentCpu().getValueFromRegister(destinationRegister),
                        "Register value not matching the expected value: " + destinationRegister);
                // Cycle count must match
                assertEquals(previousCycleCount + 1, currentCycleCount, "Cycle count not correctly matching.");
        } else {
                assertEquals(expectedValue & MASK_INT_16_BIT,
                        this.getCurrentCpu().getValueFromRegister(destinationRegister),
                        "Register value not matching the expected value: " + destinationRegister);
                // Cycle count must match
                assertEquals(previousCycleCount + 2, currentCycleCount, "Cycle count not correctly matching.");
        }

        // other flags must be the same, and update the PC to be 1 byte more
        registerValues.computeIfPresent(RegisterType.PC, (t, u) -> u + 1);
        assertEquals(registerValues, newRegisterValues, "CPU Register values did not match the previous state.");
    }

    @SuppressWarnings("checkstyle:magicnumber")
    static Stream<Arguments> generateArgumentsForAdd() {
        // parameters for this methods are the following:
        // int opcode, RegisterType sourceRegister, RegisterType destinationRegister
        return Stream.of(
                Arguments.of(0x09, RegisterType.BC, RegisterType.HL),
                Arguments.of(0x19, RegisterType.DE, RegisterType.HL),
                Arguments.of(0x29, RegisterType.HL, RegisterType.HL),
                Arguments.of(0x39, RegisterType.SP, RegisterType.HL),

                Arguments.of(0x80, RegisterType.B, RegisterType.A),
                Arguments.of(0x81, RegisterType.C, RegisterType.A),
                Arguments.of(0x82, RegisterType.D, RegisterType.A),
                Arguments.of(0x83, RegisterType.E, RegisterType.A),
                Arguments.of(0x84, RegisterType.H, RegisterType.A),
                Arguments.of(0x85, RegisterType.L, RegisterType.A)
        );

    }

    @SuppressWarnings("checkstyle:magicnumber")
    static Stream<Arguments> generateArgumentForAddWithFlagsIncludingZero() {
        return Stream.of(
                Arguments.of(0x80, RegisterType.B, 0xFF, RegisterType.A, 0x01, false, true, true, true),
                Arguments.of(0x80, RegisterType.B, 0x01, RegisterType.A, 0x0F, false, true, false, false),
                Arguments.of(0x80, RegisterType.B, 0x01, RegisterType.A, 0x01, false, false, false, false),

                Arguments.of(0x81, RegisterType.C, 0xFF, RegisterType.A, 0x01, false, true, true, true),
                Arguments.of(0x81, RegisterType.C, 0x01, RegisterType.A, 0x0F, false, true, false, false),
                Arguments.of(0x81, RegisterType.C, 0x01, RegisterType.A, 0x01, false, false, false, false),

                Arguments.of(0x82, RegisterType.D, 0xFF, RegisterType.A, 0x01, false, true, true, true),
                Arguments.of(0x82, RegisterType.D, 0x01, RegisterType.A, 0x0F, false, true, false, false),
                Arguments.of(0x82, RegisterType.D, 0x01, RegisterType.A, 0x01, false, false, false, false),

                Arguments.of(0x83, RegisterType.E, 0xFF, RegisterType.A, 0x01, false, true, true, true),
                Arguments.of(0x83, RegisterType.E, 0x01, RegisterType.A, 0x0F, false, true, false, false),
                Arguments.of(0x83, RegisterType.E, 0x01, RegisterType.A, 0x01, false, false, false, false),


                Arguments.of(0x84, RegisterType.H, 0xFF, RegisterType.A, 0x01, false, true, true, true),
                Arguments.of(0x84, RegisterType.H, 0x01, RegisterType.A, 0x0F, false, true, false, false),
                Arguments.of(0x84, RegisterType.H, 0x01, RegisterType.A, 0x01, false, false, false, false),

                Arguments.of(0x85, RegisterType.L, 0xFF, RegisterType.A, 0x01, false, true, true, true),
                Arguments.of(0x85, RegisterType.L, 0x01, RegisterType.A, 0x0F, false, true, false, false),
                Arguments.of(0x85, RegisterType.L, 0x01, RegisterType.A, 0x01, false, false, false, false)
        );

    }

    @SuppressWarnings("checkstyle:magicnumber")
    static Stream<Arguments> generateArgumentsForAddWithFlags() {
        return Stream.of(
                Arguments.of(0x09, RegisterType.BC, 0xFFFF, RegisterType.HL, 0x0001, false, true,
                        true),
                Arguments.of(0x09, RegisterType.BC, 0x0001, RegisterType.HL, 0x0FFF, false, true,
                        false),
                Arguments.of(0x09, RegisterType.BC, 0x0001, RegisterType.HL, 0x0001, false, false,
                        false),

                Arguments.of(0x19, RegisterType.DE, 0xFFFF, RegisterType.HL, 0x0001, false, true,
                        true),
                Arguments.of(0x19, RegisterType.DE, 0x0001, RegisterType.HL, 0x0FFF, false, true,
                        false),
                Arguments.of(0x19, RegisterType.DE, 0x0001, RegisterType.HL, 0x0001, false, false,
                        false),

                Arguments.of(0x29, RegisterType.HL, 0xFFFF, RegisterType.HL, 0xFFFF, false, true,
                        true),
                Arguments.of(0x29, RegisterType.HL, 0x0001, RegisterType.HL, 0x0001, false, false,
                        false),

                Arguments.of(0x39, RegisterType.SP, 0xFFFF, RegisterType.HL, 0x0001, false, true,
                        true),
                Arguments.of(0x39, RegisterType.SP, 0x0001, RegisterType.HL, 0x0FFF, false, true,
                        false),
                Arguments.of(0x39, RegisterType.SP, 0x0001, RegisterType.HL, 0x0001, false, false,
                        false));
    }
}
