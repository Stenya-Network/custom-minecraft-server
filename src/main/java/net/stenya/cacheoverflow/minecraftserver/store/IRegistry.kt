package net.stenya.cacheoverflow.minecraftserver.store

import net.stenya.cacheoverflow.minecraftserver.utils.ICopyable
import java.util.function.Function
import java.util.function.Predicate
import java.util.stream.Stream
import kotlin.reflect.KClass

/**
 * This interface is the template for a registry. A registry is an extension for the simple list with many features a.e.
 * stream generation, conditional list finding and more. This registry supports operator-overloading.
 *
 * @author Cach30verfl0w, Cedric H.
 * @since  1.0.0
 *
 * @see    Iterable
 * @see    MutableCollection
 */
@Suppress("UNUSED")
interface IRegistry<T: Any>: Iterable<T>, ICopyable<IRegistry<T>> {

    /**
     * This method registers the filtered elements from the elements array into the internal list of this registry.
     *
     * @param elements The list of elements for the registration.
     * @return         The count of elements that are successfully registered.
     *
     * @author         Cach30verfl0w, Cedric H.
     * @since          1.0.0
     *
     * @see MutableCollection.add
     */
    fun register(elements: Array<T>): Int {
        return this.directAction { list ->
            var counter = 0
            for (element in elements) {
                if (this.getRegisterPredicate() != null && !this.getRegisterPredicate()!!.test(element))
                    continue

                list.add(element)
                counter++
            }
            return@directAction counter
        }!!
    }

    fun unregisterIf(predicate: Predicate<T>) {
        this.directAction { list -> list.removeIf(predicate) }
    }

    /**
     * This method unregisters the filtered elements from the elements array into the internal list of this registry.
     *
     * @param elements The list of elements for the un-registration.
     * @return         The count of elements that are successfully unregistered.
     *
     * @author         Cach30verfl0w, Cedric H.
     * @since          1.0.0
     *
     * @see MutableCollection.remove
     */
    fun unregister(elements: Array<T>): Int {
        return this.directAction { list ->
            var counter = 0
            for (element in elements) {
                if (this.getUnregisterPredicate() != null && !this.getUnregisterPredicate()!!.test(element))
                    continue

                list.remove(element)
                counter++
            }
            return@directAction counter
        }!!
    }

    /**
     * This method opens a filtered stream of the internal list of this registry.
     *
     * @param predicate The predicate to filter all elements in this list.
     * @return          The stream of the internal list.
     *
     * @author          Cach30verfl0w, Cedric H.
     * @since           1.0.0
     *
     * @see Stream
     */
    fun openStream(predicate: Predicate<T>): Stream<T> {
        return this.openStream().filter(predicate)
    }

    /**
     * This method opens an unfiltered stream of the internal list of this registry.
     *
     * @return The stream of the internal list.
     *
     * @author Cach30verfl0w, Cedric H.
     * @since  1.0.0
     *
     * @see Stream
     */
    fun openStream(): Stream<T> {
        return this.asList().stream()
    }

    /**
     * This method returns the first by the predicate found element in the internal list of this registry or if no
     * element found, this method returns the ifNull variable.
     *
     * @param predicate The predicate to filter all elements in the list.
     * @param ifNull    The variable to return if no element got found in the filtered list.
     * @return          The first found element or the ifNull variable.
     *
     * @see Stream
     * @see Predicate
     */
    @Suppress("UNCHECKED_CAST")
    fun <E: T> findFirstOr(predicate: Predicate<T>, ifNull: E? = null): E? {
        return this.openStream(predicate).findFirst().orElse(ifNull) as E?
    }

    /**
     * This method returns the first by the class found element in the internal list of this registry or if no
     * element found, this method returns the ifNull variable.
     *
     * @param type   The type/class of the class that you want to find.
     * @param ifNull The variable to return if no element got found.
     * @return       The first found element or the ifNull variable.
     *
     * @see Stream
     * @see Predicate
     */
    fun <E: T> findFirstOr(type: KClass<E>, ifNull: E? = null): E? {
        return this.findFirstOr({ element -> type.isInstance(element) }, ifNull)
    }

    /**
     * This method executes the specified action (as function) and if the action have an output, this method returns
     * the in the action returned value or if no value is defined this method returns null. The parameter for the
     * action is the official list of this registry.
     *
     * @param action The specified action for the internal list.
     * @return       The action return value or null.
     *
     * @author       Cach30verfl0w, Cedric H.
     * @since        1.0.0
     *
     * @see          Function
     */
    fun <E> directAction(action: Function<MutableCollection<T>, E?>): E?

    /**
     * This method overloads the addition operator. This operator merges registry a and b in one registry and returns
     * it.
     *
     * @param addition This is the second registry to merge.
     * @return         The merged registry.
     *
     * @author         Cach30verfl0w, Cedric H.
     * @since          1.0.0
     */
    operator fun plus(addition: IRegistry<T>): IRegistry<T> {
        val registry: IRegistry<T> = this.copy()
        registry.directAction { list ->
            list.addAll(addition.asList())
            return@directAction true
        }
        return registry
    }

    /**
     * This method overloads the addition operator. This operator removes all elements of the registry b from the
     * registry a and returns it.
     *
     * @param addition This is the second registry to subtract.
     * @return         The subtracted registry.
     *
     * @author         Cach30verfl0w, Cedric H.
     * @since          1.0.0
     */
    operator fun minus(addition: IRegistry<T>): IRegistry<T> {
        val registry: IRegistry<T> = this.copy()
        registry.directAction { list ->
            list.removeAll(addition.asList().toSet())
            return@directAction true
        }
        return registry
    }

    /**
     * This method sets the predicate for every registration operation in this registry.
     *
     * @param predicate The predicate to set as filter.
     *
     * @author          Cach30verfl0w, Cedric H.
     * @since           1.0.0
     *
     * @see Predicate
     */
    fun setRegisterPredicate(predicate: Predicate<T>?)

    /**
     * This method sets the predicate for every un-registration operation in this registry.
     *
     * @param predicate The predicate to set as filter.
     *
     * @author          Cach30verfl0w, Cedric H.
     * @since           1.0.0
     *
     * @see Predicate
     */
    fun setUnregisterPredicate(predicate: Predicate<T>?)

    /**
     * This method returns the predicate for every registration operation in this registry.
     *
     * @return The filter as predicate.
     *
     * @author Cach30verfl0w, Cedric H.
     * @since  1.0.0
     */
    fun getRegisterPredicate(): Predicate<T>?

    /**
     * This method returns the predicate for every un-registration operation in this registry.
     *
     * @return The filter as predicate.
     *
     * @author Cach30verfl0w, Cedric H.
     * @since  1.0.0
     */
    fun getUnregisterPredicate(): Predicate<T>?

    /**
     * This method returns a copy of the internal list of this registry.
     *
     * @return The copy of the internal lists.
     *
     * @author Cach30verfl0w, Cedric H.
     * @since  1.0.0
     */
    fun asList(): MutableCollection<T>

}