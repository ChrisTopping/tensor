package dev.christopping.tensor;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * Dimension-invariant coordinate index representation
 * <p>
 * The size of the coordinates array is equal to the dimensionality of the coordinate index being represented
 */
public record Index(long[] coordinates) implements Comparable<Index> {

    /**
     * Creates a new {@code Index} comprising coordinates of the coordinate array provided
     *
     * @param coordinates array of coordinates
     * @return The new index
     */
    public static Index of(long... coordinates) {
        Objects.requireNonNull(coordinates, "Coordinates cannot be null");
        if (Arrays.stream(coordinates).anyMatch(coordinate -> coordinate < 0))
            throw new IndexOutOfBoundsException("Coordinates cannot be negative");
        return new Index(Arrays.copyOf(coordinates, coordinates.length));
    }

    /**
     * Creates a new {@code Index} comprising coordinates of the coordinate array provided
     *
     * @param coordinates array of coordinates
     * @return The new index
     */
    public static Index of(int... coordinates) {
        long[] coordinatesArray = Arrays.stream(coordinates).asLongStream().toArray();
        return of(coordinatesArray);
    }

    /**
     * Creates a new {@code Index} comprising coordinates of the coordinate list provided
     *
     * @param coordinates list of coordinates
     * @return The new index
     */
    public static Index of(List<Long> coordinates) {
        if (coordinates.stream().anyMatch(coordinate -> coordinate < 0))
            throw new IndexOutOfBoundsException("Coordinates cannot be negative");
        long[] coordinatesArray = coordinates.stream().mapToLong(Long::longValue).toArray();
        return of(coordinatesArray);
    }

    /**
     * Creates a complete list of valid indices for a given dimension based on a maximum index coordinate
     * </p>
     * The created indices are of the same dimensionality as the provided maximum index, ranging from the lowest valued index to the maximum index itself
     *
     * @param maxIndex the maximum index coordinate
     * @return the list of indices
     */
    public static List<Index> range(Index maxIndex) {
        if (maxIndex == null || maxIndex.isEmpty()) return new ArrayList<>();
        int order = maxIndex.order();

        BinaryOperator<List<Index>> combinationFunction = (first, second) ->
                first.stream()
                        .map(firstIndex -> second.stream().map(firstIndex::concatenate)).map(Stream::toList)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList());

