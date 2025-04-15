package com.egamboau.test;

import java.util.Random;

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
}
