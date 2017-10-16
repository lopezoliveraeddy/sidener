package electorum.sidener.service;

import electorum.sidener.service.dto.DistrictDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing District.
 */
public interface DistrictService {

    /**
     * Save a district.
     *
     * @param districtDTO the entity to save
     * @return the persisted entity
     */
    DistrictDTO save(DistrictDTO districtDTO);

    /**
     *  Get all the districts.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<DistrictDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" district.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    DistrictDTO findOne(Long id);

    /**
     *  Delete the "id" district.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the district corresponding to the query.
     *
     *  @param query the query of the search
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<DistrictDTO> search(String query, Pageable pageable);

    /**
     *  Get districts by id election.
     *
     *  @param id the id of the entity
     *  @param pageable the pagination information
     *  @return the entity
     */

    Page<DistrictDTO> getDistrictsByIdElection(Long id, Pageable pageable);

}
