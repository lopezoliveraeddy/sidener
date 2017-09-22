package electorum.sidener.service.impl;

import electorum.sidener.service.PoliticalPartyService;
import electorum.sidener.domain.PoliticalParty;
import electorum.sidener.repository.PoliticalPartyRepository;
import electorum.sidener.repository.search.PoliticalPartySearchRepository;
import electorum.sidener.service.dto.PoliticalPartyDTO;
import electorum.sidener.service.mapper.PoliticalPartyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing PoliticalParty.
 */
@Service
@Transactional
public class PoliticalPartyServiceImpl implements PoliticalPartyService{

    private final Logger log = LoggerFactory.getLogger(PoliticalPartyServiceImpl.class);

    private final PoliticalPartyRepository politicalPartyRepository;

    private final PoliticalPartyMapper politicalPartyMapper;

    private final PoliticalPartySearchRepository politicalPartySearchRepository;

    public PoliticalPartyServiceImpl(PoliticalPartyRepository politicalPartyRepository, PoliticalPartyMapper politicalPartyMapper, PoliticalPartySearchRepository politicalPartySearchRepository) {
        this.politicalPartyRepository = politicalPartyRepository;
        this.politicalPartyMapper = politicalPartyMapper;
        this.politicalPartySearchRepository = politicalPartySearchRepository;
    }

    /**
     * Save a politicalParty.
     *
     * @param politicalPartyDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public PoliticalPartyDTO save(PoliticalPartyDTO politicalPartyDTO) {
        log.debug("Request to save PoliticalParty : {}", politicalPartyDTO);
        PoliticalParty politicalParty = politicalPartyMapper.toEntity(politicalPartyDTO);
        politicalParty = politicalPartyRepository.save(politicalParty);
        PoliticalPartyDTO result = politicalPartyMapper.toDto(politicalParty);
        politicalPartySearchRepository.save(politicalParty);
        return result;
    }

    /**
     *  Get all the politicalParties.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PoliticalPartyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PoliticalParties");
        return politicalPartyRepository.findAll(pageable)
            .map(politicalPartyMapper::toDto);
    }

    /**
     *  Get one politicalParty by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public PoliticalPartyDTO findOne(Long id) {
        log.debug("Request to get PoliticalParty : {}", id);
        PoliticalParty politicalParty = politicalPartyRepository.findOne(id);
        return politicalPartyMapper.toDto(politicalParty);
    }

    /**
     *  Delete the  politicalParty by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PoliticalParty : {}", id);
        politicalPartyRepository.delete(id);
        politicalPartySearchRepository.delete(id);
    }

    /**
     * Search for the politicalParty corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PoliticalPartyDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PoliticalParties for query {}", query);
        Page<PoliticalParty> result = politicalPartySearchRepository.search(queryStringQuery(query), pageable);
        return result.map(politicalPartyMapper::toDto);
    }
}
