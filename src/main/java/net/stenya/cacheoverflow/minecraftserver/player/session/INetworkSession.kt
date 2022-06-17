package net.stenya.cacheoverflow.minecraftserver.player.session

import io.netty.channel.Channel
import io.netty.channel.ChannelHandler
import net.stenya.cacheoverflow.minecraftserver.network.protocol.packet.Protocol
import java.net.InetSocketAddress

interface INetworkSession {

    fun updatePipeline(key: String, handler: ChannelHandler)

    val channel: Channel
    val address: InetSocketAddress
    var compressionEnabled: Boolean
    val protocol: Protocol

}