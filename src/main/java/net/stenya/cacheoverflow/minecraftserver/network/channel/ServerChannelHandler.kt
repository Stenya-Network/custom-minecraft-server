package net.stenya.cacheoverflow.minecraftserver.network.channel

import com.google.inject.Inject
import com.google.inject.Injector
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import net.stenya.cacheoverflow.minecraftserver.MinecraftServer
import net.stenya.cacheoverflow.minecraftserver.network.protocol.packet.IPacket
import net.stenya.cacheoverflow.minecraftserver.network.protocol.packet.Protocol
import net.stenya.cacheoverflow.minecraftserver.player.session.INetworkSessionFactory
import java.net.InetSocketAddress
import java.util.function.Predicate

class ServerChannelHandler @Inject constructor(private val injector: Injector): SimpleChannelInboundHandler<IPacket>() {

    private val protocol: Protocol = this.injector.getInstance(Protocol::class.java)

    override fun channelRead0(ctx: ChannelHandlerContext?, msg: IPacket?) {
        TODO("Not yet implemented")
    }

    override fun channelActive(context: ChannelHandlerContext) {
        this.injector.getInstance(MinecraftServer::class.java).networkSessionRegistry.register(arrayOf(
            this.injector.getInstance(INetworkSessionFactory::class.java).createSession(context.channel(), context.channel().remoteAddress() as InetSocketAddress)
        ))
        println("Channel opened: Session created!")
        protocol.state(0x01)
    }

    override fun channelInactive(context: ChannelHandlerContext) {
        val address: InetSocketAddress = context.channel().remoteAddress() as InetSocketAddress
        this.injector.getInstance(MinecraftServer::class.java).networkSessionRegistry.unregisterIf { it.address == address }
        println("Channel closed: Session deleted!")
    }


}