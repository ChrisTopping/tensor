package dev.christopping.tensor;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

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

    public static <T> Tensor<T> empty() {
        return new Tensor<>();
    }

    public static <T> Tensor<T> of(List<?> list, Class<T> type) {
        if (list == null || list.isEmpty()) {
            return empty();
        } else if (list.get(0) instanceof List) {
            List<Tensor<T>> slices = list.stream().map(sublist -> of((List<?>) sublist, type)).collect(Collectors.toList());
            return Tensor.combine(slices);
        } else if (list.size() > 1) {
            return Vector.of(list).expect(type);
        } else {
            return Scalar.of(list.get(0)).expect(type);
        }
    }

    public static <T> Tensor<T> of(Object[] array, Class<T> type) {
        if (array == null || array.length == 0) {
            return empty();
        } else if (type.isInstance(array[0]) && array.length > 1) {
            return Vector.of(array).expect(type);
        } else if (type.isInstance(array[0]) && array.length == 1) {
            return Scalar.of(array[0]).expect(type);
        } else {
            List<Tensor<T>> slices = Arrays.stream(array).map(sublist -> of((Object[]) sublist, type)).collect(Collectors.toList());
            return Tensor.combine(slices);

        }
    }

    public static <T> Tensor<T> fill(T value, Index index) {
        return generate(i -> value, index);
    }

    public static <T> Tensor<T> fill(T value, long... dimensions) {
        return fill(value, Index.of(dimensions).compute(dimension -> dimension - 1));
    }

    public static <T> Tensor<T> fill(T value, int... dimensions) {
        Index compute = Index.of(dimensions).compute(dimension -> dimension - 1);
        return fill(value, compute);
    }

    public static <T> Tensor<T> generate(Function<Index, T> generator, Index index) {
        if (null == generator) throw new IllegalArgumentException("Generator function must not be null");
        Tensor<T> tensor = new Tensor<>();
        if (index.isEmpty()) {
            tensor.set(generator.apply(Index.of()), Index.of());
        } else {
            Index.indices(index).forEach(i -> tensor.set(generator.apply(i), i));
        }
        return tensor;
    }

    public static <T> Tensor<T> generate(Function<Index, T> generator, long... dimensions) {
        return generate(generator, Index.of(dimensions).compute(index -> index - 1));
    }

    public static <T> Tensor<T> generate(Function<Index, T> generator, int... dimensions) {
        return generate(generator, Index.of(dimensions).compute(index -> index - 1));
    }

    public static <T> Tensor<T> combine(List<Tensor<T>> slices) {
        if (slices == null || slices.isEmpty()) return Tensor.empty();
        Map<Index, T> map = IntStream.range(0, slices.size())
                .mapToObj(coordinate -> slices.get(coordinate).computeAndUpdateIndices(entry -> Map.entry(entry.getKey().extrude(coordinate), entry.getValue())))
                .map(tensor -> new HashMap<>(tensor.map))
                .map(HashMap::entrySet)
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return new Tensor<>(map);
    }

    public static <T> Tensor<T> combine(Tensor<T>[] slices) {
        return combine(Arrays.stream(slices).toList());
    }

    public T get(Index index) {
        return map.get(index);
    }

    public T get(long... coordinates) {
        return get(Index.of(coordinates));
    }

    public T get(int... coordinates) {
        return get(Index.of(coordinates));
    }

    public void set(T element, Index index) {
        if (!isEmpty() && order() != index.order())
            throw new IllegalArgumentException("Index order should be equal to tensor order");
        map.put(index, element);
    }

    public void set(T element, long... coordinates) {
        set(element, Index.of(coordinates));
    }

    public void set(T element, int... coordinates) {
        set(element, Index.of(coordinates));
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
        return Index.indices(Index.of(dimensions()).compute(coordinate -> coordinate - 1));
    }

    public List<T> elements() {
        return new ArrayList<>(map.values());
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
        return map.isEmpty();
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

    public <S> Tensor<S> expect(Class<S> type) {
        if (!elements().stream().allMatch(type::isInstance))
            throw new IllegalArgumentException("Tensor cannot be expected to be of given type");
        return (Tensor<S>) this;
    }

    public Tensor<T> extrude(long size) {
        Map<Index, T> extrudedMap = map.entrySet().stream()
                .map(entry -> {
                    return LongStream.range(0, size)
                            .mapToObj(coordinate -> Map.entry(entry.getKey().extrude(coordinate), entry.getValue()))
                            .collect(Collectors.toList());
                }).flatMap(Collection::stream)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return new Tensor<>(extrudedMap);
    }

    public Matrix<T> toMatrix() {
        if (order() != 2)
            throw new IllegalStateException("Tensor must be of order 2 to be converted to Matrix");
        return new Matrix<>(map);
    }

    public Vector<T> toVector() {
        if (order() != 1)
            throw new IllegalStateException("Tensor must be of order 1 to be converted to Vector");
        return new Vector<>(map);
    }

    public Scalar<T> toScalar() {
        if (order() != 0)
            throw new IllegalStateException("Tensor must be of order 0 to be converted to Scalar");
        return new Scalar<>(map);
    }

    public String toString(String defaultValue) {
        List<Index> sortedIndices = indices().stream()
                .sorted(Index::compareTo)
                .collect(Collectors.toList());

        // if scalar - print scalar value
        if (sortedIndices.size() == 0 && map.containsKey(Index.of())) return map.get(Index.of()).toString();

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
