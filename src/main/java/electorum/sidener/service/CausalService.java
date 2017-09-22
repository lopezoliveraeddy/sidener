package electorum.sidener.service;

import electorum.sidener.service.dto.CausalDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Causal.
 */
public interface CausalService {

    /**
     * Save a causal.
     *
     * @param causalDTO the entity to save
     * @return the persisted entity
     */
    CausalDTO save(CausalDTO causalDTO);

    /**
     *  Get all the causals.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<CausalDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" causal.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    CausalDTO findOne(Long id);

    /**
     *  Delete the "id" causal.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the causal corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<CausalDTO> search(String query, Pageable pageable);
}
