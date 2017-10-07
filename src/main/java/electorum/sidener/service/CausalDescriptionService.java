package electorum.sidener.service;

import electorum.sidener.service.dto.CausalDescriptionDTO;
import java.util.List;

/**
 * Service Interface for managing CausalDescription.
 */
public interface CausalDescriptionService {

    /**
     * Save a causalDescription.
     *
     * @param causalDescriptionDTO the entity to save
     * @return the persisted entity
     */
    CausalDescriptionDTO save(CausalDescriptionDTO causalDescriptionDTO);

    /**
     *  Get all the causalDescriptions.
     *
     *  @return the list of entities
     */
    List<CausalDescriptionDTO> findAll();

    /**
     *  Get the "id" causalDescription.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    CausalDescriptionDTO findOne(Long id);

    /**
     *  Delete the "id" causalDescription.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the causalDescription corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @return the list of entities
     */
    List<CausalDescriptionDTO> search(String query);
}
