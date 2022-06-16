package net.stenya.cacheoverflow.minecraftserver.network.transport

enum class EnumLoopGroupType(private val literal: String) {

    BOSS("Boss"), WORKER("Worker");

    fun asLiteral(): String {
        return literal
    }

}