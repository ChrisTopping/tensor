package dev.christopping.tensor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TensorTest {

    private static final Integer[][] INT_ARRAY_2D = {{1, 2}, {3, 4}, {5, 6}};
    private static final Integer[][][] INT_ARRAY_3D = {{{1, 2}, {3, 4}, {5, 6}}, {{10, 20}, {30, 40}, {50, 60}}};

    private <T> void assertTensor(Tensor<T> tensor, String string) {
        assertThat(tensor.toString()).isEqualTo(string);
    }

    @DisplayName("empty()")
    @Nested
    class Empty {

        @DisplayName("Should create empty tensor")
        @Test
        void shouldCreateEmptyTensor() {
            Tensor<String> tensor = Tensor.empty();
            assertThat(tensor.order()).isEqualTo(0);
            assertThat(tensor.dimensions()).isEmpty();
            assertThat(tensor.get()).isNull();
        }

    }

    @DisplayName("of(List<?> list, Class<T> type)")
    @Nested
    class OfListWithType {

        @DisplayName("Given empty list - should return empty tensor")
        @Test
        void givenEmptyList_shouldReturnEmptyTensor() {
            assertThat(Tensor.of(new ArrayList<>(), Object.class)).isEqualTo(Tensor.empty());
        }

        @DisplayName("Given single valued list - should return scalar")
        @Test
        void givenSingleValuedList_shouldReturnScalar() {
            Tensor<String> tensor = Tensor.of(List.of("abc"), String.class);
            assertThat(tensor.order()).isEqualTo(0);
            assertTensor(tensor, "abc");
        }

        @DisplayName("Given list of values - should return vector")
        @Test
        void givenListOfValues_shouldReturnVector() {
            Tensor<String> tensor = Tensor.of(List.of("abc", "def"), String.class);
            assertThat(tensor.order()).isEqualTo(1);
            assertTensor(tensor, "abc def");
        }

        @DisplayName("Given order 2 nested list - should return matrix")
        @Test
        void givenOrder2NestedList_shouldReturnMatrix() {
            Tensor<String> tensor = Tensor.of(List.of(List.of("abc", "def"), List.of("ghi", "jkl")), String.class);
            assertThat(tensor.order()).isEqualTo(2);
            assertTensor(tensor, "abc def | ghi jkl");
        }

        @DisplayName("Given order 3 nested list - should return 3-tensor")
        @Test
        void givenOrder3NestedList_shouldReturn3Tensor() {
            Tensor<String> tensor = Tensor.of(List.of(List.of(List.of("abc", "def"), List.of("ghi", "jkl")), List.of(List.of("mno", "pqr"), List.of("stu", "vwx"))), String.class);
            assertThat(tensor.order()).isEqualTo(3);
            assertTensor(tensor, "abc def | ghi jkl || mno pqr | stu vwx");
        }

        @DisplayName("Given superclass - should return superclass typed tensor")
        @Test
        void givenSuperclassType_shouldReturnSuperclassTypedTensor() {
            Tensor<Object> tensor = Tensor.of(List.of("abc", "def"), Object.class);
            assertThat(tensor.order()).isEqualTo(1);
            assertTensor(tensor, "abc def");
        }

    }

    @DisplayName("of(Object[] array, Class<T> type)")
    @Nested
    class OfArrayWithType {

        @DisplayName("Given empty array - should return empty tensor")
        @Test
        void givenEmptyArray_shouldReturnEmptyTensor() {
            assertThat(Tensor.of(new Object[]{}, Object.class)).isEqualTo(Tensor.empty());
        }

        @DisplayName("Given single valued array - should return scalar")
        @Test
        void givenSingleValuedArray_shouldReturnScalar() {
            Tensor<String> tensor = Tensor.of(new Object[]{"abc"}, String.class);
            assertThat(tensor.order()).isEqualTo(0);
            assertTensor(tensor, "abc");
        }

        @DisplayName("Given array of values - should return vector")
        @Test
        void givenArrayOfValues_shouldReturnVector() {
            Tensor<String> tensor = Tensor.of(new Object[]{"abc", "def"}, String.class);
            assertThat(tensor.order()).isEqualTo(1);
            assertTensor(tensor, "abc def");
        }

        @DisplayName("Given order 2 nested array - should return matrix")
        @Test
        void givenOrder2NestedArray_shouldReturnMatrix() {
            Tensor<String> tensor = Tensor.of(new Object[][]{{"abc", "def"},{"ghi", "jkl"}}, String.class);
            assertThat(tensor.order()).isEqualTo(2);
            assertTensor(tensor, "abc def | ghi jkl");
        }

        @DisplayName("Given order 3 nested array - should return 3-tensor")
        @Test
        void givenOrder3NestedArray_shouldReturn3Tensor() {

            Tensor<String> tensor = Tensor.of(new Object[][][]{{{"abc", "def"},{"ghi", "jkl"}},{{"mno", "pqr"},{"stu", "vwx"}}}, String.class);
            assertThat(tensor.order()).isEqualTo(3);
            assertTensor(tensor, "abc def | ghi jkl || mno pqr | stu vwx");
        }

        @DisplayName("Given superclass - should return superclass typed tensor")
        @Test
        void givenSuperclassType_shouldReturnSuperclassTypedTensor() {
            Tensor<Object> tensor = Tensor.of(new Object[]{"abc", "def"}, Object.class);
            assertThat(tensor.order()).isEqualTo(1);
            assertTensor(tensor, "abc def");
        }

    }

    @DisplayName("fill(T value, Index index)")
    @Nested
    class FillByIndex {

        @DisplayName("Given negative vararg - should throw error")
        @Test
        void givenNegativeVararg_shouldThrowError() {
            assertThatThrownBy(() -> Tensor.fill("123", Index.of(-1L))).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Given 1 vararg - should return tensor with vector value")
        @Test
        void given1Vararg_shouldReturnTensorWithVectorValue() {
            Tensor<String> tensor = Tensor.fill("123", Index.of(2L));
            assertThat(tensor.order()).isEqualTo(1);
            assertThat(tensor.dimensions()).containsExactly(3L);
            assertTensor(tensor, "123 123 123");
        }

        @DisplayName("Given 2 varargs - should return tensor with matrix value")
        @Test
        void given2Varargs_shouldReturnTensorWithMatrixValue() {
            Tensor<String> tensor = Tensor.fill("123", Index.of(2L, 2L));
            assertThat(tensor.order()).isEqualTo(2);
            assertThat(tensor.dimensions()).containsExactly(3L, 3L);
            assertTensor(tensor, "123 123 123 | 123 123 123 | 123 123 123");
        }

        @DisplayName("Given 3 varargs - should return tensor with 3D value")
        @Test
        void given3Varargs_shouldReturnTensorWith3DValue() {
            Tensor<String> tensor = Tensor.fill("123", Index.of(2L, 2L, 1L));
            assertThat(tensor.order()).isEqualTo(3);
            assertThat(tensor.dimensions()).containsExactly(3L, 3L, 2L);
            assertTensor(tensor, "123 123 123 | 123 123 123 | 123 123 123 || 123 123 123 | 123 123 123 | 123 123 123");
        }

    }

    @DisplayName("fill(T value, long... dimensions)")
    @Nested
    class FillByLongVarargs {

        @DisplayName("Given empty varargs - should return tensor with scalar value")
        @Test
        void givenEmptyVarargs_shouldReturnTensorWithScalarValue() {
            Tensor<String> tensor = Tensor.fill("123");
            assertThat(tensor.order()).isEqualTo(0);
            assertThat(tensor.dimensions()).isEmpty();
            assertThat(tensor.get()).isEqualTo("123");
        }

        @DisplayName("Given negative vararg - should throw error")
        @Test
        void givenNegativeVararg_shouldThrowError() {
            assertThatThrownBy(() -> Tensor.fill("123", -1L)).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Given 0-valued vararg - should throw error")
        @Test
        void given0ValuedVararg_shouldThrowError() {
            assertThatThrownBy(() -> Tensor.fill("123", 0L)).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Given 1 vararg - should return tensor with vector value")
        @Test
        void given1Vararg_shouldReturnTensorWithVectorValue() {
            Tensor<String> tensor = Tensor.fill("123", 3L);
            assertThat(tensor.order()).isEqualTo(1);
            assertThat(tensor.dimensions()).containsExactly(3L);
            assertTensor(tensor, "123 123 123");
        }

        @DisplayName("Given 2 varargs - should return tensor with matrix value")
        @Test
        void given2Varargs_shouldReturnTensorWithMatrixValue() {
            Tensor<String> tensor = Tensor.fill("123", 3L, 3L);
            assertThat(tensor.order()).isEqualTo(2);
            assertThat(tensor.dimensions()).containsExactly(3L, 3L);
            assertTensor(tensor, "123 123 123 | 123 123 123 | 123 123 123");
        }

        @DisplayName("Given 3 varargs - should return tensor with 3D value")
        @Test
        void given3Varargs_shouldReturnTensorWith3DValue() {
            Tensor<String> tensor = Tensor.fill("123", 3L, 3L, 2L);
            assertThat(tensor.order()).isEqualTo(3);
            assertThat(tensor.dimensions()).containsExactly(3L, 3L, 2L);
            assertTensor(tensor, "123 123 123 | 123 123 123 | 123 123 123 || 123 123 123 | 123 123 123 | 123 123 123");
        }

    }

    @DisplayName("fill(T value, int... dimensions")
    @Nested
    class FillByIntVarargs {

        @DisplayName("Given negative vararg - should throw error")
        @Test
        void givenNegativeVararg_shouldThrowError() {
            assertThatThrownBy(() -> Tensor.fill("123", -1)).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Given 0-valued vararg - should throw error")
        @Test
        void given0ValuedVararg_shouldThrowError() {
            assertThatThrownBy(() -> Tensor.fill("123", 0)).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Given 1 vararg - should return tensor with vector value")
        @Test
        void given1Vararg_shouldReturnTensorWithVectorValue() {
            Tensor<String> tensor = Tensor.fill("123", 3);
            assertThat(tensor.order()).isEqualTo(1);
            assertThat(tensor.dimensions()).containsExactly(3L);
            assertTensor(tensor, "123 123 123");
        }

        @DisplayName("Given 2 varargs - should return tensor with matrix value")
        @Test
        void given2Varargs_shouldReturnTensorWithMatrixValue() {
            Tensor<String> tensor = Tensor.fill("123", 3, 3);
            assertThat(tensor.order()).isEqualTo(2);
            assertThat(tensor.dimensions()).containsExactly(3L, 3L);
            assertTensor(tensor, "123 123 123 | 123 123 123 | 123 123 123");
        }

        @DisplayName("Given 3 varargs - should return tensor with 3D value")
        @Test
        void given3Varargs_shouldReturnTensorWith3DValue() {
            Tensor<String> tensor = Tensor.fill("123", 3, 3, 2);
            assertThat(tensor.order()).isEqualTo(3);
            assertThat(tensor.dimensions()).containsExactly(3L, 3L, 2L);
            assertTensor(tensor, "123 123 123 | 123 123 123 | 123 123 123 || 123 123 123 | 123 123 123 | 123 123 123");
        }

    }

    @DisplayName("generate(Function<Index, T> generator, int... dimensions)")
    @Nested
    class GenerateByIndex {

        @DisplayName("Given null generator function - should throw error")
        @Test
        void givenNullGeneratorFunction_shouldThrowError() {
            assertThatThrownBy(() -> Tensor.generate(null, Index.of(1, 2, 3))).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Given 1 dimension - should return vector")
        @Test
        void given1Dimension_shouldReturnScalar() {
            Tensor<String> tensor = Tensor.generate(Index::toString, Index.of(2));
            assertTensor(tensor, "(0) (1) (2)");
        }

        @DisplayName("Given 2 dimensions - should return matrix")
        @Test
        void given2Dimensions_shouldReturnMatrix() {
            Tensor<String> tensor = Tensor.generate(Index::toString, Index.of(1, 1));
            assertTensor(tensor, "(0, 0) (1, 0) | (0, 1) (1, 1)");
        }

        @DisplayName("Given 3 dimensions - should return 3-tensor")
        @Test
        void given3Dimensions_shouldReturn3Tensor() {
            Tensor<String> tensor = Tensor.generate(Index::toString, Index.of(1, 1, 1));
            assertTensor(tensor, "(0, 0, 0) (1, 0, 0) | (0, 1, 0) (1, 1, 0) || (0, 0, 1) (1, 0, 1) | (0, 1, 1) (1, 1, 1)");
        }

    }

    @DisplayName("generate(Function<Index, T> generator, long... dimensions)")
    @Nested
    class GenerateByLongVarargs {

        @DisplayName("Given null generator function - should throw error")
        @Test
        void givenNullGeneratorFunction_shouldThrowError() {
            assertThatThrownBy(() -> Tensor.generate(null, 1L, 2L, 3L)).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Given empty dimensions - should return scalar")
        @Test
        void givenEmptyDimensions_shouldReturnScalar() {
            Tensor<Object> tensor = Tensor.generate(index -> "abc");
            assertThat(tensor.isEmpty()).isFalse();
            assertTensor(tensor, "abc");
        }

        @DisplayName("Given 1 dimension - should return vector")
        @Test
        void given1Dimension_shouldReturnVector() {
            Tensor<String> tensor = Tensor.generate(Index::toString, 3L);
            assertTensor(tensor, "(0) (1) (2)");
        }

        @DisplayName("Given 2 dimensions - should return matrix")
        @Test
        void given2Dimensions_shouldReturnMatrix() {
            Tensor<String> tensor = Tensor.generate(Index::toString, 2L, 2L);
            assertTensor(tensor, "(0, 0) (1, 0) | (0, 1) (1, 1)");
        }

        @DisplayName("Given 3 dimensions - should return 3-tensor")
        @Test
        void given3Dimensions_shouldReturn3Tensor() {
            Tensor<String> tensor = Tensor.generate(Index::toString, 2L, 2L, 2L);
            assertTensor(tensor, "(0, 0, 0) (1, 0, 0) | (0, 1, 0) (1, 1, 0) || (0, 0, 1) (1, 0, 1) | (0, 1, 1) (1, 1, 1)");
        }

    }

    @DisplayName("generate(Function<Index, T> generator, int... dimensions)")
    @Nested
    class GenerateByIntVarargs {

        @DisplayName("Given null generator function - should throw error")
        @Test
        void givenNullGeneratorFunction_shouldThrowError() {
            assertThatThrownBy(() -> Tensor.generate(null, 1, 2, 3)).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Given 1 dimension - should return vector")
        @Test
        void given1Dimension_shouldReturnScalar() {
            Tensor<String> tensor = Tensor.generate(Index::toString, 3);
            assertTensor(tensor, "(0) (1) (2)");
        }

        @DisplayName("Given 2 dimensions - should return matrix")
        @Test
        void given2Dimensions_shouldReturnMatrix() {
            Tensor<String> tensor = Tensor.generate(Index::toString, 2, 2);
            assertTensor(tensor, "(0, 0) (1, 0) | (0, 1) (1, 1)");
        }

        @DisplayName("Given 3 dimensions - should return 3-tensor")
        @Test
        void given3Dimensions_shouldReturn3Tensor() {
            Tensor<String> tensor = Tensor.generate(Index::toString, 2, 2, 2);
            assertTensor(tensor, "(0, 0, 0) (1, 0, 0) | (0, 1, 0) (1, 1, 0) || (0, 0, 1) (1, 0, 1) | (0, 1, 1) (1, 1, 1)");
        }

    }

    @DisplayName("combine(List<Tensor<T>> slices)")
    @Nested
    class CombineList {

        @DisplayName("Given list is empty - should return empty tensor")
        @Test
        void givenListIsEmpty_shouldReturnEmptyTensor() {
            assertThat(Tensor.combine(List.of())).isEqualTo(Tensor.empty());
        }

        @DisplayName("Given single tensor - should return tensor")
        @Test
        void givenSingleTensor_shouldReturnTensor() {
            Tensor<String> tensor = Tensor.empty();
            tensor.set("abc");

            assertTensor(Tensor.combine(List.of(tensor)), "abc");
        }

        @DisplayName("Given two tensors - should create tensor of higher order containing both slices")
        @Test
        void givenTwoTensors_shouldCreateTensorOfHigherOrderContainingBothSlices() {
            Tensor<Integer> tensor1 = Tensor.of(new Integer[][]{{1, 2}, {3, 4}}, Integer.class);
            Tensor<Integer> tensor2 = Tensor.of(new Integer[][]{{5, 6}, {7, 8}}, Integer.class);
            assertTensor(Tensor.combine(List.of(tensor1, tensor2)), "1 2 | 3 4 || 5 6 | 7 8");
        }

        @DisplayName("Given three tensors - should create tensor of higher order containing all slices")
        @Test
        void givenThreeTensors_shouldCreateTensorOfHigherOrderContainingAllSlices() {
            Tensor<Integer> tensor1 = Tensor.of(new Integer[][]{{1, 2}, {3, 4}}, Integer.class);
            Tensor<Integer> tensor2 = Tensor.of(new Integer[][]{{5, 6}, {7, 8}}, Integer.class);
            Tensor<Integer> tensor3 = Tensor.of(new Integer[][]{{9, 10}, {11, 12}}, Integer.class);
            assertTensor(Tensor.combine(List.of(tensor1, tensor2, tensor3)), "1 2 | 3 4 || 5 6 | 7 8 || 9 10 | 11 12");
        }

    }

    @DisplayName("combine(Tensor<T>[] slices)")
    @Nested
    class CombineArray {

        @DisplayName("Given list is empty - should return empty tensor")
        @Test
        void givenListIsEmpty_shouldReturnEmptyTensor() {
            assertThat(Tensor.combine(new Tensor[]{})).isEqualTo(Tensor.empty());
        }

        @DisplayName("Given single tensor - should return tensor")
        @Test
        void givenSingleTensor_shouldReturnTensor() {
            Tensor<String> tensor = Tensor.empty();
            tensor.set("abc");

            assertTensor(Tensor.combine(new Tensor[]{tensor}), "abc");
        }

        @DisplayName("Given two tensors - should create tensor of higher order containing both slices")
        @Test
        void givenTwoTensors_shouldCreateTensorOfHigherOrderContainingBothSlices() {
            Tensor<Integer> tensor1 = Tensor.of(new Integer[][]{{1, 2}, {3, 4}}, Integer.class);
            Tensor<Integer> tensor2 = Tensor.of(new Integer[][]{{5, 6}, {7, 8}}, Integer.class);
            assertTensor(Tensor.combine(new Tensor[]{tensor1, tensor2}), "1 2 | 3 4 || 5 6 | 7 8");
        }

        @DisplayName("Given three tensors - should create tensor of higher order containing all slices")
        @Test
        void givenThreeTensors_shouldCreateTensorOfHigherOrderContainingAllSlices() {
            Tensor<Integer> tensor1 = Tensor.of(new Integer[][]{{1, 2}, {3, 4}}, Integer.class);
            Tensor<Integer> tensor2 = Tensor.of(new Integer[][]{{5, 6}, {7, 8}}, Integer.class);
            Tensor<Integer> tensor3 = Tensor.of(new Integer[][]{{9, 10}, {11, 12}}, Integer.class);
            assertTensor(Tensor.combine(new Tensor[]{tensor1, tensor2, tensor3}), "1 2 | 3 4 || 5 6 | 7 8 || 9 10 | 11 12");
        }

    }

    @DisplayName("get(Index index)")
    @Nested
    class GetByIndex {

        @DisplayName("Given null - return null")
        @Test
        void givenNull_doSomething() {
            Tensor<Integer> tensor = Tensor.of(INT_ARRAY_2D, Integer.class);
            assertThat(tensor.get()).isNull();
        }

        @DisplayName("Given mapping exists - should return value")
        @Test
        void givenMappingExists_shouldReturnValue() {
            Tensor<Integer> tensor = Tensor.of(INT_ARRAY_2D, Integer.class);

            assertThat(tensor.get(Index.of(0, 0))).isEqualTo(1);
            assertThat(tensor.get(Index.of(1, 0))).isEqualTo(2);
            assertThat(tensor.get(Index.of(0, 1))).isEqualTo(3);
            assertThat(tensor.get(Index.of(1, 1))).isEqualTo(4);
            assertThat(tensor.get(Index.of(0, 2))).isEqualTo(5);
            assertThat(tensor.get(Index.of(1, 2))).isEqualTo(6);
        }

        @DisplayName("Given mapping does not exist - should throw exception")
        @Test
        void givenMappingDoesNotExist_shouldReturnNull() {
            Tensor<Integer> tensor = Tensor.of(INT_ARRAY_2D, Integer.class);
            assertThat(tensor.get(Index.of(2, 0))).isNull();
        }

    }

    @DisplayName("get(long... coordinates")
    @Nested
    class GetByLongVarargs {

        @DisplayName("Given mapping exists - should return value")
        @Test
        void givenMappingExists_shouldReturnValue() {
            Tensor<Integer> tensor = Tensor.of(INT_ARRAY_2D, Integer.class);

            assertThat(tensor.get(0L, 0L)).isEqualTo(1);
            assertThat(tensor.get(1L, 0L)).isEqualTo(2);
            assertThat(tensor.get(0L, 1L)).isEqualTo(3);
            assertThat(tensor.get(1L, 1L)).isEqualTo(4);
            assertThat(tensor.get(0L, 2L)).isEqualTo(5);
            assertThat(tensor.get(1L, 2L)).isEqualTo(6);
        }

        @DisplayName("Given mapping does not exist - should throw exception")
        @Test
        void givenMappingDoesNotExist_shouldReturnNull() {
            Tensor<Integer> tensor = Tensor.of(INT_ARRAY_2D, Integer.class);
            assertThat(tensor.get(2L, 0L)).isNull();
        }

    }

    @DisplayName("get(int... coordinates)")
    @Nested
    class GetByIntVarargs {

        @DisplayName("Given mapping exists - should return value")
        @Test
        void givenMappingExists_shouldReturnValue() {
            Tensor<Integer> tensor = Tensor.of(INT_ARRAY_2D, Integer.class);

            assertThat(tensor.get(0, 0)).isEqualTo(1);
            assertThat(tensor.get(1, 0)).isEqualTo(2);
            assertThat(tensor.get(0, 1)).isEqualTo(3);
            assertThat(tensor.get(1, 1)).isEqualTo(4);
            assertThat(tensor.get(0, 2)).isEqualTo(5);
            assertThat(tensor.get(1, 2)).isEqualTo(6);
        }

        @DisplayName("Given mapping does not exist - should throw exception")
        @Test
        void givenMappingDoesNotExist_shouldReturnNull() {
            Tensor<Integer> tensor = Tensor.of(INT_ARRAY_2D, Integer.class);
            assertThat(tensor.get(2, 0)).isNull();
        }

    }

    @DisplayName("set(T value, Index index)")
    @Nested
    class SetByIndex {

        @DisplayName("Given order of coordinates less than order of tensor - should throw error")
        @Test
        void givenOrderOfCoordinatesLessThanOrderOfTensor_shouldThrowError() {
            assertThatThrownBy(() -> Tensor.of(new Integer[]{1}, Integer.class).set(1, Index.of(1, 2))).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Given order of coordinates greater than order of tensor")
        @Test
        void givenOrderOfCoordinatesGreaterThanOrderOfTensor_shouldThrowError() {
            assertThatThrownBy(() -> Tensor.of(INT_ARRAY_3D, Integer.class).set(1, Index.of(1, 2, 3, 4))).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Given unmapped coordinates - should set new value")
        @Test
        void givenUnmappedCoordinates_shouldSetNewValue() {
            Tensor<Integer> tensor = Tensor.of(INT_ARRAY_2D, Integer.class);

            assertThat(tensor.get(2L, 0L)).isNull();

            tensor.set(100, Index.of(2, 0));

            assertThat(tensor.get(2L, 0L)).isEqualTo(100);
        }

        @DisplayName("Given mapped coordinates - should replace value")
        @Test
        void givenMappedCoordinates_shouldReplaceValue() {
            Tensor<Integer> tensor = Tensor.of(INT_ARRAY_2D, Integer.class);

            assertThat(tensor.get(1L, 0L)).isEqualTo(2);

            tensor.set(100, Index.of(1, 0));

            assertThat(tensor.get(1L, 0L)).isEqualTo(100);
        }

    }

    @DisplayName("set(T value, long... coordinates)")
    @Nested
    class SetByLongVarargs {

        @DisplayName("Given order of coordinates less than order of tensor - should throw error")
        @Test
        void givenOrderOfCoordinatesLessThanOrderOfTensor_shouldThrowError() {
            assertThatThrownBy(() -> Tensor.of(INT_ARRAY_3D, Integer.class).set(1, 1L, 2L)).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Given order of coordinates greater than order of tensor")
        @Test
        void givenOrderOfCoordinatesGreaterThanOrderOfTensor_shouldThrowError() {
            assertThatThrownBy(() -> Tensor.of(INT_ARRAY_3D, Integer.class).set(1, 1L, 2L, 3L, 4L)).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Given unmapped coordinates - should set new value")
        @Test
        void givenUnmappedCoordinates_shouldSetNewValue() {
            Tensor<Integer> tensor = Tensor.of(INT_ARRAY_2D, Integer.class);

            assertThat(tensor.get(2L, 0L)).isNull();

            tensor.set(100, 2L, 0L);

            assertThat(tensor.get(2L, 0L)).isEqualTo(100);
        }

        @DisplayName("Given mapped coordinates - should replace value")
        @Test
        void givenMappedCoordinates_shouldReplaceValue() {
            Tensor<Integer> tensor = Tensor.of(INT_ARRAY_2D, Integer.class);

            assertThat(tensor.get(1L, 0L)).isEqualTo(2);

            tensor.set(100, 1L, 0L);

            assertThat(tensor.get(1L, 0L)).isEqualTo(100);
        }

    }

    @DisplayName("set(T value, int... coordinates)")
    @Nested
    class SetByIntVarargs {

        @DisplayName("Given order of coordinates less than order of tensor - should throw error")
        @Test
        void givenOrderOfCoordinatesLessThanOrderOfTensor_shouldThrowError() {
            assertThatThrownBy(() -> Tensor.of(INT_ARRAY_3D, Integer.class).set(1, 1, 2)).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Given order of coordinates greater than order of tensor")
        @Test
        void givenOrderOfCoordinatesGreaterThanOrderOfTensor_shouldThrowError() {
            assertThatThrownBy(() -> Tensor.of(INT_ARRAY_3D, Integer.class).set(1, 1, 2, 3, 4)).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Given unmapped coordinates - should set new value")
        @Test
        void givenUnmappedCoordinates_shouldSetNewValue() {
            Tensor<Integer> tensor = Tensor.of(INT_ARRAY_2D, Integer.class);

            assertThat(tensor.get(2, 0)).isNull();

            tensor.set(100, 2, 0);

            assertThat(tensor.get(2, 0)).isEqualTo(100);
        }

        @DisplayName("Given mapped coordinates - should replace value")
        @Test
        void givenMappedCoordinates_shouldReplaceValue() {
            Tensor<Integer> tensor = Tensor.of(INT_ARRAY_2D, Integer.class);

            assertThat(tensor.get(1, 0)).isEqualTo(2);

            tensor.set(100, 1, 0);

            assertThat(tensor.get(1, 0)).isEqualTo(100);
        }

    }

    @DisplayName("order()")
    @Nested
    class Order {

        @DisplayName("Given empty tensor - should return 0")
        @Test
        void givenEmptyTensor_shouldReturn0() {
            assertThat(Tensor.empty().order()).isEqualTo(0);
        }

        @DisplayName("Given matrix - should return 2")
        @Test
        void givenMatrix_shouldReturn2() {
            assertThat(Tensor.of(INT_ARRAY_2D, Integer.class).order()).isEqualTo(2);
        }

        @DisplayName("Given 3D tensor - should return 3")
        @Test
        void given3DTensor_shouldReturn3() {
            assertThat(Tensor.of(INT_ARRAY_3D, Integer.class).order()).isEqualTo(3);
        }

    }

    @DisplayName("size(int dimension)")
    @Nested
    class Size {

        @DisplayName("Given empty tensor - should return 0")
        @Test
        void givenEmptyTensor_shouldReturn0() {
            assertThat(Tensor.empty().size(0)).isEqualTo(0);
        }

        @DisplayName("Given dimension greater than tensor order - should return 0")
        @Test
        void givenDimensionGreaterThanTensorOrder_shouldReturn0() {
            assertThat(Tensor.of(INT_ARRAY_2D, Integer.class).size(2)).isEqualTo(0);
        }

        @DisplayName("Given dimension equal to or less than tensor order - should return correct value")
        @Test
        void givenDimensionGreaterEqualOrLessThanToTensorOrder_shouldReturnCorrectValue() {
            assertThat(Tensor.of(INT_ARRAY_2D, Integer.class).size(0)).isEqualTo(2);
            assertThat(Tensor.of(INT_ARRAY_2D, Integer.class).size(1)).isEqualTo(3);
            assertThat(Tensor.of(INT_ARRAY_3D, Integer.class).size(0)).isEqualTo(2);
            assertThat(Tensor.of(INT_ARRAY_3D, Integer.class).size(1)).isEqualTo(3);
            assertThat(Tensor.of(INT_ARRAY_3D, Integer.class).size(2)).isEqualTo(2);
        }

    }

    @DisplayName("dimensions()")
    @Nested
    class Dimensions {

        @DisplayName("Given empty tensor - should return empty list")
        @Test
        void givenEmptyTensor_shouldReturnEmptyList() {
            assertThat(Tensor.empty().dimensions()).isEmpty();
        }

        @DisplayName("Given non-empty tensor - should return list of dimensions in correct order")
        @Test
        void givenNonEmptyTensor_shouldReturnListOfDimensionsInCorrectOrder() {
            Tensor<String> tensor = Tensor.empty();
            tensor.set("A", 1, 2, 5, 6, 11);
            tensor.set("B", 0, 8, 3, 7, 9);
            tensor.set("C", 5, 3, 0, 1, 1);
            // assert dimensions contain max index taken from all coordinates (incremented to account for zero index)
            assertThat(tensor.dimensions()).containsExactly(6L, 9L, 6L, 8L, 12L);
        }

    }

    @DisplayName("transpose()")
    @Nested
    class Transpose {

        @DisplayName("Given empty - should return empty tensor")
        @Test
        void givenEmpty_shouldReturnEmptyTensor() {
            Tensor<Object> transposed = Tensor.empty().transpose();
            assertThat(transposed).isEqualTo(Tensor.empty());
        }

        @DisplayName("Given single value - should return same tensor")
        @Test
        void givenSingleValue_shouldReturnSameTensor() {
            Tensor<Integer> transposed = Tensor.fill(1, 1, 1).transpose();
            assertThat(transposed).isEqualTo(Tensor.fill(1, 1, 1));
        }

        @DisplayName("Given multiple values - should return transposed tensor")
        @Test
        void givenMultipleValues_shouldReturnTransposedTensor() {
            Tensor<Integer> transposed = Tensor.of(INT_ARRAY_2D, Integer.class).transpose();
            assertTensor(transposed, "1 3 5 | 2 4 6");
        }

    }

    @DisplayName("backfill(T value)")
    @Nested
    class Backfill {

        @DisplayName("Given empty tensor - null op")
        @Test
        void givenEmptyTensor_nullOp() {
            Tensor<Object> empty = Tensor.empty();
            empty.backfill(10);

            assertThat(empty).isEqualTo(empty);
        }

        @DisplayName("Given horizontal vector - should backfill values")
        @Test
        void givenHorizontalVector_shouldBackfillValues() {
            Tensor<Object> tensor = Tensor.empty();
            tensor.set(100, 2, 0);
            tensor.backfill("X");
            assertTensor(tensor, "X X 100");
        }

        @DisplayName("Given vertical vector - should backfill values")
        @Test
        void givenVerticalVector_shouldBackfillValues() {
            Tensor<Object> tensor = Tensor.empty();
            tensor.set(100, 0, 2);
            tensor.backfill("X");
            assertTensor(tensor, "X | X | 100");
        }

        @DisplayName("Given matrix - should backfill values")
        @Test
        void givenMatrix_shouldBackfillValues() {
            Tensor<Object> tensor = Tensor.empty();
            tensor.set(100, 1, 1);
            tensor.backfill("X");
            assertTensor(tensor, "X X | X 100");
        }

        @DisplayName("Given 3D tensor - should backfill values")
        @Test
        void given3DTensor_shouldBackfillValues() {
            Tensor<Object> tensor = Tensor.empty();
            tensor.set(100, 1, 1, 1);
            tensor.backfill("X");
            assertTensor(tensor, "X X | X X || X X | X 100");
        }

    }

    @DisplayName("compute(Function<T, S> computeFunction)")
    @Nested
    class Compute {

        @DisplayName("Given empty - null op")
        @Test
        void givenEmpty_NullOp() {
            Tensor<Integer> empty = Tensor.empty();
            empty.compute(integer -> -integer);

            assertThat(empty).isEqualTo(Tensor.empty());
        }

        @DisplayName("Given populated integer tensor - should apply compute function")
        @Test
        void givenPopulatedTensor_shouldApplyComputeFunction() {
            Tensor<Integer> tensor = Tensor.of(INT_ARRAY_2D, Integer.class);
            Tensor<Integer> computed = tensor.compute(integer -> -integer);

            assertTensor(computed, "-1 -2 | -3 -4 | -5 -6");
        }

    }

    @DisplayName("computeWithIndices(Function<Map.Entry<Index, T>, S> computeFunction)")
    @Nested
    class ComputeWithIndices {

        @DisplayName("Given empty - null op")
        @Test
        void givenEmpty_nullOp() {
            Tensor<Integer> empty = Tensor.empty();
            Tensor<String> computed = empty.computeWithIndices(entry -> "" + entry.getKey().coordinates().get(0) + entry.getKey().coordinates().get(1) + entry.getValue());

            assertThat(computed).isEqualTo(Tensor.empty());
        }

        @DisplayName("Given populated integer tensor - should apply compute function")
        @Test
        void givenPopulatedIntegerTensor_shouldApplyComputeFunction() {
            Tensor<Integer> tensor = Tensor.of(INT_ARRAY_2D, Integer.class);
            Tensor<String> computed = tensor.computeWithIndices(entry -> "" + entry.getKey().coordinates().get(0) + entry.getKey().coordinates().get(1) + entry.getValue());

            String[][] negativeIntArray = {{"001", "102"}, {"013", "114"}, {"025", "126"}};
            assertThat(computed).isEqualTo(Tensor.of(negativeIntArray, String.class));

            assertTensor(computed, "001 102 | 013 114 | 025 126");
        }

    }

    @DisplayName("computeAndUpdateIndices(Function<Map.Entry<Index, T>, Map.Entry<Index, S>> computeFunction)")
    @Nested
    class ComputeAndUpdateIndices {

        @DisplayName("Given empty tensor - null op")
        @Test
        void givenEmptyTensor_nullOp() {
            Tensor<Integer> empty = Tensor.empty();

            Function<Map.Entry<Index, Integer>, Map.Entry<Index, Integer>> computeFunction = entry ->
                    Map.entry(
                            Index.of(entry.getKey().coordinates().get(0) * 2, entry.getKey().coordinates().get(1) * 2),
                            entry.getValue() * 4
                    );
            Tensor<Integer> computed = empty.computeAndUpdateIndices(computeFunction);

            assertThat(computed).isEqualTo(Tensor.empty());
        }

        @DisplayName("Given populated tensor - should apply compute function")
        @Test
        void givenPopulatedTensor_shouldApplyComputeFunction() {
            Tensor<Integer> tensor = Tensor.of(INT_ARRAY_2D, Integer.class);

            Function<Map.Entry<Index, Integer>, Map.Entry<Index, Integer>> computeFunction = entry ->
                    Map.entry(
                            Index.of(entry.getKey().coordinates().get(0) * 2, entry.getKey().coordinates().get(1) * 2),
                            entry.getValue() * 4
                    );

            Tensor<Integer> computed = tensor.computeAndUpdateIndices(computeFunction);
            computed.backfill(0);

            assertTensor(computed, "4 0 8 | 0 0 0 | 12 0 16 | 0 0 0 | 20 0 24");
        }

    }

    @DisplayName("isEmpty()")
    @Nested
    class IsEmpty {

        @DisplayName("If empty - should return true")
        @Test
        void ifEmpty_shouldReturnTrue() {
            assertThat(Tensor.empty().isEmpty()).isTrue();
        }

        @DisplayName("If null valued - should return false")
        @Test
        void ifNullValued_shouldReturnFalse() {
            assertThat(Tensor.fill(null, 2, 2).isEmpty()).isFalse();
        }

        @DisplayName("If not empty - should return false")
        @Test
        void ifNotEmpty_shouldReturnFalse() {
            assertThat(Tensor.of(INT_ARRAY_2D, Integer.class).isEmpty()).isFalse();
        }

    }

    @DisplayName("slice(Map<Integer, Long> constraints)")
    @Nested
    class Slice {

        @DisplayName("Given empty tensor - should return empty tensor")
        @Test
        void givenEmptyTensor_shouldReturnEmptyTensor() {
            Tensor<Object> tensor = Tensor.empty();
            assertThat(tensor.slice(Map.of())).isEqualTo(Tensor.empty());
        }

        @DisplayName("Given empty map - should return original tensor")
        @Test
        void givenEmptyMap_shouldReturnOriginalTensor() {
            Tensor<Integer> tensor = Tensor.of(INT_ARRAY_2D, Integer.class);
            assertThat(tensor.slice(Map.of())).isEqualTo(tensor);
        }

        @DisplayName("Given 1 constraint on 3D tensor - should return correct tensor")
        @Test
        void given1ConstraintOn3DTensor_shouldReturnCorrectMatrix() {
            Tensor<Integer> tensor = Tensor.of(INT_ARRAY_3D, Integer.class);
            Tensor<Integer> slice = tensor.slice(Map.of(2, 0L));
            assertThat(slice.order()).isEqualTo(2);
            assertTensor(slice, "1 2 | 3 4 | 5 6");

            Tensor<Integer> slice2 = tensor.slice(Map.of(2, 1L));
            assertThat(slice2.order()).isEqualTo(2);
            assertTensor(slice2, "10 20 | 30 40 | 50 60");
        }

        @DisplayName("Given 2 constraints on a 3D tensor - should return correct 1D tensor")
        @Test
        void given2ConstraintsOnA3DTensor_shouldReturnCorrect1DTensor() {
            Tensor<Integer> tensor = Tensor.of(INT_ARRAY_3D, Integer.class);
            Tensor<Integer> slice = tensor.slice(Map.of(0, 1L, 1, 0L));
            assertThat(slice.order()).isEqualTo(1);
            assertTensor(slice, "2 20");

            Tensor<Integer> slice2 = tensor.slice(Map.of(1, 1L, 2, 0L));
            assertThat(slice2.order()).isEqualTo(1);
            assertTensor(slice2, "3 4");
        }

    }

    @DisplayName("expect(Class<S> type)")
    @Nested
    class Expect {

        @DisplayName("Given empty - should always succeed")
        @Test
        void givenEmpty_expectShouldAlwaysSucceed() {
            Tensor<Object> tensor = Tensor.empty();
            tensor.expect(Boolean.class);
            tensor.expect(Integer.class);
        }

        @DisplayName("Given type is not assignable - should fail")
        @Test
        void givenTypeIsNotAssignable_shouldFail() {
            Tensor<Integer> tensor = Tensor.of(INT_ARRAY_2D, Integer.class);
            assertThatThrownBy(() -> tensor.expect(String.class)).isInstanceOf(IllegalArgumentException.class);
            assertThatThrownBy(() -> tensor.expect(Long.class)).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Given type is assignable - should succeed")
        @Test
        void givenTypeIsAssignable_shouldSucceed() {
            Tensor<Integer> tensor = Tensor.of(INT_ARRAY_2D, Integer.class);
            tensor.expect(Integer.class);
            tensor.expect(Object.class);
        }

    }

    @DisplayName("extrude(long size")
    @Nested
    class Extrude {

        @DisplayName("Given empty tensor - should return empty tensor")
        @Test
        void givenEmptyTensor_shouldReturnEmptyTensor() {
            assertThat(Tensor.empty().extrude(2)).isEqualTo(Tensor.empty());
        }

        @DisplayName("Given scalar - should return extruded vector of given size")
        @Test
        void givenScalar_shouldReturnExtrudedVectorOfGivenSize() {
            Tensor<Object> tensor = Tensor.empty();
            tensor.set("abc");
            assertTensor(tensor.extrude(3), "abc abc abc");
        }

        @DisplayName("Given vector - should return extruded matrix of given size")
        @Test
        void givenVector_shouldReturnExtrudedMatrixOfGivenSize() {
            Tensor<String> tensor = Tensor.of(new String[]{"abc", "def", "ghi"}, String.class);
            assertTensor(tensor.extrude(3), "abc def ghi | abc def ghi | abc def ghi");
        }

        @DisplayName("Given matrix - should return extruded 3-tensor of given size")
        @Test
        void givenMatrix_shouldReturnExtruded3TensorOfGivenSize() {
            Tensor<Integer> tensor = Tensor.of(INT_ARRAY_2D, Integer.class);
            assertTensor(tensor.extrude(2), "1 2 | 3 4 | 5 6 || 1 2 | 3 4 | 5 6");
        }

    }

    @DisplayName("toMatrix()")
    @Nested
    class ToMatrix {

        @DisplayName("Given empty tensor - should throw error")
        @Test
        void givenEmptyTensor_shouldThrowError() {
            assertThatThrownBy(() -> Tensor.empty().toMatrix()).isInstanceOf(IllegalStateException.class);
        }

        @DisplayName("Given scalar - should throw error")
        @Test
        void givenScalar_shouldThrowError() {
            assertThatThrownBy(() -> Tensor.fill("123").toMatrix()).isInstanceOf(IllegalStateException.class);
        }

        @DisplayName("Given vector - should throw error")
        @Test
        void givenVector_shouldThrowError() {
            assertThatThrownBy(() -> Tensor.fill("123", 2).toMatrix()).isInstanceOf(IllegalStateException.class);
        }

        @DisplayName("Given matrix - should return matrix")
        @Test
        void givenMatrix_shouldReturnMatrix() {
            Matrix<String> matrix = Tensor.fill("123", 2, 2).toMatrix();
            assertThat(matrix).isInstanceOf(Matrix.class);
            assertThat(matrix.order()).isEqualTo(2);
            assertThat(matrix).hasToString("123 123 | 123 123");

        }

        @DisplayName("Given 3D tensor - should throw error")
        @Test
        void given3DTensor_shouldThrowError() {
            assertThatThrownBy(() -> Tensor.fill("123", 2, 2, 2).toMatrix()).isInstanceOf(IllegalStateException.class);
        }

    }

    @DisplayName("toVector()")
    @Nested
    class ToVector {

        @DisplayName("Given empty tensor - should throw error")
        @Test
        void givenEmptyTensor_shouldThrowError() {
            assertThatThrownBy(() -> Tensor.empty().toVector()).isInstanceOf(IllegalStateException.class);
        }

        @DisplayName("Given scalar - should throw error")
        @Test
        void givenScalar_shouldThrowError() {
            assertThatThrownBy(() -> Tensor.fill("123").toVector()).isInstanceOf(IllegalStateException.class);
        }

        @DisplayName("Given vector - should return vector")
        @Test
        void givenVector_shouldReturnVector() {
            Vector<String> vector = Tensor.fill("123", 2).toVector();
            assertThat(vector).isInstanceOf(Vector.class);
            assertThat(vector.order()).isEqualTo(1);
            assertThat(vector).hasToString("123 123");
        }

        @DisplayName("Given matrix - should throw error")
        @Test
        void givenMatrix_shouldThrowError() {
            assertThatThrownBy(() -> Tensor.fill("123", 2, 2).toVector()).isInstanceOf(IllegalStateException.class);
        }

        @DisplayName("Given 3D tensor - should throw error")
        @Test
        void given3DTensor_shouldThrowError() {
            assertThatThrownBy(() -> Tensor.fill("123", 2, 2, 2).toVector()).isInstanceOf(IllegalStateException.class);
        }

    }

    @DisplayName("toScalar()")
    @Nested
    class ToScalar {

        @DisplayName("Given empty tensor - should return empty scalar")
        @Test
        void givenEmptyTensor_shouldThrowError() {
            Scalar<Object> scalar = Tensor.empty().toScalar();
            assertThat(scalar).isInstanceOf(Scalar.class);
            assertThat(scalar.order()).isEqualTo(0);
            assertThat(scalar).hasToString("");
        }

        @DisplayName("Given scalar - should return scalar")
        @Test
        void givenScalar_shouldThrowError() {
            Scalar<String> scalar = Tensor.fill("123").toScalar();
            assertThat(scalar).isInstanceOf(Scalar.class);
            assertThat(scalar.order()).isEqualTo(0);
            assertThat(scalar).hasToString("123");
        }

        @DisplayName("Given vector - should throw error")
        @Test
        void givenVector_shouldReturnVector() {
            assertThatThrownBy(() -> Tensor.fill("123", 2).toScalar()).isInstanceOf(IllegalStateException.class);
        }

        @DisplayName("Given matrix - should throw error")
        @Test
        void givenMatrix_shouldThrowError() {
            assertThatThrownBy(() -> Tensor.fill("123", 2, 2).toScalar()).isInstanceOf(IllegalStateException.class);
        }

        @DisplayName("Given 3D tensor - should throw error")
        @Test
        void given3DTensor_shouldThrowError() {
            assertThatThrownBy(() -> Tensor.fill("123", 2, 2, 2).toScalar()).isInstanceOf(IllegalStateException.class);
        }

    }

    @DisplayName("toString(String defaultValue")
    @Nested
    class ToStringWithDefaultValue {
        @DisplayName("Given 3D sparse tensor - should return tensor as strings with default value")
        @Test
        void given3DSparseTensor_shouldReturnTensorAsStringsWithDefaultValue() {
            Tensor<Integer> tensor = Tensor.empty();
            tensor.set(1, 0, 0, 0);
            tensor.set(2, 0, 1, 0);
            tensor.set(3, 1, 0, 1);
            tensor.set(4, 1, 2, 1);
            assertThat(tensor.toString("x")).isEqualTo("1 x | 2 x | x x || x 3 | x x | x 4");
        }
    }

    @DisplayName("toString()")
    @Nested
    class ToString {

        @DisplayName("Given empty - should return empty string")
        @Test
        void givenEmpty_shouldReturnEmptyString() {
            assertThat(Tensor.empty().toString()).isEqualTo("");
        }

        @DisplayName("Given single value - should return single value as string")
        @Test
        void givenSingleValue_shouldReturnSingleValueAsString() {
            Tensor<Object> tensor = Tensor.empty();
            tensor.set(123, 0, 0);
            assertThat(tensor.toString()).isEqualTo("123");
        }

        @DisplayName("Given horizontal vector - should return space separated values as strings")
        @Test
        void givenHorizontalVector_shouldReturnSpaceSeparatedValues() {
            Tensor<Object> tensor = Tensor.empty();
            tensor.set(1, 0, 0);
            tensor.set(2, 1, 0);
            tensor.set(3, 2, 0);
            assertThat(tensor.toString()).isEqualTo("1 2 3");
        }

        @DisplayName("Given vertical vector - should return pipe separated values as strings")
        @Test
        void givenVerticalVector_shouldReturnPipeSeparatedValues() {
            Tensor<Object> tensor = Tensor.empty();
            tensor.set(1, 0, 0);
            tensor.set(2, 0, 1);
            tensor.set(3, 0, 2);
            assertThat(tensor.toString()).isEqualTo("1 | 2 | 3");
        }

        @DisplayName("Given matrix - should return matrix as strings")
        @Test
        void givenMatrix_shouldReturnMatrixAsStrings() {
            Tensor<Integer> tensor = Tensor.of(INT_ARRAY_2D, Integer.class);
            assertThat(tensor.toString()).isEqualTo("1 2 | 3 4 | 5 6");
        }

        @DisplayName("Given 3D tensor - should return tensor as strings")
        @Test
        void given3DTensor_shouldReturnTensorAsStrings() {
            Tensor<Integer> tensor = Tensor.of(INT_ARRAY_3D, Integer.class);
            assertThat(tensor.toString()).isEqualTo("1 2 | 3 4 | 5 6 || 10 20 | 30 40 | 50 60");
        }

        @DisplayName("Given 3D sparse tensor - should return tensor as strings")
        @Test
        void given3DSparseTensor_shouldReturnTensorAsStrings() {
            Tensor<Integer> tensor = Tensor.empty();
            tensor.set(1, 0, 0, 0);
            tensor.set(2, 0, 1, 0);
            tensor.set(3, 1, 0, 1);
            tensor.set(4, 1, 1, 0);
            assertThat(tensor.toString()).isEqualTo("1   | 2 4 ||   3 |");
        }

    }

    @DisplayName("equals(Object o)")
    @Nested
    class Equals {

        @DisplayName("Given null - should return false")
        @Test
        void givenNull_shouldReturnFalse() {
            assertThat(Tensor.empty().equals(null)).isFalse();
        }

        @DisplayName("Given non-tensor object - should return false")
        @Test
        void givenNonTensorObject_shouldReturnFalse() {
            assertThat(Tensor.empty().equals("")).isFalse();
        }

        @DisplayName("Given unequal tensor - should return false")
        @Test
        void givenUnequalTensor_shouldReturnFalse() {
            Tensor<Integer> t1 = Tensor.of(INT_ARRAY_2D, Integer.class);
            Tensor<Integer> t2 = Tensor.of(INT_ARRAY_3D, Integer.class);
            assertThat(t1.equals(t2)).isFalse();
        }

        @DisplayName("Given both are empty - should return true")
        @Test
        void givenBothAreEmpty_shouldReturnTrue() {
            assertThat(Tensor.empty().equals(Tensor.empty())).isTrue();
        }

        @DisplayName("Given both are equal - should return true")
        @Test
        void givenBothAreEqual_shouldReturnTrue() {
            Tensor<Integer> t1 = Tensor.of(INT_ARRAY_2D, Integer.class);
            Tensor<Integer> t2 = Tensor.of(INT_ARRAY_2D, Integer.class);
            assertThat(t1.equals(t2)).isTrue();
            assertThat(t2.equals(t1)).isTrue();
        }

    }

    @DisplayName("hashCode()")
    @Nested
    class HashCode {

        @DisplayName("Given empty - hashCode should be repeatable")
        @Test
        void givenEmpty_hashCodeShouldBeRepeatable() {
            int hashCode1 = Tensor.empty().hashCode();
            int hashCode2 = Tensor.empty().hashCode();
            assertThat(hashCode1).isEqualTo(hashCode2);
        }

        @DisplayName("Given non-empty - hashCode should be repeatable")
        @Test
        void givenNonEmpty_hashCodeShouldBeRepeatable() {
            int hashCode1 = Tensor.of(INT_ARRAY_3D, Integer.class).hashCode();
            int hashCode2 = Tensor.of(INT_ARRAY_3D, Integer.class).hashCode();
            assertThat(hashCode1).isEqualTo(hashCode2);

        }

    }
}