package com.egamboau.gameboy.cpu;

import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;

import com.egamboau.gameboy.cpu.instructions.RegisterType;
import com.egamboau.gameboy.memory.Bus;

public class CPUTest {

    protected CPU currentCpu = null;
    protected Bus currentBus = null;
    @BeforeEach
    void setUp() {
        currentBus = mock(Bus.class);
        currentCpu = new CPU(currentBus);
    }

    protected Map<RegisterType, Integer> getCpuRegisterValues(RegisterType... registerToFilter) {
        List<RegisterType> filterAsList = Arrays.asList(registerToFilter);
        return Arrays.stream(RegisterType.values())
            .filter(value -> !filterAsList.contains(value))
            .collect(Collectors.toMap(element -> element, element -> this.currentCpu.getValueFromRegister(element)));
    }
}
