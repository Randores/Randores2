/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 socraticphoenix@gmail.com
 * Copyright (c) 2016 contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.gmail.socraticphoenix.randores.util.probability;

import java.util.Random;

public class RandoresProbability {

    public static boolean percentChance(double chance, Random random) {
        return random.nextDouble() * 100 <= chance;
    }

    public static int linearRand(int k, double m, int b, Random random) {
        int x = random.nextInt(k);
        int y = random.nextInt(b);
        if (y > m * x + b) {
            return k - x;
        } else {
            return x;
        }
    }

    public static double rand(double min, double max, Random random) {
        return RandoresProbability.scale(min, max, random.nextDouble());
    }

    public static double expRand(double b, double min, double max, Random random) {
        return RandoresProbability.scale(min, max, RandoresProbability.expRand(b, random));
    }

    public static double expRand(double b, Random random) {
        return -Math.log(1 - (1 - Math.exp(-b)) * random.nextDouble()) / b;
    }

    public static double oneSidedNormalRand(double min, double stdev, Random random) {
        double normal = RandoresProbability.normalRand(min, stdev, random);
        if (normal < min) {
            normal = (min - normal) + min;
        }
        return normal;
    }

    public static double normalRand(double mean, double stdev, Random random) {
        return random.nextGaussian() * stdev + mean;
    }

    public static double oneSidedInflectedNormalRand(double min, double max, double stdev, Random random) {
        double normal = RandoresProbability.inflectedNormalRand(min - (max - min), max, min, stdev, random);
        if (normal < min) {
            normal = (min - normal) + min;
        }
        return normal;
    }

    public static double inflectedNormalRand(double min, double max, double mean, double stdev, Random random) {
        if(min == max) {
            throw new IllegalArgumentException("min == max");
        } else if (!(min <= mean && mean < max)) {
            throw new IllegalArgumentException("Mean is not between min and max");
        } else if (min > max) {
            throw new IllegalArgumentException("min > max");
        }
        return RandoresProbability.inflectedNormalNormalize(min, max, mean, RandoresProbability.normalRand(mean, stdev, random));
    }

    public static double scale(double min, double max, double rand) {
        return ((max - min) * rand) + min;
    }

    public static double clamp(double val, double min, double max) {
        return val > max ? max : val < min ? min : val;
    }

    public static int clamp(int val, int min, int max) {
        return val > max ? max : val < min ? min : val;
    }

    private static double inflectedNormalNormalize(double min, double max, double mean, double shifted) {
        if (shifted >= min && shifted < max) {
            return shifted;
        } else if (shifted < min) {
            while (shifted < min) {
                shifted = mean - (min - shifted);
            }
            return shifted;
        } else if (shifted >= max) {
            while (shifted >= max) {
                shifted = mean + (shifted - max);
            }
            return shifted;
        } else {
            return Double.NaN;
        }
    }

}
