package electorum.sidener.repository;

import electorum.sidener.domain.PollingPlace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the PollingPlace entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PollingPlaceRepository extends JpaRepository<PollingPlace, Long> {
    @Query("select distinct polling_place from PollingPlace polling_place left join fetch polling_place.causals")
    List<PollingPlace> findAllWithEagerRelationships();

    @Query("select polling_place from PollingPlace polling_place left join fetch polling_place.causals where polling_place.id =:id")
    PollingPlace findOneWithEagerRelationships(@Param("id") Long id);

    Page<PollingPlace> findByDistrictId(Long id, Pageable pageable);

    Page<PollingPlace> findByElectionId(Long id, Pageable pageable);

}
