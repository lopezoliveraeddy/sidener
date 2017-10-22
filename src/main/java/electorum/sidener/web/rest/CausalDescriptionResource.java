package electorum.sidener.web.rest;

import com.codahale.metrics.annotation.Timed;
import electorum.sidener.service.CausalDescriptionService;
import electorum.sidener.web.rest.util.HeaderUtil;
import electorum.sidener.web.rest.util.PaginationUtil;
import electorum.sidener.service.dto.CausalDescriptionDTO;
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
 * REST controller for managing CausalDescription.
 */
@RestController
@RequestMapping("/api")
public class CausalDescriptionResource {

    private final Logger log = LoggerFactory.getLogger(CausalDescriptionResource.class);

    private static final String ENTITY_NAME = "causalDescription";

    private final CausalDescriptionService causalDescriptionService;

    public CausalDescriptionResource(CausalDescriptionService causalDescriptionService) {
        this.causalDescriptionService = causalDescriptionService;
    }

    /**
     * POST  /causal-descriptions : Create a new causalDescription.
     *
     * @param causalDescriptionDTO the causalDescriptionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new causalDescriptionDTO, or with status 400 (Bad Request) if the causalDescription has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/causal-descriptions")
    @Timed
    public ResponseEntity<CausalDescriptionDTO> createCausalDescription(@RequestBody CausalDescriptionDTO causalDescriptionDTO) throws URISyntaxException {
        log.debug("REST request to save CausalDescription : {}", causalDescriptionDTO);
        if (causalDescriptionDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new causalDescription cannot already have an ID")).body(null);
        }
        CausalDescriptionDTO result = causalDescriptionService.save(causalDescriptionDTO);
        return ResponseEntity.created(new URI("/api/causal-descriptions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /causal-descriptions : Updates an existing causalDescription.
     *
     * @param causalDescriptionDTO the causalDescriptionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated causalDescriptionDTO,
     * or with status 400 (Bad Request) if the causalDescriptionDTO is not valid,
     * or with status 500 (Internal Server Error) if the causalDescriptionDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/causal-descriptions")
    @Timed
    public ResponseEntity<CausalDescriptionDTO> updateCausalDescription(@RequestBody CausalDescriptionDTO causalDescriptionDTO) throws URISyntaxException {
        log.debug("REST request to update CausalDescription : {}", causalDescriptionDTO);
        if (causalDescriptionDTO.getId() == null) {
            return createCausalDescription(causalDescriptionDTO);
        }
        CausalDescriptionDTO result = causalDescriptionService.save(causalDescriptionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, causalDescriptionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /causal-descriptions : get all the causalDescriptions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of causalDescriptions in body
     */
    @GetMapping("/causal-descriptions")
    @Timed
    public ResponseEntity<List<CausalDescriptionDTO>> getAllCausalDescriptions(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of CausalDescriptions");
        Page<CausalDescriptionDTO> page = causalDescriptionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/causal-descriptions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /causal-descriptions/:id : get the "id" causalDescription.
     *
     * @param id the id of the causalDescriptionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the causalDescriptionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/causal-descriptions/{id}")
    @Timed
    public ResponseEntity<CausalDescriptionDTO> getCausalDescription(@PathVariable Long id) {
        log.debug("REST request to get CausalDescription : {}", id);
        CausalDescriptionDTO causalDescriptionDTO = causalDescriptionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(causalDescriptionDTO));
    }

    /**
     * DELETE  /causal-descriptions/:id : delete the "id" causalDescription.
     *
     * @param id the id of the causalDescriptionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/causal-descriptions/{id}")
    @Timed
    public ResponseEntity<Void> deleteCausalDescription(@PathVariable Long id) {
        log.debug("REST request to delete CausalDescription : {}", id);
        causalDescriptionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/causal-descriptions?query=:query : search for the causalDescription corresponding
     * to the query.
     *
     * @param query the query of the causalDescription search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/causal-descriptions")
    @Timed
    public ResponseEntity<List<CausalDescriptionDTO>> searchCausalDescriptions(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of CausalDescriptions for query {}", query);
        Page<CausalDescriptionDTO> page = causalDescriptionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/causal-descriptions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
