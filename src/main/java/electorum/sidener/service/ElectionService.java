package electorum.sidener.service;

import electorum.sidener.service.dto.ElectionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Election.
 */
public interface ElectionService {

    /**
     * Save a election.
     *
     * @param electionDTO the entity to save
     * @return the persisted entity
     */
    ElectionDTO save(ElectionDTO electionDTO);

    /**
     *  Get all the elections.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ElectionDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" election.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ElectionDTO findOne(Long id);

    /**
     *  Delete the "id" election.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the election corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ElectionDTO> search(String query, Pageable pageable);
}
