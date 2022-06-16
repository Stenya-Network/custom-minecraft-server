package net.stenya.cacheoverflow.minecraftserver.network.channel

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import net.stenya.cacheoverflow.minecraftserver.network.protocol.packet.IPacket

class ServerChannelHandler: SimpleChannelInboundHandler<IPacket>() {

    override fun channelRead0(context: ChannelHandlerContext, packet: IPacket) {

    }


}