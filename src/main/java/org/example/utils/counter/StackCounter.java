package org.example.utils.counter;

import java.util.*;

/**
 * Count a stack of objects instead of singular objects
 * @param <T> type of object that is countable
 */
public class StackCounter<T extends Countable> implements ICounter<T>, Set<T> {
    private final Map<T, Countable> counterMap = new HashMap<>();

    @Override
    public Number count(T object) {
        var currentValue = counterMap.get(object);
        if (currentValue == null) {
            counterMap.put(object, object);
            return object.getCount();
        } else {
            currentValue.stack(object);
            return currentValue.getCount();
        }
    }

    @Override
    public Number get(Object key) {
        return counterMap.get(key).getCount();
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
    public boolean contains(Object o) {
        return counterMap.containsKey(o);
    }

    @Override
    public Iterator<T> iterator() {
        return counterMap.keySet().iterator();
    }

    @Override
    public Object[] toArray() {
        return counterMap.keySet().toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return counterMap.keySet().toArray(a);
    }

    @Override
    public boolean add(T t) {
        this.count(t);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        var c = counterMap.remove(o);
        return c != null; // if there is this object in the counter
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return counterMap.keySet().containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        c.stream().map(this::count);
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return c.stream().anyMatch(this::remove);
    }

    @Override
    public void clear() {
        counterMap.clear();
    }
}
