package electorum.sidener.repository;

import electorum.sidener.domain.DetectorCausals;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the DetectorCausals entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DetectorCausalsRepository extends JpaRepository<DetectorCausals, Long> {

}
