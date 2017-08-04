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
package com.gmail.socraticphoenix.randores.config;

import com.gmail.socraticphoenix.jlsc.JLSCArray;
import com.gmail.socraticphoenix.jlsc.JLSCException;
import com.gmail.socraticphoenix.jlsc.value.processors.JLSCNamedProcessor;
import com.gmail.socraticphoenix.randores.Randores;
import com.gmail.socraticphoenix.randores.RandoresKeys;
import com.gmail.socraticphoenix.randores.component.enumerable.MaterialType;
import com.gmail.socraticphoenix.randores.component.enumerable.MaterialTypeRegistry;

public class MaterialTypeProcessor extends JLSCNamedProcessor<MaterialType> {

    public MaterialTypeProcessor() {
        super(MaterialType.class, "material_type", false);
    }

    @Override
    protected void write(MaterialType materialType, JLSCArray jlscArray) throws JLSCException {
        jlscArray.add(materialType.getName());
    }

    @Override
    protected MaterialType read(JLSCArray jlscArray) throws JLSCException {
        String name = jlscArray.getString(0).get();
        MaterialType res = MaterialTypeRegistry.get(name);
        if(res == null) {
            Randores.warn("UNKNOWN MATERIAL TYPE " + name + ", LOADING AS INGOT, THIS MAY CAUSE PROBLEMS!");
            res = MaterialTypeRegistry.get(RandoresKeys.INGOT);
        }

        return res;
    }

}
