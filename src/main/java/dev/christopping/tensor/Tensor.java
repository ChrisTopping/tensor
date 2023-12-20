package dev.christopping.tensor;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * Generic multi dimensional sparse tensor implementation
 *
 * @param <T> matrix value type
 */
public class Tensor<T> {

    protected final Map<Index, T> map;

    protected Tensor() {
        map = new HashMap<>();
    }

    protected Tensor(Map<Index, T> map) {
        this.map = new HashMap<>(map);
    }

    public static <T> Tensor<T> fill(T value, long... dimensions) {
        Tensor<T> tensor = new Tensor<>();
        long[] zeroIndexedCoordinates = Arrays.stream(dimensions)
                .map(dimension -> dimension - 1)
                .peek(dimension -> {
                    if (dimension < 0) throw new IllegalArgumentException("Dimensions must be positive");
                }).toArray();

        tensor.set(value, zeroIndexedCoordinates);
        tensor.backfill(value);
        return tensor;
    }

    public static <T> Tensor<T> fill(T value, int... dimensions) {
        long[] longDimensions = Arrays.stream(dimensions)
                .mapToLong(dimension -> dimension)
                .toArray();

        return fill(value, longDimensions);
    }

    public static <T> Tensor<T> empty() {
        return new Tensor<>();
    }

    public T get(long... coordinates) {
        return map.get(Index.of(coordinates));
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
        map.put(Index.of(Arrays.stream(coordinates).boxed().collect(Collectors.toList())), element);
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
                .map(Index::coordinates)
                .map(List::size)
                .orElse(0);
    }

    public long size(int dimension) {
        return map.keySet().stream()
                .filter(index -> index.coordinates().size() > dimension)
                .mapToLong(index -> index.coordinates().get(dimension))
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
        return computeAndUpdateIndices(entry -> Map.entry(entry.getKey().transpose(), entry.getValue()));
    }

    public void backfill(T element) {
        indices().forEach(index -> map.putIfAbsent(index, element));
    }

    public List<Index> indices() {
        int order = order();
        BinaryOperator<List<Index>> combinationFunction = (first, second) ->
                first.stream()
                        .map(firstIndex -> second.stream().map(firstIndex::combine)).map(Stream::toList)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList());
        return IntStream.range(0, order)
                .mapToLong(this::size)
                .mapToObj(size -> LongStream.range(0, size).mapToObj(Index::of).collect(Collectors.toList()))
                .reduce(combinationFunction)
                .orElse(new ArrayList<>());
    }

    public <S> Tensor<S> compute(Function<T, S> computeFunction) {
        Map<Index, S> map = this.map.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> computeFunction.apply(entry.getValue())));
        return new Tensor<>(map);
    }

    public <S> Tensor<S> computeWithIndices(Function<Map.Entry<Index, T>, S> computeFunction) {
        Map<Index, S> map = this.map.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, computeFunction));
        return new Tensor<>(map);
    }

    public <S> Tensor<S> computeAndUpdateIndices(Function<Map.Entry<Index, T>, Map.Entry<Index, S>> computeFunction) {
        Map<Index, S> map = this.map.entrySet().stream()
                .map(computeFunction)
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return new Tensor<>(map);
    }

    public boolean isEmpty() {
        boolean empty = map.isEmpty();
        return empty;
    }

    public Tensor<T> slice(Map<Integer, Long> constraints) {
        List<Map.Entry<Index, T>> list = map.entrySet().stream()
                .filter(entry -> entry.getKey().hasCoordinates(constraints))
                .map(entry -> {
                    Index constrained = entry.getKey().constrain(constraints.keySet().stream().mapToInt(value -> value).toArray());
                    return Map.entry(constrained, entry.getValue());
                }).collect(Collectors.toList());
        Map<Index, T> submatrixMap = list.stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return new Tensor<>(submatrixMap);
    }

    public Matrix<T> toMatrix() {
        if (order() != 2)
            throw new IllegalStateException("Matrix must be of order 2 to be converted to Matrix2D");
        return new Matrix<>(map);
    }

    public String toString(String defaultValue) {
        List<Index> sortedIndices = indices().stream()
                .sorted(Index::compareTo)
                .collect(Collectors.toList());

        StringBuilder builder = new StringBuilder();
        Index previous = null;
        for (Index current : sortedIndices) {
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
