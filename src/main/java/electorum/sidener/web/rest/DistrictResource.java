package electorum.sidener.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.opencsv.CSVReader;
import electorum.sidener.service.DistrictService;
import electorum.sidener.service.dto.*;
import electorum.sidener.web.rest.util.DistrictFromFile;
import electorum.sidener.web.rest.util.HeaderUtil;
import electorum.sidener.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing District.
 */
@RestController
@RequestMapping("/api")
public class DistrictResource {

    private final Logger log = LoggerFactory.getLogger(DistrictResource.class);

    private static final String ENTITY_NAME = "district";

    private final DistrictService districtService;

    public DistrictResource(DistrictService districtService) {
        this.districtService = districtService;
    }

    /**
     * POST  /districts : Create a new district.
     *
     * @param districtDTO the districtDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new districtDTO, or with status 400 (Bad Request) if the district has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/districts")
    @Timed
    public ResponseEntity<DistrictDTO> createDistrict(@RequestBody DistrictDTO districtDTO) throws URISyntaxException {
        log.debug("REST request to save District : {}", districtDTO);
        if (districtDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new district cannot already have an ID")).body(null);
        }
        DistrictDTO result = districtService.save(districtDTO);
        return ResponseEntity.created(new URI("/api/districts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /districts : Updates an existing district.
     *
     * @param districtDTO the districtDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated districtDTO,
     * or with status 400 (Bad Request) if the districtDTO is not valid,
     * or with status 500 (Internal Server Error) if the districtDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/districts")
    @Timed
    public ResponseEntity<DistrictDTO> updateDistrict(@RequestBody DistrictDTO districtDTO) throws URISyntaxException {
        log.debug("REST request to update District : {}", districtDTO);
        if (districtDTO.getId() == null) {
            return createDistrict(districtDTO);
        }
        DistrictDTO result = districtService.save(districtDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, districtDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /districts : get all the districts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of districts in body
     */
    @GetMapping("/districts")
    @Timed
    public ResponseEntity<List<DistrictDTO>> getAllDistricts(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Districts");
        Page<DistrictDTO> page = districtService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/districts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /districts/:id : get the "id" district.
     *
     * @param id the id of the districtDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the districtDTO, or with status 404 (Not Found)
     */
    @GetMapping("/districts/{id}")
    @Timed
    public ResponseEntity<DistrictDTO> getDistrict(@PathVariable Long id) {
        log.debug("REST request to get District : {}", id);
        DistrictDTO districtDTO = districtService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(districtDTO));
    }

    /**
     * DELETE  /districts/:id : delete the "id" district.
     *
     * @param id the id of the districtDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/districts/{id}")
    @Timed
    public ResponseEntity<Void> deleteDistrict(@PathVariable Long id) {
        log.debug("REST request to delete District : {}", id);
        districtService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/districts?query=:query : search for the district corresponding
     * to the query.
     *
     * @param query the query of the district search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/districts")
    @Timed
    public ResponseEntity<List<DistrictDTO>> searchDistricts(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Districts for query {}", query);
        Page<DistrictDTO> page = districtService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/districts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET DISTRICTS  /recount/{idElection}/districts : get disctricts by "idElection"
     *
     * @param idElection the "idElection" of the district
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/recount/{idElection}/districts")
    @Timed
    public ResponseEntity<List<DistrictRecountDTO>> getDistrictsByIdElectionRecount(@PathVariable Long idElection, @ApiParam Pageable pageable) {
        log.debug("REST request to get Districts by Election : {}", idElection);
        Page<DistrictRecountDTO> page = districtService.getDistrictsByIdElectionRecount(idElection, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/recount/{idElection}/districts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET DISTRICTS  /nullity/{idElection}/districts : get disctricts by "idElection"
     *
     * @param idElection the "idElection" of the district
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/nullity/{idElection}/districts")
    @Timed
    public ResponseEntity<List<DistrictNullityDTO>> getDistrictsByIdElectionNullity(@PathVariable Long idElection, @ApiParam Pageable pageable) {
        log.debug("REST request to get Districts by Election : {}", idElection);
        Page<DistrictNullityDTO> page = districtService.getDistrictsByIdElectionNullity(idElection, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/nullity/{idElection}/districts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET DISTRICTS WON AND LOSE  /recount/{idElection}/districts-won-lose : get disctricts-won-lose by "idElection"
     *
     * @param idElection the "idElection" of the district
     * @return the result of the search
     */
    @GetMapping("/recount/{idElection}/districts-won-lose")
    @Timed
    public ResponseEntity<DistrictWonLoseDTO> districtsWonLose(@PathVariable Long idElection) {
        log.debug("REST request to get the Districts won-lose by Election : {}", idElection);
        DistrictWonLoseDTO districtsWon = districtService.districtsWonLose(idElection);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(districtsWon));
    }



    /**
     * Post /district : Create a new election data from file
     *
     * @param loadDTO
     *            the loadDTO to create
     * @return ResponseEntity with status 201 (Created) and with body the new
     *         loadDTO
     * @trhows URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/district")
    @Timed
    public ResponseEntity<List<DistrictDTO>> createDistrictByFile(@RequestBody LoadDTO loadDTO) throws URISyntaxException {
        log.debug("---> REST request to createDistrictByFile : {}", loadDTO);
        DistrictFromFile districtFromFile = new DistrictFromFile();
        byte[] csvFile = loadDTO.getDbFile();
        List<DistrictDTO> districtDTOList = new ArrayList<DistrictDTO>();
        if (csvFile != null) {
            try {
                FileUtils.writeByteArrayToFile(new File("/files/district/" + loadDTO.getEleccion() + ".csv"), csvFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            Reader reader = Files.newBufferedReader(Paths.get("/files/district/" + loadDTO.getEleccion() + ".csv"));
            CSVReader csvReader = new CSVReader(reader);
            districtDTOList = districtFromFile.processFile(csvReader,loadDTO.getEleccion());
            for (Iterator iterator = districtDTOList.iterator(); iterator.hasNext();) {
                DistrictDTO districtDTO = (DistrictDTO) iterator.next();
                log.debug("DistrictDTO : {} ",districtDTO.toString());
                districtService.save(districtDTO);

            }


        }catch(IOException e) {
            e.printStackTrace();
        }
        return null;

    }

}
