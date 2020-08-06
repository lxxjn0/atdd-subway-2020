package wooteco.subway.maps.map.dto;

import static wooteco.subway.maps.line.domain.Fare.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import wooteco.subway.maps.line.domain.Line;
import wooteco.subway.maps.map.domain.DistanceFarePolicy;
import wooteco.subway.maps.map.domain.MemberFarePolicy;
import wooteco.subway.maps.map.domain.SubwayPath;
import wooteco.subway.maps.station.domain.Station;
import wooteco.subway.maps.station.dto.StationResponse;
import wooteco.subway.members.member.domain.LoginMember;

public class PathResponseAssembler {
    public static PathResponse assemble(SubwayPath subwayPath, Map<Long, Station> stations,
            List<Line> lines, Optional<LoginMember> loginMember) {
        List<StationResponse> stationResponses = subwayPath.extractStationId().stream()
                .map(it -> StationResponse.of(stations.get(it)))
                .collect(Collectors.toList());

        int distance = subwayPath.calculateDistance();
        double fare = calculateFare(distance, lines, loginMember);

        return new PathResponse(stationResponses, subwayPath.calculateDuration(), distance,
                discountFare(loginMember, fare));
    }

    private static double calculateFare(int distance, List<Line> lines,
            Optional<LoginMember> loginMember) {
        DistanceFarePolicy distanceFarePolicy = DistanceFarePolicy.valueOf(distance);
        double extraDistanceFare = distanceFarePolicy.calculateOverFare(distance);
        double extraLineFare = lines.stream()
                .mapToDouble(line -> line.getExtraFare().getFare())
                .max()
                .orElse(DEFAULT_LINE_FARE);

        return extraDistanceFare + extraLineFare;
    }

    private static double discountFare(Optional<LoginMember> loginMember, double fare) {
        if (loginMember.isPresent()) {
            MemberFarePolicy memberFarePolicy = MemberFarePolicy.valueOf(
                    loginMember.get().getAge());
            return memberFarePolicy.calculateDiscount(fare);
        }
        return fare;
    }
}
