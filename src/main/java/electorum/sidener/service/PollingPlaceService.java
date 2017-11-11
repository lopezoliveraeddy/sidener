package electorum.sidener.service;

import electorum.sidener.domain.PollingPlace;
import electorum.sidener.repository.PollingPlaceRepository;
import electorum.sidener.repository.search.PollingPlaceSearchRepository;
import electorum.sidener.service.dto.PollingPlaceDTO;
import electorum.sidener.service.mapper.PollingPlaceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing PollingPlace.
 */
@Service
@Transactional
public class PollingPlaceService {

    private final Logger log = LoggerFactory.getLogger(PollingPlaceService.class);

    private final PollingPlaceRepository pollingPlaceRepository;

    private final PollingPlaceMapper pollingPlaceMapper;

    private final PollingPlaceSearchRepository pollingPlaceSearchRepository;

    public PollingPlaceService(PollingPlaceRepository pollingPlaceRepository, PollingPlaceMapper pollingPlaceMapper, PollingPlaceSearchRepository pollingPlaceSearchRepository) {
        this.pollingPlaceRepository = pollingPlaceRepository;
        this.pollingPlaceMapper = pollingPlaceMapper;
        this.pollingPlaceSearchRepository = pollingPlaceSearchRepository;
    }

    /**
     * Save a pollingPlace.
     *
     * @param pollingPlaceDTO the entity to save
     * @return the persisted entity
     */
    public PollingPlaceDTO save(PollingPlaceDTO pollingPlaceDTO) {
        log.debug("Request to save PollingPlace : {}", pollingPlaceDTO);
        PollingPlace pollingPlace = pollingPlaceMapper.toEntity(pollingPlaceDTO);
        pollingPlace = pollingPlaceRepository.save(pollingPlace);
        PollingPlaceDTO result = pollingPlaceMapper.toDto(pollingPlace);
        pollingPlaceSearchRepository.save(pollingPlace);
        return result;
    }

    /**
     *  Get all the pollingPlaces.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PollingPlaceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PollingPlaces");
        return pollingPlaceRepository.findAll(pageable)
            .map(pollingPlaceMapper::toDto);
    }

    /**
     *  Get one pollingPlace by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public PollingPlaceDTO findOne(Long id) {
        log.debug("Request to get PollingPlace : {}", id);
        PollingPlace pollingPlace = pollingPlaceRepository.findOneWithEagerRelationships(id);
        return pollingPlaceMapper.toDto(pollingPlace);
    }

    /**
     *  Delete the  pollingPlace by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete PollingPlace : {}", id);
        pollingPlaceRepository.delete(id);
        pollingPlaceSearchRepository.delete(id);
    }

    /**
     * Search for the pollingPlace corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PollingPlaceDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PollingPlaces for query {}", query);
        Page<PollingPlace> result = pollingPlaceSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(pollingPlaceMapper::toDto);
    }

    /**
     *  Get pollingPlaces of district by id.
     *
     *  @param id the id of the entity
     *  @param pageable the pagination information
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Page<PollingPlaceDTO> getPollingPlacesByIdDistrict(Long id, Pageable pageable) {
        log.debug("Request to get PollingPlaces by District : {}", id);
        Page<PollingPlace> result = pollingPlaceRepository.findByDistrictId(id, pageable);
        return result.map(pollingPlaceMapper::toDto);
    }
}
