package wooteco.subway.maps.map.domain;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public enum MemberFarePolicy {
    BABY((age) -> age < 6, (fare) -> 0.0),
    CHILD((age) -> age >= 6 && age < 13, (fare) -> fare * 0.5),
    YOUTH((age) -> age >= 13 && age < 19, (fare) -> fare * 0.8),
    ADULT((age) -> age >= 19, (fare) -> fare);

    private final Predicate<Integer> decideMemberType;
    private final Function<Double, Double> calculateDiscountFare;

    MemberFarePolicy(Predicate<Integer> decideMemberType,
            Function<Double, Double> calculateDiscountFare) {
        this.decideMemberType = decideMemberType;
        this.calculateDiscountFare = calculateDiscountFare;
    }

    public static MemberFarePolicy valueOf(int age) {
        return Arrays.stream(values())
                .filter(memberFarePolicy -> memberFarePolicy.decideMemberType.test(age))
                .findAny()
                .orElse(ADULT);
    }

    public double calculateDiscount(double fare) {
        return this.calculateDiscountFare.apply(fare);
    }
}
