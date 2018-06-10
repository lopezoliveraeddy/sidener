package electorum.sidener.web.rest;

import com.codahale.metrics.annotation.Timed;

import electorum.sidener.service.CausalService;
import electorum.sidener.service.DistrictService;
import electorum.sidener.service.ElectionService;
import electorum.sidener.service.PollingPlaceService;
import electorum.sidener.service.dto.*;
import electorum.sidener.service.util.Documents;
import electorum.sidener.service.util.RecountDemand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@RestController
@RequestMapping("/api")

public class DocumentResource {

    private final Logger log = LoggerFactory.getLogger(DocumentResource.class);
    private final DistrictService districtService;
    private final ElectionService electionService;
    private final PollingPlaceService pollingPlaceService;
    private final CausalService causalService;

    public DocumentResource(DistrictService districtService, ElectionService electionService, PollingPlaceService pollingPlaceService, CausalService causalService) {
        this.districtService = districtService;
        this.electionService = electionService;
        this.pollingPlaceService = pollingPlaceService;
        this.causalService = causalService;
    }


    @PostMapping("/file/")
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

        File file = new File("/Desarrollo/files/" + filename);

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

    @PostMapping("/file/getdemandnulity")
    @Timed
    public ResponseEntity<Resource> getDemandNulity( @RequestParam Long idDistrito) throws IOException{
        log.debug("Entra a getDemandNulity con ID {} ", idDistrito);

        DistrictDTO districtDTO = districtService.findOne(idDistrito);
        ElectionDTO electionDTO = electionService.findOne(districtDTO.getElectionId());
        RecountDemand recountDemand = new RecountDemand();
        String filename = "demanda-nulidad-distrito-"+idDistrito+"-eleccion-"+districtDTO.getElectionId().toString()+".doc";

        File file = new File("/Desarrollo/files/demandas/"+ filename);
        recountDemand.generateNulityDemand(electionDTO, districtDTO, file, filename);
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

    @PostMapping("/file/demandpolling")
    @Timed
    public ResponseEntity<Resource>getDemandPolling(@RequestParam Long pollingplaces) throws IOException{
        log.debug("----  String pollingplaces {} ----",pollingplaces);
        List<PollingPlaceDTO> pollingPlaceList = pollingPlaceService.getPollingPlaceChallegented(pollingplaces);

         Long election = 0L;
         RecountDemand recountDemand= new RecountDemand();
         DistrictDTO district = districtService.findOne(pollingplaces);
         election = district.getElectionId();


        List<CausalDTO> listaCompletaCausales = causalService.getAllCausalsList();
        Map<Integer, CausalPollingRelationDTO> listaTratadaCausales = new HashMap<Integer,CausalPollingRelationDTO>();

        for(CausalDTO causalDTO : listaCompletaCausales){
            CausalPollingRelationDTO causalPollingRelationDTO = new CausalPollingRelationDTO();
            causalPollingRelationDTO.setId(causalDTO.getId());
            causalPollingRelationDTO.setNombreCausal(causalDTO.getName());

            listaTratadaCausales.put((int)(long)causalDTO.getId(),causalPollingRelationDTO );
        }

        String filename = "demanda-distrito-"+pollingplaces+"-"+election+"-eleccion.doc";
        File file = new File("/Desarrollo/files/demandas/" + filename);
        ElectionDTO electionDTO = electionService.findOne(election);

        recountDemand.generateRecountDemandPollingPlace(pollingPlaceList , electionDTO,filename,district,listaTratadaCausales);

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




    @PostMapping("/file/demand")
    @Timed
    public ResponseEntity<Resource> getDemand(@RequestParam String districts) throws IOException{
        String[] districtsList = districts.split("-");
        List<DistrictDTO> districtDTOList = new ArrayList<DistrictDTO>();
        Long election = 0L;
        RecountDemand recountDemand= new RecountDemand();
        String filename = "demanda-distrito-"+districts+"-"+election+"-eleccion.doc";
        File file = new File("/Desarrollo/files/demandas/" + filename);


        for (String s:
            districtsList) {
            districtDTOList.add(districtService.findOne(Long.parseLong(s)));
            election = districtService.findOne(Long.parseLong(s)).getElectionId();
        }

        ElectionDTO electionDTO = electionService.findOne(election);
        recountDemand.generateRecountDemand(districtDTOList, electionDTO,filename);

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
