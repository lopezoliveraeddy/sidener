package electorum.sidener.web.rest;

import com.codahale.metrics.annotation.Timed;
import electorum.sidener.domain.DetectorCausals;
import electorum.sidener.domain.enumeration.TypeCausal;
import electorum.sidener.service.DetectorCausalsService;
import electorum.sidener.service.dto.CausalDTO;
import electorum.sidener.web.rest.util.HeaderUtil;
import electorum.sidener.service.dto.DetectorCausalsDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing DetectorCausals.
 */
@RestController
@RequestMapping("/api")
public class DetectorCausalsResource {

    private final Logger log = LoggerFactory.getLogger(DetectorCausalsResource.class);

    private static final String ENTITY_NAME = "detectorCausals";

    private final DetectorCausalsService detectorCausalsService;

    public DetectorCausalsResource(DetectorCausalsService detectorCausalsService) {
        this.detectorCausalsService = detectorCausalsService;
    }

    /**
     * POST  /detector-causals : Create a new detectorCausals.
     *
     * @param detectorCausalsDTO the detectorCausalsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new detectorCausalsDTO, or with status 400 (Bad Request) if the detectorCausals has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/detector-causals")
    @Timed
    public ResponseEntity<DetectorCausalsDTO> createDetectorCausals(@RequestBody DetectorCausalsDTO detectorCausalsDTO) throws URISyntaxException {
        log.debug("REST request to save DetectorCausals : {}", detectorCausalsDTO);
        if (detectorCausalsDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new detectorCausals cannot already have an ID")).body(null);
        }
        DetectorCausalsDTO result = detectorCausalsService.save(detectorCausalsDTO);
        return ResponseEntity.created(new URI("/api/detector-causals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /detector-causals : Updates an existing detectorCausals.
     *
     * @param detectorCausalsDTO the detectorCausalsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated detectorCausalsDTO,
     * or with status 400 (Bad Request) if the detectorCausalsDTO is not valid,
     * or with status 500 (Internal Server Error) if the detectorCausalsDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/detector-causals")
    @Timed
    public ResponseEntity<DetectorCausalsDTO> updateDetectorCausals(@RequestBody DetectorCausalsDTO detectorCausalsDTO) throws URISyntaxException {
        log.debug("REST request to update DetectorCausals : {}", detectorCausalsDTO);
        if (detectorCausalsDTO.getId() == null) {
            return createDetectorCausals(detectorCausalsDTO);
        }
        DetectorCausalsDTO result = detectorCausalsService.save(detectorCausalsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, detectorCausalsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /detector-causals : get all the detectorCausals.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of detectorCausals in body
     */
    @GetMapping("/detector-causals")
    @Timed
    public List<DetectorCausalsDTO> getAllDetectorCausals() {
        log.debug("REST request to get all DetectorCausals");
        return detectorCausalsService.findAll();
        }

    /**
     * GET  /detector-causals/:id : get the "id" detectorCausals.
     *
     * @param id the id of the detectorCausalsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the detectorCausalsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/detector-causals/{id}")
    @Timed
    public ResponseEntity<DetectorCausalsDTO> getDetectorCausals(@PathVariable Long id) {
        log.debug("REST request to get DetectorCausals : {}", id);
        DetectorCausalsDTO detectorCausalsDTO = detectorCausalsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(detectorCausalsDTO));
    }

    /**
     * DELETE  /detector-causals/:id : delete the "id" detectorCausals.
     *
     * @param id the id of the detectorCausalsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/detector-causals/{id}")
    @Timed
    public ResponseEntity<Void> deleteDetectorCausals(@PathVariable Long id) {
        log.debug("REST request to delete DetectorCausals : {}", id);
        detectorCausalsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/detector-causals?query=:query : search for the detectorCausals corresponding
     * to the query.
     *
     * @param query the query of the detectorCausals search
     * @return the result of the search
     */
    @GetMapping("/_search/detector-causals")
    @Timed
    public List<DetectorCausalsDTO> searchDetectorCausals(@RequestParam String query) {
        log.debug("REST request to search DetectorCausals for query {}", query);
        return detectorCausalsService.search(query);
    }

    /**
     * GET /detector-causals/polling-place/:idPollingPlace/causal/:idCausal : get detectorCausals by idPollingPlace and idCausal
     *
     *  @param idPollingPlace the "idPollingPlace" of the detectorCausals
     *  @param idCausal the "idCausal" of the detectorCausals
     *  @return the list of entities
     */
    @GetMapping("/detector-causals/polling-place/{idPollingPlace}/causal/{idCausal}")
    @Timed
    public ResponseEntity<DetectorCausalsDTO> getDetectorCausalsByPollingPlace(@PathVariable Long idPollingPlace, @PathVariable Long idCausal) {
        log.debug("REST request to get DetectorCausals by idPollingPlace {} and idCausal {}", idPollingPlace, idCausal);
        DetectorCausalsDTO detectorCausalsDTO = detectorCausalsService.getDetectorCausalsByPollingPlace(idPollingPlace, idCausal);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(detectorCausalsDTO));
    }

}
