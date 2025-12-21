package com.egamboau.gameboy.cpu.instructions.implementations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.egamboau.gameboy.cpu.CPUTestBase;
import com.egamboau.gameboy.cpu.instructions.RegisterType;
import com.egamboau.test.TestUtils;

class LoadTest extends CPUTestBase {

    @SuppressWarnings({"checkstyle:magicnumber"})
    static Stream<Arguments> generateArgumentFor8BitsInmediateDataLoadTest() {
        return Stream.of(
            Arguments.of(0x06, RegisterType.B),
            Arguments.of(0x0E, RegisterType.C),
            Arguments.of(0x16, RegisterType.D),
            Arguments.of(0x1E, RegisterType.E),
            Arguments.of(0x26, RegisterType.H),
            Arguments.of(0x2E, RegisterType.L),
            Arguments.of(0x3E, RegisterType.A)
        );
    }

    @SuppressWarnings({"checkstyle:magicnumber", "checkstyle:parameternumbercheck"})
    static Stream<Arguments> generateArgumentFor16BitsInmediateDataLoadTest() {
        return Stream.of(
            Arguments.of(0x01, RegisterType.BC),
            Arguments.of(0x11, RegisterType.DE),
            Arguments.of(0x21, RegisterType.HL)
        );
    }

    @SuppressWarnings({"checkstyle:magicnumber", "checkstyle:parameternumbercheck"})
    static Stream<Arguments> generateArgumentForRegisterToIndirectRegisterTest() {
        return Stream.of(
            Arguments.of(0x02, RegisterType.BC, RegisterType.A),
            Arguments.of(0x12, RegisterType.DE, RegisterType.A),
            Arguments.of(0x70, RegisterType.HL, RegisterType.B),
            Arguments.of(0x71, RegisterType.HL, RegisterType.C),
            Arguments.of(0x72, RegisterType.HL, RegisterType.D),
            Arguments.of(0x73, RegisterType.HL, RegisterType.E),
            Arguments.of(0x74, RegisterType.HL, RegisterType.H),
            Arguments.of(0x75, RegisterType.HL, RegisterType.L),
            Arguments.of(0x77, RegisterType.HL, RegisterType.A)

        );
    }

    @SuppressWarnings({"checkstyle:magicnumber", "checkstyle:parameternumbercheck"})
    static Stream<Arguments> generateArgumentForIndirectRegisterToRegisterTest() {
        return Stream.of(
            Arguments.of(0x0A, RegisterType.BC, RegisterType.A),
            Arguments.of(0x1A, RegisterType.DE, RegisterType.A),
            Arguments.of(0x46, RegisterType.HL, RegisterType.B),
            Arguments.of(0x4E, RegisterType.HL, RegisterType.C),
            Arguments.of(0x56, RegisterType.HL, RegisterType.D),
            Arguments.of(0x5E, RegisterType.HL, RegisterType.E),
            Arguments.of(0x66, RegisterType.HL, RegisterType.H),
            Arguments.of(0x6E, RegisterType.HL, RegisterType.L),
            Arguments.of(0x7E, RegisterType.HL, RegisterType.A)
        );
    }

    @SuppressWarnings({"checkstyle:magicnumber", "checkstyle:parameternumbercheck"})
    static Stream<Arguments> generateArgumentForRegisterToIndirectIncrementRegister() {
        return Stream.of(
            Arguments.of(0x22, RegisterType.HL, RegisterType.A)
        );
    }

    @SuppressWarnings({"checkstyle:magicnumber", "checkstyle:parameternumbercheck"})
    static Stream<Arguments> generateArgumentForRegisterToIndirectDecrementRegister() {
        return Stream.of(
            Arguments.of(0x32, RegisterType.HL, RegisterType.A)
        );
    }

    @SuppressWarnings({"checkstyle:magicnumber", "checkstyle:parameternumbercheck"})
    static Stream<Arguments> generateArgumentsInmediateToIndirectRegister() {
        return Stream.of(
            Arguments.of(0x36, RegisterType.HL)
        );
    }


