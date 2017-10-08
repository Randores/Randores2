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
import com.gmail.socraticphoenix.mirror.Reflections;
import com.gmail.socraticphoenix.randores.block.RandoresBlocks;
import com.gmail.socraticphoenix.randores.block.RandoresTileEntity;
import com.gmail.socraticphoenix.randores.component.ability.AbilityRegistry;
import com.gmail.socraticphoenix.randores.component.ability.abilities.PotionEffectAbility;
import com.gmail.socraticphoenix.randores.component.craftable.factories.AestheticGenerator;
import com.gmail.socraticphoenix.randores.component.craftable.factories.ArmorGenerator;
import com.gmail.socraticphoenix.randores.component.craftable.factories.BattleaxeGenerator;
import com.gmail.socraticphoenix.randores.component.craftable.factories.BowGenerator;
import com.gmail.socraticphoenix.randores.component.craftable.factories.SledgehammerGenerator;
import com.gmail.socraticphoenix.randores.component.craftable.factories.StickGenerator;
import com.gmail.socraticphoenix.randores.component.craftable.factories.SwordGenerator;
import com.gmail.socraticphoenix.randores.component.craftable.factories.ToolGenerator;
import com.gmail.socraticphoenix.randores.component.enumerable.CraftableType;
import com.gmail.socraticphoenix.randores.component.enumerable.CraftableTypeRegistry;
import com.gmail.socraticphoenix.randores.component.enumerable.MaterialType;
import com.gmail.socraticphoenix.randores.component.enumerable.MaterialTypeRegistry;
import com.gmail.socraticphoenix.randores.component.enumerable.OreType;
import com.gmail.socraticphoenix.randores.component.enumerable.OreTypeRegistry;
import com.gmail.socraticphoenix.randores.component.enumerable.generators.DefaultMaterialTypeGenerator;
import com.gmail.socraticphoenix.randores.component.enumerable.generators.DefaultOreTypeGenerator;
import com.gmail.socraticphoenix.randores.component.post.MaterialDefinitionEditorRegistry;
import com.gmail.socraticphoenix.randores.component.property.PropertyRegistry;
import com.gmail.socraticphoenix.randores.component.property.RandoresFuelHandler;
import com.gmail.socraticphoenix.randores.component.property.properties.FlammableProperty.Generator;
import com.gmail.socraticphoenix.randores.component.tome.DefaultTomeHook;
import com.gmail.socraticphoenix.randores.component.tome.TomeHookRegistry;
import com.gmail.socraticphoenix.randores.config.RandoresConfig;
import com.gmail.socraticphoenix.randores.config.RandoresJLSC;
import com.gmail.socraticphoenix.randores.config.RandoresModules;
import com.gmail.socraticphoenix.randores.crafting.CraftingBlocks;
import com.gmail.socraticphoenix.randores.crafting.convert.CraftiniumConvertRegistry;
import com.gmail.socraticphoenix.randores.crafting.forge.CraftiniumForgeTileEntity;
import com.gmail.socraticphoenix.randores.crafting.forge.CraftiniumSmeltRegistry;
import com.gmail.socraticphoenix.randores.crafting.recipe.CraftiniumDelegateRecipe;
import com.gmail.socraticphoenix.randores.crafting.recipe.CraftiniumDelegateSmelt;
import com.gmail.socraticphoenix.randores.crafting.recipe.RandoresCraftiniumSmelt;
import com.gmail.socraticphoenix.randores.crafting.recipe.RandoresFromOreDictConvert;
import com.gmail.socraticphoenix.randores.crafting.recipe.RandoresToOreDictConvert;
import com.gmail.socraticphoenix.randores.crafting.table.CraftiniumRecipeRegistry;
import com.gmail.socraticphoenix.randores.entity.RandoresArrow;
import com.gmail.socraticphoenix.randores.gui.RandoresGuiHandler;
import com.gmail.socraticphoenix.randores.item.RandoresItems;
import com.gmail.socraticphoenix.randores.listener.EmpoweredArmorListener;
import com.gmail.socraticphoenix.randores.listener.LivingHurtListener;
import com.gmail.socraticphoenix.randores.listener.RandoresClientListener;
import com.gmail.socraticphoenix.randores.listener.RandoresPlayerListener;
import com.gmail.socraticphoenix.randores.listener.RandoresRegistryListener;
import com.gmail.socraticphoenix.randores.listener.RandoresWorldListener;
import com.gmail.socraticphoenix.randores.listener.ScheduleListener;
import com.gmail.socraticphoenix.randores.module.altar.RandoresAltarGenerator;
import com.gmail.socraticphoenix.randores.module.dungeon.RandoresLoot;
import com.gmail.socraticphoenix.randores.module.equip.RandoresMobEquip;
import com.gmail.socraticphoenix.randores.module.kit.RandoresStarterKit;
import com.gmail.socraticphoenix.randores.network.RandoresNetworking;
import com.gmail.socraticphoenix.randores.plugin.AbstractRandoresPlugin;
import com.gmail.socraticphoenix.randores.plugin.RandoresAddon;
import com.gmail.socraticphoenix.randores.plugin.RandoresAddonProvider;
import com.gmail.socraticphoenix.randores.plugin.RandoresPlugin;
import com.gmail.socraticphoenix.randores.plugin.RandoresPluginRegistry;
import com.gmail.socraticphoenix.randores.proxy.RandoresProxy;
import com.gmail.socraticphoenix.randores.resource.RandoresResourceManager;
import com.gmail.socraticphoenix.randores.tab.RandoresArmorTab;
import com.gmail.socraticphoenix.randores.tab.SimpleTab;
import net.minecraft.block.BlockStone;
import net.minecraft.block.BlockStone.EnumType;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.discovery.ASMDataTable.ASMData;
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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * The main class for the Randores mod, it is both the mod class and the root plugin. The root plugin is the
 * {@link RandoresPlugin} registered by Randores itself
 */
