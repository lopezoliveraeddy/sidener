package electorum.sidener.web.rest;

import com.codahale.metrics.annotation.Timed;
import electorum.sidener.service.IndependentCandidateService;
import electorum.sidener.web.rest.util.HeaderUtil;
import electorum.sidener.web.rest.util.PaginationUtil;
import electorum.sidener.service.dto.IndependentCandidateDTO;
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
 * REST controller for managing IndependentCandidate.
 */
@RestController
@RequestMapping("/api")
public class IndependentCandidateResource {

    private final Logger log = LoggerFactory.getLogger(IndependentCandidateResource.class);

    private static final String ENTITY_NAME = "independentCandidate";

    private final IndependentCandidateService independentCandidateService;

    public IndependentCandidateResource(IndependentCandidateService independentCandidateService) {
        this.independentCandidateService = independentCandidateService;
    }

    /**
     * POST  /independent-candidates : Create a new independentCandidate.
     *
     * @param independentCandidateDTO the independentCandidateDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new independentCandidateDTO, or with status 400 (Bad Request) if the independentCandidate has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/independent-candidates")
    @Timed
    public ResponseEntity<IndependentCandidateDTO> createIndependentCandidate(@RequestBody IndependentCandidateDTO independentCandidateDTO) throws URISyntaxException {
        log.debug("REST request to save IndependentCandidate : {}", independentCandidateDTO);
        if (independentCandidateDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new independentCandidate cannot already have an ID")).body(null);
        }
        IndependentCandidateDTO result = independentCandidateService.save(independentCandidateDTO);
        return ResponseEntity.created(new URI("/api/independent-candidates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /independent-candidates : Updates an existing independentCandidate.
     *
     * @param independentCandidateDTO the independentCandidateDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated independentCandidateDTO,
     * or with status 400 (Bad Request) if the independentCandidateDTO is not valid,
     * or with status 500 (Internal Server Error) if the independentCandidateDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/independent-candidates")
    @Timed
    public ResponseEntity<IndependentCandidateDTO> updateIndependentCandidate(@RequestBody IndependentCandidateDTO independentCandidateDTO) throws URISyntaxException {
        log.debug("REST request to update IndependentCandidate : {}", independentCandidateDTO);
        if (independentCandidateDTO.getId() == null) {
            return createIndependentCandidate(independentCandidateDTO);
        }
        IndependentCandidateDTO result = independentCandidateService.save(independentCandidateDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, independentCandidateDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /independent-candidates : get all the independentCandidates.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of independentCandidates in body
     */
    @GetMapping("/independent-candidates")
    @Timed
    public ResponseEntity<List<IndependentCandidateDTO>> getAllIndependentCandidates(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of IndependentCandidates");
        Page<IndependentCandidateDTO> page = independentCandidateService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/independent-candidates");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /independent-candidates/:id : get the "id" independentCandidate.
     *
     * @param id the id of the independentCandidateDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the independentCandidateDTO, or with status 404 (Not Found)
     */
    @GetMapping("/independent-candidates/{id}")
    @Timed
    public ResponseEntity<IndependentCandidateDTO> getIndependentCandidate(@PathVariable Long id) {
        log.debug("REST request to get IndependentCandidate : {}", id);
        IndependentCandidateDTO independentCandidateDTO = independentCandidateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(independentCandidateDTO));
    }

    /**
     * DELETE  /independent-candidates/:id : delete the "id" independentCandidate.
     *
     * @param id the id of the independentCandidateDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/independent-candidates/{id}")
    @Timed
    public ResponseEntity<Void> deleteIndependentCandidate(@PathVariable Long id) {
        log.debug("REST request to delete IndependentCandidate : {}", id);
        independentCandidateService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/independent-candidates?query=:query : search for the independentCandidate corresponding
     * to the query.
     *
     * @param query the query of the independentCandidate search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/independent-candidates")
    @Timed
    public ResponseEntity<List<IndependentCandidateDTO>> searchIndependentCandidates(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of IndependentCandidates for query {}", query);
        Page<IndependentCandidateDTO> page = independentCandidateService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/independent-candidates");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
