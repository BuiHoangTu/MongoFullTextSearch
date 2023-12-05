package org.example.utils.counter;

import com.mongodb.lang.NonNull;

import java.util.*;

/**
 * Count a stack of objects instead of singular objects
 * @param <T> type of object that is countable
 */
@SuppressWarnings("unused")
public class StackCounter<T extends Countable> implements ICounter<T>, Set<T> {
    private final Map<CountableWrapper<T>, T> counterMap = new HashMap<>();

    @SuppressWarnings("unused")
    public StackCounter() {
    }
    @SuppressWarnings("unused")
    public StackCounter(Collection<T> startupCollection) {
        startupCollection.forEach(this::count);
    }

    @Override
    public Number count(T object) {
        T clone = (T) object.clone();
        var wrapped = new CountableWrapper<>(clone);
        var currentValue = counterMap.get(wrapped);
        if (currentValue == null) {
            counterMap.put(wrapped, clone);
            return object.getCount();
        } else {
            currentValue.stack(object);
            return currentValue.getCount();
        }
    }

    @Override
    public Number repeatCount(T object, int time) throws IllegalArgumentException {
        var currentValue = counterMap.get(new CountableWrapper<>(object));
        if (currentValue == null) {
            currentValue = (T) object.clone();
            this.counterMap.put(new CountableWrapper<>(currentValue), currentValue);
        }
        else currentValue.stack(object);

        // previous has consumed 1 count
        for (int i = 0; i < time - 1; i ++) {
            currentValue.stack(object);
        }

        return currentValue.getCount();
    }

    @Override
    public Number getCount(Object key) {
        try {
            var cKey = (T) key;
            return counterMap.get(new CountableWrapper<>(cKey)).getCount();
        } catch (ClassCastException e) {
            return null;
        }
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
        try {
            return counterMap.containsKey(new CountableWrapper<>((T) o));
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    @NonNull
    public Iterator<T> iterator() {
        return counterMap.values().iterator();
    }

    @Override
    @NonNull
    public Object[] toArray() {
        return counterMap.values().toArray();
    }

    @Override
    @NonNull
    public <T1> T1[] toArray(@NonNull T1[] a) {
        return counterMap.values().toArray(a);
    }

    @Override
    public boolean add(T t) {
        this.count(t);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        try {
            var c = counterMap.remove(new CountableWrapper<>(((T) o)));
            return c != null; // if there is this object in the counter}
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public boolean containsAll(@NonNull Collection<?> c) {
        try {
            var keys = c.stream().map(elem -> (T) elem).map(CountableWrapper::new).toList();
            return counterMap.keySet().containsAll(keys);
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        c.forEach(this::count);
        return true;
    }

    @Override
    public boolean retainAll(@NonNull Collection<?> c) {
        var res = c.stream()
                .map(elem -> {
                    try {
                        return (T) elem;
                    } catch (ClassCastException e) {
                        return null;
                    }
                })
                .map(CountableWrapper::new)
                .toList();

        return this.counterMap.keySet().retainAll(res);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return c.stream().anyMatch(this::remove);
    }

    @Override
    public void clear() {
        counterMap.clear();
    }


    private record CountableWrapper<C extends Countable>(C content) {
        @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (!(o instanceof StackCounter.CountableWrapper<?> that)) return false;
                return this.content.sameStackable(that.content);
            }

            @Override
            public int hashCode() {
                return this.content.hashStackable();
            }

            public Number getCount() {
                return content.getCount();
            }

            public void stack(Object countable) {
                content.stack(countable);
            }
    }
}
