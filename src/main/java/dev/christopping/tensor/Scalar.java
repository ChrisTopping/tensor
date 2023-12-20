package dev.christopping.tensor;

import java.util.Map;

public class Scalar<T> extends Tensor<T> {

    protected Scalar() {
        super();
    }

    protected Scalar(Map<Index, T> map) {
        super(map);
    }
}
