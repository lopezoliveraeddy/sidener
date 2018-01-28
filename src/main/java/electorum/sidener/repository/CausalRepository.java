package electorum.sidener.repository;

import electorum.sidener.domain.Causal;
import electorum.sidener.domain.enumeration.CausalType;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Causal entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CausalRepository extends JpaRepository<Causal, Long> {
    @Query("select distinct causal from Causal causal left join fetch causal.causalDescriptions")
    List<Causal> findAllWithEagerRelationships();

    @Query("select causal from Causal causal left join fetch causal.causalDescriptions where causal.id =:id")
    Causal findOneWithEagerRelationships(@Param("id") Long id);

    List<Causal> findAllByTypeCausal(CausalType causalType);

}
