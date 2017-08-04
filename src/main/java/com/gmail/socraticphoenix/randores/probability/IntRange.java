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
package com.gmail.socraticphoenix.randores.probability;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IntRange {
    private int min;
    private int max;

    public IntRange(int min, int max) {
        if(min > max) {
            throw new IllegalArgumentException("Minimum cannot be greater than maximum!");
        }
        this.max = max;
        this.min = min;
    }

    public boolean contains(int i) {
        return this.max >= i && this.min <= i;
    }

    public int getMin() {
        return this.min;
    }

    public int getMax() {
        return this.max;
    }

    public int randomElement(Random random) {
        if(this.min == this.max) {
            return this.min;
        } else if (this.min < 0 && this.max < 0) {
            return -this.randomElement(-this.min, -this.max, random);
        } else if (this.min < 0) {
            return this.randomElement(this.min, this.max, random);
        } else { //min and max are not BOTH less than 0, min is not less then 0, max is greater than min, both min and max are greater than 0
            return this.randomElement(this.min, this.max, random);
        }
    }

    //Returns a random int from the given range, inclusive, using the given random
    private int randomElement(int min, int max, Random random) {
        return random.nextInt((max - min) + 1) + min;
    }

    public int randomElement() {
        return this.randomElement(new Random());
    }

    public List<Integer> getAllInRange() {
        List<Integer> integers = new ArrayList<Integer>();
        for (int i = this.min; i < this.max; i++) {
            integers.add(i);
        }
        return integers;
    }

}