    @SuppressWarnings({"checkstyle:magicnumber", "checkstyle:parameternumbercheck"})
    static Stream<Arguments> generateArgumentForIndirectIncrementRegisterToRegister() {
        return Stream.of(
            Arguments.of(0x2A, RegisterType.HL, RegisterType.A)
        );
    }

    @SuppressWarnings({"checkstyle:magicnumber", "checkstyle:parameternumbercheck"})
    static Stream<Arguments> generateArgumentForIndirectDecrementRegisterToRegister() {
        return Stream.of(
            Arguments.of(0x3A, RegisterType.HL, RegisterType.A)
        );
    }

    @SuppressWarnings({"checkstyle:magicnumber", "checkstyle:parameternumbercheck"})
    static Stream<Arguments> generateArgumentsFor8BitRegisterTo8BitRegister() {
        return Stream.of(
            Arguments.of(0x40, RegisterType.B, RegisterType.B),
            Arguments.of(0x41, RegisterType.C, RegisterType.B),
            Arguments.of(0x42, RegisterType.D, RegisterType.B),
            Arguments.of(0x43, RegisterType.E, RegisterType.B),
            Arguments.of(0x44, RegisterType.H, RegisterType.B),
            Arguments.of(0x45, RegisterType.L, RegisterType.B),
            Arguments.of(0x47, RegisterType.A, RegisterType.B),

            Arguments.of(0x48, RegisterType.B, RegisterType.C),
            Arguments.of(0x49, RegisterType.C, RegisterType.C),
            Arguments.of(0x4A, RegisterType.D, RegisterType.C),
            Arguments.of(0x4B, RegisterType.E, RegisterType.C),
            Arguments.of(0x4C, RegisterType.H, RegisterType.C),
            Arguments.of(0x4D, RegisterType.L, RegisterType.C),
            Arguments.of(0x4F, RegisterType.A, RegisterType.C),

            Arguments.of(0x50, RegisterType.B, RegisterType.D),
            Arguments.of(0x51, RegisterType.C, RegisterType.D),
            Arguments.of(0x52, RegisterType.D, RegisterType.D),
            Arguments.of(0x53, RegisterType.E, RegisterType.D),
            Arguments.of(0x54, RegisterType.H, RegisterType.D),
            Arguments.of(0x55, RegisterType.L, RegisterType.D),
            Arguments.of(0x57, RegisterType.A, RegisterType.D),

            Arguments.of(0x58, RegisterType.B, RegisterType.E),
            Arguments.of(0x59, RegisterType.C, RegisterType.E),
            Arguments.of(0x5A, RegisterType.D, RegisterType.E),
            Arguments.of(0x5B, RegisterType.E, RegisterType.E),
            Arguments.of(0x5C, RegisterType.H, RegisterType.E),
            Arguments.of(0x5D, RegisterType.L, RegisterType.E),
            Arguments.of(0x5F, RegisterType.A, RegisterType.E),

            Arguments.of(0x60, RegisterType.B, RegisterType.H),
            Arguments.of(0x61, RegisterType.C, RegisterType.H),
            Arguments.of(0x62, RegisterType.D, RegisterType.H),
            Arguments.of(0x63, RegisterType.E, RegisterType.H),
            Arguments.of(0x64, RegisterType.H, RegisterType.H),
            Arguments.of(0x65, RegisterType.L, RegisterType.H),
            Arguments.of(0x67, RegisterType.A, RegisterType.H),

            Arguments.of(0x68, RegisterType.B, RegisterType.L),
            Arguments.of(0x69, RegisterType.C, RegisterType.L),
            Arguments.of(0x6A, RegisterType.D, RegisterType.L),
            Arguments.of(0x6B, RegisterType.E, RegisterType.L),
            Arguments.of(0x6C, RegisterType.H, RegisterType.L),
            Arguments.of(0x6D, RegisterType.L, RegisterType.L),
            Arguments.of(0x6F, RegisterType.A, RegisterType.L),

            Arguments.of(0x78, RegisterType.B, RegisterType.A),
            Arguments.of(0x79, RegisterType.C, RegisterType.A),
            Arguments.of(0x7A, RegisterType.D, RegisterType.A),
            Arguments.of(0x7B, RegisterType.E, RegisterType.A),
            Arguments.of(0x7C, RegisterType.H, RegisterType.A),
            Arguments.of(0x7D, RegisterType.L, RegisterType.A),
            Arguments.of(0x7F, RegisterType.A, RegisterType.A)

        );
    }

