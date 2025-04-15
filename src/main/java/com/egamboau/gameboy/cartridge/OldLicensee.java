package com.egamboau.gameboy.cartridge;

import java.util.HashMap;
import java.util.Map;

public enum OldLicensee {

    NONE((byte) 0x00, "None"),
    NINTENDO((byte) 0x01, "Nintendo"),
    CAPCOM((byte) 0x08, "Capcom"),
    HOT_B((byte) 0x09, "HOT-B"),
    JALECO((byte) 0x0A, "Jaleco"),
    COCONUTS_JAPAN((byte) 0x0B, "Coconuts Japan"),
    ELITE_SYSTEMS((byte) 0x0C, "Elite Systems"),
    EA((byte) 0x13, "EA (Electronic Arts)"),
    HUDSON_SOFT((byte) 0x18, "Hudson Soft"),
    ITC_ENTERTAINMENT((byte) 0x19, "ITC Entertainment"),
    YANOMAN((byte) 0x1A, "Yanoman"),
    JAPAN_CLARY((byte) 0x1D, "Japan Clary"),
    VIRGIN_GAMES((byte) 0x1F, "Virgin Games Ltd."),
    PCM_COMPLETE((byte) 0x24, "PCM Complete"),
    SAN_X((byte) 0x25, "San-X"),
    KEMCO((byte) 0x28, "Kemco"),
    SETA_CORPORATION((byte) 0x29, "SETA Corporation"),
    INFOGRAMES((byte) 0x30, "Infogrames"),
    NINTENDO_AGAIN((byte) 0x31, "Nintendo"),
    BANDAI((byte) 0x32, "Bandai"),
    NEW_LICENSEE((byte) 0x33, "Indicates that the New licensee code should be used instead."),
    KONAMI((byte) 0x34, "Konami"),
    HECTOR_SOFT((byte) 0x35, "HectorSoft"),
    BANPRESTO((byte) 0x39, "Banpresto"),
    ENTERTAINMENT_INTERACTIVE((byte) 0x3C, "Entertainment Interactive (stub)"),
    GREMLIN((byte) 0x3E, "Gremlin"),
    UBI_SOFT((byte) 0x41, "Ubi Soft"),
    ATLUS((byte) 0x42, "Atlus"),
    MALIBU_INTERACTIVE((byte) 0x44, "Malibu Interactive"),
    ANGEL((byte) 0x46, "Angel"),
    SPECTRUM_HOLOBYTE((byte) 0x47, "Spectrum HoloByte"),
    IREM((byte) 0x49, "Irem"),
    VIRGIN_GAMES_AGAIN((byte) 0x4A, "Virgin Games Ltd."),
    US_GOLD((byte) 0x4F, "U.S. Gold"),
    ABSOLUTE((byte) 0x50, "Absolute"),
    ACCLAIM((byte) 0x51, "Acclaim Entertainment"),
    ACTIVISION((byte) 0x52, "Activision"),
    SAMMY((byte) 0x53, "Sammy USA Corporation"),
    GAMETEK((byte) 0x54, "GameTek"),
    PARK_PLACE((byte) 0x55, "Park Place"),
    LJN((byte) 0x56, "LJN"),
    MATCHBOX((byte) 0x57, "Matchbox"),
    MILTON_BRADLEY((byte) 0x59, "Milton Bradley Company"),
    MINDSCAPE((byte) 0x5A, "Mindscape"),
    ROMSTAR((byte) 0x5B, "Romstar"),
    NAXAT_SOFT((byte) 0x5C, "Naxat Soft"),
    TRADEWEST((byte) 0x5D, "Tradewest"),
    TITUS((byte) 0x60, "Titus Interactive"),
    VIRGIN_GAMES_YET_AGAIN((byte) 0x61, "Virgin Games Ltd."),
    OCEAN((byte) 0x67, "Ocean Software"),
    INFOGRAMES_AGAIN((byte) 0x70, "Infogrames"),
    INTERPLAY((byte) 0x71, "Interplay Entertainment"),
    BRODERBUND((byte) 0x72, "Broderbund"),
    SCULPTURED_SOFTWARE((byte) 0x73, "Sculptured Software"),
    SALES_CURVE((byte) 0x75, "The Sales Curve Limited"),
    THQ((byte) 0x78, "THQ"),
    ACCOLADE((byte) 0x79, "Accolade"),
    MICROPROSE((byte) 0x7C, "MicroProse"),
    MISAWA((byte) 0x80, "Misawa Entertainment"),
    LOZC((byte) 0x83, "LOZC G."),
    TOKUMA_SHOTEN((byte) 0x86, "Tokuma Shoten"),
    BULLET_PROOF_SOFTWARE((byte) 0x8B, "Bullet-Proof Software"),
    VIC_TOKAI((byte) 0x8C, "Vic Tokai Corp."),
    APE_INC((byte) 0x8E, "Ape Inc."),
    CHUNSOFT((byte) 0x91, "Chunsoft Co."),
    VIDEO_SYSTEM((byte) 0x92, "Video System"),
    VARIE((byte) 0x95, "Varie"),
    YONEZAWA((byte) 0x96, "Yonezawa/S’Pal"),
    ARC((byte) 0x99, "Arc"),
    NIHON_BUSSAN((byte) 0x9A, "Nihon Bussan"),
    TECMO((byte) 0x9B, "Tecmo"),
    IMAGINEER((byte) 0x9C, "Imagineer"),
    HORI_ELECTRIC((byte) 0xA1, "Hori Electric"),
    SQUARE((byte) 0xC3, "Square"),
    TAITO((byte) 0xC0, "Taito"),
    ASCII((byte) 0xB1, "ASCII Corporation or Nexsoft"),
    HAL_LABORATORY((byte) 0xB6, "HAL Laboratory"),
    SNK((byte) 0xB7, "SNK"),
    PONY_CANYON((byte) 0xB9, "Pony Canyon"),
    CULTURE_BRAIN((byte) 0xBA, "Culture Brain"),
    SUNSOFT((byte) 0xBB, "Sunsoft"),
    SONY_IMAGESOFT((byte) 0xBD, "Sony Imagesoft"),
    SAMMY_CORPORATION((byte) 0xBF, "Sammy Corporation"),
    EXTREME_ENTERTAINMENT((byte) 0xF3, "Extreme Entertainment"),
    LJN_AGAIN((byte) 0xFF, "LJN");

    private final byte code;
    private final String licensee;

    private static final Map<Byte, OldLicensee> BY_CODE = new HashMap<>();

    static {
        for (OldLicensee licensee : values()) {
            BY_CODE.put(licensee.code, licensee);
        }
    }

    OldLicensee(byte code, String licensee) {
        this.code = code;
        this.licensee = licensee;
    }

    public byte getCode() {
        return code;
    }

    public String getLicensee() {
        return licensee;
    }

    public static OldLicensee fromByte(byte code) {
        OldLicensee licensee = BY_CODE.get(code);
        if (licensee == null) {
            throw new IllegalArgumentException("Unknown licensee code: " + code);
        }
        return licensee;
    }
}