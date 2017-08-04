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
package com.gmail.socraticphoenix.randores.component;

import com.gmail.socraticphoenix.randores.component.craftable.CraftableComponent;
import com.gmail.socraticphoenix.randores.component.enumerable.CraftableType;
import com.gmail.socraticphoenix.randores.component.enumerable.CraftableTypeRegistry;

import java.util.HashMap;
import java.util.Map;

public class ComponentType {
    private static Map<CraftableType, ComponentType> components;
    private static ComponentType oreType = new ComponentType(null, true);
    private static ComponentType materialType = new ComponentType(null, false);

    static {
        components = new HashMap<>();
        for (CraftableType type : CraftableTypeRegistry.instance().values()) {
            components.put(type, new ComponentType(type, false));
        }
    }

    private CraftableType type;
    private boolean ore;

    private ComponentType(CraftableType type, boolean ore) {
        this.type = type;
        this.ore = ore;
    }

    public static ComponentType craftable(CraftableType type) {
        return components.computeIfAbsent(type, k -> new ComponentType(type, false));
    }

    public static ComponentType craftable(String key) {
        return craftable(CraftableTypeRegistry.instance().get(key));
    }

    public static ComponentType ore() {
        return oreType;
    }

    public static ComponentType material() {
        return materialType;
    }

    public boolean has(MaterialDefinition definition) {
        if (this.type != null) {
            return definition.getCraftables().stream().anyMatch(c -> c.getType() == this.type);
        } else {
            return true;
        }
    }

    public Component from(MaterialDefinition definition) {
        if (this.type != null) {
            return definition.getCraftables().stream().filter(c -> c.getType() == this.type).findFirst().orElse(null);
        } else if (this.ore) {
            return definition.getOre();
        } else {
            return definition.getMaterial();
        }
    }

    public boolean is(Component component) {
        if (this.type != null) {
            return component instanceof CraftableComponent && ((CraftableComponent) component).getType() == this.type;
        } else if (this.ore) {
            return component instanceof OreComponent;
        } else {
            return component instanceof MaterialComponent;
        }
    }
}
