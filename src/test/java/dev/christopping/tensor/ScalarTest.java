package dev.christopping.tensor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static dev.christopping.tensor.TensorAssertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ScalarTest {

    @Nested
    @DisplayName("of()")
    class Of {

        @Test
        @DisplayName("Given null - should return null valued scalar")
        void givenNullShouldReturnNullValuedScalar() {
            Scalar<Object> scalar = Scalar.of(null);
            assertThat(scalar.order()).isEqualTo(0);
            assertThat(scalar.dimensions()).isEmpty();
            assertThat(scalar.get()).isNull();
        }

        @Test
        @DisplayName("Given non-null value - should return scalar with 0 dimensional element of same value")
        void givenNon_nullValueShouldReturnScalarWith0DimensionalElementOfSameValue() {
            Scalar<Integer> scalar = Scalar.of(123);
            assertThat(scalar.order()).isEqualTo(0);
            assertThat(scalar.dimensions()).isEmpty();
            assertThat(scalar.get()).isEqualTo(123);
        }
    }

    @Nested
    @DisplayName("set()")
    class Set {

        @Test
        @DisplayName("Should be able to set a new value to the scalar")
        void shouldBeAbleToSetANewValueToTheScalar() {
            Scalar<Object> scalar = Scalar.of(123);
            scalar.set("456");
            assertThat(scalar.get()).isEqualTo("456");
        }

        @Test
        @DisplayName("Should throw exception if index is provided")
        void shouldThrowExceptionIfIndexIsProvided() {
            Scalar<Object> scalar = Scalar.of(123);
            assertThatThrownBy(() -> scalar.set("456", Index.of(0))).isInstanceOf(IllegalArgumentException.class);
            assertThatThrownBy(() -> scalar.set("456", 0)).isInstanceOf(IllegalArgumentException.class);
            assertThatThrownBy(() -> scalar.set("456", 0L)).isInstanceOf(IllegalArgumentException.class);
        }

    }

    @Nested
    @DisplayName("order()")
    class Order {

        @Test
        @DisplayName("Should return 0")
        void shouldReturn0() {
            Scalar<Object> scalar = Scalar.of(123);
            assertThat(scalar.order()).isEqualTo(0);
        }

    }

    @Nested
    @DisplayName("size()")
    class Size {

        @Test
        @DisplayName("Should return 0 for any dimension")
        void shouldReturn0ForAnyDimension() {
            Scalar<Object> scalar = Scalar.of(123);
            assertThat(scalar.size(0)).isEqualTo(0);
            assertThat(scalar.size(1)).isEqualTo(0);
            assertThat(scalar.size(2)).isEqualTo(0);
        }

    }

    @Nested
    @DisplayName("dimensions()")
    class Dimensions {

        @Test
        @DisplayName("Should return empty list")
        void shouldReturnEmptyList() {
            Scalar<Object> scalar = Scalar.of(123);
            assertThat(scalar.dimensions()).isEmpty();
        }

    }

    @Nested
    @DisplayName("transpose()")
    class Transpose {

        @Test
        @DisplayName("Should return same scalar")
        void shouldReturnSameScalar() {
            Scalar<Object> scalar = Scalar.of(123);
            assertThat(scalar).isEqualTo(scalar.transpose());
        }

    }

    @Nested
    @DisplayName("backfill()")
    class Backfill {

        @Test
        @DisplayName("Should not mutate scalar")
        void shouldNotMutateScalar() {
            Scalar<Integer> original = Scalar.of(123);
            Scalar<Integer> backfilled = original.backfill(456);

            assertThat(backfilled).isEqualTo(original);
        }

    }

    @Nested
    @DisplayName("indices()")
    class Indices {

        @Test
        @DisplayName("Should return empty list")
        void shouldReturnEmptyIndex() {
            Scalar<Integer> scalar = Scalar.of(123);
            assertThat(scalar.indices()).isEmpty();
        }

    }

    @Nested
    @DisplayName("elements()")
    class Elements {

        @Test
        @DisplayName("Should return scalar element value")
        void shouldReturnScalarElementValue() {
            Scalar<Integer> scalar = Scalar.of(123);
            assertThat(scalar.elements()).containsExactly(123);
        }

    }

    @Nested
    @DisplayName("compute()")
    class Compute {

        @Test
        @DisplayName("Should apply to scalar value")
        void shouldApplyToScalarValue() {
            Scalar<Integer> scalar = Scalar.of(123);
            Scalar<Integer> result = scalar.compute(integer -> integer * 10);
            assertThat(result.get()).isEqualTo(1230);
        }

    }

    @Nested
    @DisplayName("computeWithIndices()")
    class ComputeWithIndices {

        @Test
        @DisplayName("Should apply to scalar value")
        void shouldApplyToScalarValue() {
            Scalar<Integer> scalar = Scalar.of(123);
            Scalar<String> result = scalar.computeWithIndices(entry -> entry.getKey() + " " + entry.getValue());
            assertThat(result.get()).isEqualTo("() 123");
        }

    }

    @Nested
    @DisplayName("computeAndUpdateIndices()")
    class ComputeAndUpdateIndices {

        // TODO : rename

        @Test
        @DisplayName("Not sure")
        void notSure() {
            Scalar<Integer> scalar = Scalar.of(123);
            Tensor<Integer> result = scalar.computeAndUpdateIndices(entry -> Map.entry(Index.of(0, 0), entry.getValue() * 2));
            assertThat(result.get(0, 0)).isEqualTo(246);
            assertTensor(result, "246");
        }

    }

    @Nested
    @DisplayName("piecewise()")
    class Piecewise {

        @Test
        @DisplayName("Given the operand is empty - should throw exception")
        void givenTheOperandIsEmptyShouldThrowException() {
            Scalar<Integer> first = Scalar.of(1);
            Scalar<Integer> second = Scalar.of(2);
            Scalar<Integer> result = first.piecewise(Integer::sum, second);
            assertScalar(result, "3");
        }

    }

    @Nested
    @DisplayName("slice()")
    class Slice {

        @Test
        @DisplayName("Given empty map: should return same valued scalar")
        void givenEmptyMap_ShouldReturnSameValuedScalar() {
            Scalar<Integer> scalar = Scalar.of(1);
            Scalar<Integer> result = scalar.slice(Map.of());
            assertScalar(result, "1");
        }

        @Test
        @DisplayName("Given non-empty map: should return empty scalar")
        void givenNonEmptyMap_ShouldReturnEmptyScalar() {
            Scalar<Integer> scalar = Scalar.of(1);
            Scalar<Integer> result = scalar.slice(Map.of(0, 0L));
            assertScalar(result, "");
        }

    }

    @Nested
    @DisplayName("expect()")
    class Expect {

        @DisplayName("Given type is not assignable - should fail")
        @Test
        void givenTypeIsNotAssignable_shouldFail() {
            Scalar<Integer> scalar = Scalar.of(123);
            assertThatThrownBy(() -> scalar.expect(String.class)).isInstanceOf(IllegalArgumentException.class);
            assertThatThrownBy(() -> scalar.expect(Long.class)).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Given type is assignable - should succeed")
        @Test
        void givenTypeIsAssignable_shouldSucceed() {
            Scalar<Integer> scalar = Scalar.of(123);
            scalar.expect(Integer.class);
            scalar.expect(Object.class);
        }

    }

    @Nested
    @DisplayName("extrude()")
    class Extrude {

        @Test
        @DisplayName("Should extrude to vector")
        void shouldExtrudeToVector() {
            Scalar<Integer> scalar = Scalar.of(1);
            assertVector(scalar.extrude(3), "[1,1,1]");
        }

    }

    @Nested
    @DisplayName("extract()")
    class Extract {

        @Test
        @DisplayName("Should return new scalar with same value")
        void shouldReturnNewScalarWithSameValue() {
            Scalar<Integer> scalar = Scalar.of(1);
            assertScalar(scalar.extract(Index.of(), Index.of()), "1");
        }

    }

    @Nested
    @DisplayName("toVector()")
    class ToVector {

        @Test
        @DisplayName("Should return single-valued vector")
        void shouldReturnSingle_valuedVector() {
            Scalar<Integer> scalar = Scalar.of(1);
            assertVector(scalar.toVector(), "1");
        }

    }

    @Nested
    @DisplayName("toMatrix()")
    class ToMatrix {

        @Test
        @DisplayName("Should return single-valued matrix")
        void shouldReturnSingle_valuedVector() {
            Scalar<Integer> scalar = Scalar.of(1);
            assertThat(scalar.toMatrix().elements()).isEqualTo(scalar.elements());
        }

    }


}
