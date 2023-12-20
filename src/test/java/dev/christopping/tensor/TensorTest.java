package dev.christopping.tensor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

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

    private <T> Tensor<T> build(T[][] values) {
        Tensor<T> tensor = Tensor.empty();
        for (int y = 0; y < values.length; y++) {
            for (int x = 0; x < values[0].length; x++) {
                tensor.set(values[y][x], x, y);
            }
        }
        return tensor;
    }

    private <T> Tensor<T> build(T[][][] values) {
        Tensor<T> tensor = Tensor.empty();
        for (int z = 0; z < values.length; z++) {
            for (int y = 0; y < values[0].length; y++) {
                for (int x = 0; x < values[0][0].length; x++) {
                    tensor.set(values[z][y][x], x, y, z);
                }
            }
        }
        return tensor;
    }

    // TODO: move tests as necessary
//    @DisplayName("getVector(int dimension, int index)")
//    @Nested
//    class GetVector {
//
//        @DisplayName("Given empty matrix - throw illegal argument exception")
//        @Test
//        void givenEmptyMatrix_throwIllegalArgumentException() {
//            assertThatThrownBy(() -> Matrix.empty().getVector(0, 0)).isInstanceOf(IllegalArgumentException.class);
//        }
//
//        @DisplayName("Given dimension does not exist - throw illegal argument exception")
//        @Test
//        void givenDimensionDoesNotExist_throwIllegalArgumentException() {
//            assertThatThrownBy(() -> Matrix.of(INT_ARRAY).getVector(2, 0)).isInstanceOf(IllegalArgumentException.class);
//        }
//
//        @DisplayName("Given index does not exist - throw illegal argument exception")
//        @Test
//        void givenIndexDoesNotExist_throwIllegalArgumentException() {
//            assertThatThrownBy(() -> Matrix.of(INT_ARRAY).getVector(0, 3)).isInstanceOf(IllegalArgumentException.class);
//        }
//
//        @DisplayName("Given dimension exists - return vector")
//        @Test
//        void givenDimensionExists_returnVector() {
//            List<Integer> vector = Matrix.of(INT_ARRAY).getVector(1, 2);
//            assertThat(vector).containsExactly(5, 6);
//        }
//
//    }

//    @DisplayName("insertColumn(List<T> column, int x)")
//    @Nested
//    class InsertColumn {
//
//        @DisplayName("Should insert column")
//        @Test
//        void shouldInsertColumn() {
//            Matrix<Integer> matrix = Matrix.of(INT_ARRAY);
//            assertThat(matrix.width()).isEqualTo(2);
//
//            matrix.insertColumn(List.of(12, 34, 56), 1);
//
//            assertMatrix(matrix, "1 12 2 | 3 34 4 | 5 56 6");
//        }
//    }
//
//    @DisplayName("insertRow(List<T> row, int y")
//    @Nested
//    class InsertRow {
//
//        @DisplayName("Should insert row")
//        @Test
//        void shouldInsertRow() {
//            Matrix<Integer> matrix = Matrix.of(INT_ARRAY);
//            assertThat(matrix.height()).isEqualTo(3);
//
//            matrix.insertRow(List.of(13, 24), 1);
//
//            assertMatrix(matrix, "1 2 | 13 24 | 3 4 | 5 6");
//        }
//    }

//    @DisplayName("width()")
//    @Nested
//    class Width {
//
//        @DisplayName("Given empty map - should return 0")
//        @Test
//        void givenEmptyMap_shouldReturn0() {
//            Matrix<Integer> matrix = Matrix.empty();
//            assertThat(matrix.width()).isEqualTo(0);
//        }
//
//        @DisplayName("Given populated map - should return width")
//        @Test
//        void givenPopulatedMap_shouldReturnWidth() {
//            Matrix<String> matrix = Matrix.filled(" ", 9, 4);
//            assertThat(matrix.width()).isEqualTo(10);
//        }
//
//    }
//
//    @DisplayName("height()")
//    @Nested
//    class Height {
//
//        @DisplayName("Given an empty map - should return 0")
//        @Test
//        void givenEmptyMap_shouldReturn0() {
//            Matrix<Integer> matrix = Matrix.empty();
//            assertThat(matrix.height()).isEqualTo(0);
//        }
//
//        @DisplayName("Given populated map - should return height")
//        @Test
//        void givenPopulatedMap_shouldReturnWidth() {
//            Matrix<String> matrix = Matrix.filled("x", 9, 4);
//            assertThat(matrix.height()).isEqualTo(5);
//        }
//
//    }

    @DisplayName("get(int x, int y)")
    @Nested
    class Get {

        @DisplayName("Given mapping exists - should return value")
        @Test
        void givenMappingExists_shouldReturnValue() {
            Tensor<Integer> tensor = build(INT_ARRAY_2D);

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
            Tensor<Integer> tensor = build(INT_ARRAY_2D);
            assertThat(tensor.get(2, 0)).isNull();
        }

    }

