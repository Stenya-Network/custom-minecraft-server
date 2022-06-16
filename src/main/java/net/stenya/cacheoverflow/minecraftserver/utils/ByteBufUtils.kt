package net.stenya.cacheoverflow.minecraftserver.utils

import io.netty.buffer.ByteBuf
import java.io.IOException

object ByteBufUtils {

    @JvmStatic fun writeVarInt(buf: ByteBuf, value: Int) {
        var tmp = value
        var part: Byte
        do {
            part = (tmp and 0x7F).toByte()
            tmp = tmp ushr 7
            if (tmp != 0) {
                part = (part.toInt() or 0x80).toByte()
            }
            buf.writeByte(part.toInt())
        } while (tmp != 0)
    }

    @Throws(IOException::class)
    @JvmStatic fun readVarInt(buf: ByteBuf): Int {
        var out = 0
        var bytes = 0
        var `in`: Byte
        do {
            `in` = buf.readByte()
            out = out or (`in`.toInt() and 0x7F shl bytes++) * 7
            if (bytes > 5) {
                throw IOException("Attempt to read int bigger than allowed for a varint!")
            }
        } while (`in`.toInt() and 0x80 == 0x80)
        return out
    }

}