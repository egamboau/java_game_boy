package com.egamboau.gameboy.cartridge;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.egamboau.gameboy.memory.BitMasks;

public class Cartridge {

    /**
     * The ending address of the global checksum section in the cartridge header.
     */
    private static final int GLOBAL_CHECKSUM_SECTION_END = 0x014C;

    /**
     * The ending address of the global checksum in the cartridge header.
     */
    private static final int GLOBAL_CHECKSUM_LOCATION_END = 0x0150;

    /**
     * The starting address of the global checksum in the cartridge header.
     */
    private static final int GLOBAL_CHECKSUM_LOCATION_START = 0x014e;

    /**
     * The memory location of the checksum in the cartridge header.
     */
    private static final int CHECKSUM_LOCATION = 0x14d;

    /**
     * The memory location of the old licensee code in the cartridge.
     */
    private static final int OLD_LICENSEE_CODE_LOCATION = 0x14A;

    /**
     * The memory location of the destination code in the cartridge.
     */
    private static final int DESTINATION_LOCATION = 0x14A;

    /**
     * The memory location of the ROM size in the cartridge.
     */
    private static final int ROM_SIZE_LOCATION = 0x0148;

    /**
     * The memory location of the ROM type in the cartridge.
     */
    private static final int ROM_TYPE_LOCATION = 0x0147;

    /**
     * The ending address of the new licensee code in the ROM.
     */
    private static final int NEW_LICENSEE_CODE_LOCATION_END = 0x0146;

    /**
     * The starting address of the new licensee code in the ROM.
     */
    private static final int NEW_LICENSEE_CODE_LOCATION_START = 0x0144;

    /**
     * The memory location of the CGB (Color Game Boy) flag in the ROM.
     */
    private static final int CGB_FLAG_LOCATION = 0x0143;

    /**
     * The starting address of the title section in the ROM.
     */
    private static final int TITLE_SECTION_START = 0x0134;

    /**
     * The ending address of the title section in the ROM.
     */
    private static final int TITLE_SECTION_END = 0x0143;
    /**
     * The starting address of the logo section in the ROM.
     */
    private static final int LOGO_SECTION_START = 0x104;

    /**
     * The ending address of the logo section in the ROM.
     */
    private static final int LOGO_SECTION_END = 0x134;

    /**
     * The starting address of the entry point in the ROM.
     */
    private static final int ENTRY_POINT_START = 0x100;

    /**
     * The ending address of the entry point in the ROM.
     */
    private static final int ENTRY_POINT_END = 0x104;

    /**
     * The name of the file associated with the cartridge.
     */
    private String fileName;

    /**
     * The ROM data loaded from the cartridge file.
     */
    private byte[] romData;

    /**
     * Logger instance for logging cartridge-related information.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Constructs a Cartridge object by loading ROM data from the specified file path.
     *
     * @param cartridgeFilePath the path to the cartridge file
     * @throws IOException if an I/O error occurs while reading the file
     */
    public Cartridge(final String cartridgeFilePath) throws IOException {
        this.fileName = cartridgeFilePath;
        Path filePath = Paths.get(fileName);
        this.romData = Files.readAllBytes(filePath);
    }

    /**
     * Get the Rom data.
     *
     * @return the rom data
     */
    public byte[] getRomData() {
        return romData;
    }

    /**
     * Get the file name where this rom was read.
     *
     * @return the rom file.
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Get the entry point bytes for the rom
     * After displaying the logo, the built-in boot ROM jumps to the address $0100,
     * which should then jump to the actual main program in the cartridge.
     *
     * @return the entry point bytes for the rom
     */
    public byte[] getEntryPoint() {
        return Arrays.copyOfRange(getRomData(), ENTRY_POINT_START, ENTRY_POINT_END);
    }

    /**
     * Get the bitmap image that is displayed when the Game Boy is powered on.
     *
     * The way the pixels are encoded is as follows:
     * <ul>
     * <li>The bytes $0104—$011B encode the top half of the logo while the bytes
     * $011C–$0133 encode the bottom half.</li>
     * <li>For each half, each nibble encodes 4 pixels (the MSB corresponds to the
     * leftmost pixel, the LSB to the rightmost); a pixel is lit if the
     * corresponding bit is set.</li>
     * <li>The 4-pixel “groups” are laid out top to bottom, left to right.</li>
     * <li>Finally, the monochrome models upscale the entire thing by a factor of 2
     * (leading to somewhat chunky pixels)..</li>
     * </ul>
     *
     * @return the raw bytes for the logo.
     */
    public byte[] getLogo() {
        return Arrays.copyOfRange(getRomData(), LOGO_SECTION_START, LOGO_SECTION_END);
    }

