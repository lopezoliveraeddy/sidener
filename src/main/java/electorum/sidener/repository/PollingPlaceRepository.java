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

    @Query("select polling_place from PollingPlace polling_place left join fetch polling_place.causals where polling_place.id = :id")
    PollingPlace findOneWithEagerRelationships(@Param("id") Long id);

    Page<PollingPlace> findByDistrictId(Long districtId, Pageable pageable);

    Page<PollingPlace> findByElectionId(Long electionId, Pageable pageable);

    Long countByDistrictIdAndPollingPlaceWonIsTrue(Long districtId);

    Long countByDistrictIdAndPollingPlaceWonIsFalse(Long districtId);

    @Query("select polling_place from PollingPlace polling_place where polling_place.district.id = :districtId and polling_place.pollingPlaceWon = :pollingPlaceWon")
    Page<PollingPlace> getPollingPlacesWon(@Param("districtId") Long districtId, @Param("pollingPlaceWon") Boolean pollingPlaceWon, Pageable pageable);

    /*
    @Query("select id from PollingPlace polling_place where polling_place.distrct_id = :idDistrict and polling_place.id = (select min(polling_place_sub.id) FROM PollingPlace polling_place_sub WHERE polling_place_sub.id > :id)")
    Long nextPollingPlace(Long idDistrict, Long id);

    @Query("select id from PollingPlace polling_place where polling_place.distrct_id = :idDistrict and polling_place.id = (select min(polling_place_sub.id) FROM PollingPlace polling_place_sub WHERE polling_place_sub.id < :id)")
    Long previousPollingPlace(Long idDistrict, Long id);
    */

}
