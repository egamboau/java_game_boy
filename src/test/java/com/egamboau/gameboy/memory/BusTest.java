package com.egamboau.gameboy.memory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.egamboau.gameboy.cartridge.Cartridge;
import com.egamboau.test.TestUtils;

class BusTest {

    private Bus bus;
    private Cartridge mockCartridge;
    private byte[] cartMockData = new byte[MemoryMapConstants.ROM_BANK_NN_END + 1];

    @BeforeEach
    @SuppressWarnings("checkstyle:magicnumber")
    void setUp() {
        mockCartridge = mock(Cartridge.class);
        doAnswer(invocation -> cartMockData[(int) invocation.getArgument(0)] & 0xFF)
                .when(mockCartridge).readByteFromAddress(anyInt());
        doAnswer(invocation -> {
            cartMockData[(int) invocation.getArgument(0)] = (byte) ((int) invocation.getArgument(1) & 0xFF);
            return null;
        }).when(mockCartridge).writeByteToAddress(anyInt(), anyInt());
        bus = new Bus(mockCartridge);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("writableRanges")
    void testReadAndWriteRange(final String name, final int start, final int end) {
        for (int address = start; address <= end; address++) {
            int expectedValue = TestUtils.getRandomIntegerInRange(0, 0xFF);
            bus.writeByteToAddress(expectedValue, address);
            assertEquals(expectedValue, bus.readByteFromAddress(address));
        }
    }

    static Stream<Arguments> writableRanges() {
        return Stream.of(
                Arguments.of("ROM", MemoryMapConstants.ROM_BANK_00_START, MemoryMapConstants.ROM_BANK_NN_END),
                Arguments.of("VRAM", MemoryMapConstants.VRAM_START, MemoryMapConstants.VRAM_END),
                Arguments.of("external RAM", MemoryMapConstants.EXTERNAL_RAM_START, MemoryMapConstants.EXTERNAL_RAM_END),
                Arguments.of("WRAM 0", MemoryMapConstants.WRAM_0_START, MemoryMapConstants.WRAM_0_START),
                Arguments.of("WRAM 1", MemoryMapConstants.WRAM_1_START, MemoryMapConstants.WRAM_1_END),
                Arguments.of("echo RAM", MemoryMapConstants.ECHO_RAM_START, MemoryMapConstants.ECHO_RAM_END),
                Arguments.of("OAM", MemoryMapConstants.OAM_START, MemoryMapConstants.OAM_START),
                Arguments.of("I/O", MemoryMapConstants.IO_REGISTERS_START, MemoryMapConstants.IO_REGISTERS_START),
                Arguments.of("HRAM", MemoryMapConstants.HRAM_START, MemoryMapConstants.HRAM_END),
                Arguments.of("interrupt enable", MemoryMapConstants.INTERRUPT_ENABLE_REGISTER,
                        MemoryMapConstants.INTERRUPT_ENABLE_REGISTER));
    }

    @Test
    void testReadAndWriteByteFromNonUsableSection() {
        for (int address = MemoryMapConstants.NOT_USABLE_START; address <= MemoryMapConstants.NOT_USABLE_END; address++) {
            int currentAddress = address;
            UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class,
                    () -> bus.writeByteToAddress(0, currentAddress));
            assertTrue(exception.getMessage().contains("not implemented"));
        }
    }

    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    void testReadByteFromAddressUnsupportedAddress() {
        UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class,
                () -> bus.readByteFromAddress(0x10000));
        assertTrue(exception.getMessage().contains("not implemented"));
    }

    @Test
    void testReadByteFromAddressBelowMemoryMap() {
        UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class,
                () -> bus.readByteFromAddress(-1));
        assertTrue(exception.getMessage().contains("not implemented"));
    }
}
