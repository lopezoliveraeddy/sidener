package electorum.sidener.service;

import electorum.sidener.domain.PollingPlace;
import electorum.sidener.repository.PollingPlaceRepository;
import electorum.sidener.repository.search.PollingPlaceSearchRepository;
import electorum.sidener.service.dto.*;
import electorum.sidener.service.mapper.PollingPlaceMapper;
import electorum.sidener.service.mapper.PollingPlaceRecountMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;

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

    private final PollingPlaceRecountMapper pollingPlaceRecountMapper;

    private final PollingPlaceSearchRepository pollingPlaceSearchRepository;

    public PollingPlaceService(PollingPlaceRepository pollingPlaceRepository, PollingPlaceMapper pollingPlaceMapper, PollingPlaceRecountMapper pollingPlaceRecountMapper,  PollingPlaceSearchRepository pollingPlaceSearchRepository) {
        this.pollingPlaceRepository = pollingPlaceRepository;
        this.pollingPlaceMapper = pollingPlaceMapper;
        this.pollingPlaceRecountMapper = pollingPlaceRecountMapper;
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
     *  @param districtId the "id" of the district
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PollingPlaceRecountDTO> getPollingPlacesByDistrictId(Long districtId, Pageable pageable) {
        log.debug("Request to get PollingPlaces by District : {}", districtId);
        Page<PollingPlaceDTO> page = pollingPlaceRepository.findByDistrictId(districtId, pageable).map(pollingPlaceMapper::toDto);
        Page<PollingPlaceRecountDTO> resultPage = resultsRecountDTO(page, pageable);
        return resultPage;
    }

    private Page<PollingPlaceRecountDTO> resultsRecountDTO(Page<PollingPlaceDTO> page, Pageable pageable){
        List<PollingPlaceRecountDTO> content = new ArrayList<>();
        for (PollingPlaceDTO pollingPlaceDTO : page) {
            PollingPlaceRecountDTO pollingPlace = pollingPlaceRecountMapper.toDto(pollingPlaceDTO);
            if(pollingPlace.getId() != null){
                if (pollingPlace.getTotalVotes() != null && pollingPlace.getTotalFirstPlace() != null && pollingPlace.getTotalSecondPlace() != null) {
                    pollingPlace.setDifference(pollingPlace.getTotalFirstPlace() - pollingPlace.getTotalSecondPlace());
                    pollingPlace.setPercentageFirstPlace(((pollingPlace.getTotalFirstPlace().doubleValue() / pollingPlace.getTotalVotes().doubleValue()) * 100));
                    pollingPlace.setPercentageSecondPlace(((pollingPlace.getTotalSecondPlace().doubleValue() / pollingPlace.getTotalVotes().doubleValue()) * 100));
                    if (pollingPlace.getNullVotes() > pollingPlace.getDifference()) {
                        pollingPlace.setCountingAssumption(true);
                    } else {
                        pollingPlace.setCountingAssumption(false);
                    }
                }
            }
            content.add(pollingPlace);
        }
        Page<PollingPlaceRecountDTO> resultpage = new PageImpl<>(content, pageable, page.getTotalElements());
        return resultpage;
    }

    /**
     *  Get pollingPlaces of election by id.
     *
     *  @param electionId the "id" of the election
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PollingPlaceDTO> getPollingPlacesByIdElection(Long electionId, Pageable pageable) {
        log.debug("Request to get PollingPlaces by Election : {}", electionId);
        Page<PollingPlace> result = pollingPlaceRepository.findByElectionId(electionId, pageable);
        return result.map(pollingPlaceMapper::toDto);
    }

    /**
     *  Get pollingPlaces won and lose by idDistrict.
     *
     *  @param districtId the "districtId" of the pollingPlace
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public PollingPlaceWonLoseDTO pollingPlaceWonLose(Long districtId) {
        log.debug("Request to get the PollingPlaces won-lose by District : {}", districtId);
        Long pollingPlaceWon = pollingPlaceRepository.countByDistrictIdAndPollingPlaceWonIsTrue(districtId);
        Long pollingPlaceLose = pollingPlaceRepository.countByDistrictIdAndPollingPlaceWonIsFalse(districtId);

        PollingPlaceWonLoseDTO pollingPlaceWonLoseDTO = new PollingPlaceWonLoseDTO();
        pollingPlaceWonLoseDTO.setPollingPlacesWon(pollingPlaceWon);
        pollingPlaceWonLoseDTO.setPollingPlacesLose(pollingPlaceLose);

        return pollingPlaceWonLoseDTO;
    }

    /**
     *  Get pollingPlaces by districtId and pollingPlaceWon.
     *
     *  @param districtId the "id" of the district
     *  @param pollingPlaceWon the "pollingPlaceWon" of the pollingPlace
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PollingPlaceDTO> getPollingPlacesWon(Long districtId, Boolean pollingPlaceWon, Pageable pageable) {
        log.debug("Request to get PollingPlaces by District : {} and PollingPlaceWon : {}", districtId, pollingPlaceWon);
        Page<PollingPlace> result = pollingPlaceRepository.getPollingPlacesWon(districtId, pollingPlaceWon, pageable);
        return result.map(pollingPlaceMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<PollingPlaceDTO> getPollingPlaceChallegented( Long districtId, Pageable pageable){
        log.debug("Request to get getPollingPlaceChallegented by districtId {}", districtId);
        Page<PollingPlace> result = pollingPlaceRepository.findByChallengedPollingPlaceIsTrueAndDistrictId(districtId, pageable);
        return result.map(pollingPlaceMapper::toDto);

    }
}
