package com.egamboau.gameboy.cpu.instructions;

public enum RegisterType {
    REGISTER_A("REGISTER_A"),
    REGISTER_AF("REGISTER_AF"), 
    REGISTER_B("REGISTER_B"),
    REGISTER_BC("REGISTER_BC"),
    REGISTER_C("REGISTER_C"),
    REGISTER_D("REGISTER_D"),
    REGISTER_DE("REGISTER_DE"),
    REGISTER_E("REGISTER_E"),
    REGISTER_F("REGISTER_F"),
    REGISTER_H("REGISTER_H"), 
    REGISTER_HL("REGISTER_HL"),
    REGISTER_L("REGISTER_L"),
    REGISTER_PC("REGISTER_PC"), 
    REGISTER_SP("REGISTER_SP");

    private final String registerName;

    public String getRegisterName() {
        return registerName;
    }

    RegisterType(String registerName) {
        this.registerName = registerName;
    }
}