@Mod(modid = "randores")
@RandoresAddon
public class Randores extends AbstractRandoresPlugin {
    /**
     * The CreativeTabs for Randores armor
     */
    public static CreativeTabs TAB_ARMOR;
    /**
     * The CreativeTabs for Randores crafting
     */
    public static CreativeTabs TAB_CRAFTING;

    /**
     * The current {@link RandoresProxy} instance
     */
    @SidedProxy(modId = "randores", clientSide = "com.gmail.socraticphoenix.randores.proxy.RandoresClientProxy", serverSide = "com.gmail.socraticphoenix.randores.proxy.RandoresServerProxy")
    public static RandoresProxy PROXY = null;
    /**
     * The current {@link Randores} instance.
     */
    public static Randores INSTANCE = null;

    //A list of words that aren't allowed to show up in ore names
    private static List<String> offensiveWords = new ArrayList<>();
    //The logger
    private Logger logger;

    //The configuration directory
    private File confDir;
    //The configuration
    private JLSCConfiguration configuration;
    //Global custom definitions
    private JLSCConfiguration globalCustoms;

    public Randores() {
        Randores.INSTANCE = this;
        this.logger = LogManager.getLogger("Randores");

        info("Constructing Randores mod...");

        Randores.TAB_CRAFTING = new SimpleTab("randores_crafting", () -> new ItemStack(CraftingBlocks.tableItem));
        Randores.TAB_ARMOR = new RandoresArmorTab();

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
        MinecraftForge.EVENT_BUS.register(new LivingHurtListener());
        MinecraftForge.EVENT_BUS.register(new ScheduleListener());
        MinecraftForge.EVENT_BUS.register(new RandoresMobEquip());
        MinecraftForge.EVENT_BUS.register(new RandoresStarterKit());
        MinecraftForge.EVENT_BUS.register(new RandoresLoot());

        if (FMLCommonHandler.instance().getSide().isClient()) {
            MinecraftForge.EVENT_BUS.register(new RandoresClientListener());
        }

        info("Registered listeners.");
    }

    /**
     * @return An array containing the {@link MaterialType}s registered by Randores.
     */
    public static MaterialType[] getDefaultMaterials() {
        return new MaterialType[]{
                MaterialTypeRegistry.instance().get(RandoresKeys.INGOT),
                MaterialTypeRegistry.instance().get(RandoresKeys.GEM),
                MaterialTypeRegistry.instance().get(RandoresKeys.EMERALD),
                MaterialTypeRegistry.instance().get(RandoresKeys.CIRCLE_GEM),
                MaterialTypeRegistry.instance().get(RandoresKeys.SHARD),
                MaterialTypeRegistry.instance().get(RandoresKeys.DUST)
        };
    }

