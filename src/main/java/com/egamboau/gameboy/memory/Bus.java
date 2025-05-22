package com.egamboau.gameboy.memory;

import com.egamboau.gameboy.cartridge.Cartridge;

/**
 * The Bus class represents the memory bus of the Game Boy, which facilitates
 * communication between the CPU, cartridge, and various memory regions.
 * It handles reading and writing operations to different memory sections
 * based on the Game Boy's memory map.
 */
public class Bus {

    /**
     * The cartridge currently loaded into the Game Boy.
     */
    private Cartridge cartridge;

    /**
     * Video RAM (VRAM) memory region.
     */
    private byte[] vram = new byte[MemoryMapConstants.VRAM_END - MemoryMapConstants.VRAM_START + 1];

    /**
     * External RAM memory region.
     */
    private byte[] externalRam = new byte[MemoryMapConstants.EXTERNAL_RAM_END - MemoryMapConstants.EXTERNAL_RAM_START + 1];

    /**
     * Work RAM bank 0 (WRAM0) memory region.
     */
    private byte[] wram0 = new byte[MemoryMapConstants.WRAM_0_END - MemoryMapConstants.WRAM_0_START + 1];

    /**
     * Work RAM bank 1 (WRAM1) memory region.
     */
    private byte[] wram1 = new byte[MemoryMapConstants.WRAM_1_END - MemoryMapConstants.WRAM_1_START + 1];

    /**
     * Echo RAM memory region (mirrors WRAM).
     */
    private byte[] echoRam = new byte[MemoryMapConstants.ECHO_RAM_END - MemoryMapConstants.ECHO_RAM_START + 1];

    /**
     * Object Attribute Memory (OAM) region.
     */
    private byte[] oam = new byte[MemoryMapConstants.OAM_END - MemoryMapConstants.OAM_START + 1];

    /**
     * I/O Registers memory region.
     */
    private byte[] ioRegister = new byte[MemoryMapConstants.IO_REGISTERS_END - MemoryMapConstants.IO_REGISTERS_START + 1];

    /**
     * High RAM (HRAM) memory region.
     */
    private byte[] hram = new byte[MemoryMapConstants.HRAM_END - MemoryMapConstants.HRAM_START + 1];

    /**
     * Interrupt Enable Register (IE).
     */
    private int interruptEnableRegister = 0;

    /**
     * Constructs a new Memory Bus.
     *
     * @param currentCartridge The cartridge that will be used.
     */
    public Bus(final Cartridge currentCartridge) {
        this.cartridge = currentCartridge;
    }

    /**
     * Reads one byte from the specified memory address.
     *
     * @param address The address to read from.
     * @return The byte stored at the specified address.
     * @throws UnsupportedOperationException If the address is not implemented.
     */
    public int readByteFromAddress(final int address) {
        if (isCartridgeMemory(address)) {
            return this.cartridge.readByteFromAddress(address);
        }

        if (isVramMemory(address)) {
            return this.vram[address - MemoryMapConstants.VRAM_START] & BitMasks.MASK_8_BIT_DATA;
        }

        if (isExternalRamSection(address)) {
            return this.externalRam[address - MemoryMapConstants.EXTERNAL_RAM_START] & BitMasks.MASK_8_BIT_DATA;
        }

        if (isWorkRam0Memory(address)) {
            return this.wram0[address - MemoryMapConstants.WRAM_0_START] & BitMasks.MASK_8_BIT_DATA;
        }

        if (isWorkRam1Memory(address)) {
            return this.wram1[address - MemoryMapConstants.WRAM_1_START] & BitMasks.MASK_8_BIT_DATA;
        }

        if (isEchoRamMemory(address)) {
            return this.echoRam[address - MemoryMapConstants.ECHO_RAM_START] & BitMasks.MASK_8_BIT_DATA;
        }

        if (isOamMemory(address)) {
            return this.oam[address - MemoryMapConstants.OAM_START] & BitMasks.MASK_8_BIT_DATA;
        }

        if (isIoRegisterMemory(address)) {
            return this.ioRegister[address - MemoryMapConstants.IO_REGISTERS_START] & BitMasks.MASK_8_BIT_DATA;
        }

        if (isHramMemory(address)) {
            return this.hram[address - MemoryMapConstants.HRAM_START] & BitMasks.MASK_8_BIT_DATA;
        }

        if (isInterruptEnableRegister(address)) {
            return this.interruptEnableRegister;
        }

        throw new UnsupportedOperationException(String.format("Reading from address %x not implemented", address));
    }

