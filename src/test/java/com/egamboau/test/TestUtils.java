package com.egamboau.test;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.egamboau.gameboy.cpu.instructions.RegisterType;

public final class TestUtils {

    private static Random random = new Random();

    /**
     * Get a new random integer, bound to the actual min and max values
     * @param min the minimum value to be generated
     * @param max the maximum value to be generated
     * @return the random integer, between min and max.
     */
    public static int getRandomIntegerInRange(int min, int max) {
        return random.nextInt(max - min) + min;
    }

    public static RegisterType[] getPairForRegister(RegisterType ... elements) {
        Set<RegisterType> result = new HashSet<>();
        for(RegisterType element:elements) {
            result.add(element);
            switch ( element) {
                case A:
                case F:
                    result.add(RegisterType.AF);
                    break;
                case AF:
                    result.add(RegisterType.A);
                    result.add(RegisterType.F);
                    break;

                case B:
                case C:
                    result.add(RegisterType.BC);
                    break;
                case BC:
                    result.add(RegisterType.B);
                    result.add(RegisterType.C);
            
                case D:
                case E:
                    result.add(RegisterType.DE);
                case DE:
                    result.add(RegisterType.D);
                    result.add(RegisterType.E);
                
                    break;
                case H:
                case L:
                    result.add(RegisterType.HL);
                case HL:
                    result.add(RegisterType.H);
                    result.add(RegisterType.L);

                default:
                    break;

            }
        }
        return result.toArray(new RegisterType[0]);
        
    }
}
