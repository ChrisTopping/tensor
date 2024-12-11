package dev.christopping.tensor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.*;

class IndexTest {

    @DisplayName("of(List<Long> coordinates)")
    @Nested
    class OfLongList {

        @DisplayName("Given negative coordinate - should throw error")
        @Test
        void givenNegativeCoordinate_shouldThrowError() {
            assertThatThrownBy(() -> Index.of(List.of(-1L))).isInstanceOf(IndexOutOfBoundsException.class);
        }

        @DisplayName("Given 1 coordinate - should return index with 1 coordinate")
        @Test
        void given1Coordinate_shouldReturnIndexWith1Coordinate() {
            assertThat(Index.of(List.of(1L)).coordinates()).containsExactly(1L);
        }

        @DisplayName("Given 2 coordinates - should return index with 2 coordinates")
        @Test
        void given2Coordinates_shouldReturnIndexWith2Coordinates() {
            assertThat(Index.of(List.of(1L, 2L)).coordinates()).containsExactly(1L, 2L);
        }

        @DisplayName("Given 3 coordinates - should return index with 3 coordinates")
        @Test
        void given3Coordinates_shouldReturnIndexWith3Coordinates() {
            assertThat(Index.of(List.of(1L, 2L, 3L)).coordinates()).containsExactly(1L, 2L, 3L);
        }

    }

    @DisplayName("of(long... coordinates)")
    @Nested
    class ofLongVarargs {

        @DisplayName("Given negative coordinate - should throw error")
        @Test
        void givenNegativeCoordinate_shouldThrowError() {
            assertThatThrownBy(() -> Index.of(-1L)).isInstanceOf(IndexOutOfBoundsException.class);
        }

        @DisplayName("Given no coordinates - should return empty index")
        @Test
        void givenNoCoordinates_shouldReturnAnEmptyIndex() {
            assertThat(Index.of().coordinates()).isEmpty();
        }

        @DisplayName("Given 1 coordinate - should return index with 1 coordinate")
        @Test
        void given1Coordinate_shouldReturnIndexWith1Coordinate() {
            assertThat(Index.of(1L).coordinates()).containsExactly(1L);
        }

        @DisplayName("Given 2 coordinates - should return index with 2 coordinates")
        @Test
        void given2Coordinates_shouldReturnIndexWith2Coordinates() {
            assertThat(Index.of(1L, 2L).coordinates()).containsExactly(1L, 2L);
        }

        @DisplayName("Given 3 coordinates - should return index with 3 coordinates")
        @Test
        void given3Coordinates_shouldReturnIndexWith3Coordinates() {
            assertThat(Index.of(1L, 2L, 3L).coordinates()).containsExactly(1L, 2L, 3L);
        }

    }

    @DisplayName("of(int... coordinates)")
    @Nested
    class ofIntVarargs {

        @DisplayName("Given negative coordinate - should throw error")
        @Test
        void givenNegativeCoordinate_shouldThrowError() {
            assertThatThrownBy(() -> Index.of(-1)).isInstanceOf(IndexOutOfBoundsException.class);
        }

        @DisplayName("Given no coordinates - should return empty index")
        @Test
        void givenNoCoordinates_shouldReturnAnEmptyIndex() {
            assertThat(Index.of().coordinates()).isEmpty();
        }

        @DisplayName("Given 1 coordinate - should return index with 1 coordinate")
        @Test
        void given1Coordinate_shouldReturnIndexWith1Coordinate() {
            assertThat(Index.of(1).coordinates()).containsExactly(1L);
        }

        @DisplayName("Given 2 coordinates - should return index with 2 coordinates")
        @Test
        void given2Coordinates_shouldReturnIndexWith2Coordinates() {
            assertThat(Index.of(1, 2).coordinates()).containsExactly(1L, 2L);
        }

        @DisplayName("Given 3 coordinates - should return index with 3 coordinates")
        @Test
        void given3Coordinates_shouldReturnIndexWith3Coordinates() {
            assertThat(Index.of(1, 2, 3).coordinates()).containsExactly(1L, 2L, 3L);
        }

    }

    @DisplayName("indices(Index maxIndex)")
    @Nested
    class Indices {

        @DisplayName("Given index is null - should return empty list")
        @Test
        void givenIndexIsNull_shouldReturnEmptyList() {
            assertThat(Index.range(null)).isEmpty();
        }

        @DisplayName("Given index is empty - should return empty list")
        @Test
        void givenIndexIsEmpty_shouldReturnEmptyList() {
            assertThat(Index.range(Index.of())).isEmpty();
        }

        @DisplayName("Given index is not empty - should return list containing all lesser indices")
        @Test
        void givenIndexIsNotEmpty_shouldReturnListContainingAllLesserIndices() {
            assertThat(Index.range(Index.of(1, 1, 1))).containsExactly(
                    Index.of(0, 0, 0),
                    Index.of(1, 0, 0),
                    Index.of(0, 1, 0),
                    Index.of(1, 1, 0),
                    Index.of(0, 0, 1),
                    Index.of(1, 0, 1),
                    Index.of(0, 1, 1),
                    Index.of(1, 1, 1)
            );
        }

    }

    @DisplayName("order()")
    @Nested
    class Order {

        @DisplayName("Given empty index - should return 0")
        @Test
        void givenEmptyIndex_shouldReturn0() {
            assertThat(Index.of().order()).isEqualTo(0);
        }

        @DisplayName("Given non-empty index - should return 0")
        @ParameterizedTest
        @ValueSource(ints = {1, 2, 3, 4, 5, 10, 100})
        void givenNonEmptyIndex_shouldReturn0(int size) {
            List<Long> coordinates = LongStream.range(0, size).boxed().collect(Collectors.toList());
            assertThat(Index.of(coordinates).order()).isEqualTo(size);
        }

    }

    @DisplayName("get(int dimension)")
    @Nested
    class Get {

        @DisplayName("Given empty array - should throw error")
        @ParameterizedTest
        @ValueSource(ints = {0, 1})
        void givenEmptyArray_shouldThrowError(int dimension) {
            assertThatThrownBy(() -> Index.of().get(dimension)).isInstanceOf(IndexOutOfBoundsException.class);
        }

        @DisplayName("Given non-empty array and dimension equal to array size - should throw error")
        @Test
        void givenNonEmptyArrayAndDimensionEqualToArraySize_shouldThrowError() {
            List<Long> coordinates = LongStream.range(0, 5).boxed().collect(Collectors.toList());
            Index index = Index.of(coordinates);
            assertThatThrownBy(() -> index.get(5)).isInstanceOf(IndexOutOfBoundsException.class);
        }

