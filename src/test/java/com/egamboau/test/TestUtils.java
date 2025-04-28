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
                case REGISTER_A:
                case REGISTER_F:
                    result.add(RegisterType.REGISTER_AF);
                    break;
                case REGISTER_AF:
                    result.add(RegisterType.REGISTER_A);
                    result.add(RegisterType.REGISTER_F);
                    break;

                case REGISTER_B:
                case REGISTER_C:
                    result.add(RegisterType.REGISTER_BC);
                    break;
                case REGISTER_BC:
                    result.add(RegisterType.REGISTER_B);
                    result.add(RegisterType.REGISTER_C);
            
                case REGISTER_D:
                case REGISTER_E:
                    result.add(RegisterType.REGISTER_DE);
                case REGISTER_DE:
                    result.add(RegisterType.REGISTER_D);
                    result.add(RegisterType.REGISTER_E);
                
                    break;
                case REGISTER_H:
                case REGISTER_L:
                    result.add(RegisterType.REGISTER_HL);
                case REGISTER_HL:
                    result.add(RegisterType.REGISTER_H);
                    result.add(RegisterType.REGISTER_L);

                default:
                    break;

            }
        }
        return result.toArray(new RegisterType[0]);
        
    }
}
