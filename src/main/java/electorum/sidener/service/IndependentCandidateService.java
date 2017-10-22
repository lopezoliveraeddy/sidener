package electorum.sidener.service;

import electorum.sidener.domain.IndependentCandidate;
import electorum.sidener.repository.IndependentCandidateRepository;
import electorum.sidener.repository.search.IndependentCandidateSearchRepository;
import electorum.sidener.service.dto.IndependentCandidateDTO;
import electorum.sidener.service.mapper.IndependentCandidateMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing IndependentCandidate.
 */
@Service
@Transactional
public class IndependentCandidateService {

    private final Logger log = LoggerFactory.getLogger(IndependentCandidateService.class);

    private final IndependentCandidateRepository independentCandidateRepository;

    private final IndependentCandidateMapper independentCandidateMapper;

    private final IndependentCandidateSearchRepository independentCandidateSearchRepository;

    public IndependentCandidateService(IndependentCandidateRepository independentCandidateRepository, IndependentCandidateMapper independentCandidateMapper, IndependentCandidateSearchRepository independentCandidateSearchRepository) {
        this.independentCandidateRepository = independentCandidateRepository;
        this.independentCandidateMapper = independentCandidateMapper;
        this.independentCandidateSearchRepository = independentCandidateSearchRepository;
    }

    /**
     * Save a independentCandidate.
     *
     * @param independentCandidateDTO the entity to save
     * @return the persisted entity
     */
    public IndependentCandidateDTO save(IndependentCandidateDTO independentCandidateDTO) {
        log.debug("Request to save IndependentCandidate : {}", independentCandidateDTO);
        IndependentCandidate independentCandidate = independentCandidateMapper.toEntity(independentCandidateDTO);
        independentCandidate = independentCandidateRepository.save(independentCandidate);
        IndependentCandidateDTO result = independentCandidateMapper.toDto(independentCandidate);
        independentCandidateSearchRepository.save(independentCandidate);
        return result;
    }

    /**
     *  Get all the independentCandidates.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<IndependentCandidateDTO> findAll(Pageable pageable) {
        log.debug("Request to get all IndependentCandidates");
        return independentCandidateRepository.findAll(pageable)
            .map(independentCandidateMapper::toDto);
    }

    /**
     *  Get one independentCandidate by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public IndependentCandidateDTO findOne(Long id) {
        log.debug("Request to get IndependentCandidate : {}", id);
        IndependentCandidate independentCandidate = independentCandidateRepository.findOne(id);
        return independentCandidateMapper.toDto(independentCandidate);
    }

    /**
     *  Delete the  independentCandidate by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete IndependentCandidate : {}", id);
        independentCandidateRepository.delete(id);
        independentCandidateSearchRepository.delete(id);
    }

    /**
     * Search for the independentCandidate corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<IndependentCandidateDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of IndependentCandidates for query {}", query);
        Page<IndependentCandidate> result = independentCandidateSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(independentCandidateMapper::toDto);
    }
}
