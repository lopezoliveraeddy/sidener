package electorum.sidener.service;

import electorum.sidener.domain.District;
import electorum.sidener.repository.DistrictRepository;
import electorum.sidener.repository.search.DistrictSearchRepository;
import electorum.sidener.service.dto.DistrictDTO;
import electorum.sidener.service.dto.DistrictExtDTO;
import electorum.sidener.service.mapper.DistrictMapper;
import electorum.sidener.service.mapper.DistrictExtMapper;
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
 * Service Implementation for managing District.
 */
@Service
@Transactional
public class DistrictService {

    private final Logger log = LoggerFactory.getLogger(DistrictService.class);

    private final DistrictRepository districtRepository;

    private final DistrictMapper districtMapper;

    private final DistrictExtMapper districtExtMapper;

    private final DistrictSearchRepository districtSearchRepository;

    public DistrictService(DistrictRepository districtRepository, DistrictMapper districtMapper, DistrictExtMapper districtExtMapper,DistrictSearchRepository districtSearchRepository) {
        this.districtRepository = districtRepository;
        this.districtMapper = districtMapper;
        this.districtExtMapper = districtExtMapper;
        this.districtSearchRepository = districtSearchRepository;
    }

    /**
     * Save a district.
     *
     * @param districtDTO the entity to save
     * @return the persisted entity
     */
    public DistrictDTO save(DistrictDTO districtDTO) {
        log.debug("Request to save District : {}", districtDTO);
        District district = districtMapper.toEntity(districtDTO);
        district = districtRepository.save(district);
        DistrictDTO result = districtMapper.toDto(district);
        districtSearchRepository.save(district);
        return result;
    }

    /**
     *  Get all the districts.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<DistrictDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Districts");
        return districtRepository.findAll(pageable)
            .map(districtMapper::toDto);
    }

    private Page<DistrictExtDTO> resultsExtDTO(Page<DistrictDTO> page, Pageable pageable){
        List<DistrictExtDTO>  content = new ArrayList<>();
        for (DistrictDTO districtDTO : page) {
            DistrictExtDTO district = districtExtMapper.toDto(districtDTO);
            if(district.getId() != null){
                if(district.getTotalFirstPlace() != null && district.getTotalSecondPlace() != null && district.getTotalVotes() != null && district.getElectoralRoll() != null ) {
                    district.setDifference(district.getTotalFirstPlace() - district.getTotalSecondPlace());
                    district.setPercentageDifference(((district.getDifference().doubleValue() / district.getTotalVotes().doubleValue()) * 100));
                    district.setPercentageFirstPlace(((district.getTotalFirstPlace().doubleValue() / district.getTotalVotes().doubleValue()) * 100));
                    district.setPercentageSecondPlace(((district.getTotalSecondPlace().doubleValue() / district.getTotalVotes().doubleValue()) * 100));
                    district.setPercentageTotalVotes(((district.getTotalVotes().doubleValue() / district.getElectoralRoll().doubleValue()) * 100));
                }
            }
            content.add(district);
        }
        Page<DistrictExtDTO> resultpage = new PageImpl<>(content, pageable, page.getTotalElements());
        return resultpage;
    }

    /**
     *  Get one district by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public DistrictDTO findOne(Long id) {
        log.debug("Request to get District : {}", id);
        District district = districtRepository.findOne(id);
        return districtMapper.toDto(district);
    }

    /**
     *  Delete the  district by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete District : {}", id);
        districtRepository.delete(id);
        districtSearchRepository.delete(id);
    }

    /**
     * Search for the district corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<DistrictDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Districts for query {}", query);
        Page<District> result = districtSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(districtMapper::toDto);
    }

    /**
     *  Get distrcits of election by id.
     *
     *  @param id the id of the entity
     *  @param pageable the pagination information
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Page<DistrictExtDTO> getDistrictsByIdElection(Long id, Pageable pageable) {
        log.debug("Request to get Districts by Election : {}", id);
        Page<DistrictDTO> page = districtRepository.findByElectionId(id, pageable).map(districtMapper::toDto);
        Page<DistrictExtDTO> resultPage = resultsExtDTO(page, pageable);
        return resultPage;
    }
}