        return IntStream.range(0, order)
                .mapToLong(maxIndex::get)
                .map(coordinate -> coordinate + 1)
                .mapToObj(size -> LongStream.range(0, size).mapToObj(Index::of).collect(Collectors.toList()))
                .reduce(combinationFunction)
                .orElse(new ArrayList<>())
                .stream()
                .sorted(Index::compareTo)
                .collect(Collectors.toList());
    }

    /**
     * Returns the order of the index
     *
     * @return number of dimensions
     */
    public int order() {
        return coordinates.length;
    }

    /**
     * Retrieves the coordinate for a given dimension
     *
     * @param dimension for desired coordinate (zero-based)
     * @return coordinate for given dimension
     * @throws IndexOutOfBoundsException if the dimension is out of bounds
     */
    public long get(int dimension) {
        if (dimension >= order())
            throw new IndexOutOfBoundsException(String.format("Given dimension [%d] is greater than Index dimension [%d]", dimension, order()));
        return coordinates[dimension];
    }

    /**
     * Checks if the index is empty (i.e., has no coordinates).
     *
     * @return true if the index has no coordinates, false otherwise
     */
    public boolean isEmpty() {
        return coordinates.length == 0;
    }

    /**
     * Checks if this tensor is a zero tensor.
     * A zero tensor is defined as a tensor where all components are zero.
     *
     * @return {@code true} if all components of the tensor are zero, {@code false} otherwise.
     */
    public boolean isZeroTensor() {
        return Arrays.stream(coordinates).allMatch(value -> value == 0);
    }

    /**
     * Tests whether a given dimension is constrained to a given coordinate
     *
     * @param dimension  to test
     * @param coordinate to test
     * @return true if dimension is constrained to coordinate or false if not
     */
    public boolean hasCoordinate(int dimension, long coordinate) {
        return dimension < order() && get(dimension) == coordinate;
    }

    /**
     * Tests whether a given dimension is constrained to a given coordinate
     *
     * @param coordinates to test
     * @return true if all given dimensions are constrained to their respective coordinates or false if any are not
     */
    public boolean hasCoordinates(Map<Integer, Long> coordinates) {
        return coordinates.entrySet().stream()
                .allMatch(entry -> hasCoordinate(entry.getKey(), entry.getValue()));
    }

    /**
     * Returns a new index whose coordinates are in reverse order
     *
     * @return transposed Index
     */
    public Index transpose() {
        int size = coordinates.length;
        List<Long> reversedCoordinates = IntStream.range(0, size)
                .mapToObj(index -> coordinates[size - index - 1])
                .collect(Collectors.toList());
        return Index.of(reversedCoordinates);
    }

    /**
     * Returns a new index whose newOrder are reordered by the provided newOrder list
     *
     * @param mapping the list of integers with which to reorder the newOrder into the new index
     * @return the new reordered index
     */
    public Index reorder(int... mapping) {
        long order = order();
        long mapOrder = mapping.length;
        if (mapOrder != order)
            throw new IllegalArgumentException("Mapping size [" + mapOrder + "] is not equal to index order [" + order + "]");
        long[] reorderedCoordinates = Arrays.stream(mapping)
                .mapToLong(i -> this.coordinates[i])
                .toArray();
        return Index.of(reorderedCoordinates);
    }

    /**
     * Creates a new index by scaling all coordinates by the specified scalar.
     *
     * @param scalar the value to scale each coordinate by
     * @return a new index with scaled coordinates
     */
    public Index scale(long scalar) {
        if (scalar < 0) {
            throw new IndexOutOfBoundsException("Scalar cannot be negative");
        }
        long[] scaled = Arrays.stream(coordinates)
                .map(coord -> coord * scalar)
                .toArray();
        return Index.of(scaled);
    }

    /**
     * Creates a new index by shifting all coordinates by the specified scalar.
     *
     * @param scalar the value to add to each coordinate
     * @return a new index with shifted coordinates
     */
    public Index shift(long scalar) {
        long[] shifted = Arrays.stream(coordinates)
                .map(coord -> coord + scalar)
                .toArray();
        return Index.of(shifted);
    }

    /**
     * Creates a new index by clamping all coordinates to be within the range [min, max].
     *
     * @param min the minimum allowed coordinate value
     * @param max the maximum allowed coordinate value
     * @return a new index with clamped coordinates
     */
    public Index clamp(long min, long max) {
        if (min > max) {
            throw new IllegalArgumentException("Minimum cannot be greater than maximum");
        }
        long[] clamped = Arrays.stream(coordinates)
                .map(coord -> Math.min(max, Math.max(min, coord)))
                .toArray();
        return Index.of(clamped);
    }

    /**
     * Creates a new index by applying modulo operation with the provided divisor on all coordinates.
     *
     * @param divisor the modulo divisor (must be greater than 0)
     * @return a new index with coordinates reduced by modulo operation
     */
    public Index modulo(long divisor) {
        if (divisor <= 0) {
            throw new IllegalArgumentException("Modulo divisor must be greater than 0");
        }
        long[] modded = Arrays.stream(coordinates)
                .map(coord -> Math.floorMod(coord, divisor))
                .toArray();
        return Index.of(modded);
    }

    /**
     * Creates a new index by expanding the coordinate array to the specified new size.
     * Coordinates beyond the current size are set to the given default value.
     *
     * @param newOrder          the new size of the index (must be >= current size)
     * @param defaultCoordinate the value assigned to new coordinates
     * @return a new index with expanded coordinates
     */
    public Index expand(int newOrder, long defaultCoordinate) {
        int order = order();
        if (newOrder < order) {
            throw new IllegalArgumentException("New size must be greater than or equal to current size");
        }
        long[] expanded = Arrays.copyOf(coordinates, newOrder);
        Arrays.fill(expanded, order, newOrder, defaultCoordinate);
        return Index.of(expanded);
    }

    /**
     * Creates a new index based on the original index constrained by the provided dimensions
     * </p>
     * The coordinates constrained by the constraint dimensions are removed from the coordinates of the new index
     *
     * @param dimension the constraint dimensions
     * @return the new constrained index
     */
    public Index constrain(int... dimension) {
        long[] constrained = IntStream.range(0, coordinates.length)
                .filter(index -> Arrays.stream(dimension).noneMatch(value -> value == index))
                .mapToLong(i -> coordinates[i])
                .toArray();
        return new Index(constrained);
    }

    /**
     * Extrudes a new index into the next largest dimension, at the provided coordinate value
     *
     * @param coordinate the new coordinate value
     * @return the new extruded index
     */
    public Index extrude(long coordinate) {
        return expand(order() + 1, coordinate);
    }

    /**
     * Computes a new index based on the original index by applying the given compute function across the coordinates
     *
     * @param computeFunction the coordinate compute function
     * @return the new computed index
     */
    public Index compute(Function<Long, Long> computeFunction) {
        List<Long> computedCoordinates = Arrays.stream(coordinates).mapToObj(computeFunction::apply).collect(Collectors.toList());
        return Index.of(computedCoordinates);
    }

    /**
     * Determines whether indices have same dimensionality
     *
     * @param other Index
     * @return true if other index has same dimensionality, false if not
     */
    public boolean isSimilar(Index other) {
        return other != null && other.order() == order();
    }

    private void assertSimilar(Index other) {
        if (!isSimilar(other)) {
            throw new IllegalArgumentException("Indices must have the same dimensionality");
        }
    }

    /**
     * Creates a new index by concatenating another index onto this index.
     *
     * @param other the other index
     * @return the new concatenated index
     */
    public Index concatenate(Index other) {
        long[] newCoordinates = Stream.concat(
                        Arrays.stream(this.coordinates).boxed(),
                        Arrays.stream(other.coordinates).boxed()
                ).mapToLong(Long::longValue)
                .toArray();
        return Index.of(newCoordinates);
    }

    /**
     * Computes the dot product of this index and another index.
     * The dot product is the sum of the products of corresponding coordinates.
     *
     * @param other the other index
     * @return the dot product
     */
    public long dotProduct(Index other) {
        assertSimilar(other);
        return IntStream.range(0, order())
                .mapToLong(i -> get(i) * other.get(i))
                .sum();
    }

    /**
     * Calculates the Minkowski distance to another index for a given Minkowski power
     *
     * @param other Index
     * @param power Minkowski power
     * @return the Minkowski distance
     */
    public double minkowskiDistance(Index other, double power) {
        assertSimilar(other);
        if (power <= 0) {
            throw new IllegalArgumentException("Power must be greater than 0");
        }

        return Math.pow(IntStream.range(0, order())
                .mapToDouble(i -> Math.pow(Math.abs(get(i) - other.get(i)), power))
                .sum(), 1.0 / power);
    }

    /**
     * Calculates the Euclidean distance i.e. the straight line distance between the two indices
     *
     * @param other Index
     * @return the Euclidean distance
     */
    public double euclideanDistance(Index other) {
        assertSimilar(other);
        double sumOfSquareOfDifferences = IntStream.range(0, order())
                .mapToLong(index -> get(index) - other.get(index))
                .mapToDouble(difference -> Math.pow(difference, 2))
                .sum();

        return Math.sqrt(sumOfSquareOfDifferences);
    }

    /**
     * Calculates the Manhattan distance i.e. the sum of absolute differences across dimensions
     *
     * @param other Index
     * @return the Manhattan distance
     */
    public long manhattanDistance(Index other) {
        assertSimilar(other);
        return IntStream.range(0, order())
                .mapToLong(i -> Math.abs(get(i) - other.get(i)))
                .sum();
    }

    /**
     * Calculates the Hamming distance i.e. the number of orthogonal dimensions by which the two indices disagree
     *
     * @param other Index
     * @return the hamming distance
     */
    public int hammingDistance(Index other) {
        assertSimilar(other);
        return (int) IntStream.range(0, order())
                .filter(index -> other.get(index) != get(index))
                .count();
    }

    /**
     * Calculates the Chebyshev distance i.e. the maximum absolute difference between the two indices
     *
     * @param other Index
     * @return the Chebyshev distance
     */
    public long chebyshevDistance(Index other) {
        assertSimilar(other);
        return IntStream.range(0, order())
                .mapToLong(i -> Math.abs(get(i) - other.get(i)))
                .max()
                .orElse(0); // Default to 0 if the indices are empty
    }

    /**
     * Calculates the highest order difference between two indices
     * Where the order is taken as the position of the coordinate in the coordinates list
     *
     * @param other Index to test against
     * @return highest order difference
     */
    public int highestOrderDifference(Index other) {
        assertSimilar(other);
        return IntStream.range(0, order())
                .boxed()
                .sorted(Collections.reverseOrder())
                .filter(index -> other.get(index) != get(index))
                .map(index -> index + 1)
                .findFirst()
                .orElse(0);
    }

    @Override
    public int compareTo(Index other) {
        assertSimilar(other);
        int size = order();
        for (int dimension = 0; dimension < size; dimension++) {
            Long thisCoordinate = coordinates[size - dimension - 1];
            Long otherCoordinate = other.coordinates[size - dimension - 1];
            if (!Objects.equals(thisCoordinate, otherCoordinate)) {
                return thisCoordinate.compareTo(otherCoordinate);
            }
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Index index = (Index) o;
        return Arrays.equals(this.coordinates, index.coordinates);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(coordinates);
    }

    @Override
    public String toString() {
        String commaSeparatedCoordinates = Arrays.stream(coordinates)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(", "));
        return "(" + commaSeparatedCoordinates + ")";
    }

}
