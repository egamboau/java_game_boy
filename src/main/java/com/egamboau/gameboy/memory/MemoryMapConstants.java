package com.egamboau.gameboy.memory;

public final class MemoryMapConstants {
    public static final int ROM_BANK_00_START = 0x0000;
    public static final int ROM_BANK_00_END = 0x3FFF;
    
    public static final int ROM_BANK_NN_START = 0x4000;
    public static final int ROM_BANK_NN_END = 0x7FFF;
    
    public static final int VRAM_START = 0x8000;
    public static final int VRAM_END = 0x9FFF;

    public static final int EXTERNAL_RAM_START = 0xA000;
    public static final int EXTERNAL_RAM_END = 0xBFFF;

    public static final int WRAM_0_START = 0xC000;
    public static final int WRAM_0_END = 0xCFFF;

    public static final int WRAM_1_START = 0xD000;
    public static final int WRAM_1_END = 0xDFFF;

    public static final int ECHO_RAM_START = 0xE000;
    public static final int ECHO_RAM_END = 0xFDFF;
    
    public static final int OAM_START = 0xFE00;
    public static final int OAM_END = 0xFE9F;

    public static final int NOT_USABLE_START = 0xFEA0;
    public static final int NOT_USABLE_END = 0xFEFF;

    public static final int IO_REGISTERS_START = 0xFF00;
    public static final int IO_REGISTERS_END = 0xFF7F;    

    public static final int HRAM_START = 0xFF80;
    public static final int HRAM_END = 0xFFFE;

    public static final int INTERRUPT_ENABLE_REGISTER = 0xFFFF;
}