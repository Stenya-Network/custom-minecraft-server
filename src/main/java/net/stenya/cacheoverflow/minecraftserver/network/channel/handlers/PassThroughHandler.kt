package net.stenya.cacheoverflow.minecraftserver.network.channel.handlers

import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.ChannelHandlerAdapter

@Sharable
class PassThroughHandler: ChannelHandlerAdapter() {

    companion object {
        @JvmStatic val INSTANCE: PassThroughHandler = PassThroughHandler()
    }

}