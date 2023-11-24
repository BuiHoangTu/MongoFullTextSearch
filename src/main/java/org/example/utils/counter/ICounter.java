package org.example.utils.counter;

public interface ICounter<T> {
    Number count(T object);

    /**
     * Repeatedly count one object n times. It is equal to loop
     * n times of count(object)
     * @param object object that need counting
     * @param time number of time that need repeating
     * @return last result of counting
     * @throws IllegalArgumentException if time <= 0
     * @implNote Implement this method if your counter perform more
     * optimization than a loop of n time
     */
    @SuppressWarnings("unused")
    default Number repeatCount(T object, int time) throws IllegalArgumentException {
        if (time <= 0) throw new IllegalArgumentException("The time of repeat can't not be smaller or equal to 0");
        Number res = null;
        for (int i = 0; i < time; i ++) {
            res = count(object);
        }
        return res;
    }

    Number getCount(Object key);
}
