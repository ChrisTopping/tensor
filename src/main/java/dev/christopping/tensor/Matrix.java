package dev.christopping.tensor;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

// TODO: test
// TODO: add javadocs
public class Matrix<T> extends Tensor<T> {

    protected Matrix() {
        super();
    }

    protected Matrix(Map<Key, T> map) {
        super(map);
    }

    public static <T> Matrix<T> of(List<List<T>> nestedList) {
        Matrix<T> matrix = new Matrix<>();
        for (int y = 0; y < nestedList.size(); y++) {
            for (int x = 0; x < nestedList.get(0).size(); x++) {
                matrix.set(nestedList.get(y).get(x), x, y);
            }
        }
        return matrix;
    }

    public static <T> Matrix<T> of(T[][] nestedArray) {
        Matrix<T> matrix = new Matrix<>();
        for (int y = 0; y < nestedArray.length; y++) {
            for (int x = 0; x < nestedArray[0].length; x++) {
                matrix.set(nestedArray[y][x], x, y);
            }
        }
        return matrix;
    }

    public static <T> Matrix<T> fill(T value, long x, long y) {
        if (x < 0 || y < 0) throw new IllegalArgumentException("x and y must both be positive");
        Matrix<T> matrix = new Matrix<>();
        matrix.set(value, x, y);
        matrix.backfill(value);
        return matrix;
    }

    public static <T> Matrix<T> fill(T value, int x, int y) {
        return fill(value, x, (long) y);
    }

    public static <T> Matrix<T> empty() {
        return new Matrix<>();
    }

    @Override
    public void set(T element, long... coordinates) {
        if (coordinates.length != 2) throw new IllegalArgumentException("Element requires 2 coordinates");
        super.set(element, coordinates);
    }

    @Override
    public void set(T element, int... coordinates) {
        if (coordinates.length != 2) throw new IllegalArgumentException("Element requires 2 coordinates");
        super.set(element, coordinates);
    }

    public List<T> getVector(int dimension, long index) {
        if (dimension >= order() || isEmpty())
            throw new IllegalArgumentException("Dimension exceeds matrix dimension");
        if (index >= size(dimension))
            throw new IllegalArgumentException("Index exceeds matrix size in given dimension");

        return map.entrySet().stream()
                .filter(entry -> entry.getKey().coordinates().get(dimension).equals(index))
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public long width() {
        return map.keySet().stream()
                .mapToLong(key -> key.coordinates().get(0))
                .max()
                .orElse(-1) + 1;
    }

    public long height() {
        return map.keySet().stream()
                .mapToLong(key -> key.coordinates().get(1))
                .max()
                .orElse(-1) + 1;
    }

    public List<List<T>> toNestedList() {
        return LongStream.range(0, height())
                .mapToObj(y -> LongStream.range(0, width()).mapToObj(x -> get(x, y)).collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    public void insertColumn(List<T> column, long x) {
        Map<Key, T> shiftedMap = map.entrySet().stream()
                .map(entry -> {
                    List<Long> key = entry.getKey().coordinates();
                    long columnIndex = key.get(0);
                    if (columnIndex >= x) {
                        key.set(0, columnIndex + 1L);
                        return Map.entry(Key.of(key), entry.getValue());
                    } else {
                        return entry;
                    }
                }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        IntStream.range(0, column.size())
                .forEach(y -> shiftedMap.put(Key.of(x, y), column.get(y)));

        map.clear();
        map.putAll(shiftedMap);
    }

    public void appendColumn(List<T> column) {
        insertColumn(column, width());
    }

    public void insertRow(List<T> row, long y) {
        Map<Key, T> shiftedMap = map.entrySet().stream()
                .map(entry -> {
                    List<Long> key = entry.getKey().coordinates();
                    long rowIndex = key.get(1);
                    if (rowIndex >= y) {
                        key.set(1, rowIndex + 1L);
                        return Map.entry(Key.of(key), entry.getValue());
                    } else {
                        return entry;
                    }
                }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        IntStream.range(0, row.size())
                .forEach(x -> shiftedMap.put(Key.of(x, y), row.get(x)));

        map.clear();
        map.putAll(shiftedMap);
    }

    public void appendRow(List<T> row) {
        insertRow(row, height());
    }

    public String toFormattedString() {
        StringBuilder builder = new StringBuilder();
        long width = size(0);
        long height = size(1);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                T value = map.getOrDefault(Key.of(x, y), null);
                builder.append(value != null ? value.toString() : " ");
                if (x + 1 < width) builder.append(" ");
            }
            builder.append("\n");
        }
        return builder.toString().trim();
    }

    public Tensor<T> toMatrix() {
        return new Tensor<>(map);
    }
}
