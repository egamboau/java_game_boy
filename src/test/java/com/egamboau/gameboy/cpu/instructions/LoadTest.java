package com.egamboau.gameboy.cpu.instructions;

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
import com.egamboau.test.TestUtils;

class LoadTest extends CPUTestBase{

    static Stream<Arguments> generateArgumentFor8BitsInmediateDataLoadTest() {
        return Stream.of(
            Arguments.of(0x06, RegisterType.B),
            Arguments.of(0x0E, RegisterType.C),
            Arguments.of(0x16, RegisterType.D),
            Arguments.of(0x1E, RegisterType.E),
            Arguments.of(0x26, RegisterType.H),
            Arguments.of(0x2E, RegisterType.L)
        );
    }

    static Stream<Arguments> generateArgumentFor16BitsInmediateDataLoadTest() {
        return Stream.of(
            Arguments.of(0x01, RegisterType.BC),
            Arguments.of(0x11, RegisterType.DE),
            Arguments.of(0x21, RegisterType.HL)
        );
    }

    static Stream<Arguments> generateArgumentForRegisterToIndirectRegisterTest() {
        return Stream.of(
            Arguments.of(0x02, RegisterType.BC, RegisterType.A),
            Arguments.of(0x12, RegisterType.DE, RegisterType.A)
        );
    }

    static Stream<Arguments> generateArgumentForIndirectRegisterToRegisterTest() {
        return Stream.of(
            Arguments.of(0x0A, RegisterType.BC, RegisterType.A),
            Arguments.of(0x1A, RegisterType.BC, RegisterType.A)
        );
    }

    static Stream<Arguments> generateArgumentForRegisterToIndirectIncrementRegister() {
        return Stream.of(
            Arguments.of(0x22, RegisterType.HL, RegisterType.A)
        );
    }

    static Stream<Arguments> generateArgumentForRegisterToIndirectDecrementRegister() {
        return Stream.of(
            Arguments.of(0x32, RegisterType.HL, RegisterType.A)
        );
    }

    static Stream<Arguments> generateArgumentForIndirectIncrementRegisterToRegister() {
        return Stream.of(
            Arguments.of(0x2A, RegisterType.HL, RegisterType.A)
        );
    }

