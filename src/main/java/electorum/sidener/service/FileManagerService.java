package electorum.sidener.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.ObjectMapper;
import electorum.sidener.config.ApplicationProperties;
import electorum.sidener.domain.FlowInfo;
import electorum.sidener.domain.enumeration.ArchiveStatus;
import electorum.sidener.service.dto.ArchiveDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Component
public class FileManagerService {
    private final Logger log = LoggerFactory.getLogger(FileManagerService.class);

    private static final String ENTITY_NAME = "document";
    final static Pattern PATTERN = Pattern.compile("(.*?)(?:\\((\\d+)\\))?(\\.[^.]*)?");
    final static int NUM_ITEMS_PER_PAGE_CRON = 50;
    private final ObjectMapper objectMapper ;
    private final ArchiveService archiveService;
    private final Path tmpLocation;
    private final Path rootLocation;


    public FileManagerService(ArchiveService archiveService, ObjectMapper objectMapper, ApplicationProperties applicationProperties){
        this.archiveService = archiveService;
        this.objectMapper = objectMapper;
        this.rootLocation = Paths.get(applicationProperties.getStorage().getLocation());
        this.tmpLocation = Paths.get(applicationProperties.getStorage().getTemporal());

    }


    private  boolean copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        boolean success = false;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            success = true;
        } finally {
            is.close();
            os.close();
        }
        return success;
    }

    public File getNewName(String name) {
        File file = new File(name);
        String filename = name;
        if (file.exists() && !file.isDirectory() ) {
            Matcher m = PATTERN.matcher(name);
            if (m.matches()) {
                String prefix = m.group(1);
                String last = m.group(2);
                String suffix = m.group(3);
                if (suffix == null) suffix = "";
                int count = last != null ? Integer.parseInt(last) : 0;
                do {
                    count++;
                    log.debug("contador "+ count);
                    filename = prefix + "(" + count + ")" + suffix;
                    file = new File(filename);
                } while (file.exists() && !file.isDirectory());
            }
        }
        return file;
    }

    /*
     * En tmp sobreescribe el archivo si existe.
     * En files comprueba el nombre para copiarlo con el nuevo obtenido
     * */
    private File copyFiles(String filename, String directoryName) throws IOException { //after
        boolean success = false;
        String finalPath = this.rootLocation + "/" + directoryName +"/"+ filename;
        String tmpPath = this.tmpLocation + "/"+ filename;
        File finalFile = new File(finalPath);
        File directory = new File(this.rootLocation + "/" +directoryName);
        if (!directory.exists()) {
            directory.mkdir();
        }
        File tmpFile = new File(tmpPath);
        if(tmpFile.exists() && !tmpFile.isDirectory() && directory.exists() && directory.isDirectory()){
            if(finalFile.exists() && !finalFile.isDirectory()){
                finalFile =  getNewName(finalPath);
                log.debug("compareToFiles -> rename to "+ finalFile.getName());
            }
            success = copyFileUsingStream(tmpFile, finalFile);
        }

        log.debug("success copy..."+success);
        return finalFile;
    }


    public String save(FlowInfo flowInfo, ArchiveDTO archiveDTO) throws IOException{
        File file = copyFiles(flowInfo.getFlowFilename(), archiveDTO.getPath());
        String mimeType = Files.probeContentType(file.toPath());
        ArchiveDTO archive = new ArchiveDTO();
        archive.setName(file.getName());
        archive.setMimeType(mimeType);
        archive.setDescription("");
        archive.setPath(file.getAbsolutePath());
        archive.setSizeLength(flowInfo.getFlowTotalSize());
        archive.setId(null);
        archive.setStatus(archive.getStatus());
        ArchiveDTO result = archiveService.save(archive);
        String jsonResult = objectMapper.writeValueAsString(result);
        return jsonResult;

    }

    /* ${myvar: defaultvalue}
	 * si no encuentra el valor 'myvar' pone en valor default que le sigue despues de :
	 * */
    @Scheduled(cron = "${application.scheduled.deletefiles: 0 0 23 * * *}")
    public void cleanDirectories() {
        log.debug("@Scheduled task: Remove files with status TEMPORARY");
        int count = 0;

        Pageable pageable = new PageRequest(count,NUM_ITEMS_PER_PAGE_CRON );
        List<ArchiveDTO> list = archiveService.findByStatus(ArchiveStatus.TEMPORARY,pageable).getContent();
        do {
            try {
                removeFiles(list);
            } catch (IOException e) {
                log.debug("IOException: {}", e.getMessage());
            }
            count ++;
            pageable = new PageRequest(count,NUM_ITEMS_PER_PAGE_CRON );
            list = archiveService.findAll(pageable).getContent();
        } while (list.size() > 0);

        try {
            deleteTemporalDir(this.tmpLocation.toFile());
        } catch (IOException e) {
            log.debug("IOException: {}"+e.getMessage());
        }
    }

    private void removeFiles(List<ArchiveDTO> list) throws IOException {
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            ArchiveDTO archiveDTO = (ArchiveDTO) iterator.next();
            try {
                File file = new File(archiveDTO.getPath());
                if(!file.isDirectory() && archiveDTO.getStatus().equals(ArchiveStatus.TEMPORARY)) {
                    if(file.exists()) {
                        if(file.delete()) {
                            log.debug("Delete file: success. File: {}", file.getName());
                        } else {
                            log.debug("Delete file: failed. File: {}", file.getName());
                        }
                    }
                    archiveService.delete(archiveDTO.getId());
                }
            } catch(SecurityException  e){
                log.debug("SecurityException: {}",e.getMessage());
            }
        }
    }

    private void deleteTemporalDir(File dir) throws IOException {
        log.debug("@Scheduled task: delete temporal directory");
        log.debug("{} {} ",dir.getAbsolutePath(), dir.getName());
        if (dir.exists() && dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                if(!file.isDirectory() && !file.isHidden()) {
                    if(file.delete()){
                        log.debug("Delete file: success. File: {}", file.getName());
                    }else {
                        log.debug("Delete file: failed. File: {}", file.getName());

                    }
                }
            }
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
        try {
            if (!Files.exists(this.tmpLocation)) {
                Files.createDirectory(this.tmpLocation);
            }
        }
        catch (IOException e) {
            throw new StorageException("Failed to create temporal directory " + this.tmpLocation, e);
        }
    }

}
