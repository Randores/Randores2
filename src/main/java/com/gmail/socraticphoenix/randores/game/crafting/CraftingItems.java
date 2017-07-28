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
package com.gmail.socraticphoenix.randores.game.crafting;

import com.gmail.socraticphoenix.randores.Randores;
import com.gmail.socraticphoenix.randores.game.tab.TomeTab;
import com.gmail.socraticphoenix.randores.game.tome.ItemTome;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;

public class CraftingItems {

    public static Item craftiniumLump;
    public static Item tome;

    public static List<Item> items;

    public static void init() {
        items = new ArrayList<>();

        craftiniumLump = new Item().setUnlocalizedName("craftinium_lump").setCreativeTab(Randores.TAB_CRAFTING);
        items.add(craftiniumLump);

        tome = new ItemTome().setUnlocalizedName("material_tome").setMaxStackSize(1);
        tome.setCreativeTab(new TomeTab());
        items.add(tome);

        for(Item item : items) {
            item.setRegistryName(item.getUnlocalizedName().substring(5));
        }
    }

    public static void register(IForgeRegistry<Item> registry) {
        for(Item item : items) {
            registry.register(item);
        }
    }

    public static void registerModels() {
        for(Item item : items) {
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation("randores:" + item.getUnlocalizedName().substring(5), "inventory"));
        }
    }

}
