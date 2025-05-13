package com.egamboau.gameboy.cartridge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.egamboau.test.TestUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CartridgeTest {

    private static final Logger LOGGER = LogManager.getLogger(); 

    private static Cartridge cartridge;
    private static File tempFile;

    private static byte[] data;

    @BeforeAll
    static void setUp() throws IOException {
        //download a test rom for this. 
        BufferedInputStream in = new BufferedInputStream(URI.create("https://github.com/retrio/gb-test-roms/raw/refs/heads/master/halt_bug.gb").toURL().openStream());
        FileAttribute<Set<PosixFilePermission>> attrs = PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rw-------"));
        tempFile = Files.createTempFile("tmpDirPrefix", null, attrs).toFile();
        LOGGER.info("Writting test rom to {}", tempFile.getAbsolutePath());
        OutputStream outStream = new FileOutputStream(tempFile);
        data = in.readAllBytes();
        outStream.write(data);
        outStream.close();
        LOGGER.info("File Written");
        
    }

    @BeforeEach
    void setUpEach() throws IOException {
        cartridge = new Cartridge(tempFile.getAbsolutePath());
    }


    @AfterAll
    static void tearDown() {
        LOGGER.info("Deleting file");
        tempFile.delete();
    }

    @Test
    void testGetRomData() throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(data);
        String expected = Base64.getEncoder().encodeToString(md.digest());
        LOGGER.info("Expected MD5: {}",expected);
        md.reset();
        md.update(cartridge.getRomData());
        String loaded =Base64.getEncoder().encodeToString(md.digest());
        LOGGER.info("Lodaded MD5: {}",loaded);
        assertEquals(expected, loaded);
    }

    @Test
    void testGetFileName() {
        assertEquals(tempFile.getAbsolutePath(), cartridge.getFileName());
    }
    
    @Test
    void testGetEntryPoint() {
        assertArrayEquals(Arrays.copyOfRange(data, 0x100, 0x104), cartridge.getEntryPoint());
    }
    
    @Test
    void testGetLogo() {
        byte[] expectedLogo = Arrays.copyOfRange(data, 0x104, 0x134);
        assertArrayEquals(expectedLogo, cartridge.getLogo());
    }

    @Test
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
    void testGetChecksum() {
        assertEquals(data[0x14d], cartridge.getChecksum());
    }

    @Test
    void testGetGlobalChecksum() {
        long expectedChecksum = new BigInteger(Arrays.copyOfRange(data, 0x014e, 0x0150)).longValue();
        assertEquals(expectedChecksum, cartridge.getGlobalChecksum());
    }

    @Test
    void testIsHeaderValid() {
        assertTrue(cartridge.isHeaderValid());
    }

    @Test
    void testReadByteFromAddress() {
        int address = TestUtils.getRandomIntegerInRange(0, data.length);
        byte expected = data[address];
        int result = cartridge.readByteFromAddress(address);
        assertEquals(expected, result);
    }

    @Test
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
}