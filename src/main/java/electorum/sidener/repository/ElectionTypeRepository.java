package electorum.sidener.repository;

import electorum.sidener.domain.ElectionType;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ElectionType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ElectionTypeRepository extends JpaRepository<ElectionType, Long> {

}
