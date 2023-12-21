package dev.christopping.tensor;

import java.util.Map;

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
}
