package dev.christopping.tensor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;

import static dev.christopping.tensor.TensorAssertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class VectorTest {

    @Nested
    @DisplayName("of(List<T> list)")
    class OfList {

        @Test
        @DisplayName("Given empty list: should return empty vector")
        void givenEmptyList_ShouldReturnEmptyVector() {
            Vector<Object> result = Vector.of(List.of());
            assertThat(result.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("Given non-empty list: should return populated vector")
        void givenNon_emptyList_ShouldReturnPopulatedVector() {
            Vector<Integer> result = Vector.of(List.of(1, 2, 3, 10, 20, 30));
            assertVector(result, "[1,2,3,10,20,30]");
        }

    }

    @Nested
    @DisplayName("of(T[] array)")
    class OfArray {

        @Test
        @DisplayName("Given empty list: should return empty vector")
        void givenEmptyList_ShouldReturnEmptyVector() {
            Vector<Integer> result = Vector.of(new Integer[]{});
            assertThat(result.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("Given non-empty list: should return populated vector")
        void givenNon_emptyList_ShouldReturnPopulatedVector() {
            Vector<Integer> result = Vector.of(new Integer[]{1, 2, 3, 10, 20, 30});
            assertVector(result, "[1,2,3,10,20,30]");
        }

    }

    @Nested
    @DisplayName("fill(long size)")
    class FillLong {

        @Test
        @DisplayName("Given 0: should return single valued vector")
        void given0_ShouldReturnEmptyVector() {
            Vector<String> result = Vector.fill("A", 0L);
            assertVector(result, "A");
        }

        @Test
        @DisplayName("Given non-zero: should return filled vector")
        void givenNon_zero_ShouldReturnVector() {
            Vector<String> result = Vector.fill("A", 5L);
            assertVector(result, "[A,A,A,A,A]");
        }

    }

    @Nested
    @DisplayName("fill(int size)")
    class FillInteger {

        @Test
        @DisplayName("Given 0: should return single valued vector")
        void given0_ShouldReturnEmptyVector() {
            Vector<String> result = Vector.fill("A", 0);
            assertVector(result, "A");
        }

        @Test
        @DisplayName("Given non-zero: should return filled vector")
        void givenNon_zero_ShouldReturnVector() {
            Vector<String> result = Vector.fill("A", 5);
            assertVector(result, "[A,A,A,A,A]");
        }

    }

    @Nested
    @DisplayName("size()")
    class Size {

        @Test
        @DisplayName("Given empty vector: should return 0")
        void givenEmptyVector_ShouldReturn0() {
            long result = Vector.empty().toVector().size();
            assertThat(result).isEqualTo(0);
        }

        @Test
        @DisplayName("Given non-empty vector: should return size")
        void givenNon_emptyVector_ShouldReturnSize() {
            long result = Vector.of(1, 2, 3, 10, 20, 30).size();
            assertThat(result).isEqualTo(6);
        }

    }

    @Nested
    @DisplayName("backfill()")
    class Backfill {

        @Test
        @DisplayName("Given empty vector: should return empty vector")
        void givenEmptyVector_ShouldReturnEmptyVector() {
            Vector<Object> result = Vector.empty().toVector().backfill("A");
            assertThat(result.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("Given non-sparse vector: should return same vector")
        void givenNon_sparseVector_ShouldReturnSameVector() {
            Vector<Integer> result = Vector.of(1, 2, 3, 10, 20, 30).backfill(100);
            assertVector(result, "[1,2,3,10,20,30]");
        }

        @Test
        @DisplayName("Given sparse vector: should return backfilled vector")
        void givenSparseVector_ShouldReturnBackfilledVector() {
            Vector<Integer> vector = Vector.of(1, 2, 3, 10, 20, 30);
            vector.remove(Index.of(0));
            vector.remove(Index.of(2));
            vector.remove(Index.of(3));
            Vector<Integer> result = vector.backfill(100);
            assertVector(result, "[100,2,100,100,20,30]");
        }

    }

    @Nested
    @DisplayName("push()")
    class Push {

        @Test
        @DisplayName("Given empty vector: should push")
        void givenEmptyVector_ShouldPush() {
            Vector<Object> vector = Vector.empty().toVector();
            vector.push("A");
            assertVector(vector, "A");
        }

        @Test
        @DisplayName("Given non-sparse vector: should push")
        void givenNon_emptyVector_ShouldPush() {
            Vector<Integer> vector = Vector.of(1, 2, 3, 10, 20, 30);
            vector.push(100);
            assertVector(vector, "[1,2,3,10,20,30,100]");
        }

        @Test
        @DisplayName("Given sparse vector: should push")
        void givenSparseVector_ShouldPush() {
            Vector<Integer> vector = Vector.of(1, 2, 3, 10, 20, 30);
            vector.remove(Index.of(0));
            vector.remove(Index.of(2));
            vector.remove(Index.of(3));
            vector.push(100);
            assertVector(vector, "[ ,2, , ,20,30,100]");
        }
    }

    @Nested
    @DisplayName("pop()")
    class Pop {

        @Test
        @DisplayName("Given empty vector: should return null")
        void givenEmptyVector_ShouldReturnNull() {
            Vector<Object> vector = Vector.empty().toVector();
            Object result = vector.pop();
            assertThat(result).isNull();
            assertScalar(vector, "");
        }

        @Test
        @DisplayName("Given non-sparse vector: should pop end of vector")
        void givenNon_emptyVector_ShouldPush() {
            Vector<Integer> vector = Vector.of(1, 2, 3, 10, 20, 30);
            Integer result = vector.pop();
            assertThat(result).isEqualTo(30);
            assertVector(vector, "[1,2,3,10,20]");
        }

        @Test
        @DisplayName("Given sparse vector: should pop end of vector")
        void givenSparseVector_ShouldPopEndOfVector() {
            Vector<Integer> vector = Vector.of(1, 2, 3, 10, 20, 30);
            vector.remove(Index.of(0));
            vector.remove(Index.of(2));
            vector.remove(Index.of(4));
            Integer result = vector.pop();
            assertThat(result).isEqualTo(30);
            assertVector(vector, "[ ,2, ,10]");
        }

    }

    @Nested
    @DisplayName("shift()")
    class Shift {

        @Test
        @DisplayName("Given empty vector: should return null")
        void givenEmptyVector_ShouldReturnNull() {
            Vector<Object> vector = Vector.empty().toVector();
            Object result = vector.shift();
            assertThat(result).isNull();
            assertScalar(vector, "");
        }

        @Test
        @DisplayName("Given non-sparse vector: should pop end of vector")
        void givenNon_emptyVector_ShouldPush() {
            Vector<Integer> vector = Vector.of(1, 2, 3, 10, 20, 30);
            Integer result = vector.shift();
            assertThat(result).isEqualTo(1);
            assertVector(vector, "[ ,2,3,10,20,30]");
        }

        @Test
        @DisplayName("Given sparse vector: should pop end of vector")
        void givenSparseVector_ShouldPopEndOfVector() {
            Vector<Integer> vector = Vector.of(1, 2, 3, 10, 20, 30);
            vector.remove(Index.of(0));
            vector.remove(Index.of(2));
            vector.remove(Index.of(4));
            Integer result = vector.shift();
            assertThat(result).isEqualTo(2);
            assertVector(vector, "[ , , ,10, ,30]");
        }

    }

    @Nested
    @DisplayName("unshift()")
    class Unshift {

        @Test
        @DisplayName("Given empty vector: should push")
        void givenEmptyVector_ShouldPush() {
            Vector<Object> vector = Vector.empty().toVector();
            vector.unshift("A");
            assertVector(vector, "A");
        }

        @Test
        @DisplayName("Given non-sparse vector: should push")
        void givenNon_emptyVector_ShouldPush() {
            Vector<Integer> vector = Vector.of(1, 2, 3, 10, 20, 30);
            vector.unshift(100);
            assertVector(vector, "[100,1,2,3,10,20,30]");
        }

        @Test
        @DisplayName("Given sparse vector: should push")
        void givenSparseVector_ShouldPush() {
            Vector<Integer> vector = Vector.of(1, 2, 3, 10, 20, 30);
            vector.remove(Index.of(0));
            vector.remove(Index.of(2));
            vector.remove(Index.of(3));
            vector.unshift(100);
            assertVector(vector, "[100, ,2, , ,20,30]");
        }

    }

    @Nested
    @DisplayName("transpose()")
    class Transpose {

        @Test
        @DisplayName("Given empty vector: should return empty vector")
        void givenEmptyVector_ShouldReturnEmptyVector() {
            Tensor<Object> result = Vector.empty().transpose();
            assertScalar(result, "");
        }

        @Test
        @DisplayName("Given non-sparse vector: should return same non-sparse vector")
        void givenNon_sparseVector_ShouldReturnSameNon_sparseVector() {
            Vector<Integer> vector = Vector.of(1, 2, 3, 10, 20, 30);
            Vector<Integer> result = vector.transpose();
            assertVector(result, "[1,2,3,10,20,30]");
        }

        @Test
        @DisplayName("Given sparse vector: should return same sparse vector")
        void givenSparseVector_ShouldReturnSameSparseVector() {
            Vector<Integer> vector = Vector.of(1, 2, 3, 10, 20, 30);
            vector.remove(Index.of(0));
            vector.remove(Index.of(2));
            vector.remove(Index.of(3));
            Vector<Integer> result = vector.transpose();
            assertVector(result, "[ ,2, , ,20,30]");
        }

    }

    @Nested
    @DisplayName("compute()")
    class Compute {

        private final Function<Integer, Integer> computeFunction = i -> i + 1;

        @Test
        @DisplayName("Given empty vector: should return same vector")
        void givenEmptyVector_ShouldReturnSameVector() {
            Vector<Integer> result = Vector.empty()
                    .toVector()
                    .expect(Integer.class)
                    .compute(o -> o + 1);

            assertScalar(result, "");
        }

        @Test
        @DisplayName("Given non-sparse vector: should return mutated vector")
        void givenNon_sparseVector_ShouldReturnMutatedVector() {
            Vector<Integer> vector = Vector.of(1, 2, 3, 10, 20, 30);
            Vector<Integer> result = vector.compute(computeFunction);
            assertVector(result, "[2,3,4,11,21,31]");
        }

        @Test
        @DisplayName("Given sparse vector: should return mutated vector")
        void givenSparseVector_ShouldReturnMutatedVector() {
            Vector<Integer> vector = Vector.of(1, 2, 3, 10, 20, 30);
            vector.remove(Index.of(0));
            vector.remove(Index.of(2));
            vector.remove(Index.of(3));
            Vector<Integer> result = vector.compute(computeFunction);
            assertVector(result, "[ ,3, , ,21,31]");
        }

    }

    @Nested
    @DisplayName("computeWithIndices()")
    class ComputeWithIndices {

        private final Function<Map.Entry<Index, Integer>, Integer> computeFunction = o ->
                (int) o.getKey().get(0) + o.getValue();

        @Test
        @DisplayName("Given empty vector: should return same vector")
        void givenEmptyVector_ShouldReturnSameVector() {
            Vector<Integer> result = Vector.empty()
                    .toVector()
                    .expect(Integer.class)
                    .computeWithIndices(computeFunction)
                    .expect(Integer.class);

            assertScalar(result, "");
        }

        @Test
        @DisplayName("Given non-sparse vector: should return mutated vector")
        void givenNon_sparseVector_ShouldReturnMutatedVector() {
            Vector<Integer> vector = Vector.of(1, 2, 3, 10, 20, 30);
            Vector<Integer> result = vector
                    .computeWithIndices(computeFunction)
                    .expect(Integer.class);
            assertVector(result, "[1,3,5,13,24,35]");
        }

        @Test
        @DisplayName("Given sparse vector: should return mutated vector")
        void givenSparseVector_ShouldReturnMutatedVector() {
            Vector<Integer> vector = Vector.of(1, 2, 3, 10, 20, 30);
            vector.remove(Index.of(0));
            vector.remove(Index.of(2));
            vector.remove(Index.of(3));
            Vector<Integer> result = vector
                    .computeWithIndices(computeFunction)
                    .expect(Integer.class);
            assertVector(result, "[ ,3, , ,24,35]");
        }

    }

    @Nested
    @DisplayName("computeAndUpdateIndices()")
    class ComputeAndUpdateIndices {

        private final Function<Map.Entry<Index, Integer>, Map.Entry<Index, Integer>> computeFunction = o -> Map.entry(
                o.getKey().compute(i -> i * 2),
                (int) o.getKey().get(0) + o.getValue()
        );

        @Test
        @DisplayName("Given empty vector: should return same vector")
        void givenEmptyVector_ShouldReturnSameVector() {
            Tensor<Integer> result = Vector.empty()
                    .toVector()
                    .expect(Integer.class)
                    .computeAndUpdateIndices(computeFunction)
                    .expect(Integer.class);

            assertScalar(result, "");
        }

        @Test
        @DisplayName("Given non-sparse vector: should return mutated vector")
        void givenNon_sparseVector_ShouldReturnMutatedVector() {
            Vector<Integer> vector = Vector.of(1, 2, 3, 10, 20, 30);
            Tensor<Integer> result = vector
                    .computeAndUpdateIndices(computeFunction)
                    .expect(Integer.class);
            assertVector(result, "[1, ,3, ,5, ,13, ,24, ,35]");
        }

        @Test
        @DisplayName("Given sparse vector: should return mutated vector")
        void givenSparseVector_ShouldReturnMutatedVector() {
            Vector<Integer> vector = Vector.of(1, 2, 3, 10, 20, 30);
            vector.remove(Index.of(0));
            vector.remove(Index.of(2));
            vector.remove(Index.of(3));
            Tensor<Integer> result = vector
                    .computeAndUpdateIndices(computeFunction)
                    .expect(Integer.class);
            assertVector(result, "[ , ,3, , , , , ,24, ,35]");
        }

    }

    @Nested
    @DisplayName("reduce(T identity, BinaryOperator<T> accumulator, int dimension)")
    class ReduceWithAccumulator {

        private final BinaryOperator<Integer> accumulator = (first, second) -> first * second;

        @Test
        @DisplayName("Given empty vector: should return empty vector")
        void givenEmptyVector_ShouldReturnEmptyVector() {
            Vector<Integer> vector = Vector.empty()
                    .toVector()
                    .expect(Integer.class);
            assertThatThrownBy(() -> vector.reduce(1, accumulator, 0)).isInstanceOf(IndexOutOfBoundsException.class);
        }

        @Test
        @DisplayName("Given non-sparse vector: should return reduced scalar")
        void givenNon_sparseVector_ShouldReturnReducedScalar() {
            Vector<Integer> vector = Vector.of(1, 2, 3, 10, 20, 30);
            Scalar<Integer> result = vector.reduce(1, accumulator, 0);
            assertScalar(result, "36000");
        }

        @Test
        @DisplayName("Given sparse vector: should return reduced scalar")
        void givenSparseVector_ShouldReturnReducedScalar() {
            Vector<Integer> vector = Vector.of(1, 2, 3, 10, 20, 30);
            vector.remove(Index.of(0));
            vector.remove(Index.of(2));
            vector.remove(Index.of(3));
            Scalar<Integer> result = vector.reduce(1, accumulator, 0);
            assertScalar(result, "1200");
        }

    }

    @Nested
    @DisplayName("reduce(S identity, BiFunction<S, T, S> accumulator, BinaryOperator<S> combiner, int dimension)")
    class ReduceWithAccumulatorAndCombiner {

        private final BiFunction<String, Integer, String> accumulator = (s, i) -> s + "" + i * 10;
        private final BinaryOperator<String> combiner = (first, second) -> first + "B" + second;

        @Test
        @DisplayName("Given empty vector: should throw IndexOutOfBoundsException")
        void givenEmptyVector_ShouldReturnEmptyVector() {
            Vector<Integer> vector = Vector.empty()
                    .toVector()
                    .expect(Integer.class);
            assertThatThrownBy(() -> vector.reduce("A", accumulator, combiner, 0)).isInstanceOf(IndexOutOfBoundsException.class);
        }

        @Test
        @DisplayName("Given non-sparse vector: should return reduced scalar")
        void givenNon_sparseVector_ShouldReturnReducedScalar() {
            Vector<Integer> vector = Vector.of(1, 2, 3, 10, 20, 30);
            Scalar<String> result = vector.reduce("A", accumulator, combiner, 0);
            assertScalar(result, "A102030100200300");
        }

        @Test
        @DisplayName("Given sparse vector: should return reduced scalar")
        void givenSparseVector_ShouldReturnReducedScalar() {
            Vector<Integer> vector = Vector.of(1, 2, 3, 10, 20, 30);
            vector.remove(Index.of(0));
            vector.remove(Index.of(2));
            vector.remove(Index.of(3));
            Scalar<String> result = vector.reduce("A", accumulator, combiner, 0);
            assertScalar(result, "A20200300");
        }

    }

    @Nested
    @DisplayName("mask()")
    class Mask {

        @Test
        @DisplayName("Given empty vector: should return empty vector")
        void givenEmptyVector_ShouldReturnEmptyVector() {
            Vector<Object> vector = Vector.empty().toVector();
            Vector<Object> result = vector.mask(Vector.empty(), 0);
            assertScalar(result, "");
        }

        @Test
        @DisplayName("Given non-sparse vector: should return masked vector")
        void givenNon_sparseVector_ShouldReturnMaskedVector() {
            Vector<Integer> vector = Vector.of(1, 2, 3, 10, 20, 30);
            Vector<Integer> result = vector.mask(Vector.of(false, true, true, false, true, false), 100);
            assertVector(result, "[100,2,3,100,20,100]");
        }

        @Test
        @DisplayName("Given sparse vector: should return masked vector")
        void givenSparseVector_ShouldReturnMaskedVector() {
            Vector<Integer> vector = Vector.of(1, 2, 3, 10, 20, 30);
            vector.remove(Index.of(0));
            vector.remove(Index.of(2));
            vector.remove(Index.of(3));
            Vector<Integer> result = vector.mask(Vector.of(false, true, true, false, true, false), 100);
            assertVector(result, "[ ,2, , ,20,100]");
        }

    }

    @Nested
    @DisplayName("piecewise()")
    class Piecewise {

        @DisplayName("Given null other - should throw error")
        @Test
        void givenNullOther_shouldThrowError() {
            assertThatThrownBy(() -> Vector.empty().piecewise((o, o2) -> o, null)).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Given other has smaller order - should throw error")
        @Test
        void givenOtherHasSmallerOrder_shouldThrowError() {
            Vector<Integer> first = Vector.of(1, 2, 3, 10, 20, 30);
            Tensor<Integer> second = Scalar.of(1);

            assertThatThrownBy(() -> first.piecewise((a, b) -> a * b, second)).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Given other has larger order - should throw error")
        @Test
        void givenOtherHasLargerOrder_shouldThrowError() {
            Vector<Integer> first = Vector.of(1, 2, 3, 10, 20, 30);
            Tensor<Integer> second = Tensor.of(new Integer[][]{{1,2},{3,4}}, Integer.class);
            assertThatThrownBy(() -> first.piecewise((a, b) -> a * b, second)).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Given orders match but sizes do not - should throw error")
        @Test
        void givenOrdersMatchButSizesDoNot_shouldThrowError() {
            Vector<Integer> first = Vector.of(1,2,3,4);
            Vector<Integer> second = Vector.of(1,2,3,4,5);
            assertThatThrownBy(() -> first.piecewise((a, b) -> a * b, second)).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Given orders and sizes match - should perform piecewise operation")
        @Test
        void givenOrdersAndSizesMatch_shouldPerformPiecewiseOperation() {
            Vector<Integer> first = Vector.of(1,2,3,4);
            Vector<Integer> second = Vector.of(10,20,30,40);
            assertVector(first.piecewise((a, b) -> a * b, second), "[10,40,90,160]");
        }

    }

    @Nested
    @DisplayName("slice()")
    class Slice {

        @Test
        @DisplayName("Given empty vector: should return empty vector")
        void givenEmptyVector_ShouldReturnEmptyVector() {
            Scalar<Object> result = Vector.empty().toVector().slice(Map.of(0, 0L));
            assertThat(result.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("Given non-sparse vector with out of bounds dimension: should return empty scalar")
        void givenNon_sparseVectorWithOutOfBoundsDimension_ShouldReturnEmptyVector() {
            Vector<Integer> vector = Vector.of(1, 2, 3, 10, 20, 30);
            Scalar<Integer> result = vector.slice(Map.of(1, 0L));
            assertThat(result.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("Given non-sparse vector with out of bounds coordinate: should return empty scalar")
        void givenNon_sparseVectorWithOutOfBoundsCoordinate_ShouldReturnEmptyScalar() {
            Vector<Integer> vector = Vector.of(1, 2, 3, 10, 20, 30);
            Scalar<Integer> result = vector.slice(Map.of(0, 6L));
            assertThat(result.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("Given non-sparse vector with in bound map: should return element at position")
        void givenNon_sparseVector_ShouldReturnElementAtPosition() {
            Vector<Integer> vector = Vector.of(1, 2, 3, 10, 20, 30);
            Scalar<Integer> result = vector.slice(Map.of(0, 5L));
            assertScalar(result, "30");
        }

        @Test
        @DisplayName("Given sparse vector with out of bounds dimension: should return empty scalar")
        void givenSparseVectorWithOutOfBoundsDimension_ShouldReturnEmptyScalar() {
            Vector<Integer> vector = Vector.of(1, 2, 3, 10, 20, 30);
            vector.remove(Index.of(0));
            vector.remove(Index.of(2));
            vector.remove(Index.of(3));
            Scalar<Integer> result = vector.slice(Map.of(1, 0L));
            assertThat(result.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("Given sparse vector with out of bounds coordinate: should return empty scalar")
        void givenSparseVectorWithOutOfBoundsCoordinate_ShouldReturnEmptyScalar() {
            Vector<Integer> vector = Vector.of(1, 2, 3, 10, 20, 30);
            vector.remove(Index.of(0));
            vector.remove(Index.of(2));
            vector.remove(Index.of(3));
            Scalar<Integer> result = vector.slice(Map.of(0, 6L));
            assertThat(result.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("Given sparse vector with in bound map but element not present: should return empty scalar")
        void givenSparseVectorWithInBoundMapButElementNotPresent_ShouldReturnEmptyScalar() {
            Vector<Integer> vector = Vector.of(1, 2, 3, 10, 20, 30);
            vector.remove(Index.of(0));
            vector.remove(Index.of(2));
            vector.remove(Index.of(3));
            Scalar<Integer> result = vector.slice(Map.of(0, 3L));
            assertScalar(result, "");
        }

        @Test
        @DisplayName("Given sparse vector with in bound map and element present: should return element at position")
        void givenSparseVectorWithInBoundMapAndElementPresent_ShouldReturnElementAtPosition() {
            Vector<Integer> vector = Vector.of(1, 2, 3, 10, 20, 30);
            vector.remove(Index.of(0));
            vector.remove(Index.of(2));
            vector.remove(Index.of(3));
            Scalar<Integer> result = vector.slice(Map.of(0, 1L));
            assertScalar(result, "2");
        }

    }

    @Nested
    @DisplayName("expect()")
    class Expect {

        @DisplayName("Given empty - should always succeed")
        @Test
        void givenEmpty_expectShouldAlwaysSucceed() {
            Vector<Object> vector = Vector.empty().toVector();
            vector.expect(Boolean.class);
            vector.expect(Integer.class);
        }

        @DisplayName("Given type is not assignable - should fail")
        @Test
        void givenTypeIsNotAssignable_shouldFail() {
            Vector<Integer> vector = Vector.of(1, 2, 3, 10, 20, 30);
            assertThatThrownBy(() -> vector.expect(String.class)).isInstanceOf(IllegalArgumentException.class);
            assertThatThrownBy(() -> vector.expect(Long.class)).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Given type is assignable - should succeed")
        @Test
        void givenTypeIsAssignable_shouldSucceed() {
            Vector<Integer> vector = Vector.of(1, 2, 3, 10, 20, 30);
            vector.expect(Integer.class);
            vector.expect(Object.class);
        }

    }

    @Nested
    @DisplayName("extrude()")
    class Extrude {

        @DisplayName("Given empty vector - should return empty tensor")
        @Test
        void givenEmptyVector_shouldReturnEmptyTensor() {
            Matrix<Object> vector = Vector.empty().toVector().extrude(2);
            assertTensor(vector, "");
        }

        @DisplayName("Given non-sparse vector - should return extruded matrix of given size")
        @Test
        void givenNonSparseVector_shouldReturnExtrudedMatrixOfGivenSize() {
            Vector<String> vector = Vector.of("abc", "def", "ghi");
            assertMatrix(vector.extrude(3), "[[abc,def,ghi][abc,def,ghi][abc,def,ghi]]");
        }

        @DisplayName("Given sparse vector - should return extruded matrix of given size")
        @Test
        void givenSparseVector_shouldReturnExtrudedMatrixOfGivenSize() {
            Vector<String> vector = Vector.of("abc", "def", "ghi");
            vector.remove(Index.of(1));
            assertMatrix(vector.extrude(3), "[[abc, ,ghi][abc, ,ghi][abc, ,ghi]]");
        }

    }

    @DisplayName("extract(Index min, Index max)")
    @Nested
    class Extract {

        @Test
        @DisplayName("Given empty vector - should return empty vector")
        void givenEmptyVector_shouldReturnEmptyVector() {
            Vector<Object> tensor = Vector.empty().toVector();
            Vector<Object> extracted = tensor.extract(Index.of(), Index.of());
            assertTensor(extracted, "");
        }

        @Test
        @DisplayName("Given min and max indices mismatch in dimensions - should throw exception")
        void givenMinAndMaxDimensionMismatch_shouldThrowException() {
            Vector<Integer> vector = Vector.of(1,2,3,4);
            assertThatThrownBy(() -> vector.extract(Index.of(0), Index.of(0, 1)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Min and max must have the same number of dimensions.");
        }

        @Test
        @DisplayName("Given min index exceeds max index - should throw exception")
        void givenMinExceedsMax_shouldThrowException() {
            Vector<Integer> vector = Vector.of(1,2,3,4);
            assertThatThrownBy(() -> vector.extract(Index.of(1), Index.of(0)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Min must be bounded by max");
        }

        @Test
        @DisplayName("Given min and max indices - should return sub-tensor within bounds")
        void givenValidIndices_shouldReturnSubVectorWithinBounds() {
            Vector<Integer> vector = Vector.of(1,2,3,4);
            Vector<Integer> extracted = vector.extract(Index.of(1), Index.of(2));

            assertThat(extracted.order()).isEqualTo(1);
            assertThat(extracted.dimensions()).containsExactly(2L);
            assertThat(extracted.get(0)).isEqualTo(2);
            assertThat(extracted.get(1)).isEqualTo(3);
        }

        @Test
        @DisplayName("Given min and max indices matching entire vector - should return identical vector")
        void givenIndicesMatchingEntireVector_shouldReturnIdenticalVector() {
            Vector<Integer> vector = Vector.of(1,2,3,4);
            Tensor<Integer> extracted = vector.extract(Index.of(0), Index.of(3));

            assertThat(extracted).isEqualTo(vector);
        }

        @Test
        @DisplayName("Given sparse vector - should correctly extract sub-tensor")
        void givenSparseVector_shouldCorrectlyExtractSubTensor() {
            Vector<Integer> vector = Vector.of(1,2,3,4,5);
            vector.remove(Index.of(2));

            Vector<Integer> extracted = vector.extract(Index.of(1), Index.of(3));

            assertThat(extracted.order()).isEqualTo(1);
            assertThat(extracted.dimensions()).containsExactly(3L);
            assertThat(extracted.get(0)).isEqualTo(2);
            assertThat(extracted.get(1)).isEqualTo(null);
            assertThat(extracted.get(2)).isEqualTo(4);
        }

        @Test
        @DisplayName("Given no overlap between min and max indices and vector - should return empty tensor")
        void givenNoOverlap_shouldReturnEmptyTensor() {
            Vector<Integer> vector = Vector.of(1,2,3,4,5);
            Vector<Integer> extracted = vector.extract(Index.of(6), Index.of(7));

            assertThat(extracted).isEqualTo(Tensor.empty());
        }

    }

}