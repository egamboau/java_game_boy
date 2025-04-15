package com.egamboau.gameboy.cpu.instructions;

public class Instruction {

    private InstrtuctionType instrtuctionType = null;
    private AddressMode addressMode = null;
    private RegisterType firstRegister = null;
    private RegisterType secondRegister = null;
    private InstructionCondition condition = null;
    private Byte parameter = 0;

    public Instruction(InstrtuctionType instrtuctionType, AddressMode addressMode, RegisterType firstRegister,RegisterType secondRegister, InstructionCondition condition, Byte parameter) {
        this.instrtuctionType = instrtuctionType;
        this.addressMode = addressMode;
        this.firstRegister = firstRegister;
        this.secondRegister = secondRegister;
        this.condition = condition;
        this.parameter = parameter;
    }

    public InstrtuctionType getInstrtuctionType() {
        return instrtuctionType;
    }

    public AddressMode getAddressMode() {
        return addressMode;
    }

    public RegisterType getFirstRegister() {
        return firstRegister;
    }

    public RegisterType getSecondRegister() {
        return secondRegister;
    }

    public InstructionCondition getCondition() {
        return condition;
    }

    public Byte getParameter() {
        return parameter;
    }

    
}
