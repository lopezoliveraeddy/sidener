package electorum.sidener.repository;

import electorum.sidener.domain.Coalition;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Coalition entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CoalitionRepository extends JpaRepository<Coalition, Long> {
    @Query("select distinct coalition from Coalition coalition left join fetch coalition.politicalParties")
    List<Coalition> findAllWithEagerRelationships();

    @Query("select coalition from Coalition coalition left join fetch coalition.politicalParties where coalition.id =:id")
    Coalition findOneWithEagerRelationships(@Param("id") Long id);

}
