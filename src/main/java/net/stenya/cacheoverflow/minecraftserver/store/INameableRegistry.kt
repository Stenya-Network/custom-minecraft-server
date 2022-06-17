package net.stenya.cacheoverflow.minecraftserver.store

import net.stenya.cacheoverflow.minecraftserver.utils.IStringIdentifiable
import java.util.function.Predicate
import java.util.stream.Stream

/**
 * This interface is the template for a nameable registry. A registry is an extension for the simple list with many
 * features a.e. stream generation, conditional list finding and more. But this template is specially for nameable
 * objects with the IStringIdentifiable interface.
 *
 * @author Cach30verfl0w, Cedric H.
 * @since  1.0.0
 *
 * @see    Iterable
 * @see    MutableCollection
 * @see    IStringIdentifiable
 */
@Suppress("UNUSED")
interface INameableRegistry<T: IStringIdentifiable> : IRegistry<T> {

    /**
     * This method returns the first by the name found element in the internal list of this registry or if no element
     * found, this method returns the ifNull variable.
     *
     * @param name       The name of the object what you want do find.
     * @param ignoreCase Weather the string should check case-sensitive or not case-sensitive
     * @param ifNull     The variable to return if no element got found in the filtered list.
     * @return           The first found element or the ifNull variable.
     *
     * @see Stream
     * @see Predicate
     */
    fun <E: T> findFirstOr(name: String, ignoreCase: Boolean, ifNull: E? = null): E? {
        return this.findFirstOr({ element -> element.asString().equals(name, ignoreCase) }, ifNull)
    }

}