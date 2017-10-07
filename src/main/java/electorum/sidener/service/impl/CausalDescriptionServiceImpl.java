package electorum.sidener.service.impl;

import electorum.sidener.service.CausalDescriptionService;
import electorum.sidener.domain.CausalDescription;
import electorum.sidener.repository.CausalDescriptionRepository;
import electorum.sidener.repository.search.CausalDescriptionSearchRepository;
import electorum.sidener.service.dto.CausalDescriptionDTO;
import electorum.sidener.service.mapper.CausalDescriptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing CausalDescription.
 */
@Service
@Transactional
public class CausalDescriptionServiceImpl implements CausalDescriptionService{

    private final Logger log = LoggerFactory.getLogger(CausalDescriptionServiceImpl.class);

    private final CausalDescriptionRepository causalDescriptionRepository;

    private final CausalDescriptionMapper causalDescriptionMapper;

    private final CausalDescriptionSearchRepository causalDescriptionSearchRepository;

    public CausalDescriptionServiceImpl(CausalDescriptionRepository causalDescriptionRepository, CausalDescriptionMapper causalDescriptionMapper, CausalDescriptionSearchRepository causalDescriptionSearchRepository) {
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
    @Override
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
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<CausalDescriptionDTO> findAll() {
        log.debug("Request to get all CausalDescriptions");
        return causalDescriptionRepository.findAll().stream()
            .map(causalDescriptionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one causalDescription by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
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
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CausalDescription : {}", id);
        causalDescriptionRepository.delete(id);
        causalDescriptionSearchRepository.delete(id);
    }

    /**
     * Search for the causalDescription corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<CausalDescriptionDTO> search(String query) {
        log.debug("Request to search CausalDescriptions for query {}", query);
        return StreamSupport
            .stream(causalDescriptionSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(causalDescriptionMapper::toDto)
            .collect(Collectors.toList());
    }
}
