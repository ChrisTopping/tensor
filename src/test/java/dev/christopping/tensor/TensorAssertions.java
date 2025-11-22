package dev.christopping.tensor;

import static org.assertj.core.api.Assertions.assertThat;

public class TensorAssertions {

    public static <T> void assertTensor(Tensor<T> tensor, String expected) {
        assertThat(tensor.toString()).isEqualTo(expected);
    }

    public static <T> void assertScalar(Tensor<T> scalar, String expected) {
        assertThat(scalar.isScalar()).isTrue();
        assertThat(scalar.toString()).isEqualTo(expected);
    }

    public static <T> void assertVector(Tensor<T> vector, String expected) {
        assertThat(vector.isVector()).isTrue();
        assertThat(vector.toString()).isEqualTo(expected);
    }

    public static <T> void assertMatrix(Tensor<T> matrix, String expected) {
        assertThat(matrix.isMatrix()).isTrue();
        assertThat(matrix.toString()).isEqualTo(expected);
    }

}
