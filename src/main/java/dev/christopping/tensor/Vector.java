package dev.christopping.tensor;

import java.util.List;
import java.util.Map;

public class Vector<T> extends Tensor<T> {

    protected Vector() {
        super();
    }

    protected Vector(Map<Index, T> map) {
        super(map);
    }

    public static <T> Vector<T> of(List<T> list) {
        Vector<T> vector = new Vector<>();
        for (int i = 0; i < list.size(); i++) {
            vector.set(list.get(i), i);
        }
        return vector;
    }

    public static <T> Vector<T> of(T[] array) {
        Vector<T> vector = new Vector<>();
        for (int i = 0; i < array.length; i++) {
            vector.set(array[i], i);
        }
        return vector;
    }

    public static <T> Vector<T> fill(T value, long size) {
        if (size < 0) throw new IndexOutOfBoundsException("Size must be positive");
        return Tensor.fill(value, size).toVector();
    }

    public static <T> Vector<T> fill(T value, int size) {
        return fill(value, (long) size);
    }

    public static <T> Vector<T> empty() {
        return new Vector<>();
    }

    @Override
    public void set(T element, long... coordinates) {
        if (coordinates.length != 1) throw new IllegalArgumentException("Element requires 1 coordinate");
        super.set(element, coordinates);
    }

    @Override
    public void set(T element, int... coordinates) {
        if (coordinates.length != 1) throw new IllegalArgumentException("Element requires 1 coordinate");
        super.set(element, coordinates);
    }
}
