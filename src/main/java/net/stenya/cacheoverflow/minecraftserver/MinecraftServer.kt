package net.stenya.cacheoverflow.minecraftserver

import com.google.inject.Guice
import com.google.inject.Injector
import net.stenya.cacheoverflow.minecraftserver.injection.modules.GuiceInjectionModule
import net.stenya.cacheoverflow.minecraftserver.network.NetworkServer
import kotlin.reflect.KClass

class MinecraftServer(val name: String, val hostname: String, val port: Int) {

    private val injector: Injector = Guice.createInjector(GuiceInjectionModule(this))

    fun start(worldName: String) {
        this.get(NetworkServer::class).start()
    }

    fun <T: Any> get(type: KClass<T>): T {
        return injector.getInstance(type.java)
    }

}