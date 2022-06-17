package net.stenya.cacheoverflow.minecraftserver.bootstrap

import net.stenya.cacheoverflow.minecraftserver.MinecraftServer

object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        val startTime: Long = System.currentTimeMillis()
        MinecraftServer("Test", "127.0.0.1", 25565).start("Test")
        print(System.currentTimeMillis() - startTime)
        println("ms")
    }

}