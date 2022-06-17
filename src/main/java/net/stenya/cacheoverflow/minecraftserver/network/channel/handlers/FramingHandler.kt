package net.stenya.cacheoverflow.minecraftserver.network.channel.handlers

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageCodec
import net.stenya.cacheoverflow.minecraftserver.utils.ByteBufUtils

class FramingHandler: ByteToMessageCodec<ByteBuf>() {

    private fun readableVarInt(buf: ByteBuf): Boolean {
        if (buf.readableBytes() > 5) {
            return true
        }
        val idx = buf.readerIndex()
        var `in`: Byte
        do {
            if (buf.readableBytes() < 1) {
                buf.readerIndex(idx)
                return false
            }
            `in` = buf.readByte()
        } while (`in`.toInt() and 0x80 != 0)
        buf.readerIndex(idx)
        return true
    }

    override fun encode(context: ChannelHandlerContext, inputBuffer: ByteBuf, outputBuffer: ByteBuf) {
        ByteBufUtils.writeVarInt(outputBuffer, inputBuffer.readableBytes())
        outputBuffer.writeBytes(inputBuffer)
    }

    override fun decode(context: ChannelHandlerContext, inputBuffer: ByteBuf, outputData: MutableList<Any>) {
        inputBuffer.markReaderIndex()
        if (!readableVarInt(inputBuffer))
            return

        val length: Int = ByteBufUtils.readVarInt(inputBuffer)
        if (inputBuffer.readableBytes() < length) {
            inputBuffer.resetReaderIndex()
            return
        }

        val buffer: ByteBuf = context.alloc().buffer(length)
        inputBuffer.readBytes(buffer, length)
        outputData.add(buffer)
    }

}