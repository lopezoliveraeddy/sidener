package electorum.sidener.repository;

import electorum.sidener.domain.PollingPlace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the PollingPlace entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PollingPlaceRepository extends JpaRepository<PollingPlace, Long> {

    Page<PollingPlace> findByDistrictId(Long id, Pageable pageable);

}
