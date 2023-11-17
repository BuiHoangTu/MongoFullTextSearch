package org.example.utils.counter;

public interface ICounter<T> {
    Number count(T object);
    Number get(Object key);
}
