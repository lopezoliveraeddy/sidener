package electorum.sidener.service;

import electorum.sidener.domain.CausalDescription;
import electorum.sidener.repository.CausalDescriptionRepository;
import electorum.sidener.repository.search.CausalDescriptionSearchRepository;
import electorum.sidener.service.dto.CausalDescriptionDTO;
import electorum.sidener.service.mapper.CausalDescriptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing CausalDescription.
 */
@Service
@Transactional
public class CausalDescriptionService {

    private final Logger log = LoggerFactory.getLogger(CausalDescriptionService.class);

    private final CausalDescriptionRepository causalDescriptionRepository;

    private final CausalDescriptionMapper causalDescriptionMapper;

    private final CausalDescriptionSearchRepository causalDescriptionSearchRepository;

    public CausalDescriptionService(CausalDescriptionRepository causalDescriptionRepository, CausalDescriptionMapper causalDescriptionMapper, CausalDescriptionSearchRepository causalDescriptionSearchRepository) {
        this.causalDescriptionRepository = causalDescriptionRepository;
        this.causalDescriptionMapper = causalDescriptionMapper;
        this.causalDescriptionSearchRepository = causalDescriptionSearchRepository;
    }

    /**
     * Save a causalDescription.
     *
     * @param causalDescriptionDTO the entity to save
     * @return the persisted entity
     */
    public CausalDescriptionDTO save(CausalDescriptionDTO causalDescriptionDTO) {
        log.debug("Request to save CausalDescription : {}", causalDescriptionDTO);
        CausalDescription causalDescription = causalDescriptionMapper.toEntity(causalDescriptionDTO);
        causalDescription = causalDescriptionRepository.save(causalDescription);
        CausalDescriptionDTO result = causalDescriptionMapper.toDto(causalDescription);
        causalDescriptionSearchRepository.save(causalDescription);
        return result;
    }

    /**
     *  Get all the causalDescriptions.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CausalDescriptionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CausalDescriptions");
        return causalDescriptionRepository.findAll(pageable)
            .map(causalDescriptionMapper::toDto);
    }

    /**
     *  Get one causalDescription by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public CausalDescriptionDTO findOne(Long id) {
        log.debug("Request to get CausalDescription : {}", id);
        CausalDescription causalDescription = causalDescriptionRepository.findOne(id);
        return causalDescriptionMapper.toDto(causalDescription);
    }

    /**
     *  Delete the  causalDescription by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete CausalDescription : {}", id);
        causalDescriptionRepository.delete(id);
        causalDescriptionSearchRepository.delete(id);
    }

    /**
     * Search for the causalDescription corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CausalDescriptionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CausalDescriptions for query {}", query);
        Page<CausalDescription> result = causalDescriptionSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(causalDescriptionMapper::toDto);
    }
}
