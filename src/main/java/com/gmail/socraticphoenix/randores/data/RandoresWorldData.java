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
package com.gmail.socraticphoenix.randores.data;

import com.gmail.socraticphoenix.jlsc.JLSCArray;
import com.gmail.socraticphoenix.jlsc.JLSCCompound;
import com.gmail.socraticphoenix.jlsc.JLSCConfiguration;
import com.gmail.socraticphoenix.jlsc.JLSCException;
import com.gmail.socraticphoenix.jlsc.JLSCFormat;
import com.gmail.socraticphoenix.jlsc.io.JLSCStyle;
import com.gmail.socraticphoenix.jlsc.io.JLSCSyntax;
import com.gmail.socraticphoenix.randores.Randores;
import com.gmail.socraticphoenix.randores.component.MaterialDefinition;
import com.gmail.socraticphoenix.randores.component.MaterialDefinitionGenerator;
import com.gmail.socraticphoenix.randores.component.enumerable.OreType;
import com.gmail.socraticphoenix.randores.plugin.RandoresPlugin;
import com.gmail.socraticphoenix.randores.plugin.RandoresPluginRegistry;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class RandoresWorldData extends WorldSavedData {
    public static final UUID DUMMY_ID = new UUID(0, 0);

    public static final String NAME = "randores:world_data";
    public static final Map<UUID, RandoresWorldData> DATA_MAP = new HashMap<>();

    private List<MaterialDefinition> cache = new ArrayList<>();

    private Map<OreType, List<MaterialDefinition>> dimensionOnly = new HashMap<>();
    private Map<OreType, List<MaterialDefinition>> dimensionShuffleCaches = new HashMap<>();

    private boolean loaded;
    private Kind kind;

    //Saved data
    private long seed;
    private UUID id;
    private Map<String, Long> pluginSeeds = new HashMap<>();
    //End saved data

    public RandoresWorldData(String name) {
        super(name);
        this.loaded = false;
        this.id = DUMMY_ID;
        this.kind = Kind.SEEDED;
    }

    public RandoresWorldData() {
        this(NAME);
    }

    public Kind getKind() {
        return this.kind;
    }

    public RandoresWorldData setKind(Kind kind) {
        this.kind = kind;
        return this;
    }

    public boolean isLoaded() {
        return this.loaded;
    }

    public static boolean contains(RandoresItemData data) {
        return delegate(data, def -> true, () -> false);
    }

    public static MaterialDefinition get(RandoresItemData data) {
        return delegate(data, Function.identity(), () -> null);
    }

    public static <T> T delegate(RandoresItemData data, Function<MaterialDefinition, T> ifPresent, Supplier<T> ifAbsent) {
        if (data != null) {
            RandoresWorldData trgData = DATA_MAP.get(data.getId());
            if (trgData != null) {
                List<MaterialDefinition> definitions = trgData.getCache();
                int index = data.getIndex();
                if (index >= 0 && index < definitions.size()) {
                    return ifPresent.apply(definitions.get(index));
                }
            }
        }
        return ifAbsent.get();
    }

    public static void delegateVoid(RandoresItemData data, Consumer<MaterialDefinition> ifPresent, Runnable ifAbsent) {
        if (data != null) {
            RandoresWorldData trgData = DATA_MAP.get(data.getId());
            if (trgData != null) {
                List<MaterialDefinition> definitions = trgData.getCache();
                int index = data.getIndex();
                if (index >= 0 && index < definitions.size()) {
                    ifPresent.accept(definitions.get(index));
                }
            }
        }
        ifAbsent.run();
    }

    public static RandoresWorldData loadFrom(World world) {
        RandoresWorldData data = RandoresWorldData.getSimply(world);
        while (data.id.equals(DUMMY_ID)) {
            data.id = UUID.randomUUID();
        }
        File worldDir = world.getSaveHandler().getWorldDirectory();
        File custom1 = new File(worldDir, "randores_custom.cjlsc");
        File custom2 = new File(worldDir, "randores_custom.jlsc");
        long seed = data.getSeed();

        boolean dirty = false;

        Random seedGenerator = new Random(seed);
        for(RandoresPlugin plugin : RandoresPluginRegistry.getPlugins()) {
            String id = plugin.id();
            if(!data.pluginSeeds.containsKey(id)) {
                data.pluginSeeds.put(id, seedGenerator.nextLong());
                dirty = true;
            }
        }

        try {
            if (custom1.exists()) {
                Randores.info("Found compressed custom definitions... Loading file...");
                JLSCConfiguration configuration = JLSCConfiguration.fromCompressed(custom1);
                Randores.info("Loaded definitions file, loading definitions...");
                data.cache = createCustomFrom(configuration, data.id);
                data.kind = Kind.CUSTOM;
            } else if (custom2.exists()) {
                Randores.info("Found custom definitions... Loading file...");
                JLSCConfiguration configuration = JLSCConfiguration.fromText(custom2);
                Randores.info("Loaded definitions file, loading definitions...");
                data.cache = createCustomFrom(configuration, data.id);
                data.kind = Kind.CUSTOM;
            } else {
                Randores.info("Using seed definitions");
                data.defineBySeed();
                if(Randores.getConfigObj().isConvert()) {
                    Randores.info("Converting to custom definitions...");
                    JLSCArray array = new JLSCArray();
                    data.getCache().forEach(array::add);
                    JLSCConfiguration configuration = new JLSCConfiguration(new JLSCCompound(), new File(worldDir, "randores_custom.jlsc"), JLSCFormat.TEXT, JLSCStyle.DEFAULT, JLSCSyntax.DEFAULT, false);
                    configuration.put("definitions", array);
                    configuration.save();
                    Randores.info("Definitions converted.");
                }
            }
            Randores.info("Statistics:");
            MaterialDefinitionGenerator.logStatistics(data.cache);
        } catch (JLSCException | IOException e) {
            Randores.warn("ERROR LOADING OR SAVING RANDORES CUSTOM DEFINITIONS, FALLING BACK TO SEEDED DEFINITIONS", e);
            Randores.info("Using seed definitions");
            data.defineBySeed();
        }

        MaterialDefinitionGenerator.applyBackers(data.cache);
        data.coalesceCaches();
        data.loaded = true;
        RandoresWorldData.DATA_MAP.put(data.id, data);
        if(dirty) {
            data.markDirty();
        }
        return data;
    }

    private static List<MaterialDefinition> createCustomFrom(JLSCConfiguration configuration, UUID id) {
        Randores.info("Loading custom definitions...");
        JLSCArray array = configuration.getArray("definitions").get();
        List<MaterialDefinition> definitions = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            MaterialDefinition definition = array.getAs(i, MaterialDefinition.class).get();
            Randores.info("Loaded custom definitions: " + definition.getName());
            definition.provideData(id, i);
            definitions.add(definition);
        }
        Randores.info("Loaded definitions.");
        return definitions;
    }

    public static RandoresWorldData getSimply(World world) {
        MapStorage storage = world.getMapStorage();
        RandoresWorldData data = (RandoresWorldData) storage.getOrLoadData(RandoresWorldData.class, NAME);
        if (data == null) {
            data = new RandoresWorldData();
            data.setSeed(genSeed(world));
            storage.setData(NAME, data);
        }
        return data;
    }

    private static long genSeed(World world) {
        Random prng = new Random(world.getSeed());
        long seed = prng.nextLong();
        while (seed == 0) {
            seed = prng.nextLong();
        }
        return seed;
    }

    public UUID getId() {
        return this.id;
    }

    public static boolean hasData(World world) {
        MapStorage storage = world.getMapStorage();
        return storage.getOrLoadData(RandoresWorldData.class, NAME) != null;
    }

    public RandoresWorldData setLoaded(boolean loaded) {
        this.loaded = loaded;
        return this;
    }

    public void cleanup() {
        MaterialDefinitionGenerator.removeBackers(this.cache);
        RandoresWorldData.DATA_MAP.remove(this.id);
    }

    public void coalesceCaches() {
        for (MaterialDefinition definition : this.cache) {
            OreType oreType = definition.getOre().getOreType();
            this.dimensionOnly.computeIfAbsent(oreType, k -> new ArrayList<>()).add(definition);
            this.dimensionShuffleCaches.computeIfAbsent(oreType, k -> new ArrayList<>()).add(definition);
        }
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<MaterialDefinition> shuffleAndGetDimensionCache(OreType oreType, Random random) {
        List<MaterialDefinition> cache = this.getDimensionCache(oreType);
        Collections.shuffle(cache, random);
        return cache;
    }

    public List<MaterialDefinition> getDimensionCache(OreType oreType) {
        return this.dimensionShuffleCaches.getOrDefault(oreType, new ArrayList<>());
    }

    public List<MaterialDefinition> getDimension(OreType oreType) {
        return this.dimensionOnly.getOrDefault(oreType, Collections.emptyList());
    }

    public List<MaterialDefinition> getCache() {
        return this.cache;
    }

    public void setSeed(long seed) {
        this.seed = seed;
        this.markDirty();
    }

    public long getSeed() {
        return this.seed;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        this.seed = nbt.getLong("seed");
        this.id = nbt.getUniqueId("id");
        NBTTagCompound compound = nbt.getCompoundTag("pluginSeeds");
        this.pluginSeeds.clear();
        for(String key : compound.getKeySet()) {
            this.pluginSeeds.put(key, compound.getLong(key));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setLong("seed", this.seed);
        nbt.setUniqueId("id", this.id);
        NBTTagCompound subseeds = new NBTTagCompound();
        this.pluginSeeds.forEach(subseeds::setLong);
        nbt.setTag("pluginSeeds", subseeds);
        return nbt;
    }

    public Map<String, Long> getPluginSeeds() {
        return this.pluginSeeds;
    }

    public static UUID getId(World world) {
        return RandoresWorldData.getSimply(world).id;
    }

    public static List<MaterialDefinition> getAll(UUID id) {
        List<MaterialDefinition> list = new ArrayList<>();

        RandoresWorldData trgData = DATA_MAP.get(id);
        if (trgData != null) {
            list.addAll(trgData.getCache());
        }

        return list;
    }

    public void clearAll() {
        this.cache.clear();
        this.dimensionOnly.values().forEach(List::clear);
        this.dimensionShuffleCaches.values().forEach(List::clear);
    }

    public void defineBySeed() {
        this.cache = MaterialDefinitionGenerator.makeDefinitions(MaterialDefinitionGenerator.generateColors(new Random(seed), Randores.getCount()), this);
    }

    public void register() {
        DATA_MAP.put(this.id, this);
    }

    public enum Kind {
        CUSTOM, SEEDED
    }

}
