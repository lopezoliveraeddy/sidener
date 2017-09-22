package electorum.sidener.repository;

import electorum.sidener.domain.Election;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Election entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ElectionRepository extends JpaRepository<Election, Long> {
    @Query("select distinct election from Election election left join fetch election.politicalParties left join fetch election.independentCandidates left join fetch election.coalitions left join fetch election.causals")
    List<Election> findAllWithEagerRelationships();

    @Query("select election from Election election left join fetch election.politicalParties left join fetch election.independentCandidates left join fetch election.coalitions left join fetch election.causals where election.id =:id")
    Election findOneWithEagerRelationships(@Param("id") Long id);

}
