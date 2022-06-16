package net.stenya.cacheoverflow.minecraftserver.network.protocol.packet

import kotlin.reflect.KClass

class Protocol(private val states: MutableList<ProtocolBuilder.State>) {

    private val outboundMap: MutableMap<Int, Class<*>> = HashMap()
    private val inboundMap: MutableMap<Int, Class<*>> = HashMap()

    private lateinit var currentState: ProtocolBuilder.State

    fun state(id: Int) {
        if (this.currentState.id >= id)
            throw IllegalStateException("You can't change the state to a state with a lower id!")
        this.currentState = states.first { state -> state.id == id }

        this.outboundMap.clear()
        this.outboundMap.putAll(this.currentState.outboundMap)

        this.inboundMap.clear()
        this.inboundMap.putAll(this.currentState.inboundMap)
    }

    class ProtocolBuilder {

        private val states: MutableList<State> = ArrayList()

        fun state(id: Int): State.StateBuilder {
            return State.StateBuilder(id, this)
        }

        fun build() {

        }

        class State(val id: Int, val outboundMap: Map<Int, Class<*>>, val inboundMap: Map<Int, Class<*>>) {

            class StateBuilder(private val id: Int, private val protocolBuilder: ProtocolBuilder) {

                private val outboundMap: MutableMap<Int, Class<*>> = HashMap()
                private val inboundMap: MutableMap<Int, Class<*>> = HashMap()

                fun <T: IPacket> inbound(id: Int, packetType: KClass<T>): StateBuilder {
                    return this.inbound(id, packetType.java)
                }

                fun <T: IPacket> outbound(id: Int, packetType: KClass<T>): StateBuilder {
                    return this.outbound(id, packetType.java)
                }

                fun <T: IPacket> inbound(id: Int, packetType: Class<T>): StateBuilder {
                    this.inboundMap[id] = packetType
                    return this
                }

                fun <T: IPacket> outbound(id: Int, packetType: Class<T>): StateBuilder {
                    this.outboundMap[id] = packetType
                    return this
                }

                fun finish(): ProtocolBuilder {
                    this.protocolBuilder.states.add(State(id, outboundMap, inboundMap))
                    return this.protocolBuilder
                }

            }

        }

    }

}