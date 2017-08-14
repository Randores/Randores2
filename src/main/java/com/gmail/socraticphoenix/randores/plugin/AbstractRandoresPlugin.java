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
package com.gmail.socraticphoenix.randores.plugin;

import com.gmail.socraticphoenix.randores.component.ability.AbilityRegistry;
import com.gmail.socraticphoenix.randores.component.craftable.CraftableRegistry;
import com.gmail.socraticphoenix.randores.component.enumerable.CraftableTypeRegistry;
import com.gmail.socraticphoenix.randores.component.enumerable.MaterialTypeRegistry;
import com.gmail.socraticphoenix.randores.component.enumerable.OreTypeRegistry;
import com.gmail.socraticphoenix.randores.component.post.MaterialDefinitionEditorRegistry;
import com.gmail.socraticphoenix.randores.component.property.PropertyRegistry;
import com.gmail.socraticphoenix.randores.component.tome.TomeHookRegistry;

public abstract class AbstractRandoresPlugin implements RandoresPlugin {
    private RandomContainer container;

    public AbstractRandoresPlugin() {
        this.container = new RandomContainer();
    }

    @Override
    public RandomContainer getRandomContainer() {
        return this.container;
    }

    @Override
    public void registerOreTypes(OreTypeRegistry registry) {

    }

    @Override
    public void registerMaterialTypes(MaterialTypeRegistry registry) {

    }

    @Override
    public void registerCraftables(CraftableTypeRegistry registry) {

    }

    @Override
    public void registerProperties(PropertyRegistry registry) {

    }

    @Override
    public void registerAbilities(AbilityRegistry registry) {

    }

    @Override
    public void registerCraftables(CraftableRegistry registry) {

    }

    @Override
    public void registerTomeHooks(TomeHookRegistry registry) {

    }

    @Override
    public void registerEditors(MaterialDefinitionEditorRegistry registry) {

    }

}
