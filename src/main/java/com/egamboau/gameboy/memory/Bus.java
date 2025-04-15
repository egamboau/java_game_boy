package com.egamboau.gameboy.memory;

import com.egamboau.gameboy.cartridge.Cartridge;

public class Bus {

    Cartridge cartridge;
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
    public byte readByteFromAddress(int address) {
        //check what memory mapping we need, and read accordingly
        if (address >= MemoryMapConstants.ROM_BANK_00_START && address <= MemoryMapConstants.ROM_BANK_NN_END) {
            return this.cartridge.readByteFromAddress(address);
        }

        throw new UnsupportedOperationException(String.format("Reading from address %i not implemented", address));
    }

    
    public void writeByteToAddress(byte value, int address) {
        if (address >= MemoryMapConstants.ROM_BANK_00_START && address <= MemoryMapConstants.ROM_BANK_NN_END) {
            this.cartridge.writeByteToAddress(address, value);
        }

        throw new UnsupportedOperationException(String.format("Writting from address %i not implemented", address));
    }
}
