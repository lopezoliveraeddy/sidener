package electorum.sidener.web.rest;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import electorum.sidener.domain.PollingPlace;
import electorum.sidener.service.dto.*;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.opencsv.*;

import electorum.sidener.service.PollingPlaceService;
import electorum.sidener.web.rest.util.ElectionFromFile;
import electorum.sidener.web.rest.util.HeaderUtil;
import electorum.sidener.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

/**
 * REST controller for managing PollingPlace.
 */
@RestController
@RequestMapping("/api")
public class PollingPlaceResource {

    private final Logger log = LoggerFactory.getLogger(PollingPlaceResource.class);

    private static final String ENTITY_NAME = "pollingPlace";

    private final PollingPlaceService pollingPlaceService;

    public PollingPlaceResource(PollingPlaceService pollingPlaceService) {
        this.pollingPlaceService = pollingPlaceService;
    }

    /**
     * POST /polling-places : Create a new pollingPlace.
     *
     * @param pollingPlaceDTO the pollingPlaceDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new
     * pollingPlaceDTO, or with status 400 (Bad Request) if the pollingPlace
     * has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/polling-places")
    @Timed
    public ResponseEntity<PollingPlaceDTO> createPollingPlace(@RequestBody PollingPlaceDTO pollingPlaceDTO)
        throws URISyntaxException {
        log.debug("REST request to save PollingPlace : {}", pollingPlaceDTO);
        if (pollingPlaceDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists",
                "A new pollingPlace cannot already have an ID")).body(null);
        }
        PollingPlaceDTO result = pollingPlaceService.save(pollingPlaceDTO);
        return ResponseEntity.created(new URI("/api/polling-places/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
    }

    /**
     * PUT /polling-places : Updates an existing pollingPlace.
     *
     * @param pollingPlaceDTO the pollingPlaceDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated
     * pollingPlaceDTO, or with status 400 (Bad Request) if the
     * pollingPlaceDTO is not valid, or with status 500 (Internal Server
     * Error) if the pollingPlaceDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/polling-places")
    @Timed
    public ResponseEntity<PollingPlaceDTO> updatePollingPlace(@RequestBody PollingPlaceDTO pollingPlaceDTO)
        throws URISyntaxException {
        log.debug("REST request to update PollingPlace : {}", pollingPlaceDTO);
        if (pollingPlaceDTO.getId() == null) {
            return createPollingPlace(pollingPlaceDTO);
        }
        PollingPlaceDTO result = pollingPlaceService.save(pollingPlaceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pollingPlaceDTO.getId().toString()))
            .body(result);
    }

    @PutMapping("/process-places")
    @Timed
    public ResponseEntity<List<PollingPlaceDTO>> createPollingPlaces(@RequestBody LoadDTO loadDTO)
        throws URISyntaxException {
        log.debug("REST request to process a bunch of records");
        byte[] dbFile = loadDTO.getDbFile();
        try {
            String decoded = new String(dbFile, "UTF-8");
            log.debug("CSV {}", decoded);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;

    }

    /**
     * GET /polling-places : get all the pollingPlaces.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of pollingPlaces
     * in body
     */
    @GetMapping("/polling-places")
    @Timed
    public ResponseEntity<List<PollingPlaceDTO>> getAllPollingPlaces(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of PollingPlaces");
        Page<PollingPlaceDTO> page = pollingPlaceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/polling-places");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET /polling-places/:id : get the "id" pollingPlace.
     *
     * @param id the id of the pollingPlaceDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the
     * pollingPlaceDTO, or with status 404 (Not Found)
     */
    @GetMapping("/polling-places/{id}")
    @Timed
    public ResponseEntity<PollingPlaceDTO> getPollingPlace(@PathVariable Long id) {
        log.debug("REST request to get PollingPlace : {}", id);
        PollingPlaceDTO pollingPlaceDTO = pollingPlaceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pollingPlaceDTO));
    }

    /**
     * DELETE /polling-places/:id : delete the "id" pollingPlace.
     *
     * @param id the id of the pollingPlaceDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/polling-places/{id}")
    @Timed
    public ResponseEntity<Void> deletePollingPlace(@PathVariable Long id) {
        log.debug("REST request to delete PollingPlace : {}", id);
        pollingPlaceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH /_search/polling-places?query=:query : search for the pollingPlace
     * corresponding to the query.
     *
     * @param query    the query of the pollingPlace search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/polling-places")
    @Timed
    public ResponseEntity<List<PollingPlaceDTO>> searchPollingPlaces(@RequestParam String query,
                                                                     @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of PollingPlaces for query {}", query);
        Page<PollingPlaceDTO> page = pollingPlaceService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page,
            "/api/_search/polling-places");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET POLLING PLACES /districts/{id}/polling-places : get pollingPlaces by "idDistrict".
     *
     * @param idDistrict the "id" of the District
     * @param pageable   the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of pollingPlaces
     * in body
     */
    @GetMapping("/recount/{idDistrict}/polling-places")
    @Timed
    public ResponseEntity<List<PollingPlaceRecountDTO>> getPollingPlacesByIdDistrict(@PathVariable Long idDistrict,
                                                                                     @ApiParam Pageable pageable) {
        log.debug("REST request to get PollingPlaces by District : {}", idDistrict);
        Page<PollingPlaceRecountDTO> page = pollingPlaceService.getPollingPlacesByIdDistrict(idDistrict, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/recount/{idDistrict}/polling-places");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET POLLING PLACES WON AND LOSE  /recount/{idDistrict}/polling-places-won-lose : get pollingPlaces-won-lose by "idDistrict"
     *
     * @param idDistrict the "idDistrict" of the pollingPlace
     * @return the ResponseEntity with status 200 (OK)
     */
    @GetMapping("/recount/{idDistrict}/polling-places-won-lose")
    @Timed
    public ResponseEntity<PollingPlaceWonLoseDTO> pollingPlacesWonLose(@PathVariable Long idDistrict) {
        log.debug("REST request to get the PollingPlaces won-lose by District : {}", idDistrict);
        PollingPlaceWonLoseDTO pollingPlaceWonLoseDTO = pollingPlaceService.pollingPlaceWonLose(idDistrict);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pollingPlaceWonLoseDTO));
    }

    /**
     * Post /election : Create a new election data from file
     *
     * @param loadDTO the loadDTO to create
     * @return ResponseEntity with status 201 (Created) and with body the new
     * loadDTO
     * @trhows URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/election")
    @Timed
    @SuppressWarnings("null")
    public ResponseEntity<List<ElectionDTO>> createElectionFile(@RequestBody LoadDTO loadDTO)
        throws URISyntaxException {
        log.debug("---> REST request to save LoadDTO : {}", loadDTO);
        ElectionFromFile electionFromFile = new ElectionFromFile();
        byte[] csvFile = loadDTO.getDbFile();
        List<PollingPlaceDTO> pollingPlaceDTOList = new ArrayList<PollingPlaceDTO>();

        // se carga en un temporal
        if (csvFile != null) {
            try {
                FileUtils.writeByteArrayToFile(new File("/Desarrollo/tmp/" + loadDTO.getEleccion() + ".csv"), csvFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            Reader reader = Files.newBufferedReader(Paths.get("/Desarrollo/tmp/" + loadDTO.getEleccion() + ".csv"));
            CSVReader csvReader = new CSVReader(reader);
            pollingPlaceDTOList = electionFromFile.processFile(csvReader, loadDTO.getEleccion());
            for (Iterator iterator = pollingPlaceDTOList.iterator(); iterator.hasNext(); ) {
                PollingPlaceDTO pollingPlaceDTO = (PollingPlaceDTO) iterator.next();
                log.debug("pollingPlace : {} ", pollingPlaceDTO.toString());
                pollingPlaceService.save(pollingPlaceDTO);

            }


        } catch (IOException e) {
            e.printStackTrace();
        }




        /* regresa resultados */
        return null;
    }



}
