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
package com.gmail.socraticphoenix.randores.listener;

import com.gmail.socraticphoenix.randores.Randores;
import com.gmail.socraticphoenix.randores.block.RandoresBlocks;
import com.gmail.socraticphoenix.randores.component.ability.EmpoweredEnchantment;
import com.gmail.socraticphoenix.randores.crafting.CraftingBlocks;
import com.gmail.socraticphoenix.randores.crafting.CraftingItems;
import com.gmail.socraticphoenix.randores.item.RandoresItems;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RandoresRegistryListener {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onBlockRegister(RegistryEvent.Register<Block> ev) {
        Randores.info("Registering blocks...");
        RandoresBlocks.init();
        CraftingBlocks.init();

        CraftingBlocks.registerBlocks(ev.getRegistry());
        RandoresBlocks.registerBlocks(ev.getRegistry());
        Randores.info("Registered blocks.");
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onItemRegister(RegistryEvent.Register<Item> ev) {
        Randores.info("Registering items...");
        RandoresItems.init();
        CraftingItems.init();

        CraftingBlocks.registerItems(ev.getRegistry());
        CraftingItems.register(ev.getRegistry());

        RandoresBlocks.registerItems(ev.getRegistry());
        RandoresItems.register(ev.getRegistry());
        Randores.info("Registered items.");
    }

    @SubscribeEvent
    public void onEnchantRegister(RegistryEvent.Register<Enchantment> ev) {
        Randores.info("Registering enchantments...");
        ev.getRegistry().register(EmpoweredEnchantment.INSTANCE);
        Randores.info("Registered enchantments.");
    }

}
