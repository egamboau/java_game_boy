package com.egamboau.gameboy.memory;

import com.egamboau.gameboy.cartridge.Cartridge;
import com.egamboau.test.TestUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class BusTest {

    private Bus bus;
    private Cartridge mockCartridge;

    private static final Logger LOGGER = LogManager.getLogger();
    private byte[] cartMockData = new byte[MemoryMapConstants.ROM_BANK_NN_END - MemoryMapConstants.ROM_BANK_00_START  + 1];

    @BeforeEach
    void setUp() {
        mockCartridge = mock(Cartridge.class);
        doAnswer(invocation -> {
            return cartMockData[(int)invocation.getArgument(0)] & 0x00FF;
        }).when(mockCartridge).readByteFromAddress(anyInt());


        doAnswer(invocation -> {
            Object address = invocation.getArgument(0);
            Object value = invocation.getArgument(1);
            cartMockData[(int)address] = (byte)((int)value & 0xFF);
            return null;
        }).when(mockCartridge).writeByteToAddress(anyInt(), anyInt());
        bus = new Bus(mockCartridge);
    }

    @Test
    void testReadAndWriteByteFromRomBank() {
        for (int address = 0; address <= MemoryMapConstants.ROM_BANK_NN_END; address++) {
            LOGGER.info("Testing write to address {} on the memory bus", address);
            int expectedValue = TestUtils.getRandomIntegerInRange(0x00, 0xFF);
            bus.writeByteToAddress(expectedValue, address);
            LOGGER.info("Testing read to address {} on the memory bus", address);
            int readValue = bus.readByteFromAddress(address);
            assertEquals(expectedValue, readValue);
        }
    }

    @Test
    void testReadAndWriteByteFromRomVRAM() {
        for (int address = MemoryMapConstants.VRAM_START; address <= MemoryMapConstants.VRAM_END; address++) {
            LOGGER.info("Testing write to address {} on the memory bus", address);
            int expectedValue = TestUtils.getRandomIntegerInRange(0x00, 0xFF);
            bus.writeByteToAddress(expectedValue, address);
            LOGGER.info("Testing read to address {} on the memory bus", address);
            int readValue = bus.readByteFromAddress(address);
            assertEquals(expectedValue, readValue);
        }
    }

    @Test
    void testReadAndWriteByteFromExternalRam() {
        for (int address = MemoryMapConstants.EXTERNAL_RAM_START; address <= MemoryMapConstants.EXTERNAL_RAM_END; address++) {
            LOGGER.info("Testing write to address {} on the memory bus", address);
            int expectedValue = TestUtils.getRandomIntegerInRange(0x00, 0xFF);
            bus.writeByteToAddress(expectedValue, address);
            LOGGER.info("Testing read to address {} on the memory bus", address);
            int readValue = bus.readByteFromAddress(address);
            assertEquals(expectedValue, readValue);
        }
    }

    @Test
    void testReadAndWriteByteFromRomWRAM0() {
        for (int address = MemoryMapConstants.WRAM_0_START; address <= MemoryMapConstants.WRAM_0_START; address++) {
            LOGGER.info("Testing write to address {} on the memory bus", address);
            int expectedValue = TestUtils.getRandomIntegerInRange(0x00, 0xFF);
            bus.writeByteToAddress(expectedValue, address);
            LOGGER.info("Testing read to address {} on the memory bus", address);
            int readValue = bus.readByteFromAddress(address);
            assertEquals(expectedValue, readValue);
        }
    }

    @Test
    void testReadAndWriteByteFromRomWRAM1() {
        for (int address = MemoryMapConstants.WRAM_1_START; address <= MemoryMapConstants.WRAM_1_END; address++) {
            LOGGER.info("Testing write to address {} on the memory bus", address);
            int expectedValue = TestUtils.getRandomIntegerInRange(0x00, 0xFF);
            bus.writeByteToAddress(expectedValue, address);
            LOGGER.info("Testing read to address {} on the memory bus", address);
            int readValue = bus.readByteFromAddress(address);
            assertEquals(expectedValue, readValue);
        }
    }

    @Test
    void testReadAndWriteByteFromEchoRam() {
        for (int address = MemoryMapConstants.ECHO_RAM_START; address <= MemoryMapConstants.ECHO_RAM_END; address++) {
            LOGGER.info("Testing write to address {} on the memory bus", address);
            int expectedValue = TestUtils.getRandomIntegerInRange(0x00, 0xFF);
            bus.writeByteToAddress(expectedValue, address);
            LOGGER.info("Testing read to address {} on the memory bus", address);
            int readValue = bus.readByteFromAddress(address);
            assertEquals(expectedValue, readValue);
        }
    }

    @Test
    void testReadAndWriteByteFromOAM() {
        for (int address = MemoryMapConstants.OAM_START; address <= MemoryMapConstants.OAM_START; address++) {
            LOGGER.info("Testing write to address {} on the memory bus", address);
            int expectedValue = TestUtils.getRandomIntegerInRange(0x00, 0xFF);
            bus.writeByteToAddress(expectedValue, address);
            LOGGER.info("Testing read to address {} on the memory bus", address);
            int readValue = bus.readByteFromAddress(address);
            assertEquals(expectedValue, readValue);
        }
    }

    @Test
    void testReadAndWriteByteFromIORegisters() {
        for (int address = MemoryMapConstants.IO_REGISTERS_START; address <= MemoryMapConstants.IO_REGISTERS_START; address++) {
            LOGGER.info("Testing write to address {} on the memory bus", address);
            int expectedValue = TestUtils.getRandomIntegerInRange(0x00, 0xFF);
            bus.writeByteToAddress(expectedValue, address);
            LOGGER.info("Testing read to address {} on the memory bus", address);
            int readValue = bus.readByteFromAddress(address);
            assertEquals(expectedValue, readValue);
        }
    }

    @Test
    void testReadAndWriteByteFromHRAM() {
        for (int address = MemoryMapConstants.HRAM_START; address <= MemoryMapConstants.HRAM_END; address++) {
            LOGGER.info("Testing write to address {} on the memory bus", address);
            int expectedValue = TestUtils.getRandomIntegerInRange(0x00, 0xFF);
            bus.writeByteToAddress(expectedValue, address);
            LOGGER.info("Testing read to address {} on the memory bus", address);
            int readValue = bus.readByteFromAddress(address);
            assertEquals(expectedValue, readValue);
        }
    }

    @Test
    void testReadAndWriteByteFromInterruptHandler() {
        LOGGER.info("Testing write to address {} on the memory bus", MemoryMapConstants.INTERRUPT_ENABLE_REGISTER);
        int expectedValue = TestUtils.getRandomIntegerInRange(0x00, 0xFF);
        bus.writeByteToAddress(expectedValue, MemoryMapConstants.INTERRUPT_ENABLE_REGISTER);
        LOGGER.info("Testing read to address {} on the memory bus", MemoryMapConstants.INTERRUPT_ENABLE_REGISTER);
        int readValue = bus.readByteFromAddress(MemoryMapConstants.INTERRUPT_ENABLE_REGISTER);
        assertEquals(expectedValue, readValue);
    }

    @Test
    void testReadAndWriteByteFromNonUsableSection() {
        for (int address = MemoryMapConstants.NOT_USABLE_START; address <= MemoryMapConstants.NOT_USABLE_END; address++) {
            final int currentAddress = address;
            LOGGER.info("Testing write to address {} on the memory bus", currentAddress);
            int expectedValue = TestUtils.getRandomIntegerInRange(0x00, 0xFF);
            Exception exception = assertThrows(UnsupportedOperationException.class, () -> {
                bus.writeByteToAddress(expectedValue, currentAddress);
            });
    
            assertTrue(exception.getMessage().contains("not implemented"));
        }
    }

    @Test
    void testReadByteFromAddress_UnsupportedAddress() {
        int address = 0xFFFF + 1; // Invalid address

        Exception exception = assertThrows(UnsupportedOperationException.class, () -> {
            bus.readByteFromAddress(address);
        });

        assertTrue(exception.getMessage().contains("not implemented"));
    }
}