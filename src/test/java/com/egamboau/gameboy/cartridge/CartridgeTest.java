package com.egamboau.gameboy.cartridge;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.egamboau.test.TestUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CartridgeTest {

    /**
     * The Cartridge instance used for testing.
     */
    private static Cartridge cartridge;

    /**
     * Temporary file to store the test ROM.
     */
    private static File tempFile;

    /**
     * Byte array to hold the ROM data for testing.
     */
    private static byte[] data;

    @BeforeAll
    @SuppressWarnings("checkstyle:magicnumber")
    static void setUp() throws IOException {
        data = new byte[0x150];
        data[0x143] = (byte) 0x80;
        data[0x147] = 0x02;

        byte checksum = 0;
        for (int address = 0x134; address <= 0x14C; address++) {
            checksum = (byte) (checksum - data[address] - 1);
        }
        data[0x14D] = checksum;

        tempFile = Files.createTempFile("test-rom", ".gb").toFile();
        Files.write(tempFile.toPath(), data);
    }

    @BeforeEach
    void setUpEach() throws IOException {
        cartridge = new Cartridge(tempFile.getAbsolutePath());
    }


    @AfterAll
    static void tearDown() {
        tempFile.delete();
    }

    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    void testGetRomData() throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(data);
        String expected = Base64.getEncoder().encodeToString(md.digest());
        md.reset();
        md.update(cartridge.getRomData());
        String loaded = Base64.getEncoder().encodeToString(md.digest());
        assertEquals(expected, loaded);
    }

    @Test
    void testGetFileName() {
        assertEquals(tempFile.getAbsolutePath(), cartridge.getFileName());
    }

    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    void testGetEntryPoint() {
        assertArrayEquals(Arrays.copyOfRange(data, 0x100, 0x104), cartridge.getEntryPoint());
    }

    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    void testGetLogo() {
        byte[] expectedLogo = Arrays.copyOfRange(data, 0x104, 0x134);
        assertArrayEquals(expectedLogo, cartridge.getLogo());
    }

    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    void testGetTitle() {
        String expectedTitle = new String(Arrays.copyOfRange(data, 0x0134, 0x0143), StandardCharsets.US_ASCII);
        assertEquals(expectedTitle, cartridge.getTitle());
    }

    @Test
    void testGetCGBFlag() {
        assertEquals(CGBValues.CGB_ENHANCED, cartridge.getCGBFlag());
    }

    @Test
    void testGetNewLicenseeCode() {
        assertEquals(NewLicensee.NOT_AVAILABLE, cartridge.getNewLicenseeCode());
    }

    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    void testGetNewLicenseeCodeWhenOldCodeSelectsIt() {
        cartridge.getRomData()[0x14A] = 0x33;
        cartridge.getRomData()[0x144] = '0';
        cartridge.getRomData()[0x145] = '1';

        assertEquals(NewLicensee.NINTENDO_RD1, cartridge.getNewLicenseeCode());
    }

    @Test
    void testGetRomType() {
        assertEquals(Roms.MBC1_RAM, cartridge.getRomType());
    }

    @Test
    void testGetRomSize() {
        assertEquals(RomSize.SIZE_32KB, cartridge.getRomSize());
    }

    @Test
    void testGetDestination() {
        assertEquals(Destination.JAPAN, cartridge.getDestination());
    }

    @Test
    void testGetOldLicensee() {
        assertEquals(OldLicensee.NONE, cartridge.getOldLicensee());
    }

    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    void testGetChecksum() {
        assertEquals(data[0x14d], cartridge.getChecksum());
    }

    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    void testGetGlobalChecksum() {
        long expectedChecksum = new BigInteger(Arrays.copyOfRange(data, 0x014e, 0x0150)).longValue();
        assertEquals(expectedChecksum, cartridge.getGlobalChecksum());
    }

    @Test
    void testIsHeaderValid() {
        assertTrue(cartridge.isHeaderValid());
    }

    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    void testIsHeaderInvalid() {
        cartridge.getRomData()[0x134] = 1;

        assertFalse(cartridge.isHeaderValid());
    }

    @Test
    void testReadByteFromAddress() {
        int address = TestUtils.getRandomIntegerInRange(0, data.length);
        byte expected = data[address];
        int result = cartridge.readByteFromAddress(address);
        assertEquals(expected, result);
    }

    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    void testWriteByteToAddress() {
        int address = TestUtils.getRandomIntegerInRange(0, data.length);
        int value;
        do {
            value = TestUtils.getRandomIntegerInRange(0, 0xFF);
        } while (value == data[address]);

        cartridge.writeByteToAddress(address, value);
        int readValue = cartridge.readByteFromAddress(address);
        assertEquals(value, readValue);
        assertNotEquals(value, data[address]);
    }

    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    void testWriteByteToRomOnlyDoesNothing() {
        cartridge.getRomData()[0x147] = 0;
        cartridge.writeByteToAddress(0, 1);

        assertEquals(0, cartridge.readByteFromAddress(0));
    }
}