    @ParameterizedTest
    @MethodSource("generateArgumentFor16BitsInmediateDataLoadTest")
    void run16BitInmediateDataLoad(int opcode, RegisterType register) {
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
    void runRegisterToIndirectRegisterTest(int opcode, RegisterType addressRegister, RegisterType sourceRegister) {
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
    void run8BitsInmediateDataLoadTest(int opcode, RegisterType register) {
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
    void runIndirectRegistertRegisterTest(int opcode, RegisterType addressRegister, RegisterType destination) {
        
        int bRegisterData = TestUtils.getRandomIntegerInRange(0x00, 0xFF);
        int cRegisterData = TestUtils.getRandomIntegerInRange(0x00, 0xFF);

        int expectedData = TestUtils.getRandomIntegerInRange(0x00, 0xFF);

        int address = (bRegisterData << 8 ) | cRegisterData;
        when(this.getCurrentBus().readByteFromAddress(anyInt())).thenReturn(
            0x0A//the opcode
            );

        when(this.getCurrentBus().readByteFromAddress(address)).thenReturn(
            expectedData
        );

        runLoadMemoryDataIntoRegister(address, addressRegister, expectedData, destination);
    }
 
    @ParameterizedTest
    @MethodSource("generateArgumentForRegisterToIndirectIncrementRegister")
    void runRegisterToIndirectIncrementRegister(int opcode, RegisterType addressRegister, RegisterType source) {
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
    void runIndirectIncrementRegisterToRegisterTest(int opcode, RegisterType addressRegister, RegisterType destinationRegister) {        
        int hRegisterData = TestUtils.getRandomIntegerInRange(0x00, 0xFF);
        int lRegisterData = TestUtils.getRandomIntegerInRange(0x00, 0xFF);

        int expectedData = TestUtils.getRandomIntegerInRange(0x00, 0xFF);

        int address = (hRegisterData << 8 ) | lRegisterData;
        when(this.getCurrentBus().readByteFromAddress(anyInt())).thenReturn(
            opcode
            );

        when(this.getCurrentBus().readByteFromAddress(address)).thenReturn(
            expectedData
        );

        runLoadMemoryDataIntoRegisterWithSourceIncremenet(address, addressRegister, expectedData, destinationRegister);
    }

    @ParameterizedTest
    @MethodSource("generateArgumentForRegisterToIndirectDecrementRegister")
    void runRegisterToIndirectDecrementRegisterTest(int opcode, RegisterType addressRegister, RegisterType sourceRegister) {
        /*
         * Store the contents of register A into the memory location specified by register pair HL, and simultaneously decrement the contents of HL.
         */
        int registerData = TestUtils.getRandomIntegerInRange(0x00, 0xFF);
        int hRegisterData = TestUtils.getRandomIntegerInRange(0x00, 0xFF);
        int lRegisterData = TestUtils.getRandomIntegerInRange(0x00, 0xFF);

        int address = (hRegisterData << 8) | lRegisterData;

        when(this.getCurrentBus().readByteFromAddress(anyInt())).thenReturn(
            0x32//the opcode
            );

            runLoadRegisterDataIntoIndirectAddressWithSourceDecrement(address, addressRegister, registerData, sourceRegister);
    }

    private void runLoadInmediateDataToRegister(int loadedData, RegisterType register, boolean is16Bit) {
        Map<RegisterType, Integer> registerValues = this.getCpuRegisters(TestUtils.getPairForRegister(register));
        long previousCycleCount = getCurrentCpu().getCycles();
        this.getCurrentCpu().cpuStep();
        long currentCycleCount = getCurrentCpu().getCycles();
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisters(TestUtils.getPairForRegister(register));

        //update the expected values to the ones we want before the comparison
        if(is16Bit) {
            registerValues.computeIfPresent(RegisterType.PC, (t, u) -> u+3);
            previousCycleCount += 3;
        } else {
            registerValues.computeIfPresent(RegisterType.PC, (t, u) -> u+2);
            previousCycleCount += 2;
        }
        
        assertEquals(loadedData,getCurrentCpu().getValueFromRegister(register));
        assertEquals(previousCycleCount, currentCycleCount, "Cycle count not currently matching.");

        assertEquals(registerValues, newRegisterValues);
    }

    private void runLoadRegisterDataIntoIndirectAddress(int address, RegisterType addressRegister,  int registerData, RegisterType sourceRegister) {

        this.getCurrentCpu().setValueInRegister(registerData, sourceRegister);
        this.getCurrentCpu().setValueInRegister(address, addressRegister);

        Map<RegisterType, Integer> registerValues = this.getCpuRegisters();
        long previousCycleCount = getCurrentCpu().getCycles();
        this.getCurrentCpu().cpuStep();
        long currentCycleCount = getCurrentCpu().getCycles();
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisters();

        registerValues.computeIfPresent(RegisterType.PC, (t, u) -> u+1);
        assertEquals(registerValues, newRegisterValues);
        assertEquals(previousCycleCount+2, currentCycleCount);
        verify(this.getCurrentBus(), times(1)).writeByteToAddress(registerData, address);
    }

    private void runLoadMemoryDataIntoRegister(int address, RegisterType addressRegister,  int registerData, RegisterType destinationRegister) {

        this.getCurrentCpu().setValueInRegister(address, addressRegister);

        Map<RegisterType, Integer> registerValues = this.getCpuRegisters(TestUtils.getPairForRegister(destinationRegister));
        long previousCycleCount = getCurrentCpu().getCycles();
        this.getCurrentCpu().cpuStep();
        long currentCycleCount = getCurrentCpu().getCycles();
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisters(TestUtils.getPairForRegister(destinationRegister));

        registerValues.computeIfPresent(RegisterType.PC, (t, u) -> u+1);
        assertEquals(registerValues, newRegisterValues);
        assertEquals(previousCycleCount+2, currentCycleCount);
        assertEquals(registerData, getCurrentCpu().getValueFromRegister(destinationRegister));
    }

    private void runLoadRegisterDataIntoIndirectAddressWithSourceIncrement(int address, RegisterType addressRegister,  int registerData, RegisterType sourceRegister) {

        this.getCurrentCpu().setValueInRegister(registerData, sourceRegister);
        this.getCurrentCpu().setValueInRegister(address, addressRegister);

        Map<RegisterType, Integer> registerValues = this.getCpuRegisters(TestUtils.getPairForRegister(addressRegister));
        long previousCycleCount = getCurrentCpu().getCycles();
        this.getCurrentCpu().cpuStep();
        long currentCycleCount = getCurrentCpu().getCycles();
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisters(TestUtils.getPairForRegister(addressRegister));

        registerValues.computeIfPresent(RegisterType.PC, (t, u) -> u+1);
        registerValues.computeIfPresent(addressRegister, (t, u) -> u+1);
        assertEquals(registerValues, newRegisterValues);
        assertEquals(previousCycleCount+2, currentCycleCount);
        assertEquals(address+1, getCurrentCpu().getValueFromRegister(addressRegister));
        verify(this.getCurrentBus(), times(1)).writeByteToAddress(registerData, address);
    }

    private void runLoadRegisterDataIntoIndirectAddressWithSourceDecrement(int address, RegisterType addressRegister,  int registerData, RegisterType sourceRegister) {

        this.getCurrentCpu().setValueInRegister(registerData, sourceRegister);
        this.getCurrentCpu().setValueInRegister(address, addressRegister);

        Map<RegisterType, Integer> registerValues = this.getCpuRegisters(TestUtils.getPairForRegister(addressRegister));
        long previousCycleCount = getCurrentCpu().getCycles();
        this.getCurrentCpu().cpuStep();
        long currentCycleCount = getCurrentCpu().getCycles();
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisters(TestUtils.getPairForRegister(addressRegister));

        registerValues.computeIfPresent(RegisterType.PC, (t, u) -> u+1);
        registerValues.computeIfPresent(addressRegister, (t, u) -> u+1);
        assertEquals(registerValues, newRegisterValues);
        assertEquals(previousCycleCount+2, currentCycleCount);
        assertEquals(address-1, getCurrentCpu().getValueFromRegister(addressRegister));
        verify(this.getCurrentBus(), times(1)).writeByteToAddress(registerData, address);
    }

    private void runLoadMemoryDataIntoRegisterWithSourceIncremenet(int address, RegisterType addressRegister, int expectedData, RegisterType destinationRegister) {
        this.getCurrentCpu().setValueInRegister(address, addressRegister);

        Map<RegisterType, Integer> registerValues = this.getCpuRegisters(TestUtils.getPairForRegister(destinationRegister, addressRegister));
        long previousCycleCount = getCurrentCpu().getCycles();
        this.getCurrentCpu().cpuStep();
        long currentCycleCount = getCurrentCpu().getCycles();
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisters(TestUtils.getPairForRegister(destinationRegister, addressRegister));

        registerValues.computeIfPresent(RegisterType.PC, (t, u) -> u+1);
        assertEquals(registerValues, newRegisterValues);
        assertEquals(previousCycleCount+2, currentCycleCount);
        assertEquals(address+1, getCurrentCpu().getValueFromRegister(addressRegister));
        assertEquals(expectedData, getCurrentCpu().getValueFromRegister(destinationRegister));
        verify(this.getCurrentBus(), times(1)).readByteFromAddress(address);
    }
}
