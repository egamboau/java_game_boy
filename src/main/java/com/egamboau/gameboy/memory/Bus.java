package com.egamboau.gameboy.memory;

import com.egamboau.gameboy.cartridge.Cartridge;

public class Bus {

    private Cartridge cartridge;
    
    private byte[] vram = new byte[MemoryMapConstants.VRAM_END - MemoryMapConstants.VRAM_START + 1];
    private byte[] external_ram = new byte[MemoryMapConstants.EXTERNAL_RAM_END - MemoryMapConstants.EXTERNAL_RAM_START + 1];
    private byte[] wram_0 = new byte[MemoryMapConstants.WRAM_0_END - MemoryMapConstants.WRAM_0_START + 1];
    private byte[] wram_1 = new byte[MemoryMapConstants.WRAM_1_END - MemoryMapConstants.WRAM_1_START + 1];
    private byte[] echo_ram = new byte[MemoryMapConstants.ECHO_RAM_END - MemoryMapConstants.ECHO_RAM_START + 1];
    private byte[] oam = new byte[MemoryMapConstants.OAM_END - MemoryMapConstants.OAM_START + 1];
    private byte[] io_register = new byte[MemoryMapConstants.IO_REGISTERS_END - MemoryMapConstants.IO_REGISTERS_START + 1];
    private byte[] hram = new byte[MemoryMapConstants.HRAM_END - MemoryMapConstants.HRAM_START + 1];
    private byte interrupt_enable = 0x00;

    /**
     * Construct a new Memory bus
     * @param cartridge the cartdrige that will be used.
     */
    public Bus(Cartridge cartridge) {
        this.cartridge = cartridge;
    }

    /**
     * Read one byte from the specified address 
     * @param address the address to read from
     * @return the byte stored in the address.
     */
    public int readByteFromAddress(int address) {
        //check what memory mapping we need, and read accordingly
        if (address >= MemoryMapConstants.ROM_BANK_00_START && address <= MemoryMapConstants.ROM_BANK_NN_END) {
            return this.cartridge.readByteFromAddress(address);
        }
        
        if (address >= MemoryMapConstants.VRAM_START && address <= MemoryMapConstants.VRAM_END) {
            return this.vram[address - MemoryMapConstants.VRAM_START] & 0x00FF;
        }
        
        if (address >= MemoryMapConstants.EXTERNAL_RAM_START && address <= MemoryMapConstants.EXTERNAL_RAM_END) {
            return this.external_ram[address - MemoryMapConstants.EXTERNAL_RAM_START] & 0x00FF;
        }

        if (address >= MemoryMapConstants.WRAM_0_START && address <= MemoryMapConstants.WRAM_0_START) {
            return this.wram_0[address - MemoryMapConstants.WRAM_0_START] & 0x00FF;
        }

        if (address >= MemoryMapConstants.WRAM_1_START && address <= MemoryMapConstants.WRAM_1_END) {
            return this.wram_1[address - MemoryMapConstants.WRAM_1_START] & 0x00FF;
        }

        if (address >= MemoryMapConstants.ECHO_RAM_START && address <= MemoryMapConstants.ECHO_RAM_END) {
            return this.echo_ram[address - MemoryMapConstants.ECHO_RAM_START] & 0x00FF;
        }

        if (address >= MemoryMapConstants.OAM_START && address <= MemoryMapConstants.OAM_END) {
            return this.oam[address - MemoryMapConstants.OAM_START] & 0x00FF;
        }

        if (address >= MemoryMapConstants.IO_REGISTERS_START && address <= MemoryMapConstants.IO_REGISTERS_END) {
            return this.io_register[address - MemoryMapConstants.IO_REGISTERS_START] & 0x00FF;
        }

        if (address >= MemoryMapConstants.HRAM_START && address <= MemoryMapConstants.HRAM_END) {
            return this.hram[address - MemoryMapConstants.HRAM_START] & 0x00FF;
        }

        if (address == MemoryMapConstants.INTERRUPT_ENABLE_REGISTER) {
            return this.interrupt_enable & 0x00FF;
        }

        throw new UnsupportedOperationException(String.format("Reading from address %x not implemented", address));

    }

    
    public void writeByteToAddress(int value, int address) {
        if (address >= MemoryMapConstants.ROM_BANK_00_START && address <= MemoryMapConstants.ROM_BANK_NN_END) {
            this.cartridge.writeByteToAddress(address, value);
            return;
        }

        if (address >= MemoryMapConstants.VRAM_START && address <= MemoryMapConstants.VRAM_END) {
            this.vram[address - MemoryMapConstants.VRAM_START] = (byte) (value & 0xFF);;
            return;
        }
        
        if (address >= MemoryMapConstants.EXTERNAL_RAM_START && address <= MemoryMapConstants.EXTERNAL_RAM_END) {
            this.external_ram[address - MemoryMapConstants.EXTERNAL_RAM_START] = (byte) (value & 0xFF);;
            return;
        }

        if (address >= MemoryMapConstants.WRAM_0_START && address <= MemoryMapConstants.WRAM_0_START) {
            this.wram_0[address - MemoryMapConstants.WRAM_0_START] = (byte) (value & 0xFF);;
            return;
        }

        if (address >= MemoryMapConstants.WRAM_1_START && address <= MemoryMapConstants.WRAM_1_END) {
            this.wram_1[address - MemoryMapConstants.WRAM_1_START] = (byte) (value & 0xFF);;
            return;
        }

        if (address >= MemoryMapConstants.ECHO_RAM_START && address <= MemoryMapConstants.ECHO_RAM_END) {
            this.echo_ram[address - MemoryMapConstants.ECHO_RAM_START] = (byte) (value & 0xFF);;
            return;
        }

        if (address >= MemoryMapConstants.OAM_START && address <= MemoryMapConstants.OAM_END) {
            this.oam[address - MemoryMapConstants.OAM_START] = (byte) (value & 0xFF);;
            return;
        }

        if (address >= MemoryMapConstants.IO_REGISTERS_START && address <= MemoryMapConstants.IO_REGISTERS_END) {
            this.io_register[address - MemoryMapConstants.IO_REGISTERS_START] = (byte) (value & 0xFF);;
            return;
        }

        if (address >= MemoryMapConstants.HRAM_START && address <= MemoryMapConstants.HRAM_END) {
            this.hram[address - MemoryMapConstants.HRAM_START] = (byte) (value & 0xFF);;
            return;
        }

        if (address == MemoryMapConstants.INTERRUPT_ENABLE_REGISTER) {
            this.interrupt_enable  = (byte) (value & 0xFF);;
            return;
        }

        throw new UnsupportedOperationException(String.format("Writting to address %x not implemented", address));
    }
}
