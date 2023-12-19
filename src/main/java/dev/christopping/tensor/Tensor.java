package dev.christopping.tensor;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

// TODO: add javadocs
// TODO: creational pattern for instatiation of general matrix
// TODO: create Vector class

/**
 * Generic multi dimensional sparse tensor implementation
 *
 * @param <T> matrix value type
 */
public class Tensor<T> {

    protected final Map<Key, T> map;

    protected Tensor() {
        map = new HashMap<>();
    }

    protected Tensor(Map<Key, T> map) {
        this.map = new HashMap<>(map);
    }

    // TODO: static factory method that accepts arrays of dynamic order

    // TODO: test
    public static <T> Tensor<T> fill(T value, long... dimensions) {
        Tensor<T> tensor = new Tensor<>();
        tensor.set(value, dimensions);
        tensor.backfill(value);
        return tensor;
    }

    // TODO: test
    public static <T> Tensor<T> fill(T value, int... dimensions) {
        long[] longDimensions = Arrays.stream(dimensions)
                .mapToLong(value1 -> value1)
                .toArray();

        return fill(value, longDimensions);
    }

    // TODO: test
    public static <T> Tensor<T> empty() {
        return new Tensor<>();
    }

    public T get(long... coordinates) {
        return map.get(Key.of(coordinates));
    }

    public T get(int... coordinates) {
        long[] longCoordinates = Arrays.stream(coordinates)
                .mapToLong(v -> v)
                .toArray();

        return get(longCoordinates);
    }

    public void set(T element, long... coordinates) {
        if ((!isEmpty()) && (order() != coordinates.length))
            throw new IllegalArgumentException("Coordinate order should be equal to matrix order");
        map.put(Key.of(Arrays.stream(coordinates).boxed().collect(Collectors.toList())), element);
    }

    public void set(T element, int... coordinates) {
        long[] longCoordinates = Arrays.stream(coordinates)
                .mapToLong(v -> v)
                .toArray();

        set(element, longCoordinates);
    }

    public int order() {
        return map.keySet().stream()
                .findFirst()
                .map(Key::coordinates)
                .map(List::size)
                .orElse(0);
    }

    public long size(int dimension) {
        return map.keySet().stream()
                .filter(key -> key.coordinates().size() > dimension)
                .mapToLong(key -> key.coordinates().get(dimension))
                .max()
                .orElse(-1) + 1;
    }

    public List<Long> dimensions() {
        return IntStream.range(0, order())
                .mapToLong(this::size)
                .boxed()
                .collect(Collectors.toList());
    }

    public Tensor<T> transpose() {
        return computeAndUpdateKeys(entry -> Map.entry(entry.getKey().transpose(), entry.getValue()));
    }

    public void backfill(T element) {
        keys().forEach(key -> map.putIfAbsent(key, element));
    }

    public List<Key> keys() {
        int order = order();
        BinaryOperator<List<Key>> combinationFunction = (first, second) ->
                first.stream()
                        .map(firstKey -> second.stream().map(firstKey::combine)).map(Stream::toList)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList());
        return IntStream.range(0, order)
                .mapToLong(this::size)
                .mapToObj(size -> LongStream.range(0, size).mapToObj(Key::of).collect(Collectors.toList()))
                .reduce(combinationFunction)
                .orElse(new ArrayList<>());
    }

    public <S> Tensor<S> compute(Function<T, S> computeFunction) {
        Map<Key, S> map = this.map.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> computeFunction.apply(entry.getValue())));
        return new Tensor<>(map);
    }

    public <S> Tensor<S> computeWithKeys(Function<Map.Entry<Key, T>, S> computeFunction) {
        Map<Key, S> map = this.map.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, computeFunction));
        return new Tensor<>(map);
    }

    public <S> Tensor<S> computeAndUpdateKeys(Function<Map.Entry<Key, T>, Map.Entry<Key, S>> computeFunction) {
        Map<Key, S> map = this.map.entrySet().stream()
                .map(computeFunction)
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return new Tensor<>(map);
    }

    // TODO: reduce()

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public Tensor<T> subtensor(Map<Integer, Long> constraints) {
        List<Map.Entry<Key, T>> list = map.entrySet().stream()
                .filter(entry -> entry.getKey().hasCoordinates(constraints))
                .map(entry -> {
                    Key constrained = entry.getKey().constrain(constraints.keySet().stream().mapToInt(value -> value).toArray());
                    return Map.entry(constrained, entry.getValue());
                }).collect(Collectors.toList());
        Map<Key, T> submatrixMap = list.stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return new Tensor<>(submatrixMap);
    }

    public Matrix<T> toMatrix2D() {
        if (order() != 2)
            throw new IllegalStateException("dev.christopping.tensor.Matrix must be of order 2 to be converted to Matrix2D");
        return new Matrix<>(map);
    }

    public String toString(String defaultValue) {
        List<Key> sortedKeys = keys().stream()
                .sorted(Key::compareTo)
                .collect(Collectors.toList());

        StringBuilder builder = new StringBuilder();
        Key previous = null;
        for (Key current : sortedKeys) {
            if (previous != null) {

                int distance = previous.highestOrderDifference(current);
                if (distance > 1) {
                    builder.append(" ")
                            .append("|".repeat(distance - 1));
                }
            }
            T element = map.get(current);
            builder.append(" ").append(element == null ? defaultValue : element);
            previous = current;
        }
        return builder.toString().trim();
    }

    @Override
    public String toString() {
        return toString(" ");
    }

    // TODO: remove Objects.equal()?
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tensor<?> tensor = (Tensor<?>) o;

        return map == tensor.map || (map.equals(tensor.map));
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }
}