    @ParameterizedTest
    @MethodSource("generateArgumentsFor8BitRegisterTo8BitRegister")
    @SuppressWarnings({"checkstyle:magicnumber"})
    void run8BitRegisterTo8BitRegister(final int opcode, final RegisterType sourceRegister,
            final RegisterType destinationRegister) {
        int data =  TestUtils.getRandomIntegerInRange(0x00, 0xFF);
        when(this.getCurrentBus().readByteFromAddress(anyInt())).thenReturn(
            opcode //the opcode
            );

        this.getCurrentCpu().setValueInRegister(data, sourceRegister);

        Map<RegisterType, Integer> registerValues = this.getCpuRegisters(TestUtils.getPairForRegister(destinationRegister));
        long previousCycleCount = getCurrentCpu().getCycles();
        this.getCurrentCpu().cpuStep();
        long currentCycleCount = getCurrentCpu().getCycles();
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisters(TestUtils.getPairForRegister(destinationRegister));

        registerValues.computeIfPresent(RegisterType.PC, (t, u) -> u + 1);
        assertEquals(registerValues, newRegisterValues);
        assertEquals(previousCycleCount + 1, currentCycleCount);
        assertEquals(data, this.getCurrentCpu().getValueFromRegister(destinationRegister));
        assertEquals(data, this.getCurrentCpu().getValueFromRegister(sourceRegister));
    }

    @ParameterizedTest
    @MethodSource("generateArgumentFor16BitsInmediateDataLoadTest")
    @SuppressWarnings({"checkstyle:magicnumber", "checkstyle:parameternumbercheck"})
    void run16BitInmediateDataLoad(final int opcode, final RegisterType register) {
        int lowerByte = TestUtils.getRandomIntegerInRange(0x00, 0xFF);
        int upperByte = TestUtils.getRandomIntegerInRange(0x00, 0xFF);
        when(this.getCurrentBus().readByteFromAddress(anyInt())).thenReturn(
            opcode, //the opcode
            lowerByte, //lower byte of data
            upperByte //and the upper byte.
            );

        int expectedData = upperByte << 8 | lowerByte;
        runLoadInmediateDataToRegister(expectedData, register, true);
    }

    @ParameterizedTest
    @MethodSource("generateArgumentForRegisterToIndirectRegisterTest")
    @SuppressWarnings({"checkstyle:magicnumber", "checkstyle:parameternumbercheck"})
    void runRegisterToIndirectRegisterTest(final int opcode, final RegisterType addressRegister,
            final RegisterType sourceRegister) {
        int registerData = TestUtils.getRandomIntegerInRange(0x00, 0xFF);

        int bRegisterData = TestUtils.getRandomIntegerInRange(0x00, 0xFF);
        int cRegisterData = TestUtils.getRandomIntegerInRange(0x00, 0xFF);

        int address = bRegisterData << 8 | cRegisterData;

        when(this.getCurrentBus().readByteFromAddress(anyInt())).thenReturn(
            opcode//the opcode
            );

        runLoadRegisterDataIntoIndirectAddress(address, addressRegister, registerData, sourceRegister);
    }

