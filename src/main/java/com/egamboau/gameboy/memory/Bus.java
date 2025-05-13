package com.egamboau.gameboy.memory;

import com.egamboau.gameboy.cartridge.Cartridge;

public class Bus {

    private Cartridge cartridge;
    
    private byte[] vram = new byte[MemoryMapConstants.VRAM_END - MemoryMapConstants.VRAM_START + 1];
    private byte[] externalRam = new byte[MemoryMapConstants.EXTERNAL_RAM_END - MemoryMapConstants.EXTERNAL_RAM_START + 1];
    private byte[] wram0 = new byte[MemoryMapConstants.WRAM_0_END - MemoryMapConstants.WRAM_0_START + 1];
    private byte[] wram1 = new byte[MemoryMapConstants.WRAM_1_END - MemoryMapConstants.WRAM_1_START + 1];
    private byte[] echoRam = new byte[MemoryMapConstants.ECHO_RAM_END - MemoryMapConstants.ECHO_RAM_START + 1];
    private byte[] oam = new byte[MemoryMapConstants.OAM_END - MemoryMapConstants.OAM_START + 1];
    private byte[] ioRegister = new byte[MemoryMapConstants.IO_REGISTERS_END - MemoryMapConstants.IO_REGISTERS_START + 1];
    private byte[] hram = new byte[MemoryMapConstants.HRAM_END - MemoryMapConstants.HRAM_START + 1];
    private int interruptEnableRegister = 0;

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
        if (isCartridgeMemory(address)) {
            return this.cartridge.readByteFromAddress(address);
        }
        
        if (isVramMemory(address)) {
            return this.vram[address - MemoryMapConstants.VRAM_START] & 0x00FF;
        }
        
        if (isExternalRamSection(address)) {
            return this.externalRam[address - MemoryMapConstants.EXTERNAL_RAM_START] & 0x00FF;
        }

        if (isWorkRam0Memory(address)) {
            return this.wram0[address - MemoryMapConstants.WRAM_0_START] & 0x00FF;
        }

        if (isWorkRam1Memory(address)) {
            return this.wram1[address - MemoryMapConstants.WRAM_1_START] & 0x00FF;
        }

        if (isEchoRamMemory(address)) {
            return this.echoRam[address - MemoryMapConstants.ECHO_RAM_START] & 0x00FF;
        }

        if (isOamMemory(address)) {
            return this.oam[address - MemoryMapConstants.OAM_START] & 0x00FF;
        }

        if (isIoRegisterMemory(address)) {
            return this.ioRegister[address - MemoryMapConstants.IO_REGISTERS_START] & 0x00FF;
        }

        if (isHramMemory(address)) {
            return this.hram[address - MemoryMapConstants.HRAM_START] & 0x00FF;
        }

        if (isInterruptEnableRegister(address)) {
            return this.interruptEnableRegister;
        }

        throw new UnsupportedOperationException(String.format("Reading from address %x not implemented", address));

    }

    private boolean isHramMemory(int address) {
        return address >= MemoryMapConstants.HRAM_START && address <= MemoryMapConstants.HRAM_END;
    }

    private boolean isIoRegisterMemory(int address) {
        return address >= MemoryMapConstants.IO_REGISTERS_START && address <= MemoryMapConstants.IO_REGISTERS_END;
    }

    private boolean isOamMemory(int address) {
        return address >= MemoryMapConstants.OAM_START && address <= MemoryMapConstants.OAM_END;
    }

    private boolean isEchoRamMemory(int address) {
        return address >= MemoryMapConstants.ECHO_RAM_START && address <= MemoryMapConstants.ECHO_RAM_END;
    }

    private boolean isWorkRam1Memory(int address) {
        return address >= MemoryMapConstants.WRAM_1_START && address <= MemoryMapConstants.WRAM_1_END;
    }

    private boolean isWorkRam0Memory(int address) {
        return address >= MemoryMapConstants.WRAM_0_START && address <= MemoryMapConstants.WRAM_0_START;
    }

    private boolean isExternalRamSection(int address) {
        return address >= MemoryMapConstants.EXTERNAL_RAM_START && address <= MemoryMapConstants.EXTERNAL_RAM_END;
    }

    private boolean isVramMemory(int address) {
        return address >= MemoryMapConstants.VRAM_START && address <= MemoryMapConstants.VRAM_END;
    }

    private boolean isCartridgeMemory(int address) {
        return address >= MemoryMapConstants.ROM_BANK_00_START && address <= MemoryMapConstants.ROM_BANK_NN_END;
    }

    private boolean isInterruptEnableRegister(int address) {
        return address == MemoryMapConstants.INTERRUPT_ENABLE_REGISTER;
    }



    
    public void writeByteToAddress(int value, int address) {
        if (isCartridgeMemory(address)) {
            this.cartridge.writeByteToAddress(address, value);
            return;
        }

        if (isVramMemory(address)) {
            this.vram[address - MemoryMapConstants.VRAM_START] = (byte) (value & 0xFF);
            return;
        }
        
        if (isExternalRamSection(address)) {
            this.externalRam[address - MemoryMapConstants.EXTERNAL_RAM_START] = (byte) (value & 0xFF);
            return;
        }

        if (isWorkRam0Memory(address)) {
            this.wram0[address - MemoryMapConstants.WRAM_0_START] = (byte) (value & 0xFF);
            return;
        }

        if (isWorkRam1Memory(address)) {
            this.wram1[address - MemoryMapConstants.WRAM_1_START] = (byte) (value & 0xFF);
            return;
        }

        if (isEchoRamMemory(address)) {
            this.echoRam[address - MemoryMapConstants.ECHO_RAM_START] = (byte) (value & 0xFF);
            return;
        }

        if (isOamMemory(address)) {
            this.oam[address - MemoryMapConstants.OAM_START] = (byte) (value & 0xFF);
            return;
        }

        if (isIoRegisterMemory(address)) {
            this.ioRegister[address - MemoryMapConstants.IO_REGISTERS_START] = (byte) (value & 0xFF);
            return;
        }

        if (isHramMemory(address)) {
            this.hram[address - MemoryMapConstants.HRAM_START] = (byte) (value & 0xFF);
            return;
        }

        if (isInterruptEnableRegister(address)) {
            this.interruptEnableRegister = value & 0xFF;
            return;
        }

        throw new UnsupportedOperationException(String.format("Writting to address %x not implemented", address));
    }
}
