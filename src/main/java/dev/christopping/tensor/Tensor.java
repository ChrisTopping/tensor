package dev.christopping.tensor;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

/**
 * Generic multi dimensional sparse tensor implementation
 *
 * @param <T> element value type
 */
public class Tensor<T> {

    protected final Map<Index, T> map;

    /**
     * Constructs a new empty {@code Tensor}
     */
    protected Tensor() {
        map = new HashMap<>();
    }

    /**
     * Constructs a new {@code Tensor} comprising values contained within the map provided
     */
    protected Tensor(Map<Index, T> map) {
        this.map = new HashMap<>(map);
    }

    /**
     * Creates a new empty {@code Tensor}
     *
     * @param <T> the type of values contained in this tensor
     * @return The new tensor
     */
    public static <T> Tensor<T> empty() {
        return new Tensor<>();
    }

    /**
     * Creates a new {@code Tensor} comprising items of the given type contained in the list
     *
     * @param list nested list of values
     * @param type type of values
     * @param <T>  the type of values contained in this tensor
     * @return The new tensor
     */
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

    /**
     * Creates a new {@code Tensor} comprising items of the given type contained in the list
     *
     * @param array nested array of values
     * @param type  type of values
     * @param <T>   the type of values contained in this tensor
     * @return The new tensor
     */
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

    /**
     * Creates a new {@code Tensor} comprising the same uniform provided value for all indices up to the maximum index provided
     *
     * @param value the value to be set at all indices
     * @param shape the shape index of the resultant tensor
     * @param <T>   the type of values contained in this tensor
     * @return The new tensor
     */
    public static <T> Tensor<T> fill(T value, Index shape) {
        return generate(i -> value, shape);
    }

    /**
     * Creates a new {@code Tensor} comprising the same uniform provided value for all indices up to the dimensions provided
     *
     * @param value      the value to be set at all indices
     * @param dimensions the dimensions of the new tensor
     * @param <T>        the type of values contained in this tensor
     * @return The new tensor
     */
    public static <T> Tensor<T> fill(T value, long... dimensions) {
        return fill(value, Index.of(dimensions).compute(dimension -> Math.max(dimension - 1, 0)));
    }

    /**
     * Creates a new {@code Tensor} comprising the same uniform provided value for all indices up to the dimensions provided
     *
     * @param value      the value to be set at all indices
     * @param dimensions the dimensions of the new tensor
     * @param <T>        the type of values contained in this tensor
     * @return The new tensor
     */
    public static <T> Tensor<T> fill(T value, int... dimensions) {
        Index compute = Index.of(dimensions).compute(dimension -> Math.max(dimension - 1, 0));
        return fill(value, compute);
    }

    public static Tensor<Integer> identity(Index shape) {
        return Tensor.generate(index -> index.isIdentityIndex() ? 1 : 0, shape);
    }

    /**
     * Creates a new {@code Tensor} using a generator function
     *
     * @param generator the generator function
     * @param shape     the shape index of the resultant tensor
     * @param <T>       the type of values contained in this tensor
     * @return The new tensor
     */
    public static <T> Tensor<T> generate(Function<Index, T> generator, Index shape) {
        if (null == generator) throw new IllegalArgumentException("Generator function must not be null");
        Tensor<T> tensor = new Tensor<>();
        if (shape.isEmpty()) {
            tensor.set(generator.apply(Index.of()), Index.of());
        } else {
            Index.range(shape).forEach(i -> tensor.set(generator.apply(i), i));
        }
        return tensor;
    }

    /**
     * Creates a new {@code Tensor} using a generator function
     *
     * @param generator  the generator function
     * @param dimensions the dimensions of the new tensor
     * @param <T>        the type of values contained in this tensor
     * @return The new tensor
     */
    public static <T> Tensor<T> generate(Function<Index, T> generator, long... dimensions) {
        return generate(generator, Index.of(dimensions).compute(index -> index - 1));
    }

    /**
     * Creates a new {@code Tensor} using a generator function
     *
     * @param generator  the generator function
     * @param dimensions the dimensions of the new tensor
     * @param <T>        the type of values contained in this tensor
     * @return The new tensor
     */
    public static <T> Tensor<T> generate(Function<Index, T> generator, int... dimensions) {
        return generate(generator, Index.of(dimensions).compute(index -> index - 1));
    }

