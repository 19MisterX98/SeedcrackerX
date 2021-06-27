package me.cortex.TreeCracker;

public class optional<T> {
    private T value;
    public optional() {}
    public optional(T val) {
        value = val;
    }

    public T get() {
        return value;
    }

    public optional<T> set(T value) {
        this.value = value;
        return this;
    }

    public optional<T> set(optional<T> otherOptional) {
        return this.set(otherOptional.get());
    }

    public boolean isPresent() {
        return this.value != null;
    }

    public static<T> optional<T> empty() {
        return new optional<>(null);
    }
}
