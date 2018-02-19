package electorum.sidener.web.rest;

import com.codahale.metrics.annotation.Timed;
import electorum.sidener.domain.ElectionType;
import electorum.sidener.service.DistrictService;
import electorum.sidener.service.ElectionService;
import electorum.sidener.service.ElectionTypeService;
import electorum.sidener.service.dto.DistrictDTO;
import electorum.sidener.service.dto.ElectionDTO;
import electorum.sidener.service.dto.ElectionTypeDTO;
import electorum.sidener.service.util.Documents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;


@RestController
@RequestMapping("/api")

public class DocumentResource {

    private final Logger log = LoggerFactory.getLogger(DocumentResource.class);
    private final DistrictService districtService;
    private final ElectionService electionService;

    public DocumentResource(DistrictService districtService, ElectionService electionService) {
        this.districtService = districtService;
        this.electionService = electionService;
    }


    @GetMapping("/file/")
    @Timed()
    public ResponseEntity<Resource> getFile(@RequestParam Long idDistrito) throws IOException{
        log.debug("getFile -- parameter -> {}", idDistrito );
        DistrictDTO districtDTO = new DistrictDTO();
        ElectionDTO electionDTO = new ElectionDTO();
        ElectionTypeDTO electionTypeDTO;
        Documents documents = new Documents();
        districtDTO = districtService.findOne(idDistrito);
        electionDTO = electionService.findOne(districtDTO.getElectionId());


        log.debug("DISTRITO A BUSCAR : {}", districtDTO);
        log.debug("ELECCION :  {}", electionDTO);




        String filename = "demanda-distrito-"+districtDTO.getId()+"-eleccion-"+electionDTO.getId()+".doc";
        documents.generateWord(districtDTO,electionDTO, filename);

        File file = new File("/files/" + filename);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        headers.add("Content-Disposition", "inline; filename=\"" + filename + "\"");

        Path path = Paths.get(file.getAbsolutePath());

        try{
            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

            String mimeType = MimetypesFileTypeMap
                .getDefaultFileTypeMap()
                .getContentType(file);

            MediaType mediaType = MediaType.parseMediaType(mimeType);
            log.debug("MediaType {}",mediaType.toString());
            return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(mediaType)
                .body(resource);
        }catch (NoSuchFileException e) {
            return ResponseEntity.notFound().headers(headers).build();
        }




    }

}
