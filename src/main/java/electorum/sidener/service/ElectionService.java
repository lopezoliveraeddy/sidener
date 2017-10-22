package electorum.sidener.service;

import electorum.sidener.domain.Election;
import electorum.sidener.repository.ElectionRepository;
import electorum.sidener.repository.search.ElectionSearchRepository;
import electorum.sidener.service.dto.ElectionDTO;
import electorum.sidener.service.mapper.ElectionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Election.
 */
@Service
@Transactional
public class ElectionService {

    private final Logger log = LoggerFactory.getLogger(ElectionService.class);

    private final ElectionRepository electionRepository;

    private final ElectionMapper electionMapper;

    private final ElectionSearchRepository electionSearchRepository;

    public ElectionService(ElectionRepository electionRepository, ElectionMapper electionMapper, ElectionSearchRepository electionSearchRepository) {
        this.electionRepository = electionRepository;
        this.electionMapper = electionMapper;
        this.electionSearchRepository = electionSearchRepository;
    }

    /**
     * Save a election.
     *
     * @param electionDTO the entity to save
     * @return the persisted entity
     */
    public ElectionDTO save(ElectionDTO electionDTO) {
        log.debug("Request to save Election : {}", electionDTO);
        Election election = electionMapper.toEntity(electionDTO);
        election = electionRepository.save(election);
        ElectionDTO result = electionMapper.toDto(election);
        electionSearchRepository.save(election);
        return result;
    }

    /**
     *  Get all the elections.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ElectionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Elections");
        return electionRepository.findAll(pageable)
            .map(electionMapper::toDto);
    }

    /**
     *  Get one election by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ElectionDTO findOne(Long id) {
        log.debug("Request to get Election : {}", id);
        Election election = electionRepository.findOneWithEagerRelationships(id);
        return electionMapper.toDto(election);
    }

    /**
     *  Delete the  election by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Election : {}", id);
        electionRepository.delete(id);
        electionSearchRepository.delete(id);
    }

    /**
     * Search for the election corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ElectionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Elections for query {}", query);
        Page<Election> result = electionSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(electionMapper::toDto);
    }
}
