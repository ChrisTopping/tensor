package dev.christopping.tensor;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;

public class Scalar<T> extends Tensor<T> {

    protected Scalar() {
        super();
    }

    protected Scalar(Map<Index, T> map) {
        super(map);
    }

    public static <T> Scalar<T> of(T element) {
        Scalar<T> scalar = new Scalar<>();
        scalar.set(element);
        return scalar;
    }

    @Override
    public <S> Scalar<S> expect(Class<S> type) {
        return super.expect(type).toScalar();
    }

    @Override
    public Scalar<T> backfill(T element) {
        return new Scalar<>(map);
    }

    @Override
    public Scalar<T> transpose() {
        return new Scalar<T>(map);
    }

    @Override
    public <S> Scalar<S> compute(Function<T, S> computeFunction) {
        return super.compute(computeFunction).toScalar();
    }

    @Override
    public <S> Scalar<S> computeWithIndices(Function<Map.Entry<Index, T>, S> computeFunction) {
        return super.computeWithIndices(computeFunction).toScalar();
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
    public Scalar<T> mask(Tensor<Boolean> mask, T maskedValue) {
        return super.mask(mask, maskedValue).toScalar();
    }

    @Override
    public <S, U> Scalar<S> piecewise(BiFunction<T, U, S> piecewiseFunction, Tensor<U> other) {
        return super.piecewise(piecewiseFunction, other).toScalar();
    }

    @Override
    public Scalar<T> slice(Map<Integer, Long> constraints) {
        return super.slice(constraints).toScalar();
    }

    @Override
    public Vector<T> extrude(long size) {
        return super.extrude(size).toVector();
    }

    @Override
    public Scalar<T> extract(Index min, Index max) {
        return super.extract(min, max).toScalar();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Scalar<?> scalar = (Scalar<?>) o;

        return scalar.get() == get();
    }
}
