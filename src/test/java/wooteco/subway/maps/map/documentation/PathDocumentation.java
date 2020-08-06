package wooteco.subway.maps.map.documentation;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.web.context.WebApplicationContext;

import wooteco.subway.common.documentation.Documentation;
import wooteco.subway.maps.line.dto.LineResponse;
import wooteco.subway.maps.line.dto.LineStationResponse;
import wooteco.subway.maps.map.application.MapService;
import wooteco.subway.maps.map.dto.MapResponse;
import wooteco.subway.maps.map.dto.PathResponse;
import wooteco.subway.maps.map.ui.MapController;
import wooteco.subway.maps.station.dto.StationResponse;

@WebMvcTest(controllers = {MapController.class})
public class PathDocumentation extends Documentation {
    @Autowired
    private MapController mapController;
    @MockBean
    private MapService mapService;

    @BeforeEach
    public void setUp(WebApplicationContext context,
            RestDocumentationContextProvider restDocumentation) {
        super.setUp(context, restDocumentation);
    }

    @Test
    void findPath() {
        PathResponse pathResponse = new PathResponse(
                Lists.newArrayList(
                        new StationResponse(3L, "강남역", LocalDateTime.now(), LocalDateTime.now()),
                        new StationResponse(4L, "역삼역", LocalDateTime.now(), LocalDateTime.now())),
                4, 3, 1_250);
        when(mapService.findPath(any(), any(), any())).thenReturn(pathResponse);

        given().log().all().
                param("source", 1L).
                param("target", 3L).
                param("type", "DISTANCE").
                contentType(MediaType.APPLICATION_JSON_VALUE).
                when().
                get("/paths").
                then().
                log().all().
                apply(document("paths/find",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParameters(
                                parameterWithName("source").description("출발역 아이디"),
                                parameterWithName("target").description("도착역 아이디"),
                                parameterWithName("type").description("경로 검색 타입")),
                        responseFields(
                                fieldWithPath("stations[].id").description("탐색한 경로의 지하철역의 아이디"),
                                fieldWithPath("stations[].name").description("탐색한 경로의 지하철역의 이름"),
                                fieldWithPath("stations[].createdDate").description(
                                        "탐색한 경로의 지하철역의 생성 시간"),
                                fieldWithPath("stations[].modifiedDate").description(
                                        "탐색한 경로의 지하철역의 수정 시간"),
                                fieldWithPath("duration").description("소요 시간"),
                                fieldWithPath("distance").description("이동 거리"),
                                fieldWithPath("fare").description("요금")))).
                extract();
    }

    @Test
    void findMap() {
        LineStationResponse 출발_강남 = new LineStationResponse(
                new StationResponse(1L, "강남역", LocalDateTime.now(), LocalDateTime.now()), null, 1L,
                0, 0);
        LineStationResponse 강남_양재 = new LineStationResponse(
                new StationResponse(2L, "양재역", LocalDateTime.now(), LocalDateTime.now()), 1L, 1L, 2,
                2);
        LineStationResponse 출발_교대 = new LineStationResponse(
                new StationResponse(3L, "교대역", LocalDateTime.now(), LocalDateTime.now()), null, 2L,
                0, 0);
        LineStationResponse 교대_강남 = new LineStationResponse(
                new StationResponse(1L, "강남역", LocalDateTime.now(), LocalDateTime.now()), 3L, 2L, 2,
                1);

        LineResponse 신분당선 = new LineResponse(1L, "신분당선", "red lighten-1", LocalTime.now(),
                LocalTime.now(), 10, 0, Lists.newArrayList(출발_강남, 강남_양재), LocalDateTime.now(),
                LocalDateTime.now());
        LineResponse 이호선 = new LineResponse(2L, "2호선", "green lighten-1", LocalTime.now(),
                LocalTime.now(), 10, 400, Lists.newArrayList(출발_교대, 교대_강남), LocalDateTime.now(),
                LocalDateTime.now());
        MapResponse mapResponse = new MapResponse(Lists.newArrayList(신분당선, 이호선));

        when(mapService.findMap()).thenReturn(mapResponse);

        given().log().all()
                .when()
                .get("/maps")
                .then()
                .log()
                .all()
                .apply(document("maps/find",
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("lineResponses[].id").description(
                                        "지하철 노선의 아이디"),
                                fieldWithPath("lineResponses[].name").description(
                                        "지하철 노선의 이름"),
                                fieldWithPath("lineResponses[].color").description(
                                        "지하철 노선의 색상"),
                                fieldWithPath("lineResponses[].startTime").description(
                                        "지하철 노선의 첫차 시간"),
                                fieldWithPath("lineResponses[].endTime").description(
                                        "지하철 노선의 막차 시간"),
                                fieldWithPath("lineResponses[].intervalTime").description(
                                        "지하철 노선의 배차 시간"),
                                fieldWithPath("lineResponses[].extraFare").description(
                                        "지하철 노선의 추가 요금"),
                                fieldWithPath(
                                        "lineResponses[].stations[].station.id").description(
                                        "지하철 노선 내 경로의 현재역의 아이디"),
                                fieldWithPath(
                                        "lineResponses[].stations[].station.name").description(
                                        "지하철 노선 내 경로의 현재역의 이름"),
                                fieldWithPath(
                                        "lineResponses[].stations[].station.createdDate").description(
                                        "지하철 노선 내 경로의 현재역의 생성 시간"),
                                fieldWithPath(
                                        "lineResponses[].stations[].station.modifiedDate").description(
                                        "지하철 노선 내 경로의 현재역의 수정 시간"),
                                fieldWithPath(
                                        "lineResponses[].stations[].preStationId").description(
                                        "지하철 노선 내 경로의 이전역의 아이디").optional(),
                                fieldWithPath(
                                        "lineResponses[].stations[].lineId").description(
                                        "지하철 노선 내 경로의 노선의 아이디"),
                                fieldWithPath(
                                        "lineResponses[].stations[].distance").description(
                                        "지하철 노선 내 경로의 노선의 이동 거리"),
                                fieldWithPath(
                                        "lineResponses[].stations[].duration").description(
                                        "지하철 노선 내 경로의 노선의 소요 시간"),
                                fieldWithPath("lineResponses[].createdDate").description(
                                        "지하철 노선의 생성 시간"),
                                fieldWithPath("lineResponses[].modifiedDate").description(
                                        "지하철 노선의 수정 시간")))).
                extract();
    }
}
