package com.egamboau.gameboy.cartridge;

import java.util.HashMap;
import java.util.Map;

public enum NewLicensee {
    NOT_AVAILABLE(""),
    UNKNOWN(null),
    NONE("00"),
    NINTENDO_RD1("01"),
    CAPCOM("08"),
    EA("13"),
    HUDSON_SOFT("18"),
    B_AI("19"),
    KSS("20"),
    PLANNING_OFFICE_WADA("22"),
    PCM_COMPLETE("24"),
    SAN_X("25"),
    KEMCO("28"),
    SETA_CORPORATION("29"),
    VIACOM("30"),
    NINTENDO("31"),
    BANDAI("32"),
    OCEAN_ACCLAIM("33"),
    KONAMI("34"),
    HECTOR_SOFT("35"),
    TAITO("37"),
    BANPRESTO("39"),
    UBI_SOFT("41"),
    ATLUS("42"),
    MALIBU_INTERACTIVE("44"),
    ANGEL("46"),
    BULLET_PROOF_SOFTWARE("47"),
    IREM("49"),
    ABSOLUTE("50"),
    ACCLAIM("51"),
    ACTIVISION("52"),
    SAMMY("53"),
    HI_TECH_EXPRESSIONS("55"),
    LJN("56"),
    MATCHBOX("57"),
    MATTEL("58"),
    MILTON_BRADLEY("59"),
    TITUS("60"),
    VIRGIN_GAMES("61"),
    LUCASFILM("64"),
    OCEAN("67"),
    INFOGRAMES("70"),
    INTERPLAY("71"),
    BRODERBUND("72"),
    SCULPTURED_SOFTWARE("73"),
    SALES_CURVE("75"),
    THQ("78"),
    ACCOLADE("79"),
    MISAWA("80"),
    LOZC("83"),
    TOKUMA_SHOTEN("86"),
    TSUKUDA_ORIGINAL("87"),
    CHUNSOFT("91"),
    VIDEO_SYSTEM("92"),
    VARIE("95"),
    YONEZAWA("96"),
    KANEKO("97"),
    PACK_IN_VIDEO("99"),
    BOTTOM_UP("9H"),
    KONAMI_YUGIOH("A4"),
    MTO("BL"),
    KODANSHA("DK");

    private final String code;

    private static final Map<String, NewLicensee> BY_CODE = new HashMap<>();

    static {
        for (NewLicensee licensee : values()) {
            BY_CODE.put(licensee.code, licensee);
        }
    }


    NewLicensee(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static NewLicensee fromCode(String code) {
        NewLicensee licensee = BY_CODE.get(code);
        if (licensee == null) {
            return UNKNOWN;
        }
        return licensee;
    }
}