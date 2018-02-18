package electorum.sidener.web.rest;

import com.codahale.metrics.annotation.Timed;
import electorum.sidener.service.ArchiveService;
import electorum.sidener.service.FileManagerService;
import electorum.sidener.web.rest.util.HeaderUtil;
import electorum.sidener.web.rest.util.PaginationUtil;
import electorum.sidener.service.dto.ArchiveDTO;
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

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Archive.
 */
@RestController
@RequestMapping("/api")
public class ArchiveResource {

    private final Logger log = LoggerFactory.getLogger(ArchiveResource.class);

    private static final String ENTITY_NAME = "archive";

    private final ArchiveService archiveService;

    private final FileManagerService fileManagerService;

    public ArchiveResource(ArchiveService archiveService, FileManagerService fileManagerService) {
        this.archiveService = archiveService;
        this.fileManagerService = fileManagerService;
    }

    /**
     * POST  /archives : Create a new archive.
     *
     * @param archiveDTO the archiveDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new archiveDTO, or with status 400 (Bad Request) if the archive has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/archives")
    @Timed
    public ResponseEntity<ArchiveDTO> createArchive(@RequestBody ArchiveDTO archiveDTO) throws URISyntaxException {
        log.debug("REST request to save Archive : {}", archiveDTO);
        if (archiveDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new archive cannot already have an ID")).body(null);
        }
        ArchiveDTO result = archiveService.save(archiveDTO);
        return ResponseEntity.created(new URI("/api/archives/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /archives : Updates an existing archive.
     *
     * @param archiveDTO the archiveDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated archiveDTO,
     * or with status 400 (Bad Request) if the archiveDTO is not valid,
     * or with status 500 (Internal Server Error) if the archiveDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/archives")
    @Timed
    public ResponseEntity<ArchiveDTO> updateArchive(@RequestBody ArchiveDTO archiveDTO) throws URISyntaxException {
        log.debug("REST request to update Archive : {}", archiveDTO);
        if (archiveDTO.getId() == null) {
            return createArchive(archiveDTO);
        }
        ArchiveDTO result = archiveService.save(archiveDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, archiveDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /archives : get all the archives.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of archives in body
     */
    @GetMapping("/archives")
    @Timed
    public ResponseEntity<List<ArchiveDTO>> getAllArchives(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Archives");
        Page<ArchiveDTO> page = archiveService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/archives");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /archives/:id : get the "id" archive.
     *
     * @param id the id of the archiveDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the archiveDTO, or with status 404 (Not Found)
     */
    @GetMapping("/archives/{id}")
    @Timed
    public ResponseEntity<ArchiveDTO> getArchive(@PathVariable Long id) {
        log.debug("REST request to get Archive : {}", id);
        ArchiveDTO archiveDTO = archiveService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(archiveDTO));
    }

    /**
     * DELETE  /archives/:id : delete the "id" archive.
     *
     * @param id the id of the archiveDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/archives/{id}")
    @Timed
    public ResponseEntity<Void> deleteArchive(@PathVariable Long id) {
        log.debug("REST request to delete Archive : {}", id);
        archiveService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/archives?query=:query : search for the archive corresponding
     * to the query.
     *
     * @param query the query of the archive search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/archives")
    @Timed
    public ResponseEntity<List<ArchiveDTO>> searchArchives(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Archives for query {}", query);
        Page<ArchiveDTO> page = archiveService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/archives");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
