package dev.christopping.tensor;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public record Key(List<Long> coordinates) implements Comparable<Key> {

    public static Key of(List<Long> coordinates) {
        if (coordinates.stream().anyMatch(coordinate -> coordinate < 0))
            throw new IllegalArgumentException("Coordinates cannot be negative");
        return new Key(coordinates);
    }

    public static Key of(long... coordinates) {
        return new Key(new ArrayList<>(Arrays.stream(coordinates).boxed().toList()));
    }

    public static Key of(int... coordinates) {
        return new Key(new ArrayList<>(Arrays.stream(coordinates).mapToLong(value -> value).boxed().toList()));
    }

    /**
     * Retrieves the number of dimensions constrained by the coordinates
     *
     * @return number of dimensions
     */
    public int dimensionality() {
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
        if (dimension >= dimensionality())
            throw new IllegalArgumentException(String.format("Given dimension [%d] is greater than Key dimension [%d]", dimension, dimensionality()));
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
        return dimension < dimensionality() && get(dimension) == coordinate;
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

    public double euclideanDistance(Key other) {
        if (!isSimilar(other)) throw new IllegalArgumentException("Keys should be of same size");
        double sumOfSquareOfDifferences = IntStream.range(0, dimensionality())
                .mapToLong(index -> get(index) - other.get(index))
                .mapToDouble(difference -> Math.pow(difference, 2))
                .sum();

        return Math.sqrt(sumOfSquareOfDifferences);
    }

    /**
     * Calculates the number of orthogonal dimensions by which the two keys disagree
     *
     * @param other Key
     * @return number of disagreeing dimensions
     */
    public int orthogonalDistance(Key other) {
        if (!isSimilar(other)) throw new IllegalArgumentException("Keys should be of same size");
        return (int) IntStream.range(0, dimensionality())
                .filter(index -> other.get(index) != get(index))
                .count();
    }

    /**
     * Calculates the highest order difference between two keys
     * Where the order is taken as the position of the coordinate in the coordinates list
     *
     * @param other Key to test against
     * @return highest order difference
     */
    public int highestOrderDifference(Key other) {
        if (!isSimilar(other)) throw new IllegalArgumentException("Keys should be of same size");
        return IntStream.range(0, dimensionality())
                .boxed()
                .sorted(Collections.reverseOrder())
                .filter(index -> other.get(index) != get(index))
                .map(index -> index + 1)
                .findFirst()
                .orElse(0);
    }

    /**
     * Determines whether keys have same dimensionality
     *
     * @param other Key
     * @return true if other key has same dimensionality, false if not
     */
    public boolean isSimilar(Key other) {
        return other != null && other.dimensionality() == dimensionality();
    }

    /**
     * Returns a new key whose coordinates are in reverse order
     *
     * @return transposed Key
     */
    public Key transpose() {
        int size = coordinates.size();
        List<Long> reversedCoordinates = IntStream.range(0, size)
                .mapToObj(index -> coordinates.get(size - index - 1))
                .collect(Collectors.toList());
        return Key.of(reversedCoordinates);
    }

    public Key reorder(List<Integer> order) {
        long size = dimensionality();
        int orderSize = order.size();
        if (orderSize != size)
            throw new IllegalArgumentException("Order list size [" + orderSize + "] is not equal to coordinate size [" + size + "]");
        List<Long> reorderedCoordinates = order.stream()
                .map(coordinates::get)
                .collect(Collectors.toList());
        return Key.of(reorderedCoordinates);
    }

    public Key constrain(int... toRemove) {
        List<Long> constrained = IntStream.range(0, coordinates.size())
                .filter(index -> Arrays.stream(toRemove).noneMatch(value -> value == index))
                .mapToObj(coordinates::get)
                .collect(Collectors.toList());
        return new Key(constrained);
    }

    public Key combine(Key other) {
        ArrayList<Long> newCoordinates = new ArrayList<>(coordinates);
        newCoordinates.addAll(other.coordinates);
        return Key.of(newCoordinates);
    }

    @Override
    public int compareTo(Key other) {
        if (!isSimilar(other)) throw new IllegalArgumentException("Keys should be of same size");
        int size = dimensionality();
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
        Key key = (Key) o;
        return new ArrayList<>(this.coordinates).equals(new ArrayList<>(key.coordinates));
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
