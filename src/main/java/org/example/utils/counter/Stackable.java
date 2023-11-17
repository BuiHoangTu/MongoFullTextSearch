package org.example.utils.counter;

/**
 * Represent objects that can be merged together.
 */
public interface Stackable {
    /**
     * stack with new object
     * @param countable new object
     * Return itself if it is modifiable. Return null if they are not sameType:
     * this.sameType(stackable) == false
     */
    void stack(Object countable);

    /**
     * Check if this can be merged with new object
     * @implNote To be the same type, they must have the same hashCode()
     * meaning: this.sameType(stackable) != true if this.hashCode() != stackable.hashCode()
     * @param countable new object
     * @return true if they can merge
     */
    boolean sameType(Object countable);
}
