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
package com.gmail.socraticphoenix.randores.network;

import com.gmail.socraticphoenix.randores.network.client.handler.RandoresBeginDataTransferRequestHandler;
import com.gmail.socraticphoenix.randores.network.client.handler.RandoresClearDataCacheRequestHandler;
import com.gmail.socraticphoenix.randores.network.client.handler.RandoresDataNeededQueryHandler;
import com.gmail.socraticphoenix.randores.network.client.handler.RandoresDefineByDataRequestHandler;
import com.gmail.socraticphoenix.randores.network.client.handler.RandoresDefineBySeedRequestHandler;
import com.gmail.socraticphoenix.randores.network.client.handler.RandoresEndDataTransferRequestHandler;
import com.gmail.socraticphoenix.randores.network.client.handler.RandoresUpdatePluginSeedRequestHandler;
import com.gmail.socraticphoenix.randores.network.client.packet.RandoresDataRequest;
import com.gmail.socraticphoenix.randores.network.server.handler.RandoresDataRequestHandler;
import com.gmail.socraticphoenix.randores.network.server.packet.RandoresBeginDataTransferRequest;
import com.gmail.socraticphoenix.randores.network.server.packet.RandoresClearDataCacheRequest;
import com.gmail.socraticphoenix.randores.network.server.packet.RandoresDataNeededQuery;
import com.gmail.socraticphoenix.randores.network.server.packet.RandoresDefineByDataRequest;
import com.gmail.socraticphoenix.randores.network.server.packet.RandoresDefineBySeedRequest;
import com.gmail.socraticphoenix.randores.network.server.packet.RandoresEndDataTransferRequest;
import com.gmail.socraticphoenix.randores.network.server.packet.RandoresUpdatePluginSeedRequest;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class RandoresNetworking {
    private static byte netId = 0;
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("randores");

    public static void initNetwork() {
        INSTANCE.registerMessage(RandoresDataRequestHandler.class, RandoresDataRequest.class, netId++, Side.SERVER);
        INSTANCE.registerMessage(RandoresBeginDataTransferRequestHandler.class, RandoresBeginDataTransferRequest.class, netId++, Side.CLIENT);
        INSTANCE.registerMessage(RandoresClearDataCacheRequestHandler.class, RandoresClearDataCacheRequest.class, netId++, Side.CLIENT);
        INSTANCE.registerMessage(RandoresDataNeededQueryHandler.class, RandoresDataNeededQuery.class, netId++, Side.CLIENT);
        INSTANCE.registerMessage(RandoresDefineByDataRequestHandler.class, RandoresDefineByDataRequest.class, netId++, Side.CLIENT);
        INSTANCE.registerMessage(RandoresDefineBySeedRequestHandler.class, RandoresDefineBySeedRequest.class, netId++, Side.CLIENT);
        INSTANCE.registerMessage(RandoresEndDataTransferRequestHandler.class, RandoresEndDataTransferRequest.class, netId++, Side.CLIENT);
        INSTANCE.registerMessage(RandoresUpdatePluginSeedRequestHandler.class, RandoresUpdatePluginSeedRequest.class, netId++, Side.CLIENT);
    }


}
