package wooteco.subway.maps.map.dto;

import java.util.List;

import wooteco.subway.maps.station.dto.StationResponse;

public class PathResponse {
    private List<StationResponse> stations;
    private int duration;
    private int distance;
    private long fare;

    public PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, int duration, int distance, long fare) {
        this.stations = stations;
        this.duration = duration;
        this.distance = distance;
        this.fare = fare;
    }

    public PathResponse(List<StationResponse> stations, int duration, int distance) {
        this.stations = stations;
        this.duration = duration;
        this.distance = distance;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDuration() {
        return duration;
    }

    public int getDistance() {
        return distance;
    }

    public long getFare() {
        return fare;
    }
}
