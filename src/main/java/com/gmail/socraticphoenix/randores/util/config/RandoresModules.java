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
    @Serialize(value = "mobequip", comments = "If true, zombies, skeletons, and vindicators will randomly be equipped with randores items")
    private boolean mobEquip;
    @Serialize(value = "dungeonloot", comments = "If true, loot chests from dungeons, villages, etc. will spawn with randores items")
    private boolean dungeonLoot;
    @Serialize(value = "dimensionless", comments = {"If true, randores will not check the dimension before spawning ore", "Ores that are set in stone will only spawn in stone, set in endstone will only spawn in endstone, etc.", "However, ores will spawn in any dimension with the necessary blocks, rather than just the overworld, end, and nether."})
    private boolean dimensionLess;
    @Serialize(value = "starterkit", comments = "If true, players that log in for the first time will be given a random set of randores items")
    private boolean starterKit;
    @Serialize(value = "altar", comments = "If true, small brick structures will spawn in the overworld, with chests full of randores items inside")
    private boolean altar;
    @Serialize(value = "youtubemode", comments = {"If true, the chance that ore will spawn and, if altars is true, that altars will spawn, will be nearly doubled", "This makes it much easier to show off randores"})
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
