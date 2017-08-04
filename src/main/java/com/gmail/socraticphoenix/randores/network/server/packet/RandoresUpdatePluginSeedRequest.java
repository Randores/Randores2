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

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.nio.charset.StandardCharsets;

public class RandoresUpdatePluginSeedRequest implements IMessage {
    private String plugin;
    private long seed;

    public RandoresUpdatePluginSeedRequest() {
        this.plugin = "";
        this.seed = 0;
    }

    public String getPlugin() {
        return this.plugin;
    }

    public RandoresUpdatePluginSeedRequest setPlugin(String plugin) {
        this.plugin = plugin;
        return this;
    }

    public long getSeed() {
        return this.seed;
    }

    public RandoresUpdatePluginSeedRequest setSeed(long seed) {
        this.seed = seed;
        return this;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int len = buf.readInt();
        byte[] encodedKey = new byte[len];
        buf.readBytes(encodedKey);
        this.setPlugin(new String(encodedKey, StandardCharsets.UTF_8));
        this.setSeed(buf.readLong());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        byte[] encodedKey = this.plugin.getBytes(StandardCharsets.UTF_8);
        buf.writeInt(encodedKey.length);
        buf.writeBytes(encodedKey);
        buf.writeLong(this.seed);
    }

}
