package wooteco.subway.maps.line.domain;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LineRepository extends JpaRepository<Line, Long> {
    @Override
    List<Line> findAll();

    List<Line> findAllByIdIn(Collection<Long> id);
}
