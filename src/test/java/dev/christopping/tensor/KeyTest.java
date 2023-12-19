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

class KeyTest {

    @DisplayName("of(List<Long> coordinates)")
    @Nested
    class OfLongList {

        @DisplayName("Given 1 coordinate - should return key with 1 coordinate")
        @Test
        void given1Coordinate_shouldReturnKeyWith1Coordinate() {
            assertThat(Key.of(List.of(1L)).coordinates()).containsExactly(1L);
        }

        @DisplayName("Given 2 coordinates - should return key with 2 coordinates")
        @Test
        void given2Coordinates_shouldReturnKeyWith2Coordinates() {
            assertThat(Key.of(List.of(1L, 2L)).coordinates()).containsExactly(1L, 2L);
        }

        @DisplayName("Given 3 coordinates - should return key with 3 coordinates")
        @Test
        void given3Coordinates_shouldReturnKeyWith3Coordinates() {
            assertThat(Key.of(List.of(1L, 2L, 3L)).coordinates()).containsExactly(1L, 2L, 3L);
        }

    }

    @DisplayName("of(long... coordinates)")
    @Nested
    class ofLongVarargs {

        @DisplayName("Given no coordinates - should return empty key")
        @Test
        void givenNoCoordinates_shouldReturnAnEmptyKey() {
            assertThat(Key.of().coordinates()).isEmpty();
        }

        @DisplayName("Given 1 coordinate - should return key with 1 coordinate")
        @Test
        void given1Coordinate_shouldReturnKeyWith1Coordinate() {
            assertThat(Key.of(1L).coordinates()).containsExactly(1L);
        }

        @DisplayName("Given 2 coordinates - should return key with 2 coordinates")
        @Test
        void given2Coordinates_shouldReturnKeyWith2Coordinates() {
            assertThat(Key.of(1L, 2L).coordinates()).containsExactly(1L, 2L);
        }

        @DisplayName("Given 3 coordinates - should return key with 3 coordinates")
        @Test
        void given3Coordinates_shouldReturnKeyWith3Coordinates() {
            assertThat(Key.of(1L, 2L, 3L).coordinates()).containsExactly(1L, 2L, 3L);
        }

    }

    @DisplayName("of(int... coordinates)")
    @Nested
    class ofIntVarargs {

        @DisplayName("Given no coordinates - should return empty key")
        @Test
        void givenNoCoordinates_shouldReturnAnEmptyKey() {
            assertThat(Key.of().coordinates()).isEmpty();
        }

        @DisplayName("Given 1 coordinate - should return key with 1 coordinate")
        @Test
        void given1Coordinate_shouldReturnKeyWith1Coordinate() {
            assertThat(Key.of(1).coordinates()).containsExactly(1L);
        }

        @DisplayName("Given 2 coordinates - should return key with 2 coordinates")
        @Test
        void given2Coordinates_shouldReturnKeyWith2Coordinates() {
            assertThat(Key.of(1, 2).coordinates()).containsExactly(1L, 2L);
        }

        @DisplayName("Given 3 coordinates - should return key with 3 coordinates")
        @Test
        void given3Coordinates_shouldReturnKeyWith3Coordinates() {
            assertThat(Key.of(1, 2, 3).coordinates()).containsExactly(1L, 2L, 3L);
        }

    }

    @DisplayName("dimensionality()")
    @Nested
    class Dimensionality {

        @DisplayName("Given empty key - should return 0")
        @Test
        void givenEmptyKey_shouldReturn0() {
            assertThat(Key.of().dimensionality()).isEqualTo(0);
        }

        @DisplayName("Given non-empty key - should return 0")
        @ParameterizedTest
        @ValueSource(ints = {1, 2, 3, 4, 5, 10, 100})
        void givenNonEmptyKey_shouldReturn0(int size) {
            List<Long> coordinates = LongStream.range(0, size).boxed().collect(Collectors.toList());
            assertThat(Key.of(coordinates).dimensionality()).isEqualTo(size);
        }

    }

