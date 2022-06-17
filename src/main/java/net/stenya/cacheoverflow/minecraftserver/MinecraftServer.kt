package net.stenya.cacheoverflow.minecraftserver

import com.google.inject.Guice
import com.google.inject.Injector
import net.stenya.cacheoverflow.minecraftserver.injection.modules.GuiceInjectionModule
import net.stenya.cacheoverflow.minecraftserver.network.NetworkServer
import net.stenya.cacheoverflow.minecraftserver.player.session.INetworkSession
import net.stenya.cacheoverflow.minecraftserver.store.IRegistry
import net.stenya.cacheoverflow.minecraftserver.store.defaults.DefaultRegistry
import kotlin.reflect.KClass

class MinecraftServer(val name: String, val hostname: String, val port: Int) {

    private val injector: Injector = Guice.createInjector(GuiceInjectionModule(this))

    val networkSessionRegistry: IRegistry<INetworkSession> = DefaultRegistry()

    fun start(worldName: String) {
        this.get(NetworkServer::class).start(this.injector)
    }

    fun <T: Any> get(type: KClass<T>): T {
        return injector.getInstance(type.java)
    }

}