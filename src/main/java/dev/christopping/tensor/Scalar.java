package dev.christopping.tensor;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
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
    public Scalar<T> transpose() {
        return super.transpose().toScalar();
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
    public <S, U> Scalar<S> piecewise(BiFunction<T, U, S> piecewiseFunction, Tensor<U> other) {
        return super.piecewise(piecewiseFunction, other).toScalar();
    }

    @Override
    public <S> Scalar<S> expect(Class<S> type) {
        return super.expect(type).toScalar();
    }

    @Override
    public Vector<T> extrude(long size) {
        return super.extrude(size).toVector();
    }

    @Override
    public Vector<T> toVector() {
        return Vector.of(List.of(get()));
    }

    @Override
    public Matrix<T> toMatrix() {
        return Matrix.of(List.of(List.of(get())));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Scalar<?> scalar = (Scalar<?>) o;

        return scalar.get() == get();
    }
}
