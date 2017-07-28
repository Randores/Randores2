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

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public enum  Dimension {
    OVERWORLD(0, "overworld") {
        @Override
        public Block[] getGenerateIn() {
            return new Block[] {Blocks.STONE};
        }
    },
    END(1, "end") {
        @Override
        public Block[] getGenerateIn() {
            return new Block[] {Blocks.END_STONE};
        }
    },
    NETHER(-1, "nether") {
        @Override
        public Block[] getGenerateIn() {
            return new Block[] {Blocks.NETHERRACK};
        }
    };

    private int id;
    private String name;

    Dimension(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public abstract Block[] getGenerateIn();

    public int getId() {
        return this.id;
    }

    public String getDimensionName() {
        return this.name;
    }
}
