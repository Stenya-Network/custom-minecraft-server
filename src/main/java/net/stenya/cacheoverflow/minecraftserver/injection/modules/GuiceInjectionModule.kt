package net.stenya.cacheoverflow.minecraftserver.injection.modules

import com.google.inject.AbstractModule
import net.stenya.cacheoverflow.minecraftserver.MinecraftServer
import net.stenya.cacheoverflow.minecraftserver.network.NetworkServer
import net.stenya.cacheoverflow.minecraftserver.network.protocol.packet.Protocol
import net.stenya.cacheoverflow.minecraftserver.network.protocol.packet.all.status.ClientboundStatusResponsePacket
import net.stenya.cacheoverflow.minecraftserver.network.protocol.packet.all.status.ServerboundStatusRequestPacket
import net.stenya.cacheoverflow.minecraftserver.player.defaults.session.DefaultNetworkSessionFactory
import net.stenya.cacheoverflow.minecraftserver.player.session.INetworkSessionFactory

class GuiceInjectionModule(private val server: MinecraftServer) : AbstractModule() {

    override fun configure() {
        this.bind(INetworkSessionFactory::class.java).to(DefaultNetworkSessionFactory::class.java)

        this.bind(MinecraftServer::class.java).toInstance(this.server)
        this.bind(NetworkServer::class.java).toInstance(NetworkServer(this.server.hostname, this.server.port))

        val protocol: Protocol = Protocol.ProtocolBuilder()
            .state(-0x01).finish()
            .state(0x00).finish()
            .state(0x01)
                .outbound(0x00, ClientboundStatusResponsePacket::class)
                .outbound(0x01, ServerboundStatusRequestPacket::class)
                .finish()
            .state(0x02).finish()
            .build() // TODO: Move Protocol Builder to other class
        this.bind(Protocol::class.java).toInstance(protocol)
    }

}