    @ParameterizedTest
    @MethodSource("generateArgumentFor8BitsInmediateDataLoadTest")
    @SuppressWarnings({"checkstyle:magicnumber", "checkstyle:parameternumbercheck"})
    void run8BitsInmediateDataLoadTest(final int opcode, final RegisterType register) {
        /*
         * Load the 8-bit immediate operand d8 into register B.
         */
        int data = TestUtils.getRandomIntegerInRange(0x00, 0xFF);
        when(this.getCurrentBus().readByteFromAddress(anyInt())).thenReturn(
            opcode, //the opcode
            data //lower byte of data
            );
        runLoadInmediateDataToRegister(data, register, false);

    }

    @ParameterizedTest
    @MethodSource("generateArgumentForIndirectRegisterToRegisterTest")
    @SuppressWarnings({"checkstyle:magicnumber", "checkstyle:parameternumbercheck"})
    void runIndirectRegistertRegisterTest(final int opcode, final RegisterType addressRegister,
            final RegisterType destination) {

        int bRegisterData = TestUtils.getRandomIntegerInRange(0x00, 0xFF);
        int cRegisterData = TestUtils.getRandomIntegerInRange(0x00, 0xFF);

        int expectedData = TestUtils.getRandomIntegerInRange(0x00, 0xFF);

        int address = (bRegisterData << 8) | cRegisterData;
        when(this.getCurrentBus().readByteFromAddress(anyInt())).thenReturn(
            opcode
            );

        when(this.getCurrentBus().readByteFromAddress(address)).thenReturn(
            expectedData
        );

        runLoadMemoryDataIntoRegister(address, addressRegister, expectedData, destination);
    }

    @ParameterizedTest
    @MethodSource("generateArgumentForRegisterToIndirectIncrementRegister")
    @SuppressWarnings({"checkstyle:magicnumber", "checkstyle:parameternumbercheck"})
    void runRegisterToIndirectIncrementRegister(final int opcode, final RegisterType addressRegister,
            final RegisterType source) {
        int registerData = TestUtils.getRandomIntegerInRange(0x00, 0xFF);
        int dRegisterData = TestUtils.getRandomIntegerInRange(0x00, 0xFF);
        int eRegisterData = TestUtils.getRandomIntegerInRange(0x00, 0xFF);

        int address = (dRegisterData << 8) | eRegisterData;

        when(this.getCurrentBus().readByteFromAddress(anyInt())).thenReturn(
            0x22//the opcode
            );

        runLoadRegisterDataIntoIndirectAddressWithSourceIncrement(address, addressRegister, registerData, source);
    }

    @ParameterizedTest
    @MethodSource("generateArgumentForIndirectIncrementRegisterToRegister")
    @SuppressWarnings({"checkstyle:magicnumber", "checkstyle:parameternumbercheck"})
    void runIndirectIncrementRegisterToRegisterTest(final int opcode, final RegisterType addressRegister,
            final RegisterType destinationRegister) {
        int hRegisterData = TestUtils.getRandomIntegerInRange(0x00, 0xFF);
        int lRegisterData = TestUtils.getRandomIntegerInRange(0x00, 0xFF);

        int expectedData = TestUtils.getRandomIntegerInRange(0x00, 0xFF);

        int address = (hRegisterData << 8) | lRegisterData;
        when(this.getCurrentBus().readByteFromAddress(anyInt())).thenReturn(
                opcode);

        when(this.getCurrentBus().readByteFromAddress(address)).thenReturn(
                expectedData);

        runLoadMemoryDataIntoRegisterWithSourceIncremenet(address, addressRegister, expectedData, destinationRegister);
    }

