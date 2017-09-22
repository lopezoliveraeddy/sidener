package electorum.sidener.web.rest;

import com.codahale.metrics.annotation.Timed;
import electorum.sidener.service.ElectionTypeService;
import electorum.sidener.web.rest.util.HeaderUtil;
import electorum.sidener.web.rest.util.PaginationUtil;
import electorum.sidener.service.dto.ElectionTypeDTO;
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
 * REST controller for managing ElectionType.
 */
@RestController
@RequestMapping("/api")
public class ElectionTypeResource {

    private final Logger log = LoggerFactory.getLogger(ElectionTypeResource.class);

    private static final String ENTITY_NAME = "electionType";

    private final ElectionTypeService electionTypeService;

    public ElectionTypeResource(ElectionTypeService electionTypeService) {
        this.electionTypeService = electionTypeService;
    }

    /**
     * POST  /election-types : Create a new electionType.
     *
     * @param electionTypeDTO the electionTypeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new electionTypeDTO, or with status 400 (Bad Request) if the electionType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/election-types")
    @Timed
    public ResponseEntity<ElectionTypeDTO> createElectionType(@RequestBody ElectionTypeDTO electionTypeDTO) throws URISyntaxException {
        log.debug("REST request to save ElectionType : {}", electionTypeDTO);
        if (electionTypeDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new electionType cannot already have an ID")).body(null);
        }
        ElectionTypeDTO result = electionTypeService.save(electionTypeDTO);
        return ResponseEntity.created(new URI("/api/election-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /election-types : Updates an existing electionType.
     *
     * @param electionTypeDTO the electionTypeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated electionTypeDTO,
     * or with status 400 (Bad Request) if the electionTypeDTO is not valid,
     * or with status 500 (Internal Server Error) if the electionTypeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/election-types")
    @Timed
    public ResponseEntity<ElectionTypeDTO> updateElectionType(@RequestBody ElectionTypeDTO electionTypeDTO) throws URISyntaxException {
        log.debug("REST request to update ElectionType : {}", electionTypeDTO);
        if (electionTypeDTO.getId() == null) {
            return createElectionType(electionTypeDTO);
        }
        ElectionTypeDTO result = electionTypeService.save(electionTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, electionTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /election-types : get all the electionTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of electionTypes in body
     */
    @GetMapping("/election-types")
    @Timed
    public ResponseEntity<List<ElectionTypeDTO>> getAllElectionTypes(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of ElectionTypes");
        Page<ElectionTypeDTO> page = electionTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/election-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /election-types/:id : get the "id" electionType.
     *
     * @param id the id of the electionTypeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the electionTypeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/election-types/{id}")
    @Timed
    public ResponseEntity<ElectionTypeDTO> getElectionType(@PathVariable Long id) {
        log.debug("REST request to get ElectionType : {}", id);
        ElectionTypeDTO electionTypeDTO = electionTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(electionTypeDTO));
    }

    /**
     * DELETE  /election-types/:id : delete the "id" electionType.
     *
     * @param id the id of the electionTypeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/election-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteElectionType(@PathVariable Long id) {
        log.debug("REST request to delete ElectionType : {}", id);
        electionTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/election-types?query=:query : search for the electionType corresponding
     * to the query.
     *
     * @param query the query of the electionType search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/election-types")
    @Timed
    public ResponseEntity<List<ElectionTypeDTO>> searchElectionTypes(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of ElectionTypes for query {}", query);
        Page<ElectionTypeDTO> page = electionTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/election-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
