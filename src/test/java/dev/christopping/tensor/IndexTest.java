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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IndexTest {

    @DisplayName("of(List<Long> coordinates)")
    @Nested
    class OfLongList {

        @DisplayName("Given negative coordinate - should throw error")
        @Test
        void givenNegativeCoordinate_shouldThrowError() {
            assertThatThrownBy(() -> Index.of(List.of(-1L))).isInstanceOf(IllegalArgumentException.class);
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
            assertThatThrownBy(() -> Index.of(-1L)).isInstanceOf(IllegalArgumentException.class);
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
            assertThatThrownBy(() -> Index.of(-1)).isInstanceOf(IllegalArgumentException.class);
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
            assertThat(Index.indices(null)).isEmpty();
        }

        @DisplayName("Given index is empty - should return empty list")
        @Test
        void givenIndexIsEmpty_shouldReturnEmptyList() {
            assertThat(Index.indices(Index.of())).isEmpty();
        }

        @DisplayName("Given index is not empty - should return list containing all lesser indices")
        @Test
        void givenIndexIsNotEmpty_shouldReturnListContainingAllLesserIndices() {
            assertThat(Index.indices(Index.of(1, 1, 1))).containsExactly(
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
            assertThatThrownBy(() -> Index.of().get(dimension)).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Given non-empty array and dimension equal to array size - should throw error")
        @Test
        void givenNonEmptyArrayAndDimensionEqualToArraySize_shouldThrowError() {
            List<Long> coordinates = LongStream.range(0, 5).boxed().collect(Collectors.toList());
            Index index = Index.of(coordinates);
            assertThatThrownBy(() -> index.get(5)).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Given non-empty array and dimension greater than array size - should throw error")
        @Test
        void givenNonEmptyArrayAndDimensionGreaterThanArraySize_shouldThrowError() {
            List<Long> coordinates = LongStream.range(0, 5).boxed().collect(Collectors.toList());
            Index index = Index.of(coordinates);
            assertThatThrownBy(() -> index.get(6)).isInstanceOf(IllegalArgumentException.class);
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
            assertThatThrownBy(() -> Index.of(1, 2, 3).orthogonalDistance(null)).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Given parameter index is smaller size than primary index - should throw error")
        @Test
        void givenParameterIndexIsSmallerSizeThanPrimaryIndex_shouldThrowError() {
            assertThatThrownBy(() -> Index.of(1, 2).orthogonalDistance(Index.of(1))).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Given parameter index is larger size than primary index - should throw error")
        @Test
        void givenParameterIndexIsLargerSizeThanPrimaryIndex_shouldThrowError() {
            assertThatThrownBy(() -> Index.of(1).orthogonalDistance(Index.of(1, 2))).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Given both indices are equal - should return 0")
        @Test
        void givenBothIndicesAreEqual_shouldReturn0() {
            assertThat(Index.of(5, 2, 7, 1).orthogonalDistance(Index.of(5, 2, 7, 1))).isEqualTo(0);
        }

        @DisplayName("Given indices differ by 1 dimension - should return 1")
        @Test
        void givenIndicesDifferBy1Dimension_shouldReturn1() {
            assertThat(Index.of(5, 2, 7, 1).orthogonalDistance(Index.of(5, 20, 7, 1))).isEqualTo(1);
        }

        @DisplayName("Given indices differ by 2 dimensions - should return 2")
        @Test
        void givenIndicesDifferBy2Dimensions_shouldReturn2() {
            assertThat(Index.of(5, 2, 70, 1).orthogonalDistance(Index.of(5, 20, 7, 1))).isEqualTo(2);
        }

        @DisplayName("Given indices differ by 3 dimensions - should return 3")
        @Test
        void givenIndicesDifferBy3Dimensions_shouldReturn3() {
            assertThat(Index.of(5, 2, 70, 1).orthogonalDistance(Index.of(5, 20, 7, 10))).isEqualTo(3);
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
            assertThat(Index.of().reorder(List.of())).isEqualTo(Index.of());
        }

        @DisplayName("Given size greater than list size - should throw error")
        @Test
        void givenSizeGreaterThanListSize_shouldThrowError() {
            assertThatThrownBy(() -> Index.of(10, 20, 30).reorder(List.of(0, 1))).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Given size less than list size - should throw error")
        @Test
        void givenSizeLessThanListSize_shouldThrowError() {
            assertThatThrownBy(() -> Index.of(10, 20).reorder(List.of(0, 1, 2))).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Given list does not change order - should return clone of index")
        @Test
        void givenListDoesNotChangeOrder_shouldReturnCloneOfIndex() {
            assertThat(Index.of(10, 20, 30).reorder(List.of(0, 1, 2))).isEqualTo(Index.of(10, 20, 30));
        }

        @DisplayName("Given list changes order - should return index with new order")
        @Test
        void givenListChangesOrder_shouldReturnIndexWithNewOrder() {
            assertThat(Index.of(10, 20, 30).reorder(List.of(1, 2, 0))).isEqualTo(Index.of(20, 30, 10));
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
            assertThat(Index.of().combine(Index.of())).isEqualTo(Index.of());
        }

        @DisplayName("Given primary index is empty - should return clone of parameter index")
        @Test
        void givenPrimaryIndexIsEmpty_shouldReturnCloneOfParameterIndex() {
            assertThat(Index.of().combine(Index.of(1, 2, 3))).isEqualTo(Index.of(1, 2, 3));
        }

        @DisplayName("Given parameter index is empty - should return clone of primary index")
        @Test
        void givenParameterIndexIsEmpty_shouldReturnCloneOfPrimaryIndex() {
            assertThat(Index.of(1, 2, 3).combine(Index.of())).isEqualTo(Index.of(1, 2, 3));
        }

        @DisplayName("Given primary index and parameter index are not empty - should return Index with parameter coordinates appended to primary coordinates")
        @Test
        void givenPrimaryIndexAndParameterIndexAreNotEmpty_shouldReturnIndexWithParameterCoordinatesAppendedToPrimaryCoordinates() {
            assertThat(Index.of(1, 2, 3).combine(Index.of(4, 5, 6))).isEqualTo(Index.of(1, 2, 3, 4, 5, 6));
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
            assertThat(Index.of(5,3,7).hashCode()).isEqualTo(Index.of(5L,3L,7L).hashCode());
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
}