package electorum.sidener.service;

import electorum.sidener.service.dto.IndependentCandidateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing IndependentCandidate.
 */
public interface IndependentCandidateService {

    /**
     * Save a independentCandidate.
     *
     * @param independentCandidateDTO the entity to save
     * @return the persisted entity
     */
    IndependentCandidateDTO save(IndependentCandidateDTO independentCandidateDTO);

    /**
     *  Get all the independentCandidates.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<IndependentCandidateDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" independentCandidate.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    IndependentCandidateDTO findOne(Long id);

    /**
     *  Delete the "id" independentCandidate.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the independentCandidate corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<IndependentCandidateDTO> search(String query, Pageable pageable);
}
