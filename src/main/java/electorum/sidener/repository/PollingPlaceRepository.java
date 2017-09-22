package electorum.sidener.repository;

import electorum.sidener.domain.PollingPlace;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the PollingPlace entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PollingPlaceRepository extends JpaRepository<PollingPlace, Long> {

}
