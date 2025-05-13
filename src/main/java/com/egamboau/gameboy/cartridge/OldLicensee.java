package com.egamboau.gameboy.cartridge;

import java.util.HashMap;
import java.util.Map;


public enum OldLicensee {

    /**
     * No licensee.
     */
    NONE((byte) 0x00),

    /**
     * Nintendo licensee.
     */
    NINTENDO((byte) 0x01),

    /**
     * Capcom licensee.
     */
    CAPCOM((byte) 0x08),

    /**
     * HOT-B licensee.
     */
    HOT_B((byte) 0x09),

    /**
     * Jaleco licensee.
     */
    JALECO((byte) 0x0A),

    /**
     * Coconuts Japan licensee.
     */
    COCONUTS_JAPAN((byte) 0x0B),

    /**
     * Elite Systems licensee.
     */
    ELITE_SYSTEMS((byte) 0x0C),

    /**
     * EA (Electronic Arts) licensee.
     */
    EA((byte) 0x13),

    /**
     * Hudson Soft licensee.
     */
    HUDSON_SOFT((byte) 0x18),

    /**
     * ITC Entertainment licensee.
     */
    ITC_ENTERTAINMENT((byte) 0x19),

    /**
     * Yanoman licensee.
     */
    YANOMAN((byte) 0x1A),

    /**
     * Japan Clary licensee.
     */
    JAPAN_CLARY((byte) 0x1D),

    /**
     * Virgin Games licensee.
     */
    VIRGIN_GAMES((byte) 0x1F),

    /**
     * PCM Complete licensee.
     */
    PCM_COMPLETE((byte) 0x24),

    /**
     * San-X licensee.
     */
    SAN_X((byte) 0x25),

    /**
     * Kemco licensee.
     */
    KEMCO((byte) 0x28),

    /**
     * Seta Corporation licensee.
     */
    SETA_CORPORATION((byte) 0x29),

    /**
     * Infogrames licensee.
     */
    INFOGRAMES((byte) 0x30),

    /**
     * Nintendo (again) licensee.
     */
    NINTENDO_AGAIN((byte) 0x31),

    /**
     * Bandai licensee.
     */
    BANDAI((byte) 0x32),

    /**
     * New licensee.
     */
    NEW_LICENSEE((byte) 0x33),

    /**
     * Konami licensee.
     */
    KONAMI((byte) 0x34),

    /**
     * Hector Soft licensee.
     */
    HECTOR_SOFT((byte) 0x35),

    /**
     * Banpresto licensee.
     */
    BANPRESTO((byte) 0x39),

    /**
     * Entertainment Interactive licensee.
     */
    ENTERTAINMENT_INTERACTIVE((byte) 0x3C),

    /**
     * Gremlin licensee.
     */
    GREMLIN((byte) 0x3E),

    /**
     * Ubisoft licensee.
     */
    UBI_SOFT((byte) 0x41),

    /**
     * Atlus licensee.
     */
    ATLUS((byte) 0x42),

    /**
     * Malibu Interactive licensee.
     */
    MALIBU_INTERACTIVE((byte) 0x44),

    /**
     * Angel licensee.
     */
    ANGEL((byte) 0x46),

    /**
     * Spectrum Holobyte licensee.
     */
    SPECTRUM_HOLOBYTE((byte) 0x47),

    /**
     * Irem licensee.
     */
    IREM((byte) 0x49),

    /**
     * Virgin Games (again) licensee.
     */
    VIRGIN_GAMES_AGAIN((byte) 0x4A),

    /**
     * US Gold licensee.
     */
    US_GOLD((byte) 0x4F),

    /**
     * Absolute licensee.
     */
    ABSOLUTE((byte) 0x50),

    /**
     * Acclaim licensee.
     */
    ACCLAIM((byte) 0x51),

    /**
     * Activision licensee.
     */
    ACTIVISION((byte) 0x52),

    /**
     * Sammy licensee.
     */
    SAMMY((byte) 0x53),

    /**
     * GameTek licensee.
     */
    GAMETEK((byte) 0x54),

    /**
     * Park Place licensee.
     */
    PARK_PLACE((byte) 0x55),

    /**
     * LJN licensee.
     */
    LJN((byte) 0x56),

    /**
     * Matchbox licensee.
     */
    MATCHBOX((byte) 0x57),

    /**
     * Milton Bradley licensee.
     */
    MILTON_BRADLEY((byte) 0x59),

    /**
     * Mindscape licensee.
     */
    MINDSCAPE((byte) 0x5A),

    /**
     * Romstar licensee.
     */
    ROMSTAR((byte) 0x5B),

    /**
     * Naxat Soft licensee.
     */
    NAXAT_SOFT((byte) 0x5C),

    /**
     * Tradewest licensee.
     */
    TRADEWEST((byte) 0x5D),

    /**
     * Titus licensee.
     */
    TITUS((byte) 0x60),

