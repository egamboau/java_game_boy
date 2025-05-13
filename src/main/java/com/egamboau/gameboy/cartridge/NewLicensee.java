package com.egamboau.gameboy.cartridge;

import java.util.HashMap;
import java.util.Map;

public enum NewLicensee {
    /**
     * Represents a licensee that is not available.
     */
    NOT_AVAILABLE(""),

    /**
     * Represents an unknown licensee.
     */
    UNKNOWN(null),
    /**
     * Represents no licensee.
     */
    NONE("00"),

    /**
     * Represents Nintendo R&D1.
     */
    NINTENDO_RD1("01"),
    /**
     * Represents Capcom.
     */
    CAPCOM("08"),

    /**
     * Represents Electronic Arts.
     */
    EA("13"),
    /**
     * Represents Hudson Soft.
     */
    HUDSON_SOFT("18"),
    /**
     * Represents B-AI.
     */
    B_AI("19"),
    /**
     * Represents KSS.
     */
    KSS("20"),
    /**
     * Represents Planning Office Wada.
     */
    PLANNING_OFFICE_WADA("22"),
    /**
     * Represents PCM Complete.
     */
    PCM_COMPLETE("24"),
    /**
     * Represents San-X.
     */
    SAN_X("25"),
    /**
     * Represents Kemco.
     */
    KEMCO("28"),
    /**
     * Represents Seta Corporation.
     */
    SETA_CORPORATION("29"),
    /**
     * Represents Viacom.
     */
    VIACOM("30"),
    /**
     * Represents Nintendo.
     */
    NINTENDO("31"),
    /**
     * Represents Bandai.
     */
    BANDAI("32"),
    /**
     * Represents Ocean/Acclaim.
     */
    OCEAN_ACCLAIM("33"),
    /**
     * Represents Konami.
     */
    KONAMI("34"),
    /**
     * Represents Hector Soft.
     */
    HECTOR_SOFT("35"),
    /**
     * Represents Taito.
     */
    TAITO("37"),
    /**
     * Represents Banpresto.
     */
    BANPRESTO("39"),
    /**
     * Represents Ubisoft.
     */
    UBI_SOFT("41"),
    /**
     * Represents Atlus.
     */
    ATLUS("42"),
    /**
     * Represents Malibu Interactive.
     */
    MALIBU_INTERACTIVE("44"),
    /**
     * Represents Angel.
     */
    ANGEL("46"),
    /**
     * Represents Bullet-Proof Software.
     */
    BULLET_PROOF_SOFTWARE("47"),
    /**
     * Represents Irem.
     */
    IREM("49"),
    /**
     * Represents Absolute Entertainment.
     */
    ABSOLUTE("50"),
    /**
     * Represents Acclaim Entertainment.
     */
    ACCLAIM("51"),
    /**
     * Represents Activision.
     */
    ACTIVISION("52"),
    /**
     * Represents Sammy.
     */
    SAMMY("53"),
    /**
     * Represents Hi Tech Expressions.
     */
    HI_TECH_EXPRESSIONS("55"),
    /**
     * Represents LJN.
     */
    LJN("56"),
    /**
     * Represents Matchbox.
     */
    MATCHBOX("57"),
    /**
     * Represents Mattel.
     */
    MATTEL("58"),
    /**
     * Represents Milton Bradley.
     */
    MILTON_BRADLEY("59"),
    /**
     * Represents Titus.
     */
    TITUS("60"),
    /**
     * Represents Virgin Games.
     */
    VIRGIN_GAMES("61"),
    /**
     * Represents Lucasfilm.
     */
    LUCASFILM("64"),
    /**
     * Represents Ocean.
     */
    OCEAN("67"),
    /**
     * Represents Infogrames.
     */
    INFOGRAMES("70"),
    /**
     * Represents Interplay.
     */
    INTERPLAY("71"),
    /**
     * Represents Broderbund.
     */
    BRODERBUND("72"),
    /**
     * Represents Sculptured Software.
     */
    SCULPTURED_SOFTWARE("73"),
    /**
     * Represents Sales Curve.
     */
    SALES_CURVE("75"),
    /**
     * Represents THQ.
     */
    THQ("78"),
    /**
     * Represents Accolade.
     */
    ACCOLADE("79"),
    /**
     * Represents Misawa.
     */
    MISAWA("80"),
    /**
     * Represents Lozc.
     */
    LOZC("83"),
    /**
     * Represents Tokuma Shoten.
     */
    TOKUMA_SHOTEN("86"),
    /**
     * Represents Tsukuda Original.
     */
    TSUKUDA_ORIGINAL("87"),
    /**
     * Represents Chunsoft.
     */
    CHUNSOFT("91"),
    /**
     * Represents Video System.
     */
    VIDEO_SYSTEM("92"),
    /**
     * Represents Varie.
     */
    VARIE("95"),
    /**
     * Represents Yonezawa.
     */
    YONEZAWA("96"),
    /**
     * Represents Kaneko.
     */
    KANEKO("97"),
    /**
     * Represents Pack-In-Video.
     */
    PACK_IN_VIDEO("99"),
    /**
     * Represents Bottom-Up.
     */
    BOTTOM_UP("9H"),
    /**
     * Represents Konami Yu-Gi-Oh.
     */
    KONAMI_YUGIOH("A4"),
    /**
     * Represents MTO.
     */
    MTO("BL"),
    /**
     * Represents Kodansha.
     */
    KODANSHA("DK");

    /**
     * The code representing the licensee.
     */
    private final String code;

    /**
     * A map to store the mapping between licensee codes and their corresponding enum values.
     */
    private static final Map<String, NewLicensee> BY_CODE = new HashMap<>();

    static {
        for (NewLicensee licensee : values()) {
            BY_CODE.put(licensee.code, licensee);
        }
    }


    NewLicensee(final String licenseeCode) {
        this.code = licenseeCode;
    }

    /**
     * Gets the code representing the licensee.
     *
     * @return the licensee code.
     */
    public String getCode() {
        return code;
    }

    /**
     * Retrieves the NewLicensee enum value corresponding to the given code.
     *
     * @param code the licensee code to look up.
     * @return the corresponding NewLicensee enum value, or UNKNOWN if the code is not found.
     */
    public static NewLicensee fromCode(final String code) {
        NewLicensee licensee = BY_CODE.get(code);
        if (licensee == null) {
            return UNKNOWN;
        }
        return licensee;
    }
}
