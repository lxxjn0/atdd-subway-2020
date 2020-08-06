package wooteco.subway.maps.line.domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;

@Access(AccessType.FIELD)
@Embeddable
public class Fare {
    public static double DEFAULT_LINE_FARE = 0;

    private double fare;

    protected Fare() {
    }

    public Fare(double fare) {
        this.fare = fare;
    }

    public double getFare() {
        return fare;
    }
}