        @DisplayName("Given non-empty array and dimension greater than array size - should throw error")
        @Test
        void givenNonEmptyArrayAndDimensionGreaterThanArraySize_shouldThrowError() {
            List<Long> coordinates = LongStream.range(0, 5).boxed().collect(Collectors.toList());
            Index index = Index.of(coordinates);
            assertThatThrownBy(() -> index.get(6)).isInstanceOf(IndexOutOfBoundsException.class);
        }

        @DisplayName("Given non-empty array and dimension less than array size - should return coordinate")
        @Test
        void givenNonEmptyArrayAndDimensionLessThanArraySize_shouldReturnCoordinate() {
            List<Long> coordinates = LongStream.range(0, 5).boxed().collect(Collectors.toList());
            assertThat(coordinates.get(4)).isEqualTo(4L);
        }

    }

    @DisplayName("hasCoordinate(int dimension, long coordinate")
    @Nested
    class hasCoordinate {

        @DisplayName("Given index is empty - should return false")
        @Test
        void givenIndexIsEmpty_shouldReturnFalse() {
            assertThat(Index.of().hasCoordinate(0, 1)).isFalse();
        }

        @DisplayName("Given dimension is greater than size - should return false")
        @Test
        void givenDimensionIsGreaterThanSize_shouldReturnFalse() {
            assertThat(Index.of(0, 0).hasCoordinate(2, 0)).isFalse();
        }

        @DisplayName("Given index does not contain coordinate - should return false")
        @Test
        void givenIndexDoesNotContainCoordinate_shouldReturnFalse() {
            assertThat(Index.of(1, 2).hasCoordinate(1, 1)).isFalse();
        }

        @DisplayName("Given index contains coordinate - should return true")
        @Test
        void givenIndexContainsCoordinate_shouldReturnTrue() {
            assertThat(Index.of(1, 2).hasCoordinate(1, 2)).isTrue();
        }

    }

    @DisplayName("hasCoordinates(Map<Integer, Long> coordinates)")
    @Nested
    class HasCoordinates {

        @DisplayName("Given empty index and empty map - should return true")
        @Test
        void givenEmptyIndex_shouldReturnFalse() {
            assertThat(Index.of().hasCoordinates(Map.of())).isTrue();
        }

        @DisplayName("Given size is smaller than some of map index - should return false")
        @Test
        void givenSizeIsSmallerThanSomeOfMapIndices_shouldReturnFalse() {
            assertThat(Index.of(1, 2).hasCoordinates(Map.of(1, 2L, 2, 1L))).isFalse();
        }

        @DisplayName("Given index does not contain some values - should return false")
        @Test
        void givenIndexDoesNotContainSomeValues_shouldReturnFalse() {
            assertThat(Index.of(1, 2).hasCoordinates(Map.of(0, 2L, 1, 2L))).isFalse();
        }

        @DisplayName("Given index contains all values - should return true")
        @Test
        void givenIndexContainsAllValues_shouldReturnTrue() {
            assertThat(Index.of(1, 2).hasCoordinates(Map.of(0, 1L, 1, 2L))).isTrue();
        }

    }

    @DisplayName("euclideanDistance(Index other)")
    @Nested
    class EuclideanDistance {

