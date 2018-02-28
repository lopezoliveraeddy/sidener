package electorum.sidener.repository;

import electorum.sidener.domain.District;
import electorum.sidener.service.dto.DistrictDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the District entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DistrictRepository extends JpaRepository<District, Long> {

    Page<District> findByElectionId(Long idElection, Pageable pageable);

    Long countByElectionIdAndDistrictWonIsTrue(Long idElection);

    Long countByElectionIdAndDistrictWonIsFalse(Long idElection);

    // @Query("select count(district) from District district where district.election_id =:election_id and district.district_won =:district_won")
    // District districtsWon(@Param("election_id") Long election_id, @Param("district_won") Boolean district_won);


}
