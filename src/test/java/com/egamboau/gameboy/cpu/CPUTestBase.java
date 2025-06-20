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
}
