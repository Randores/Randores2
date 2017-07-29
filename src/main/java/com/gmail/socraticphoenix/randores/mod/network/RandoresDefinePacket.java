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
package com.gmail.socraticphoenix.randores.mod.network;

import com.gmail.socraticphoenix.jlsc.JLSCArray;
import com.gmail.socraticphoenix.jlsc.JLSCException;
import com.gmail.socraticphoenix.jlsc.io.JLSCReadWriteUtil;
import com.gmail.socraticphoenix.pio.ByteStream;
import com.gmail.socraticphoenix.randores.mod.component.MaterialDefinition;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class RandoresDefinePacket implements IMessage {
    private MaterialDefinition definition;
    private long seed;
    private int index;

    public long getSeed() {
        return this.seed;
    }

    public RandoresDefinePacket setSeed(long seed) {
        this.seed = seed;
        return this;
    }

    public int getIndex() {
        return this.index;
    }

    public RandoresDefinePacket setIndex(int index) {
        this.index = index;
        return this;
    }

    public MaterialDefinition getDefinition() {
        return this.definition;
    }

    public RandoresDefinePacket setDefinition(MaterialDefinition definition) {
        this.definition = definition;
        this.setIndex(definition.getIndex()).setSeed(definition.getSeed());
        return this;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.setIndex(buf.readInt()).setSeed(buf.readLong());
        int len = buf.readInt();
        byte[] bytes = new byte[len];
        buf.readBytes(bytes);
        try {
            JLSCArray array = JLSCReadWriteUtil.readArray(ByteStream.of(bytes), LinkedHashMap::new, ArrayList::new);
            this.definition = array.get(0).get().getAs(MaterialDefinition.class).get();
        } catch (JLSCException e) {
            throw new RuntimeException(e);
        }

        this.definition.provideData(this.seed, this.index);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.index);
        buf.writeLong(this.seed);
        JLSCArray array = new JLSCArray();
        array.add(this.definition);
        try {
            byte[] arr = array.writeBytes();
            buf.writeInt(arr.length);
            buf.writeBytes(arr);
        } catch (JLSCException e) {
            throw new RuntimeException(e);
        }
    }
}
