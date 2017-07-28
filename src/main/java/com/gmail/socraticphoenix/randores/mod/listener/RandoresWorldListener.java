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
package com.gmail.socraticphoenix.randores.mod.listener;

import com.gmail.socraticphoenix.randores.Randores;
import com.gmail.socraticphoenix.randores.mod.component.MaterialDefinitionGenerator;
import com.gmail.socraticphoenix.randores.mod.data.RandoresSeed;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RandoresWorldListener {
    private List<Long> loaded = new ArrayList<Long>();

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload ev) {
        World world = ev.getWorld();
        if (!world.isRemote) {
            long seed = RandoresSeed.getSeed(world);
            this.loaded.remove(seed);
            if (!this.loaded.contains(seed)) {
                MaterialDefinitionGenerator.removeDefinitions(seed);
            }
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load ev) throws IOException {
        World world = ev.getWorld();
        if (!world.isRemote) {
            long seed = RandoresSeed.getSeed(world);
            this.loaded.add(seed);
            MaterialDefinitionGenerator.registerDefinitionsIfNeeded(seed, Randores.getCount());
        }
    }

}