    /**
     * Writes one byte to the specified memory address.
     *
     * @param value   The value to write.
     * @param address The address to write to.
     * @throws UnsupportedOperationException If the address is not implemented.
     */
    public final void writeByteToAddress(final int value, final int address) {
        if (isCartridgeMemory(address)) {
            this.cartridge.writeByteToAddress(address, value);
            return;
        }

        if (isVramMemory(address)) {
            this.vram[address - MemoryMapConstants.VRAM_START] = (byte) (value & BitMasks.MASK_8_BIT_DATA);
            return;
        }

        if (isExternalRamSection(address)) {
            this.externalRam[address - MemoryMapConstants.EXTERNAL_RAM_START] = (byte) (value & BitMasks.MASK_8_BIT_DATA);
            return;
        }

        if (isWorkRam0Memory(address)) {
            this.wram0[address - MemoryMapConstants.WRAM_0_START] = (byte) (value & BitMasks.MASK_8_BIT_DATA);
            return;
        }

        if (isWorkRam1Memory(address)) {
            this.wram1[address - MemoryMapConstants.WRAM_1_START] = (byte) (value & BitMasks.MASK_8_BIT_DATA);
            return;
        }

        if (isEchoRamMemory(address)) {
            this.echoRam[address - MemoryMapConstants.ECHO_RAM_START] = (byte) (value & BitMasks.MASK_8_BIT_DATA);
            return;
        }

        if (isOamMemory(address)) {
            this.oam[address - MemoryMapConstants.OAM_START] = (byte) (value & BitMasks.MASK_8_BIT_DATA);
            return;
        }

        if (isIoRegisterMemory(address)) {
            this.ioRegister[address - MemoryMapConstants.IO_REGISTERS_START] = (byte) (value & BitMasks.MASK_8_BIT_DATA);
            return;
        }

        if (isHramMemory(address)) {
            this.hram[address - MemoryMapConstants.HRAM_START] = (byte) (value & BitMasks.MASK_8_BIT_DATA);
            return;
        }

        if (isInterruptEnableRegister(address)) {
            this.interruptEnableRegister = value & BitMasks.MASK_8_BIT_DATA;
            return;
        }

        throw new UnsupportedOperationException(String.format("Writing to address %x not implemented", address));
    }

    // Private helper methods to check memory regions

    private boolean isHramMemory(final int address) {
        return address >= MemoryMapConstants.HRAM_START && address <= MemoryMapConstants.HRAM_END;
    }

    private boolean isIoRegisterMemory(final int address) {
        return address >= MemoryMapConstants.IO_REGISTERS_START && address <= MemoryMapConstants.IO_REGISTERS_END;
    }

    private boolean isOamMemory(final int address) {
        return address >= MemoryMapConstants.OAM_START && address <= MemoryMapConstants.OAM_END;
    }

    private boolean isEchoRamMemory(final int address) {
        return address >= MemoryMapConstants.ECHO_RAM_START && address <= MemoryMapConstants.ECHO_RAM_END;
    }

    private boolean isWorkRam1Memory(final int address) {
        return address >= MemoryMapConstants.WRAM_1_START && address <= MemoryMapConstants.WRAM_1_END;
    }

    private boolean isWorkRam0Memory(final int address) {
        return address >= MemoryMapConstants.WRAM_0_START && address <= MemoryMapConstants.WRAM_0_END;
    }

    private boolean isExternalRamSection(final int address) {
        return address >= MemoryMapConstants.EXTERNAL_RAM_START && address <= MemoryMapConstants.EXTERNAL_RAM_END;
    }

    private boolean isVramMemory(final int address) {
        return address >= MemoryMapConstants.VRAM_START && address <= MemoryMapConstants.VRAM_END;
    }

    private boolean isCartridgeMemory(final int address) {
        return address >= MemoryMapConstants.ROM_BANK_00_START && address <= MemoryMapConstants.ROM_BANK_NN_END;
    }

    private boolean isInterruptEnableRegister(final int address) {
        return address == MemoryMapConstants.INTERRUPT_ENABLE_REGISTER;
    }
}
