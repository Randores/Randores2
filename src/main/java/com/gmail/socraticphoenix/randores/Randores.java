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
package com.gmail.socraticphoenix.randores;

import com.gmail.socraticphoenix.jlsc.JLSCCompound;
import com.gmail.socraticphoenix.jlsc.JLSCConfiguration;
import com.gmail.socraticphoenix.jlsc.JLSCFormat;
import com.gmail.socraticphoenix.jlsc.value.JLSCValue;
import com.gmail.socraticphoenix.randores.game.block.RandoresBlocks;
import com.gmail.socraticphoenix.randores.game.block.RandoresTileEntity;
import com.gmail.socraticphoenix.randores.game.crafting.CraftingBlocks;
import com.gmail.socraticphoenix.randores.game.crafting.CraftingItems;
import com.gmail.socraticphoenix.randores.game.crafting.forge.CraftiniumDelegateSmelt;
import com.gmail.socraticphoenix.randores.game.crafting.forge.CraftiniumForgeTileEntity;
import com.gmail.socraticphoenix.randores.game.crafting.forge.CraftiniumSmeltRegistry;
import com.gmail.socraticphoenix.randores.game.crafting.table.CraftiniumDelegateRecipe;
import com.gmail.socraticphoenix.randores.game.crafting.table.CraftiniumRecipeRegistry;
import com.gmail.socraticphoenix.randores.game.entity.RandoresArrow;
import com.gmail.socraticphoenix.randores.game.gui.RandoresGuiHandler;
import com.gmail.socraticphoenix.randores.game.item.RandoresItems;
import com.gmail.socraticphoenix.randores.game.recipe.RandoresForgeRecipe;
import com.gmail.socraticphoenix.randores.game.tab.SimpleTab;
import com.gmail.socraticphoenix.randores.game.world.RandoresWorldGenerator;
import com.gmail.socraticphoenix.randores.mod.component.MaterialDefinition;
import com.gmail.socraticphoenix.randores.mod.component.ability.AbilityRegistry;
import com.gmail.socraticphoenix.randores.mod.component.ability.abilities.PotionEffectAbility;
import com.gmail.socraticphoenix.randores.mod.component.property.RandoresFuelHandler;
import com.gmail.socraticphoenix.randores.mod.listener.EmpoweredArmorListener;
import com.gmail.socraticphoenix.randores.mod.listener.RandoresClientListener;
import com.gmail.socraticphoenix.randores.mod.listener.RandoresPlayerListener;
import com.gmail.socraticphoenix.randores.mod.listener.RandoresRegistryListener;
import com.gmail.socraticphoenix.randores.mod.listener.RandoresWorldListener;
import com.gmail.socraticphoenix.randores.mod.listener.ScheduleListener;
import com.gmail.socraticphoenix.randores.mod.network.RandoresNetworking;
import com.gmail.socraticphoenix.randores.mod.proxy.RandoresProxy;
import com.gmail.socraticphoenix.randores.module.altar.RandoresAltarGenerator;
import com.gmail.socraticphoenix.randores.module.dungeon.RandoresLoot;
import com.gmail.socraticphoenix.randores.module.equip.RandoresMobEquip;
import com.gmail.socraticphoenix.randores.module.kit.RandoresStarterKit;
import com.gmail.socraticphoenix.randores.resource.RandoresResourceManager;
import com.gmail.socraticphoenix.randores.util.config.RandoresConfig;
import com.gmail.socraticphoenix.randores.util.config.RandoresJLSC;
import com.gmail.socraticphoenix.randores.util.config.RandoresModules;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Mod(modid = "randores")
public class Randores {
    public static CreativeTabs TAB_CRAFTING;

    @SidedProxy(modId = "randores", clientSide = "com.gmail.socraticphoenix.randores.mod.proxy.RandoresClientProxy", serverSide = "com.gmail.socraticphoenix.randores.mod.proxy.RandoresServerProxy")
    public static RandoresProxy PROXY = null;
    public static Randores INSTANCE = null;

    private static List<String> offensiveWords = new ArrayList<>();
    private Logger logger;

    private File confDir;
    private JLSCConfiguration configuration;

    public Randores() {
        Randores.INSTANCE = this;
        this.logger = LogManager.getLogger("Randores");

        info("Constructing Randores mod...",
                "Building item, block, and tab objects...");

        Randores.TAB_CRAFTING = new SimpleTab("randores_crafting", () -> new ItemStack(CraftingBlocks.tableItem));

        RandoresItems.init();
        RandoresBlocks.init();

        CraftingItems.init();
        CraftingBlocks.init();

        RandoresJLSC.registerDefaults();

        info("Loading configuration...");

        this.confDir = new File("config", "randores");
        this.loadConfig();

        info("Finished loading config.",
                "Registering listeners...");
        MinecraftForge.EVENT_BUS.register(new RandoresJLSC());

        MinecraftForge.EVENT_BUS.register(new RandoresRegistryListener());
        MinecraftForge.EVENT_BUS.register(new RandoresPlayerListener());
        MinecraftForge.EVENT_BUS.register(new RandoresWorldListener());
        MinecraftForge.EVENT_BUS.register(new EmpoweredArmorListener());
        MinecraftForge.EVENT_BUS.register(new ScheduleListener());
        MinecraftForge.EVENT_BUS.register(new RandoresMobEquip());
        MinecraftForge.EVENT_BUS.register(new RandoresStarterKit());
        MinecraftForge.EVENT_BUS.register(new RandoresLoot());

        if(FMLCommonHandler.instance().getSide().isClient()) {
            MinecraftForge.EVENT_BUS.register(new RandoresClientListener());
        }

        info("Registered listeners.");
    }

