package org.example.utils.counter;

public class MutableLong extends Number implements Comparable<Long> {
    private Long value;

    public MutableLong(Number value) {
        this.value = value.longValue();
    }

    @Override
    public int intValue() {
        return value.intValue();
    }

    @Override
    public long longValue() {
        return value;
    }

    @Override
    public float floatValue() {
        return value.floatValue();
    }

    @Override
    public double doubleValue() {
        return value.doubleValue();
    }

    public MutableLong setValue(long value) {
        this.value = value;
        return this;
    }

    @Override
    public int compareTo(Long o) {
        return value.compareTo(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        super.clone();
        return new MutableLong(value);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
