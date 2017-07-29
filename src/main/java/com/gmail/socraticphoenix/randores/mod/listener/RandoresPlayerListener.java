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
package com.gmail.socraticphoenix.randores.mod.listener;

import com.gmail.socraticphoenix.randores.Randores;
import com.gmail.socraticphoenix.randores.mod.component.MaterialDefinition;
import com.gmail.socraticphoenix.randores.mod.component.MaterialDefinitionRegistry;
import com.gmail.socraticphoenix.randores.mod.data.RandoresSeed;
import com.gmail.socraticphoenix.randores.mod.network.RandoresDefinePacket;
import com.gmail.socraticphoenix.randores.mod.network.RandoresNetworking;
import com.gmail.socraticphoenix.randores.mod.network.RandoresSeedPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RandoresPlayerListener {
    private Map<UUID, Long> playersSeed = new HashMap<>();

    @SubscribeEvent
    public void onLeave(PlayerEvent.PlayerLoggedOutEvent ev) {
        EntityPlayer player = ev.player;
        if (player instanceof EntityPlayerMP) {
            this.playersSeed.remove(ev.player.getUniqueID());
        }
    }

    @SubscribeEvent
    public void onSwitch(PlayerEvent.PlayerChangedDimensionEvent ev) {
        EntityPlayer player = ev.player;
        if (player instanceof EntityPlayerMP) {
            EntityPlayerMP playerMP = (EntityPlayerMP) player;
            World world = ((EntityPlayerMP) player).world;
            UUID id = playerMP.getUniqueID();
            long seed = RandoresSeed.getSeed(world);
            String name = world.getWorldInfo().getWorldName();
            if (!this.playersSeed.containsKey(id) || this.playersSeed.get(id) != seed) {
                if(MaterialDefinitionRegistry.containsCustom(name)) {
                    Randores.info("Sending custom definition data to player " + playerMP.getName() + "...");
                    List<MaterialDefinition> definitions = MaterialDefinitionRegistry.getCustom(name);
                    RandoresNetworking.INSTANCE.sendTo(new RandoresSeedPacket().setSeed(0), playerMP);
                    for(MaterialDefinition definition : definitions) {
                        RandoresNetworking.INSTANCE.sendTo(new RandoresDefinePacket().setDefinition(definition), playerMP);
                    }
                    Randores.info("Sent custom definition data to player " + playerMP.getName() + ".");
                } else {
                    Randores.info("Sending seed " + seed + " and ore count " + Randores.getCount() + " to player " + playerMP.getName() + ".");
                    RandoresNetworking.INSTANCE.sendTo(new RandoresSeedPacket().setSeed(seed).setOreCount(Randores.getCount()), playerMP);
                }
            }
            this.playersSeed.put(id, seed);
        }
    }

    @SubscribeEvent
    public void onSpawn(PlayerEvent.PlayerLoggedInEvent ev) {
        EntityPlayer player = ev.player;
        if (player instanceof EntityPlayerMP) {
            EntityPlayerMP playerMP = (EntityPlayerMP) player;
            World world = ((EntityPlayerMP) player).world;
            UUID id = playerMP.getUniqueID();
            long seed = RandoresSeed.getSeed(world);
            String name = world.getWorldInfo().getWorldName();
            if (!this.playersSeed.containsKey(id) || this.playersSeed.get(id) != seed) {
                if(MaterialDefinitionRegistry.containsCustom(name)) {
                    Randores.info("Sending custom definition data to player " + playerMP.getName() + "...");
                    List<MaterialDefinition> definitions = MaterialDefinitionRegistry.getCustom(name);
                    RandoresNetworking.INSTANCE.sendTo(new RandoresSeedPacket().setSeed(0), playerMP);
                    for(MaterialDefinition definition : definitions) {
                        RandoresNetworking.INSTANCE.sendTo(new RandoresDefinePacket().setDefinition(definition), playerMP);
                    }
                    Randores.info("Sent custom definition data to player " + playerMP.getName() + ".");
                } else {
                    Randores.info("Sending seed " + seed + " and ore count " + Randores.getCount() + " to player " + playerMP.getName() + ".");
                    RandoresNetworking.INSTANCE.sendTo(new RandoresSeedPacket().setSeed(seed).setOreCount(Randores.getCount()), playerMP);
                }
            }
            this.playersSeed.put(id, seed);
        }
    }

}