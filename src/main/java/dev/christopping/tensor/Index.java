package dev.christopping.tensor;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public record Index(List<Long> coordinates) implements Comparable<Index> {

    public static Index of(List<Long> coordinates) {
        if (coordinates.stream().anyMatch(coordinate -> coordinate < 0))
            throw new IllegalArgumentException("Coordinates cannot be negative");
        return new Index(new ArrayList<>(coordinates));
    }

    public static Index of(long... coordinates) {
        return of(Arrays.stream(coordinates).boxed().toList());
    }

    public static Index of(int... coordinates) {
        return of(Arrays.stream(coordinates).mapToLong(Long::valueOf).boxed().toList());
    }

    public static List<Index> indices(Index maxIndex) {
        if (maxIndex.isEmpty()) return new ArrayList<>();
        int order = maxIndex.order();

        BinaryOperator<List<Index>> combinationFunction = (first, second) ->
                first.stream()
                        .map(firstIndex -> second.stream().map(firstIndex::combine)).map(Stream::toList)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList());

        return IntStream.range(0, order)
                .mapToLong(maxIndex::get)
                .map(coordinate -> coordinate + 1)
                .mapToObj(size -> LongStream.range(0, size).mapToObj(Index::of).collect(Collectors.toList()))
                .reduce(combinationFunction)
                .orElse(new ArrayList<>());
    }

    /**
     * Retrieves the number of dimensions constrained by the coordinates
     *
     * @return number of dimensions
     */
    public int order() {
        return coordinates.size();
    }

    /**
     * Retrieves the coordinate for a given dimension
     *
     * @param dimension for desired coordinate
     * @return coordinate for given dimension
     * @throws IllegalArgumentException if given dimension is greater than dimensionality
     */
    public long get(int dimension) {
        if (dimension >= order())
            throw new IllegalArgumentException(String.format("Given dimension [%d] is greater than Index dimension [%d]", dimension, order()));
        return coordinates.get(dimension);
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
        return coordinates.entrySet().stream().allMatch(entry -> hasCoordinate(entry.getKey(), entry.getValue()));
    }

    public double euclideanDistance(Index other) {
        if (!isSimilar(other)) throw new IllegalArgumentException("Indices should be of same size");
        double sumOfSquareOfDifferences = IntStream.range(0, order())
                .mapToLong(index -> get(index) - other.get(index))
                .mapToDouble(difference -> Math.pow(difference, 2))
                .sum();

        return Math.sqrt(sumOfSquareOfDifferences);
    }

    /**
     * Calculates the number of orthogonal dimensions by which the two indices disagree
     *
     * @param other Index
     * @return number of disagreeing dimensions
     */
    public int orthogonalDistance(Index other) {
        if (!isSimilar(other)) throw new IllegalArgumentException("Indices should be of same size");
        return (int) IntStream.range(0, order())
                .filter(index -> other.get(index) != get(index))
                .count();
    }

    /**
     * Calculates the highest order difference between two indices
     * Where the order is taken as the position of the coordinate in the coordinates list
     *
     * @param other Index to test against
     * @return highest order difference
     */
    public int highestOrderDifference(Index other) {
        if (!isSimilar(other)) throw new IllegalArgumentException("Indices should be of same size");
        return IntStream.range(0, order())
                .boxed()
                .sorted(Collections.reverseOrder())
                .filter(index -> other.get(index) != get(index))
                .map(index -> index + 1)
                .findFirst()
                .orElse(0);
    }

    public boolean isEmpty() {
        return coordinates.isEmpty();
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

    /**
     * Returns a new index whose coordinates are in reverse order
     *
     * @return transposed Index
     */
    public Index transpose() {
        int size = coordinates.size();
        List<Long> reversedCoordinates = IntStream.range(0, size)
                .mapToObj(index -> coordinates.get(size - index - 1))
                .collect(Collectors.toList());
        return Index.of(reversedCoordinates);
    }

    public Index reorder(List<Integer> order) {
        long size = order();
        int orderSize = order.size();
        if (orderSize != size)
            throw new IllegalArgumentException("Order list size [" + orderSize + "] is not equal to coordinate size [" + size + "]");
        List<Long> reorderedCoordinates = order.stream()
                .map(coordinates::get)
                .collect(Collectors.toList());
        return Index.of(reorderedCoordinates);
    }

    public Index constrain(int... toRemove) {
        List<Long> constrained = IntStream.range(0, coordinates.size())
                .filter(index -> Arrays.stream(toRemove).noneMatch(value -> value == index))
                .mapToObj(coordinates::get)
                .collect(Collectors.toList());
        return new Index(constrained);
    }

    public Index combine(Index other) {
        ArrayList<Long> newCoordinates = new ArrayList<>(coordinates);
        newCoordinates.addAll(other.coordinates);
        return Index.of(newCoordinates);
    }

    public Index compute(Function<Long, Long> computeFunction) {
        List<Long> computedCoordinates = coordinates.stream().map(computeFunction).collect(Collectors.toList());
        return Index.of(computedCoordinates);
    }

    public Index stretch(long coordinate) {
        ArrayList<Long> stretchedCoordinates = new ArrayList<>(this.coordinates);
        stretchedCoordinates.add(coordinate);
        return Index.of(stretchedCoordinates);
    }

    @Override
    public int compareTo(Index other) {
        if (!isSimilar(other)) throw new IllegalArgumentException("Indices should be of same size");
        int size = order();
        for (int dimension = 0; dimension < size; dimension++) {
            Long thisCoordinate = coordinates.get(size - dimension - 1);
            Long otherCoordinate = other.coordinates.get(size - dimension - 1);
            if (!java.util.Objects.equals(thisCoordinate, otherCoordinate)) {
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
        return new ArrayList<>(this.coordinates).equals(new ArrayList<>(index.coordinates));
    }

    @Override
    public int hashCode() {
        return coordinates.hashCode();
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(", ");
        coordinates.stream()
                .map(java.util.Objects::toString)
                .forEach(joiner::add);
        return "(" + joiner + ")";
    }
}
