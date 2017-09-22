package electorum.sidener.service;

import electorum.sidener.service.dto.CoalitionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Coalition.
 */
public interface CoalitionService {

    /**
     * Save a coalition.
     *
     * @param coalitionDTO the entity to save
     * @return the persisted entity
     */
    CoalitionDTO save(CoalitionDTO coalitionDTO);

    /**
     *  Get all the coalitions.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<CoalitionDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" coalition.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    CoalitionDTO findOne(Long id);

    /**
     *  Delete the "id" coalition.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the coalition corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<CoalitionDTO> search(String query, Pageable pageable);
}
