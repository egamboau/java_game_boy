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
    void testAddInstruction(final int opcode, final RegisterType sourceRegister,
                    final RegisterType destinationRegister) {
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

    @SuppressWarnings("checkstyle:magicnumber")
    private void executeAddTest(final int sourceValue, final int destinationValue, final RegisterType sourceRegister,
        final RegisterType destinationRegister) {
        int expectedValue = sourceValue + destinationValue;

        this.getCurrentCpu().setValueInRegister(sourceValue, sourceRegister);
        this.getCurrentCpu().setValueInRegister(destinationValue, destinationRegister);

        Map<RegisterType, Integer> registerValues = this
                .getCpuRegisters(TestUtils.getPairForRegister(RegisterType.HL, RegisterType.F));
        long previousCycleCount = getCurrentCpu().getCycles();
        this.getCurrentCpu().cpuStep();
        long currentCycleCount = getCurrentCpu().getCycles();
        Map<RegisterType, Integer> newRegisterValues = this
                .getCpuRegisters(TestUtils.getPairForRegister(RegisterType.HL, RegisterType.F));

        // HL Value must be updated with the new value.
        assertEquals(expectedValue & MASK_INT_16_BIT,
                this.getCurrentCpu().getValueFromRegister(destinationRegister),
                "Register value not matching the expected value: " + destinationRegister);
        // Cycle count must match
        assertEquals(previousCycleCount + 2, currentCycleCount, "Cycle count not currently matching.");

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
                Arguments.of(0x29, RegisterType.HL, RegisterType.HL));
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
                Arguments.of(0x09, RegisterType.BC, 0x0001, RegisterType.HL, 0x0001, false, false,
                        false),

                Arguments.of(0x19, RegisterType.DE, 0xFFFF, RegisterType.HL, 0x0001, false, true,
                        true),
                Arguments.of(0x19, RegisterType.DE, 0x0001, RegisterType.HL, 0x0FFF, false, true,
                        false),
                Arguments.of(0x19, RegisterType.DE, 0x0001, RegisterType.HL, 0x0001, false, false,
                        false),
                Arguments.of(0x19, RegisterType.DE, 0x0001, RegisterType.HL, 0x0001, false, false,
                        false),

                Arguments.of(0x29, RegisterType.HL, 0xFFFF, RegisterType.HL, 0xFFFF, false, true,
                        true),
                Arguments.of(0x29, RegisterType.HL, 0x0001, RegisterType.HL, 0x0001, false, false,
                        false));
    }
}
