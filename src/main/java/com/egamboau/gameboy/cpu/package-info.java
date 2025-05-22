/**
 * This package contains the core classes and components related to the Game Boy CPU.
 * It includes the implementation of the CPU itself, as well as supporting classes
 * such as registers, flags, and instruction management.
 *
 * <p>The classes in this package are responsible for emulating the behavior of the
 * Game Boy's CPU, including instruction fetching, decoding, and execution. These
 * components work together to provide an accurate simulation of the Game Boy's
 * processing unit.</p>
 *
 * <h2>Key Components:</h2>
 * <ul>
 *   <li><b>CPU:</b> The main class representing the Game Boy's CPU, responsible for executing instructions.</li>
 *   <li><b>FlagRegister:</b> A specialized register that manages the CPU flags (Zero, Subtract, Half-Carry, Carry).</li>
 *   <li><b>Register:</b> Base class for CPU registers, providing storage and manipulation of register values.</li>
 *   <li><b>Instruction Management:</b> Integration with the instruction set to fetch, decode, and execute instructions.</li>
 * </ul>
 *
 * <p>This package is part of the Game Boy emulator project and is designed to provide
 * a robust and extensible framework for simulating the Game Boy's CPU.</p>
 */
package com.egamboau.gameboy.cpu;
