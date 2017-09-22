package electorum.sidener.web.rest;

import com.codahale.metrics.annotation.Timed;
import electorum.sidener.service.CausalService;
import electorum.sidener.web.rest.util.HeaderUtil;
import electorum.sidener.web.rest.util.PaginationUtil;
import electorum.sidener.service.dto.CausalDTO;
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
 * REST controller for managing Causal.
 */
@RestController
@RequestMapping("/api")
public class CausalResource {

    private final Logger log = LoggerFactory.getLogger(CausalResource.class);

    private static final String ENTITY_NAME = "causal";

    private final CausalService causalService;

    public CausalResource(CausalService causalService) {
        this.causalService = causalService;
    }

    /**
     * POST  /causals : Create a new causal.
     *
     * @param causalDTO the causalDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new causalDTO, or with status 400 (Bad Request) if the causal has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/causals")
    @Timed
    public ResponseEntity<CausalDTO> createCausal(@RequestBody CausalDTO causalDTO) throws URISyntaxException {
        log.debug("REST request to save Causal : {}", causalDTO);
        if (causalDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new causal cannot already have an ID")).body(null);
        }
        CausalDTO result = causalService.save(causalDTO);
        return ResponseEntity.created(new URI("/api/causals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /causals : Updates an existing causal.
     *
     * @param causalDTO the causalDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated causalDTO,
     * or with status 400 (Bad Request) if the causalDTO is not valid,
     * or with status 500 (Internal Server Error) if the causalDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/causals")
    @Timed
    public ResponseEntity<CausalDTO> updateCausal(@RequestBody CausalDTO causalDTO) throws URISyntaxException {
        log.debug("REST request to update Causal : {}", causalDTO);
        if (causalDTO.getId() == null) {
            return createCausal(causalDTO);
        }
        CausalDTO result = causalService.save(causalDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, causalDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /causals : get all the causals.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of causals in body
     */
    @GetMapping("/causals")
    @Timed
    public ResponseEntity<List<CausalDTO>> getAllCausals(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Causals");
        Page<CausalDTO> page = causalService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/causals");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /causals/:id : get the "id" causal.
     *
     * @param id the id of the causalDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the causalDTO, or with status 404 (Not Found)
     */
    @GetMapping("/causals/{id}")
    @Timed
    public ResponseEntity<CausalDTO> getCausal(@PathVariable Long id) {
        log.debug("REST request to get Causal : {}", id);
        CausalDTO causalDTO = causalService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(causalDTO));
    }

    /**
     * DELETE  /causals/:id : delete the "id" causal.
     *
     * @param id the id of the causalDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/causals/{id}")
    @Timed
    public ResponseEntity<Void> deleteCausal(@PathVariable Long id) {
        log.debug("REST request to delete Causal : {}", id);
        causalService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/causals?query=:query : search for the causal corresponding
     * to the query.
     *
     * @param query the query of the causal search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/causals")
    @Timed
    public ResponseEntity<List<CausalDTO>> searchCausals(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Causals for query {}", query);
        Page<CausalDTO> page = causalService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/causals");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