        @DisplayName("Given null parameter index - should throw error")
        @Test
        void givenNullIndex_shouldThrowError() {
            assertThatThrownBy(() -> Index.of().euclideanDistance(null)).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Given parameter index is smaller size than primary index - should throw error")
        @Test
        void givenParameterIndexIsSmallerSizeThanPrimaryIndex_shouldThrowError() {
            assertThatThrownBy(() -> Index.of(1, 2).euclideanDistance(Index.of(1))).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Given parameter index is larger size than primary index - should throw error")
        @Test
        void givenParameterIndexIsLargerSizeThanPrimaryIndex_shouldThrowError() {
            assertThatThrownBy(() -> Index.of(1).euclideanDistance(Index.of(1, 2))).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Given both indices are equal - should return 0")
        @Test
        void givenBothIndicesAreEqual_shouldReturn0() {
            assertThat(Index.of(5, 2, 7, 1).euclideanDistance(Index.of(5, 2, 7, 1))).isEqualTo(0d);
        }

        @DisplayName("Given non-equal indices - should return correct distance")
        @Test
        void givenNonEqualIndices_shouldReturnCorrectDistance() {
            assertThat(Index.of(1, 1, 0).euclideanDistance(Index.of(2, 1, 2))).isEqualTo(2.23606797749979d);
        }

    }

    @DisplayName("orthogonalDistance(Index other)")
    @Nested
    class OrthogonalDistance {

        @DisplayName("Given null parameter index - should throw error")
        @Test
        void givenNullParameterIndex_shouldThrowError() {
            assertThatThrownBy(() -> Index.of(1, 2, 3).hammingDistance(null)).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Given parameter index is smaller size than primary index - should throw error")
        @Test
        void givenParameterIndexIsSmallerSizeThanPrimaryIndex_shouldThrowError() {
            assertThatThrownBy(() -> Index.of(1, 2).hammingDistance(Index.of(1))).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Given parameter index is larger size than primary index - should throw error")
        @Test
        void givenParameterIndexIsLargerSizeThanPrimaryIndex_shouldThrowError() {
            assertThatThrownBy(() -> Index.of(1).hammingDistance(Index.of(1, 2))).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Given both indices are equal - should return 0")
        @Test
        void givenBothIndicesAreEqual_shouldReturn0() {
            assertThat(Index.of(5, 2, 7, 1).hammingDistance(Index.of(5, 2, 7, 1))).isEqualTo(0);
        }

        @DisplayName("Given indices differ by 1 dimension - should return 1")
        @Test
        void givenIndicesDifferBy1Dimension_shouldReturn1() {
            assertThat(Index.of(5, 2, 7, 1).hammingDistance(Index.of(5, 20, 7, 1))).isEqualTo(1);
        }

        @DisplayName("Given indices differ by 2 dimensions - should return 2")
        @Test
        void givenIndicesDifferBy2Dimensions_shouldReturn2() {
            assertThat(Index.of(5, 2, 70, 1).hammingDistance(Index.of(5, 20, 7, 1))).isEqualTo(2);
        }

        @DisplayName("Given indices differ by 3 dimensions - should return 3")
        @Test
        void givenIndicesDifferBy3Dimensions_shouldReturn3() {
            assertThat(Index.of(5, 2, 70, 1).hammingDistance(Index.of(5, 20, 7, 10))).isEqualTo(3);
        }

    }

    @DisplayName("highestOrderDifference(Index other)")
    @Nested
    class HighestOrderDifference {

        @DisplayName("Given both indices are empty - should return 0")
        @Test
        void givenBothIndicesAreEmpty_shouldReturn0() {
            assertThat(Index.of().highestOrderDifference(Index.of())).isEqualTo(0);
        }

        @DisplayName("Given parameter index has larger size than primary index - should throw error")
        @Test
        void givenParameterIndexHasLargerSizeThanPrimaryIndex_shouldThrowError() {
            assertThatThrownBy(() -> Index.of(1, 2).highestOrderDifference(Index.of(1, 2, 3))).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Given parameter index has smaller size than primary index - should throw error")
        @Test
        void givenParameterIndexHasSmallerSizeThanPrimaryIndex_shouldThrowError() {
            assertThatThrownBy(() -> Index.of(1, 2, 3).highestOrderDifference(Index.of(1, 2))).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Given both indices are equal - should return 0")
        @Test
        void givenBothIndicesAreEqual_shouldReturn0() {
            assertThat(Index.of(1, 2, 3).highestOrderDifference(Index.of(1, 2, 3))).isEqualTo(0);
        }

        @DisplayName("Given only first coordinate is different - should return 1")
        @Test
        void givenOnlyFirstCoordinateIsDifferent_shouldReturn1() {
            assertThat(Index.of(1, 2, 3).highestOrderDifference(Index.of(10, 2, 3))).isEqualTo(1);
            assertThat(Index.of(10, 2, 3).highestOrderDifference(Index.of(1, 2, 3))).isEqualTo(1);
        }

        @DisplayName("Given only second coordinate is different - should return 2")
        @Test
        void givenOnlySecondCoordinateIsDifferent_shouldReturn2() {
            assertThat(Index.of(1, 2, 3).highestOrderDifference(Index.of(1, 20, 3))).isEqualTo(2);
            assertThat(Index.of(1, 20, 3).highestOrderDifference(Index.of(1, 2, 3))).isEqualTo(2);
        }

        @DisplayName("Given only third coordinate is different - should return 3")
        @Test
        void givenOnlyThirdCoordinateIsDifferent_shouldReturn3() {
            assertThat(Index.of(1, 2, 3).highestOrderDifference(Index.of(1, 2, 30))).isEqualTo(3);
            assertThat(Index.of(1, 2, 30).highestOrderDifference(Index.of(1, 2, 3))).isEqualTo(3);
        }

        @DisplayName("Given first and second coordinates are different - should return 2")
        @Test
        void givenFirstAndSecondCoordinatesAreDifferent_shouldReturn2() {
            assertThat(Index.of(1, 2, 3).highestOrderDifference(Index.of(10, 20, 3))).isEqualTo(2);
            assertThat(Index.of(10, 20, 3).highestOrderDifference(Index.of(1, 2, 3))).isEqualTo(2);
        }

        @DisplayName("Given first and third coordinates are different - should return 3")
        @Test
        void givenFirstAndThirdCoordinatesAreDifferent_shouldReturn3() {
            assertThat(Index.of(1, 2, 3).highestOrderDifference(Index.of(10, 2, 30))).isEqualTo(3);
            assertThat(Index.of(10, 2, 30).highestOrderDifference(Index.of(1, 2, 3))).isEqualTo(3);
        }

    }

    @DisplayName("isEmpty()")
    @Nested
    class IsEmpty {

        @DisplayName("Given index is empty - should return true")
        @Test
        void givenIndexIsEmpty_shouldReturnTrue() {
            assertThat(Index.of().isEmpty()).isTrue();
        }

        @DisplayName("Given index is not empty - should return false")
        @Test
        void givenIndexIsNotEmpty_shouldReturnFalse() {
            assertThat(Index.of(0).isEmpty()).isFalse();
            assertThat(Index.of(0, 0).isEmpty()).isFalse();
            assertThat(Index.of(0, 0, 0).isEmpty()).isFalse();
        }

    }

    @DisplayName("isSimilar(Index other)")
    @Nested
    class IsSimilar {

        @DisplayName("Given null parameter index - should return false")
        @Test
        void givenNullParameterIndex_shouldReturnFalse() {
            assertThat(Index.of(1, 2, 3).isSimilar(null)).isFalse();
        }

        @DisplayName("Given indices are identical - should return true")
        @Test
        void givenIndicesAreIdentical_shouldReturnTrue() {
            assertThat(Index.of(1, 2, 3).isSimilar(Index.of(1, 2, 3))).isTrue();
        }

        @DisplayName("Given parameter index has greater size than primary index - should return false")
        @Test
        void givenParameterIndexHasGreaterSizeThanPrimaryIndex_shouldReturnFalse() {
            assertThat(Index.of(1, 2).isSimilar(Index.of(1, 2, 3))).isFalse();
        }

        @DisplayName("Given parameter index has smaller size than primary index - should return false")
        @Test
        void givenParameterIndexHasSmallerSizeThanPrimaryIndex_shouldReturnFalse() {
            assertThat(Index.of(1, 2, 3).isSimilar(Index.of(1, 2))).isFalse();
        }

        @DisplayName("Given both indices have same size - should return true")
        @Test
        void givenBothIndicesHaveSameSize_shouldReturnTrue() {
            assertThat(Index.of(1, 2, 3).isSimilar(Index.of(51, 37, 47))).isTrue();
        }

    }

    @DisplayName("transpose()")
    @Nested
    class Transpose {

        @DisplayName("Given empty index - should return empty index")
        @Test
        void givenEmptyIndex_shouldReturnEmptyIndex() {
            assertThat(Index.of().transpose()).isEqualTo(Index.of());
        }

        @DisplayName("Given single valued index - should return single valued index")
        @Test
        void givenSingleValuedIndex_shouldReturnSingleValuedIndex() {
            assertThat(Index.of(1).transpose()).isEqualTo(Index.of(1));
        }

        @DisplayName("Given multi-valued index - should return index with reversed coordinates")
        @Test
        void givenMultiValuedIndex_shouldReturnIndexWithReversedCoordinates() {
            assertThat(Index.of(1, 2, 4, 8, 16).transpose().coordinates()).containsExactly(16L, 8L, 4L, 2L, 1L);
        }

    }

    @DisplayName("reorder(List<Integer> order)")
    @Nested
    class Reorder {

        @DisplayName("Given empty index and empty list - should return empty index")
        @Test
        void givenEmptyIndexAndEmptyList_shouldReturnEmptyIndex() {
            assertThat(Index.of().reorder()).isEqualTo(Index.of());
        }

        @DisplayName("Given size greater than list size - should throw error")
        @Test
        void givenSizeGreaterThanListSize_shouldThrowError() {
            assertThatThrownBy(() -> Index.of(10, 20, 30).reorder(0, 1)).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Given size less than list size - should throw error")
        @Test
        void givenSizeLessThanListSize_shouldThrowError() {
            assertThatThrownBy(() -> Index.of(10, 20).reorder(0, 1, 2)).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Given list does not change order - should return clone of index")
        @Test
        void givenListDoesNotChangeOrder_shouldReturnCloneOfIndex() {
            assertThat(Index.of(10, 20, 30).reorder(0, 1, 2)).isEqualTo(Index.of(10, 20, 30));
        }

        @DisplayName("Given list changes order - should return index with new order")
        @Test
        void givenListChangesOrder_shouldReturnIndexWithNewOrder() {
            assertThat(Index.of(10, 20, 30).reorder(1, 2, 0)).isEqualTo(Index.of(20, 30, 10));
        }

    }

    @DisplayName("constrain(int... toRemove")
    @Nested
    class Constrain {

        @DisplayName("Given no constraints - should return unchanged copy of matrix")
        @Test
        void givenEmptyVarargs_shouldReturnUnchangedCopyOfIndex() {
            assertThat(Index.of(1, 2, 3, 4, 5, 6).constrain()).isEqualTo(Index.of(1, 2, 3, 4, 5, 6));
        }

        @DisplayName("Given 1 constraint - should remove coordinate for given constraint")
        @Test
        void given1Constraint_shouldRemoveCoordinateForGivenConstraint() {
            assertThat(Index.of(1, 2, 3, 4, 5, 6).constrain(3)).isEqualTo(Index.of(1, 2, 3, 5, 6));
        }

        @DisplayName("Given multiple constraints - should remove all coordinates for given constraints")
        @Test
        void givenMultipleConstraints_shouldRemoveAllCoordinatesForGivenConstraints() {
            assertThat(Index.of(1, 2, 3, 4, 5, 6).constrain(0, 2, 4)).isEqualTo(Index.of(2, 4, 6));
        }

    }

    @DisplayName("combine(Index other)")
    @Nested
    class Combine {

        @DisplayName("Given both indices are empty - should return empty index")
        @Test
        void givenBothIndicesAreEmpty_shouldReturnEmptyIndex() {
            assertThat(Index.of().concatenate(Index.of())).isEqualTo(Index.of());
        }

        @DisplayName("Given primary index is empty - should return clone of parameter index")
        @Test
        void givenPrimaryIndexIsEmpty_shouldReturnCloneOfParameterIndex() {
            assertThat(Index.of().concatenate(Index.of(1, 2, 3))).isEqualTo(Index.of(1, 2, 3));
        }

        @DisplayName("Given parameter index is empty - should return clone of primary index")
        @Test
        void givenParameterIndexIsEmpty_shouldReturnCloneOfPrimaryIndex() {
            assertThat(Index.of(1, 2, 3).concatenate(Index.of())).isEqualTo(Index.of(1, 2, 3));
        }

        @DisplayName("Given primary index and parameter index are not empty - should return Index with parameter coordinates appended to primary coordinates")
        @Test
        void givenPrimaryIndexAndParameterIndexAreNotEmpty_shouldReturnIndexWithParameterCoordinatesAppendedToPrimaryCoordinates() {
            assertThat(Index.of(1, 2, 3).concatenate(Index.of(4, 5, 6))).isEqualTo(Index.of(1, 2, 3, 4, 5, 6));
        }

    }

    @DisplayName("compute(Function<Long, Long> computeFunction)")
    @Nested
    class Compute {

        @DisplayName("Given index is empty - return empty index")
        @Test
        void givenIndexIsEmpty_returnEmptyIndex() {
            assertThat(Index.of().compute(i -> i).isEmpty()).isTrue();
        }

        @DisplayName("Given non-empty index - return computed index")
        @Test
        void givenNonEmptyIndex_returnComputedIndex() {
            Index computed = Index.of(1, 2, 3, 4).compute(i -> i * i);
            assertThat(computed.coordinates()).containsExactly(1L, 4L, 9L, 16L);
        }

    }

    @DisplayName("extrude(long coordinate)")
    @Nested
    class Extrude {

        @DisplayName("Given empty array - should return order 1 index")
        @Test
        void givenEmptyArray_shouldReturnOrder1Index() {
            assertThat(Index.of().extrude(5).coordinates()).containsExactly(5L);
        }

        @DisplayName("Given non-empty array - should append coordinate to end of list")
        @Test
        void givenNonEmptyArray_shouldAppendCoordinateToEndOfList() {
            assertThat(Index.of(4, 1, 7, 2).extrude(5).coordinates()).containsExactly(4L, 1L, 7L, 2L, 5L);
        }

    }

    @DisplayName("compareTo(Index other)")
    @Nested
    class CompareTo {

        @DisplayName("Given null - should throw exception")
        @Test
        void givenNull_shouldThrowException() {
            assertThatThrownBy(() -> Index.of().compareTo(null)).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Given size less than parameter size - should throw exception")
        @Test
        void givenSizeLessThanParameterSize_shouldThrowException() {
            assertThatThrownBy(() -> Index.of(1, 2).compareTo(Index.of(1))).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Given size greater than parameter size - should throw exception")
        @Test
        void givenSizeGreaterThanParameterSize_shouldThrowException() {
            assertThatThrownBy(() -> Index.of(1).compareTo(Index.of(1, 2))).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Given both indices are empty - should return 0")
        @Test
        void givenBothIndicesAreEmpty_shouldReturn0() {
            assertThat(Index.of().compareTo(Index.of())).isEqualTo(0);
        }

        @DisplayName("Given both are identical - should return 0")
        @Test
        void givenBothAreIdentical_shouldReturn0() {
            assertThat(Index.of(1, 5, 3).compareTo(Index.of(1, 5, 3))).isEqualTo(0);
        }

        @DisplayName("Given both are order 1 and parameter is greater - should return -1")
        @Test
        void givenBothAreOrder1AndParameterHasGreaterValue_shouldReturnMinus1() {
            assertThat(Index.of(1).compareTo(Index.of(2))).isEqualTo(-1);
        }

        @DisplayName("Given both are order 1 and parameter is smaller - should return 1")
        @Test
        void givenBothAreOrder1AndParameterIsSmaller_shouldReturn1() {
            assertThat(Index.of(2).compareTo(Index.of(1))).isEqualTo(1);
        }

        @DisplayName("Given parameter's last coordinate is greater - should return -1")
        @Test
        void givenParametersLastCoordinateIsGreater_shouldReturnMinus1() {
            assertThat(Index.of(5, 1).compareTo(Index.of(4, 2))).isEqualTo(-1);
        }

        @DisplayName("Given parameter's last coordinate is smaller - should return 1")
        @Test
        void givenParametersLastCoordinateIsSmaller_shouldReturn1() {
            assertThat(Index.of(4, 2).compareTo(Index.of(5, 1))).isEqualTo(1);
        }

        @DisplayName("Given parameter's last different coordinate is greater - should return -1")
        @Test
        void givenParametersLastDifferentCoordinateIsGreater_shouldReturnMinus1() {
            assertThat(Index.of(5, 1, 8).compareTo(Index.of(4, 2, 8))).isEqualTo(-1);
        }

        @DisplayName("Given parameter's last different coordinate is smaller - should return 1")
        @Test
        void givenParametersLastDifferentCoordinateIsSmaller_shouldReturn1() {
            assertThat(Index.of(4, 2, 8).compareTo(Index.of(5, 1, 8))).isEqualTo(1);
        }

    }

    @DisplayName("equals(Object o)")
    @Nested
    class Equals {

        @DisplayName("Given null - should return false")
        @Test
        void givenNull_shouldReturnFalse() {
            assertThat(Index.of(1, 2, 3).equals(null)).isFalse();
        }

        @DisplayName("Given same index - should return true")
        @Test
        void givenSameIndex_shouldReturnTrue() {
            Index index = Index.of(1, 2, 3);
            assertThat(index.equals(index)).isTrue();
        }

        @DisplayName("Given index with different size - should return false")
        @Test
        void givenIndexWithDifferentSize_shouldReturnFalse() {
            assertThat(Index.of(1, 2, 3).equals(Index.of(1, 2, 3, 4))).isFalse();
        }

        @DisplayName("Given index with different coordinates - should return false")
        @Test
        void givenIndexWithDifferentCoordinates_shouldReturnFalse() {
            assertThat(Index.of(1, 2, 3).equals(Index.of(1, 20, 3))).isFalse();
        }

        @DisplayName("Given index with same coordinates - should return true")
        @Test
        void givenIndexWithSameCoordinates_shouldReturnTrue() {
            assertThat(Index.of(1, 2, 3).equals(Index.of(1, 2, 3))).isTrue();
        }

        @DisplayName("Given index with same coordinates from different factory - should return true")
        @Test
        void givenIndexWithSameCoordinatesFromDifferentFactory_shouldReturnTrue() {
            assertThat(Index.of(1, 2, 3).equals(Index.of(1L, 2L, 3L))).isTrue();
            assertThat(Index.of(1L, 2L, 3L).equals(Index.of(1, 2, 3))).isTrue();
        }

    }

    @DisplayName("hashCode()")
    @Nested
    class HashCode {

        @DisplayName("Given empty - should be repeatable")
        @Test
        void givenEmpty_shouldBeRepeatable() {
            assertThat(Index.of().hashCode()).isEqualTo(Index.of().hashCode());
        }

        @DisplayName("Given non-empty - should be repeatable")
        @Test
        void givenNonEmpty_shouldBeRepeatable() {
            assertThat(Index.of(5, 3, 7).hashCode()).isEqualTo(Index.of(5L, 3L, 7L).hashCode());
        }

    }

    @DisplayName("toString()")
    @Nested
    class ToString {

        @DisplayName("Given empty index - should return empty braces")
        @Test
        void givenEmptyIndex_shouldReturnEmptyBraces() {
            assertThat(Index.of()).hasToString("()");
        }

        @DisplayName("Given non-empty index - should return coordinates in braces in order")
        @Test
        void givenNonEmptyIndex_shouldReturnCoordinatesInBracesInOrder() {
            assertThat(Index.of(5, 1, 4, 2, 3)).hasToString("(5, 1, 4, 2, 3)");
        }

    }

    @Nested
    @DisplayName("scale()")
    class Scale {

        @Test
        @DisplayName("Given empty index: should return empty index")
        void givenEmptyIndex_ShouldReturnEmptyIndex() {
            Index index = Index.of();
            Index result = index.scale(1);
            assertThat(result.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("Given 0 scalar: should create origin of same order")
        void given0Scalar_ShouldCreateOriginOfSameOrder() {
            Index index = Index.of(3, 2, 1);
            Index result = index.scale(0);
            assertThat(result).isEqualTo(Index.of(0, 0, 0));
        }

        @Test
        @DisplayName("Given positive scalar: should return scaled index")
        void givenPositiveScalar_ShouldReturnScaledIndex() {
            Index index = Index.of(3, 2, 1);
            Index result = index.scale(10);
            assertThat(result).isEqualTo(Index.of(30, 20, 10));
        }

        @Test
        @DisplayName("Given negative scalar: should throw IndexOutOfBoundsException")
        void givenNegativeScalar_ShouldThrowIndexOutOfBoundsException() {
            Index index = Index.of(3, 2, 1);
            assertThatThrownBy(() -> index.scale(-10)).isInstanceOf(IndexOutOfBoundsException.class);
        }

    }

    @Nested
    @DisplayName("shift()")
    class Shift {

        @Test
        @DisplayName("Given index is empty: should return empty index")
        void givenIndexIsEmpty_ShouldReturnEmptyIndex() {
            Index index = Index.of();
            Index result = index.shift(10);
            assertThat(result.isEmpty()).isTrue();
        }
        
        @Test
        @DisplayName("Given scalar is zero: should return same index")
        void givenScalarIsZero_ShouldReturnSameIndex() {
            Index index = Index.of(3,2,1);
            Index result = index.shift(0);
            assertThat(index).isEqualTo(result);
        }

        @Test
        @DisplayName("Given scalar is positive: should return the shifted index")
        void givenScalarIsPositive_ShouldReturnTheShiftedIndex() {
            Index index = Index.of(3,2,1);
            Index result = index.shift(10);
            assertThat(result).isEqualTo(Index.of(13,12,11));
        }

        @Test
        @DisplayName("Given scalar is negative but smaller than the smallest coordinate: should return the shifted index")
        void givenScalarIsNegativeButSmallerThanTheSmallestCoordinate_ShouldReturnTheShiftedIndex() {
            Index index = Index.of(13,12,11);
            Index result = index.shift(-10);
            assertThat(result).isEqualTo(Index.of(3,2,1));
        }

        @Test
        @DisplayName("Given scalar is negative but the same magnitude as the smallest coordinate: should return the shifted index")
        void givenScalarIsNegativeButTheSameMagnitudeAsTheSmallestCoordinate_ShouldReturnTheShiftedIndex() {
            Index index = Index.of(13,12,11);
            Index result = index.shift(-11);
            assertThat(result).isEqualTo(Index.of(2,1,0));
        }

        @Test
        @DisplayName("Given scalar is negative and larger than the smallest coordinate: should throw IndexOutOfBoundsException")
        void givenScalarIsNegativeAndLargerThanTheSmallestCoordinate_ShouldThrowIndexOutOfBoundsException() {
            Index index = Index.of(13,12,11);
            assertThatThrownBy(() -> index.shift(-12)).isInstanceOf(IndexOutOfBoundsException.class);
        }

    }

    @Nested
    @DisplayName("clamp()")
    class Clamp {

        @Test
        @DisplayName("Given index is empty: should return an empty index")
        void givenIndexIsEmpty_ShouldReturnAnEmptyIndex() {
            Index index = Index.of();
            Index result = index.clamp(0, 100);
            assertThat(result.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("Given min is greater than max: should throw IllegalArgumentException")
        void givenMinIsGreaterThanMax_ShouldThrowIllegalArgumentException() {
            Index index = Index.of();
            assertThatThrownBy(() -> index.clamp(100, 0)).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Given all coordinates below min: should clamp all coordinates to min")
        void givenAllCoordinatesBelowMin_ShouldClampAllCoordinatesToMin() {
            Index index = Index.of(3, 10, 104, 0);
            Index result = index.clamp(1000,2000);
            assertThat(result).isEqualTo(Index.of(1000,1000,1000,1000));
        }

        @Test
        @DisplayName("Given all coordinates above max: should clamp all coordinates to max")
        void givenAllCoordinatesAboveMax_ShouldClampAllCoordinatesToMax() {
            Index index = Index.of(1023, 102, 501);
            Index result = index.clamp(50,100);
            assertThat(result).isEqualTo(Index.of(100,100,100));
        }

        @Test
        @DisplayName("Given all coordinates within range: should return the same index")
        void givenAllCoordinatesWithinRange_ShouldReturnTheSameIndex() {
            Index index = Index.of(45,50,55);
            Index result = index.clamp(25,75);
            assertThat(result).isEqualTo(index);
        }

        @Test
        @DisplayName("Given some coordinates are outside range and others are within: should clamp all coordinates out of range")
        void givenSomeCoordinatesAreOutsideRangeAndOthersAreWithin_ShouldClampAllCoordinatesOutOfRange() {
            Index index = Index.of(10,20,45,50,55,80,90);
            Index result = index.clamp(25,75);
            assertThat(result).isEqualTo(Index.of(25,25,45,50,55,75,75));
        }

    }

    @Nested
    @DisplayName("modulo()")
    class Modulo {

        @Test
        @DisplayName("Given index is empty: should return empty index")
        void givenIndexIsEmpty_ShouldReturnEmptyIndex() {
            Index index = Index.of();
            Index result = index.modulo(3);
            assertThat(result.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("Given divisor is negative: should throw IllegalArgumentException")
        void givenDivisorIsNegative_ShouldThrowIllegalArgumentException() {
            Index index = Index.of(1);
            assertThatThrownBy(() -> index.modulo(-1)).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Given divisor is zero: should throw IllegalArgumentException")
        void givenDivisorIsZero_ShouldThrowIllegalArgumentException() {
            Index index = Index.of(1);
            assertThatThrownBy(() -> index.modulo(0)).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Given divisor is positive: should return index where every coordinate is modulo by the divisor")
        void givenDivisorIsPositive_ShouldReturnIndexWhereEveryCoordinateIsModuloByTheDivisor() {
            Index index = Index.of(3, 2, 10);
            Index result = index.modulo(3);
            assertThat(result).isEqualTo(Index.of(0,2,1));
        }

    }

    @Nested
    @DisplayName("isZeroTensor()")
    class IsZeroTensor {

        @Test
        @DisplayName("Given tensor is empty: should return true")
        void givenTensorIsEmpty_ShouldReturnTrue() {
            Index index = Index.of();
            assertThat(index.isZeroTensor()).isTrue();
        }

        @Test
        @DisplayName("Given tensor has only zeros: should return true")
        void givenTensorHasOnlyZeros_ShouldReturnTrue() {
            Index index = Index.of(0, 0, 0, 0, 0);
            assertThat(index.isZeroTensor()).isTrue();
        }

        @Test
        @DisplayName("Given tensor has some non-zero values: should return false ")
        void givenTensorHasSomeNon_zeroValues_ShouldReturnFalse() {
            Index index = Index.of(0, 0, 0, 1, 0);
            assertThat(index.isZeroTensor()).isFalse();
        }

    }

    @Nested
    @DisplayName("dotProduct()")
    class DotProduct {

        @Test
        @DisplayName("Given other index is null: should throw IllegalArgumentException")
        void givenOtherIndexIsNull_ShouldThrowIllegalArgumentException() {
            Index index = Index.of(1, 2, 3);
            assertThatThrownBy(() -> index.dotProduct(null)).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Given other index is of lower order: should throw IllegalArgumentException")
        void givenOtherIndexIsOfLowerOrder_ShouldThrowIllegalArgumentException() {
            Index index = Index.of(1, 2, 3);
            Index other = Index.of(10, 20);
            assertThatThrownBy(() -> index.dotProduct(other)).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Given other index is of higher order: should throw IllegalArgumentException")
        void givenOtherIndexIsOfHigherOrder_ShouldThrowIllegalArgumentException() {
            Index index = Index.of(1, 2, 3);
            Index other = Index.of(10, 20, 30, 40);
            assertThatThrownBy(() -> index.dotProduct(other)).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Given other index is of same order: should return the sum of the product of each coordinate pair")
        void givenOtherIndexIsOfSameOrder_ShouldReturnTheSumOfTheProductOfEachCoordinatePair() {
            Index index = Index.of(1, 2, 3);
            Index other = Index.of(10, 20, 30);
            long result = index.dotProduct(other);
            assertThat(result).isEqualTo(140);
        }

    }

    @Nested
    @DisplayName("minkowskiDistance(Index other, double power)")
    class MinkowskiDistance {

        @Test
        @DisplayName("Given null other index - should throw IllegalArgumentException")
        void givenNullOtherIndex_ShouldThrowIllegalArgumentException() {
            Index index = Index.of(1, 2, 3);
            assertThatThrownBy(() -> index.minkowskiDistance(null, 1))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Given other index of smaller order - should throw IllegalArgumentException")
        void givenOtherIndexOfSmallerOrder_ShouldThrowIllegalArgumentException() {
            Index index = Index.of(1, 2, 3);
            Index other = Index.of(1, 2);
            assertThatThrownBy(() -> index.minkowskiDistance(other, 1))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Given other index of larger order - should throw IllegalArgumentException")
        void givenOtherIndexOfLargerOrder_ShouldThrowIllegalArgumentException() {
            Index index = Index.of(1, 2);
            Index other = Index.of(1, 2, 3);
            assertThatThrownBy(() -> index.minkowskiDistance(other, 1))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Given non-positive power - should throw IllegalArgumentException")
        void givenNonPositivePower_ShouldThrowIllegalArgumentException() {
            Index index = Index.of(1, 2, 3);
            Index other = Index.of(2, 3, 4);
            assertThatThrownBy(() -> index.minkowskiDistance(other, 0))
                    .isInstanceOf(IllegalArgumentException.class);
            assertThatThrownBy(() -> index.minkowskiDistance(other, -1))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Given both indices are equal - should return 0 regardless of power")
        void givenBothIndicesAreEqual_ShouldReturn0RegardlessOfPower() {
            Index index = Index.of(3, 3, 3);
            Index other = Index.of(3, 3, 3);

            // Test a few different powers
            assertThat(index.minkowskiDistance(other, 1)).isEqualTo(0);
            assertThat(index.minkowskiDistance(other, 2)).isEqualTo(0);
            assertThat(index.minkowskiDistance(other, 3)).isEqualTo(0);
        }

        @Test
        @DisplayName("Given power = 1 (Manhattan distance) - should return correct result")
        void givenPowerIsOne_ShouldReturnManhattanDistance() {
            Index index = Index.of(1, 2, 3);
            Index other = Index.of(4, 6, 5);

            // Manhattan distance = |1-4| + |2-6| + |3-5| = 3 + 4 + 2 = 9
            assertThat(index.minkowskiDistance(other, 1)).isEqualTo(9.0);
        }

        @Test
        @DisplayName("Given power = 2 (Euclidean distance) - should return correct result")
        void givenPowerIsTwo_ShouldReturnEuclideanDistance() {
            Index index = Index.of(1, 1, 0);
            Index other = Index.of(2, 1, 2);

            // Euclidean distance already tested as 2.23606797749979 in previous tests
            assertThat(index.minkowskiDistance(other, 2)).isEqualTo(2.23606797749979d);
        }

        @Test
        @DisplayName("Given arbitrary power (e.g. 3) - should return correct Minkowski distance")
        void givenArbitraryPower_ShouldReturnCorrectMinkowskiDistance() {
            Index index = Index.of(2, 4, 6);
            Index other = Index.of(5, 1, 3);
            double power = 3.0;

            // Minkowski distance for power=3:
            // |2-5|^3 + |4-1|^3 + |6-3|^3 = (3^3) + (3^3) + (3^3) = 27 + 27 + 27 = 81
            // Minkowski distance = 81^(1/3) = 81^(1/3) = 4.32674871 (approx)
            double distance = index.minkowskiDistance(other, power);
            assertThat(distance).isCloseTo(4.32674871, within(1e-9));
        }

        @Test
        @DisplayName("Given large coordinates and non-integer power - should still compute correct Minkowski distance")
        void givenLargeCoordinatesAndNonIntegerPower_ShouldComputeCorrectly() {
            Index index = Index.of(100, 200, 300);
            Index other = Index.of(110, 190, 310);
            double power = 1.5;

            // Differences: |100-110|=10, |200-190|=10, |300-310|=10
            // Sum of differences^power: 10^1.5 + 10^1.5 + 10^1.5
            // 10^1.5 = 10^(3/2) = (10^3)^(1/2) = 1000^(1/2) = 31.6227766017 approx
            // sum = 31.6227766017 * 3 = 94.8683298051
            // Minkowski distance = (94.8683298051)^(1/1.5) = (94.8683298051)^(2/3)
            // Approximate: (94.8683)^(0.666...)
            // Let's do a rough calculation:
            // cube root of 94.8683 ≈ 4.57
            // (4.57^2) ≈ 20.8849
            // A more precise calculation would be good, but let's just check it's close:
            double distance = index.minkowskiDistance(other, power);
            // We won't match exactly but let's ensure it's reasonable.
            // Using an online calculator: (10^1.5)=31.6227766017
            // sum=31.6227766017*3=94.8683298051
            // now 94.8683298051^(2/3) =
            // Let's do exact: power reciprocal = 1/(1.5)=2/3.
            // Using a calculator: 94.8683298051^(1/3)=4.568063524
            // (4.568063524^2)=20.87301986 approx
            assertThat(distance).isCloseTo(20.800838230519037, within(1e-7));
        }

    }

    @Nested
    @DisplayName("manhattanDistance(Index other)")
    class ManhattanDistance {

        @Test
        @DisplayName("Given null other index - should throw IllegalArgumentException")
        void givenNullOtherIndex_ShouldThrowIllegalArgumentException() {
            Index index = Index.of(1, 2, 3);
            assertThatThrownBy(() -> index.manhattanDistance(null))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Given other index of smaller order - should throw IllegalArgumentException")
        void givenOtherIndexOfSmallerOrder_ShouldThrowIllegalArgumentException() {
            Index index = Index.of(1, 2, 3);
            Index other = Index.of(1, 2);
            assertThatThrownBy(() -> index.manhattanDistance(other))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Given other index of larger order - should throw IllegalArgumentException")
        void givenOtherIndexOfLargerOrder_ShouldThrowIllegalArgumentException() {
            Index index = Index.of(1, 2);
            Index other = Index.of(1, 2, 3);
            assertThatThrownBy(() -> index.manhattanDistance(other))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Given both indices are empty - should return 0")
        void givenBothIndicesAreEmpty_ShouldReturn0() {
            Index index = Index.of();
            Index other = Index.of();
            assertThat(index.manhattanDistance(other)).isEqualTo(0);
        }

        @Test
        @DisplayName("Given both indices are identical - should return 0")
        void givenBothIndicesAreIdentical_ShouldReturn0() {
            Index index = Index.of(3, 3, 3);
            Index other = Index.of(3, 3, 3);
            assertThat(index.manhattanDistance(other)).isEqualTo(0);
        }

        @Test
        @DisplayName("Given indices differ in some coordinates - should return correct Manhattan distance")
        void givenIndicesDiffer_ShouldReturnCorrectManhattanDistance() {
            Index index = Index.of(1, 2, 3);
            Index other = Index.of(4, 6, 5);

            // Manhattan distance = |1-4| + |2-6| + |3-5| = 3 + 4 + 2 = 9
            assertThat(index.manhattanDistance(other)).isEqualTo(9);
        }

        @Test
        @DisplayName("Given indices differ in all coordinates - should return correct Manhattan distance")
        void givenIndicesDifferInAllCoordinates_ShouldReturnCorrectManhattanDistance() {
            Index index = Index.of(10, 20, 30);
            Index other = Index.of(0, 0, 0);

            // Manhattan distance = |10-0| + |20-0| + |30-0| = 10 + 20 + 30 = 60
            assertThat(index.manhattanDistance(other)).isEqualTo(60);
        }

        @Test
        @DisplayName("Given indices with varying differences - should return sum of absolute differences")
        void givenIndicesWithVaryingDifferences_ShouldReturnSumOfAbsoluteDifferences() {
            Index index = Index.of(5, 10, 15, 20);
            Index other = Index.of(5, 20, 5, 25);

            // Differences:
            // |5-5|=0
            // |10-20|=10
            // |15-5|=10
            // |20-25|=5
            // sum = 0+10+10+5=25
            assertThat(index.manhattanDistance(other)).isEqualTo(25);
        }

    }

    @Nested
    @DisplayName("chebyshevDistance(Index other)")
    class ChebyshevDistance {

        @Test
        @DisplayName("Given null other index - should throw IllegalArgumentException")
        void givenNullOtherIndex_ShouldThrowIllegalArgumentException() {
            Index index = Index.of(1, 2, 3);
            assertThatThrownBy(() -> index.chebyshevDistance(null))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Given other index of smaller order - should throw IllegalArgumentException")
        void givenOtherIndexOfSmallerOrder_ShouldThrowIllegalArgumentException() {
            Index index = Index.of(1, 2, 3);
            Index other = Index.of(1, 2);
            assertThatThrownBy(() -> index.chebyshevDistance(other))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Given other index of larger order - should throw IllegalArgumentException")
        void givenOtherIndexOfLargerOrder_ShouldThrowIllegalArgumentException() {
            Index index = Index.of(1, 2);
            Index other = Index.of(1, 2, 3);
            assertThatThrownBy(() -> index.chebyshevDistance(other))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Given both indices are empty - should return 0")
        void givenBothIndicesAreEmpty_ShouldReturn0() {
            Index index = Index.of();
            Index other = Index.of();
            assertThat(index.chebyshevDistance(other)).isEqualTo(0);
        }

        @Test
        @DisplayName("Given both indices are identical - should return 0")
        void givenBothIndicesAreIdentical_ShouldReturn0() {
            Index index = Index.of(3, 3, 3);
            Index other = Index.of(3, 3, 3);
            assertThat(index.chebyshevDistance(other)).isEqualTo(0);
        }

        @Test
        @DisplayName("Given indices differ in one dimension only - should return that difference")
        void givenIndicesDifferInOneDimensionOnly_ShouldReturnThatDifference() {
            Index index = Index.of(10, 10, 10);
            Index other = Index.of(10, 10, 13);
            // max(|10-10|, |10-10|, |10-13|) = max(0, 0, 3) = 3
            assertThat(index.chebyshevDistance(other)).isEqualTo(3);
        }

        @Test
        @DisplayName("Given indices differ in multiple dimensions - should return the largest absolute difference")
        void givenIndicesDifferInMultipleDimensions_ShouldReturnLargestAbsoluteDifference() {
            Index index = Index.of(5, 10, 15, 20);
            Index other = Index.of(3, 20, 0, 30);

            // Differences:
            // |5-3|=2, |10-20|=10, |15-0|=15, |20-30|=10
            // max(2,10,15,10) = 15
            assertThat(index.chebyshevDistance(other)).isEqualTo(15);
        }

    }

    @Nested
    @DisplayName("expand(int newOrder, long defaultCoordinate)")
    class Expand {

        @Test
        @DisplayName("Given newOrder is less than current order - should throw IllegalArgumentException")
        void givenNewOrderLessThanCurrentOrder_ShouldThrowException() {
            Index index = Index.of(1, 2, 3);
            assertThatThrownBy(() -> index.expand(2, 99))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("New size must be greater than or equal to current size");
        }

        @Test
        @DisplayName("Given newOrder equals current order - should return a clone of the index")
        void givenNewOrderEqualsCurrentOrder_ShouldReturnCloneOfIndex() {
            Index index = Index.of(1, 2, 3);
            Index expanded = index.expand(3, 99);
            assertThat(expanded).isEqualTo(index);
            // Ensure it's not the same instance (if your Index.of creates a new object)
            assertThat(expanded).isNotSameAs(index);
        }

        @Test
        @DisplayName("Given newOrder is greater than current order - should return expanded index with default values")
        void givenNewOrderGreaterThanCurrentOrder_ShouldReturnExpandedIndexWithDefaults() {
            Index index = Index.of(1, 2, 3);
            Index expanded = index.expand(6, 99);

            // Original coordinates: (1, 2, 3)
            // Expanded by 3 more: (1, 2, 3, 99, 99, 99)
            assertThat(expanded.coordinates()).containsExactly(1L, 2L, 3L, 99L, 99L, 99L);
        }

        @Test
        @DisplayName("Given empty index and newOrder is 0 - should return empty index")
        void givenEmptyIndexAndNewOrderIs0_ShouldReturnEmptyIndex() {
            Index index = Index.of();
            Index expanded = index.expand(0, 5);
            assertThat(expanded.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("Given empty index and newOrder is greater than 0 - should return index filled with default values")
        void givenEmptyIndexAndNewOrderGreaterThan0_ShouldReturnIndexFilledWithDefaults() {
            Index index = Index.of();
            Index expanded = index.expand(5, 10);
            // Since original is empty, all coordinates should be 10
            assertThat(expanded.coordinates()).containsExactly(10L, 10L, 10L, 10L, 10L);
        }

        @Test
        @DisplayName("Given a large expansion - should return expanded index correctly")
        void givenLargeExpansion_ShouldReturnExpandedIndexCorrectly() {
            Index index = Index.of(1);
            Index expanded = index.expand(10, 0);

            // Expanded: (1,0,0,0,0,0,0,0,0,0)
            assertThat(expanded.coordinates()).containsExactly(1L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L);
        }

    }

}