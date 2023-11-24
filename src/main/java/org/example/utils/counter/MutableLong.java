package org.example.utils.counter;

public class MutableLong extends Number implements Comparable<Number> {
    private Long value;

    @SuppressWarnings("unused")
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

    @SuppressWarnings("unused")
    public MutableLong setValue(long value) {
        this.value = value;
        return this;
    }

    @Override
    public int compareTo(Number o) {
        return value.compareTo(o.longValue());
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
        return String.valueOf(value);
    }
}