    /**
     * Combines same-dimensional tensors
     * If only 1 tensor is provided, the same tensor is returned
     * Else, the resultant tensor will be 1 dimension higher than the provided tensors
     *
     * @param slices list of tensors to combine
     * @param <T>    type of the tensor element values
     * @return the combined tensor
     */
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

    /**
     * Combines same-dimensional tensors
     * If only 1 tensor is provided, the same tensor is returned
     * Else, the resultant tensor will be 1 dimension higher than the provided tensors
     *
     * @param slices array of tensors to combine
     * @param <T>    type of the tensor element values
     * @return the combined tensor
     */
    public static <T> Tensor<T> combine(Tensor<T>[] slices) {
        return combine(Arrays.stream(slices).toList());
    }

    /**
     * Returns the value at a given index
     *
     * @param index the index at which to return the value
     * @return the value
     */
    public T get(Index index) {
        return map.get(index);
    }

    public T getOrDefault(T defaultValue, Index index) {
        return map.getOrDefault(index, defaultValue);
    }

    /**
     * Returns the value at a given set of coordinates
     *
     * @param coordinates the set of coordinates at which to return the value
     * @return the value
     */
    public T get(long... coordinates) {
        return get(Index.of(coordinates));
    }

    public T getOrDefault(T defaultValue, long... coordinates) {
        return getOrDefault(defaultValue, Index.of(coordinates));
    }

    /**
     * Returns the value at a given set of coordinates
     *
     * @param coordinates the set of coordinates at which to return the value
     * @return the value
     */
    public T get(int... coordinates) {
        return get(Index.of(coordinates));
    }

    public T getOrDefault(T defaultValue, int... coordinates) {
        return getOrDefault(defaultValue, Index.of(coordinates));
    }

    /**
     * Sets the value at a given index
     *
     * @param element the value of the element to be set
     * @param index   the index at which to set the value
     */
    public void set(T element, Index index) {
        if (!isEmpty() && order() != index.order())
            throw new IllegalArgumentException("Index order should be equal to tensor order");
        map.put(index, element);
    }

    public void setIfAbsent(T element, Index index) {
        if (!isEmpty() && order() != index.order())
            throw new IllegalArgumentException("Index order should be equal to tensor order");
        map.putIfAbsent(index, element);
    }

    /**
     * Sets the value at a given set of coordinates
     *
     * @param element     the value of the element to be set
     * @param coordinates the set of coordinates at which to set the value
     */
    public void set(T element, long... coordinates) {
        set(element, Index.of(coordinates));
    }

    public void setIfAbsent(T element, long... coordinates) {
        setIfAbsent(element, Index.of(coordinates));
    }

    /**
     * Sets the value at a given set of coordinates
     *
     * @param element     the value of the element to be set
     * @param coordinates the set of coordinates at which to set the value
     */
    public void set(T element, int... coordinates) {
        set(element, Index.of(coordinates));
    }

    public void setIfAbsent(T element, int... coordinates) {
        setIfAbsent(element, Index.of(coordinates));
    }

    public void remove(Index index) {
        map.remove(index);
    }

    /**
     * Returns the number of dimensions (i.e. order) of the tensor
     *
     * @return the order
     */
    public int order() {
        return map.keySet().stream()
                .findFirst()
                .map(Index::order)
                .orElse(0);
    }

    /**
     * Returns the size (i.e. maximum index) within a given dimension
     *
     * @param dimension the dimension of which to return the size
     * @return the size
     */
    public long size(int dimension) {
        return map.keySet().stream()
                .filter(index -> index.order() > dimension)
                .mapToLong(index -> index.get(dimension))
                .max()
                .orElse(-1) + 1;
    }

    /**
     * Returns the sizes (i.e. maximum indices) for all valid dimensions
     *
     * @return the dimensions
     */
    public List<Long> dimensions() {
        return IntStream.range(0, order())
                .mapToLong(this::size)
                .boxed()
                .collect(Collectors.toList());
    }

    /**
     * Returns all valid indices contained within the dimensionality of the tensor
     *
     * @return the list of all valid indices
     */
    public List<Index> indices() {
        return Index.range(Index.of(dimensions()).compute(coordinate -> Math.max(coordinate - 1, 0)));
    }