    /**
     * Virgin Games (yet again) licensee.
     */
    VIRGIN_GAMES_YET_AGAIN((byte) 0x61),

    /**
     * Ocean licensee.
     */
    OCEAN((byte) 0x67),

    /**
     * Infogrames (again) licensee.
     */
    INFOGRAMES_AGAIN((byte) 0x70),

    /**
     * Interplay licensee.
     */
    INTERPLAY((byte) 0x71),

    /**
     * Broderbund licensee.
     */
    BRODERBUND((byte) 0x72),

    /**
     * Sculptured Software licensee.
     */
    SCULPTURED_SOFTWARE((byte) 0x73),

    /**
     * Sales Curve licensee.
     */
    SALES_CURVE((byte) 0x75),

    /**
     * THQ licensee.
     */
    THQ((byte) 0x78),

    /**
     * Accolade licensee.
     */
    ACCOLADE((byte) 0x79),

    /**
     * MicroProse licensee.
     */
    MICROPROSE((byte) 0x7C),

    /**
     * Misawa licensee.
     */
    MISAWA((byte) 0x80),

    /**
     * Lozc licensee.
     */
    LOZC((byte) 0x83),

    /**
     * Tokuma Shoten licensee.
     */
    TOKUMA_SHOTEN((byte) 0x86),

    /**
     * Bullet-Proof Software licensee.
     */
    BULLET_PROOF_SOFTWARE((byte) 0x8B),

    /**
     * Vic Tokai licensee.
     */
    VIC_TOKAI((byte) 0x8C),

    /**
     * Ape Inc. licensee.
     */
    APE_INC((byte) 0x8E),

    /**
     * Chunsoft licensee.
     */
    CHUNSOFT((byte) 0x91),

    /**
     * Video System licensee.
     */
    VIDEO_SYSTEM((byte) 0x92),

    /**
     * Varie licensee.
     */
    VARIE((byte) 0x95),

    /**
     * Yonezawa licensee.
     */
    YONEZAWA((byte) 0x96),

    /**
     * Arc licensee.
     */
    ARC((byte) 0x99),

    /**
     * Nihon Bussan licensee.
     */
    NIHON_BUSSAN((byte) 0x9A),

    /**
     * Tecmo licensee.
     */
    TECMO((byte) 0x9B),

    /**
     * Imagineer licensee.
     */
    IMAGINEER((byte) 0x9C),

    /**
     * Hori Electric licensee.
     */
    HORI_ELECTRIC((byte) 0xA1),

    /**
     * Square licensee.
     */
    SQUARE((byte) 0xC3),

    /**
     * Taito licensee.
     */
    TAITO((byte) 0xC0),

    /**
     * ASCII licensee.
     */
    ASCII((byte) 0xB1),

    /**
     * HAL Laboratory licensee.
     */
    HAL_LABORATORY((byte) 0xB6),

    /**
     * SNK licensee.
     */
    SNK((byte) 0xB7),

    /**
     * Pony Canyon licensee.
     */
    PONY_CANYON((byte) 0xB9),

    /**
     * Culture Brain licensee.
     */
    CULTURE_BRAIN((byte) 0xBA),

    /**
     * Sunsoft licensee.
     */
    SUNSOFT((byte) 0xBB),

    /**
     * Sony Imagesoft licensee.
     */
    SONY_IMAGESOFT((byte) 0xBD),

    /**
     * Sammy Corporation licensee.
     */
    SAMMY_CORPORATION((byte) 0xBF),

    /**
     * Extreme Entertainment licensee.
     */
    EXTREME_ENTERTAINMENT((byte) 0xF3),

    /**
     * LJN (again) licensee.
     */
    LJN_AGAIN((byte) 0xFF);

    /**
     * The byte code associated with the licensee.
     */
    private final byte code;

    /**
     * A map for quick lookup of licensees by their byte code.
     */
    private static final Map<Byte, OldLicensee> BY_CODE = new HashMap<>();

    static {
        for (OldLicensee licensee : values()) {
            BY_CODE.put(licensee.code, licensee);
        }
    }

    /**
     * Constructor for the enum.
     *
     * @param licenseeCode The byte code associated with the licensee.
     */
    OldLicensee(final byte licenseeCode) {
        this.code = licenseeCode;
    }

    /**
     * Gets the byte code associated with the licensee.
     *
     * @return The byte code.
     */
    public byte getCode() {
        return code;
    }

    /**
     * Retrieves the licensee corresponding to the given byte code.
     *
     * @param code The byte code of the licensee.
     * @return The corresponding {@link OldLicensee}.
     * @throws IllegalArgumentException If the byte code does not match any licensee.
     */
    public static OldLicensee fromByte(final byte code) {
        OldLicensee licensee = BY_CODE.get(code);
        if (licensee == null) {
            throw new IllegalArgumentException("Unknown licensee code: " + code);
        }
        return licensee;
    }
}
