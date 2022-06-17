package net.stenya.cacheoverflow.minecraftserver.network.channel

import com.google.inject.Injector
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.timeout.IdleStateHandler
import io.netty.handler.timeout.ReadTimeoutHandler
import net.stenya.cacheoverflow.minecraftserver.network.channel.handlers.FramingHandler
import net.stenya.cacheoverflow.minecraftserver.network.channel.handlers.PacketHandler
import net.stenya.cacheoverflow.minecraftserver.network.channel.handlers.PassThroughHandler

class ServerChannelInitializer(private val injector: Injector): ChannelInitializer<SocketChannel>() {

    override fun initChannel(channel: SocketChannel) {
        channel.pipeline()
            .addLast("encryption", PassThroughHandler.INSTANCE)
            .addLast("framing", FramingHandler())
            .addLast("compression", PassThroughHandler.INSTANCE)
            .addLast("codec", PacketHandler(this.injector))
            .addLast("readtimeout", ReadTimeoutHandler(30))
            .addLast(IdleStateHandler(0, 15, 0))
            .addLast(this.injector.getInstance(ServerChannelHandler::class.java))
    }


}