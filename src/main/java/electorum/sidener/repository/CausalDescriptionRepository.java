package electorum.sidener.repository;

import electorum.sidener.domain.CausalDescription;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the CausalDescription entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CausalDescriptionRepository extends JpaRepository<CausalDescription, Long> {

}
