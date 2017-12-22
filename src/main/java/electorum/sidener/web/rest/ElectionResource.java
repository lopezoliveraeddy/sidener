package electorum.sidener.web.rest;

import com.codahale.metrics.annotation.Timed;

import electorum.sidener.service.ElectionService;
import electorum.sidener.web.rest.util.HeaderUtil;
import electorum.sidener.web.rest.util.PaginationUtil;
import electorum.sidener.service.dto.ElectionDTO;
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
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Election.
 */
@RestController
@RequestMapping("/api")
public class ElectionResource {

    private final Logger log = LoggerFactory.getLogger(ElectionResource.class);

    private static final String ENTITY_NAME = "election";

    private final ElectionService electionService;

    public ElectionResource(ElectionService electionService) {
        this.electionService = electionService;
    }

    /**
     * POST  /elections : Create a new election.
     *
     * @param electionDTO the electionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new electionDTO, or with status 400 (Bad Request) if the election has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/elections")
    @Timed
    public ResponseEntity<ElectionDTO> createElection(@RequestBody ElectionDTO electionDTO) throws URISyntaxException {
        log.debug("REST request to save Election : {}", electionDTO);
        if (electionDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new election cannot already have an ID")).body(null);
        }
        /*Start Database file management*/
        byte[] dbFile = electionDTO.getDbFile();
        ElectionDTO result = electionService.save(electionDTO);
        if(dbFile != null) {
        		try {
        			FileUtils.writeByteArrayToFile(new File("/files/database/" + result.getId() + ".csv"), dbFile);
        		}catch (IOException e) {
        			e.printStackTrace();
				}
        }
        /*end database file management*/
    
        return ResponseEntity.created(new URI("/api/elections/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /elections : Updates an existing election.
     *
     * @param electionDTO the electionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated electionDTO,
     * or with status 400 (Bad Request) if the electionDTO is not valid,
     * or with status 500 (Internal Server Error) if the electionDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/elections")
    @Timed
    public ResponseEntity<ElectionDTO> updateElection(@RequestBody ElectionDTO electionDTO) throws URISyntaxException {
        log.debug("REST request to update Election : {}", electionDTO);
        /*Start Database file management*/
        if (electionDTO.getId() == null) {
            return createElection(electionDTO);
        }
        if(electionDTO.getDbFile() != null) {
        		Path dbExisting = Paths.get("/files/database/" + electionDTO.getId() + ".csv");
        		if(Files.exists(dbExisting)) {
        			try {
        				Files.delete(dbExisting);
        			}catch(IOException e) {
        				e.printStackTrace();
        			}
        		}
        }
        try {
        	FileUtils.writeByteArrayToFile(new File("/files/database/" + electionDTO.getId() + ".csv"), electionDTO.getDbFile());
        }catch (IOException e) {
            e.printStackTrace();
        }
        /*end database file management*/
        ElectionDTO result = electionService.save(electionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, electionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /elections : get all the elections.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of elections in body
     */
    @GetMapping("/elections")
    @Timed
    public ResponseEntity<List<ElectionDTO>> getAllElections(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Elections");
        Page<ElectionDTO> page = electionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/elections");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /elections/:id : get the "id" election.
     *
     * @param id the id of the electionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the electionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/elections/{id}")
    @Timed
    public ResponseEntity<ElectionDTO> getElection(@PathVariable Long id) {
        log.debug("REST request to get Election : {}", id);
        ElectionDTO electionDTO = electionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(electionDTO));
    }

    /**
     * DELETE  /elections/:id : delete the "id" election.
     *
     * @param id the id of the electionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/elections/{id}")
    @Timed
    public ResponseEntity<Void> deleteElection(@PathVariable Long id) {
        log.debug("REST request to delete Election : {}", id);
        /*Start Database file management*/
        Path dbFile = Paths.get("/files/database/" + id + ".csv");
        if(Files.exists(dbFile)){
            try {
                Files.delete(dbFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        /*end database file management*/
        electionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/elections?query=:query : search for the election corresponding
     * to the query.
     *
     * @param query the query of the election search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/elections")
    @Timed
    public ResponseEntity<List<ElectionDTO>> searchElections(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Elections for query {}", query);
        Page<ElectionDTO> page = electionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/elections");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
