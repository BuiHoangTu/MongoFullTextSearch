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
     * @implNote To be the sameStackable,
     * they must have the same hashStackable() result.<br>
     * Meaning: this.sameStackable(that) != true
     * if this.hashStackable() != that.hashStackable()
     * @param countable new object
     * @return true if they can merge
     */
    boolean sameStackable(Object countable);

    /**
     * Help perform speed optimization in counter. <br>
     * @implNote If this.sameStackable(that) == true
     * then this.hashStackable() == that.hashStackable()
     * @return a hash code represent type of stackable
     */
    int hashStackable();
}
