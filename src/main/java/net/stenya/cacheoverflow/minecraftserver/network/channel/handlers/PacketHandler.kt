package net.stenya.cacheoverflow.minecraftserver.network.channel.handlers

import com.google.inject.Injector
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.DecoderException
import io.netty.handler.codec.MessageToMessageCodec
import net.stenya.cacheoverflow.minecraftserver.MinecraftServer
import net.stenya.cacheoverflow.minecraftserver.network.protocol.packet.IPacket
import net.stenya.cacheoverflow.minecraftserver.player.session.INetworkSession
import net.stenya.cacheoverflow.minecraftserver.utils.ByteBufUtils
import java.util.function.Predicate

class PacketHandler(private val injector: Injector): MessageToMessageCodec<ByteBuf, IPacket>() {

    override fun encode(context: ChannelHandlerContext, inputPacket: IPacket, outputData: MutableList<Any>) {
        val session: INetworkSession = this.injector.getInstance(MinecraftServer::class.java).networkSessionRegistry
            .findFirstOr({ it.address == context.channel().remoteAddress() })
            ?: throw IllegalStateException("No session created but client sends packet!")

        val id: Int = session.protocol.packet(inputPacket.javaClass)
        val outputBuffer: ByteBuf = context.alloc().buffer()
        ByteBufUtils.writeVarInt(outputBuffer, id)

        inputPacket.write(outputBuffer)
        outputData.add(inputPacket)
    }

    override fun decode(context: ChannelHandlerContext, inputBuffer: ByteBuf, outputData: MutableList<Any>) {
        if (inputBuffer.readableBytes() == 0)
            return

        val session: INetworkSession = this.injector.getInstance(MinecraftServer::class.java).networkSessionRegistry
            .findFirstOr({ it.address == context.channel().remoteAddress() })
            ?: throw IllegalStateException("No session created but client sends packet!")

        val id: Int = ByteBufUtils.readVarInt(inputBuffer)
        val packet: IPacket = this.injector.getInstance(session.protocol.packet(id))
        packet.read(inputBuffer)

        if (inputBuffer.readableBytes() > 0)
            throw DecoderException("Packet $id (${packet.javaClass.simpleName}) was larger than expected! Found ${inputBuffer.readableBytes()} bytes extra after reading packet!")

        outputData.add(packet)
    }

}