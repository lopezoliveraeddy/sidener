package electorum.sidener.service;

import electorum.sidener.domain.ElectionType;
import electorum.sidener.repository.ElectionTypeRepository;
import electorum.sidener.repository.search.ElectionTypeSearchRepository;
import electorum.sidener.service.dto.ElectionTypeDTO;
import electorum.sidener.service.mapper.ElectionTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ElectionType.
 */
@Service
@Transactional
public class ElectionTypeService {

    private final Logger log = LoggerFactory.getLogger(ElectionTypeService.class);

    private final ElectionTypeRepository electionTypeRepository;

    private final ElectionTypeMapper electionTypeMapper;

    private final ElectionTypeSearchRepository electionTypeSearchRepository;

    public ElectionTypeService(ElectionTypeRepository electionTypeRepository, ElectionTypeMapper electionTypeMapper, ElectionTypeSearchRepository electionTypeSearchRepository) {
        this.electionTypeRepository = electionTypeRepository;
        this.electionTypeMapper = electionTypeMapper;
        this.electionTypeSearchRepository = electionTypeSearchRepository;
    }

    /**
     * Save a electionType.
     *
     * @param electionTypeDTO the entity to save
     * @return the persisted entity
     */
    public ElectionTypeDTO save(ElectionTypeDTO electionTypeDTO) {
        log.debug("Request to save ElectionType : {}", electionTypeDTO);
        ElectionType electionType = electionTypeMapper.toEntity(electionTypeDTO);
        electionType = electionTypeRepository.save(electionType);
        ElectionTypeDTO result = electionTypeMapper.toDto(electionType);
        electionTypeSearchRepository.save(electionType);
        return result;
    }

    /**
     *  Get all the electionTypes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ElectionTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ElectionTypes");
        return electionTypeRepository.findAll(pageable)
            .map(electionTypeMapper::toDto);
    }

    /**
     *  Get one electionType by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ElectionTypeDTO findOne(Long id) {
        log.debug("Request to get ElectionType : {}", id);
        ElectionType electionType = electionTypeRepository.findOne(id);
        return electionTypeMapper.toDto(electionType);
    }

    /**
     *  Delete the  electionType by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ElectionType : {}", id);
        electionTypeRepository.delete(id);
        electionTypeSearchRepository.delete(id);
    }

    /**
     * Search for the electionType corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ElectionTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ElectionTypes for query {}", query);
        Page<ElectionType> result = electionTypeSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(electionTypeMapper::toDto);
    }
}
