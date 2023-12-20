package dev.christopping.tensor;

import static org.junit.jupiter.api.Assertions.*;

class MatrixTest {

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
//
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
//
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
//
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

}