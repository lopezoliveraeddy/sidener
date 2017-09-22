package electorum.sidener.web.rest;

import com.codahale.metrics.annotation.Timed;
import electorum.sidener.service.PoliticalPartyService;
import electorum.sidener.web.rest.util.HeaderUtil;
import electorum.sidener.web.rest.util.PaginationUtil;
import electorum.sidener.service.dto.PoliticalPartyDTO;
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
 * REST controller for managing PoliticalParty.
 */
@RestController
@RequestMapping("/api")
public class PoliticalPartyResource {

    private final Logger log = LoggerFactory.getLogger(PoliticalPartyResource.class);

    private static final String ENTITY_NAME = "politicalParty";

    private final PoliticalPartyService politicalPartyService;

    public PoliticalPartyResource(PoliticalPartyService politicalPartyService) {
        this.politicalPartyService = politicalPartyService;
    }

    /**
     * POST  /political-parties : Create a new politicalParty.
     *
     * @param politicalPartyDTO the politicalPartyDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new politicalPartyDTO, or with status 400 (Bad Request) if the politicalParty has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/political-parties")
    @Timed
    public ResponseEntity<PoliticalPartyDTO> createPoliticalParty(@RequestBody PoliticalPartyDTO politicalPartyDTO) throws URISyntaxException {
        log.debug("REST request to save PoliticalParty : {}", politicalPartyDTO);
        if (politicalPartyDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new politicalParty cannot already have an ID")).body(null);
        }
        PoliticalPartyDTO result = politicalPartyService.save(politicalPartyDTO);
        return ResponseEntity.created(new URI("/api/political-parties/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /political-parties : Updates an existing politicalParty.
     *
     * @param politicalPartyDTO the politicalPartyDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated politicalPartyDTO,
     * or with status 400 (Bad Request) if the politicalPartyDTO is not valid,
     * or with status 500 (Internal Server Error) if the politicalPartyDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/political-parties")
    @Timed
    public ResponseEntity<PoliticalPartyDTO> updatePoliticalParty(@RequestBody PoliticalPartyDTO politicalPartyDTO) throws URISyntaxException {
        log.debug("REST request to update PoliticalParty : {}", politicalPartyDTO);
        if (politicalPartyDTO.getId() == null) {
            return createPoliticalParty(politicalPartyDTO);
        }
        PoliticalPartyDTO result = politicalPartyService.save(politicalPartyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, politicalPartyDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /political-parties : get all the politicalParties.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of politicalParties in body
     */
    @GetMapping("/political-parties")
    @Timed
    public ResponseEntity<List<PoliticalPartyDTO>> getAllPoliticalParties(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of PoliticalParties");
        Page<PoliticalPartyDTO> page = politicalPartyService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/political-parties");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /political-parties/:id : get the "id" politicalParty.
     *
     * @param id the id of the politicalPartyDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the politicalPartyDTO, or with status 404 (Not Found)
     */
    @GetMapping("/political-parties/{id}")
    @Timed
    public ResponseEntity<PoliticalPartyDTO> getPoliticalParty(@PathVariable Long id) {
        log.debug("REST request to get PoliticalParty : {}", id);
        PoliticalPartyDTO politicalPartyDTO = politicalPartyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(politicalPartyDTO));
    }

    /**
     * DELETE  /political-parties/:id : delete the "id" politicalParty.
     *
     * @param id the id of the politicalPartyDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/political-parties/{id}")
    @Timed
    public ResponseEntity<Void> deletePoliticalParty(@PathVariable Long id) {
        log.debug("REST request to delete PoliticalParty : {}", id);
        politicalPartyService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/political-parties?query=:query : search for the politicalParty corresponding
     * to the query.
     *
     * @param query the query of the politicalParty search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/political-parties")
    @Timed
    public ResponseEntity<List<PoliticalPartyDTO>> searchPoliticalParties(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of PoliticalParties for query {}", query);
        Page<PoliticalPartyDTO> page = politicalPartyService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/political-parties");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
