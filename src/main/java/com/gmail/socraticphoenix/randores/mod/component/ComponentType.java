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
package com.gmail.socraticphoenix.randores.mod.component;

public class ComponentType {
    private CraftableType type;
    private boolean ore;

    private ComponentType(CraftableType type, boolean ore) {
        this.type = type;
        this.ore = ore;
    }

    public static ComponentType craftable(CraftableType type) {
        return new ComponentType(type, false);
    }

    public static ComponentType ore() {
        return new ComponentType(null, true);
    }

    public static ComponentType material() {
        return new ComponentType(null, false);
    }

    public boolean has(MaterialDefinition definition) {
        if(this.type != null) {
            return definition.getCraftables().stream().filter(c -> c.getType() == this.type).findFirst().isPresent();
        } else {
            return true;
        }
    }

    public Component from(MaterialDefinition definition) {
        if(this.type != null) {
            return definition.getCraftables().stream().filter(c -> c.getType() == this.type).findFirst().get();
        } else if (this.ore) {
            return definition.getOre();
        } else {
            return definition.getMaterial();
        }
    }

}
