package net.stenya.cacheoverflow.minecraftserver.network.channel

import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelPipeline
import io.netty.channel.socket.SocketChannel

class ServerChannelInitializer: ChannelInitializer<SocketChannel>() {

    override fun initChannel(channel: SocketChannel) {
        val pipeline: ChannelPipeline = channel.pipeline()
        pipeline.addLast(ServerChannelHandler())
    }

}