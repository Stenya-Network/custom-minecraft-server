package net.stenya.cacheoverflow.minecraftserver.utils

/**
 * This interface implements the functionality to clone the own class.
 *
 * @author Cach30verfl0w, Cedric H.
 * @since  1.0.0
 */
interface ICopyable<T> {

    /**
     * This method does a clean copy the own class and return the copy.
     *
     * @return The copy of this class.
     *
     * @author Cach30verfl0w, Cedric H.
     * @since  1.0.0
     */
    fun copy(): T

}