package electorum.sidener.web.rest;

import com.codahale.metrics.annotation.Timed;
import electorum.sidener.service.CoalitionService;
import electorum.sidener.web.rest.util.HeaderUtil;
import electorum.sidener.web.rest.util.PaginationUtil;
import electorum.sidener.service.dto.CoalitionDTO;
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
 * REST controller for managing Coalition.
 */
@RestController
@RequestMapping("/api")
public class CoalitionResource {

    private final Logger log = LoggerFactory.getLogger(CoalitionResource.class);

    private static final String ENTITY_NAME = "coalition";

    private final CoalitionService coalitionService;

    public CoalitionResource(CoalitionService coalitionService) {
        this.coalitionService = coalitionService;
    }

    /**
     * POST  /coalitions : Create a new coalition.
     *
     * @param coalitionDTO the coalitionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new coalitionDTO, or with status 400 (Bad Request) if the coalition has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/coalitions")
    @Timed
    public ResponseEntity<CoalitionDTO> createCoalition(@RequestBody CoalitionDTO coalitionDTO) throws URISyntaxException {
        log.debug("REST request to save Coalition : {}", coalitionDTO);
        if (coalitionDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new coalition cannot already have an ID")).body(null);
        }
        CoalitionDTO result = coalitionService.save(coalitionDTO);
        return ResponseEntity.created(new URI("/api/coalitions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /coalitions : Updates an existing coalition.
     *
     * @param coalitionDTO the coalitionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated coalitionDTO,
     * or with status 400 (Bad Request) if the coalitionDTO is not valid,
     * or with status 500 (Internal Server Error) if the coalitionDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/coalitions")
    @Timed
    public ResponseEntity<CoalitionDTO> updateCoalition(@RequestBody CoalitionDTO coalitionDTO) throws URISyntaxException {
        log.debug("REST request to update Coalition : {}", coalitionDTO);
        if (coalitionDTO.getId() == null) {
            return createCoalition(coalitionDTO);
        }
        CoalitionDTO result = coalitionService.save(coalitionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, coalitionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /coalitions : get all the coalitions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of coalitions in body
     */
    @GetMapping("/coalitions")
    @Timed
    public ResponseEntity<List<CoalitionDTO>> getAllCoalitions(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Coalitions");
        Page<CoalitionDTO> page = coalitionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/coalitions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /coalitions/:id : get the "id" coalition.
     *
     * @param id the id of the coalitionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the coalitionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/coalitions/{id}")
    @Timed
    public ResponseEntity<CoalitionDTO> getCoalition(@PathVariable Long id) {
        log.debug("REST request to get Coalition : {}", id);
        CoalitionDTO coalitionDTO = coalitionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(coalitionDTO));
    }

    /**
     * DELETE  /coalitions/:id : delete the "id" coalition.
     *
     * @param id the id of the coalitionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/coalitions/{id}")
    @Timed
    public ResponseEntity<Void> deleteCoalition(@PathVariable Long id) {
        log.debug("REST request to delete Coalition : {}", id);
        coalitionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/coalitions?query=:query : search for the coalition corresponding
     * to the query.
     *
     * @param query the query of the coalition search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/coalitions")
    @Timed
    public ResponseEntity<List<CoalitionDTO>> searchCoalitions(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Coalitions for query {}", query);
        Page<CoalitionDTO> page = coalitionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/coalitions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
