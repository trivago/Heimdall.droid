package de.rheinfabrik.heimdall.utils;

public class ValueHolder<T> {

    // Members

    public T value = null;

    // Constructor

    public ValueHolder() {
        this(null);
    }

    public ValueHolder(T initialValue) {
        super();

        value = initialValue;
    }
}
