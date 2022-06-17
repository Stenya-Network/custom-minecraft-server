package net.stenya.cacheoverflow.minecraftserver.player.defaults.session

import com.google.inject.Inject
import com.google.inject.Injector
import io.netty.channel.Channel
import io.netty.channel.ChannelHandler
import net.stenya.cacheoverflow.minecraftserver.network.protocol.packet.Protocol
import net.stenya.cacheoverflow.minecraftserver.player.session.INetworkSession
import net.stenya.cacheoverflow.minecraftserver.player.session.INetworkSessionFactory
import java.net.InetSocketAddress

class DefaultNetworkSessionFactory @Inject constructor(private val injector: Injector) : INetworkSessionFactory {

    override fun createSession(channel: Channel, address: InetSocketAddress): INetworkSession {

        return object : INetworkSession {
            override fun updatePipeline(key: String, handler: ChannelHandler) {
                this.channel.pipeline().replace(key, key, handler)
            }

            override val channel: Channel
                get() = channel
            override val address: InetSocketAddress
                get() = address
            override val protocol: Protocol
                get() = injector.getInstance(Protocol::class.java).clone()
            override var compressionEnabled = false
        }

    }

}