    /**
     * @return An array containing the {@link OreType}s registered by Randores.
     */
    public static OreType[] getDefaultOres() {
        return new OreType[]{
                OreTypeRegistry.instance().get(OreTypeRegistry.OVERWORLD),
                OreTypeRegistry.instance().get(OreTypeRegistry.END),
                OreTypeRegistry.instance().get(OreTypeRegistry.NETHER)
        };
    }

    /**
     * @return The {@link RandoresConfig} object representing the current configuration.
     */
    public static RandoresConfig getConfigObj() {
        return Randores.getConfiguration().get("config").get().getAs(RandoresConfig.class).get();
    }

    /**
     * @return True if the current configuration can be mapped to a {@link RandoresConfig} object, false otherwise.
     */
    public static boolean hasConfigObj() {
        Optional<JLSCValue> conf = Randores.getConfiguration().get("config");
        if (conf.isPresent()) {
            Optional<RandoresConfig> obj = conf.get().getAs(RandoresConfig.class);
            if (obj.isPresent()) {
                return true;
            }
        }

        return false;
    }

    //Loads the config
    private void loadConfig() {
        File conf = new File(this.confDir, "config.jlsc");
        RandoresConfig config = new RandoresConfig(300, 20, false, false,
                new RandoresModules(true, true, false, false, true, false));
        try {
            if (!conf.exists()) {
                this.configuration = new JLSCConfiguration(new JLSCCompound().toConcurrent(), conf, JLSCFormat.TEXT, true);
                this.configuration.put("config", config);

                this.confDir.getAbsoluteFile().mkdirs();
                this.configuration.save();
            } else {
                this.configuration = JLSCConfiguration.fromText(conf);
                if (!hasConfigObj()) {
                    throw new IllegalStateException("Config does not have necessary ConfigObj mappings! (To fix this error, delete and re-generate your config)");
                }
            }



            File custom1 = new File(this.confDir, "randores_custom.cjlsc");
            File custom2 = new File(this.confDir, "randores_custom.jlsc");
            if(custom1.exists()) {
                info("Found compressed global custom definitions... loading file...");
                JLSCConfiguration configuration = JLSCConfiguration.fromCompressed(custom1);
                info("Loaded definitions file. Definitions will be deserializedseparatelyy for each world.");
                this.globalCustoms = configuration;
            } else if (custom2.exists()) {
                info("Found global custom definitions... loading file...");
                JLSCConfiguration configuration = JLSCConfiguration.fromCompressed(custom2);
                info("Loaded definitions file. Definitions will be deserializedseparatelyy for each world.");
                this.globalCustoms = configuration;
            }
        } catch (Throwable e) {
            this.logger.error("FATAL ERROR: Failed to load randores configuration! Falling back to default values.", e);
            this.configuration = new JLSCConfiguration(new JLSCCompound().toConcurrent(), conf, JLSCFormat.TEXT, true);
            this.configuration.put("config", config);
        }
    }

    public static boolean hasGlobalCustomDefinitions() {
        return INSTANCE.globalCustoms != null;
    }

    public static JLSCConfiguration getGlobalCustoms() {
        return INSTANCE.globalCustoms;
    }

