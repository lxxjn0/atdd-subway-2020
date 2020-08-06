package wooteco.subway.maps.map.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberFarePolicyTest {
    @DisplayName("나이에 따른 MemberFarePolicy를 반환한다.")
    @Test
    void valueOf() {
        MemberFarePolicy baby = MemberFarePolicy.valueOf(0);
        MemberFarePolicy child = MemberFarePolicy.valueOf(8);
        MemberFarePolicy youth = MemberFarePolicy.valueOf(16);
        MemberFarePolicy adult = MemberFarePolicy.valueOf(27);

        assertAll(
                () -> assertThat(baby).isEqualTo(MemberFarePolicy.BABY),
                () -> assertThat(child).isEqualTo(MemberFarePolicy.CHILD),
                () -> assertThat(youth).isEqualTo(MemberFarePolicy.YOUTH),
                () -> assertThat(adult).isEqualTo(MemberFarePolicy.ADULT)
        );
    }

    @DisplayName("MemberFarePolicy에 따른 할인 정책을 적용한 가격을 반환받는다.")
    @Test
    void calculateDiscount() {
        double fare = 2_000;

        MemberFarePolicy baby = MemberFarePolicy.valueOf(0);
        MemberFarePolicy child = MemberFarePolicy.valueOf(8);
        MemberFarePolicy youth = MemberFarePolicy.valueOf(16);
        MemberFarePolicy adult = MemberFarePolicy.valueOf(27);

        assertAll(
                () -> assertThat(baby.calculateDiscount(fare)).isEqualTo(0),
                () -> assertThat(child.calculateDiscount(fare)).isEqualTo(1_000),
                () -> assertThat(youth.calculateDiscount(fare)).isEqualTo(1_600),
                () -> assertThat(adult.calculateDiscount(fare)).isEqualTo(2_000)
        );
    }
}