    @ParameterizedTest
    @MethodSource("generateArgumentForIndirectDecrementRegisterToRegister")
    @SuppressWarnings({"checkstyle:magicnumber", "checkstyle:parameternumbercheck"})
    void runIndirectDecrementRegisterToRegisterTest(final int opcode, final RegisterType addressRegister,
            final RegisterType destinationRegister) {
        int hRegisterData = TestUtils.getRandomIntegerInRange(0x00, 0xFF);
        int lRegisterData = TestUtils.getRandomIntegerInRange(0x00, 0xFF);

        int expectedData = TestUtils.getRandomIntegerInRange(0x00, 0xFF);

        int address = (hRegisterData << 8) | lRegisterData;
        when(this.getCurrentBus().readByteFromAddress(anyInt())).thenReturn(
                opcode);

        when(this.getCurrentBus().readByteFromAddress(address)).thenReturn(
                expectedData);

        runLoadMemoryDataIntoRegisterWithSourceDecrement(address, addressRegister, expectedData, destinationRegister);
    }

    @ParameterizedTest
    @MethodSource("generateArgumentForRegisterToIndirectDecrementRegister")
    @SuppressWarnings({"checkstyle:magicnumber", "checkstyle:parameternumbercheck"})
    void runRegisterToIndirectDecrementRegisterTest(final int opcode, final RegisterType addressRegister,
            final RegisterType sourceRegister) {
        /*
         * Store the contents of register A into the memory location specified by
         * register pair HL, and simultaneously decrement the contents of HL.
         */
        int registerData = TestUtils.getRandomIntegerInRange(0x00, 0xFF);
        int hRegisterData = TestUtils.getRandomIntegerInRange(0x00, 0xFF);
        int lRegisterData = TestUtils.getRandomIntegerInRange(0x00, 0xFF);

        int address = (hRegisterData << 8) | lRegisterData;

        when(this.getCurrentBus().readByteFromAddress(anyInt())).thenReturn(
                0x32// the opcode
        );

        runLoadRegisterDataIntoIndirectAddressWithSourceDecrement(address, addressRegister, registerData,
                sourceRegister);
    }

@ParameterizedTest
    @MethodSource("generateArgumentsInmediateToIndirectRegister")
    @SuppressWarnings({"checkstyle:magicnumber", "checkstyle:parameternumbercheck"})
    void runInmediateToIndirectRegisterTest(final int opcode, final RegisterType addressRegister) {

        int inmediateValue = TestUtils.getRandomIntegerInRange(0x00, 0xFF);
        int hRegisterData = TestUtils.getRandomIntegerInRange(0x00, 0xFF);
        int lRegisterData = TestUtils.getRandomIntegerInRange(0x00, 0xFF);

        int address = (hRegisterData << 8) | lRegisterData;

        when(this.getCurrentBus().readByteFromAddress(anyInt())).thenReturn(
                0x36, // the opcode
                inmediateValue
        );

        runLoadInmediateDataIntoIndirectAddressWithSourceDecrement(address, addressRegister, inmediateValue);
    }

    @SuppressWarnings({"checkstyle:magicnumber", "checkstyle:parameternumbercheck"})
    private void runLoadInmediateDataToRegister(final int loadedData, final RegisterType register,
            final boolean is16Bit) {
        Map<RegisterType, Integer> registerValues = this.getCpuRegisters(TestUtils.getPairForRegister(register));
        long previousCycleCount = getCurrentCpu().getCycles();
        this.getCurrentCpu().cpuStep();
        long currentCycleCount = getCurrentCpu().getCycles();
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisters(TestUtils.getPairForRegister(register));

        // update the expected values to the ones we want before the comparison
        if (is16Bit) {
            registerValues.computeIfPresent(RegisterType.PC, (t, u) -> u + 3);
            previousCycleCount += 3;
        } else {
            registerValues.computeIfPresent(RegisterType.PC, (t, u) -> u + 2);
            previousCycleCount += 2;
        }

        assertEquals(loadedData, getCurrentCpu().getValueFromRegister(register));
        assertEquals(previousCycleCount, currentCycleCount, "Cycle count not currently matching.");

        assertEquals(registerValues, newRegisterValues);
    }

