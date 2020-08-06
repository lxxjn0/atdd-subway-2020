package wooteco.subway.maps.line.domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;

@Access(AccessType.FIELD)
@Embeddable
public class Fare {
    private long fare;

    protected Fare() {
    }

    public Fare(int fare) {
        this.fare = fare;
    }

    public long getFare() {
        return fare;
    }
}
