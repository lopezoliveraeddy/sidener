package electorum.sidener.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.opencsv.CSVReader;

import electorum.sidener.service.PollingPlaceService;
import electorum.sidener.web.rest.util.HeaderUtil;
import electorum.sidener.web.rest.util.PaginationUtil;
import electorum.sidener.service.dto.ElectionDTO;
import electorum.sidener.service.dto.LoadDTO;
import electorum.sidener.service.dto.PollingPlaceDTO;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

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
     * POST  /polling-places : Create a new pollingPlace.
     *
     * @param pollingPlaceDTO the pollingPlaceDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pollingPlaceDTO, or with status 400 (Bad Request) if the pollingPlace has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/polling-places")
    @Timed
    public ResponseEntity<PollingPlaceDTO> createPollingPlace(@RequestBody PollingPlaceDTO pollingPlaceDTO) throws URISyntaxException {
        log.debug("REST request to save PollingPlace : {}", pollingPlaceDTO);
        if (pollingPlaceDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new pollingPlace cannot already have an ID")).body(null);
        }
        PollingPlaceDTO result = pollingPlaceService.save(pollingPlaceDTO);
        return ResponseEntity.created(new URI("/api/polling-places/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /polling-places : Updates an existing pollingPlace.
     *
     * @param pollingPlaceDTO the pollingPlaceDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pollingPlaceDTO,
     * or with status 400 (Bad Request) if the pollingPlaceDTO is not valid,
     * or with status 500 (Internal Server Error) if the pollingPlaceDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/polling-places")
    @Timed
    public ResponseEntity<PollingPlaceDTO> updatePollingPlace(@RequestBody PollingPlaceDTO pollingPlaceDTO) throws URISyntaxException {
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
    public ResponseEntity<List<PollingPlaceDTO>> createPollingPlaces (@RequestBody LoadDTO loadDTO) throws URISyntaxException{
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
     * GET  /polling-places : get all the pollingPlaces.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of pollingPlaces in body
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
     * GET  /polling-places/:id : get the "id" pollingPlace.
     *
     * @param id the id of the pollingPlaceDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pollingPlaceDTO, or with status 404 (Not Found)
     */
    @GetMapping("/polling-places/{id}")
    @Timed
    public ResponseEntity<PollingPlaceDTO> getPollingPlace(@PathVariable Long id) {
        log.debug("REST request to get PollingPlace : {}", id);
        PollingPlaceDTO pollingPlaceDTO = pollingPlaceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pollingPlaceDTO));
    }

    /**
     * DELETE  /polling-places/:id : delete the "id" pollingPlace.
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
     * SEARCH  /_search/polling-places?query=:query : search for the pollingPlace corresponding
     * to the query.
     *
     * @param query the query of the pollingPlace search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/polling-places")
    @Timed
    public ResponseEntity<List<PollingPlaceDTO>> searchPollingPlaces(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of PollingPlaces for query {}", query);
        Page<PollingPlaceDTO> page = pollingPlaceService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/polling-places");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET DISTRICTS  /districts/{id}/polling-places : get pollingPlaces by "id" district
     * to the query.
     *
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/districts/{id}/polling-places")
    @Timed
    public ResponseEntity<List<PollingPlaceDTO>> getDistrictsByIdElection(@PathVariable Long id, @ApiParam Pageable pageable) {
        log.debug("REST request to get PollingPlaces by District : {}", id);
        Page<PollingPlaceDTO> page = pollingPlaceService.getPollingPlacesByIdDistrict(id, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/districts/{id}/polling-places");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    /**
     * Post /election : Create a new election data from file
     * @param loadDTO the loadDTO to create
     * @return ResponseEntity with status 201 (Created) and with body the new loadDTO
     * @trhows URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/election")
    @Timed
	@SuppressWarnings("null")
    public ResponseEntity<List<ElectionDTO>> createElectionFile(@RequestBody LoadDTO loadDTO ) throws URISyntaxException {
    		log.debug("---> REST request to save LoadDTO : {}", loadDTO);	
    		byte[] csvFile = loadDTO.getDbFile();    
    		int it                  = 0;
    		int topeDatos           = 0;
    		int topePartidos        = 0;
    		int topeCoaliciones     = 0;
    		int topeCandidatosInd   = 0;
    		List<String>  partidos = new ArrayList<String>();
    		
    		
        //se carga en un temporal
    		if(csvFile != null) {
	    		try {
	    			FileUtils.writeByteArrayToFile(new File("/files/tmp/" + loadDTO.getEleccion() + ".csv"), csvFile);
	    		}catch (IOException e) {
	    			e.printStackTrace();
			}
        }
    		/*se procesa el archivo temporal*/
		try {
			Reader reader = Files.newBufferedReader(Paths.get("/files/tmp/" + loadDTO.getEleccion() + ".csv"));
			CSVReader csvReader = new CSVReader(reader);
			String[] nextRecord;
		
			if (it <= 0) {
				nextRecord        = csvReader.readNext();
				topeDatos         = ArrayUtils.indexOf(nextRecord,"S1");
				topePartidos      = ArrayUtils.indexOf(nextRecord,"S2");
				topeCoaliciones   = ArrayUtils.indexOf(nextRecord,"S3");
				topeCandidatosInd = ArrayUtils.indexOf(nextRecord,"S4");
				
				for (int k = topeDatos +1 ; k <= topePartidos-1 ; k++) {
					partidos.add(nextRecord[k]);
	    			}
				it+=1;
				
			}
            while ((nextRecord = csvReader.readNext()) != null) {
            		PollingPlaceDTO pollingPlaceDTO = new PollingPlaceDTO();
            		pollingPlaceDTO.setDistrictId(Long.valueOf(nextRecord[0]));
            		pollingPlaceDTO.setSection(nextRecord[1]);
            		pollingPlaceDTO.setTown(nextRecord[2]);
            		pollingPlaceDTO.setTypePollingPlace(null);
            		pollingPlaceDTO.setAddress(nextRecord[4]);
            		pollingPlaceDTO.setLeftoverBallots(Long.valueOf(nextRecord[5]));
            		pollingPlaceDTO.setVotingCitizens(Long.valueOf(nextRecord[6]));
            		pollingPlaceDTO.setTotalVotes((Long.valueOf(nextRecord[8])));
            		
            		
            		/**
            		 * Procesamiento de votos por partido 
            		 * */
            		JSONArray votaciones = new JSONArray();
            		JSONObject partidosVotos = new JSONObject();
            		JSONObject partidosJson = new JSONObject();
            		JSONObject resultadosPrimerLugar = new JSONObject();
            		JSONObject resultadosSegundoLugar = new JSONObject();
            		int j = 0;
            		int primerLugarNumber      = 0;
            		int segundoLugarNumber     = 0;
            		String primerLugarPartido  = "";
            		String segundoLugarPartido = "";
        			
            		for (int k = topeDatos +1 ; k <= topePartidos-1 ; k++) {
        				
        				
            			try {
            					if(nextRecord[k] == "") {
            						nextRecord[k] = "0";
            					}
            					if ( Integer.parseInt(nextRecord[k]) > primerLugarNumber ) {
            						segundoLugarNumber  = primerLugarNumber;
            						segundoLugarPartido = primerLugarPartido;
            						
            						primerLugarNumber   = Integer.parseInt(nextRecord[k]);
            						primerLugarPartido  = partidos.get(j);
            					}
            					if( Integer.parseInt(nextRecord[k]) > segundoLugarNumber  &&  Integer.parseInt(nextRecord[k]) < primerLugarNumber ) {
            						segundoLugarNumber = Integer.parseInt(nextRecord[k]);
            						segundoLugarPartido = partidos.get(j);
            					}
            					partidosJson.put( partidos.get(j), nextRecord[k] );
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
            			j++;
            		}
            		try {
            				resultadosPrimerLugar.put(primerLugarPartido,primerLugarNumber);
            				resultadosSegundoLugar.put(segundoLugarPartido,segundoLugarNumber);
						partidosVotos.put("partidos", partidosJson);
						partidosVotos.put("primer-lugar", resultadosPrimerLugar);
						partidosVotos.put("segundo-lugar", resultadosSegundoLugar);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            		votaciones.put(partidosVotos);
            		log.debug("---> REST request to save LoadDTO : {}", votaciones.toString());	
            	
            		
            		
            		
            		log.debug("--->  save LoadDTO : {}", pollingPlaceDTO.toString());
            		pollingPlaceService.save(pollingPlaceDTO);
            		
            		/*if(pollingPlaceDTO.getDistrictId() != null) {
        				@SuppressWarnings("null")
						PollingPlaceDTO result = pollingPlaceService.save(pollingPlaceDTO);
            		}*/
            		log.debug("<---- NextRecord : {}", nextRecord.toString());
            }

		}catch (IOException e) {
			e.printStackTrace();
		}
    		/*se almacena la elección*/
    		
    		/*regresa resultados*/
    		return null;
    }

}
