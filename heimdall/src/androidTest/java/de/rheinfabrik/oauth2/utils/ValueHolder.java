package de.rheinfabrik.oauth2.utils;

public class ValueHolder<T> {
    public T value = null;

    public ValueHolder() {
        this(null);
    }

    public ValueHolder(T initialValue) {
        super();

        value = initialValue;
    }
}
