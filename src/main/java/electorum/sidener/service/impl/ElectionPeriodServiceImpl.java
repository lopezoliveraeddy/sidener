package electorum.sidener.service.impl;

import electorum.sidener.service.ElectionPeriodService;
import electorum.sidener.domain.ElectionPeriod;
import electorum.sidener.repository.ElectionPeriodRepository;
import electorum.sidener.repository.search.ElectionPeriodSearchRepository;
import electorum.sidener.service.dto.ElectionPeriodDTO;
import electorum.sidener.service.mapper.ElectionPeriodMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ElectionPeriod.
 */
@Service
@Transactional
public class ElectionPeriodServiceImpl implements ElectionPeriodService{

    private final Logger log = LoggerFactory.getLogger(ElectionPeriodServiceImpl.class);

    private final ElectionPeriodRepository electionPeriodRepository;

    private final ElectionPeriodMapper electionPeriodMapper;

    private final ElectionPeriodSearchRepository electionPeriodSearchRepository;

    public ElectionPeriodServiceImpl(ElectionPeriodRepository electionPeriodRepository, ElectionPeriodMapper electionPeriodMapper, ElectionPeriodSearchRepository electionPeriodSearchRepository) {
        this.electionPeriodRepository = electionPeriodRepository;
        this.electionPeriodMapper = electionPeriodMapper;
        this.electionPeriodSearchRepository = electionPeriodSearchRepository;
    }

    /**
     * Save a electionPeriod.
     *
     * @param electionPeriodDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ElectionPeriodDTO save(ElectionPeriodDTO electionPeriodDTO) {
        log.debug("Request to save ElectionPeriod : {}", electionPeriodDTO);
        ElectionPeriod electionPeriod = electionPeriodMapper.toEntity(electionPeriodDTO);
        electionPeriod = electionPeriodRepository.save(electionPeriod);
        ElectionPeriodDTO result = electionPeriodMapper.toDto(electionPeriod);
        electionPeriodSearchRepository.save(electionPeriod);
        return result;
    }

    /**
     *  Get all the electionPeriods.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ElectionPeriodDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ElectionPeriods");
        return electionPeriodRepository.findAll(pageable)
            .map(electionPeriodMapper::toDto);
    }

    /**
     *  Get one electionPeriod by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ElectionPeriodDTO findOne(Long id) {
        log.debug("Request to get ElectionPeriod : {}", id);
        ElectionPeriod electionPeriod = electionPeriodRepository.findOne(id);
        return electionPeriodMapper.toDto(electionPeriod);
    }

    /**
     *  Delete the  electionPeriod by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ElectionPeriod : {}", id);
        electionPeriodRepository.delete(id);
        electionPeriodSearchRepository.delete(id);
    }

    /**
     * Search for the electionPeriod corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ElectionPeriodDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ElectionPeriods for query {}", query);
        Page<ElectionPeriod> result = electionPeriodSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(electionPeriodMapper::toDto);
    }
}
