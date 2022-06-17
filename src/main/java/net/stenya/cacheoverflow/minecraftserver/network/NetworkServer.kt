package net.stenya.cacheoverflow.minecraftserver.network

import com.google.inject.Injector
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import net.stenya.cacheoverflow.minecraftserver.network.channel.ServerChannelInitializer
import net.stenya.cacheoverflow.minecraftserver.network.transport.EnumLoopGroupType
import net.stenya.cacheoverflow.minecraftserver.network.transport.EnumTransportType

class NetworkServer(private val hostname: String, private val port: Int) {

    private val transportType: EnumTransportType = EnumTransportType.findTransportType()

    private var bossGroup: EventLoopGroup? = null
    private var workerGroup: EventLoopGroup? = null
    private var serverFuture: ChannelFuture? = null

    fun start(injector: Injector) {
        this.bossGroup = this.transportType.create(EnumLoopGroupType.BOSS)
        this.workerGroup = this.transportType.create(EnumLoopGroupType.WORKER)

        val bootstrap: ServerBootstrap = ServerBootstrap()
        bootstrap.group(this.bossGroup, this.workerGroup)

        bootstrap.childOption(ChannelOption.TCP_NODELAY, true)

        bootstrap.childHandler(ServerChannelInitializer(injector))
        bootstrap.channelFactory(this.transportType.serverChannelFactory)

        this.serverFuture = bootstrap.bind(this.hostname, this.port).addListener { future ->
            run {
                if (!future.isSuccess)
                    future.cause().printStackTrace()
            }
        }
    }

    fun stop() {
        if (this.serverFuture != null)
            this.serverFuture!!.cancel(true)

        if (this.bossGroup != null)
            this.bossGroup!!.shutdownGracefully()

        if (this.workerGroup != null)
            this.workerGroup!!.shutdownGracefully()
    }

}