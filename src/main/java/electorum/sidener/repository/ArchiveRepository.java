package electorum.sidener.repository;

import electorum.sidener.domain.Archive;
import electorum.sidener.domain.enumeration.ArchiveStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Archive entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArchiveRepository extends JpaRepository<Archive, Long> {
    Page<Archive> findByStatus(ArchiveStatus status, Pageable pageable);
}
