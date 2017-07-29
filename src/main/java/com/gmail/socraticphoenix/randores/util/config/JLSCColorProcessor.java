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
package com.gmail.socraticphoenix.randores.util.config;

import com.gmail.socraticphoenix.jlsc.JLSCArray;
import com.gmail.socraticphoenix.jlsc.JLSCException;
import com.gmail.socraticphoenix.jlsc.value.processors.JLSCNamedProcessor;

import java.awt.Color;

public class JLSCColorProcessor extends JLSCNamedProcessor<Color> {

    public JLSCColorProcessor() {
        super(Color.class, "color", false);
    }

    @Override
    protected void write(Color color, JLSCArray jlscArray) throws JLSCException {
        jlscArray.add(color.getRed());
        jlscArray.add(color.getGreen());
        jlscArray.add(color.getBlue());
    }

    @Override
    protected Color read(JLSCArray jlscArray) throws JLSCException {
        return new Color(jlscArray.getInteger(0, 0), jlscArray.getInteger(1, 0), jlscArray.getInteger(2, 0));
    }

}
