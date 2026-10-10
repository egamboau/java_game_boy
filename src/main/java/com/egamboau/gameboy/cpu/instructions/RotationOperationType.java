package com.egamboau.gameboy.cpu.instructions;

public enum RotationOperationType {

    RLC,
    RRC,
    RL,
    RR,
    SLA,
    SRA,
    SWAP,
    SRL;

    /**
     * Returns the rotation operation at the given opcode index.
     *
     * @param index the opcode index
     * @return the corresponding rotation operation
     */
    public static RotationOperationType getRotationOperationType(final int index) {
        return RotationOperationType.values()[index];
    }
}