    //Formats the method class and name
    private static String methodNotation(Class owner, Method method) {
        return owner.getName() + "::" + method.getName();
    }

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent ev) throws IOException {
        info("Randores is PreInitializing...",
                "Loading all plugins...");

        ASMDataTable table = ev.getAsmData();
        String name = RandoresAddon.class.getCanonicalName();
        Set<ASMData> data = table.getAll(name);
        for (ASMData plugin : data) {
            Randores.info("Attempting to register plugins from: " + plugin.getClassName());
            Optional<Class> clazz = Reflections.resolveClass(plugin.getClassName());
            if (clazz.isPresent()) {
                Class target = clazz.get();
                for (Method method : target.getDeclaredMethods()) {
                    if (method.getAnnotation(RandoresAddonProvider.class) != null) {
                        if (method.getParameterCount() == 0) {
                            if (Modifier.isStatic(method.getModifiers())) {
                                if (Modifier.isPublic(method.getModifiers())) {
                                    if (RandoresPlugin.class.isAssignableFrom(method.getReturnType())) {
                                        try {
                                            RandoresPlugin pluginInst = (RandoresPlugin) method.invoke(null);
                                            if (pluginInst != null) {
                                                RandoresPluginRegistry.register(pluginInst);
                                                Randores.info("Successfully registered plugin from provider " + methodNotation(target, method));
                                            } else {
                                                Randores.warn("Failed to access plugin provider " + methodNotation(target, method) + ", it returned a null value");
                                            }
                                        } catch (IllegalAccessException | InvocationTargetException | ClassCastException e) {
                                            Randores.warn("Failed to access plugin provider " + methodNotation(target, method) + ", invocation failed", e);
                                        }
                                    } else {
                                        Randores.warn("Failed to access plugin provider " + methodNotation(target, method) + ", it doesn't return a RandoresPlugin");
                                    }
                                } else {
                                    Randores.warn("Failed to access plugin provider " + methodNotation(target, method) + ", it wasn't public");

                                }
                            } else {
                                Randores.warn("Failed to access plugin provider " + methodNotation(target, method) + ", it wasn't static");
                            }
                        } else {
                            Randores.warn("Failed to access plugin provider " + methodNotation(target, method) + ", it has more than 0 arguments");
                        }
                    }
                }
            } else {
                Randores.warn("Failed to load plugins from " + plugin.getClassName() + ", the class doesn't exist");
            }
        }

        info("Loaded plugins", "Initializing all plugins...");
        for (RandoresPlugin plugin : RandoresPluginRegistry.getPlugins()) {
            plugin.registerOreTypes(OreTypeRegistry.instance());
            plugin.registerMaterialTypes(MaterialTypeRegistry.instance());
            plugin.registerCraftables(CraftableTypeRegistry.instance());
            plugin.registerProperties(PropertyRegistry.instance());
            plugin.registerAbilities(AbilityRegistry.instance());
            plugin.registerTomeHooks(TomeHookRegistry.instance());
            plugin.registerEditors(MaterialDefinitionEditorRegistry.instance());
            info("Initialized plugin: " + plugin.id());
        }
        info("Initialized plugins.");

        info("Loading curse word dictionary...");
        Randores.offensiveWords.addAll(RandoresResourceManager.getResourceLines("offensive_words.txt"));
        info("Curse words loaded.",
                "Registering entities and recipes...");

        GameRegistry.registerFuelHandler(new RandoresFuelHandler());
        EntityRegistry.registerModEntity(new ResourceLocation("randores:randores_arrow"), RandoresArrow.class, "randores:randores_arrow", 0, this, 20, 1, true);
        GameRegistry.registerTileEntity(CraftiniumForgeTileEntity.class, "craftinium_forge");
        GameRegistry.registerTileEntity(RandoresTileEntity.class, "randores_data");

        CraftiniumRecipeRegistry.register(new CraftiniumDelegateRecipe());

        CraftiniumSmeltRegistry.register(new CraftiniumDelegateSmelt());
        CraftiniumSmeltRegistry.register(new RandoresCraftiniumSmelt());

        CraftiniumConvertRegistry.register(new RandoresFromOreDictConvert());
        CraftiniumConvertRegistry.register(new RandoresToOreDictConvert());

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
        FMLInterModComms.sendMessage("waila", "register", "com.gmail.socraticphoenix.randores.waila.RandoresWailaHandler.callbackRegister");
        if (Loader.isModLoaded("waila")) {
            info("WAILA was found and should have receieved the handler message.");
        } else {
            info("WAILA wasn't found. The handler message will be ignored.");
        }

        info("Registering up GUI handler and world generators...");
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new RandoresGuiHandler());
        GameRegistry.registerWorldGenerator(new RandoresWorldGenerator(), 10);
        GameRegistry.registerWorldGenerator(new RandoresAltarGenerator(), -100);
        info("Registered GUI hander and world generators.", "Calling proxy Initialization...");
        Randores.PROXY.initSided(ev);
        info("Finished Initialization.");
    }

    @Mod.EventHandler
    public void onPostInit(FMLPostInitializationEvent ev) {
        info("Randores is PostInitializing...");
        info("Calling proxy PostInitialization...");
        Randores.PROXY.postInitSided(ev);
        info("Finished PostInitialization.");
    }

    /**
     * @return The number of ores to generate.
     */
    public static int getCount() {
        return Randores.getConfigObj().getCount();
    }

    /**
     * Logs multiple lines of info.
     *
     * @param lines The lines to log.
     */
    public static void info(String... lines) {
        for (String l : lines) {
            info(l);
        }
    }

    /**
     * Logs multiple lines of debug, if debug is enabled.
     *
     * @param lines The lines to log.
     */
    public static void debug(String... lines) {
        if (Randores.getConfigObj().isDebug()) {
            for (String l : lines) {
                debug(l);
            }
        }
    }

    /**
     * Logs multiple lines of warnings.
     *
     * @param lines The lines to log.
     */
    public static void warn(String... lines) {
        if (Randores.getConfigObj().isDebug()) {
            for (String l : lines) {
                warn(l);
            }
        }
    }

    //The logger prefix - fixes a bug in Forge
    //TODO remove when the bug is fixed
    private String prefix = FMLCommonHandler.instance().getSide().isClient() ? "[Randores] " : "";

    /**
     * Logs a single line of debug, if debug is enabled.
     *
     * @param info The line to log.
     */
    public static void debug(String info) {
        if (Randores.getConfigObj().isDebug()) {
            Randores.getLogger().info(INSTANCE.prefix + "[Debug] " + info);
        }
    }

    /**
     * Logs a single line of info.
     *
     * @param info The line to log.
     */
    public static void info(String info) {
        Randores.getLogger().info(INSTANCE.prefix + info);
    }

    /**
     * Logs a single line of warning.
     *
     * @param info The line to log.
     */
    public static void warn(String info) {
        Randores.getLogger().info(INSTANCE.prefix + "[Warn] " + info);
    }

    /**
     * Logs a single line of warning, along with the stacktrace of the given Throwable.
     *
     * @param info The line to log.
     * @param exception The throwable to log.
     */
    public static void warn(String info, Throwable exception) {
        Randores.getLogger().info(INSTANCE.prefix + "[Warn] " + info, exception);
    }

    /**
     * @return The Randores logger.
     */
    public static Logger getLogger() {
        return Randores.INSTANCE.logger;
    }

    /**
     * @return The Randores configuration. Generally, {@link Randores#getConfigObj()} should be used instead of this method.
     */
    public static JLSCConfiguration getConfiguration() {
        return Randores.INSTANCE.configuration;
    }

    /**
     * Checks if the given string contains one of the words listed as potentially offensive by Randores.
     *
     * @param res The string to check.
     * @return True if the string contains offensive words, false otherwise.
     */
    public static boolean containsOffensiveWord(String res) {
        res = res.toLowerCase();
        for (String word : offensiveWords) {
            if (res.contains(word)) {
                return true;
            }
        }

        return false;
    }

    //Begin plugin

    /**
     * @return {@link Randores#INSTANCE}.
     */
    @RandoresAddonProvider
    public static Randores instance() {
        return INSTANCE;
    }

    @Override
    public void registerOreTypes(OreTypeRegistry registry) {
        registry.register(
                new OreType(w -> w.provider.getDimension() == 0, OreTypeRegistry.OVERWORLD, b -> b.getBlock() == Blocks.STONE && b.getValue(BlockStone.VARIANT) == EnumType.STONE),
                new OreType(w -> w.provider.getDimension() == 1, OreTypeRegistry.END, b -> b.getBlock() == Blocks.END_STONE),
                new OreType(w -> w.provider.getDimension() == -1, OreTypeRegistry.NETHER, b -> b.getBlock() == Blocks.NETHERRACK)
        );
        registry.register(new DefaultOreTypeGenerator());
    }

    @Override
    public void registerMaterialTypes(MaterialTypeRegistry registry) {
        registry.register(
                new MaterialType(RandoresKeys.INGOT, "ore", "ingot", true),
                new MaterialType(RandoresKeys.GEM, "gem_ore", "gem", false),
                new MaterialType(RandoresKeys.EMERALD, "emerald_ore", "gem", false),
                new MaterialType(RandoresKeys.CIRCLE_GEM, "circle_ore", "gem", false),
                new MaterialType(RandoresKeys.SHARD, "shard_ore", "shard", false),
                new MaterialType(RandoresKeys.DUST, "dust_ore", "dust", false)
        );
        registry.register(
                new DefaultMaterialTypeGenerator(RandoresKeys.INGOT, 100),
                new DefaultMaterialTypeGenerator(RandoresKeys.GEM, 60),
                new DefaultMaterialTypeGenerator(RandoresKeys.EMERALD, 50),
                new DefaultMaterialTypeGenerator(RandoresKeys.CIRCLE_GEM, 40),
                new DefaultMaterialTypeGenerator(RandoresKeys.SHARD, 30),
                new DefaultMaterialTypeGenerator(RandoresKeys.DUST, 20)
        );
    }

    @Override
    public void registerCraftables(CraftableTypeRegistry registry) {
        registry.register(
                new CraftableType(RandoresKeys.AXE, false, true, true, false, true, true, EntityEquipmentSlot.MAINHAND, () -> RandoresItems.axe),
                new CraftableType(RandoresKeys.HOE, false, true, true, false, true, false, EntityEquipmentSlot.MAINHAND, () -> RandoresItems.hoe),
                new CraftableType(RandoresKeys.PICKAXE, false, true, true, false, true, true, EntityEquipmentSlot.MAINHAND, () -> RandoresItems.pickaxe),
                new CraftableType(RandoresKeys.SHOVEL, false, true, true, false, true, true, EntityEquipmentSlot.MAINHAND, () -> RandoresItems.shovel),
                new CraftableType(RandoresKeys.HELMET, false, true, true, true, false, false, EntityEquipmentSlot.HEAD, () -> RandoresItems.helmet),
                new CraftableType(RandoresKeys.CHESTPLATE, false, true, true, true, false, false, EntityEquipmentSlot.CHEST, () -> RandoresItems.chestplate),
                new CraftableType(RandoresKeys.LEGGINGS, false, true, true, true, false, false, EntityEquipmentSlot.LEGS, () -> RandoresItems.leggings),
                new CraftableType(RandoresKeys.BOOTS, false, true, true, true, false, false, EntityEquipmentSlot.FEET, () -> RandoresItems.boots),
                new CraftableType(RandoresKeys.SWORD, false, true, true, false, true, false, EntityEquipmentSlot.MAINHAND, () -> RandoresItems.sword),
                new CraftableType(RandoresKeys.BATTLEAXE, false, true, true, false, true, true, EntityEquipmentSlot.MAINHAND, () -> RandoresItems.battleaxe),
                new CraftableType(RandoresKeys.SLEDGEHAMMER, false, true, true, false, true, false, EntityEquipmentSlot.MAINHAND, () -> RandoresItems.sledgehammer),
                new CraftableType(RandoresKeys.BOW, false, true, true, false, false, false, EntityEquipmentSlot.MAINHAND, () -> RandoresItems.bow),
                new CraftableType(RandoresKeys.STICK, false, false, false, false, false, false, EntityEquipmentSlot.MAINHAND, () -> RandoresItems.stick),
                new CraftableType(RandoresKeys.BRICKS, true, false, false, false, false, false, EntityEquipmentSlot.MAINHAND, () -> RandoresBlocks.brickItem),
                new CraftableType(RandoresKeys.TORCH, true, false, false, false, false, false, EntityEquipmentSlot.MAINHAND, () -> RandoresBlocks.torchItem)
        );
        registry.register(new AestheticGenerator(),
                new ArmorGenerator(),
                new BattleaxeGenerator(),
                new BowGenerator(),
                new SledgehammerGenerator(),
                new StickGenerator(),
                new SwordGenerator(),
                new ToolGenerator());
    }

    @Override
    public void registerProperties(PropertyRegistry registry) {
        registry.register(new Generator());
    }

    @Override
    public void registerAbilities(AbilityRegistry registry) {
        registry.register(new PotionEffectAbility.Generator());
    }


    @Override
    public void registerTomeHooks(TomeHookRegistry registry) {
        registry.register(new DefaultTomeHook());
    }

}
