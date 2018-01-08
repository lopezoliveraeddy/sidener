package electorum.sidener.web.rest;

import com.codahale.metrics.annotation.Timed;
import electorum.sidener.service.PollingPlaceService;
import electorum.sidener.web.rest.util.HeaderUtil;
import electorum.sidener.web.rest.util.PaginationUtil;
import electorum.sidener.service.dto.LoadDTO;
import electorum.sidener.service.dto.PollingPlaceDTO;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing PollingPlace.
 */
@RestController
@RequestMapping("/api")
public class PollingPlaceResource {

    private final Logger log = LoggerFactory.getLogger(PollingPlaceResource.class);

    private static final String ENTITY_NAME = "pollingPlace";

    private final PollingPlaceService pollingPlaceService;

    public PollingPlaceResource(PollingPlaceService pollingPlaceService) {
        this.pollingPlaceService = pollingPlaceService;
    }

    /**
     * POST  /polling-places : Create a new pollingPlace.
     *
     * @param pollingPlaceDTO the pollingPlaceDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pollingPlaceDTO, or with status 400 (Bad Request) if the pollingPlace has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/polling-places")
    @Timed
    public ResponseEntity<PollingPlaceDTO> createPollingPlace(@RequestBody PollingPlaceDTO pollingPlaceDTO) throws URISyntaxException {
        log.debug("REST request to save PollingPlace : {}", pollingPlaceDTO);
        if (pollingPlaceDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new pollingPlace cannot already have an ID")).body(null);
        }
        PollingPlaceDTO result = pollingPlaceService.save(pollingPlaceDTO);
        return ResponseEntity.created(new URI("/api/polling-places/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /polling-places : Updates an existing pollingPlace.
     *
     * @param pollingPlaceDTO the pollingPlaceDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pollingPlaceDTO,
     * or with status 400 (Bad Request) if the pollingPlaceDTO is not valid,
     * or with status 500 (Internal Server Error) if the pollingPlaceDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/polling-places")
    @Timed
    public ResponseEntity<PollingPlaceDTO> updatePollingPlace(@RequestBody PollingPlaceDTO pollingPlaceDTO) throws URISyntaxException {
        log.debug("REST request to update PollingPlace : {}", pollingPlaceDTO);
        if (pollingPlaceDTO.getId() == null) {
            return createPollingPlace(pollingPlaceDTO);
        }
        PollingPlaceDTO result = pollingPlaceService.save(pollingPlaceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pollingPlaceDTO.getId().toString()))
            .body(result);
    }
    
    @PutMapping("/process-places")
    @Timed
    public ResponseEntity<List<PollingPlaceDTO>> createPollingPlaces (@RequestBody LoadDTO loadDTO) throws URISyntaxException{
    		log.debug("REST request to process a bunch of records");
    		byte[] dbFile = loadDTO.getDbFile();
    		try {
				String decoded = new String(dbFile, "UTF-8");
				log.debug("CSV {}", decoded);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    	
    		return null;
    	
    }

    /**
     * GET  /polling-places : get all the pollingPlaces.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of pollingPlaces in body
     */
    @GetMapping("/polling-places")
    @Timed
    public ResponseEntity<List<PollingPlaceDTO>> getAllPollingPlaces(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of PollingPlaces");
        Page<PollingPlaceDTO> page = pollingPlaceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/polling-places");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /polling-places/:id : get the "id" pollingPlace.
     *
     * @param id the id of the pollingPlaceDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pollingPlaceDTO, or with status 404 (Not Found)
     */
    @GetMapping("/polling-places/{id}")
    @Timed
    public ResponseEntity<PollingPlaceDTO> getPollingPlace(@PathVariable Long id) {
        log.debug("REST request to get PollingPlace : {}", id);
        PollingPlaceDTO pollingPlaceDTO = pollingPlaceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pollingPlaceDTO));
    }

    /**
     * DELETE  /polling-places/:id : delete the "id" pollingPlace.
     *
     * @param id the id of the pollingPlaceDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/polling-places/{id}")
    @Timed
    public ResponseEntity<Void> deletePollingPlace(@PathVariable Long id) {
        log.debug("REST request to delete PollingPlace : {}", id);
        pollingPlaceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/polling-places?query=:query : search for the pollingPlace corresponding
     * to the query.
     *
     * @param query the query of the pollingPlace search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/polling-places")
    @Timed
    public ResponseEntity<List<PollingPlaceDTO>> searchPollingPlaces(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of PollingPlaces for query {}", query);
        Page<PollingPlaceDTO> page = pollingPlaceService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/polling-places");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET DISTRICTS  /districts/{id}/polling-places : get pollingPlaces by "id" district
     * to the query.
     *
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/districts/{id}/polling-places")
    @Timed
    public ResponseEntity<List<PollingPlaceDTO>> getDistrictsByIdElection(@PathVariable Long id, @ApiParam Pageable pageable) {
        log.debug("REST request to get PollingPlaces by District : {}", id);
        Page<PollingPlaceDTO> page = pollingPlaceService.getPollingPlacesByIdDistrict(id, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/districts/{id}/polling-places");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
