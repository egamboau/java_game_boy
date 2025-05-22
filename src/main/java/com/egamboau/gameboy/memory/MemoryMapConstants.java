package com.egamboau.gameboy.memory;

/**
 * This class defines constants representing the memory map of the Game Boy.
 * The memory map specifies the address ranges for various memory regions,
 * including ROM, RAM, VRAM, and special-purpose registers.
 *
 * <p>These constants are used throughout the emulator to identify and
 * interact with specific memory regions.</p>
 *
 * <h2>Memory Map Overview:</h2>
 * <ul>
 *   <li><b>ROM Bank 00:</b> 0x0000 - 0x3FFF (Fixed ROM bank from the cartridge).</li>
 *   <li><b>ROM Bank NN:</b> 0x4000 - 0x7FFF (Switchable ROM bank from the cartridge).</li>
 *   <li><b>VRAM:</b> 0x8000 - 0x9FFF (Video RAM, used for graphics).</li>
 *   <li><b>External RAM:</b> 0xA000 - 0xBFFF (Cartridge RAM, if available).</li>
 *   <li><b>WRAM:</b> 0xC000 - 0xDFFF (Work RAM, split into two banks).</li>
 *   <li><b>Echo RAM:</b> 0xE000 - 0xFDFF (Mirror of WRAM, not recommended for use).</li>
 *   <li><b>OAM:</b> 0xFE00 - 0xFE9F (Object Attribute Memory, used for sprites).</li>
 *   <li><b>I/O Registers:</b> 0xFF00 - 0xFF7F (Special-purpose registers for I/O).</li>
 *   <li><b>HRAM:</b> 0xFF80 - 0xFFFE (High RAM, fast-access memory).</li>
 *   <li><b>Interrupt Enable Register:</b> 0xFFFF (Controls interrupt handling).</li>
 * </ul>
 */
public final class MemoryMapConstants {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private MemoryMapConstants() {
    }

    /**
     * Start address of ROM Bank 00.
     */
    public static final int ROM_BANK_00_START = 0x0000;

    /**
     * End address of ROM Bank 00.
     */
    public static final int ROM_BANK_00_END = 0x3FFF;

    /**
     * Start address of ROM Bank NN (switchable bank).
     */
    public static final int ROM_BANK_NN_START = 0x4000;

    /**
     * End address of ROM Bank NN (switchable bank).
     */
    public static final int ROM_BANK_NN_END = 0x7FFF;

    /**
     * Start address of Video RAM (VRAM).
     */
    public static final int VRAM_START = 0x8000;

    /**
     * End address of Video RAM (VRAM).
     */
    public static final int VRAM_END = 0x9FFF;

    /**
     * Start address of External RAM.
     */
    public static final int EXTERNAL_RAM_START = 0xA000;

    /**
     * End address of External RAM.
     */
    public static final int EXTERNAL_RAM_END = 0xBFFF;

    /**
     * Start address of Work RAM Bank 0 (WRAM0).
     */
    public static final int WRAM_0_START = 0xC000;

    /**
     * End address of Work RAM Bank 0 (WRAM0).
     */
    public static final int WRAM_0_END = 0xCFFF;

    /**
     * Start address of Work RAM Bank 1 (WRAM1).
     */
    public static final int WRAM_1_START = 0xD000;

    /**
     * End address of Work RAM Bank 1 (WRAM1).
     */
    public static final int WRAM_1_END = 0xDFFF;

    /**
     * Start address of Echo RAM (mirror of WRAM).
     */
    public static final int ECHO_RAM_START = 0xE000;

    /**
     * End address of Echo RAM (mirror of WRAM).
     */
    public static final int ECHO_RAM_END = 0xFDFF;

    /**
     * Start address of Object Attribute Memory (OAM).
     */
    public static final int OAM_START = 0xFE00;

    /**
     * End address of Object Attribute Memory (OAM).
     */
    public static final int OAM_END = 0xFE9F;

    /**
     * Start address of the unusable memory region.
     */
    public static final int NOT_USABLE_START = 0xFEA0;

    /**
     * End address of the unusable memory region.
     */
    public static final int NOT_USABLE_END = 0xFEFF;

    /**
     * Start address of I/O Registers.
     */
    public static final int IO_REGISTERS_START = 0xFF00;

    /**
     * End address of I/O Registers.
     */
    public static final int IO_REGISTERS_END = 0xFF7F;

    /**
     * Start address of High RAM (HRAM).
     */
    public static final int HRAM_START = 0xFF80;

    /**
     * End address of High RAM (HRAM).
     */
    public static final int HRAM_END = 0xFFFE;

    /**
     * Address of the Interrupt Enable Register.
     */
    public static final int INTERRUPT_ENABLE_REGISTER = 0xFFFF;
}
