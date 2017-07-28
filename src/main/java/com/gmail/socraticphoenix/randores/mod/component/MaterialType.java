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

import com.gmail.socraticphoenix.randores.translations.Keys;
import net.minecraft.client.resources.I18n;

public enum MaterialType {
    INGOT(Keys.INGOT, "ore"),
    GEM(Keys.GEM, "gem_ore"),
    EMERALD(Keys.EMERALD, "emerald_ore"),
    CIRCLE_GEM(Keys.CIRCLE_GEM, "circle_ore"),
    SHARD(Keys.SHARD, "shard_ore"),
    DUST(Keys.DUST, "dust_ore");

    private String name;
    private String oreName;

    MaterialType(String name, String oreName) {
        this.name = name;
        this.oreName = oreName;
    }

    public String getName() {
        return this.name;
    }

    public String getOreName() {
        return this.oreName;
    }

    public String getLocalName() {
        return I18n.format(this.getName());
    }

}
