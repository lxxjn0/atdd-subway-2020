package wooteco.subway.members.favorite.documentation;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.web.context.WebApplicationContext;

import com.google.common.collect.Lists;
import wooteco.security.core.TokenResponse;
import wooteco.subway.common.documentation.Documentation;
import wooteco.subway.maps.station.dto.StationResponse;
import wooteco.subway.members.favorite.application.FavoriteService;
import wooteco.subway.members.favorite.dto.FavoriteResponse;
import wooteco.subway.members.favorite.ui.FavoriteController;

@WebMvcTest(controllers = {FavoriteController.class})
public class FavoriteDocumentation extends Documentation {
    @Autowired
    private FavoriteController favoriteController;
    @MockBean
    private FavoriteService favoriteService;

    protected TokenResponse tokenResponse;

    @BeforeEach
    public void setUp(WebApplicationContext context,
            RestDocumentationContextProvider restDocumentation) {
        super.setUp(context, restDocumentation);
        tokenResponse = new TokenResponse("token");
    }

    @Test
    void createFavorite() {
        Map<String, Object> params = new HashMap<>();
        params.put("source", 1L);
        params.put("target", 2L);

        given().log().all().
                header("Authorization", "Bearer " + tokenResponse.getAccessToken()).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                body(params).
                when().
                post("/favorites").
                then().
                log().all().
                apply(document("favorites/create",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(
                                headerWithName("Authorization").description(
                                        "Bearer auth credentials")),
                        requestFields(
                                fieldWithPath("source").type(JsonFieldType.NUMBER)
                                        .description("출발역 아이디"),
                                fieldWithPath("target").type(JsonFieldType.NUMBER)
                                        .description("도착역 아이디")))).
                extract();
    }

    @Test
    void findFavorites() {
        List<FavoriteResponse> favoriteResponses = Lists.newArrayList(
                new FavoriteResponse(1L,
                        new StationResponse(1L, "광교중앙역", LocalDateTime.now(), LocalDateTime.now()),
                        new StationResponse(2L, "잠실역", LocalDateTime.now(), LocalDateTime.now())),
                new FavoriteResponse(2L,
                        new StationResponse(3L, "강남역", LocalDateTime.now(), LocalDateTime.now()),
                        new StationResponse(4L, "역삼역", LocalDateTime.now(), LocalDateTime.now()))
        );
        when(favoriteService.findFavorites(any())).thenReturn(favoriteResponses);

        given().log().all().
                header("Authorization", "Bearer " + tokenResponse.getAccessToken()).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                get("/favorites").
                then().
                log().all().
                apply(document("favorites/find-list",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(
                                headerWithName("Authorization").description(
                                        "Bearer auth credentials")),
                        responseFields(
                                fieldWithPath("[]").type(JsonFieldType.ARRAY)
                                        .description("즐겨찾기 목록"),
                                fieldWithPath("[].id").type(JsonFieldType.NUMBER)
                                        .description("즐겨찾기 아이디"),
                                fieldWithPath("[].source").type(JsonFieldType.OBJECT)
                                        .description("출발역"),
                                fieldWithPath("[].source.id").type(JsonFieldType.NUMBER)
                                        .description("지하철역 아이디"),
                                fieldWithPath("[].source.name").type(JsonFieldType.STRING)
                                        .description("지하철역 이름"),
                                fieldWithPath("[].target").type(JsonFieldType.OBJECT)
                                        .description("도착역"),
                                fieldWithPath("[].target.id").type(JsonFieldType.NUMBER)
                                        .description("지하철역 아이디"),
                                fieldWithPath("[].target.name").type(JsonFieldType.STRING)
                                        .description("지하철역 이름")))).
                extract();
    }

    @Test
    void deleteFavorite() {
        given().
                header("Authorization", "Bearer " + tokenResponse.getAccessToken()).
                when().
                delete("/favorites/{favoriteId}", 1L).
                then().
                log().all().
                apply(document("favorites/delete",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(
                                headerWithName("Authorization").description(
                                        "Bearer auth credentials")),
                        pathParameters(
                                parameterWithName("favoriteId").description("삭제할 즐겨찾기 아이디")))).
                extract();
    }
}
