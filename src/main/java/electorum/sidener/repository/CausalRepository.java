package electorum.sidener.repository;

import electorum.sidener.domain.Causal;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Causal entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CausalRepository extends JpaRepository<Causal, Long> {

}
