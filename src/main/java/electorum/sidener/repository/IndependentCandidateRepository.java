package electorum.sidener.repository;

import electorum.sidener.domain.IndependentCandidate;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the IndependentCandidate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IndependentCandidateRepository extends JpaRepository<IndependentCandidate, Long> {

}
