package electorum.sidener.service;

import electorum.sidener.domain.DetectorCausals;
import electorum.sidener.repository.DetectorCausalsRepository;
import electorum.sidener.repository.search.DetectorCausalsSearchRepository;
import electorum.sidener.service.dto.DetectorCausalsDTO;
import electorum.sidener.service.mapper.DetectorCausalsMapper;
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
 * Service Implementation for managing DetectorCausals.
 */
@Service
@Transactional
public class DetectorCausalsService {

    private final Logger log = LoggerFactory.getLogger(DetectorCausalsService.class);

    private final DetectorCausalsRepository detectorCausalsRepository;

    private final DetectorCausalsMapper detectorCausalsMapper;

    private final DetectorCausalsSearchRepository detectorCausalsSearchRepository;


    public DetectorCausalsService(DetectorCausalsRepository detectorCausalsRepository, DetectorCausalsMapper detectorCausalsMapper, DetectorCausalsSearchRepository detectorCausalsSearchRepository) {
        this.detectorCausalsRepository = detectorCausalsRepository;
        this.detectorCausalsMapper = detectorCausalsMapper;
        this.detectorCausalsSearchRepository = detectorCausalsSearchRepository;
    }

    /**
     * Save a detectorCausals.
     *
     * @param detectorCausalsDTO the entity to save
     * @return the persisted entity
     */
    public DetectorCausalsDTO save(DetectorCausalsDTO detectorCausalsDTO) {
        log.debug("Request to save DetectorCausals : {}", detectorCausalsDTO);
        DetectorCausals detectorCausals = detectorCausalsMapper.toEntity(detectorCausalsDTO);
        detectorCausals = detectorCausalsRepository.save(detectorCausals);
        DetectorCausalsDTO result = detectorCausalsMapper.toDto(detectorCausals);
        detectorCausalsSearchRepository.save(detectorCausals);
        return result;
    }

    /**
     *  Get all the detectorCausals.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<DetectorCausalsDTO> findAll() {
        log.debug("Request to get all DetectorCausals");
        return detectorCausalsRepository.findAll().stream()
            .map(detectorCausalsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one detectorCausals by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public DetectorCausalsDTO findOne(Long id) {
        log.debug("Request to get DetectorCausals : {}", id);
        DetectorCausals detectorCausals = detectorCausalsRepository.findOne(id);
        return detectorCausalsMapper.toDto(detectorCausals);
    }

    /**
     *  Delete the  detectorCausals by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete DetectorCausals : {}", id);
        detectorCausalsRepository.delete(id);
        detectorCausalsSearchRepository.delete(id);
    }

    /**
     * Search for the detectorCausals corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<DetectorCausalsDTO> search(String query) {
        log.debug("Request to search DetectorCausals for query {}", query);
        return StreamSupport
            .stream(detectorCausalsSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(detectorCausalsMapper::toDto)
            .collect(Collectors.toList());
    }

    /**
     * Get detectorCausals by idPollingPlace and idCausal
     *
     *  @param idPollingPlace the "idPollingPlace" of the detectorCausals
     *  @param idCausal the "idCausal" of the detectorCausals
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public DetectorCausalsDTO getDetectorCausalsByPollingPlaceAndCausal(Long idPollingPlace, Long idCausal) {
        log.debug("Request to get a list of DetectorCausals by idPollingPlace {} and idCausal {}", idPollingPlace, idCausal);
        DetectorCausals detectorCausals = detectorCausalsRepository.findDetectorCausalsByIdPollingPlaceAndIdCausal(idPollingPlace, idCausal);
        return detectorCausalsMapper.toDto(detectorCausals);
    }

    @Transactional(readOnly =  true)
    public List<DetectorCausalsDTO> getDetectorCausalsDTOList (Long idDistrict){
        List<DetectorCausals> result = detectorCausalsRepository.findDetectorCausalsByIdDistrictOrderByIdCausal(idDistrict);
        return  detectorCausalsMapper.toDto(result);
    }

    @Transactional(readOnly =  true)
    public List<DetectorCausalsDTO> getDetectorCausalByIdDistrict(Long idDistrict){
        List<DetectorCausals> result = detectorCausalsRepository.findDetectorCausalsByIdDistrictOrderByIdCausal(idDistrict);
        return detectorCausalsMapper.toDto(result);

    }




}
