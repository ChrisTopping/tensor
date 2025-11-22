package dev.christopping.tensor;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Vector<T> extends Tensor<T> {

    protected Vector(Map<Index, T> map) {
        super(map);
    }

    public static <T> Vector<T> of(List<T> list) {
        Map<Index, T> map = IntStream.range(0, list.size())
                .mapToObj(i -> Map.entry(Index.of(i), list.get(i)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return new Vector<>(map);
    }

    public static <T> Vector<T> of(T... array) {
        Map<Index, T> map = IntStream.range(0, array.length)
                .mapToObj(i -> Map.entry(Index.of(i), array[i]))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return new Vector<>(map);
    }

    public static <T> Vector<T> fill(T value, long size) {
        if (size < 0) throw new IndexOutOfBoundsException("Size must be positive");
        return Tensor.fill(value, size).toVector();
    }

    public static <T> Vector<T> fill(T value, int size) {
        return fill(value, (long) size);
    }

    public long size() {
        return size(0);
    }

    @Override
    public Vector<T> backfill(T element) {
        return super.backfill(element).toVector();
    }

    public void push(T element) {
        map.put(Index.of(size()), element);
    }

    public T pop() {
        Index toPop = map.entrySet().stream()
                .max(Map.Entry.comparingByKey())
                .map(Map.Entry::getKey)
                .orElse(Index.of());
        return map.remove(toPop);
    }

    public T  shift() {
        Index toShift = map.entrySet().stream()
                .min(Map.Entry.comparingByKey())
                .map(Map.Entry::getKey)
                .orElse(Index.of());

        T shifted = map.remove(toShift);
        computeAndUpdateIndices(e -> Map.entry(e.getKey().compute(i -> i - 1), e.getValue()));
        return shifted;
    }

    public void unshift(T element) {
        Map<Index, T> updated = this.map.entrySet().stream()
                .map(e -> Map.entry(e.getKey().compute(i -> i + 1), e.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        map.clear();
        map.putAll(updated);
        set(element, Index.of(0));
    }

    @Override
    public Vector<T> transpose() {
        return new Vector<>(map);
    }

    @Override
    public <S> Vector<S> compute(Function<T, S> computeFunction) {
        return super.compute(computeFunction).toVector();
    }

    @Override
    public <S> Vector<S> computeWithIndices(Function<Map.Entry<Index, T>, S> computeFunction) {
        return super.computeWithIndices(computeFunction).toVector();
    }

    @Override
    public Scalar<T> reduce(T identity, BinaryOperator<T> accumulator, int dimension) {
        return super.reduce(identity, accumulator, dimension).toScalar();
    }

    @Override
    public <S> Scalar<S> reduce(S identity, BiFunction<S, T, S> accumulator, BinaryOperator<S> combiner, int dimension) {
        return super.reduce(identity, accumulator, combiner, dimension).toScalar();
    }

    @Override
    public Vector<T> mask(Tensor<Boolean> mask, T maskedValue) {
        return super.mask(mask, maskedValue).toVector();
    }

    @Override
    public <S, U> Vector<S> piecewise(BiFunction<T, U, S> piecewiseFunction, Tensor<U> other) {
        return super.piecewise(piecewiseFunction, other).toVector();
    }

    @Override
    public Scalar<T> slice(Map<Integer, Long> constraints) {
        return super.slice(constraints).toScalar();
    }

    @Override
    public <S> Vector<S> expect(Class<S> type) {
        return super.expect(type).toVector();
    }

    @Override
    public Matrix<T> extrude(long size) {
        return super.extrude(size).toMatrix();
    }

    @Override
    public Vector<T> extract(Index min, Index max) {
        return super.extract(min, max).toVector();
    }

}
