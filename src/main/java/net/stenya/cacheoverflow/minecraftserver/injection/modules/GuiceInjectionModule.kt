package net.stenya.cacheoverflow.minecraftserver.injection.modules

import com.google.inject.AbstractModule
import net.stenya.cacheoverflow.minecraftserver.MinecraftServer
import net.stenya.cacheoverflow.minecraftserver.network.NetworkServer

class GuiceInjectionModule(private val server: MinecraftServer) : AbstractModule() {

    override fun configure() {
        this.bind(NetworkServer::class.java).toInstance(NetworkServer(this.server.hostname, this.server.port))
    }

}