    /**
     * Get the title of the game in upper case ASCII. If the title is less than 16
     * characters long, the remaining bytes should be padded with $00s
     *
     * @return the game title
     */
    public String getTitle() {
        return new String(
            Arrays.copyOfRange(getRomData(), TITLE_SECTION_START, TITLE_SECTION_END),
            StandardCharsets.US_ASCII);
    }

    /**
     * Get the CGB flag, used to decide whether to enable Color mode (“CGB Mode”) or
     * to fall back to monochrome compatibility mode (“Non-CGB Mode”).
     *
     * @return the valie for the CGB flag
     */
    public CGBValues getCGBFlag() {
        return CGBValues.fromByte(getRomData()[CGB_FLAG_LOCATION]);
    }

    /**
     * Gets the game's publisher. It is only meaningful if the Old licensee is
     * exactly $33;
     *
     * @return the new game publisher value
     */
    public NewLicensee getNewLicenseeCode() {
        OldLicensee oldLicensee = getOldLicensee();
        if (oldLicensee == OldLicensee.NEW_LICENSEE) {
            return NewLicensee.fromCode(
                new String(Arrays.copyOfRange(
                    getRomData(),
                    NEW_LICENSEE_CODE_LOCATION_START,
                    NEW_LICENSEE_CODE_LOCATION_END)));
        } else {
            return NewLicensee.NOT_AVAILABLE;
        }
    }

    /**
     * Get the rom type, and what hardware was present on the cartridge.
     *
     * @return the hardware type
     */
    public Roms getRomType() {
        byte value = getRomData()[ROM_TYPE_LOCATION];
        return Roms.fromByte(value);
    }

    /**
     * Get the size of the ROM present.
     *
     * @return how much ROM is present on the cartridge
     */
    public RomSize getRomSize() {
        return RomSize.fromByte(getRomData()[ROM_SIZE_LOCATION]);
    }

    /**
     * Get the destination of the actual game.
     *
     * @return the destination on the ROM
     */
    public Destination getDestination() {
        return Destination.fromByte(getRomData()[DESTINATION_LOCATION]);
    }

    /**
     * Gets the old licensee code stored in the rom, indicating the actual publisher.
     * name
     *
     * @return the old license code stored
     */
    public OldLicensee getOldLicensee() {
        return OldLicensee.fromByte(getRomData()[OLD_LICENSEE_CODE_LOCATION]);
    }

    /**
     * Return the byte for the checksum of the header.
     *
     * @return the data on the checksum location.
     */
    public byte getChecksum() {
        return getRomData()[CHECKSUM_LOCATION];
    }

    /**
     * returns the 16-bit checksum simply computed as the sum of all the bytes of
     * the cartridge ROM (except these two checksum bytes).
     *
     * @return the checksum
     */
    public long getGlobalChecksum() {
        return new BigInteger(
            Arrays.copyOfRange(
                getRomData(),
                GLOBAL_CHECKSUM_LOCATION_START,
                 GLOBAL_CHECKSUM_LOCATION_END)).longValue();
    }

    /**
     * Checks if the header is actually valid or not.
     *
     * @return true if the header section is valid.
     */
    public boolean isHeaderValid() {
        byte checksum = 0;
        for (int address = TITLE_SECTION_START; address <= GLOBAL_CHECKSUM_SECTION_END; address++) {
            checksum = (byte) (checksum - getRomData()[address] - 1);
        }
        return checksum == getChecksum();
    }

    /**
     * Read a byte from the memory on the cartdrige and returns it.
     *
     * @param address the address to read
     * @return the value of the given address.
     */
    public int readByteFromAddress(final int address) {
        byte value = getRomData()[address];
        return (value & BitMasks.MASK_8_BIT_DATA);
    }

    /**
     * Write the given byte to the address on the cart.
     *
     * @param address the destination address
     * @param value   the value to be read.
     */
    public void writeByteToAddress(final int address, final int value) {
        //rom only has no write
        if (this.getRomType() == Roms.ROM_ONLY) {
            LOGGER.info("Current cart is ROM only, writes are not allowed");
            return;
        } else {
            getRomData()[address] = (byte) (value & BitMasks.MASK_8_BIT_DATA);
        }
    }
}
