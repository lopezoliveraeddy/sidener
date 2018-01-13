package electorum.sidener.web.rest;

import com.codahale.metrics.annotation.Timed;
import electorum.sidener.config.ApplicationProperties;
import electorum.sidener.service.ArchiveService;
import electorum.sidener.service.StorageException;
import electorum.sidener.service.dto.ArchiveDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * REST controller for managing File.
 */
@RestController
@RequestMapping("/public/api")
public class PublicArchive {

    private final Logger log = LoggerFactory.getLogger(ArchiveResource.class);

    private static final String ENTITY_NAME = "archive";

    private final ArchiveService archiveService;

    private final Path rootLocation;

    public PublicArchive(ArchiveService archiveService, ApplicationProperties applicationProperties) {
        this.archiveService = archiveService;
        this.rootLocation = Paths.get(applicationProperties.getStorage().getLocation());
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

    @GetMapping("/archives/download")
    @Timed
    public ResponseEntity<Resource> download(@RequestParam String filename) throws IOException {
        log.info("Consultando Archive: {}", filename);
        File file = new File(this.rootLocation + "/" + filename);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        headers.add("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");

        Path path = Paths.get(file.getAbsolutePath());

        try {
            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
            String mimeType = MimetypesFileTypeMap
                .getDefaultFileTypeMap()
                .getContentType(file);
            MediaType mediaType = MediaType.parseMediaType(mimeType);

            return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(mediaType)
                .body(resource);
        } catch (NoSuchFileException e) {
            return ResponseEntity.notFound().headers(headers).build();
        }
    }

    @PostConstruct
    public void postConstruct() throws Exception {
        try {
            if (!Files.exists(this.rootLocation)) {
                Files.createDirectory(this.rootLocation);
            }
        }
        catch (IOException e) {
            throw new StorageException("Failed to create root store directory " + this.rootLocation, e);
        }

    }

}