    private void runLoadRegisterDataIntoIndirectAddress(final int address, final RegisterType addressRegister,
            final int registerData, final RegisterType sourceRegister) {

        this.getCurrentCpu().setValueInRegister(registerData, sourceRegister);
        this.getCurrentCpu().setValueInRegister(address, addressRegister);

        Map<RegisterType, Integer> registerValues = this.getCpuRegisters();
        long previousCycleCount = getCurrentCpu().getCycles();
        this.getCurrentCpu().cpuStep();
        long currentCycleCount = getCurrentCpu().getCycles();
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisters();

        registerValues.computeIfPresent(RegisterType.PC, (t, u) -> u + 1);
        assertEquals(registerValues, newRegisterValues);
        assertEquals(previousCycleCount + 2, currentCycleCount);
        verify(this.getCurrentBus(), times(1)).writeByteToAddress(this.getCurrentCpu().getValueFromRegister(sourceRegister), address);
    }

    private void runLoadMemoryDataIntoRegister(final int address, final RegisterType addressRegister,
            final int registerData, final RegisterType destinationRegister) {

        this.getCurrentCpu().setValueInRegister(address, addressRegister);

        Map<RegisterType, Integer> registerValues = this
                .getCpuRegisters(TestUtils.getPairForRegister(destinationRegister));
        long previousCycleCount = getCurrentCpu().getCycles();
        this.getCurrentCpu().cpuStep();
        long currentCycleCount = getCurrentCpu().getCycles();
        Map<RegisterType, Integer> newRegisterValues = this
                .getCpuRegisters(TestUtils.getPairForRegister(destinationRegister));

        registerValues.computeIfPresent(RegisterType.PC, (t, u) -> u + 1);
        assertEquals(registerValues, newRegisterValues);
        assertEquals(previousCycleCount + 2, currentCycleCount);
        assertEquals(registerData, getCurrentCpu().getValueFromRegister(destinationRegister));
    }

    private void runLoadRegisterDataIntoIndirectAddressWithSourceIncrement(final int address,
            final RegisterType addressRegister, final int registerData, final RegisterType sourceRegister) {

        this.getCurrentCpu().setValueInRegister(registerData, sourceRegister);
        this.getCurrentCpu().setValueInRegister(address, addressRegister);

        Map<RegisterType, Integer> registerValues = this.getCpuRegisters(TestUtils.getPairForRegister(addressRegister));
        long previousCycleCount = getCurrentCpu().getCycles();
        this.getCurrentCpu().cpuStep();
        long currentCycleCount = getCurrentCpu().getCycles();
        Map<RegisterType, Integer> newRegisterValues = this
                .getCpuRegisters(TestUtils.getPairForRegister(addressRegister));

        registerValues.computeIfPresent(RegisterType.PC, (t, u) -> u + 1);
        registerValues.computeIfPresent(addressRegister, (t, u) -> u + 1);
        assertEquals(registerValues, newRegisterValues);
        assertEquals(previousCycleCount + 2, currentCycleCount);
        assertEquals(address + 1, getCurrentCpu().getValueFromRegister(addressRegister));
        verify(this.getCurrentBus(), times(1)).writeByteToAddress(registerData, address);
    }

    private void runLoadRegisterDataIntoIndirectAddressWithSourceDecrement(final int address,
            final RegisterType addressRegister, final int registerData, final RegisterType sourceRegister) {

        this.getCurrentCpu().setValueInRegister(registerData, sourceRegister);
        this.getCurrentCpu().setValueInRegister(address, addressRegister);

        Map<RegisterType, Integer> registerValues = this.getCpuRegisters(TestUtils.getPairForRegister(addressRegister));
        long previousCycleCount = getCurrentCpu().getCycles();
        this.getCurrentCpu().cpuStep();
        long currentCycleCount = getCurrentCpu().getCycles();
        Map<RegisterType, Integer> newRegisterValues = this
                .getCpuRegisters(TestUtils.getPairForRegister(addressRegister));

        registerValues.computeIfPresent(RegisterType.PC, (t, u) -> u + 1);
        registerValues.computeIfPresent(addressRegister, (t, u) -> u + 1);
        assertEquals(registerValues, newRegisterValues);
        assertEquals(previousCycleCount + 2, currentCycleCount);
        assertEquals(address - 1, getCurrentCpu().getValueFromRegister(addressRegister));
        verify(this.getCurrentBus(), times(1)).writeByteToAddress(registerData, address);
    }

