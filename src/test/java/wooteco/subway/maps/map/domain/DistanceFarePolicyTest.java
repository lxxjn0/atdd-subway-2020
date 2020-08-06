package wooteco.subway.maps.map.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DistanceFarePolicyTest {
    @DisplayName("요금의 범위에 따라 해당하는 구간 요금제를 반환한다.")
    @Test
    void valueOf() {
        DistanceFarePolicy defaultPolicy = DistanceFarePolicy.valueOf(10);
        DistanceFarePolicy overTenUnderFiftyPolicy = DistanceFarePolicy.valueOf(35);
        DistanceFarePolicy overFiftyPolicy = DistanceFarePolicy.valueOf(60);

        assertAll(
                () -> assertThat(defaultPolicy).isEqualTo(DistanceFarePolicy.DEFAULT),
                () -> assertThat(overTenUnderFiftyPolicy).isEqualTo(
                        DistanceFarePolicy.OVER_TEN_UNDER_FIFTY),
                () -> assertThat(overFiftyPolicy).isEqualTo(DistanceFarePolicy.OVER_FIFTY)
        );
    }

    @DisplayName("기본 요금 범위일 경우 기본 요금만 반환한다.")
    @Test
    void calculateOverFare_Default() {
        int distance = 10;
        DistanceFarePolicy defaultPolicy = DistanceFarePolicy.valueOf(distance);

        assertThat(defaultPolicy.calculateOverFare(distance)).isEqualTo(1_250);
    }

    @DisplayName("10보다 크고 50이하일 경우 기본 요금에서 5km당 100원의 추가 요금을 반영해 반환한다.")
    @Test
    void calculateOverFare_OverTenUnderFifty() {
        int distance = 35;
        DistanceFarePolicy overTenUnderFiftyPolicy = DistanceFarePolicy.valueOf(distance);

        assertThat(overTenUnderFiftyPolicy.calculateOverFare(distance)).isEqualTo(1_750);
    }

    @DisplayName("10보다 크고 50이하일 경우 기본 요금에서 5km당 100원의 추가 요금을 반영해 반환한다.")
    @Test
    void calculateOverFare_OverFifty() {
        int distance = 60;
        DistanceFarePolicy overFiftyPolicy = DistanceFarePolicy.valueOf(distance);

        assertThat(overFiftyPolicy.calculateOverFare(distance)).isEqualTo(2_250);
    }
}
