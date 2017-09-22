package electorum.sidener.service;

import electorum.sidener.service.dto.PoliticalPartyDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing PoliticalParty.
 */
public interface PoliticalPartyService {

    /**
     * Save a politicalParty.
     *
     * @param politicalPartyDTO the entity to save
     * @return the persisted entity
     */
    PoliticalPartyDTO save(PoliticalPartyDTO politicalPartyDTO);

    /**
     *  Get all the politicalParties.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<PoliticalPartyDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" politicalParty.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    PoliticalPartyDTO findOne(Long id);

    /**
     *  Delete the "id" politicalParty.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the politicalParty corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<PoliticalPartyDTO> search(String query, Pageable pageable);
}
