package net.stenya.cacheoverflow.minecraftserver.network.transport

import io.netty.channel.ChannelFactory
import io.netty.channel.EventLoopGroup
import io.netty.channel.epoll.*
import io.netty.channel.kqueue.*
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.ServerSocketChannel
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.channel.unix.DomainSocketChannel
import io.netty.channel.unix.ServerDomainSocketChannel
import io.netty.util.concurrent.FastThreadLocalThread
import java.util.*
import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.BiFunction

fun newThreadFactory(name: String, type: EnumLoopGroupType): ThreadFactory {
    return EnumTransportType.FastNettyThreadFactory("Netty " + type.asLiteral() + ' ' + " Thread#%d")
}

enum class EnumTransportType(
    val literal: String, val isAvailable: Boolean,
    private val eventLoopGroupFactory: BiFunction<EnumLoopGroupType, String, EventLoopGroup>,
    val serverChannelFactory: ChannelFactory<out ServerSocketChannel>,
    val channelFactory: ChannelFactory<out SocketChannel>,
    val serverDomainChannelFactory: ChannelFactory<out ServerDomainSocketChannel>?,
    val domainChannelFactory: ChannelFactory<out DomainSocketChannel>?
) {

    EPOLL(
        "Epoll",
        Epoll.isAvailable(),
        BiFunction<EnumLoopGroupType, String, EventLoopGroup> { type: EnumLoopGroupType, name: String ->
            EpollEventLoopGroup(4.coerceAtMost(Runtime.getRuntime().availableProcessors() * 2), newThreadFactory(name, type))
        },
        ChannelFactory<ServerSocketChannel> { EpollServerSocketChannel() },
        ChannelFactory<SocketChannel> { EpollSocketChannel() },
        ChannelFactory<ServerDomainSocketChannel> { EpollServerDomainSocketChannel() },
        ChannelFactory<DomainSocketChannel> { EpollDomainSocketChannel() }
    ),
    KQUEUE(
        "KQueue",
        KQueue.isAvailable(),
        BiFunction<EnumLoopGroupType, String, EventLoopGroup> { type: EnumLoopGroupType, name: String ->
            KQueueEventLoopGroup(4.coerceAtMost(Runtime.getRuntime().availableProcessors() * 2), newThreadFactory(name, type))
        },
        ChannelFactory<ServerSocketChannel> { KQueueServerSocketChannel() },
        ChannelFactory<SocketChannel> { KQueueSocketChannel() },
        ChannelFactory<ServerDomainSocketChannel> { KQueueServerDomainSocketChannel() },
        ChannelFactory<DomainSocketChannel> { KQueueDomainSocketChannel() }
    ),
    NIO(
        "NIO",
        true,
        BiFunction<EnumLoopGroupType, String, EventLoopGroup> { type: EnumLoopGroupType, name: String ->
            NioEventLoopGroup(4.coerceAtMost(Runtime.getRuntime().availableProcessors() * 2), newThreadFactory(name, type))
        },
        ChannelFactory<ServerSocketChannel> { NioServerSocketChannel() },
        ChannelFactory<SocketChannel> { NioSocketChannel() },
        null,
        null
    );

    fun create(type: EnumLoopGroupType): EventLoopGroup {
        return eventLoopGroupFactory.apply(type, name)
    }

    class FastNettyThreadFactory(private val format: String) : ThreadFactory {
        override fun newThread(runnable: Runnable): Thread {
            return FastThreadLocalThread(runnable, String.format(format, THREAD_COUNTER.getAndIncrement()))
        }

        companion object {
            private val THREAD_COUNTER = AtomicInteger()
        }
    }

    companion object {
        @JvmStatic fun findTransportType(): EnumTransportType {
            return if (java.lang.Boolean.getBoolean("network.native.disable")) NIO else Arrays.stream(values())
                .filter { obj: EnumTransportType -> obj.isAvailable }
                .findFirst().orElse(NIO)
        }
    }

}