//    @DisplayName("toNestedList()")
//    @Nested
//    class ToNestedList {
//
//        @DisplayName("Given empty matrix - should return empty list")
//        @Test
//        void givenEmptyMatrix_shouldReturnEmptyList() {
//            Matrix<Object> empty = Matrix.empty();
//            assertThat(empty.toNestedList()).isEmpty();
//        }
//
//        @DisplayName("Given populated matrix - should return nested list of values")
//        @Test
//        void givenPopulatedMatrix_shouldReturnNestedListOfValues() {
//            List<List<Integer>> nestedList = Matrix.of(INT_ARRAY).toNestedList();
//
//            assertThat(nestedList.get(0)).containsExactly(1, 2);
//            assertThat(nestedList.get(1)).containsExactly(3, 4);
//            assertThat(nestedList.get(2)).containsExactly(5, 6);
//        }
//
//    }

    @DisplayName("set(int x, int y, T value)")
    @Nested
    class Set {

        @DisplayName("Given order of coordinates less than order of tensor - should throw error")
        @Test
        void givenOrderOfCoordinatesLessThanOrderOfTensor_shouldThrowError() {
            assertThatThrownBy(() -> build(INT_ARRAY_3D).set(1, 1, 2)).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Given order of coordinates greater than order of tensor")
        @Test
        void givenOrderOfCoordinatesGreaterThanOrderOfTensor_shouldThrowError() {
            assertThatThrownBy(() -> build(INT_ARRAY_3D).set(1, 1, 2, 3, 4)).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Given unmapped coordinates - should set new value")
        @Test
        void givenUnmappedCoordinates_shouldSetNewValue() {
            Tensor<Integer> tensor = build(INT_ARRAY_2D);

            assertThat(tensor.get(2, 0)).isNull();

            tensor.set(100, 2, 0);

            assertThat(tensor.get(2, 0)).isEqualTo(100);
        }

        @DisplayName("Given mapped coordinates - should replace value")
        @Test
        void givenMappedCoordinates_shouldReplaceValue() {
            Tensor<Integer> tensor = build(INT_ARRAY_2D);

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
            assertThat(build(INT_ARRAY_2D).order()).isEqualTo(2);
        }

        @DisplayName("Given 3D tensor - should return 3")
        @Test
        void given3DTensor_shouldReturn3() {
            assertThat(build(INT_ARRAY_3D).order()).isEqualTo(3);
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
            assertThat(build(INT_ARRAY_2D).size(2)).isEqualTo(0);
        }

        @DisplayName("Given dimension equal to or less than tensor order - should return correct value")
        @Test
        void givenDimensionGreaterEqualOrLessThanToTensorOrder_shouldReturnCorrectValue() {
            assertThat(build(INT_ARRAY_2D).size(0)).isEqualTo(2);
            assertThat(build(INT_ARRAY_2D).size(1)).isEqualTo(3);
            assertThat(build(INT_ARRAY_3D).size(0)).isEqualTo(2);
            assertThat(build(INT_ARRAY_3D).size(1)).isEqualTo(3);
            assertThat(build(INT_ARRAY_3D).size(2)).isEqualTo(2);
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
            Tensor<Integer> transposed = Tensor.fill(1, 0, 0).transpose();
            assertThat(transposed).isEqualTo(Tensor.fill(1, 0, 0));
        }

        @DisplayName("Given multiple values - should return transposed tensor")
        @Test
        void givenMultipleValues_shouldReturnTransposedTensor() {
            Tensor<Integer> transposed = build(INT_ARRAY_2D).transpose();
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
            Tensor<Integer> tensor = build(INT_ARRAY_2D);
            Tensor<Integer> computed = tensor.compute(integer -> -integer);

            assertTensor(computed, "-1 -2 | -3 -4 | -5 -6");
        }

    }

    @DisplayName("computeWithKeys(Function<Map.Entry<Key, T>, S> computeFunction)")
    @Nested
    class ComputeWithKeys {

        @DisplayName("Given empty - null op")
        @Test
        void givenEmpty_nullOp() {
            Tensor<Integer> empty = Tensor.empty();
            Tensor<String> computed = empty.computeWithKeys(entry -> "" + entry.getKey().coordinates().get(0) + entry.getKey().coordinates().get(1) + entry.getValue());

            assertThat(computed).isEqualTo(Tensor.empty());
        }

        @DisplayName("Given populated integer tensor - should apply compute function")
        @Test
        void givenPopulatedIntegerTensor_shouldApplyComputeFunction() {
            Tensor<Integer> tensor = build(INT_ARRAY_2D);
            Tensor<String> computed = tensor.computeWithKeys(entry -> "" + entry.getKey().coordinates().get(0) + entry.getKey().coordinates().get(1) + entry.getValue());

            String[][] negativeIntArray = {{"001", "102"}, {"013", "114"}, {"025", "126"}};
            assertThat(computed).isEqualTo(build(negativeIntArray));

            assertTensor(computed, "001 102 | 013 114 | 025 126");
        }

    }

    @DisplayName("computeAndUpdateKeys(Function<Map.Entry<Key, T>, Map.Entry<Key, S>> computeFunction)")
    @Nested
    class ComputeAndUpdateKeys {

        @DisplayName("Given empty tensor - null op")
        @Test
        void givenEmptyTensor_nullOp() {
            Tensor<Integer> empty = Tensor.empty();

            Function<Map.Entry<Key, Integer>, Map.Entry<Key, Integer>> computeFunction = entry ->
                    Map.entry(
                            Key.of(entry.getKey().coordinates().get(0) * 2, entry.getKey().coordinates().get(1) * 2),
                            entry.getValue() * 4
                    );
            Tensor<Integer> computed = empty.computeAndUpdateKeys(computeFunction);

            assertThat(computed).isEqualTo(Tensor.empty());
        }

        @DisplayName("Given populated tensor - should apply compute function")
        @Test
        void givenPopulatedTensor_shouldApplyComputeFunction() {
            Tensor<Integer> tensor = build(INT_ARRAY_2D);

            Function<Map.Entry<Key, Integer>, Map.Entry<Key, Integer>> computeFunction = entry ->
                    Map.entry(
                            Key.of(entry.getKey().coordinates().get(0) * 2, entry.getKey().coordinates().get(1) * 2),
                            entry.getValue() * 4
                    );

            Tensor<Integer> computed = tensor.computeAndUpdateKeys(computeFunction);
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
            assertThat(build(INT_ARRAY_2D).isEmpty()).isFalse();
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
            Tensor<Integer> tensor = build(INT_ARRAY_2D);
            assertThat(tensor.slice(Map.of())).isEqualTo(tensor);
        }

        @DisplayName("Given 1 constraint on 3D tensor - should return correct tensor")
        @Test
        void given1ConstraintOn3DTensor_shouldReturnCorrectMatrix() {
            Tensor<Integer> tensor = build(INT_ARRAY_3D);
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
            Tensor<Integer> tensor = build(INT_ARRAY_3D);
            Tensor<Integer> slice = tensor.slice(Map.of(0, 1L, 1, 0L));
            assertThat(slice.order()).isEqualTo(1);
            assertTensor(slice, "2 20");

            Tensor<Integer> slice2 = tensor.slice(Map.of(1, 1L, 2, 0L));
            assertThat(slice2.order()).isEqualTo(1);
            assertTensor(slice2, "3 4");
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
            Tensor<Object> tensor = build(INT_ARRAY_2D);
            assertThat(tensor.toString()).isEqualTo("1 2 | 3 4 | 5 6");
        }

        @DisplayName("Given 3D tensor - should return tensor as strings")
        @Test
        void given3DTensor_shouldReturnTensorAsStrings() {
            Tensor<Object> tensor = build(INT_ARRAY_3D);
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
}