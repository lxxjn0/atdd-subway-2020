package wooteco.subway.members.favorite.application;

import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.maps.station.application.StationService;
import wooteco.subway.maps.station.domain.Station;
import wooteco.subway.maps.station.dto.StationResponse;
import wooteco.subway.members.favorite.domain.Favorite;
import wooteco.subway.members.favorite.domain.FavoriteRepository;
import wooteco.subway.members.favorite.dto.FavoriteRequest;
import wooteco.subway.members.favorite.dto.FavoriteResponse;
import wooteco.subway.members.member.domain.LoginMember;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class FavoriteService {
    private FavoriteRepository favoriteRepository;
    private StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
    }

    public void createFavorite(LoginMember loginMember, FavoriteRequest request) {
        Favorite favorite = new Favorite(loginMember.getId(), request.getSource(), request.getTarget());
        favoriteRepository.save(favorite);
    }

    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        List<Favorite> favorites = favoriteRepository.findByMemberId(loginMember.getId());
        Map<Long, Station> stations = extractStations(favorites);

        return favorites.stream()
                .map(it -> FavoriteResponse.of(
                        it,
                        StationResponse.of(stations.get(it.getSourceStationId())),
                        StationResponse.of(stations.get(it.getTargetStationId()))))
                .collect(Collectors.toList());
    }

    public void deleteFavorite(LoginMember loginMember, Long id) {
        Favorite favorite = favoriteRepository.findById(id).orElseThrow(RuntimeException::new);
        if (!favorite.isCreatedBy(loginMember.getId())) {
            throw new RuntimeException();
        }
        favoriteRepository.deleteById(id);
    }

    private Map<Long, Station> extractStations(List<Favorite> favorites) {
        List<Long> stationIds = extractStationIds(favorites);
        return stationService.findStationsByIds(stationIds);
    }

    private List<Long> extractStationIds(List<Favorite> favorites) {
        List<Long> stationIds = new ArrayList<>();
        for (Favorite favorite : favorites) {
            stationIds.add(favorite.getSourceStationId());
            stationIds.add(favorite.getTargetStationId());
        }
        return stationIds;
    }
}

