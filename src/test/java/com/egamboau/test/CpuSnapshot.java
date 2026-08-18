package com.egamboau.test;

import java.util.Map;

import com.egamboau.gameboy.cpu.instructions.RegisterType;

public record CpuSnapshot(Map<RegisterType, Integer> registers, long cycles) {

}
