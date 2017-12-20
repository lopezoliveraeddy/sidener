package electorum.sidener.service;

import electorum.sidener.domain.Archive;
import electorum.sidener.domain.enumeration.ArchiveStatus;
import electorum.sidener.repository.ArchiveRepository;
import electorum.sidener.repository.search.ArchiveSearchRepository;
import electorum.sidener.service.dto.ArchiveDTO;
import electorum.sidener.service.mapper.ArchiveMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Archive.
 */
@Service
@Transactional
public class ArchiveService {

    private final Logger log = LoggerFactory.getLogger(ArchiveService.class);

    private final ArchiveRepository archiveRepository;

    private final ArchiveMapper archiveMapper;

    private final ArchiveSearchRepository archiveSearchRepository;

    public ArchiveService(ArchiveRepository archiveRepository, ArchiveMapper archiveMapper, ArchiveSearchRepository archiveSearchRepository) {
        this.archiveRepository = archiveRepository;
        this.archiveMapper = archiveMapper;
        this.archiveSearchRepository = archiveSearchRepository;
    }

    /**
     * Save a archive.
     *
     * @param archiveDTO the entity to save
     * @return the persisted entity
     */
    public ArchiveDTO save(ArchiveDTO archiveDTO) {
        log.debug("Request to save Archive : {}", archiveDTO);
        Archive archive = archiveMapper.toEntity(archiveDTO);
        archive = archiveRepository.save(archive);
        ArchiveDTO result = archiveMapper.toDto(archive);
        archiveSearchRepository.save(archive);
        return result;
    }

    /**
     *  Get all the archives.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ArchiveDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Archives");
        return archiveRepository.findAll(pageable)
            .map(archiveMapper::toDto);
    }

    /**
     *  Get one archive by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ArchiveDTO findOne(Long id) {
        log.debug("Request to get Archive : {}", id);
        Archive archive = archiveRepository.findOne(id);
        return archiveMapper.toDto(archive);
    }

    /**
     *  Delete the  archive by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Archive : {}", id);
        archiveRepository.delete(id);
        archiveSearchRepository.delete(id);
    }

    /**
     * Search for the archive corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ArchiveDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Archives for query {}", query);
        Page<Archive> result = archiveSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(archiveMapper::toDto);
    }

    /**
     * Search for the archive corresponding to the query.
     *
     *  @param status the ArchiveStatus of the Archive
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    public Page<ArchiveDTO> findByStatus(ArchiveStatus status, Pageable pageable) {
        log.debug("Request to get all Archives by status {}", status);
        return archiveRepository.findByStatus(status, pageable)
            .map(archiveMapper::toDto);
    }

}
