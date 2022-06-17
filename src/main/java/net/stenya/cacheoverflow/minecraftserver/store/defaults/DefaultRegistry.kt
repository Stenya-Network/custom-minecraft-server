package net.stenya.cacheoverflow.minecraftserver.store.defaults

import net.stenya.cacheoverflow.minecraftserver.store.IRegistry
import java.util.function.Function
import java.util.function.Predicate

class DefaultRegistry<T: Any>(
    private val elements: MutableCollection<T> = ArrayList(),
    private var registerPredicate: Predicate<T>? = Predicate { true },
    private var unregisterPredicate: Predicate<T>? = Predicate { true }
): IRegistry<T> {

    override fun <E> directAction(action: Function<MutableCollection<T>, E?>): E? {
        return action.apply(this.elements)
    }

    override fun setRegisterPredicate(predicate: Predicate<T>?) {
        this.registerPredicate = predicate
    }

    override fun setUnregisterPredicate(predicate: Predicate<T>?) {
        this.unregisterPredicate = predicate
    }

    override fun getRegisterPredicate(): Predicate<T>? {
        return this.registerPredicate
    }

    override fun getUnregisterPredicate(): Predicate<T>? {
        return this.unregisterPredicate
    }

    override fun asList(): MutableCollection<T> {
        return ArrayList(this.elements)
    }

    override fun iterator(): Iterator<T> {
        return this.elements.iterator()
    }

    override fun copy(): IRegistry<T> {
        return DefaultRegistry(ArrayList(this.elements), registerPredicate, unregisterPredicate)
    }

}
