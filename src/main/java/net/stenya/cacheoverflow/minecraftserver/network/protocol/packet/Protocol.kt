package net.stenya.cacheoverflow.minecraftserver.network.protocol.packet

import net.stenya.cacheoverflow.minecraftserver.network.transport.EnumDirection
import kotlin.reflect.KClass

class Protocol(private val states: MutableList<ProtocolBuilder.State>): Cloneable {

    private val packets: MutableList<ProtocolBuilder.PacketContainer> = ArrayList()

    private var currentState: ProtocolBuilder.State? = null

    public override fun clone(): Protocol {
        return Protocol(states)
    }

    fun state(id: Int) {
        if (this.currentState != null && this.currentState!!.id > id)
            throw IllegalStateException("You can't change the state to a state with a lower id!")
        this.currentState = states.first { state -> state.id == id }

        this.packets.clear()
        this.packets.addAll(this.currentState!!.packets)
    }

    fun packet(id: Int): Class<out IPacket> {
        return this.packets.first { it.id == id }.packetClass
    }

    fun packet(type: Class<out IPacket>): Int {
        return this.packets.first { it.packetClass == type }.id
    }

    class ProtocolBuilder {

        private val states: MutableList<State> = ArrayList()

        fun state(id: Int): State.StateBuilder {
            return State.StateBuilder(id, this)
        }

        fun build(): Protocol {
            return Protocol(states)
        }

        class State(val id: Int, val packets: MutableList<PacketContainer>) {

            class StateBuilder(private val id: Int, private val protocolBuilder: ProtocolBuilder) {

                private val packets: MutableList<PacketContainer> = ArrayList()

                fun <P: IPacket> inbound(id: Int, packetType: KClass<P>): StateBuilder {
                    return this.inbound(id, packetType.java)
                }

                fun <P: IPacket> outbound(id: Int, packetType: KClass<P>): StateBuilder {
                    return this.outbound(id, packetType.java)
                }

                fun <P: IPacket> inbound(id: Int, packetType: Class<P>): StateBuilder {
                    this.register(id, EnumDirection.SERVERBOUND, packetType)
                    return this
                }

                fun <P: IPacket> outbound(id: Int, packetType: Class<P>): StateBuilder {
                    this.register(id, EnumDirection.CLIENTBOUND, packetType)
                    return this
                }

                fun <P: IPacket> register(id: Int, direction: EnumDirection, packetClass: Class<P>) {
                    this.packets.add(PacketContainer(id, direction, packetClass))
                }

                fun finish(): ProtocolBuilder {
                    this.protocolBuilder.states.add(State(id, packets))
                    return this.protocolBuilder
                }

            }

        }

        class PacketContainer(val id: Int, val direction: EnumDirection, val packetClass: Class<out IPacket>)

    }

}