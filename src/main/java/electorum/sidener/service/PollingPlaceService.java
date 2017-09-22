package electorum.sidener.service;

import electorum.sidener.service.dto.PollingPlaceDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing PollingPlace.
 */
public interface PollingPlaceService {

    /**
     * Save a pollingPlace.
     *
     * @param pollingPlaceDTO the entity to save
     * @return the persisted entity
     */
    PollingPlaceDTO save(PollingPlaceDTO pollingPlaceDTO);

    /**
     *  Get all the pollingPlaces.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<PollingPlaceDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" pollingPlace.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    PollingPlaceDTO findOne(Long id);

    /**
     *  Delete the "id" pollingPlace.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the pollingPlace corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<PollingPlaceDTO> search(String query, Pageable pageable);
}
