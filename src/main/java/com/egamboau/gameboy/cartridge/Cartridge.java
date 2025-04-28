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

public class Cartridge {

    private String fileName;
    private byte[] romData;

    private final Logger LOGGER = LogManager.getLogger();

    public Cartridge(String fileName) throws IOException {
        this.fileName = fileName;
        Path filePath = Paths.get(fileName);
        this.romData = Files.readAllBytes(filePath);
    }

    /**
     * Get the Rom data 
     * @return the rom data
     */
    public byte[] getRomData() {
        return romData;
    }

    /**
     * Get the file name where this rom was read
     * @return the rom file.
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Get the entry point bytes for the rom
     * After displaying the logo, the built-in boot ROM jumps to the address $0100, which should then jump to the actual main program in the cartridge.
     * @return the entry point bytes for the rom
     */
    public byte[] getEntryPoint() {
        return Arrays.copyOfRange(getRomData(), 0x100, 0x104);
    }

    /**
     * Get the bitmap image that is displayed when the Game Boy is powered on. 
     * 
     * The way the pixels are encoded is as follows:
     * <ul>
     * <li> The bytes $0104—$011B encode the top half of the logo while the bytes $011C–$0133 encode the bottom half. </li>
     * <li> For each half, each nibble encodes 4 pixels (the MSB corresponds to the leftmost pixel, the LSB to the rightmost); a pixel is lit if the corresponding bit is set. </li>
     * <li> The 4-pixel “groups” are laid out top to bottom, left to right. </li>
     * <li> Finally, the monochrome models upscale the entire thing by a factor of 2 (leading to somewhat chunky pixels).. </li> 
     * </ul>
     * 
     * @return the raw bytes for the logo.
     */
    public byte[] getLogo() {
        return Arrays.copyOfRange(getRomData(), 0x104, 0x134);
    }
    /**
     * Get the title of the game in upper case ASCII. If the title is less than 16 characters long, the remaining bytes should be padded with $00s
     * @return the game title
     */
    public String getTitle() {
        return new String(Arrays.copyOfRange(getRomData(), 0x0134, 0x0143), StandardCharsets.US_ASCII);
    }

    /**
     * Get the CGB flag, used to decide whether to enable Color mode (“CGB Mode”) or to fall back to monochrome compatibility mode (“Non-CGB Mode”).
     * @return the valie for the CGB flag
     */
    public CGBValues getCGBFlag() {
        return CGBValues.fromByte(getRomData()[0x0143]);
    }

    /**
     * Gets the game's publisher. It is only meaningful if the Old licensee is exactly $33;
     * @return the new game publisher value
     */
    public NewLicensee getNewLicenseeCode() {
        OldLicensee oldLicensee = getOldLicensee();
        if(oldLicensee == OldLicensee.NEW_LICENSEE) {
            return NewLicensee.fromCode(new String(Arrays.copyOfRange(getRomData(), 0x0144, 0x0146)));
        } else {
            return NewLicensee.NOT_AVAILABLE;
        }
    }

    /**
     * Get the rom type, and what hardware was present on the cartridge
     * @return the hardware type
     */
    public Roms getRomType(){
        byte value = getRomData()[0x0147];
        return Roms.fromByte(value);
    }

    /**
     * Get the size of the ROM present
     * @return how much ROM is present on the cartridge
     */
    public RomSize getRomSize() {
        return RomSize.fromByte(getRomData()[0x0148]);
    }
    
    /**
     * Get the destination of the actual game
     * @return the destination on the ROM
     */
    public Destination getDestination() {
        return Destination.fromByte(getRomData()[0x14A]);
    }

    /**
     * Gets the old licensee code stored in the rom, indicating the actual publisher name
     * @return the old license code stored
     */
    public OldLicensee getOldLicensee() {
        return OldLicensee.fromByte(getRomData()[0x14A]);
    }

    /**
     * Return the byte for the checksum of the header.
     * @return
     */
    public byte getChecksum() {
        return getRomData()[0x14d];
    }

    /**
     * returns the 16-bit checksum simply computed as the sum of all the bytes of the cartridge ROM (except these two checksum bytes).
     * @return the checksum
     */
    public long getGlobalChecksum() {
        return new BigInteger(Arrays.copyOfRange(getRomData(), 0x014e, 0x0150)).longValue();
    }

    /**
     * Checks if the header is actually valid or not
     * @return true if the header section is valid.
     */
    public boolean isHeaderValid() {
        byte checksum = 0;
        for (int address = 0x0134; address <= 0x014C; address++) {
            checksum = (byte)(checksum - getRomData()[address] - 1);
        }
        return checksum == getChecksum();
    }

    /**
     * Read a byte from the memory on the cartdrige and returns it
     * @param address the address to read
     * @return the value of the given address.
     */
    public int readByteFromAddress(int address) {
        byte value = getRomData()[address];
        return (value & 0x00FF);
    }

    /**
     * Write the given byte to the address on the cart
     * @param address the destination address
     * @param value the value to be read.
     */
    public void writeByteToAddress(int address, int value) {
        //rom only has no write
        if (this.getRomType() == Roms.ROM_ONLY) {
            LOGGER.info("Current cart is ROM only, writes are not allowed");
            return;
        } else {
            getRomData()[address] = (byte) (value & 0xFF) ;
        }
        
    }
}
