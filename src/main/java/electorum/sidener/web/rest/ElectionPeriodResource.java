package electorum.sidener.web.rest;

import com.codahale.metrics.annotation.Timed;
import electorum.sidener.service.ElectionPeriodService;
import electorum.sidener.web.rest.util.HeaderUtil;
import electorum.sidener.web.rest.util.PaginationUtil;
import electorum.sidener.service.dto.ElectionPeriodDTO;
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

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing ElectionPeriod.
 */
@RestController
@RequestMapping("/api")
public class ElectionPeriodResource {

    private final Logger log = LoggerFactory.getLogger(ElectionPeriodResource.class);

    private static final String ENTITY_NAME = "electionPeriod";

    private final ElectionPeriodService electionPeriodService;

    public ElectionPeriodResource(ElectionPeriodService electionPeriodService) {
        this.electionPeriodService = electionPeriodService;
    }

    /**
     * POST  /election-periods : Create a new electionPeriod.
     *
     * @param electionPeriodDTO the electionPeriodDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new electionPeriodDTO, or with status 400 (Bad Request) if the electionPeriod has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/election-periods")
    @Timed
    public ResponseEntity<ElectionPeriodDTO> createElectionPeriod(@RequestBody ElectionPeriodDTO electionPeriodDTO) throws URISyntaxException {
        log.debug("REST request to save ElectionPeriod : {}", electionPeriodDTO);
        if (electionPeriodDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new electionPeriod cannot already have an ID")).body(null);
        }
        ElectionPeriodDTO result = electionPeriodService.save(electionPeriodDTO);
        return ResponseEntity.created(new URI("/api/election-periods/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /election-periods : Updates an existing electionPeriod.
     *
     * @param electionPeriodDTO the electionPeriodDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated electionPeriodDTO,
     * or with status 400 (Bad Request) if the electionPeriodDTO is not valid,
     * or with status 500 (Internal Server Error) if the electionPeriodDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/election-periods")
    @Timed
    public ResponseEntity<ElectionPeriodDTO> updateElectionPeriod(@RequestBody ElectionPeriodDTO electionPeriodDTO) throws URISyntaxException {
        log.debug("REST request to update ElectionPeriod : {}", electionPeriodDTO);
        if (electionPeriodDTO.getId() == null) {
            return createElectionPeriod(electionPeriodDTO);
        }
        ElectionPeriodDTO result = electionPeriodService.save(electionPeriodDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, electionPeriodDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /election-periods : get all the electionPeriods.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of electionPeriods in body
     */
    @GetMapping("/election-periods")
    @Timed
    public ResponseEntity<List<ElectionPeriodDTO>> getAllElectionPeriods(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of ElectionPeriods");
        Page<ElectionPeriodDTO> page = electionPeriodService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/election-periods");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /election-periods/:id : get the "id" electionPeriod.
     *
     * @param id the id of the electionPeriodDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the electionPeriodDTO, or with status 404 (Not Found)
     */
    @GetMapping("/election-periods/{id}")
    @Timed
    public ResponseEntity<ElectionPeriodDTO> getElectionPeriod(@PathVariable Long id) {
        log.debug("REST request to get ElectionPeriod : {}", id);
        ElectionPeriodDTO electionPeriodDTO = electionPeriodService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(electionPeriodDTO));
    }

    /**
     * DELETE  /election-periods/:id : delete the "id" electionPeriod.
     *
     * @param id the id of the electionPeriodDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/election-periods/{id}")
    @Timed
    public ResponseEntity<Void> deleteElectionPeriod(@PathVariable Long id) {
        log.debug("REST request to delete ElectionPeriod : {}", id);
        electionPeriodService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/election-periods?query=:query : search for the electionPeriod corresponding
     * to the query.
     *
     * @param query the query of the electionPeriod search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/election-periods")
    @Timed
    public ResponseEntity<List<ElectionPeriodDTO>> searchElectionPeriods(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of ElectionPeriods for query {}", query);
        Page<ElectionPeriodDTO> page = electionPeriodService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/election-periods");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