    @SuppressWarnings({"checkstyle:magicnumber", "checkstyle:parameternumbercheck"})
    private void runLoadInmediateDataIntoIndirectAddressWithSourceDecrement(final int address,
            final RegisterType addressRegister, final int inmediateValue) {

        this.getCurrentCpu().setValueInRegister(address, addressRegister);

        Map<RegisterType, Integer> registerValues = this.getCpuRegisters(TestUtils.getPairForRegister(addressRegister));
        long previousCycleCount = getCurrentCpu().getCycles();
        this.getCurrentCpu().cpuStep();
        long currentCycleCount = getCurrentCpu().getCycles();
        Map<RegisterType, Integer> newRegisterValues = this
                .getCpuRegisters(TestUtils.getPairForRegister(addressRegister));

        registerValues.computeIfPresent(RegisterType.PC, (t, u) -> u + 2);
        assertEquals(registerValues, newRegisterValues);
        assertEquals(previousCycleCount + 3, currentCycleCount);
        verify(this.getCurrentBus(), times(1)).writeByteToAddress(inmediateValue, address);
    }


    private void runLoadMemoryDataIntoRegisterWithSourceIncremenet(final int address,
            final RegisterType addressRegister, final int expectedData, final RegisterType destinationRegister) {
        this.getCurrentCpu().setValueInRegister(address, addressRegister);

        Map<RegisterType, Integer> registerValues = this.getCpuRegisters(TestUtils.getPairForRegister(destinationRegister, addressRegister));
        long previousCycleCount = getCurrentCpu().getCycles();
        this.getCurrentCpu().cpuStep();
        long currentCycleCount = getCurrentCpu().getCycles();
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisters(TestUtils.getPairForRegister(destinationRegister, addressRegister));

        registerValues.computeIfPresent(RegisterType.PC, (t, u) -> u + 1);
        assertEquals(registerValues, newRegisterValues);
        assertEquals(previousCycleCount + 2, currentCycleCount);
        assertEquals(address + 1, getCurrentCpu().getValueFromRegister(addressRegister));
        assertEquals(expectedData, getCurrentCpu().getValueFromRegister(destinationRegister));
        verify(this.getCurrentBus(), times(1)).readByteFromAddress(address);
    }

    private void runLoadMemoryDataIntoRegisterWithSourceDecrement(final int address,
            final RegisterType addressRegister, final int expectedData, final RegisterType destinationRegister) {
        this.getCurrentCpu().setValueInRegister(address, addressRegister);

        Map<RegisterType, Integer> registerValues = this.getCpuRegisters(TestUtils.getPairForRegister(destinationRegister, addressRegister));
        long previousCycleCount = getCurrentCpu().getCycles();
        this.getCurrentCpu().cpuStep();
        long currentCycleCount = getCurrentCpu().getCycles();
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisters(TestUtils.getPairForRegister(destinationRegister, addressRegister));

        registerValues.computeIfPresent(RegisterType.PC, (t, u) -> u + 1);
        assertEquals(registerValues, newRegisterValues);
        assertEquals(previousCycleCount + 2, currentCycleCount);
        assertEquals(address - 1, getCurrentCpu().getValueFromRegister(addressRegister));
        assertEquals(expectedData, getCurrentCpu().getValueFromRegister(destinationRegister));
        verify(this.getCurrentBus(), times(1)).readByteFromAddress(address);
    }
}