    public boolean contains(T value) {
        return map.containsValue(value);
    }

    /**
     * Returns a list all present element values
     *
     * @return element values
     */
    public List<T> elements() {
        return map.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public Vector<T> flatten(T defaultValue) {
        return Vector.of(backfill(defaultValue).elements());
    }

    /**
     * Checks if the tensor has any elements.</p>
     *
     * @return true if the tensor has 0 elements, false if the tensor has 1 or more elements
     */
    public boolean isEmpty() {
        return map.isEmpty();
    }

    /**
     * Checks all elements to ensure an exact type-match against the given type
     *
     * @param type the type to be type-matched against
     * @param <S>  the parameter type of the type-casted tensor
     * @return the type-casted tensor
     * @throws IllegalArgumentException if there are any failed type-matches against any of the elements
     */
    public <S> Tensor<S> expect(Class<S> type) {
        if (!elements().stream().allMatch(type::isInstance))
            throw new IllegalArgumentException("Tensor cannot be expected to be of given type");
        return (Tensor<S>) this;
    }

    /**
     * Returns a copy of the original tensor where all non-present elements in the tensor are set to the provided element value
     *
     * @param element the value to be back-filled
     */
    public Tensor<T> backfill(T element) {
        List<Index> indices = indices();

        if (indices.isEmpty()) {
            return new Tensor<>(map);
        }

        Map<Index, T> backfilledMap = indices.stream()
                .collect(Collectors.toMap(index -> index, index -> map.getOrDefault(index, element)));
        return new Tensor<>(backfilledMap);
    }

    /**
     * Returns a new tensor with generically transposed values.</p>
     * Calculates the transposed index of each element and creates a new tensor with all elements at their transposed index.</p>
     * Retains the element size of the original tensor.
     *
     * @return the transposed tensor
     */
    public Tensor<T> transpose() {
        return computeAndUpdateIndices(entry -> Map.entry(entry.getKey().transpose(), entry.getValue()));
    }

    /**
     * Creates a new tensor with values calculated using the compute function
     *
     * @param computeFunction a scalar function applied to the elements of the original tensor to create the values for the new tensor.
     * @param <S>             the parameter type of the new tensor
     * @return the new tensor
     */
    public <S> Tensor<S> compute(Function<T, S> computeFunction) {
        Map<Index, S> map = this.map.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> computeFunction.apply(entry.getValue())));
        return new Tensor<>(map);
    }

    /**
     * Creates a new tensor with values calculated using the compute function
     *
     * @param computeFunction a non-scalar function applied to the elements of the original tensor and their indices to create the values for the new tensor.
     * @param <S>             the parameter type of the new tensor
     * @return the new tensor
     */
    public <S> Tensor<S> computeWithIndices(Function<Map.Entry<Index, T>, S> computeFunction) {
        Map<Index, S> map = this.map.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, computeFunction));
        return new Tensor<>(map);
    }

    /**
     * Creates a new tensor with values calculated using the compute function. Sets the index of each element based on the compute function.
     *
     * @param computeFunction a non-scalar function applied to the elements of the original tensor and their indices to create the values for the new tensor.
     * @param <S>             the parameter type of the new tensor
     * @return the new tensor
     */
    public <S> Tensor<S> computeAndUpdateIndices(Function<Map.Entry<Index, T>, Map.Entry<Index, S>> computeFunction) {
        Map<Index, S> map = this.map.entrySet().stream()
                .map(computeFunction)
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return new Tensor<>(map);
    }

    public Tensor<T> reduce(T identity, BinaryOperator<T> accumulator, int dimension) {
        if (dimension > order() - 1) {
            throw new IndexOutOfBoundsException("Specified dimension greater than order of tensor");
        }

        Map<Index, T> reduced = map.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.groupingBy(e -> e.getKey().constrain(dimension)))
                .values().stream()
                .collect(Collectors.toMap(
                        e -> e.get(0).getKey().constrain(dimension),
                        e -> e.stream().map(Map.Entry::getValue).reduce(identity, accumulator)
                ));

        return new Tensor<>(reduced);
    }

    public <S> Tensor<S> reduce(S identity, BiFunction<S, T, S> accumulator, BinaryOperator<S> combiner, int dimension) {
        if (dimension >= order()) {
            throw new IndexOutOfBoundsException("Specified dimension greater than order of tensor");
        }

        Map<Index, S> reduced = map.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.groupingBy(e -> e.getKey().constrain(dimension)))
                .values().stream()
                .collect(Collectors.toMap(
                        e -> e.get(0).getKey().constrain(dimension),
                        e -> {
                            List<T> t = e.stream().map(Map.Entry::getValue).collect(Collectors.toList());
                            return t.stream().reduce(identity, accumulator, combiner);
                        }));

        return new Tensor<>(reduced);
    }

    public Tensor<T> mask(Tensor<Boolean> mask, T maskedValue) {
        return piecewise((element, o) -> ((Boolean.TRUE.equals(o)) ? element : maskedValue), mask);
    }

    /**
     * Applies a piecewise bi-function onto the tensor and another provided tensor
     *
     * @param piecewiseFunction the function which takes values of two same-dimensioned tensors at each index and returns a new value
     * @param other             the other tensor
     * @param <U>               - the type of the other tensor
     * @param <S>               - the type of the new tensor
     * @return the resultant tensor
     */
    public <S, U> Tensor<S> piecewise(BiFunction<T, U, S> piecewiseFunction, Tensor<U> other) {
        if (other == null || !dimensions().equals(other.dimensions()))
            throw new IllegalArgumentException("Tensor dimensions must match");
        Map<Index, S> resultMap = map.keySet().stream()
                .filter(index -> other.get(index) != null)
                .collect(Collectors.toMap(index -> index, index -> piecewiseFunction.apply(get(index), other.get(index))));
        return new Tensor<>(resultMap);
    }

    /**
     * Slices the tensor across a given set of constraints
     * Returns a tensor of order (previous order) - (number of constraints)
     *
     * @param constraints a map of constraints by which to slice the tensor - each constraint locking the sub-tensor to a fixed value in the given dimension
     * @return the sliced tensor
     */
    public Tensor<T> slice(Map<Integer, Long> constraints) {
        List<Map.Entry<Index, T>> list = map.entrySet().stream()
                .filter(entry -> entry.getKey().hasCoordinates(constraints))
                .map(entry -> {
                    Index constrained = entry.getKey().constrain(constraints.keySet().stream().mapToInt(value -> value).toArray());
                    return Map.entry(constrained, entry.getValue());
                }).collect(Collectors.toList());
        Map<Index, T> slicedMap = list.stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return new Tensor<>(slicedMap);
    }

    /**
     * Extrudes the tensor along the next dimension.
     * Spreads this tensor {@code size} times in the higher dimension.
     *
     * @param size the number of times to spread the tensor
     * @return the extruded tensor of order 1 higher than this tensor
     */
    public Tensor<T> extrude(long size) {
        Map<Index, T> extrudedMap = map.entrySet().stream()
                .map(entry -> LongStream.range(0, size)
                        .mapToObj(coordinate -> Map.entry(entry.getKey().extrude(coordinate), entry.getValue()))
                        .collect(Collectors.toList())
                ).flatMap(Collection::stream)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return new Tensor<>(extrudedMap);
    }

    public Tensor<T> extract(Index min, Index max) {
        if (min.order() != max.order()) {
            throw new IllegalArgumentException("Min and max must have the same number of dimensions.");
        }
        if (!min.isWithinBounds(max)) {
            throw new IllegalArgumentException("Min must be bounded by max");
        }

        // Directly use min and max as boundaries, treating 'max' as an upper bound, not a range
        Map<Index, T> extractedMap = map.entrySet().stream()
                .filter(entry -> entry.getKey().isWithinBounds(min, max))
                .collect(Collectors.toMap(
                        entry -> entry.getKey().subtract(min),
                        Map.Entry::getValue
                ));

        return new Tensor<>(extractedMap);
    }

    public boolean isScalar() {
        return order() == 0;
    }

    /**
     * Coerces the tensor to a {@code Scalar}
     *
     * @return the coerced scalar
     * @throws IllegalStateException if tensor is not of order 0
     */
    public Scalar<T> toScalar() {
        if (order() != 0)
            throw new IllegalStateException("Tensor must have an order of 0 to be converted to a Scalar");
        return new Scalar<>(map);
    }

    public boolean isVector() {
        return order() == 1;
    }

    /**
     * Coerces the tensor to a {@code Vector}
     *
     * @return the coerced vector
     * @throws IllegalStateException if tensor is not of order 1
     */
    public Vector<T> toVector() {
        if (order() > 1) {
            throw new IllegalStateException("Tensor must have an order of 1 to be converted to a Vector");
        } else if (isScalar()) {
            return Vector.of(elements());
        } else {
            return new Vector<>(map);
        }
    }

    public boolean isMatrix() {
        return order() == 2;
    }

    /**
     * Coerces the tensor to a {@code Matrix}
     *
     * @return the coerced matrix
     * @throws IllegalStateException if tensor is not of order 2
     */
    public Matrix<T> toMatrix() {
        if (order() > 2)
            throw new IllegalStateException("Tensor must have an order of 2 to be converted to a Matrix");
        return new Matrix<>(map);
    }

    public Tensor<T> toTensor() {
        return new Tensor<>(map);
    }

    /**
     * Converts tensor to string representation
     *
     * @return string representation of tensor
     */
    @Override
    public String toString() {
        return toString("[", "]", ",", "", " ", false);
    }

    /**
     * Converts tensor to string representation
     * </p>
     * Uses parameterised values to change the formatting of the representation
     *
     * @param open               opening bracketing symbol
     * @param close              closing bracketing symbol
     * @param separator          element separator
     * @param delineator         sub-tensor delineator
     * @param defaultValue       default value for non-present elements
     * @param repeatedDelineator determines whether the delineator is repeated on nested sub-tensors
     * @return tensor string representation
     */
    public String toString(String open, String close, String separator, String delineator, String defaultValue, boolean repeatedDelineator) {
        // Handle empty map
        if (map.isEmpty()) {
            return "";
        }

        // Handle scalar value
        if (order() == 0) {
            return get().toString();
        }

        List<Index> sortedIndices = indices().stream()
                .sorted(Index::compareTo)
                .collect(Collectors.toList());

        int order = sortedIndices.get(sortedIndices.size() - 1)
                .highestOrderDifference(sortedIndices.get(0));
        long sizeOfLowestDimension = sortedIndices.get(sortedIndices.size() - 1).get(0);

        StringBuilder builder = new StringBuilder().append(open.repeat(order));

        boolean delineatorSurroundedByWhitespace = delineator.matches("\s.\s");

        String normalisedDelineator = delineator.trim().length() == 0 ? delineator : delineator.trim();

        Index previous = null;
        for (Index current : sortedIndices) {
            if (previous != null) {
                if (previous.get(0) != sizeOfLowestDimension) {
                    builder.append(separator);
                }

                int distance = previous.highestOrderDifference(current);
                if (distance > 1) {
                    builder.append(close.repeat(Math.max(distance - 1, 0)))
                            .append(delineatorSurroundedByWhitespace ? " " : "")
                            .append(repeatedDelineator ? normalisedDelineator.repeat(Math.max(distance - 1, 0)) : delineator)
                            .append(delineatorSurroundedByWhitespace ? " " : "")
                            .append(open.repeat(Math.max(distance - 1, 0)));
                }

            }
            T element = map.get(current);
            builder.append(element == null ? defaultValue : element);

            previous = current;
        }

        return builder.append(close.repeat(order))
                .toString();
    }

    public String toFormattedString() {
        return toString("", "", "\t", "\n", ".", true);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tensor<?> tensor)) return false;
        return map == tensor.map || (map.equals(tensor.map));
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }

    public static <T> TensorBuilder<T> builder() {
        return new TensorBuilder<>();
    }

    public static class TensorBuilder<T> {
        private final Tensor<T> tensor;

        public TensorBuilder() {
            tensor = Tensor.empty();
        }

        public TensorBuilder<T> add(Index index, T element) {
            tensor.set(element, index);
            return this;
        }

        public TensorBuilder<T> add(Map<Index, T> map) {
            tensor.map.putAll(map);
            return this;
        }

        public Tensor<T> build() {
            return tensor;
        }
    }



}
