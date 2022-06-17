package net.stenya.cacheoverflow.minecraftserver.network.protocol.packet

import io.netty.buffer.ByteBuf

interface IPacket {

    fun write(buffer: ByteBuf)

    fun read(buffer: ByteBuf)

}