package electorum.sidener.service;

import electorum.sidener.service.dto.ElectionPeriodDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing ElectionPeriod.
 */
public interface ElectionPeriodService {

    /**
     * Save a electionPeriod.
     *
     * @param electionPeriodDTO the entity to save
     * @return the persisted entity
     */
    ElectionPeriodDTO save(ElectionPeriodDTO electionPeriodDTO);

    /**
     *  Get all the electionPeriods.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ElectionPeriodDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" electionPeriod.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ElectionPeriodDTO findOne(Long id);

    /**
     *  Delete the "id" electionPeriod.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the electionPeriod corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ElectionPeriodDTO> search(String query, Pageable pageable);
}
