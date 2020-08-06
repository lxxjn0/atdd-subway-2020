package wooteco.subway.maps.map.domain;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public enum DistanceFarePolicy {
    DEFAULT((distance) -> distance <= 10, DistanceFarePolicy::defaultCalculateOverFare),
    OVER_TEN_UNDER_FIFTY((distance) -> distance > 10 && distance <= 50,
            DistanceFarePolicy::overTenUnderFiftyCalculateOverFare),
    OVER_FIFTY((distance) -> distance > 50, DistanceFarePolicy::overFiftyCalculateOverFare);

    private static final long DEFAULT_DISTANCE_FARE = 1_250L;

    private final Predicate<Integer> decideDistanceRange;
    private final Function<Integer, Double> calculateOverFare;

    DistanceFarePolicy(Predicate<Integer> decideDistanceRange,
            Function<Integer, Double> calculateOverFare) {
        this.decideDistanceRange = decideDistanceRange;
        this.calculateOverFare = calculateOverFare;
    }

    public static DistanceFarePolicy valueOf(int distance) {
        return Arrays.stream(values())
                .filter(distanceFarePolicy -> distanceFarePolicy.decideDistanceRange.test(distance))
                .findAny()
                .orElse(DEFAULT);
    }

    public double calculateOverFare(int distance) {
        return this.calculateOverFare.apply(distance);
    }

    private static double defaultCalculateOverFare(int distance) {
        return DEFAULT_DISTANCE_FARE;
    }

    private static double overTenUnderFiftyCalculateOverFare(int distance) {
        int calculatedDistance = distance - 10;
        return calculateOverFareByDistanceUnit(calculatedDistance, 5) + DEFAULT_DISTANCE_FARE;
    }

    private static double overFiftyCalculateOverFare(int distance) {
        int calculatedDistance = distance - 50;

        return (calculateOverFareByDistanceUnit(calculatedDistance, 8)) + 800
                + DEFAULT_DISTANCE_FARE;
    }

    private static double calculateOverFareByDistanceUnit(int calculatedDistance,
            int distanceUnit) {
        return (Math.ceil((calculatedDistance - 1) / distanceUnit) + 1) * 100;
    }
}
