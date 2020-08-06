package wooteco.subway.maps.map.dto;

import static wooteco.subway.maps.line.domain.Fare.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import wooteco.subway.maps.line.domain.Line;
import wooteco.subway.maps.map.domain.DistanceFare;
import wooteco.subway.maps.map.domain.SubwayPath;
import wooteco.subway.maps.station.domain.Station;
import wooteco.subway.maps.station.dto.StationResponse;

public class PathResponseAssembler {
    public static PathResponse assemble(SubwayPath subwayPath, Map<Long, Station> stations,
            List<Line> lines) {
        List<StationResponse> stationResponses = subwayPath.extractStationId().stream()
                .map(it -> StationResponse.of(stations.get(it)))
                .collect(Collectors.toList());

        int distance = subwayPath.calculateDistance();

        return new PathResponse(stationResponses, subwayPath.calculateDuration(), distance,
                calculateFare(distance, lines));
    }

    private static double calculateFare(int distance, List<Line> lines) {
        DistanceFare distanceFare = DistanceFare.valueOf(distance);
        double extraDistanceFare = distanceFare.calculateOverFare(distance);

        double extraLineFare = lines.stream()
                .mapToDouble(line -> line.getExtraFare().getFare())
                .max()
                .orElse(DEFAULT_LINE_FARE);

        return extraDistanceFare + extraLineFare;
    }
}
