package org.example.utils.counter;

import lombok.NonNull;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class UniqueCounter<T> implements ICounter<T>, Map<T, Number>{
    private final Map<T, MutableLong> counterMap;

    @SuppressWarnings("unused")
    public UniqueCounter() {
        this.counterMap = new HashMap<>();
    }
    @SuppressWarnings("unused")
    public UniqueCounter(Map<T, Number> startupMap) {
        this.counterMap = new HashMap<>(startupMap.entrySet().stream().collect(Collectors.toMap(Entry::getKey, entry -> new MutableLong(entry.getValue()))));
    }
    @SuppressWarnings("unused")
    public UniqueCounter(Collection<T> startupElements) {
        this.counterMap = new HashMap<>();
        startupElements.forEach(this::count);
    }

    /**
     * Put the object in the Counter. If the counter previously contained
     * this object, the old value is increased by 1.
     * @param object object which need counting its appearance
     * @return total times this object appeared including this time
     */
    @Override
    public Number count(T object) {
        return this.put(object, 1);
    }

    @Override
    public Number repeatCount(T object, int time) throws IllegalArgumentException {
        return this.put(object, time);
    }

    @Override
    public Number getCount(Object key) {
        return counterMap.get(key);
    }

    @Override
    public Number get(Object key) {
        return this.getCount(key);
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
    @NonNull
    public Set<T> keySet() {
        return counterMap.keySet();
    }

    @Override
    @NonNull
    public Collection< Number> values() {
        return new ArrayList<>(counterMap.values());
    }

    @Override
    @NonNull
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
            @SuppressWarnings("all")
            public boolean equals(Object o) {
                return mutableEntry.equals(o);
            }

            @Override
            public int hashCode() {
                return mutableEntry.hashCode();
            }
        }).collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return counterMap.toString();
    }
}
