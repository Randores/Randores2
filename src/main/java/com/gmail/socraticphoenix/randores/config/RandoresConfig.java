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


import com.gmail.socraticphoenix.jlsc.serialization.annotation.Name;
import com.gmail.socraticphoenix.jlsc.serialization.annotation.Serializable;
import com.gmail.socraticphoenix.jlsc.serialization.annotation.SerializationConstructor;
import com.gmail.socraticphoenix.jlsc.serialization.annotation.Serialize;

@Serializable
public class RandoresConfig {
    @Serialize(value = "count", reflect = false, comments = "The number of ores to create in a single player world")
    private int count;
    @Serialize(value = "maxOres", reflect = false, comments = "The maximum number of ores to generate per chunk")
    private int maxOres;
    @Serialize(value = "convert", reflect = false, comments = {"If true, generated definitions will be saved as custom ones", "After first being generated, these definitions will behave like custom definitions"})
    private boolean convert;
    @Serialize(value = "debug", reflect = false, comments = "If true, randores will print out lots of information about everything it does")
    private boolean debug;
    @Serialize(value = "modules", reflect = false)
    private RandoresModules modules;

    @SerializationConstructor
    public RandoresConfig(@Name("count") int count, @Name("maxOres") int maxOres, @Name("convert") boolean convert, @Name("debug") boolean debug, @Name("modules") RandoresModules modules) {
        this.count = count;
        this.modules = modules;
        this.convert = convert;
        this.debug = debug;
        this.maxOres = maxOres;
    }

    public int getMaxOres() {
        return this.maxOres;
    }

    public boolean isDebug() {
        return this.debug;
    }

    public boolean isConvert() {
        return this.convert;
    }

    public int getCount() {
        return this.count;
    }

    public RandoresModules getModules() {
        return this.modules;
    }

}
