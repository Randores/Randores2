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
package com.gmail.socraticphoenix.randores.network.server.handler;

import com.gmail.socraticphoenix.randores.data.RandoresWorldData;
import com.gmail.socraticphoenix.randores.data.RandoresWorldData.Kind;
import com.gmail.socraticphoenix.randores.network.RandoresNetworking;
import com.gmail.socraticphoenix.randores.network.client.packet.RandoresDataRequest;
import com.gmail.socraticphoenix.randores.network.server.packet.RandoresBeginDataTransferRequest;
import com.gmail.socraticphoenix.randores.network.server.packet.RandoresClearDataCacheRequest;
import com.gmail.socraticphoenix.randores.network.server.packet.RandoresDefineByDataRequest;
import com.gmail.socraticphoenix.randores.network.server.packet.RandoresDefineBySeedRequest;
import com.gmail.socraticphoenix.randores.network.server.packet.RandoresEndDataTransferRequest;
import com.gmail.socraticphoenix.randores.network.server.packet.RandoresUpdatePluginSeedRequest;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class RandoresDataRequestHandler implements IMessageHandler<RandoresDataRequest, IMessage> {

    @Override
    public IMessage onMessage(RandoresDataRequest message, MessageContext ctx) {
        EntityPlayerMP player = ctx.getServerHandler().player;
        player.getServerWorld().addScheduledTask(() -> {
            RandoresNetworking.INSTANCE.sendTo(new RandoresClearDataCacheRequest().setDoClear(true), player);
            World world = player.world;
            RandoresWorldData data = RandoresWorldData.getSimply(world);
            if (data.getId().equals(message.getId())) {
                Kind kind = data.getKind();
                RandoresNetworking.INSTANCE.sendTo(new RandoresBeginDataTransferRequest().setId(data.getId()), player);
                data.getPluginSeeds().forEach((plugin, seed) -> {
                    RandoresNetworking.INSTANCE.sendTo(new RandoresUpdatePluginSeedRequest().setPlugin(plugin).setSeed(seed), player);
                });
                if (kind == Kind.SEEDED) {
                    RandoresNetworking.INSTANCE.sendTo(new RandoresDefineBySeedRequest().setSeed(data.getSeed()), player);
                } else if (kind == Kind.CUSTOM) {
                    data.getCache().stream().map(def -> new RandoresDefineByDataRequest().setDefinition(def)).forEach(req -> RandoresNetworking.INSTANCE.sendTo(req, player));
                }
                RandoresNetworking.INSTANCE.sendTo(new RandoresEndDataTransferRequest().setId(data.getId()), player);
            }

        });
        return null;
    }

}
