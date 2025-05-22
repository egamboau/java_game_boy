package com.egamboau.gameboy.cpu.instructions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.egamboau.gameboy.cpu.CPUTestBase;
import com.egamboau.test.TestUtils;

class JumpTest extends CPUTestBase{

    static Stream<Arguments> generateJrTestArguments() {
        return Stream.of(
            Arguments.of(0x18, TestUtils.getRandomIntegerInRange(1, 127) & 0xFF),
            Arguments.of(0x18, TestUtils.getRandomIntegerInRange(-127, -1) & 0xFF)
        );
    }

    static Stream<Arguments> generateJrNzTestArguments() {
        return Stream.of(
            Arguments.of(0x20,TestUtils.getRandomIntegerInRange(1, 127) & 0xFF, false, 3),
            Arguments.of(0x20,TestUtils.getRandomIntegerInRange(-127, -1) & 0xFF, true, 2),
            Arguments.of(0x20,TestUtils.getRandomIntegerInRange(1, 127) & 0xFF, false, 3),
            Arguments.of(0x20,TestUtils.getRandomIntegerInRange(-127, -1) & 0xFF, true, 2)
        );
    }

    static Stream<Arguments> generateJrZTestArguments() {
        return Stream.of(
            Arguments.of(0x28,TestUtils.getRandomIntegerInRange(1, 127) & 0xFF, false, 2),
            Arguments.of(0x28,TestUtils.getRandomIntegerInRange(-127, -1) & 0xFF, true, 3),
            Arguments.of(0x28,TestUtils.getRandomIntegerInRange(1, 127) & 0xFF, false, 2),
            Arguments.of(0x28,TestUtils.getRandomIntegerInRange(-127, -1) & 0xFF, true, 3)
        );
    }

    static Stream<Arguments> generateJrNcTestArguments() {
        return Stream.of(
            Arguments.of(0x30,TestUtils.getRandomIntegerInRange(1, 127) & 0xFF, false, 3),
            Arguments.of(0x30,TestUtils.getRandomIntegerInRange(-127, -1) & 0xFF, true, 2),
            Arguments.of(0x30,TestUtils.getRandomIntegerInRange(1, 127) & 0xFF, false, 3),
            Arguments.of(0x30,TestUtils.getRandomIntegerInRange(-127, -1) & 0xFF, true, 2)
        );
    }

    @ParameterizedTest
    @MethodSource("generateJrTestArguments")
    void testJR(int opcode, int offset) {
        when(this.getCurrentBus().readByteFromAddress(anyInt())).thenReturn(
            opcode, //the opcode
            offset
            );
        
        Map<RegisterType, Integer> oldRegisterValues = this.getCpuRegisters();
        long previousCycleCount = getCurrentCpu().getCycles();
        this.getCurrentCpu().cpuStep();
        long currentCycleCount = getCurrentCpu().getCycles();        
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisters();

        //pc should increment by 2 (the size of the instruction) + the encoded offset
        oldRegisterValues.computeIfPresent(RegisterType.PC, (t, u) -> (u + 2 + (byte) offset)&0xFFFF);
        assertEquals(previousCycleCount+3, currentCycleCount);
        assertEquals(oldRegisterValues, newRegisterValues);

    }

    
    @ParameterizedTest
    @MethodSource("generateJrNzTestArguments")
    void testJR_NZ(int opcode, int offset, boolean zeroFlagStatus, int cycleCountOffset) {
        when(this.getCurrentBus().readByteFromAddress(anyInt())).thenReturn(
            opcode, //the opcode
            offset
            );
        runZeroFlagJumpTest(zeroFlagStatus, true, cycleCountOffset, offset);

    }

    @ParameterizedTest
    @MethodSource("generateJrZTestArguments")
    void testJR_Z(int opcode, int offset, boolean zeroFlagStatus, int cycleCountOffset) {
        when(this.getCurrentBus().readByteFromAddress(anyInt())).thenReturn(
            opcode, //the opcode
            offset
            );
        
        runZeroFlagJumpTest(zeroFlagStatus, false, cycleCountOffset, offset);

    }

    @ParameterizedTest
    @MethodSource("generateJrNcTestArguments")
    void testJR_NC(int opcode, int offset, boolean carryFlagStatus, int cycleCountOffset) {
        when(this.getCurrentBus().readByteFromAddress(anyInt())).thenReturn(
            opcode, //the opcode
            offset
            );
        
        runCarryFlagJumpTest(carryFlagStatus,true,cycleCountOffset,offset);

    }
    
    private void runZeroFlagJumpTest(boolean zeroFlagStatus, boolean jumpIfUnset, int cycleCountOffset, int offset) {
        this.getCurrentCpu().setZero(zeroFlagStatus);
        Map<RegisterType, Integer> oldRegisterValues = this.getCpuRegisters();
        long previousCycleCount = getCurrentCpu().getCycles();
        this.getCurrentCpu().cpuStep();
        long currentCycleCount = getCurrentCpu().getCycles();        
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisters();

        checkJumpCondition(jumpIfUnset?!zeroFlagStatus:zeroFlagStatus, cycleCountOffset, offset, oldRegisterValues, previousCycleCount, currentCycleCount,
                newRegisterValues);
    }

    private void runCarryFlagJumpTest(boolean carryFlagStatus, boolean jumpIfUnset, int cycleCountOffset, int offset) {
        this.getCurrentCpu().setCarry(carryFlagStatus);
        Map<RegisterType, Integer> oldRegisterValues = this.getCpuRegisters();
        long previousCycleCount = getCurrentCpu().getCycles();
        this.getCurrentCpu().cpuStep();
        long currentCycleCount = getCurrentCpu().getCycles();        
        Map<RegisterType, Integer> newRegisterValues = this.getCpuRegisters();

        checkJumpCondition(jumpIfUnset?!carryFlagStatus:carryFlagStatus, cycleCountOffset, offset, oldRegisterValues, previousCycleCount, currentCycleCount,
                newRegisterValues);
    }

    private void checkJumpCondition(boolean condition, int cycleCountOffset, int offset,
            Map<RegisterType, Integer> oldRegisterValues, long previousCycleCount, long currentCycleCount,
            Map<RegisterType, Integer> newRegisterValues) {
        //pc should increment by 2 (the size of the instruction) + the encoded offset
        if (condition) {
            oldRegisterValues.computeIfPresent(RegisterType.PC, (t, u) -> (u + 2  + (byte)offset)&0xFFFF);
        } else {
            oldRegisterValues.computeIfPresent(RegisterType.PC, (t, u) -> u + 2 );
        }
        assertEquals(previousCycleCount+cycleCountOffset, currentCycleCount);
        assertEquals(oldRegisterValues, newRegisterValues);
    }


}
