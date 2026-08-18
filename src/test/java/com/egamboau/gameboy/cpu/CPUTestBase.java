package com.egamboau.gameboy.cpu;

import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;

import com.egamboau.gameboy.cpu.instructions.RegisterType;
import com.egamboau.gameboy.memory.Bus;

public class CPUTestBase {

    /**
     * A constant representing a 16-bit mask for integers.
     * This mask is used to ensure that only the lower 16 bits of an integer
     * are considered, effectively truncating any higher bits.
     */
    protected static final int MASK_INT_16_BIT = 0xFFFF;

    /**
     * Bitmask for isolating the lower 8 bits of an int value.
     *
     * Used to convert a signed Java byte or any integer into its
     * unsigned 0–255 representation
     */
    protected static final int MASK_INT_8_BIT = 0xFF;
    /**
     * The CPU instance used in the test environment.
     * This is initialized before each test execution.
     */
    private CPU currentCpu = null;

    /**
     * The Bus instance used in the test environment.
     * This is initialized before each test execution.
     */
    private Bus currentBus = null;

    /**
     * Sets up the test environment before each test execution.
     * Initializes a mock Bus instance and a CPU instance connected to the Bus.
     * This ensures that each test starts with a clean and isolated state.
     */
    @BeforeEach
    final void setUp() {
        currentBus = mock(Bus.class);
        currentCpu = new CPU(currentBus);
    }

    /**
     * Return a map of CPU registers to their current integer values, excluding any registers
     * supplied as arguments.
     *
     * The map contains an entry for each RegisterType returned by RegisterType.values()
     * except those provided in the varargs parameter. If no registers are supplied, the
     * returned map will contain all registers.
     *
     * @param registers zero or more register types to exclude from the returned map
     * @return a map from RegisterType to Integer representing the current value of each included register
     */
    protected final Map<RegisterType, Integer> getCpuRegisters(final RegisterType... registers) {
        List<RegisterType> filterAsList = Arrays.asList(registers);
        return Arrays.stream(RegisterType.values())
            .filter(value -> !filterAsList.contains(value))
            .collect(Collectors.toMap(
                element -> element,
                element -> this.currentCpu.getValueFromRegister(element)
            ));
    }

    /**
     * Retrieves the current Bus instance.
     *
     * @return the current Bus instance.
     */
    public Bus getCurrentBus() {
        return currentBus;
    }

    /**
     * Retrieves the current CPU instance.
     *
     * @return the current CPU instance.
     */
    protected CPU getCurrentCpu() {
        return currentCpu;
    }

    /**
     * Helper for tests to stub the opcode byte read from the bus when the CPU
     * fetches from the program counter. Uses a generic anyInt matcher so specific
     * address-based stubs can still be added in tests where needed.
     *
     * @param opcode the opcode byte to return
     */
    protected void stubOpcode(final int opcode) {
        // import is static in tests; avoid adding unused static imports here
        org.mockito.Mockito.when(this.getCurrentBus().readByteFromAddress(org.mockito.ArgumentMatchers.anyInt()))
            .thenReturn(opcode);
    }
}
