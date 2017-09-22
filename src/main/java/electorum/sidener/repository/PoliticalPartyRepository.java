package electorum.sidener.repository;

import electorum.sidener.domain.PoliticalParty;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the PoliticalParty entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PoliticalPartyRepository extends JpaRepository<PoliticalParty, Long> {

}
