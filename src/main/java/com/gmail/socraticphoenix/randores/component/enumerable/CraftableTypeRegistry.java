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
package com.gmail.socraticphoenix.randores.component.enumerable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CraftableTypeRegistry {
    private static final CraftableTypeRegistry instance = new CraftableTypeRegistry();

    private Map<String, CraftableType> craftableTypes = new HashMap<>();
    private List<CraftableType> craftablesAsList = new ArrayList<>();

    public  static CraftableTypeRegistry instance() {
        return instance;
    }

    public void register(CraftableType... CraftableTypes) {
        for(CraftableType CraftableType : CraftableTypes) {
            register(CraftableType);
        }
    }

    public void register(CraftableType CraftableType) {
        if(craftableTypes.containsKey(CraftableType.getName())) {
            throw new IllegalArgumentException("Attempted to register duplicate CraftableType: " + CraftableType.getName());
        }

        craftableTypes.put(CraftableType.getName(), CraftableType);
        craftablesAsList.add(CraftableType);
    }

    public CraftableType get(String CraftableType) {
        return craftableTypes.get(CraftableType);
    }

    public Map<String, CraftableType> getCraftableTypes() {
        return craftableTypes;
    }

    public List<CraftableType> values() {
        return craftablesAsList;
    }

}
