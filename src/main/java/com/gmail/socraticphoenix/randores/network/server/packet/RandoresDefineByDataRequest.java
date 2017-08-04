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
package com.gmail.socraticphoenix.randores.network.server.packet;

import com.gmail.socraticphoenix.jlsc.JLSCArray;
import com.gmail.socraticphoenix.jlsc.JLSCException;
import com.gmail.socraticphoenix.jlsc.io.JLSCReadWriteUtil;
import com.gmail.socraticphoenix.pio.ByteStream;
import com.gmail.socraticphoenix.randores.component.MaterialDefinition;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.UUID;

public class RandoresDefineByDataRequest implements IMessage {
    private MaterialDefinition definition;

    public MaterialDefinition getDefinition() {
        return this.definition;
    }

    public RandoresDefineByDataRequest setDefinition(MaterialDefinition definition) {
        this.definition = definition;
        return this;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int index = buf.readInt();
        UUID id = new UUID(buf.readLong(), buf.readLong());
        int len = buf.readInt();
        byte[] val = new byte[len];
        buf.readBytes(val);

        try {
            MaterialDefinition definition = JLSCReadWriteUtil.readArray(ByteStream.of(val), LinkedHashMap::new, ArrayList::new).getAs(0, MaterialDefinition.class).get();
            definition.provideData(id, index);
            this.definition = definition;
        } catch (JLSCException e) {
            throw new IllegalStateException("This should be impossible!", e);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.definition.getIndex());
        UUID id = this.definition.getId();
        buf.writeLong(id.getMostSignificantBits());
        buf.writeLong(id.getLeastSignificantBits());

        try {
            byte[] val = JLSCArray.of(this.definition).writeBytes();
            buf.writeInt(val.length);
            buf.writeBytes(val);
        } catch (JLSCException e) {
            throw new IllegalStateException("This should be impossible!", e);
        }
    }

}
