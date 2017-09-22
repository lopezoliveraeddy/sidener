package electorum.sidener.service.impl;

import electorum.sidener.service.CoalitionService;
import electorum.sidener.domain.Coalition;
import electorum.sidener.repository.CoalitionRepository;
import electorum.sidener.repository.search.CoalitionSearchRepository;
import electorum.sidener.service.dto.CoalitionDTO;
import electorum.sidener.service.mapper.CoalitionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Coalition.
 */
@Service
@Transactional
public class CoalitionServiceImpl implements CoalitionService{

    private final Logger log = LoggerFactory.getLogger(CoalitionServiceImpl.class);

    private final CoalitionRepository coalitionRepository;

    private final CoalitionMapper coalitionMapper;

    private final CoalitionSearchRepository coalitionSearchRepository;

    public CoalitionServiceImpl(CoalitionRepository coalitionRepository, CoalitionMapper coalitionMapper, CoalitionSearchRepository coalitionSearchRepository) {
        this.coalitionRepository = coalitionRepository;
        this.coalitionMapper = coalitionMapper;
        this.coalitionSearchRepository = coalitionSearchRepository;
    }

    /**
     * Save a coalition.
     *
     * @param coalitionDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CoalitionDTO save(CoalitionDTO coalitionDTO) {
        log.debug("Request to save Coalition : {}", coalitionDTO);
        Coalition coalition = coalitionMapper.toEntity(coalitionDTO);
        coalition = coalitionRepository.save(coalition);
        CoalitionDTO result = coalitionMapper.toDto(coalition);
        coalitionSearchRepository.save(coalition);
        return result;
    }

    /**
     *  Get all the coalitions.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CoalitionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Coalitions");
        return coalitionRepository.findAll(pageable)
            .map(coalitionMapper::toDto);
    }

    /**
     *  Get one coalition by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public CoalitionDTO findOne(Long id) {
        log.debug("Request to get Coalition : {}", id);
        Coalition coalition = coalitionRepository.findOneWithEagerRelationships(id);
        return coalitionMapper.toDto(coalition);
    }

    /**
     *  Delete the  coalition by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Coalition : {}", id);
        coalitionRepository.delete(id);
        coalitionSearchRepository.delete(id);
    }

    /**
     * Search for the coalition corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CoalitionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Coalitions for query {}", query);
        Page<Coalition> result = coalitionSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(coalitionMapper::toDto);
    }
}
