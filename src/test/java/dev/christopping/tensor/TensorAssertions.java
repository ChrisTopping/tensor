package dev.christopping.tensor;

import static org.assertj.core.api.Assertions.assertThat;

public class TensorAssertions {

    public static <T> void assertTensor(Tensor<T> tensor, String expected) {
        assertThat(tensor.toString("", "", " ", " | ", " ", true)).isEqualTo(expected);
    }

    public static <T> void assertScalar(Scalar<T> scalar, String expected) {
        assertThat(scalar.toString()).isEqualTo(expected);
    }

    public static <T> void assertVector(Vector<T> vector, String expected) {
        assertThat(vector.toString()).isEqualTo(expected);
    }

    public static <T> void assertMatrix(Matrix<T> matrix, String expected) {
        assertThat(matrix.toString()).isEqualTo(expected);
    }

}
