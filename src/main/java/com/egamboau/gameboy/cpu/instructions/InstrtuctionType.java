package com.egamboau.gameboy.cpu.instructions;

public enum InstrtuctionType {
    ADD,
    DECIMAL_ACUMULATOR_ADJUSTMENT, 
    DECREMENT,
    FLIP_CARRY_FLAG,
    INCREMENT,
    JUMP_RELATIVE,
    LOAD,
    NOOP,
    ONE_COMPLEMENT,
    ROTATE_LEFT_A,
    ROTATE_LEFT_CARRY_A,
    ROTATE_RIGTH_A,
    ROTATE_RIGTH_CARRY_A,
    SET_CARRY_FLAG,
    STOP, HALT, ADD_WITH_CARRY,
}