    @DisplayName("get(int dimension)")
    @Nested
    class Get {

        @DisplayName("Given empty array - should throw error")
        @ParameterizedTest
        @ValueSource(ints = {0, 1})
        void givenEmptyArray_shouldThrowError(int dimension) {
            assertThatThrownBy(() -> Key.of().get(dimension)).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Given non-empty array and dimension equal to array size - should throw error")
        @Test
        void givenNonEmptyArrayAndDimensionEqualToArraySize_shouldThrowError() {
            List<Long> coordinates = LongStream.range(0, 5).boxed().collect(Collectors.toList());
            Key key = Key.of(coordinates);
            assertThatThrownBy(() -> key.get(5)).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Given non-empty array and dimension greater than array size - should throw error")
        @Test
        void givenNonEmptyArrayAndDimensionGreaterThanArraySize_shouldThrowError() {
            List<Long> coordinates = LongStream.range(0, 5).boxed().collect(Collectors.toList());
            Key key = Key.of(coordinates);
            assertThatThrownBy(() -> key.get(6)).isInstanceOf(IllegalArgumentException.class);
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

        @DisplayName("Given key is empty - should return false")
        @Test
        void givenKeyIsEmpty_shouldReturnFalse() {
            assertThat(Key.of().hasCoordinate(0, 1)).isFalse();
        }

        @DisplayName("Given dimension is greater than size - should return false")
        @Test
        void givenDimensionIsGreaterThanSize_shouldReturnFalse() {
            assertThat(Key.of(0, 0).hasCoordinate(2, 0)).isFalse();
        }

        @DisplayName("Given key does not contain coordinate - should return false")
        @Test
        void givenKeyDoesNotContainCoordinate_shouldReturnFalse() {
            assertThat(Key.of(1, 2).hasCoordinate(1, 1)).isFalse();
        }

        @DisplayName("Given key contains coordinate - should return true")
        @Test
        void givenKeyContainsCoordinate_shouldReturnTrue() {
            assertThat(Key.of(1, 2).hasCoordinate(1, 2)).isTrue();
        }

    }

    @DisplayName("hasCoordinates(Map<Integer, Long> coordinates)")
    @Nested
    class HasCoordinates {

        @DisplayName("Given empty key and empty map - should return true")
        @Test
        void givenEmptyKey_shouldReturnFalse() {
            assertThat(Key.of().hasCoordinates(Map.of())).isTrue();
        }

        @DisplayName("Given size is smaller than some of map keys - should return false")
        @Test
        void givenSizeIsSmallerThanSomeOfMapKeys_shouldReturnFalse() {
            assertThat(Key.of(1, 2).hasCoordinates(Map.of(1, 2L, 2, 1L))).isFalse();
        }

        @DisplayName("Given key does not contain some values - should return false")
        @Test
        void givenKeyDoesNotContainSomeValues_shouldReturnFalse() {
            assertThat(Key.of(1, 2).hasCoordinates(Map.of(0, 2L, 1, 2L))).isFalse();
        }

        @DisplayName("Given key contains all values - should return true")
        @Test
        void givenKeyContainsAllValues_shouldReturnTrue() {
            assertThat(Key.of(1, 2).hasCoordinates(Map.of(0, 1L, 1, 2L))).isTrue();
        }

    }

    @DisplayName("euclideanDistance(Key other)")
    @Nested
    class EuclideanDistance {

        @DisplayName("Given null parameter key - should throw error")
        @Test
        void givenNullKey_shouldThrowError() {
            assertThatThrownBy(() -> Key.of().euclideanDistance(null)).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Given parameter key is smaller size than primary key - should throw error")
        @Test
        void givenParameterKeyIsSmallerSizeThanPrimaryKey_shouldThrowError() {
            assertThatThrownBy(() -> Key.of(1,2).euclideanDistance(Key.of(1))).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Given parameter key is larger size than primary key - should throw error")
        @Test
        void givenParameterKeyIsLargerSizeThanPrimaryKey_shouldThrowError() {
            assertThatThrownBy(() -> Key.of(1).euclideanDistance(Key.of(1,2))).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Given both keys are equal - should return 0")
        @Test
        void givenBothKeysAreEqual_shouldReturn0() {
            assertThat(Key.of(5,2,7,1).euclideanDistance(Key.of(5,2,7,1))).isEqualTo(0d);
        }

        @DisplayName("Given non-equal keys - should return correct distance")
        @Test
        void givenNonEqualKeys_shouldReturnCorrectDistance() {
            assertThat(Key.of(1,1,0).euclideanDistance(Key.of(2,1,2))).isEqualTo(2.23606797749979d);
        }

    }

    @DisplayName("orthogonalDistance(Key other)")
    @Nested
    class OrthogonalDistance {

        @DisplayName("Given null parameter key - should throw error")
        @Test
        void givenNullParameterKey_shouldThrowError() {
            assertThatThrownBy(() -> Key.of(1,2,3).orthogonalDistance(null)).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Given parameter key is smaller size than primary key - should throw error")
        @Test
        void givenParameterKeyIsSmallerSizeThanPrimaryKey_shouldThrowError() {
            assertThatThrownBy(() -> Key.of(1,2).orthogonalDistance(Key.of(1))).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Given parameter key is larger size than primary key - should throw error")
        @Test
        void givenParameterKeyIsLargerSizeThanPrimaryKey_shouldThrowError() {
            assertThatThrownBy(() -> Key.of(1).orthogonalDistance(Key.of(1,2))).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Given both keys are equal - should return 0")
        @Test
        void givenBothKeysAreEqual_shouldReturn0() {
            assertThat(Key.of(5,2,7,1).orthogonalDistance(Key.of(5,2,7,1))).isEqualTo(0);
        }

        @DisplayName("Given keys differ by 1 dimension - should return 1")
        @Test
        void givenKeysDifferBy1Dimension_shouldReturn1() {
            assertThat(Key.of(5,2,7,1).orthogonalDistance(Key.of(5,20,7,1))).isEqualTo(1);
        }

        @DisplayName("Given keys differ by 2 dimensions - should return 2")
        @Test
        void givenKeysDifferBy2Dimensions_shouldReturn2() {
            assertThat(Key.of(5,2,70,1).orthogonalDistance(Key.of(5,20,7,1))).isEqualTo(2);
        }

        @DisplayName("Given keys differ by 3 dimensions - should return 3")
        @Test
        void givenKeysDifferBy3Dimensions_shouldReturn3() {
            assertThat(Key.of(5,2,70,1).orthogonalDistance(Key.of(5,20,7,10))).isEqualTo(3);
        }

    }

    @DisplayName("highestOrderDifference(Key other)")
    @Nested
    class HighestOrderDifference {

        @DisplayName("Given both keys are empty - should return 0")
        @Test
        void givenBothKeysAreEmpty_shouldReturn0() {
            assertThat(Key.of().highestOrderDifference(Key.of())).isEqualTo(0);
        }

        @DisplayName("Given parameter key has larger size than primary key - should throw error")
        @Test
        void givenParameterKeyHasLargerSizeThanPrimaryKey_shouldThrowError() {
            assertThatThrownBy(() -> Key.of(1, 2).highestOrderDifference(Key.of(1, 2, 3))).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Given parameter key has smaller size than primary key - should throw error")
        @Test
        void givenParameterKeyHasSmallerSizeThanPrimaryKey_shouldThrowError() {
            assertThatThrownBy(() -> Key.of(1, 2, 3).highestOrderDifference(Key.of(1, 2))).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Given both keys are equal - should return 0")
        @Test
        void givenBothKeysAreEqual_shouldReturn0() {
            assertThat(Key.of(1, 2, 3).highestOrderDifference(Key.of(1, 2, 3))).isEqualTo(0);
        }

        @DisplayName("Given only first coordinate is different - should return 1")
        @Test
        void givenOnlyFirstCoordinateIsDifferent_shouldReturn1() {
            assertThat(Key.of(1, 2, 3).highestOrderDifference(Key.of(10, 2, 3))).isEqualTo(1);
            assertThat(Key.of(10, 2, 3).highestOrderDifference(Key.of(1, 2, 3))).isEqualTo(1);
        }

        @DisplayName("Given only second coordinate is different - should return 2")
        @Test
        void givenOnlySecondCoordinateIsDifferent_shouldReturn2() {
            assertThat(Key.of(1, 2, 3).highestOrderDifference(Key.of(1, 20, 3))).isEqualTo(2);
            assertThat(Key.of(1, 20, 3).highestOrderDifference(Key.of(1, 2, 3))).isEqualTo(2);
        }

        @DisplayName("Given only third coordinate is different - should return 3")
        @Test
        void givenOnlyThirdCoordinateIsDifferent_shouldReturn3() {
            assertThat(Key.of(1, 2, 3).highestOrderDifference(Key.of(1, 2, 30))).isEqualTo(3);
            assertThat(Key.of(1, 2, 30).highestOrderDifference(Key.of(1, 2, 3))).isEqualTo(3);
        }

        @DisplayName("Given first and second coordinates are different - should return 2")
        @Test
        void givenFirstAndSecondCoordinatesAreDifferent_shouldReturn2() {
            assertThat(Key.of(1, 2, 3).highestOrderDifference(Key.of(10, 20, 3))).isEqualTo(2);
            assertThat(Key.of(10, 20, 3).highestOrderDifference(Key.of(1, 2, 3))).isEqualTo(2);
        }

        @DisplayName("Given first and third coordinates are different - should return 3")
        @Test
        void givenFirstAndThirdCoordinatesAreDifferent_shouldReturn3() {
            assertThat(Key.of(1, 2, 3).highestOrderDifference(Key.of(10, 2, 30))).isEqualTo(3);
            assertThat(Key.of(10, 2, 30).highestOrderDifference(Key.of(1, 2, 3))).isEqualTo(3);
        }

    }

    @DisplayName("isSimilar(Key other)")
    @Nested
    class IsSimilar {

        @DisplayName("Given null parameter key - should return false")
        @Test
        void givenNullParameterKey_shouldReturnFalse() {
            assertThat(Key.of(1,2,3).isSimilar(null)).isFalse();
        }

        @DisplayName("Given keys are identical - should return true")
        @Test
        void givenKeysAreIdentical_shouldReturnTrue() {
            assertThat(Key.of(1,2,3).isSimilar(Key.of(1,2,3))).isTrue();
        }

        @DisplayName("Given parameter key has greater size than primary key - should return false")
        @Test
        void givenParameterKeyHasGreaterSizeThanPrimaryKey_shouldReturnFalse() {
            assertThat(Key.of(1,2).isSimilar(Key.of(1,2,3))).isFalse();
        }

        @DisplayName("Given parameter key has smaller size than primary key - should return false")
        @Test
        void givenParameterKeyHasSmallerSizeThanPrimaryKey_shouldReturnFalse() {
            assertThat(Key.of(1,2,3).isSimilar(Key.of(1,2))).isFalse();
        }

        @DisplayName("Given both keys have same size - should return true")
        @Test
        void givenBothKeysHaveSameSize_shouldReturnTrue() {
            assertThat(Key.of(1,2,3).isSimilar(Key.of(51,37,47))).isTrue();
        }

    }

    @DisplayName("transpose()")
    @Nested
    class Transpose {

        @DisplayName("Given empty key - should return empty key")
        @Test
        void givenEmptyKey_shouldReturnEmptyKey() {
            assertThat(Key.of().transpose()).isEqualTo(Key.of());
        }

        @DisplayName("Given single valued key - should return single valued key")
        @Test
        void givenSingleValuedKey_shouldReturnSingleValuedKey() {
            assertThat(Key.of(1).transpose()).isEqualTo(Key.of(1));
        }

        @DisplayName("Given multi-valued key - should return key with reversed coordinates")
        @Test
        void givenMultiValuedKey_shouldReturnKeyWithReversedCoordinates() {
            assertThat(Key.of(1, 2, 4, 8, 16).transpose().coordinates()).containsExactly(16L, 8L, 4L, 2L, 1L);
        }

    }

    @DisplayName("reorder(List<Integer> order)")
    @Nested
    class Reorder {

        @DisplayName("Given empty key and empty list - should return empty key")
        @Test
        void givenEmptyKeyAndEmptyList_shouldReturnEmptyKey() {
            assertThat(Key.of().reorder(List.of())).isEqualTo(Key.of());
        }

        @DisplayName("Given size greater than list size - should throw error")
        @Test
        void givenSizeGreaterThanListSize_shouldThrowError() {
            assertThatThrownBy(() -> Key.of(10, 20, 30).reorder(List.of(0, 1))).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Given size less than list size - should throw error")
        @Test
        void givenSizeLessThanListSize_shouldThrowError() {
            assertThatThrownBy(() -> Key.of(10, 20).reorder(List.of(0, 1, 2))).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Given list does not change order - should return clone of key")
        @Test
        void givenListDoesNotChangeOrder_shouldReturnCloneOfKey() {
            assertThat(Key.of(10, 20, 30).reorder(List.of(0, 1, 2))).isEqualTo(Key.of(10, 20, 30));
        }

        @DisplayName("Given list changes order - should return key with new order")
        @Test
        void givenListChangesOrder_shouldReturnKeyWithNewOrder() {
            assertThat(Key.of(10, 20, 30).reorder(List.of(1, 2, 0))).isEqualTo(Key.of(20, 30, 10));
        }

    }

    @DisplayName("constrain(int... toRemove")
    @Nested
    class Constrain {

        @DisplayName("Given no constraints - should return unchanged copy of matrix")
        @Test
        void givenEmptyVarargs_shouldReturnUnchangedCopyOfKey() {
            assertThat(Key.of(1,2,3,4,5,6).constrain()).isEqualTo(Key.of(1,2,3,4,5,6));
        }

        @DisplayName("Given 1 constraint - should remove coordinate for given constraint")
        @Test
        void given1Constraint_shouldRemoveCoordinateForGivenConstraint() {
            assertThat(Key.of(1,2,3,4,5,6).constrain(3)).isEqualTo(Key.of(1,2,3,5,6));
        }

        @DisplayName("Given multiple constraints - should remove all coordinates for given constraints")
        @Test
        void givenMultipleConstraints_shouldRemoveAllCoordinatesForGivenConstraints() {
            assertThat(Key.of(1,2,3,4,5,6).constrain(0,2,4)).isEqualTo(Key.of(2,4,6));
        }

    }

    @DisplayName("combine(Key other)")
    @Nested
    class Combine {

        @DisplayName("Given both keys are empty - should return empty key")
        @Test
        void givenBothKeysAreEmpty_shouldReturnEmptyKey() {
            assertThat(Key.of().combine(Key.of())).isEqualTo(Key.of());
        }

        @DisplayName("Given primary key is empty - should return clone of parameter key")
        @Test
        void givenPrimaryKeyIsEmpty_shouldReturnCloneOfParameterKey() {
            assertThat(Key.of().combine(Key.of(1, 2, 3))).isEqualTo(Key.of(1, 2, 3));
        }

        @DisplayName("Given parameter key is empty - should return clone of primary key")
        @Test
        void givenParameterKeyIsEmpty_shouldReturnCloneOfPrimaryKey() {
            assertThat(Key.of(1, 2, 3).combine(Key.of())).isEqualTo(Key.of(1, 2, 3));
        }

        @DisplayName("Given primary key and parameter key are not empty - should return Key with parameter coordinates appended to primary coordinates")
        @Test
        void givenPrimaryKeyAndParameterKeyAreNotEmpty_shouldReturnKeyWithParameterCoordinatesAppendedToPrimaryCoordinates() {
            assertThat(Key.of(1, 2, 3).combine(Key.of(4, 5, 6))).isEqualTo(Key.of(1, 2, 3, 4, 5, 6));
        }

    }

    @DisplayName("compareTo(Key other)")
    @Nested
    class CompareTo {

        @DisplayName("Given null - should throw exception")
        @Test
        void givenNull_shouldThrowException() {
            assertThatThrownBy(() -> Key.of().compareTo(null)).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Given size less than parameter size - should throw exception")
        @Test
        void givenSizeLessThanParameterSize_shouldThrowException() {
            assertThatThrownBy(() -> Key.of(1, 2).compareTo(Key.of(1))).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Given size greater than parameter size - should throw exception")
        @Test
        void givenSizeGreaterThanParameterSize_shouldThrowException() {
            assertThatThrownBy(() -> Key.of(1).compareTo(Key.of(1, 2))).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Given both keys are empty - should return 0")
        @Test
        void givenBothKeysAreEmpty_shouldReturn0() {
            assertThat(Key.of().compareTo(Key.of())).isEqualTo(0);
        }

    }

    @DisplayName("equals(Object o)")
    @Nested
    class Equals {

        @DisplayName("Given null - should return false")
        @Test
        void givenNull_shouldReturnFalse() {
            assertThat(Key.of(1, 2, 3).equals(null)).isFalse();
        }

        @DisplayName("Given same key - should return true")
        @Test
        void givenSameKey_shouldReturnTrue() {
            Key key = Key.of(1, 2, 3);
            assertThat(key.equals(key)).isTrue();
        }

        @DisplayName("Given key with different size - should return false")
        @Test
        void givenKeyWithDifferentSize_shouldReturnFalse() {
            assertThat(Key.of(1, 2, 3).equals(Key.of(1, 2, 3, 4))).isFalse();
        }

        @DisplayName("Given key with different coordinates - should return false")
        @Test
        void givenKeyWithDifferentCoordinates_shouldReturnFalse() {
            assertThat(Key.of(1, 2, 3).equals(Key.of(1, 20, 3))).isFalse();
        }

        @DisplayName("Given key with same coordinates - should return true")
        @Test
        void givenKeyWithSameCoordinates_shouldReturnTrue() {
            assertThat(Key.of(1, 2, 3).equals(Key.of(1, 2, 3))).isTrue();
        }

        @DisplayName("Given key with same coordinates from different factory - should return true")
        @Test
        void givenKeyWithSameCoordinatesFromDifferentFactory_shouldReturnTrue() {
            assertThat(Key.of(1, 2, 3).equals(Key.of(1L, 2L, 3L))).isTrue();
            assertThat(Key.of(1L, 2L, 3L).equals(Key.of(1, 2, 3))).isTrue();
        }

    }

    @DisplayName("toString()")
    @Nested
    class ToString {

        @DisplayName("Given empty key - should return empty braces")
        @Test
        void givenEmptyKey_shouldReturnEmptyBraces() {
            assertThat(Key.of()).hasToString("()");
        }

        @DisplayName("Given non-empty key - should return coordinates in braces in order")
        @Test
        void givenNonEmptyKey_shouldReturnCoordinatesInBracesInOrder() {
            assertThat(Key.of(5, 1, 4, 2, 3)).hasToString("(5, 1, 4, 2, 3)");
        }

    }
}