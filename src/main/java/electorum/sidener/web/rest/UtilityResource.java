package electorum.sidener.web.rest;

import java.net.URISyntaxException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import electorum.sidener.service.dto.ElectionTypeDTO;
import electorum.sidener.service.dto.LoadDTO;
import electorum.sidener.web.rest.util.HeaderUtil;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/api")
public class UtilityResource {
	private final Logger log = LoggerFactory.getLogger(AccountResource.class);
	private static final String ENTITY_NAME = "electionType";
	@GetMapping("/election-by-file")
	public ResponseEntity<List<ElectionTypeDTO>> getAllElectionTypes(@RequestBody LoadDTO loadDTO)  throws URISyntaxException {
	    log.debug("REST request to save ElectionType : {}", loadDTO);
        if (loadDTO.getEleccion() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new electionType cannot already have an ID")).body(null);
        }
        byte[] dbFile = loadDTO.getDbFile();
        
        /* -- */
        
		return null;
	}

}
