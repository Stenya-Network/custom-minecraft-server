package net.stenya.cacheoverflow.minecraftserver.network.protocol.packet.all.status

import io.netty.buffer.ByteBuf
import net.stenya.cacheoverflow.minecraftserver.network.protocol.packet.IPacket

class ClientboundStatusResponsePacket : IPacket {

    override fun write(buffer: ByteBuf) {}
    override fun read(buffer: ByteBuf) {}

}