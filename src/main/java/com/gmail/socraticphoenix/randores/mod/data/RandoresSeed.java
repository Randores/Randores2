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
package com.gmail.socraticphoenix.randores.mod.data;

import net.minecraft.world.World;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class RandoresSeed {
    private static AtomicLong clientSeed = new AtomicLong(0);

    public static long getSeed(World world) {
        if(world.isRemote) {
            return RandoresSeed.clientSeed.get();
        } else {
            Random prng = new Random(world.getSeed());
            long seed = prng.nextLong();
            while (seed == 0) {
                seed = prng.nextLong();
            }
            return seed;
        }
    }

    public static long getClientSeed() {
        return clientSeed.get();
    }

    public static void setClientSeed(long seed) {
        RandoresSeed.clientSeed.set(seed);
    }

    public static boolean isClient() {
        return RandoresSeed.clientSeed.get() != 0;
    }

}
