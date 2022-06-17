package net.stenya.cacheoverflow.minecraftserver.player.session

import io.netty.channel.Channel
import java.net.InetSocketAddress

interface INetworkSessionFactory {

    fun createSession(channel: Channel, address: InetSocketAddress): INetworkSession

}