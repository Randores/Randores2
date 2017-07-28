/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 socraticphoenix@gmail.com
 * Copyright (c) 2016 contributors
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


import com.gmail.socraticphoenix.randores.mod.component.MaterialDefinitionGenerator;
import com.gmail.socraticphoenix.randores.mod.data.RandoresSeed;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RandoresSeedPacketHandler implements IMessageHandler<RandoresSeedPacket, IMessage> {

    @Override
    @SideOnly(Side.CLIENT)
    public IMessage onMessage(final RandoresSeedPacket message, final MessageContext ctx) {
        Runnable runnable = new Runnable() {
            @Override
            @SideOnly(Side.CLIENT)
            public void run() {
                final long seed = message.getSeed();
                final int count = message.getOreCount();
                MaterialDefinitionGenerator.removeAllExcept(seed);
                RandoresSeed.setClientSeed(seed);
                MaterialDefinitionGenerator.registerDefinitionsIfNeeded(seed, count);
            }
        };
        Minecraft.getMinecraft().addScheduledTask(runnable);
        return null;
    }

}
