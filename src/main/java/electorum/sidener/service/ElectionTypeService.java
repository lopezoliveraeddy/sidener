package electorum.sidener.service;

import electorum.sidener.service.dto.ElectionTypeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing ElectionType.
 */
public interface ElectionTypeService {

    /**
     * Save a electionType.
     *
     * @param electionTypeDTO the entity to save
     * @return the persisted entity
     */
    ElectionTypeDTO save(ElectionTypeDTO electionTypeDTO);

    /**
     *  Get all the electionTypes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ElectionTypeDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" electionType.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ElectionTypeDTO findOne(Long id);

    /**
     *  Delete the "id" electionType.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the electionType corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ElectionTypeDTO> search(String query, Pageable pageable);
}