    public static RandoresConfig getConfigObj() {
        return Randores.getConfiguration().get("config").get().getAs(RandoresConfig.class).get();
    }

    public static boolean hasConfigObj() {
        Optional<JLSCValue> conf = Randores.getConfiguration().get("config");
        if(conf.isPresent()) {
            Optional<RandoresConfig> obj = conf.get().getAs(RandoresConfig.class);
            if(obj.isPresent()) {
                return true;
            }
        }

        return false;
    }

    private void loadConfig() {
        File conf = new File(this.confDir, "config.jlsc");
        RandoresConfig config = new RandoresConfig(300, false,
                new RandoresModules(true, true, false, false, true, false));
        try {
            if (!conf.exists()) {
                this.configuration = new JLSCConfiguration(new JLSCCompound().toConcurrent(), conf, JLSCFormat.TEXT, true);
                this.configuration.put("config", config);

                this.confDir.getAbsoluteFile().mkdirs();
                this.configuration.save();
            } else {
                this.configuration = JLSCConfiguration.fromText(conf);
                if(!hasConfigObj()) {
                    throw new IllegalStateException("Config does not have necessary ConfigObj mappings! (To fix this error, delete and re-generate your config)");
                }
            }
        } catch (Throwable e) {
            this.logger.error("FATAL ERROR: Failed to load randores configuration! Falling back to default values.", e);
            this.configuration = new JLSCConfiguration(new JLSCCompound().toConcurrent(), conf, JLSCFormat.TEXT, true);
            this.configuration.put("config", config);
        }
    }

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent ev) throws IOException {
        info("Randores is PreInitializing...",
                "Loading curse word dictionary...");
        Randores.offensiveWords.addAll(RandoresResourceManager.getResourceLines("offensive_words.txt"));
        info("Curse words loaded.",
                "Registering entities and recipes...");
        GameRegistry.registerFuelHandler(new RandoresFuelHandler());
        EntityRegistry.registerModEntity(new ResourceLocation("randores:randores_arrow"), RandoresArrow.class, "randores:randores_arrow", 0, this, 20, 1, true);
        GameRegistry.registerTileEntity(CraftiniumForgeTileEntity.class, "craftinium_forge");
        GameRegistry.registerTileEntity(RandoresTileEntity.class, "randores_data");

        MaterialDefinition.CRAFTING_MAPPINGS.put('S', Items.STICK);
        MaterialDefinition.CRAFTING_MAPPINGS.put('T', Item.getItemFromBlock(Blocks.TORCH));
        MaterialDefinition.CRAFTING_MAPPINGS.put('R', Items.STRING);

        CraftiniumRecipeRegistry.register(new CraftiniumDelegateRecipe());

        CraftiniumSmeltRegistry.register(new CraftiniumDelegateSmelt());
        CraftiniumSmeltRegistry.register(new RandoresForgeRecipe());

        info("Registered entities and recipes.",
                "Initializing network...");

        RandoresNetworking.initNetwork();

        info("Initialized network.",
                "Calling proxy PreInitialization...");

        Randores.PROXY.preInitSided(ev);
        info("Finished PreInitialization.");
    }

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent ev) {
        info("Randores is Initializing...",
                "Sending handler message to WAILA.");
        FMLInterModComms.sendMessage("waila", "register", "com.gmail.socraticphoenix.randores.mod.waila.RandoresWailaHandler.callbackRegister");
        if(Loader.isModLoaded("waila")) {
            info("WAILA was found and should have receieved the handler message.");
        } else {
            info("WAILA wasn't found. The handler message will be ignored.");
        }

        info("Registering up GUI handler and world generators...");
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new RandoresGuiHandler());
        GameRegistry.registerWorldGenerator(new RandoresWorldGenerator(), 10);
        GameRegistry.registerWorldGenerator(new RandoresAltarGenerator(), -100);
        info("Registered GUI hander and world generators.",
                "Calling proxy Initialization...");
        Randores.PROXY.initSided(ev);
        info("Finished Initialization.");
    }

    @Mod.EventHandler
    public void onPostInit(FMLPostInitializationEvent ev) {
        info("Randores is PostInitializing...", "" +
                "Registering abilities...");
        Iterator<Potion> iterator = Potion.REGISTRY.iterator();
        while (iterator.hasNext()) {
            Potion next = iterator.next();
            AbilityRegistry.register(new PotionEffectAbility(next));
        }
        info("Registered abilities.",
                "Calling proxy PostInitialization...");
        Randores.PROXY.postInitSided(ev);
        info("Finished PostInitialization.");
    }

    public static int getCount() {
        return Randores.getConfigObj().getCount();
    }

    public static void info(String... lines) {
        for(String l : lines) {
            info(l);
        }
    }

    public static void info(String info) {
        Randores.getLogger().info("Randores | " + info);
    }

    public static void warn(String... lines) {
        for(String l : lines) {
            warn(l);
        }
    }

    public static void warn(String warn) {
        Randores.getLogger().warn("Randores | " + warn);
    }

    public static Logger getLogger() {
        return Randores.INSTANCE.logger;
    }

    public static JLSCConfiguration getConfiguration() {
        return Randores.INSTANCE.configuration;
    }

    public static boolean containsOffensiveWord(String res) {
        for(String word : offensiveWords) {
            if(res.contains(word)) {
                return true;
            }
        }

        return false;
    }
}
