package electorum.sidener.repository;

import electorum.sidener.domain.ElectionPeriod;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ElectionPeriod entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ElectionPeriodRepository extends JpaRepository<ElectionPeriod, Long> {

}
