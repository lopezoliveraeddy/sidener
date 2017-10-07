package electorum.sidener.service.impl;

import electorum.sidener.service.CausalService;
import electorum.sidener.domain.Causal;
import electorum.sidener.repository.CausalRepository;
import electorum.sidener.repository.search.CausalSearchRepository;
import electorum.sidener.service.dto.CausalDTO;
import electorum.sidener.service.mapper.CausalMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Causal.
 */
@Service
@Transactional
public class CausalServiceImpl implements CausalService{

    private final Logger log = LoggerFactory.getLogger(CausalServiceImpl.class);

    private final CausalRepository causalRepository;

    private final CausalMapper causalMapper;

    private final CausalSearchRepository causalSearchRepository;

    public CausalServiceImpl(CausalRepository causalRepository, CausalMapper causalMapper, CausalSearchRepository causalSearchRepository) {
        this.causalRepository = causalRepository;
        this.causalMapper = causalMapper;
        this.causalSearchRepository = causalSearchRepository;
    }

    /**
     * Save a causal.
     *
     * @param causalDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CausalDTO save(CausalDTO causalDTO) {
        log.debug("Request to save Causal : {}", causalDTO);
        Causal causal = causalMapper.toEntity(causalDTO);
        causal = causalRepository.save(causal);
        CausalDTO result = causalMapper.toDto(causal);
        causalSearchRepository.save(causal);
        return result;
    }

    /**
     *  Get all the causals.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CausalDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Causals");
        return causalRepository.findAll(pageable)
            .map(causalMapper::toDto);
    }

    /**
     *  Get one causal by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public CausalDTO findOne(Long id) {
        log.debug("Request to get Causal : {}", id);
        Causal causal = causalRepository.findOneWithEagerRelationships(id);
        return causalMapper.toDto(causal);
    }

    /**
     *  Delete the  causal by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Causal : {}", id);
        causalRepository.delete(id);
        causalSearchRepository.delete(id);
    }

    /**
     * Search for the causal corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CausalDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Causals for query {}", query);
        Page<Causal> result = causalSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(causalMapper::toDto);
    }
}
