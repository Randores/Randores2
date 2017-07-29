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

import com.gmail.socraticphoenix.jlsc.JLSCArray;
import com.gmail.socraticphoenix.jlsc.JLSCConfiguration;
import com.gmail.socraticphoenix.jlsc.JLSCException;
import com.gmail.socraticphoenix.randores.Randores;
import com.gmail.socraticphoenix.randores.mod.component.Dimension;
import com.gmail.socraticphoenix.randores.mod.component.MaterialDefinition;
import com.gmail.socraticphoenix.randores.mod.component.MaterialDefinitionGenerator;
import com.gmail.socraticphoenix.randores.mod.component.MaterialDefinitionRegistry;
import com.gmail.socraticphoenix.randores.mod.data.RandoresSeed;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RandoresWorldListener {
    private List<Long> loaded = new ArrayList<>();

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload ev) {
        World world = ev.getWorld();
        if (!world.isRemote && world.provider.getDimension() == Dimension.OVERWORLD.getId()) {
            long seed = RandoresSeed.getSeed(world);
            if (this.loaded.contains(seed)) {
                this.loaded.remove(seed);
                Randores.info("Removing definitions for seed " + seed + ".");
                MaterialDefinitionGenerator.removeDefinitions(seed);
            }
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load ev) throws IOException, JLSCException {
        World world = ev.getWorld();
        if (!world.isRemote && world.provider.getDimension() == Dimension.OVERWORLD.getId()) {
            long seed = RandoresSeed.getSeed(world);
            if (!this.loaded.contains(seed)) {
                this.loaded.add(seed);
                File dir = world.getSaveHandler().getWorldDirectory();
                File custom = new File(dir, "randores_custom.jlsc");
                File custom2 = new File(dir, "randores_custom.cjlsc");
                if (custom.exists()) {
                    Randores.info("Discovered custom definitions for world " + dir.getName() + ".",
                            "Loading custom definitions...");
                    JLSCConfiguration configuration = JLSCConfiguration.fromText(custom);
                    createCustomFrom(configuration, world.getWorldInfo().getWorldName(), seed);
                } else if (custom2.exists()) {
                    Randores.info("Discovered compressed custom definitions for world " + dir.getName() + ".",
                            "Loading custom definitions...");
                    JLSCConfiguration configuration = JLSCConfiguration.fromCompressed(custom2);
                    createCustomFrom(configuration, world.getWorldInfo().getWorldName(), seed);
                } else {
                    Randores.info("Registering definitions for seed " + seed + ".");
                    MaterialDefinitionGenerator.registerDefinitionsIfNeeded(seed, Randores.getCount(), world);
                }
                Randores.info("Statistics:");
                MaterialDefinitionGenerator.logStatistics(MaterialDefinitionRegistry.getAll(seed));
            }
        }
    }

    private void createCustomFrom(JLSCConfiguration configuration, String name, long seed) {
        JLSCArray array = configuration.getArray("definitions").get();
        List<MaterialDefinition> definitions = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            MaterialDefinition definition = array.getAs(i, MaterialDefinition.class).get();
            Randores.info("Loaded custom definition " + definition.getName());
            definition.provideData(seed, i);
            definitions.add(definition);
        }
        Randores.info("Registering loaded definitions...");
        MaterialDefinitionRegistry.registerCustom(name, definitions);
        Randores.info("Loaded custom definitions.");
    }

}
