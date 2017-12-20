package electorum.sidener.web.rest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import electorum.sidener.config.ApplicationProperties;
import electorum.sidener.domain.enumeration.ArchiveStatus;
import electorum.sidener.domain.enumeration.DocumentStatus;
import electorum.sidener.service.FileManagerService;
import electorum.sidener.service.StorageException;
import electorum.sidener.service.dto.ArchiveDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import electorum.sidener.domain.FlowInfo;
import electorum.sidener.domain.FlowInfoStorage;
import electorum.sidener.domain.HttpUtils;
import org.springframework.web.context.WebApplicationContext;


public class UploadServlet extends HttpServlet{
    private final Logger log = LoggerFactory.getLogger(UploadServlet.class);

    private static final long serialVersionUID = 1L;

    private FileManagerService fileManagerService;

    private Path tmpLocation;

    private Path rootLocation;

    public  UploadServlet(ApplicationProperties properties) {
        this.tmpLocation = Paths.get(properties.getStorage().getTemporal());
        this.rootLocation = Paths.get(properties.getStorage().getLocation());
    }


    public void init(ServletConfig config) {
        log.debug("UploadServlet has been initialized");
        ApplicationContext ac = (ApplicationContext) config.getServletContext().getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        this.fileManagerService = (FileManagerService)ac.getBean("fileManagerService");
        try {
            if(!Files.exists(this.rootLocation)) {
                Files.createDirectory(this.rootLocation);
            }
        } catch (IOException e) {
            throw new StorageException("Failed to create root store directory " + this.rootLocation, e);
        }
        try {
            if (!Files.exists(this.tmpLocation)) {
                Files.createDirectory(this.tmpLocation);

            }
        } catch (IOException e) {
            throw new StorageException("Failed to create temporal directory " + this.tmpLocation, e);
        }
    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        int flowChunkNumber = getflowChunkNumber(request);

        FlowInfo info = getFlowInfo(request);

        RandomAccessFile raf = new RandomAccessFile(info.flowFilePath, "rw");

        // Seek to position
        raf.seek((flowChunkNumber - 1) * info.getFlowChunkSize());

        // Save to file
        InputStream is = request.getInputStream();
        long readed = 0;
        long content_length = request.getContentLength();
        byte[] bytes = new byte[1024 * 100];
        while (readed < content_length) {
            int r = is.read(bytes);
            if (r < 0) {
                break;
            }
            raf.write(bytes, 0, r);
            readed += r;
        }
        raf.close();

        // Mark as uploaded.
        info.uploadedChunks.add(new FlowInfo.flowChunkNumber(flowChunkNumber));
        String archivoFinal = info.checkIfUploadFinished();
        if (archivoFinal != null) { // Check if all chunks uploaded, and
            // change filename
            FlowInfoStorage.getInstance().remove(info);
            log.debug(info.getFlowFilename());
            log.debug(info.toString());
            String resource = fileManagerService.save(info, getDirectory(request));
            response.getWriter().write(resource);

        } else {
            String message= "{\"message\":\"upload\"}";
            response.getWriter().write(message);
        }

        out.close();
    }

    private ArchiveDTO getDirectory(HttpServletRequest request) {
        String directory = "default";
        String basePath = request.getParameter("basePath");
        String status = request.getParameter("status");
        ArchiveDTO archive = new ArchiveDTO();
        if(basePath != null && !basePath.trim().isEmpty()) {
            archive.setPath(basePath);
        } else {
            archive.setPath(directory);
        }
        if(status != null && ArchiveStatus.PERMANENT.toString().equals(status)) {
            archive.setStatus(ArchiveStatus.PERMANENT);
        } else {
            archive.setStatus(ArchiveStatus.TEMPORARY);
        }
        return archive;
    }


    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        int flowChunkNumber = getflowChunkNumber(request);

        PrintWriter out = response.getWriter();
        FlowInfo info = getFlowInfo(request);

        Object fcn = new FlowInfo.flowChunkNumber(flowChunkNumber);

        if (info.uploadedChunks.contains(fcn)) {
            log.debug("Do Get arriba");
            String message= "{\"message\":\"Uploaded\"}";
            response.getWriter().write(message);
            // This Chunk has been
            // Uploaded.
        } else {
            log.debug("Do Get something is wrong");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

        out.close();
    }

    private int getflowChunkNumber(HttpServletRequest request) {
        return HttpUtils.toInt(request.getParameter("flowChunkNumber"), -1);
    }

    private FlowInfo getFlowInfo(HttpServletRequest request)
        throws ServletException {

        String base_dir = this.tmpLocation.toFile().getAbsolutePath();

        int FlowChunkSize = HttpUtils.toInt(
            request.getParameter("flowChunkSize"), -1);

        long FlowTotalSize = HttpUtils.toLong(
            request.getParameter("flowTotalSize"), -1); //flowTotalChunks
        String FlowIdentifier = request.getParameter("flowIdentifier");
        String FlowFilename = request.getParameter("flowFilename");
        String FlowRelativePath = request.getParameter("flowRelativePath");

        // Here we add a ".temp" to every upload file to indicate NON-FINISHED
        String FlowFilePath = new File(base_dir, FlowFilename)
            .getAbsolutePath() + ".temp";

        FlowInfoStorage storage = FlowInfoStorage.getInstance();

        FlowInfo info = storage.get(FlowChunkSize, FlowTotalSize,
            FlowIdentifier, FlowFilename, FlowRelativePath, FlowFilePath);
        if (!info.valid()) {
            storage.remove(info);
            throw new ServletException("Invalid request params.");
        }

        return info;
    }

}

