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
package com.gmail.socraticphoenix.randores.module.dungeon;

import com.gmail.socraticphoenix.randores.Randores;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RandoresLoot {
    private static String[] chests = {"abandoned_mineshaft", "desert_pyramid", "igloo_chest", "jungle_temple", "nether_bridge", "simple_dungeon", "stronghold", "village_blacksmith", "woodland_mansion"};

    @SubscribeEvent
    public void addTables(LootTableLoadEvent event){
        String name = event.getName().toString();
        if(Randores.getConfigObj().getModules().isDungeonLoot()) {
            if (is(name, chests)) {
                event.getTable().addPool(new LootPool(new LootEntry[] {new RandoresLootEntry(1, 2, true, 10, 20, new LootCondition[0], "randores_flexible_loot_entry")}, new LootCondition[0], new RandomValueRange(1), new RandomValueRange(0, 1), "randores_flexible_pool"));
            } else if (name.contains("end_city_treasure")) {
                event.getTable().addPool(new LootPool(new LootEntry[] {new RandoresLootEntry(1, 5, true, 20, 50, new LootCondition[0], "randores_flexible_loot_entry")}, new LootCondition[0], new RandomValueRange(1), new RandomValueRange(0, 1), "randores_flexible_pool"));
            } else if (name.contains("spawn_bonus_chest")) {
                event.getTable().addPool(new LootPool(new LootEntry[] {new RandoresLootEntry(1, 1, false, 0, 0, new LootCondition[0], "randores_flexible_loot_entry")}, new LootCondition[0], new RandomValueRange(1), new RandomValueRange(0, 1), "randores_flexible_pool"));
            }
        }
    }

    private boolean is(String s, String... ss) {
        for(String i : ss) {
            if(s.toLowerCase().contains(i.toLowerCase())) {
                return true;
            }
        }
        return false;
    }


}
