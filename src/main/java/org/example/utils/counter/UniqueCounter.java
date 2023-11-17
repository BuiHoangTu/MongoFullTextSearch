package org.example.utils.counter;

import java.util.*;
import java.util.stream.Collectors;

public class UniqueCounter<T> implements ICounter<T>, Map<T, Number>{
    private final Map<T, MutableLong> counterMap = new HashMap<>();

    /**
     * Put the object in the Counter. If the counter previously contained
     * this object, the old value is increased by 1.
     * @param object object which need counting its appearance
     * @return total times this object appeared including this time
     */
    public Number count(T object) {
        return this.put(object, 1);
    }

    @Override
    public Number get(Object key) {
        return counterMap.get(key);
    }

    @Override
    public int size() {
        return counterMap.size();
    }

    @Override
    public boolean isEmpty() {
        return counterMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return counterMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return counterMap.containsValue(value);
    }

    /**
     * Associates the specified value with the specified key in this map
     * (optional operation). If the counter previously contained a mapping
     * for the key, the old value is increased by the specified value.
     * (A map m is said to contain a mapping for a key k if and only if
     * m.containsKey(k) would return true.)
     * @param key key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return new value in the Counter
     */
    @Override
    public Number put(T key, Number value) {
        var currentValue = counterMap.get(key);
        if (currentValue == null) {
            counterMap.put(key, new MutableLong(value.longValue()));
            return value.longValue();
        } else {
            return currentValue.setValue(currentValue.longValue() + value.longValue());
        }
    }

    @Override
    public Number remove(Object key) {
        return counterMap.remove(key);
    }

    @Override
    public void putAll(Map<? extends T, ? extends Number> m) {
        for (var entry : m.entrySet()) {
            this.put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        counterMap.clear();
    }

    @Override
    public Set<T> keySet() {
        return counterMap.keySet();
    }

    @Override
    public Collection< Number> values() {
        return new ArrayList<>(counterMap.values());
    }

    @Override
    public Set<Entry<T, Number>> entrySet() {
        return counterMap.entrySet().stream().map((mutableEntry) -> new Entry<T, Number>() {
            @Override
            public T getKey() {
                return mutableEntry.getKey();
            }

            @Override
            public Number getValue() {
                return mutableEntry.getValue();
            }

            @Override
            public Number setValue(Number value) {
                return mutableEntry.getValue().setValue(value.longValue());
            }

            @Override
            public boolean equals(Object o) {
                return mutableEntry.equals(o);
            }

            @Override
            public int hashCode() {
                return mutableEntry.hashCode();
            }
        }).collect(Collectors.toSet());
    }
}
