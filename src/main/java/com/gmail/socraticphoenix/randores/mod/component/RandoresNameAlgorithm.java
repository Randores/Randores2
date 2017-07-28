/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 socraticphoenix@gmail.com
 * Copyright (c) 2017 contributors
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
package com.gmail.socraticphoenix.randores.mod.component;

import com.gmail.socraticphoenix.randores.Randores;

import java.awt.Color;
import java.util.Random;

public class RandoresNameAlgorithm {

    private static final String[] POST_DICTIONARY = {"ite", "ium", "anite", "enite", "agnum", "onite", "onium", "on", "inium", "ide", "ine"};
    private static final String[] VOWEL_DICTIONARY = {"a", "e", "i", "u", "o"};
    private static final String[] CONSONANT_DICTIONARY = {"w", "r", "t", "y", "p", "s", "d", "f", "x", "g", "h", "j", "k", "l", "z", "c", "v", "b", "n", "m", "sh", "ch", "ph", "th", "qu", "ck"};


    public static String name(Color color) {
        Random random = new Random(color.getRGB());
        String res = RandoresNameAlgorithm.name(random);
        while (Randores.containsOffensiveWord(res)) {
            res = RandoresNameAlgorithm.name(random);
        }
        return res;
    }

    private static String name(Random random) {
        String res = "";
        int consLen = random.nextInt(1) + 2;
        for (int i = 0; i < consLen; i++) {
            res += CONSONANT_DICTIONARY[random.nextInt(CONSONANT_DICTIONARY.length)];
            if (i < consLen - 1) {
                res += VOWEL_DICTIONARY[random.nextInt(VOWEL_DICTIONARY.length)];
                if(random.nextInt(10) == 0) {
                    res += VOWEL_DICTIONARY[random.nextInt(VOWEL_DICTIONARY.length)];
                    if(random.nextInt(30) == 0) {
                        res += VOWEL_DICTIONARY[random.nextInt(VOWEL_DICTIONARY.length)];
                    }
                }
            }
        }
        res += POST_DICTIONARY[random.nextInt(POST_DICTIONARY.length)];
        res = Character.toUpperCase(res.charAt(0)) + res.substring(1);
        return res;
    }

}