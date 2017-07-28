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
package com.gmail.socraticphoenix.randores.util.config;

import com.gmail.socraticphoenix.jlsc.serialization.annotation.Serializable;
import com.gmail.socraticphoenix.jlsc.serialization.annotation.SerializationConstructor;
import com.gmail.socraticphoenix.jlsc.serialization.annotation.Serialize;

@Serializable
public class RandoresModules {
    @Serialize(value = "mobequip")
    private boolean mobEquip;
    @Serialize(value = "dungeonloot")
    private boolean dungeonLoot;
    @Serialize(value = "dimensionless")
    private boolean dimensionLess;
    @Serialize(value = "starterkit")
    private boolean starterKit;
    @Serialize(value = "altar")
    private boolean altar;
    @Serialize(value = "youtubemode")
    private boolean youtubeMode;

    @SerializationConstructor
    public RandoresModules() {

    }

    public RandoresModules(boolean mobEquip, boolean dungeonLoot, boolean dimensionLess, boolean starterKit, boolean altar, boolean youtubeMode) {
        this.mobEquip = mobEquip;
        this.dungeonLoot = dungeonLoot;
        this.dimensionLess = dimensionLess;
        this.starterKit = starterKit;
        this.altar = altar;
        this.youtubeMode = youtubeMode;
    }

    public boolean isMobEquip() {
        return this.mobEquip;
    }

    public boolean isDungeonLoot() {
        return this.dungeonLoot;
    }

    public boolean isDimensionLess() {
        return this.dimensionLess;
    }

    public boolean isStarterKit() {
        return this.starterKit;
    }

    public boolean isAltar() {
        return this.altar;
    }

    public boolean isYoutubeMode() {
        return this.youtubeMode;
    }
}
